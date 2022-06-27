package org.barren.modules.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.barren.core.tool.http.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/system/menus")
public class MenuController {

    @ApiOperation(value = "获取前端菜单", notes = "根据角色生成前端侧边栏菜单数据（动态路由数据）")
    @GetMapping(value = "/build")
    public R<Object> buildMenus() {


        return R.ok();
    }
}
