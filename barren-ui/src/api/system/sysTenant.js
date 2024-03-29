import request from '@/utils/request'

/**
* 通过id查询详情
*
* @param id
*/
export function detail(id) {
  return request({
    url: '/system/sysTenant/detail',
    method: 'get',
    params: id
  })
}

/**
* 分页查询
*
* @param current
* @param size
* @param params
*/
export function getList(data) {
  return request({
    url: '/system/sysTenant/page',
    method: 'get',
    params: data
  })
}

/**
* 新增
*
* @param data
*/
export function add(data) {
  return request({
    url: '/system/sysTenant/save',
    method: 'post',
    data
  })
}

/**
* 通过id修改
*
* @param data
*/
export function update(data) {
  return request({
    url: '/system/sysTenant/update',
    method: 'post',
    data:data
  })
}

/**
* 通过ids删除
*
* @param ids
*/
export function del(ids) {
  return request({
    url: '/system/sysTenant/delete',
    method: 'post',
    data:ids
  })
}
