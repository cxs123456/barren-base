package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysTenant;
import org.barren.modules.system.service.ISysTenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 租户表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "租户表", tags = "租户表接口")
@RequestMapping("/system/sysTenant")
@AllArgsConstructor
public class SysTenantController {
    private final ISysTenantService sysTenantService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysTenant> detail(@RequestParam Long id){
        SysTenant result = sysTenantService.getById(id);
        return R.ok(result);
    }

    @GetMapping("list" )
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public R<List<SysTenant>> list(SysTenant query) {
        return R.ok(sysTenantService.list(Wrappers.query(query)));
    }


    @GetMapping("page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysTenant>> page(Page page, SysTenant query) {
        return R.ok(sysTenantService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysTenant param){
        sysTenantService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysTenant param){
        sysTenantService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList){
        sysTenantService.removeByIds(idList);
        return R.ok();
    }
}
