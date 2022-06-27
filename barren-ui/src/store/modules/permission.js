// import { asyncRoutes, constantRoutes } from '@/router'
import { constantRoutes } from '@/router'
import { buildMenus } from '@/api/system/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = [];

  routes.forEach(route => {
    const tmp = { ...route };
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  });

  return res
}

const state = {
  routes: [],
  addRoutes: [],
  sidebarRouters: []
};

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes;
    state.routes = constantRoutes.concat(routes)
  },
  SET_SIDEBAR_ROUTERS: (state, routers) => {
    state.sidebarRouters = constantRoutes.concat(routers)
  }
};

const actions = {
  generateRoutes({ commit, state }) {
    // return new Promise(resolve => {
    //   let accessedRoutes
    //   if (roles.includes('admin')) {
    //     accessedRoutes = asyncRoutes || []
    //   } else {
    //     accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    //   }
    //   commit('SET_ROUTES', accessedRoutes)
    //   resolve(accessedRoutes)
    // })

    return new Promise((resolve, reject) => {
      // 向后端请求路由数据
      buildMenus().then(response => {
        if (!response.data) {
          response.data = []
        }

        const sdata = JSON.parse(JSON.stringify(response.data));
        const rdata = JSON.parse(JSON.stringify(response.data));
        // todo 对 目录 菜单 类型的菜单表数据进行处理 ...
        const sidebarRoutes = filterAsyncRouter(sdata);
        const rewriteRoutes = filterAsyncRouter(rdata, false, true);
        rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true });
        commit('SET_ROUTES', rewriteRoutes);
        commit('SET_SIDEBAR_ROUTERS', sidebarRoutes);

        resolve(rewriteRoutes)
      }).catch(error => {
        reject(error)
      })
    })
  }
};

/* 遍历路由集合，对 目录和菜单 类型的路由对象中 component 属性进行处理
 * routers : 路由集合
 * lastRouter : 父路由对象
 * type : 对路由集合数据是否复写
 */
function filterAsyncRouter(routers, lastRouter = false, type = false) {
  return routers.filter(router => {
    if (type && router.children) {
      router.children = filterChildren(router.children)
    }
    if (router.component) { // 目录和菜单 处理
      if (router.component === 'Layout') { // 顶级的目录 component == Layout组件
        router.component = Layout
      } else if (router.component === 'ParentView') { // 多级菜单，除了1级目录component=Layout，次级目录的component=ParentView需要嵌套 router-view
        router.component = ParentView
      } else { // 叶子菜单（非目录），拼接组件
        router.component = loadView(router.component)
      }
    }
    if (router.children != null && router.children && router.children.length) { // 处理子路由
      router.children = filterAsyncRouter(router.children, router, type)
    } else { // 没有子路由，将属性删除防止路由数据报错
      delete router['children'];
      delete router['redirect']
    }
    return true
  })
}

/* 用于处理多级目录，层级>2
 * 遍历路由集合中的 ParentView 目录以及它下面的子菜单，对 目录的叶子菜单 的路由对象中 path 属性进行处理（不需要处理目录，因为没有path属性）
 * childrenMap : 子路由集合
 * lastRouter : 父路由
 */
function filterChildren(childrenMap, lastRouter = false) {
  var children = [];
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path;
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c));
            return // forEach 通过return 终结本次循环
          }
          children.push(c)
        });
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  });
  return children
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
