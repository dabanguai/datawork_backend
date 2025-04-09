package com.dbg.datawork.model.dto.datasource;

import lombok.Data;

import java.util.List;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 2:49
 */
@Data
public class TableSyncTaskRequest {

    private Long originDatasourceId;

    private Long targetDatasourceId;

    private List<String> originTableNameList;

    private String targetTableName;

    private String taskName;

}
