import request from '@/utils/request'

/**
 * 通过id查询详情
 *
 * @param id
 */
export function detail(id) {
  return request({
    url: '/system/menu/detail',
    method: 'get',
    params: id
  })
}

/**
 * 查询菜单数据的tree列表
 *
 * @param data
 */
export function getTreeList(data) {
  return request({
    url: '/system/menu/treeList',
    method: 'get',
    params: data
  })
}

/**
 * 查询目录菜单数据的tree列表，提供给前端 树型选择框 的数据
 *
 * @param pid
 */
export function lazyTreeList(pid) {
  return request({
    url: '/system/menu/lazyTreeList',
    method: 'get',
    params: pid
  })
}

/**
 * 查询所有菜单数据的tree列表
 */
export function allTreeList() {
  return request({
    url: '/system/menu/allTreeList',
    method: 'get'
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
    url: '/system/menu/page',
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
    url: '/system/menu/save',
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
    url: '/system/menu/update',
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
    url: '/system/menu/delete',
    method: 'post',
    data: ids
  })
}
