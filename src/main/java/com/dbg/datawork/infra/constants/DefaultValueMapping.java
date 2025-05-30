package com.dbg.datawork.infra.constants;

import lombok.Data;

import java.util.HashMap;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/25 0:01
 */
@Data
public class DefaultValueMapping {

    public static final HashMap<String, String> MYSQL_DEFAULT_TO_POSTGRE_DEFAULT
            = new HashMap<String, String>();

    static {
        MYSQL_DEFAULT_TO_POSTGRE_DEFAULT.put("NULL", "NULL");
        MYSQL_DEFAULT_TO_POSTGRE_DEFAULT.put("CURRENT_TIMESTAMP", "CURRENT_TIMESTAMP");
        MYSQL_DEFAULT_TO_POSTGRE_DEFAULT.put("CURRENT_DATE", "CURRENT_DATE");
        MYSQL_DEFAULT_TO_POSTGRE_DEFAULT.put("CURRENT_TIME", "CURRENT_TIME");
    }

}
