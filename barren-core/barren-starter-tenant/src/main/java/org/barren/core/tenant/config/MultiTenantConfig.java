package org.barren.core.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.barren.core.tenant.properties.MultiTenantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户配置
 *
 * @author cxs
 */
@Configuration
@ConditionalOnProperty(prefix = MultiTenantProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties({MultiTenantProperties.class})
public class MultiTenantConfig {
    @Autowired
    private MultiTenantProperties properties;

    @Bean
    public TenantLineHandler tenantLineHandler(MybatisPlusInterceptor mybatisPlusInterceptor) {
        TenantLineHandlerImpl tenantLineHandler = new TenantLineHandlerImpl();
        TenantLineHandlerImpl.IGNORE_TABLES = properties.getExcludeTables();
        TenantLineHandlerImpl.tenantIdColumn = properties.getColumn();
        // 启用多租户插件
        mybatisPlusInterceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));
        return tenantLineHandler;
    }

}
