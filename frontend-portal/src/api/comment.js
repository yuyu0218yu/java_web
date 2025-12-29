import request from '@/utils/request'

// 获取评论列表
export function getComments(params) {
  return request.get('/portal/comment/list', { params })
}

// 发表评论
export function addComment(data) {
  return request.post('/portal/comment/add', data)
}

// 回复评论
export function replyComment(id, data) {
  return request.post(`/portal/comment/${id}/reply`, data)
}

// 删除评论
export function deleteComment(id) {
  return request.delete(`/portal/comment/${id}`)
}

// 点赞评论
export function likeComment(id) {
  return request.post(`/portal/comment/${id}/like`)
}

// 获取我的评论
export function getMyComments(params) {
  return request.get('/portal/comment/my', { params })
}

// 获取评论回复
export function getCommentReplies(id) {
  return request.get(`/portal/comment/${id}/replies`)
}

// 获取景点评分统计
export function getScenicRatingStats(scenicId) {
  return request.get(`/portal/comment/scenic/${scenicId}/stats`)
}
