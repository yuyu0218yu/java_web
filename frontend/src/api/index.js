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

  register(data) {
    return request({
      url: '/auth/register',
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
  },

  // 获取个人信息
  getProfile() {
    return request({
      url: '/auth/profile',
      method: 'get'
    })
  },

  // 更新个人信息
  updateProfile(data) {
    return request({
      url: '/auth/profile',
      method: 'put',
      data
    })
  },

  // 修改密码
  changePassword(data) {
    return request({
      url: '/auth/profile/password',
      method: 'put',
      data
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
      data: { newPassword }
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

// 菜单管理API
export const menuApi = {
  // 获取菜单树
  getMenuTree() {
    return request({
      url: '/menus/tree',
      method: 'get'
    })
  },

  // 获取菜单列表
  getMenuList() {
    return request({
      url: '/menus/list',
      method: 'get'
    })
  },

  // 创建菜单
  createMenu(data) {
    return request({
      url: '/menus',
      method: 'post',
      data
    })
  },

  // 更新菜单
  updateMenu(id, data) {
    return request({
      url: `/menus/${id}`,
      method: 'put',
      data
    })
  },

  // 删除菜单
  deleteMenu(id) {
    return request({
      url: `/menus/${id}`,
      method: 'delete'
    })
  },

  // 获取角色的菜单ID列表
  getRoleMenuIds(roleId) {
    return request({
      url: `/menus/role/${roleId}`,
      method: 'get'
    })
  },

  // 分配角色菜单权限
  saveRoleMenus(roleId, menuIds) {
    return request({
      url: `/menus/role/${roleId}`,
      method: 'post',
      data: menuIds
    })
  }
}

// 文件管理API
export const fileApi = {
  // 上传单个文件
  uploadFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/files/upload',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 批量上传文件
  uploadFiles(files) {
    const formData = new FormData()
    files.forEach(file => {
      formData.append('files', file)
    })
    return request({
      url: '/files/batch-upload',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 获取文件列表
  getFileList(params) {
    return request({
      url: '/files/list',
      method: 'get',
      params
    })
  },

  // 获取文件信息
  getFileInfo(fileId) {
    return request({
      url: `/files/${fileId}`,
      method: 'get'
    })
  },

  // 删除文件
  deleteFile(fileId) {
    return request({
      url: `/files/${fileId}`,
      method: 'delete'
    })
  },

  // 获取文件统计
  getFileStatistics() {
    return request({
      url: '/files/statistics',
      method: 'get'
    })
  },

  // 获取热门文件
  getHotFiles(limit = 10) {
    return request({
      url: '/files/hot',
      method: 'get',
      params: { limit }
    })
  },

  // 获取最新文件
  getLatestFiles(limit = 10) {
    return request({
      url: '/files/latest',
      method: 'get',
      params: { limit }
    })
  },

  // 下载文件
  downloadFile(fileId) {
    const token = localStorage.getItem('token')
    const link = document.createElement('a')
    link.href = `/api/files/${fileId}/download`
    // Note: For actual download with auth, you may need to use blob approach
    link.click()
  },

  // 下载文件 (blob方式，支持认证)
  async downloadFileBlob(fileId, fileName) {
    const token = localStorage.getItem('token')
    try {
      const response = await fetch(`/api/files/${fileId}/download`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      
      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || '下载失败')
      }
      
      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName || 'download'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      throw error
    }
  },

  // 预览文件URL
  getPreviewUrl(fileId) {
    return `/api/files/${fileId}/preview`
  }
}

// 仪表板API
export const dashboardApi = {
  // 获取仪表板统计数据
  getStatistics(period = 'month') {
    return request({
      url: '/dashboard/statistics',
      method: 'get',
      params: { period }
    })
  }
}

// 代码生成器API (RuoYi风格)
export const generatorApi = {
  // 分页查询已导入的表列表
  getTablePage(params) {
    return request({
      url: '/gen/table/page',
      method: 'get',
      params
    })
  },

  // 查询数据库表列表（未导入的）
  getDbTableList(params) {
    return request({
      url: '/gen/db/list',
      method: 'get',
      params
    })
  },

  // 获取表详情
  getTableById(tableId) {
    return request({
      url: `/gen/table/${tableId}`,
      method: 'get'
    })
  },

  // 导入表结构
  importTable(tableNames) {
    return request({
      url: '/gen/table/import',
      method: 'post',
      data: tableNames
    })
  },

  // 更新表配置
  updateTable(genTable) {
    return request({
      url: '/gen/table',
      method: 'put',
      data: genTable
    })
  },

  // 删除表
  deleteTable(tableIds) {
    return request({
      url: `/gen/table/${tableIds.join(',')}`,
      method: 'delete'
    })
  },

  // 同步数据库表结构
  syncDb(tableId) {
    return request({
      url: `/gen/table/sync/${tableId}`,
      method: 'post'
    })
  },

  // 预览代码
  previewCode(tableId) {
    return request({
      url: `/gen/preview/${tableId}`,
      method: 'get'
    })
  },

  // 下载代码（单表）
  async downloadCode(tableId, tableName = 'code') {
    const token = localStorage.getItem('token')
    try {
      const response = await fetch(`/api/gen/download/${tableId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || '下载失败')
      }

      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      const timestamp = new Date().getTime()
      link.download = `${tableName}_${timestamp}.zip`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      throw error
    }
  },

  // 批量下载代码
  async downloadCodeBatch(tableIds) {
    const token = localStorage.getItem('token')
    try {
      const response = await fetch(`/api/gen/download/batch?tableIds=${tableIds.join(',')}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || '下载失败')
      }

      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      const timestamp = new Date().getTime()
      link.download = `code_batch_${timestamp}.zip`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      throw error
    }
  }
}
