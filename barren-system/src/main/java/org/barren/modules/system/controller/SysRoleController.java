package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysRole;
import org.barren.modules.system.service.ISysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "系统角色表", tags = "系统角色表接口")
@RequestMapping("/system/sysRole")
@AllArgsConstructor
public class SysRoleController {
    private final ISysRoleService sysRoleService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysRole> detail(@RequestParam Long id) {
        SysRole result = sysRoleService.getById(id);
        return R.ok(result);
    }

    @GetMapping("list")
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public R<List<SysRole>> list(SysRole query) {
        return R.ok(sysRoleService.list(Wrappers.query(query)));
    }

    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysRole>> page(Page page, SysRole query) {
        return R.ok(sysRoleService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    @PreAuthorize("@el.check('role:add') or hasRole('admin')")
    public R save(@RequestBody SysRole param) {
        sysRoleService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    @PreAuthorize("@el.check('role:update') or hasRole('admin')")
    public R update(@RequestBody SysRole param) {
        sysRoleService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    @PreAuthorize("@el.check('role:delete') or hasRole('admin')")
    public R delete(@RequestBody List<Long> idList) {
        sysRoleService.removeByIds(idList);
        return R.ok();
    }
}
