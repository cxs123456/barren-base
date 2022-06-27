import request from '@/utils/request'

export function detail(id) {
  return request({
    url: '/${package.ModuleName}/${table.entityPath}/detail',
    method: 'get',
    params: id
  })
}

export function page(current, size, params) {
  return request({
    url: '/${package.ModuleName}/${table.entityPath}/page',
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
    url: '/${package.ModuleName}/${table.entityPath}/save',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/${package.ModuleName}/${table.entityPath}/update',
    method: 'post',
    data:data
  })
}

export function del(ids) {
  return request({
    url: '/${package.ModuleName}/${table.entityPath}/delete',
    method: 'post',
    data:ids
  })
}
