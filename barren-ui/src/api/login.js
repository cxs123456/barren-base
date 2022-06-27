import request from '@/utils/request'

export function login(username, password, code, codeKey) {
  return request({
    url: 'auth/login',
    method: 'post',
    data: {
      username,
      password,
      code,
      codeKey
    }
  })
}

export function getInfo() {
  return request({
    url: 'auth/info',
    // url: 'demo/hello',
    method: 'get'
  })
}

export function getCodeImg() {
  return request({
    url: 'auth/captcha',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'delete'
  })
}
