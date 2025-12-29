import request from '@/utils/request'

// 获取景点列表
export function getScenicList(params) {
  return request.get('/portal/scenic/list', { params })
}

// 获取景点详情
export function getScenicDetail(id) {
  return request.get(`/portal/scenic/${id}`)
}

// 获取景点门票
export function getScenicTickets(scenicId) {
  return request.get(`/portal/scenic/${scenicId}/tickets`)
}

// 获取门票详情
export function getTicketDetail(ticketId) {
  return request.get(`/portal/scenic/ticket/${ticketId}`)
}

// 获取景点分类
export function getScenicCategories() {
  return request.get('/portal/scenic/categories')
}

// 搜索景点
export function searchScenic(keyword) {
  return request.get('/portal/scenic/search', { params: { keyword } })
}
