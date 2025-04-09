package com.dbg.datawork.infra.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/26 16:39
 */
// @Configuration
// public class PostgresDataSourceConfig {
//     @Bean(name = "postgresqlDataSource")
//     @ConfigurationProperties(prefix = "spring.datasource.dynamic.postgresql")
//     public DataSource postgresDataSource() {
//         return DataSourceBuilder.create().build();
//     }
//
//     @Bean(name = "postgresJdbcTemplate")
//     public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresqlDataSource") DataSource dataSource) {
//         return new JdbcTemplate(dataSource);
//     }
// }
