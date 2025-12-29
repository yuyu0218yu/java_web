import request from '@/utils/request'

// 登录
export function login(data) {
  return request.post('/auth/login', data)
}

// 注册
export function register(data) {
  return request.post('/auth/register', data)
}

// 获取当前用户信息
export function getUserInfo() {
  return request.get('/portal/user/profile')
}

// 更新用户信息
export function updateUserInfo(data) {
  return request.put('/portal/user/profile', data)
}

// 修改密码
export function changePassword(data) {
  return request.post('/portal/user/password', null, { params: data })
}

// 更新头像
export function updateAvatar(avatarUrl) {
  return request.post('/portal/user/avatar', null, { params: { avatarUrl } })
}

// 获取用户统计
export function getUserStats() {
  return request.get('/portal/user/stats')
}
