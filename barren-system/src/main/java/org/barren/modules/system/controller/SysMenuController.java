package org.barren.modules.system.controller;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.barren.core.auth.utils.AuthUtil;
import org.barren.core.auth.utils.UserInfo;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysMenu;
import org.barren.modules.system.entity.SysRoleMenu;
import org.barren.modules.system.entity.SysUserRole;
import org.barren.modules.system.service.ISysMenuService;
import org.barren.modules.system.service.ISysRoleMenuService;
import org.barren.modules.system.service.ISysRoleService;
import org.barren.modules.system.service.ISysUserRoleService;
import org.barren.modules.system.vo.MenuMetaVo;
import org.barren.modules.system.vo.MenuVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/system/menu")
public class SysMenuController {

    private final ISysUserRoleService userRoleService;
    private final ISysRoleService roleService;
    private final ISysRoleMenuService roleMenuService;
    private final ISysMenuService menuService;

    @ApiOperation(value = "构建前端菜单数据", notes = "根据角色生成前端菜单数据（动态路由数据）")
    @GetMapping(value = "/build")
    public R<List<MenuVo>> buildMenus() {
        // 1.获取用户roles
        UserInfo user = AuthUtil.getCurrentUser();
        Long userId = user.getId();
        List<SysUserRole> userRoleList = userRoleService.list(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        if (CollectionUtils.isEmpty(userRoleList)) {
            return R.ok();
        }
        List<Long> roleIds = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        // 2.查询 角色菜单列表，获取菜单
        List<SysRoleMenu> roleMenuList = roleMenuService.list(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds));
        if (CollectionUtils.isEmpty(roleMenuList)) {
            return R.ok();
        }
        List<Long> menuIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        // 只查询目录和菜单，不查询按钮
        List<SysMenu> menuList = menuService.list((Wrappers.<SysMenu>lambdaQuery().in(SysMenu::getId, menuIds).ne(SysMenu::getType, 2)));
        if (CollectionUtils.isEmpty(menuList)) {
            return R.ok();
        }
        // 3.返回前端菜单数据格式
        List<SysMenu> menuTrees = ISysMenuService.buildTree(menuList);
        List<MenuVo> menuVos = ISysMenuService.buildMenus(menuTrees);
        return R.ok(menuVos);
    }

    /**
     * @param menuList
     * @return
     */
    public static List<MenuVo> buildTree(List<SysMenu> menuList) {


        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 否则都是默认值
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setWeightKey("sort");

        List<Tree<Long>> list = TreeUtil.build(menuList, null, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            tree.setWeight(treeNode.getSort());
            tree.setName(treeNode.getName());

            // 扩展属性 ... 构建 菜单路由 数据
            // 1级目录需要加斜杠，不然会报警告
            tree.putExtra("path", treeNode.getPid() == null ? "/" + treeNode.getPath() : treeNode.getPath());
            tree.putExtra("hidden", treeNode.getHidden());

            if (treeNode.getPid() == null) {// 1级菜单，目录处理
                tree.putExtra("component", StringUtils.isEmpty(treeNode.getComponent()) ? "Layout" : treeNode.getComponent());
            } else if (treeNode.getType() == 0) {  // 如果不是1级菜单，并且菜单类型为目录，则代表是多级菜单
                tree.putExtra("component", StringUtils.isEmpty(treeNode.getComponent()) ? "ParentView" : treeNode.getComponent());
            } else if (StringUtils.isNotBlank(treeNode.getComponent())) {
                tree.putExtra("component", treeNode.getComponent());
            }
            // // 如果不是外链
            // if (treeNode.getIFrame()) {
            //
            // }
            tree.putExtra("meta", new MenuMetaVo(treeNode.getTitle(), treeNode.getIcon(), !treeNode.getCache()));
            tree.putExtra("hidden", treeNode.getHidden());


        });
        return null;
    }

}
