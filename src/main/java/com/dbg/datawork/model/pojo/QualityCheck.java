package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.dbg.datawork.infra.common.BaseEntity;
import lombok.Data;

/**
 * 数据校验
 * @TableName quality_check
 */
@TableName(value ="quality_check")
@Data
public class QualityCheck extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 源数据库id
     */
    private Long source_datasource_id;

    /**
     * 目标数据库id
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
     * 类型:DDL(DDL校验),ROWS(行数校验)
     */
    private String type;

    /**
     * 校验规则JSON
     */
    private String rule;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}