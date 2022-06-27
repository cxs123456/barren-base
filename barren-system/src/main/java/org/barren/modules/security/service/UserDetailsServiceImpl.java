package org.barren.modules.security.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.barren.core.auth.utils.UserJwt;
import org.barren.modules.system.entity.SysMenu;
import org.barren.modules.system.entity.SysRoleMenu;
import org.barren.modules.system.entity.SysUser;
import org.barren.modules.system.entity.SysUserRole;
import org.barren.modules.system.service.*;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 自定义用户认证详情
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ISysUserService userService;
    private final ISysUserRoleService userRoleService;
    private final ISysRoleService roleService;
    private final ISysRoleMenuService roleMenuService;
    private final ISysMenuService menuService;

    /**
     * 自定义授权认证
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 静态指定密码，以后改成通过name到数据库取
        //String pwd = new BCryptPasswordEncoder().encode("123456");
        SysUser user = userService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (user == null) {
            return null;
        }
        String pwd = user.getPassword();
        List<String> permissions = new ArrayList<>();
        if (user.getIsAdmin()) {
            permissions.add("admin");
        } else {
            // 查询角色 和 菜单 权限
            Long userId = user.getId();
            List<SysUserRole> userRoleList = userRoleService.list(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
            if (CollectionUtils.isNotEmpty(userRoleList)) {
                List<Long> roleIds = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                // 2.查询 角色菜单列表，获取菜单
                List<SysRoleMenu> roleMenuList = roleMenuService.list(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds));
                if (CollectionUtils.isNotEmpty(roleMenuList)) {
                    List<Long> menuIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                    // 查询目录，菜单，按钮
                    List<SysMenu> menuList = menuService.list((Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getId, menuIds)));
                    permissions = menuList.stream().map(SysMenu::getPermission).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                }
            }
        }
        //创建User对象
        //String permissions = "admin,user";
        UserJwt userDetails = new UserJwt(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(permissions, ",")));
        userDetails.setNickname(user.getNickName());
        userDetails.setId(user.getId());
        userDetails.setPhone(user.getPhone());
        userDetails.setRoles(permissions);
        return userDetails;
    }
}
