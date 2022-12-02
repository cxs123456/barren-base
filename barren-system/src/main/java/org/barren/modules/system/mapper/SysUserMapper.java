package org.barren.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.barren.modules.system.entity.SysUser;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
// @Mapper // 使用 `@MapperScan` 后，接口类 就不需要使用 `@Mapper` 注解。
public interface SysUserMapper extends BaseMapper<SysUser> {

}
