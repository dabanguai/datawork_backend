package com.dbg.datawork.model.dto.dataset;

import com.dbg.datawork.infra.common.PageRequest;
import com.dbg.datawork.datasource.DatasourceConfig;
import com.dbg.datawork.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/16 15:49
 */
public class DatasetRequest extends PageRequest {

    @Data
    public static class TransformDataRequest {
        // 数据源
        private String sourceDatasourceName;
        private String sourceDatasourceType;
        private String targetDatasourceType;
        private String targetDatasourceName;
        // private DatasourceConfig sourceConfig;
        // private DatasourceConfig targetConfig;

        // 数据选择
        private String sourceTable;
        private String targetTable;
        // todo 字段映射规则
        // private Map<String, String> fieldMappings;
        // 数据过滤条件
        // private String filterCondition;
        // 增量迁移标识
        private String incrementalField;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endTime;

        // 调度与重试
        // 任务启动时间（立即执行或定时执行）
        // private Date startAt;
        // Cron 表达式（周期性任务）
        // private String cronExpression;


        public TransformDataRequest(DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest) {
            this.setSourceDatasourceName(addDataMigrationTaskRequest.getSourceName());
            this.setSourceDatasourceType(addDataMigrationTaskRequest.getSourceType());
            this.setTargetDatasourceType(addDataMigrationTaskRequest.getTargetType());
            this.setTargetDatasourceName(addDataMigrationTaskRequest.getTargetName());
            this.setSourceTable(addDataMigrationTaskRequest.getSourceTable());
            this.setTargetTable(addDataMigrationTaskRequest.getTargetTable());
            this.setIncrementalField("created_at");
            this.setStartTime(addDataMigrationTaskRequest.getStartTime());
            this.setEndTime(addDataMigrationTaskRequest.getEndTime());

        }
    }

    @Data
    public static class GetTableStructTransfer {

        private String datasourceType;

        private String TableName;
    }

    @Data
    public static class AddDataMigrationTaskRequest {

        private String taskName;

        private String taskDescription;

        private String sourceType;

        private String sourceName;

        private String sourceTable;

        private String targetType;

        private String targetName;

        private String targetTable;

        private ScheduleType scheduleType;

        private String cronExpression;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endTime;

    }
}
