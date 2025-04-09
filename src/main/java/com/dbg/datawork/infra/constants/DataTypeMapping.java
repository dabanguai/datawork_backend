package com.dbg.datawork.infra.constants;

import java.util.HashMap;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 14:33
 */
public class DataTypeMapping {
    public static final HashMap<String, String> MYSQL_TYPE_TO_POSTGRE_TYPE
            = new HashMap<String, String>();

    static {
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("bigint", "bigint");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("int", "int");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("tinyint", "smallint");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("smallint", "smallint");

        MYSQL_TYPE_TO_POSTGRE_TYPE.put("varchar", "varchar");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("char", "char");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("text", "text");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("mediumtext", "text");

        MYSQL_TYPE_TO_POSTGRE_TYPE.put("datetime", "timestamp");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("date", "date");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("time", "time");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("timestamp", "timestamp");

        MYSQL_TYPE_TO_POSTGRE_TYPE.put("decimal", "numeric");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("DOUBLE", "DOUBLE PRECISION");
        MYSQL_TYPE_TO_POSTGRE_TYPE.put("float", "real");
    }
}
