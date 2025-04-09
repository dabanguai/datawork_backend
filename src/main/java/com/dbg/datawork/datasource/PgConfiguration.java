package com.dbg.datawork.datasource;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 14:11
 */
@Data
public class PgConfiguration extends JdbcConfiguration{

    private String driver = "org.postgresql.Driver";

    private String url = null;

    private String schema = "public";

    public String spliceUrl() {
        url = this.getUrl() + "/" + this.getDatabase();
        return url;
    }
}
