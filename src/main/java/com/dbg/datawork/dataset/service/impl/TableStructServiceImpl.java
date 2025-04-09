package com.dbg.datawork.dataset.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.dataset.service.TableStructService;
import com.dbg.datawork.datasource.JdbcProvider;
import com.dbg.datawork.exception.BusinessException;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.TableStructTransferTask;
import com.dbg.datawork.service.TableStructTransferTaskService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 16:34
 */
@Service
@DS("mysql")
public class TableStructServiceImpl implements TableStructService {

    @Resource
    private JdbcProvider jdbcProvider;
    @Resource
    private JdbcTemplate mysqlJdbcTemplate;
    @Resource
    private JdbcTemplate postgresJdbcTemplate;

    @Resource
    private TableStructTransferTaskService tableStructTransferTaskService;

    @Override
    public String getTableDDL(DatasourceRequest.GetSourceDDL datasourceRequest) {
        String mysqlDDL = null;
        try {
            mysqlDDL = jdbcProvider.getMysqlDDL(datasourceRequest);
        } catch (SQLException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        return mysqlDDL;
    }


    @Override
    public String transferTable(DatasourceRequest.GetTransferDDL transferDDLRequest) {
        String targetDDL = jdbcProvider.transferTable(transferDDLRequest);
        return targetDDL;
    }

    @Override
    @DS("mysql")
    public void createTable(DatasourceRequest.UseDDL createRequest) {
        try {
            jdbcProvider.generateTableInPg(createRequest);
        } catch (SQLException e) {
            // todo 自定义
            throw new RuntimeException(e);
        }
    }

    @Override
    public TableStructTransferTask addTask(DatasourceRequest.AddTaskRequest addTaskRequest) {
        TableStructTransferTask task = tableStructTransferTaskService.addTask(addTaskRequest);
        switch (addTaskRequest.getScheduleType()) {
            case NOW -> {
                createTable(new DatasourceRequest.UseDDL(addTaskRequest.getTargetDatabaseType(), addTaskRequest.getTargetDatabaseName(), addTaskRequest.getTargetDDL()));
                break;
            }
            case LATER -> {
                break;
            }
            case CRON -> {
            }
            default -> {throw new IllegalArgumentException("添加表结构同步任务失败");}
        }
        return task;
    }

    /**
     * 迁移指定表的数据
     *
     * @param tableName 表名
     */
    public void migrateTable(String tableName) {
        // 1. 查询 MySQL 表的列信息
        List<String> columns = jdbcProvider.getTableColumns(tableName, mysqlJdbcTemplate);

        // 2. 构造 SQL 查询语句
        String selectSql = jdbcProvider.buildSelectSql(tableName);
        String insertSql = jdbcProvider.buildInsertSql(tableName, columns);

        // 3. 从 MySQL 查询数据
        List<Map<String, Object>> rows = mysqlJdbcTemplate.queryForList(selectSql);

        // 4. 将数据插入到 PostgreSQL
        for (Map<String, Object> row : rows) {
            List<Object> values = new ArrayList<>();
            for (String column : columns) {
                values.add(row.get(column));
            }
            postgresJdbcTemplate.update(insertSql, values.toArray());
        }
    }



}
