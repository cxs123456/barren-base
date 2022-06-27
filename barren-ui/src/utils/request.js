import axios from 'axios'
import { Message, Notification } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 20000 // request timeout
});

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token) {
      // 设置Bearer授权请求头
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }

    return config
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error)
  }
);

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    // 二进制数据则直接返回
    if (response.request.responseType === 'blob' || response.request.responseType === 'arraybuffer') {
      return response.data
    }
    // 未设置状态码则默认成功状态
    const code = response.data.code || 200;
    // 获取错误信息
    const msg = response.data.msg;
    if (code == 401) {
      Message({
        message: '用户名或密码错误',
        type: 'error',
        duration: 5 * 1000
      });
      return Promise.reject(new Error(msg || 'Error'))
    }
    // 后端 code=0或200 为成功
    if (code !== 0 && code !== 200) {
      Message({
        message: msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      });
      return Promise.reject(new Error(msg || 'Error'))
    }
    return response.data
  },
  error => {
    let code = 0;
    try {
      code = error.response.data.status
    } catch (e) {
      if (error.toString().indexOf('Error: timeout') !== -1) {
        Notification.error({
          title: '网络请求超时',
          duration: 5000
        });
        return Promise.reject(error)
      }
    }
    // 没有访问权限，重新登录
    if (code === 401) {
      location.reload()
    } else {
      Notification.error({
        title: '请求失败',
        duration: 5000
      })
    }
    return Promise.reject(error)
  }
);

export default service
