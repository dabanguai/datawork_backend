package com.dbg.datawork.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/23 13:21
 */
@Data
public class TableStructTransferVo implements Serializable {

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 源库
     */
    private String originalDatabase;

    /**
     * 目标库
     */
    private String targetDatabase;

    /**
     * 表名称
     */
    private String tableName;

}
