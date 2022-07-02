import request from '@/utils/request'

/**
 * 通过id查询详情
 *
 * @param id
 */
export function detail(id) {
  return request({
    url: '/system/sysDictItem/detail',
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
    url: '/system/sysDictItem/page',
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
    url: '/system/sysDictItem/save',
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
    url: '/system/sysDictItem/update',
    method: 'post',
    data: data
  })
}

/**
 * 通过ids删除
 *
 * @param ids
 */
export function del(ids) {
  return request({
    url: '/system/sysDictItem/delete',
    method: 'post',
    data: ids
  })
}

/**
 * 查询列表
 * @param data
 */
export function list(data) {
  return request({
    url: '/system/sysDictItem/list',
    method: 'get',
    params: data
  })
}
