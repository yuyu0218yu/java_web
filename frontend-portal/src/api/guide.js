import request from '@/utils/request'

// 获取攻略列表
export function getGuideList(params) {
  return request.get('/portal/guide/list', { params })
}

// 获取攻略详情
export function getGuideDetail(id) {
  return request.get(`/portal/guide/${id}`)
}

// 发布攻略
export function publishGuide(data) {
  return request.post('/portal/guide/publish', data)
}

// 获取我的攻略
export function getMyGuides(params) {
  return request.get('/portal/guide/my', { params })
}

// 更新攻略
export function updateGuide(id, data) {
  return request.put(`/portal/guide/${id}`, data)
}

// 删除攻略
export function deleteGuide(id) {
  return request.delete(`/portal/guide/${id}`)
}

// 点赞攻略
export function likeGuide(id) {
  return request.post(`/portal/guide/${id}/like`)
}

// 获取热门攻略
export function getHotGuides(limit = 10) {
  return request.get('/portal/guide/hot', { params: { limit } })
}
