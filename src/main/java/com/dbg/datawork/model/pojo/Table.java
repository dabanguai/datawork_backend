package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;;
import com.dbg.datawork.infra.common.BaseEntity;
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
    private Long datasourceId;

    /**
     * 
     */
    private Long groupId;

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
    private Integer dataSynced;

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
    private LocalDateTime lastSyncTime;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}