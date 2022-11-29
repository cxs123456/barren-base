package org.barren.core.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.barren.core.auth.utils.AuthUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author cxs
 */
@Slf4j
public class TenantLineHandlerImpl implements TenantLineHandler {
    /**
     * 默认租户ID
     */
    public static final Long DEFAULT_TENANT_ID = 1L;

    public static String tenantIdColumn = "tenant_id";

    /**
     * 不进行租户处理的table
     */
    public static List<String> IGNORE_TABLES = Arrays.asList("sys_tenant");

    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    @Override
    public Expression getTenantId() {
        return new LongValue(AuthUtil.getCurrentUser().getTenantId());
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名叫: tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return IGNORE_TABLES.contains(tableName);
    }

}
