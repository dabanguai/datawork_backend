package com.dbg.datawork.datasource;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 13:54
 */
@Data
public class MysqlConfiguration extends JdbcConfiguration {
    // todo 抽取连接
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String params = "?serverTimezone=GMT%2B8&useOldAliasMetadataBehavior=true";

    private String url = null;

    public String spliceUrl() {
        url = this.getUrl() + "/" + this.getDatabase() + params;
        return url;
    }
}
