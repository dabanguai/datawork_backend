package com.dbg.datawork.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/30 11:33
 */
@Data
@AllArgsConstructor
public class DatasourceTableCountVo {
    private String datasourceName;
    private Long tableCount;
}
