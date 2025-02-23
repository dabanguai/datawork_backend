package com.dbg.datawork.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;;
import com.dbg.datawork.common.BaseEntity;
import lombok.Data;

/**
 * 表字段信息
 * @TableName table_column
 */
@TableName(value ="table_column")
@Data
public class TableColumn extends BaseEntity implements Serializable {
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
     * 
     */
    private String origin_name;

    /**
     * 名称
     */
    private String name;

    /**
     * 
     */
    private String remark;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否为分区键
     */
    private Integer partitioned;

    /**
     * 是否为主键
     */
    private Integer primary_key;

    /**
     * 
     */
    private Integer size;

    /**
     * 
     */
    private Integer column_index;

    /**
     * 
     */
    private LocalDateTime last_sync_time;

    /**
     * 
     */
    private Integer checked;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField
    private static final long serialVersionUID = 1L;
}