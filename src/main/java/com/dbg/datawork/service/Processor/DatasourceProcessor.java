package com.dbg.datawork.service.Processor;

import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.ColumnMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

// todo 原来做表结构转换的时候，针对获取列信息的方法是在JDBC工厂中直接写定方法的，可以后期把这块和工厂做合并，工厂写一个抽象类，这个类去实现
@Service
public interface DatasourceProcessor {
    /**
     * 获取表列元数据
     */
    List<ColumnMeta> getTableColumns(String tableName, JdbcTemplate jdbcTemplate);

    Long getTableData(String tableName, JdbcTemplate jdbcTemplate);

    /**
     * 类型兼容性映射（如 MySQL INT → PostgreSQL INT4）
     */
    Map<String, Set<String>> getTypeMappings();

    /**
     * 支持的数据库类型标识（如 "mysql", "postgresql"）
     */
    String getSupportedDataSourceType();

    String parseBaseType(String type);

    Integer parseLength(String type);

    DataSource buildDataSource(DatasourceRequest.GetConnection config) throws JsonProcessingException; // 这里返回连接池 DataSource
}
