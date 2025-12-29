import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request.post('/portal/order/create', data)
}

// 获取我的订单列表
export function getMyOrders(params) {
  return request.get('/portal/order/list', { params })
}

// 获取订单详情
export function getOrderDetail(orderNo) {
  return request.get(`/portal/order/${orderNo}`)
}

// 取消订单
export function cancelOrder(orderNo) {
  return request.post(`/portal/order/${orderNo}/cancel`)
}
