package com.dbg.datawork.model.enums;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/9 8:55
 */
public enum ExecuteStatusEnum {

    SUCCESS("success", "执行成功"),
    FAILED("failed", "执行失败"),
    RUNNING("running", "正在执行"),
    WAITING("waiting", "等待执行"),
    TIMEOUT("timeout", "执行超时"),
    QUEUED("queued", "排队中"),
    CANCELED("canceled", "已取消");


    private final String type;
    private final String info;

    ExecuteStatusEnum(String type, String info) {
        this.type = type;
        this.info = info;
    }
}
