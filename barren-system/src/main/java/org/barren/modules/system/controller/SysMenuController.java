package org.barren.modules.system.controller;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.barren.modules.system.vo.MenuVo;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("treeList")
    @ApiOperation(value = "查询菜单数据的tree列表", notes = "查询菜单数据的tree列表")
    public R<List<SysMenu>> treeList(SysMenu query) {
        LambdaQueryWrapper<SysMenu> wrapper;
        if (StringUtils.isNotBlank(query.getTitle())) {
            wrapper = Wrappers.<SysMenu>lambdaQuery().like(SysMenu::getTitle, query.getTitle());
        } else {
            wrapper = null;
        }
        List<SysMenu> menuList = menuService.list(wrapper);
        if (CollectionUtils.isEmpty(menuList)) {
            return R.ok();
        }
        List<SysMenu> menuTrees = ISysMenuService.buildTree(menuList);
        return R.ok(menuTrees);
    }

    @GetMapping("lazyTreeList")
    @ApiOperation(value = "查询目录菜单数据的tree列表", notes = "查询目录菜单数据的tree列表，提供给前端 树型选择框 的数据")
    public R<List<SysMenu>> lazyTreeList(@RequestParam Long pid) {
        List<SysMenu> list;
        if (pid != null && !pid.equals(0L)) {
            list = menuService.list(Wrappers.<SysMenu>lambdaQuery()
                    .eq(SysMenu::getPid, pid)
                    .orderByAsc(SysMenu::getSort));
        } else {
            list = menuService.list(Wrappers.<SysMenu>lambdaQuery()
                    .isNull(SysMenu::getPid)
                    .orderByAsc(SysMenu::getSort));
        }

        // LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery()
        //         .ne(SysMenu::getType, 2);// 不查询按钮
        // List<SysMenu> menuList = menuService.list(wrapper);
        // buildTree(menuList);
        return R.ok(list);
    }

    @GetMapping("allTreeList")
    @ApiOperation(value = "查询所有菜单数据的tree列表", notes = "查询所有菜单数据的tree列表")
    public R<List<Tree<Long>>> allTreeList() {
        //LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery();// 不查询按钮
        List<SysMenu> menuList = menuService.list();
        List<Tree<Long>> trees = buildTree(menuList);
        return R.ok(trees);
    }


    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysMenu> detail(@RequestParam Long id) {
        SysMenu result = menuService.getById(id);
        return R.ok(result);
    }

    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysMenu>> page(Page page, SysMenu query) {
        return R.ok(menuService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysMenu param) {
        if (param.getPid() == 0) {
            param.setPid(null);
        }
        menuService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysMenu param) {
        if (param.getPid() == 0) {
            param.setPid(null);
        }
        menuService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList) {
        menuService.removeByIds(idList);
        return R.ok();
    }

    /**
     * 构建 tree 列表
     *
     * @param menuList
     * @return
     */
    public static List<Tree<Long>> buildTree(List<SysMenu> menuList) {


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
            tree.putExtra("label", treeNode.getTitle());
            tree.putExtra("value", treeNode.getId());
            // 1级目录需要加斜杠，不然会报警告
            // tree.putExtra("path", treeNode.getPid() == null ? "/" + treeNode.getPath() : treeNode.getPath());
            // tree.putExtra("hidden", treeNode.getHidden());
            //
            // if (treeNode.getPid() == null) {// 1级菜单，目录处理
            //     tree.putExtra("component", StringUtils.isEmpty(treeNode.getComponent()) ? "Layout" : treeNode.getComponent());
            // } else if (treeNode.getType() == 0) {  // 如果不是1级菜单，并且菜单类型为目录，则代表是多级菜单
            //     tree.putExtra("component", StringUtils.isEmpty(treeNode.getComponent()) ? "ParentView" : treeNode.getComponent());
            // } else if (StringUtils.isNotBlank(treeNode.getComponent())) {
            //     tree.putExtra("component", treeNode.getComponent());
            // }
            // // // 如果不是外链
            // // if (treeNode.getIFrame()) {
            // //
            // // }
            // tree.putExtra("meta", new MenuMetaVo(treeNode.getTitle(), treeNode.getIcon(), !treeNode.getCache()));
            // tree.putExtra("hidden", treeNode.getHidden());
        });
        return list;
    }

}
