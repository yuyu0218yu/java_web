import request from '@/utils/request'

// 切换收藏状态
export function toggleFavorite(targetType, targetId) {
  return request.post('/portal/favorite/toggle', null, {
    params: { targetType, targetId }
  })
}

// 检查是否已收藏
export function checkFavorite(targetType, targetId) {
  return request.get('/portal/favorite/check', {
    params: { targetType, targetId }
  })
}

// 获取收藏的景点
export function getFavoriteScenics(params) {
  return request.get('/portal/favorite/scenic', { params })
}

// 获取收藏的攻略
export function getFavoriteGuides(params) {
  return request.get('/portal/favorite/guide', { params })
}

// 获取所有收藏
export function getAllFavorites(params) {
  return request.get('/portal/favorite/list', { params })
}

// 取消收藏
export function removeFavorite(id) {
  return request.delete(`/portal/favorite/${id}`)
}
