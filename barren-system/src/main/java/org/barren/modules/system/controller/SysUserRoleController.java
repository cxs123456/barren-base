package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysUserRole;
import org.barren.modules.system.service.ISysUserRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "用户角色关联表", tags = "用户角色关联表接口")
@RequestMapping("/system/sysUserRole")
@AllArgsConstructor
public class SysUserRoleController {
    private final ISysUserRoleService sysUserRoleService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysUserRole> detail(@RequestParam Long id){
        SysUserRole result = sysUserRoleService.getById(id);
        return R.ok(result);
    }

    @GetMapping("page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUserRole>> page(Page page, SysUserRole query) {
        return R.ok(sysUserRoleService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysUserRole param){
        sysUserRoleService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysUserRole param){
        sysUserRoleService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList){
        sysUserRoleService.removeByIds(idList);
        return R.ok();
    }
}
