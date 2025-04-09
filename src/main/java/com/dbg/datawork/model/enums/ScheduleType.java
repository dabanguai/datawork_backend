package com.dbg.datawork.model.enums;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/8 14:38
 */
public enum ScheduleType {
    NOW("NOW", "立即执行"),
    LATER("LATER", "稍后手动执行"),
    CRON("CRON", "定时执行");

    private final String type;
    private final String info;

    ScheduleType(String type, String info) {
        this.type = type;
        this.info = info;
    }
}
