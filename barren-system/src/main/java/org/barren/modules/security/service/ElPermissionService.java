package org.barren.modules.security.service;

import org.apache.commons.collections4.CollectionUtils;
import org.barren.core.auth.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限 检查服务，与@PreAuthorize 配合使用
 *
 * @author cxs
 */
@Service(value = "el")
public class ElPermissionService {

    public Boolean check(String... permissions) {
        // 取当前用户的所有权限
        // TODO 以后修改权限存在Redis中，从Redis中获取
        List<String> elPermissions = AuthUtil.getCurrentUser().getAuthorities();
        if (CollectionUtils.isEmpty(elPermissions)) {
            return false;
        }
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("admin") || elPermissions.stream().anyMatch(elPermissions::contains);
    }
}
