package com.dbg.datawork.infra.database;

import org.springframework.stereotype.Component;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/17 19:33
 */
@Component
public class DdlManager {

    // private final JdbcTemplate jdbcTemplate;

    // 通过依赖注入数据库连接
    // public DdlManager(JdbcTemplate jdbcTemplate) {
    //     this.jdbcTemplate = jdbcTemplate;
    // }

    // // 执行建表语句
    // public void initializeDatabase() {
    //     jdbcTemplate.execute(DDLDefinitions.CREATE_USER_TABLE);
    //     jdbcTemplate.execute(DDLDefinitions.CREATE_ORDER_TABLE);
    // }

}
