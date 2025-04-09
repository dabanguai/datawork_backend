package com.dbg.datawork.service.Processor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.dbg.datawork.datasource.MysqlConfiguration;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.ColumnMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@DS("postgresql")
public class PostgresqlProcessor implements DatasourceProcessor {

    @Resource(name = "hikariDataSourceCreator")
    private DataSourceCreator dataSourceCreator;

    public List<ColumnMeta> getTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
        // 处理可能的 schema 限定表名（如 "public.user"）
        String schema = "public";
        String pureTableName = tableName;
        if (tableName.contains(".")) {
            String[] parts = tableName.split("\\.");
            schema = parts[0];
            pureTableName = parts[1];
        }

        return jdbcTemplate.query(
                "SELECT column_name, data_type, is_nullable, udt_name " +
                        "FROM information_schema.columns " +
                        "WHERE table_schema = ? AND table_name = ? " +
                        "ORDER BY ordinal_position",
                new Object[]{schema, pureTableName},
                (rs, rowNum) -> {
                    String type = rs.getString("data_type");
                    String fullType = rs.getString("udt_name"); // 包含更详细类型信息（如 int4, timestamptz）
                    return new ColumnMeta(
                            rs.getString("column_name"),
                            parseBaseType(type),
                            fullType.toUpperCase(), // 原始类型保留详细信息
                            parseLength(type),
                            rs.getString("is_nullable").equalsIgnoreCase("YES")
                    );
                }
        );
    }

    @Override
    public Map<String, Set<String>> getTypeMappings() {
        // PostgreSQL 类型映射（可根据需要扩展）
        return Map.of(
                "INT", Set.of("INT4", "INTEGER", "SERIAL"),
                "VARCHAR", Set.of("VARCHAR", "TEXT", "BPCHAR"),
                "NUMERIC", Set.of("NUMERIC", "DECIMAL"),
                "TIMESTAMP", Set.of("TIMESTAMP", "TIMESTAMPTZ")
        );
    }

    @Override
    public String getSupportedDataSourceType() {
        return "postgresql";
    }

    @Override
    public String parseBaseType(String type) {
        if (type == null) return null;
        // 处理类型中的括号和长度信息（如 varchar(255) -> varchar）
        String baseType = type.split("\\$")[0].split("\\s")[0];
        return baseType.trim().toUpperCase();
    }

    @Override
    public Integer parseLength(String type) {
        if (type == null) return null;
        // 匹配括号内的第一个数字（兼容多种类型，如 VARCHAR(255)、NUMERIC(10,2)）
        Matcher matcher = Pattern.compile("\\((\\d+)").matcher(type);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return null; // 非数字部分忽略
            }
        }
        return null; // 无长度信息
    }

    // 可选：处理 PostgreSQL 特有类型（如数组、范围类型）
    private String handleSpecialTypes(String type) {
        if (type.endsWith("[]")) {
            return "ARRAY";
        }
        return type;
    }

    @Override
    public Long getTableData(String tableName, JdbcTemplate jdbcTemplate) {
        String countQuery = String.format("SELECT COUNT(*) FROM %s", tableName);
        return jdbcTemplate.queryForObject(countQuery, Long.class);
    }

    @Override
    public DataSource buildDataSource(DatasourceRequest.GetConnection config) throws JsonProcessingException {
        MysqlConfiguration mysqlConfig = new ObjectMapper()
                .readValue(config.getConfiguration(), MysqlConfiguration.class);

        DataSourceProperty prop = new DataSourceProperty();
        prop.setUrl(mysqlConfig.spliceUrl());
        prop.setUsername(mysqlConfig.getUserName());
        prop.setPassword(mysqlConfig.getPassword());
        prop.setDriverClassName(mysqlConfig.getDriver());

        return dataSourceCreator.createDataSource(prop);
    }
}