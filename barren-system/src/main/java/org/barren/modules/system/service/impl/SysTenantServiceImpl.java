package org.barren.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.barren.modules.system.entity.SysTenant;
import org.barren.modules.system.mapper.SysTenantMapper;
import org.barren.modules.system.service.ISysTenantService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author cxs
 * @since 2022-11-29
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

}
