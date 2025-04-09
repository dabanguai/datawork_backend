package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;;
import com.dbg.datawork.infra.common.BaseEntity;
import lombok.Data;

/**
 * 数据库校验日志
 * @TableName table_check_log
 */
@TableName(value ="table_check_log")
@Data
public class TableCheckLog extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * tableCheck外键
     */
    private Long table_check_id;

    /**
     * tableCheck名称
     */
    private String table_check_name;

    /**
     * 状态
     */
    private String status;

    /**
     * 源表数据量
     */
    private Integer source_row;

    /**
     * 目标表数据量
     */
    private Integer target_row;

    /**
     * 时间区间
     */
    private String time_range;

    /**
     * 详细消息
     */
    private String message;

    /**
     * 源数据SQL
     */
    private String source_sql;

    /**
     * 目标数据库SQL
     */
    private String target_sql;

    /**
     * 开始时间
     */
    private LocalDateTime begin_time;

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