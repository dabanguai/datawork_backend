package com.dbg.datawork.dataset.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.dbg.datawork.dataset.service.DatasetService;
import com.dbg.datawork.infra.database.DDLDefinitions;
import com.dbg.datawork.infra.database.DynamicDataSourceRegisterHelper;
import com.dbg.datawork.model.dto.dataset.DatasetRequest;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.enums.ExecuteStatusEnum;
import com.dbg.datawork.model.enums.ScheduleType;
import com.dbg.datawork.model.pojo.DataMigrationTask;
import com.dbg.datawork.model.pojo.DataMigrationTaskLog;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.service.DataMigrationTaskLogService;
import com.dbg.datawork.service.DataMigrationTaskService;
import com.dbg.datawork.service.impl.DatasourceServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/17 18:42
 */
@Service
public class DatasetServiceImpl implements DatasetService {

    @Resource
    private DatasourceServiceImpl datasourceService;

    @Resource
    private DataMigrationTaskService dataMigrationTaskService;

    @Resource
    private DataMigrationTaskLogService dataMigrationTaskLogService;

    @Resource
    private DynamicDataSourceRegisterHelper dynamicHelper;

    @Override
    public void transformData(Long taskId) throws SQLException {

        DataMigrationTask migrationTaskServiceById = dataMigrationTaskService.getById(taskId);
        DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest = new DatasetRequest.AddDataMigrationTaskRequest();
        BeanUtil.copyProperties(migrationTaskServiceById, addDataMigrationTaskRequest);
        DatasetRequest.TransformDataRequest transformDataRequest = new DatasetRequest.TransformDataRequest(addDataMigrationTaskRequest);
        // 1. 获取原表数据
        // 1.1 数据源连接
        ResultSet resultSet = null;
        Statement selectStmt = null;
        Connection conn = null;
        PreparedStatement insertStmt = null;
        Connection targetConn = null;
        try {
            Datasource sourceDatasource = datasourceService.getByInfo(transformDataRequest.getSourceDatasourceType(), transformDataRequest.getSourceDatasourceName());
            DatasourceRequest.GetConnection sourceCondition = new DatasourceRequest.GetConnection(sourceDatasource.getType(), sourceDatasource.getName(), sourceDatasource.getConfiguration());
            DataSource ds = dynamicHelper.registerAndGet(sourceCondition);
            conn = ds.getConnection();
            // 1.2 获取源表元数据
            // 获取原表数据
            // 拼接数据范围
            String selectColumnSql = getScopeDDL(transformDataRequest);
            selectStmt = conn.createStatement();
            resultSet = selectStmt.executeQuery(selectColumnSql);
            ResultSetMetaData sourceMetaData = resultSet.getMetaData();
            int columnCount = sourceMetaData.getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(sourceMetaData.getColumnLabel(i));
            }

            // 2. 生成插入语句
            String insertSql = DDLDefinitions.INSERT_DATA_CONDITION
                    .replace("{target_table}", transformDataRequest.getTargetTable())
                    .replace("{column}", String.join(",", columns))
                    .replace("{values}", String.join(",", Collections.nCopies(columnCount, "?")));


            // 3. 数据插入
            // 3.1 连接目标数据库
            Datasource targetDatasource = datasourceService.getByInfo(transformDataRequest.getTargetDatasourceType(), transformDataRequest.getTargetDatasourceName());
            DatasourceRequest.GetConnection targetCondition = new DatasourceRequest.GetConnection(targetDatasource.getType(), targetDatasource.getName(), targetDatasource.getConfiguration());

            DataSource targetDs = dynamicHelper.registerAndGet(targetCondition);
            targetConn = targetDs.getConnection();
            insertStmt = targetConn.prepareStatement(insertSql);

            // 4. 批量插入数据
            int batchSize = 1000, count = 0;
            int dataAmount = resultSet.getRow();

            DataMigrationTaskLog dataMigrationTaskLog = new DataMigrationTaskLog();
            // 若 数据量小于1000 直接记录

            LocalDateTime startTime = LocalDateTime.now();
            if (dataAmount != 0 && dataAmount < 1000) {

                dataMigrationTaskLog.setBatchCount(dataAmount);
                dataMigrationTaskLog.setStatus(ExecuteStatusEnum.RUNNING.name());
                dataMigrationTaskLog.setStartTime(startTime);
                dataMigrationTaskLog.setTaskId(taskId);
                dataMigrationTaskLogService.save(dataMigrationTaskLog);

                try {
                    insertStmt.executeBatch();
                    LocalDateTime endTime = LocalDateTime.now();
                    dataMigrationTaskLog.setRecordsProcessed(100L);
                    dataMigrationTaskLog.setEndTime(LocalDateTime.now());
                    dataMigrationTaskLog.setDuration(Duration.ofSeconds(endTime.toEpochSecond(ZoneOffset.UTC), startTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                    dataMigrationTaskLog.setStatus(ExecuteStatusEnum.SUCCESS.name());
                    dataMigrationTaskLogService.updateById(dataMigrationTaskLog);

                } catch (Exception e) {
                    LocalDateTime failTime = LocalDateTime.now();
                    dataMigrationTaskLog.setStatus(ExecuteStatusEnum.FAILED.name());
                    dataMigrationTaskLog.setEndTime(failTime);
                    dataMigrationTaskLog.setDuration(Duration.ofSeconds(failTime.toEpochSecond(ZoneOffset.UTC), startTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                    dataMigrationTaskLog.setErrorMessage(e.getMessage());
                    dataMigrationTaskLogService.updateById(dataMigrationTaskLog);
                    throw new RuntimeException(e);
                }
            }

            if (dataAmount >= 1000) {
                while (resultSet.next()) {

                    for (int i = 1; i <= columnCount; i++) {
                        insertStmt.setObject(i, resultSet.getObject(i));
                    }
                    insertStmt.addBatch();

                    // 执行详情记录
                    if (++count % batchSize == 0) {
                        DataMigrationTaskLog batchDataMigrationTaskLog = new DataMigrationTaskLog();
                        // 若 数据量小于1000 直接记录
                        LocalDateTime batchStartTime = LocalDateTime.now();

                        batchDataMigrationTaskLog.setBatchCount(dataAmount);
                        batchDataMigrationTaskLog.setStatus(ExecuteStatusEnum.RUNNING.name());
                        batchDataMigrationTaskLog.setStartTime(batchStartTime);
                        batchDataMigrationTaskLog.setTaskId(taskId);
                        dataMigrationTaskLogService.save(batchDataMigrationTaskLog);

                        try {

                            insertStmt.executeBatch();
                            LocalDateTime batchEndTime = LocalDateTime.now();
                            batchDataMigrationTaskLog.setRecordsProcessed(100L);
                            batchDataMigrationTaskLog.setEndTime(batchEndTime);
                            batchDataMigrationTaskLog.setDuration(Duration.ofSeconds(batchEndTime.toEpochSecond(ZoneOffset.UTC), batchStartTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                            batchDataMigrationTaskLog.setStatus(ExecuteStatusEnum.SUCCESS.name());
                            dataMigrationTaskLogService.updateById(batchDataMigrationTaskLog);
                        } catch (Exception e) {
                            LocalDateTime failTime = LocalDateTime.now();
                            dataMigrationTaskLog.setStatus(ExecuteStatusEnum.FAILED.name());
                            dataMigrationTaskLog.setEndTime(failTime);
                            dataMigrationTaskLog.setDuration(Duration.ofSeconds(failTime.toEpochSecond(ZoneOffset.UTC), startTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                            dataMigrationTaskLog.setErrorMessage(e.getMessage());
                            dataMigrationTaskLogService.updateById(dataMigrationTaskLog);
                            throw new RuntimeException(e);
                        }
                    }
                }

                DataMigrationTaskLog lastBatchDataMigrationTaskLog = new DataMigrationTaskLog();
                // 若 数据量小于1000 直接记录
                LocalDateTime batchStartTime = LocalDateTime.now();

                lastBatchDataMigrationTaskLog.setBatchCount(dataAmount);
                lastBatchDataMigrationTaskLog.setStatus(ExecuteStatusEnum.RUNNING.name());
                lastBatchDataMigrationTaskLog.setStartTime(batchStartTime);
                lastBatchDataMigrationTaskLog.setTaskId(taskId);
                dataMigrationTaskLogService.save(lastBatchDataMigrationTaskLog);
                try {
                    insertStmt.executeBatch();
                    insertStmt.executeBatch();
                    LocalDateTime batchEndTime = LocalDateTime.now();
                    lastBatchDataMigrationTaskLog.setRecordsProcessed(100L);
                    lastBatchDataMigrationTaskLog.setEndTime(batchEndTime);
                    lastBatchDataMigrationTaskLog.setDuration(Duration.ofSeconds(batchEndTime.toEpochSecond(ZoneOffset.UTC), batchStartTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                    lastBatchDataMigrationTaskLog.setStatus(ExecuteStatusEnum.SUCCESS.name());
                    dataMigrationTaskLogService.updateById(lastBatchDataMigrationTaskLog);
                } catch (Exception e) {
                    LocalDateTime failTime = LocalDateTime.now();
                    lastBatchDataMigrationTaskLog.setStatus(ExecuteStatusEnum.FAILED.name());
                    lastBatchDataMigrationTaskLog.setEndTime(failTime);
                    lastBatchDataMigrationTaskLog.setDuration(Duration.ofSeconds(failTime.toEpochSecond(ZoneOffset.UTC), startTime.toEpochSecond(ZoneOffset.UTC)).getSeconds());
                    lastBatchDataMigrationTaskLog.setErrorMessage(e.getMessage());
                    dataMigrationTaskLogService.updateById(lastBatchDataMigrationTaskLog);
                    throw new RuntimeException(e);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 5. 关闭资源
            resultSet.close();
            selectStmt.close();
            conn.close();
            insertStmt.close();
            targetConn.close();
        }

    }


    @Override
    @DS("mysql")
    public DataMigrationTask addDataMigrationTask(DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest) {
        DataMigrationTask dataMigrationTask = null;
        try {
            dataMigrationTask = dataMigrationTaskService.saveTask(addDataMigrationTaskRequest);

            DatasetRequest.TransformDataRequest transformDataRequest = new DatasetRequest.TransformDataRequest(addDataMigrationTaskRequest);
            if (ScheduleType.NOW.equals(addDataMigrationTaskRequest.getScheduleType())) {
                transformData(dataMigrationTask.getId());
            }
            return dataMigrationTask;
        } catch (Exception e) {
            dataMigrationTask.setStatus(ExecuteStatusEnum.FAILED.name());
            dataMigrationTask.setLastErrorMessage(e.getMessage());
            dataMigrationTaskService.updateById(dataMigrationTask);
            throw new RuntimeException(e);
        }
    }

    private String getScopeDDL(DatasetRequest.TransformDataRequest request) {

        final String sourceTable = request.getSourceTable();
        final String targetTable = request.getTargetTable();
        final LocalDateTime startTime = request.getStartTime();
        final LocalDateTime endTime = request.getEndTime();

        // 构建时间条件表达式
        final String condition = buildTimeCondition(startTime, endTime);

        if (request.getEndTime() == null && request.getStartTime() == null) {
            return String.format(DDLDefinitions.SELECT_DATA_LIMIT_1, targetTable);
        }

        // 使用预编译占位符替换
        return String.format(DDLDefinitions.SELECT_DATA_CONDITION, targetTable, condition);

    }

    /**
     * 构建时间条件表达式
     */
    private String buildTimeCondition(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return String.format("created_at BETWEEN '%s' AND '%s'",
                    formatDateTime(start), formatDateTime(end));
        }
        if (start != null) {
            return String.format("created_at >= '%s'", formatDateTime(start));
        }
        return String.format("created_at <= '%s'", formatDateTime(end));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }


}
