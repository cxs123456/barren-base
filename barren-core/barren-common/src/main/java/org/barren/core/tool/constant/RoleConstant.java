
package org.barren.core.tool.constant;

/**
 * 系统默认角色
 *
 * @author cxs
 */
public class RoleConstant {
    public static final String ADMINISTRATOR = "administrator";
    public static final String HAS_ROLE_ADMINISTRATOR = "hasRole('administrator')";
    public static final String ADMIN = "admin";
    public static final String HAS_ROLE_ADMIN = "hasAnyRole('administrator', 'admin')";
    public static final String USER = "user";
    public static final String HAS_ROLE_USER = "hasRole('user')";
    public static final String TEST = "test";
    public static final String HAS_ROLE_TEST = "hasRole('test')";

}
