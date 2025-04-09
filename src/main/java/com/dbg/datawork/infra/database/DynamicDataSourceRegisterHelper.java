package com.dbg.datawork.infra.database;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.dbg.datawork.datasource.MysqlConfiguration;
import com.dbg.datawork.datasource.PgConfiguration;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DynamicDataSourceRegisterHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource(name = "hikariDataSourceCreator")
    private DataSourceCreator dataSourceCreator;

    @Resource
    private DataSource dynamicRoutingDataSource;

    private final ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<>();

    public DataSource registerAndGet(DatasourceRequest.GetConnection config) {
        String key = config.getType() + "::" + config.getName();
        return cache.computeIfAbsent(key, k -> {
            try {
                DataSourceProperty property = buildProperty(config);
                DataSource ds = dataSourceCreator.createDataSource(property);
                ((DynamicRoutingDataSource) dynamicRoutingDataSource).addDataSource(key, ds);
                return ds;
            } catch (Exception e) {
                throw new RuntimeException("动态数据源注册失败: " + e.getMessage());
            }
        });
    }

    private DataSourceProperty buildProperty(DatasourceRequest.GetConnection config) throws Exception {
        DataSourceProperty prop = new DataSourceProperty();
        switch (config.getType().toLowerCase()) {
            case "mysql":
                MysqlConfiguration mysql = objectMapper.readValue(config.getConfiguration(), MysqlConfiguration.class);
                prop.setUrl(mysql.spliceUrl());
                prop.setUsername(mysql.getUserName());
                prop.setPassword(mysql.getPassword());
                prop.setDriverClassName(mysql.getDriver());
                break;
            case "postgresql":
                PgConfiguration pg = objectMapper.readValue(config.getConfiguration(), PgConfiguration.class);
                prop.setUrl(pg.spliceUrl());
                prop.setUsername(pg.getUserName());
                prop.setPassword(pg.getPassword());
                prop.setDriverClassName(pg.getDriver());
                break;
        }
        return prop;
    }
}
