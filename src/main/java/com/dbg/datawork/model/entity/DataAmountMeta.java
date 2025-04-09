package com.dbg.datawork.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/8 11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAmountMeta {
    private String tableName;
    private Long amount;
}
