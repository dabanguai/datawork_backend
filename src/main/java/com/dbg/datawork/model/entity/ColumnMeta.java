package com.dbg.datawork.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15968
 * @version 1.0
 * @description: 列元信息，用于做数据校验
 * @date 2025/4/2 8:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMeta {
    private String name;
    private String type;  // 统一的基础类型（如 INT, VARCHAR）
    private String nativeType; // 数据库原生类型（如 INT4, VARCHAR(255)）
    private Integer length;
    private Boolean nullable;
}

