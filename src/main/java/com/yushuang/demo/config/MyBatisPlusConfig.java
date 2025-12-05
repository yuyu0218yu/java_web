package com.yushuang.demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 字段自动填充处理器
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Component
@Slf4j
public class MyBatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        LocalDateTime now = LocalDateTime.now();
        
        // 填充创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        
        // 填充逻辑删除字段默认值
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        
        // 填充状态默认值（如果实体有status字段且为Integer类型）
        if (metaObject.hasGetter("status") && metaObject.getSetterType("status").equals(Integer.class)) {
            this.strictInsertFill(metaObject, "status", Integer.class, 1);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
