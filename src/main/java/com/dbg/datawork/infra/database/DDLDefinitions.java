package com.dbg.datawork.infra.database;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/17 19:35
 */
public class DDLDefinitions {

    public static final String SELECT_DATA_LIMIT_1 = """
            SELECT * FROM {source_table} LIMIT 1;
            """;

    public static final String SELECT_DATA_FULL = """
            SELECT * FROM {source_table} LIMIT 1;
            """;

    public static final String SELECT_DATA_CONDITION = """
            SELECT * FROM {source_table} WHERE {condition}
            """;

    public static final String INSERT_DATA_CONDITION = """
            INSERT INTO {target_table} ({column}) VALUES ({values})
            """;

}
