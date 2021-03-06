package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysRoleMenu;
import org.barren.modules.system.service.ISysRoleMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关联表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "角色菜单关联表", tags = "角色菜单关联表接口")
@RequestMapping("/system/sysRoleMenu")
@AllArgsConstructor
public class SysRoleMenuController {
    private final ISysRoleMenuService sysRoleMenuService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysRoleMenu> detail(@RequestParam Long id) {
        SysRoleMenu result = sysRoleMenuService.getById(id);
        return R.ok(result);
    }

    @GetMapping("list")
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public R<List<SysRoleMenu>> list(SysRoleMenu query) {
        return R.ok(sysRoleMenuService.list(Wrappers.query(query)));
    }

    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysRoleMenu>> page(Page page, SysRoleMenu query) {
        return R.ok(sysRoleMenuService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysRoleMenu param) {
        sysRoleMenuService.save(param);
        return R.ok();
    }

    @PostMapping("saveRoleMenus")
    @ApiOperation(value = "新增角色菜单数据", notes = "新增角色菜单数据")
    public R saveRoleMenus(@RequestBody SysRoleMenu param) {
        List<Long> menuIds = param.getMenuIds();
        Long roleId = param.getRoleId();
        if (CollectionUtils.isEmpty(menuIds) || roleId == null) {
            return R.fail("参数错误");
        }
        // 1.先删除所有，2.再更新
        List<SysRoleMenu> delList = sysRoleMenuService.list(Wrappers.query(new SysRoleMenu().setRoleId(roleId)));
        sysRoleMenuService.removeBatchByIds(delList,true);

        List<SysRoleMenu> addList = new ArrayList<>();
        for (Long menuId : menuIds) {
            addList.add(new SysRoleMenu().setMenuId(menuId).setRoleId(roleId));
        }
        sysRoleMenuService.saveBatch(addList);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysRoleMenu param) {
        sysRoleMenuService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList) {
        sysRoleMenuService.removeByIds(idList);
        return R.ok();
    }
}
