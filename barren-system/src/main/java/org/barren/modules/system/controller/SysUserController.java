package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysUser;
import org.barren.modules.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "系统用户表", tags = "系统用户表接口")
@RequestMapping("/system/sysUser")
@AllArgsConstructor
public class SysUserController {
    private final ISysUserService sysUserService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysUser> detail(@RequestParam Long id){
        SysUser result = sysUserService.getById(id);
        return R.ok(result);
    }

    @GetMapping("page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUser>> page(Page page, SysUser query) {
        return R.ok(sysUserService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysUser param){
        sysUserService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysUser param){
        sysUserService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList){
        sysUserService.removeByIds(idList);
        return R.ok();
    }
}
