package com.dbg.datawork.service.Processor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.dbg.datawork.datasource.MysqlConfiguration;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.ColumnMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/2 8:34
 */
@Component
@DS("mysql")
public class MysqlProcessor implements DatasourceProcessor {

    @Resource(name = "hikariDataSourceCreator")
    private DataSourceCreator dataSourceCreator;

    
    @Override
    public List<ColumnMeta> getTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(
                "SHOW COLUMNS FROM " + tableName,
                (rs, rowNum) -> new ColumnMeta(
                        rs.getString("Field"),
                        parseBaseType(rs.getString("Type")),
                        rs.getString("Type"),
                        parseLength(rs.getString("Type")),
                        rs.getString("Null").equals("YES")
                )
        );
    }

    @Override
    public Map<String, Set<String>> getTypeMappings() {
        // todo 可配置化：从数据库或 yml 文件加载
        return Map.of(
                "INT", Set.of("INT4", "INTEGER"),
                "VARCHAR", Set.of("VARCHAR", "TEXT")
        );
    }

    @Override
    public String getSupportedDataSourceType() {
        return "mysql";
    }

    @Override
    public String parseBaseType(String type) {
        if (type == null) {
            return null;
        }
        // 去除括号及后面的部分，并处理无括号的情况（如BIGINT UNSIGNED）
        String baseType = type.split("\\$")[0].split(" ")[0];
        return baseType.trim().toUpperCase(); // 统一返回大写类型名，如INT, VARCHAR
    }


    public Integer parseLength(String type) {
        if (type == null) return null;
        Matcher matcher = Pattern.compile("\\$([^)]+)\\$").matcher(type);
        if (matcher.find()) {
            String lengthPart = matcher.group(1);
            // 取第一个数字（兼容 DECIMAL(10,2) -> 10）
            String[] segments = lengthPart.split("[,\\s]+");
            try {
                return Integer.parseInt(segments[0]);
            } catch (NumberFormatException e) {
                return null; // 非数字部分直接忽略（如 ENUM('a','b')）
            }
        }
        return null;
    }

    // todo 外部传入JdbcTemplate -> 内部硬编码？
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
