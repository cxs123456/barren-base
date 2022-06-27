package org.barren.tests;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.barren.modules.system.entity.SysMenu;
import org.barren.modules.system.entity.SysUser;
import org.barren.modules.system.service.ISysMenuService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 单纯测试
 *
 * @author cxs
 **/
@Slf4j
public class Demo {

    @Test
    public void test() {
        String a = "/" + null;
        System.out.println(a);


    }

    @Test
    public void json() {

        String json = "{\n" +
                "    \"phone\": \"1320335756\",\n" +
                "    \"user_name\": \"aaa\",\n" +
                "    \"scope\": [\n" +
                "        \"all\"\n" +
                "    ],\n" +
                "    \"roles\": [\n" +
                "        \"admin\",\n" +
                "        \"user\"\n" +
                "    ],\n" +
                "    \"nickname\": \"小鸟\",\n" +
                "    \"name\": null,\n" +
                "    \"id\": \"8789\",\n" +
                "    \"exp\": 1656109724,\n" +
                "    \"ok\": true,\n" +
                "    \"authorities\": [\n" +
                "        \"admin\",\n" +
                "        \"user\"\n" +
                "    ],\n" +
                "    \"jti\": \"05dccd13-d145-4d11-85e2-977c2f1c10e8\",\n" +
                "    \"client_id\": \"web\"\n" +
                "}";


        SysUser user = JSON.parseObject(json, SysUser.class);

        System.out.println(user);
    }


    public static void main(String[] args) {
        List<SysMenu> menuList = new ArrayList<>();
        SysMenu menu1 = new SysMenu();
        menu1.setId(1L);
        menu1.setPid(null);
        menu1.setName("1111");
        menu1.setSort(1);

        SysMenu menu2 = new SysMenu();
        menu2.setId(2L);
        menu2.setPid(null);
        menu2.setName("2222");
        menu2.setSort(2);

        SysMenu menu3 = new SysMenu();
        menu3.setId(3L);
        menu3.setPid(null);
        menu3.setName("2222");
        menu3.setSort(3);

        SysMenu menu4 = new SysMenu();
        menu4.setId(4L);
        menu4.setPid(2L);
        menu4.setName("4444");
        menu4.setSort(1);

        SysMenu menu5 = new SysMenu();
        menu5.setId(5L);
        menu5.setPid(3L);
        menu5.setName("5555");
        menu5.setSort(1);

        SysMenu menu6 = new SysMenu();
        menu6.setId(6L);
        menu6.setPid(3L);
        menu6.setName("6666");
        menu6.setSort(2);

        SysMenu menu7 = new SysMenu();
        menu7.setId(7L);
        menu7.setPid(3L);
        menu7.setName("7777");
        menu7.setSort(3);

        menuList.add(menu1);
        menuList.add(menu2);
        menuList.add(menu3);
        menuList.add(menu4);
        menuList.add(menu5);
        menuList.add(menu6);
        menuList.add(menu7);

        // TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // // 自定义属性名 否则都是默认值
        // treeNodeConfig.setIdKey("id");
        // treeNodeConfig.setParentIdKey("pid");
        // treeNodeConfig.setWeightKey("sort");
        //
        // List<Tree<Long>> list = TreeUtil.build(menuList, null, treeNodeConfig, (treeNode, tree) -> {
        //     tree.setId(treeNode.getId());
        //     tree.setParentId(treeNode.getPid());
        //     tree.setWeight(treeNode.getSort());
        //     tree.setName(treeNode.getName());
        // });
        // System.out.println(JSON.toJSONString(list, true));
        // String jsonString = JSON.toJSONString(list, true);
        // List<SysMenu> sysMenus = JSON.parseArray(jsonString, SysMenu.class);
        // System.out.println("----------------");
        // System.out.println(JSON.toJSONString(list, true));
        System.out.println(JSON.toJSONString(ISysMenuService.buildTree(menuList), true));
    }
}
