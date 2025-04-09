package com.dbg.datawork.infra.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 0:09
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdBy", () -> "system", String.class);
        this.strictInsertFill(metaObject, "updatedBy", () -> "system", String.class);
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "deleted", () -> 0, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 默认值填充
        this.strictUpdateFill(metaObject, "updatedBy", () -> "system", String.class);
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }

}