package ${package.Controller};

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.barren.core.tool.http.R;
import java.util.Arrays;
import java.util.List;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@Api(value = "${table.comment!}", tags = "${table.comment!}接口")
@RequestMapping("/v1<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@AllArgsConstructor
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final ${table.serviceName} ${entity?uncap_first}Service;

    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<${entity}> detail(@PathVariable Long id){
        ${entity} result = ${entity?uncap_first}Service.getById(id);
        return R.ok(result);
    }


    @GetMapping("")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<${entity}>> page(Page page, ${entity} query) {
        return R.ok(${entity?uncap_first}Service.page(page, Wrappers.query(query)));
    }

    @PostMapping("")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody ${entity} param){
        ${entity?uncap_first}Service.save(param);
        return R.ok();
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@PathVariable Long id, @RequestBody ${entity} param){
        ${entity?uncap_first}Service.updateById(param);
        return R.ok();
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@PathVariable Long id){
        ${entity?uncap_first}Service.removeByIds(Arrays.asList(id));
        return R.ok();
    }
}
