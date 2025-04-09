package com.dbg.datawork.dataset.service.impl;

import com.dbg.datawork.dataset.service.DatasetService;
import com.dbg.datawork.infra.database.DDLDefinitions;
import com.dbg.datawork.infra.database.DynamicDataSourceRegisterHelper;
import com.dbg.datawork.model.dto.dataset.DatasetRequest;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.enums.ExecuteStatusEnum;
import com.dbg.datawork.model.enums.ScheduleType;
import com.dbg.datawork.model.pojo.DataMigrationTask;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.service.DataMigrationTaskService;
import com.dbg.datawork.service.impl.DatasourceServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
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
    private DynamicDataSourceRegisterHelper dynamicHelper;

    @Override
    public void transformData(DatasetRequest.TransformDataRequest transformDataRequest) throws SQLException {
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
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    insertStmt.setObject(i, resultSet.getObject(i));
                }
                insertStmt.addBatch();

                if (++count % batchSize == 0) {
                    insertStmt.executeBatch();
                }
            }
            insertStmt.executeBatch();
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
    public DataMigrationTask addDataMigrationTask(DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest) {
        DataMigrationTask dataMigrationTask = null;
        try {
            dataMigrationTask = dataMigrationTaskService.saveTask(addDataMigrationTaskRequest);

            DatasetRequest.TransformDataRequest transformDataRequest = new DatasetRequest.TransformDataRequest(addDataMigrationTaskRequest);
            if (ScheduleType.NOW.equals(addDataMigrationTaskRequest.getScheduleType())) {
                transformData(transformDataRequest);
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
            return DDLDefinitions.SELECT_DATA_LIMIT_1.replace("{source_table}", sourceTable);
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
