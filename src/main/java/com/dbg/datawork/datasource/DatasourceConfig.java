package com.dbg.datawork.datasource;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/15 23:37
 */
@Data
public class DatasourceConfig {
    private String host;
    private Integer port;
    private String url;
    private String userName;
    private String password;
}
