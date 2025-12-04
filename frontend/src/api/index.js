import request from '@/utils/request'

// 认证管理API
export const authApi = {
  // 用户登录
  login(data) {
    return request({
      url: '/auth/login',
      method: 'post',
      data
    })
  },

  // 获取用户信息
  getUserInfo() {
    return request({
      url: '/auth/userinfo',
      method: 'get'
    })
  },

  // 刷新Token
  refreshToken(refreshToken) {
    return request({
      url: '/auth/refresh',
      method: 'post',
      data: { refreshToken }
    })
  },

  // 退出登录
  logout() {
    return request({
      url: '/auth/logout',
      method: 'post'
    })
  }
}

// 用户管理API
export const userApi = {
  // 获取用户分页列表
  getUserPage(params) {
    return request({
      url: '/users/page',
      method: 'get',
      params
    })
  },

  // 根据ID获取用户
  getUserById(id) {
    return request({
      url: `/users/${id}`,
      method: 'get'
    })
  },

  // 根据用户名获取用户
  getUserByUsername(username) {
    return request({
      url: `/users/username/${username}`,
      method: 'get'
    })
  },

  // 创建用户
  createUser(data) {
    return request({
      url: '/users',
      method: 'post',
      data
    })
  },

  // 更新用户
  updateUser(id, data) {
    return request({
      url: `/users/${id}`,
      method: 'put',
      data
    })
  },

  // 删除用户
  deleteUser(id) {
    return request({
      url: `/users/${id}`,
      method: 'delete'
    })
  },

  // 批量删除用户
  batchDeleteUsers(ids) {
    return request({
      url: '/users/batch',
      method: 'delete',
      data: { ids }
    })
  },

  // 重置用户密码
  resetPassword(id, newPassword) {
    return request({
      url: `/users/${id}/reset-password`,
      method: 'put',
      data: { password: newPassword }
    })
  },

  // 更新用户状态
  updateUserStatus(id, status) {
    return request({
      url: `/users/${id}/status`,
      method: 'put',
      data: { status }
    })
  }
}

// 角色管理API
export const roleApi = {
  // 获取角色列表
  getRoleList() {
    return request({
      url: '/roles',
      method: 'get'
    })
  },

  // 创建角色
  createRole(data) {
    return request({
      url: '/roles',
      method: 'post',
      data
    })
  },

  // 更新角色
  updateRole(id, data) {
    return request({
      url: `/roles/${id}`,
      method: 'put',
      data
    })
  },

  // 删除角色
  deleteRole(id) {
    return request({
      url: `/roles/${id}`,
      method: 'delete'
    })
  }
}

// 权限管理API
export const permissionApi = {
  // 获取权限列表
  getPermissionList() {
    return request({
      url: '/permissions',
      method: 'get'
    })
  },

  // 获取权限树
  getPermissionTree() {
    return request({
      url: '/permissions/tree',
      method: 'get'
    })
  },

  // 创建权限
  createPermission(data) {
    return request({
      url: '/permissions',
      method: 'post',
      data
    })
  },

  // 更新权限
  updatePermission(id, data) {
    return request({
      url: `/permissions/${id}`,
      method: 'put',
      data
    })
  },

  // 删除权限
  deletePermission(id) {
    return request({
      url: `/permissions/${id}`,
      method: 'delete'
    })
  }
}
