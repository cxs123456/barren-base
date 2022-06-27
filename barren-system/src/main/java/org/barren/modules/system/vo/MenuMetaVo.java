package org.barren.modules.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * 构建前端路由 meta 数据
 *
 * @author cxs
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}
