package org.barren.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.barren.core.tool.http.R;
import org.barren.modules.system.entity.SysUser;
import org.barren.modules.system.entity.SysUserRole;
import org.barren.modules.system.service.ISysUserRoleService;
import org.barren.modules.system.service.ISysUserService;
import org.barren.modules.system.vo.UserPassVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final ISysUserRoleService sysUserRoleService;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("detail")
    @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    public R<SysUser> detail(@RequestParam Long id) {
        SysUser result = sysUserService.getById(id);
        return R.ok(result);
    }

    @GetMapping("page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUser>> page(Page page, SysUser query) {
        return R.ok(sysUserService.page(page, Wrappers.query(query)));
    }

    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "新增")
    public R save(@RequestBody SysUser param) {

        // 默认密码是 888888
        param.setPassword(passwordEncoder.encode("123456"));
        sysUserService.save(param);

        List<Long> roleIds = param.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            // 新增角色
            Long id = param.getId();
            List<SysUserRole> addList = new ArrayList<>();
            for (Long roleId : roleIds) {
                addList.add(new SysUserRole().setUserId(id).setRoleId(roleId));
            }
            sysUserRoleService.saveBatch(addList);
        }

        return R.ok();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "通过id修改")
    public R update(@RequestBody SysUser param) {
        sysUserService.updateById(param);

        List<Long> roleIds = param.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            Long id = param.getId();
            // 修改角色
            sysUserRoleService.remove(Wrappers.query(new SysUserRole().setUserId(id)));

            List<SysUserRole> addList = new ArrayList<>();
            for (Long roleId : roleIds) {
                addList.add(new SysUserRole().setUserId(id).setRoleId(roleId));
            }
            sysUserRoleService.saveBatch(addList);
        }

        return R.ok();
    }

    /**
     * @param idList
     * @return
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "通过ids删除")
    public R delete(@RequestBody List<Long> idList) {
        sysUserService.removeByIds(idList);
        return R.ok();
    }

    /**
     * 密码设置
     *
     * @param param
     * @return
     */
    @PostMapping("updatePass")
    @ApiOperation(value = "修改密码", notes = "通过id修改修改密码")
    public R updatePass(@RequestBody UserPassVo param) {
        // todo 配置 admin可以修改任何人的密码
        // UserInfo currentUser = AuthUtil.getCurrentUser();

        // todo 这里密码需要前后端 RSA 加密传输
        String newPass = param.getNewPass();
        String oldPass = param.getOldPass();
        // 1、修改密码，判断用户是激活状态就修改
        SysUser user = sysUserService.getById(param.getId());
        if (user.getStatus() != 1) {
            return R.fail("用户未激活！请联系管理员激活");
        }
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            return R.fail("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            return R.fail("新密码不能与旧密码相同");
        }

        user.setPassword(passwordEncoder.encode(param.getNewPass()));
        sysUserService.updateById(user);
        return R.ok();
    }
}
