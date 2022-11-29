package org.barren.core.tenant.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author cxs
 */
@Data
@ConfigurationProperties(prefix = MultiTenantProperties.PREFIX)
public class MultiTenantProperties {

    public static final String PREFIX = "tenant";

    /**
     * 是否启用多租户
     */
    private Boolean enabled;
    /**
     * 多租户对应字段
     */
    private String column;
    /**
     * 排除表
     */
    private List<String> excludeTables;

}
