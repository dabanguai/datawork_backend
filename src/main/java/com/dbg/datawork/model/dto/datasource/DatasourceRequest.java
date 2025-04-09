package com.dbg.datawork.model.dto.datasource;

import com.dbg.datawork.infra.common.PageRequest;
import com.dbg.datawork.datasource.DatasourceConfig;
import com.dbg.datawork.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/23 17:18
 */
@Data
public class DatasourceRequest extends PageRequest {

    private String id;
    private String type;
    private String name;
    private String configuration;


    @JsonIgnore
    public DatasourceConfig getConfig() throws JsonProcessingException {
        if (this.configuration == null) return null;
        return new ObjectMapper().readValue(this.configuration, DatasourceConfig.class);
    }


    @Data
    public static class AddDatasourceDto {
        private String type;
        private String name;
        private String configuration;
    }

    @Data
    public static class UpdateDatasourceDto {
        private String id;
        private String type;
        private String name;
        private String configuration;
    }

    @Data
    @AllArgsConstructor
    public static class GetConnection {
        private String type;
        private String name;
        private String configuration;
    }
    @Data
    @AllArgsConstructor
    public static class GetSourceDDL {
        private String databaseType;
        private String databaseName;
        private String tableName;
    }


    @Data
    public static class GetTransferDDL {

        // 数据源
        private String originalDDL;
        private String originalType;
        private String targetType;
        private String targetDatasource;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UseDDL {
        // 数据源
        private String targetType;
        private String targetDatasource;
        private String ddl;
    }

    @Data
    public static class AddTaskRequest {
        private String originalDatabaseType;
        private String targetDatabaseType;
        private String originalDatabaseName;
        private String originalTableName;
        private String targetDatabaseName;
        private String targetTableName;
        private String targetDDL;
        private String schema;
        private ScheduleType scheduleType;

    }
}
