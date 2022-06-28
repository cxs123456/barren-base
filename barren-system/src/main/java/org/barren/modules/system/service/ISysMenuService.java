package org.barren.modules.system.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.StringUtils;
import org.barren.modules.system.entity.SysMenu;
import org.barren.modules.system.vo.MenuMetaVo;
import org.barren.modules.system.vo.MenuVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 构建 menu tree 结构
     *
     * @param menus
     * @return
     */
    static List<SysMenu> buildTree(List<SysMenu> menus) {
        List<SysMenu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (SysMenu menu : menus) {
            if (menu.getPid() == null) {
                trees.add(menu);
            }
            for (SysMenu it : menus) {
                if (menu.getId().equals(it.getPid())) {
                    if (menu.getChildren() == null) {
                        menu.setChildren(new ArrayList<>());
                    }
                    menu.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    /**
     * 构建 前端菜单路由 tree 结构
     *
     * @param menuList
     * @return
     */
    static List<MenuVo> buildMenus(List<SysMenu> menuList) {
        List<MenuVo> list = new LinkedList<>();
        menuList.forEach(menu -> {
                    if (menu != null) {
                        String path = Objects.toString(menu.getPath(), "");
                        List<SysMenu> menuDtoList = menu.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(menu.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menu.getPid() == null ? "/" + path : path);
                        menuVo.setHidden(menu.getHidden());
                        // 如果不是外链
                        if (!menu.getIFrame()) {
                            if (menu.getPid() == null) {
                                menuVo.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                                // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                            } else if (menu.getType() == 0) {
                                menuVo.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
                            } else if (StringUtils.isNoneBlank(menu.getComponent())) {
                                menuVo.setComponent(menu.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menu.getTitle(), menu.getIcon(), !menu.getCache()));
                        if (CollectionUtil.isNotEmpty(menuDtoList)) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noRedirect");
                            menuVo.setChildren(buildMenus(menuDtoList));

                        } else if (menu.getPid() == null) { // 处理是一级菜单并且没有子菜单的情况
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menu.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(path);
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }
}
