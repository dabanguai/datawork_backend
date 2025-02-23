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
 * 表SQL日志
 * @TableName table_sql_log
 */
@TableName(value ="table_sql_log")
@Data
public class TableSqlLog extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 行为
     */
    private String action;

    /**
     * 
     */
    private String before;

    /**
     * 操作后数据
     */
    private String after;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}