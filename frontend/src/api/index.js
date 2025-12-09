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
  },

  // 导出用户
  async exportUsers() {
    const token = localStorage.getItem('token')
    try {
      const response = await fetch('/api/users/export', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (!response.ok) {
        throw new Error('导出失败')
      }

      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      const timestamp = new Date().getTime()
      link.download = `用户数据_${timestamp}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      throw error
    }
  },

  // 导入用户
  importUsers(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/users/import',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 下载导入模板
  async downloadImportTemplate() {
    const token = localStorage.getItem('token')
    try {
      const response = await fetch('/api/users/import/template', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (!response.ok) {
        throw new Error('下载失败')
      }

      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '用户导入模板.xlsx'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      throw error
    }
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

// 部门管理API
export const deptApi = {
  // 获取部门树
  getDeptTree() {
    return request({
      url: '/depts/tree',
      method: 'get'
    })
  },
  
  // 获取部门下拉选项
  getDeptOptions() {
    return request({
      url: '/depts/options',
      method: 'get'
    })
  },

  // 获取部门详情
  getDeptById(id) {
    return request({
      url: `/depts/${id}`,
      method: 'get'
    })
  },

  // 创建部门
  createDept(data) {
    return request({
      url: '/depts',
      method: 'post',
      data
    })
  },

  // 更新部门
  updateDept(id, data) {
    return request({
      url: `/depts/${id}`,
      method: 'put',
      data
    })
  },

  // 删除部门
  deleteDept(id) {
    return request({
      url: `/depts/${id}`,
      method: 'delete'
    })
  },
  
  // 获取子部门ID列表
  getChildDeptIds(id) {
    return request({
      url: `/depts/${id}/children/ids`,
      method: 'get'
    })
  }
}

// 系统配置API
export const configApi = {
  // 获取组件列表
  getComponents() {
    return request({
      url: '/config/components',
      method: 'get'
    })
  },

  // 获取图标列表
  getIcons() {
    return request({
      url: '/config/icons',
      method: 'get'
    })
  },

  // 获取权限标识列表
  getPermissions() {
    return request({
      url: '/config/permissions',
      method: 'get'
    })
  },

  // 通用：根据字典类型获取数据
  getDictByType(dictType) {
    return request({
      url: `/config/dict/${dictType}`,
      method: 'get'
    })
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
  },
  // 获取全部操作日志（分页）
  getAllActivities(params) {
    return request({
      url: '/dashboard/activities',
      method: 'get',
      params
    })
  }
}

// 字典类型管理API
export const dictTypeApi = {
  // 分页查询字典类型
  getPage(params) {
    return request({
      url: '/dict/types/page',
      method: 'get',
      params
    })
  },

  // 获取字典类型列表
  getList() {
    return request({
      url: '/dict/types/list',
      method: 'get'
    })
  },

  // 获取字典类型详情
  getById(id) {
    return request({
      url: `/dict/types/${id}`,
      method: 'get'
    })
  },

  // 创建字典类型
  create(data) {
    return request({
      url: '/dict/types',
      method: 'post',
      data
    })
  },

  // 更新字典类型
  update(id, data) {
    return request({
      url: `/dict/types/${id}`,
      method: 'put',
      data
    })
  },

  // 删除字典类型
  delete(id) {
    return request({
      url: `/dict/types/${id}`,
      method: 'delete'
    })
  },

  // 批量删除字典类型
  batchDelete(ids) {
    return request({
      url: '/dict/types/batch',
      method: 'delete',
      data: ids
    })
  }
}

// 字典数据管理API
export const dictDataApi = {
  // 分页查询字典数据
  getPage(params) {
    return request({
      url: '/dict/data/page',
      method: 'get',
      params
    })
  },

  // 根据字典类型获取数据
  getByType(dictType) {
    return request({
      url: `/dict/data/type/${dictType}`,
      method: 'get'
    })
  },

  // 获取字典数据详情
  getById(id) {
    return request({
      url: `/dict/data/${id}`,
      method: 'get'
    })
  },

  // 创建字典数据
  create(data) {
    return request({
      url: '/dict/data',
      method: 'post',
      data
    })
  },

  // 更新字典数据
  update(id, data) {
    return request({
      url: `/dict/data/${id}`,
      method: 'put',
      data
    })
  },

  // 删除字典数据
  delete(id) {
    return request({
      url: `/dict/data/${id}`,
      method: 'delete'
    })
  },

  // 批量删除字典数据
  batchDelete(ids) {
    return request({
      url: '/dict/data/batch',
      method: 'delete',
      data: ids
    })
  }
}

// 操作日志API
export const operationLogApi = {
  // 分页查询操作日志
  getPage(params) {
    return request({
      url: '/logs/operation/page',
      method: 'get',
      params
    })
  },

  // 获取操作日志详情
  getById(id) {
    return request({
      url: `/logs/operation/${id}`,
      method: 'get'
    })
  },

  // 根据用户名查询
  getByUsername(username, limit = 100) {
    return request({
      url: `/logs/operation/user/${username}`,
      method: 'get',
      params: { limit }
    })
  },

  // 删除操作日志
  delete(id) {
    return request({
      url: `/logs/operation/${id}`,
      method: 'delete'
    })
  },

  // 批量删除操作日志
  batchDelete(ids) {
    return request({
      url: '/logs/operation/batch',
      method: 'delete',
      data: ids
    })
  },

  // 清空操作日志
  clean() {
    return request({
      url: '/logs/operation/clean',
      method: 'delete'
    })
  },

  // 删除指定天数之前的日志
  cleanBeforeDays(days) {
    return request({
      url: `/logs/operation/clean/${days}`,
      method: 'delete'
    })
  }
}

// 登录日志API
export const loginLogApi = {
  // 分页查询登录日志
  getPage(params) {
    return request({
      url: '/logs/login/page',
      method: 'get',
      params
    })
  },

  // 获取登录日志详情
  getById(id) {
    return request({
      url: `/logs/login/${id}`,
      method: 'get'
    })
  },

  // 删除登录日志
  delete(id) {
    return request({
      url: `/logs/login/${id}`,
      method: 'delete'
    })
  },

  // 批量删除登录日志
  batchDelete(ids) {
    return request({
      url: '/logs/login/batch',
      method: 'delete',
      data: ids
    })
  },

  // 清空登录日志
  clean() {
    return request({
      url: '/logs/login/clean',
      method: 'delete'
    })
  },

  // 删除指定天数之前的日志
  cleanBeforeDays(days) {
    return request({
      url: `/logs/login/clean/${days}`,
      method: 'delete'
    })
  }
}

// 通知公告API
export const noticeApi = {
  // 分页查询通知公告
  getPage(params) {
    return request({
      url: '/notices/page',
      method: 'get',
      params
    })
  },

  // 获取通知公告详情
  getById(id) {
    return request({
      url: `/notices/${id}`,
      method: 'get'
    })
  },

  // 获取最新通知公告
  getLatest(limit = 10) {
    return request({
      url: '/notices/latest',
      method: 'get',
      params: { limit }
    })
  },

  // 创建通知公告
  create(data) {
    return request({
      url: '/notices',
      method: 'post',
      data
    })
  },

  // 更新通知公告
  update(id, data) {
    return request({
      url: `/notices/${id}`,
      method: 'put',
      data
    })
  },

  // 删除通知公告
  delete(id) {
    return request({
      url: `/notices/${id}`,
      method: 'delete'
    })
  },

  // 批量删除通知公告
  batchDelete(ids) {
    return request({
      url: '/notices/batch',
      method: 'delete',
      data: ids
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

// 定时任务API
export const jobApi = {
  // 分页查询定时任务
  getPage(params) {
    return request({
      url: '/jobs/page',
      method: 'get',
      params
    })
  },

  // 获取定时任务详情
  getById(id) {
    return request({
      url: `/jobs/${id}`,
      method: 'get'
    })
  },

  // 获取所有正常状态的任务
  getActiveJobs() {
    return request({
      url: '/jobs/active',
      method: 'get'
    })
  },

  // 创建定时任务
  create(data) {
    return request({
      url: '/jobs',
      method: 'post',
      data
    })
  },

  // 更新定时任务
  update(id, data) {
    return request({
      url: `/jobs/${id}`,
      method: 'put',
      data
    })
  },

  // 删除定时任务
  delete(id) {
    return request({
      url: `/jobs/${id}`,
      method: 'delete'
    })
  },

  // 批量删除定时任务
  batchDelete(ids) {
    return request({
      url: '/jobs/batch',
      method: 'delete',
      data: ids
    })
  },

  // 暂停任务
  pause(id) {
    return request({
      url: `/jobs/${id}/pause`,
      method: 'put'
    })
  },

  // 恢复任务
  resume(id) {
    return request({
      url: `/jobs/${id}/resume`,
      method: 'put'
    })
  },

  // 立即执行一次任务
  run(id) {
    return request({
      url: `/jobs/${id}/run`,
      method: 'post'
    })
  },

  // 校验cron表达式
  checkCron(cronExpression) {
    return request({
      url: '/jobs/check-cron',
      method: 'post',
      data: { cronExpression }
    })
  }
}

// 定时任务日志API
export const jobLogApi = {
  // 分页查询任务日志
  getPage(params) {
    return request({
      url: '/jobs/logs/page',
      method: 'get',
      params
    })
  },

  // 获取任务日志详情
  getById(id) {
    return request({
      url: `/jobs/logs/${id}`,
      method: 'get'
    })
  },

  // 根据任务ID查询日志
  getByJobId(jobId, params) {
    return request({
      url: `/jobs/logs/job/${jobId}`,
      method: 'get',
      params
    })
  },

  // 删除任务日志
  delete(id) {
    return request({
      url: `/jobs/logs/${id}`,
      method: 'delete'
    })
  },

  // 批量删除任务日志
  batchDelete(ids) {
    return request({
      url: '/jobs/logs/batch',
      method: 'delete',
      data: ids
    })
  },

  // 清空任务日志
  clean() {
    return request({
      url: '/jobs/logs/clean',
      method: 'delete'
    })
  },

  // 删除指定天数之前的日志
  cleanBeforeDays(days) {
    return request({
      url: `/jobs/logs/clean/${days}`,
      method: 'delete'
    })
  }
}
