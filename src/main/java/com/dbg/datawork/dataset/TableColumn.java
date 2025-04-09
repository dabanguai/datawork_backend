package com.dbg.datawork.dataset;

import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 13:31
 */
@Data
@AllArgsConstructor
public class TableColumn {

    private String name;

    private String columnType;

    private Integer length;

    private Boolean isNullable;

    private Integer numericPrecision;

    private Integer numericScale;

    private Integer position;

    private Boolean keyFlag;

    private String Comment;

    private String defaultValue;


    public void setNumericPrecision(Integer numericPrecision) {
        this.numericPrecision = ObjUtil.isNull(numericPrecision) ? 2 : numericPrecision;
    }
}
