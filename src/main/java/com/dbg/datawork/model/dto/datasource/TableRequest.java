package com.dbg.datawork.model.dto.datasource;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/30 10:59
 */
@Data
public class TableRequest {

    @Data
    public static class TableRequestDto {
        private String dataSourceName;
        private String datasourceType;
        private String tableName;
        private String remark;
    }

}
