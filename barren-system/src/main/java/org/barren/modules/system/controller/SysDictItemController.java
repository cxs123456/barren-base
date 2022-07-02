package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysDictItem;
import org.barren.modules.system.service.ISysDictItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据字典详情表 前端控制器
 * </p>
 *
 * @author cxs
 */
@RestController
@Api(value = "数据字典详情表", tags = "数据字典详情表接口")
@RequestMapping("/system/sysDictItem")
@AllArgsConstructor
public class SysDictItemController {
    private final ISysDictItemService sysDictItemService;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysDictItem> detail(@RequestParam Long id){
        SysDictItem result = sysDictItemService.getById(id);
        return R.ok(result);
    }

    @GetMapping("list" )
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public R<List<SysDictItem>> list(SysDictItem query) {
        return R.ok(sysDictItemService.list(Wrappers.query(query)));
    }


    @GetMapping("page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysDictItem>> page(Page page, SysDictItem query) {
        return R.ok(sysDictItemService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysDictItem param){
        sysDictItemService.save(param);
        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysDictItem param){
        sysDictItemService.updateById(param);
        return R.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList){
        sysDictItemService.removeByIds(idList);
        return R.ok();
    }
}
