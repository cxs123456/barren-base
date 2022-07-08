import Vue from 'vue'
import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'

// 路由权限钩子
import './permission' // permission control
// 权限指令
import permission from '@/directive/permission'
import checkPermission from '@/utils/permission'

import './assets/icons' // icon
import './utils/error-log' // error log
import * as filters from './filters' // global filters
// 引入Avue
import Avue from '@smallwei/avue'
import '@smallwei/avue/lib/index.css'
import axios from 'axios'


Vue.use(permission);
Vue.use(Avue, {axios});
Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
});
// vue全局权限检查方法
Vue.prototype.$checkPermission = checkPermission;

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
});

Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
});
