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
 * 定时任务日志
 * @TableName task_log
 */
@TableName(value ="task_log")
@Data
public class TaskLog extends BaseEntity implements Serializable {
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
     * 任务id(外键)
     */
    private Long task_id;

    /**
     * 任务名称
     */
    private String task_name;

    /**
     * 
     */
    private String layer;

    /**
     * 状态
     */
    private String status;

    /**
     * 任务详细日志
     */
    private String message;

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
     * 开始时间
     */
    private LocalDateTime start_time;

    /**
     * 结束时间
     */
    private LocalDateTime end_time;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}