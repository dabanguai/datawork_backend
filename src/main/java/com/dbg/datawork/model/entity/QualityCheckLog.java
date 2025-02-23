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
 * 数据校验日志
 * @TableName quality_check_log
 */
@TableName(value ="quality_check_log")
@Data
public class QualityCheckLog extends BaseEntity implements Serializable {
    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 质量校验id
     */
    private Long quality_check_id;

    /**
     * 质量校验名称
     */
    private String quality_check_name;

    /**
     * key
     */
    private String key;

    /**
     * 状态
     */
    private String status;

    /**
     * 校验信息
     */
    private String content;

    /**
     * 失败消息
     */
    private String message;



    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 质量校验类型
     */
    private String quality_check_type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}