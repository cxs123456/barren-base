import request from '@/utils/request'

export function detail(id) {
  return request({
    url: '/system/sysUser/detail',
    method: 'get',
    params: id
  })
}

export function page(current, size, params) {
  return request({
    url: '/system/sysUser/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export function save(data) {
  return request({
    url: '/system/sysUser/save',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/system/sysUser/update',
    method: 'post',
    data:data
  })
}

export function del(ids) {
  return request({
    url: '/system/sysUser/delete',
    method: 'post',
    data:ids
  })
}
