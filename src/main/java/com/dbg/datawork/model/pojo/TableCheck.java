package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
;
import com.dbg.datawork.infra.common.BaseEntity;
import lombok.Data;

/**
 * 数据库校验
 * @TableName table_check
 */
@TableName(value ="table_check")
@Data
public class TableCheck extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 原数据库
     */
    private Long source_datasource_id;

    /**
     * 目标数据库
     */
    private Long target_datasource_id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 时间区间
     */
    private String time_range;

    /**
     * 源schema
     */
    private String source_schema;

    /**
     * 源表名
     */
    private String source_table;

    /**
     * 源表时间字段
     */
    private String source_time_field;

    /**
     * 目标schema
     */
    private String target_schema;

    /**
     * 目标表名
     */
    private String target_table;

    /**
     * 目标时间字段
     */
    private String target_time_field;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}