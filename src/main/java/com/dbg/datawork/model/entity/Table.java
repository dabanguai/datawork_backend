package com.dbg.datawork.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;;
import com.dbg.datawork.common.BaseEntity;
import lombok.Data;

/**
 * 数据集表
 * @TableName table
 */
@TableName(value ="table")
@Data
public class Table extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private Long datasource_id;

    /**
     * 
     */
    private Long group_id;

    /**
     * 
     */
    private String layer;

    /**
     * 名称
     */
    private String name;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private String extractType;

    /**
     * 
     */
    private Integer data_synced;

    /**
     * 是否分区
     */
    private Integer partitioned;

    /**
     * 
     */
    private String info;

    /**
     * 
     */
    private LocalDateTime last_sync_time;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}