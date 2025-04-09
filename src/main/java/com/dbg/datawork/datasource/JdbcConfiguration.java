package com.dbg.datawork.datasource;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 13:58
 */
@Data
public class JdbcConfiguration {
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String database;
    private String url;
    private String datasourceType = "jdbc";
}
