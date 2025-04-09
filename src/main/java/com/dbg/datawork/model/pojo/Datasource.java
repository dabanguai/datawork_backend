package com.dbg.datawork.model.pojo;

import com.dbg.datawork.infra.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 数据源
 * @TableName datasource
 */
@TableName(value ="datasource")
@Data
public class Datasource extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 数据源类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 配置信息
     */
    private String configuration;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}