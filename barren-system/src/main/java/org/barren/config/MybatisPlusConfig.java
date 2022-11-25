package org.barren.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.barren.core.auth.utils.AuthUtil;
import org.barren.core.auth.utils.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis-plus 配置
 *
 * @author cxs
 **/
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 启用多租户插件
        // interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new MyTenantLineHandlerImpl()));
        return interceptor;
    }


    /**
     * 自动填充 create_user，update_user，create_time，update_time
     *
     * @author cxs
     */
    @Slf4j
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            UserInfo user = AuthUtil.getCurrentUser();
            Long userId = user.getId();
            //log.info("start insert fill ....");
            // 数据库表设计 createTime，updateTime 已经设置成了自动插入当前时间和更新当前时间
            // this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
            // this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
            // this.strictInsertFill(metaObject, "createUser", () -> userId, Long.class);
            // this.strictInsertFill(metaObject, "updateUser", () -> userId, Long.class);
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("createUser", userId);
            metaObject.setValue("updateUser", userId);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            UserInfo user = AuthUtil.getCurrentUser();
            Long userId = user.getId();
            //log.info("start update fill ....");
            // 数据库表设计 updateTime 已经设置成了自动更新当前时间
            // this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
            // this.strictUpdateFill(metaObject, "updateUser", () -> userId, Long.class); // 起始版本 3.3.3(推荐)
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("updateUser", userId);
        }
    }
}
