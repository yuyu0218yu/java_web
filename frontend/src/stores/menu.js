import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/utils/request'

export const useMenuStore = defineStore('menu', () => {
  // 状态
  const menus = ref([])
  const permissions = ref([])
  const isLoaded = ref(false)

  // 计算属性
  const hasPermission = computed(() => (permission) => {
    if (!permission) return true
    return permissions.value.includes(permission) || permissions.value.includes('*')
  })

  // 获取用户菜单
  const fetchMenus = async () => {
    try {
      const response = await request({
        url: '/menus/user',
        method: 'get'
      })
      menus.value = response.data || []
      isLoaded.value = true
      return true
    } catch (error) {
      console.error('获取菜单失败:', error)
      menus.value = []
      return false
    }
  }

  // 获取用户权限
  const fetchPermissions = async () => {
    try {
      const response = await request({
        url: '/menus/user/permissions',
        method: 'get'
      })
      permissions.value = response.data || []
      // 同步到localStorage
      localStorage.setItem('permissions', JSON.stringify(permissions.value))
      return true
    } catch (error) {
      console.error('获取权限失败:', error)
      permissions.value = []
      return false
    }
  }

  // 初始化菜单和权限
  const init = async () => {
    if (isLoaded.value) return true
    await Promise.all([fetchMenus(), fetchPermissions()])
    return true
  }

  // 重置
  const reset = () => {
    menus.value = []
    permissions.value = []
    isLoaded.value = false
  }

  return {
    menus,
    permissions,
    isLoaded,
    hasPermission,
    fetchMenus,
    fetchPermissions,
    init,
    reset
  }
})
