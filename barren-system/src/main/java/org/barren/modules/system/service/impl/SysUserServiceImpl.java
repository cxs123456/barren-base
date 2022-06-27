package org.barren.modules.system.service.impl;

import org.barren.modules.system.entity.SysUser;
import org.barren.modules.system.mapper.SysUserMapper;
import org.barren.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
