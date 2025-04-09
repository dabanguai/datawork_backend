package com.dbg.datawork.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/**
 *
 * @TableName table_struct_transfer_task
 */
@TableName(value ="table_struct_transfer_task")
@Data
public class TableStructTransferTask implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 原数据库id

     */
    private Long originalDatabaseId;

    /**
     * 原数据库名称

     */
    private String originalDatabaseName;

    /**
     * 原表名
     */
    private String originalTableName;

    /**
     * 目标数据库id

     */
    private Long targetDatabaseId;

    /**
     * 目标数据库名称

     */
    private String targetDatabaseName;

    /**
     * 目标表所属schema

     */
    private String schema;

    /**
     * 目标表名
     */
    private String targetTableName;

    /**
     * 生成的DLL
     */
    private String targetDDL;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}