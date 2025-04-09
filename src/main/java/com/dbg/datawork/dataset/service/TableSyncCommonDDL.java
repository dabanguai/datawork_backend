package com.dbg.datawork.dataset.service;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.ObjUtil;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.service.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 2:59
 */
@Slf4j
@Component
public class TableSyncCommonDDL {

    private static final Map<String, String> PG_TYPE_MAP = new HashMap<>();

    static {
        PG_TYPE_MAP.put("VARCHAR", "varchar");
        PG_TYPE_MAP.put("CHAR", "char");
        PG_TYPE_MAP.put("TEXT", "text");
        PG_TYPE_MAP.put("TINYTEXT", "text");
        PG_TYPE_MAP.put("MEDIUMTEXT", "text");
        PG_TYPE_MAP.put("LONGTEXT", "text");
        PG_TYPE_MAP.put("INT", "integer");
        PG_TYPE_MAP.put("INTEGER", "integer");
        PG_TYPE_MAP.put("BIGINT", "bigint");
        PG_TYPE_MAP.put("FLOAT", "real");
        PG_TYPE_MAP.put("DOUBLE", "double precision");
        PG_TYPE_MAP.put("DECIMAL", "numeric");
        PG_TYPE_MAP.put("DATE", "date");
        PG_TYPE_MAP.put("DATETIME", "timestamp");
        PG_TYPE_MAP.put("TIMESTAMP", "timestamp");
        PG_TYPE_MAP.put("TINYINT", "smallint");
        PG_TYPE_MAP.put("SMALLINT", "smallint");
        PG_TYPE_MAP.put("MEDIUMINT", "integer");
        PG_TYPE_MAP.put("BIT", "boolean");
        PG_TYPE_MAP.put("BOOLEAN", "boolean");
        PG_TYPE_MAP.put("BLOB", "bytea");
        PG_TYPE_MAP.put("TINYBLOB", "bytea");
        PG_TYPE_MAP.put("MEDIUMBLOB", "bytea");
        PG_TYPE_MAP.put("LONGBLOB", "bytea");
    }

    @Resource
    private DatasourceService datasourceService;

    /**
     * 获取数据源
     *
     * @param datasourceId
     * @return
     */
    public Datasource getDataSource(Long datasourceId) {
        Datasource datasource = datasourceService.getById(datasourceId);
        if (ObjUtil.isNotEmpty(datasource)) {
            throw new ValidateException(ErrorCode.NOT_FOUND_ERROR.getCode(), ErrorCode.NOT_FOUND_ERROR.getMessage());
        }
        return datasource;
    }

    // public List<TableColumn> getColumnList(String tableName) {
    //     JdbcProvider jdbcProvider = ProviderFactory.getJdbcProvider();
    //     List<TableColumn> targetColumnList = new ArrayList<>();
    //
    //     jdbcProvider.getMysqlColumnList(tableName);
    // }
}
