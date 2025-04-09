package com.dbg.datawork.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15968
 * @version 1.0
 * @description: 存储数据量校验结果
 * @date 2025/4/8 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAmountCompareResult {

    private String sourceTableName;

    private String targetTableName;

    private Long sourceDataAmount;

    private Long targetDataAmount;

    private Boolean isConsistent;

    private Long difference;

}
