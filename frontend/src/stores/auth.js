import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const permissions = ref(JSON.parse(localStorage.getItem('permissions') || '[]'))

  // 计算属性
  const isAuthenticated = computed(() => !!token.value)
  const hasPermission = computed(() => (permission) => {
    return permissions.value.includes(permission) || permissions.value.includes('*')
  })

  // 方法
  const login = async (credentials) => {
    try {
      // 注意：这里需要后端提供登录接口
      const response = await request({
        url: '/auth/login',
        method: 'post',
        data: credentials
      })

      const { token: newToken, user: userInfo, permissions: userPermissions } = response.data

      // 保存到状态和本地存储
      token.value = newToken
      user.value = userInfo
      permissions.value = userPermissions

      localStorage.setItem('token', newToken)
      localStorage.setItem('user', JSON.stringify(userInfo))
      localStorage.setItem('permissions', JSON.stringify(userPermissions))

      ElMessage.success('登录成功')
      return true
    } catch (error) {
      ElMessage.error('登录失败：' + (error.message || '未知错误'))
      return false
    }
  }

  const logout = () => {
    token.value = ''
    user.value = null
    permissions.value = []

    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('permissions')

    ElMessage.success('已退出登录')
  }

  const refreshToken = async () => {
    try {
      const response = await request({
        url: '/auth/refresh',
        method: 'post'
      })

      const { token: newToken } = response.data
      token.value = newToken
      localStorage.setItem('token', newToken)

      return true
    } catch (error) {
      logout()
      return false
    }
  }

  const getUserInfo = async () => {
    try {
      const response = await request({
        url: '/auth/userinfo',
        method: 'get'
      })

      user.value = response.data
      localStorage.setItem('user', JSON.stringify(response.data))

      return true
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return false
    }
  }

  return {
    // 状态
    token,
    user,
    permissions,
    
    // 计算属性
    isAuthenticated,
    hasPermission,
    
    // 方法
    login,
    logout,
    refreshToken,
    getUserInfo
  }
})
