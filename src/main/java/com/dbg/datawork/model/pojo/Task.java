package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.dbg.datawork.infra.common.BaseEntity;
import lombok.Data;

/**
 * 定时任务
 * @TableName task
 */
@TableName(value ="task")
@Data
public class Task extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 表id(外键)
     */
    private Long tableId;

    /**
     * 
     */
    private String layer;

    /**
     * 
     */
    private String status;

    /**
     * 任务同步类型
     */
    private String syncType;

    /**
     * 
     */
    private String syncTimeRange;

    /**
     * 
     */
    private String syncDataRange;

    /**
     * 
     */
    private String extractType;

    /**
     * 是否为分区表
     */
    private Integer partitioned;

    /**
     * 
     */
    private String handler;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 
     */
    private String cron;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}