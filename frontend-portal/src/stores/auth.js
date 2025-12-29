import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('portal_token') || '')
  const user = ref(null)

  const isAuthenticated = computed(() => !!token.value)

  // 登录
  async function login(credentials) {
    const res = await loginApi(credentials)
    if (res.code === 200) {
      token.value = res.data.token
      localStorage.setItem('portal_token', res.data.token)
      await fetchUserInfo()
    }
    return res
  }

  // 注册
  async function register(userData) {
    const res = await registerApi(userData)
    return res
  }

  // 获取用户信息
  async function fetchUserInfo() {
    if (!token.value) return
    try {
      const res = await getUserInfo()
      if (res.code === 200) {
        user.value = res.data
      }
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }

  // 退出登录
  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('portal_token')
  }

  // 初始化时获取用户信息
  if (token.value) {
    fetchUserInfo()
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    register,
    fetchUserInfo,
    logout
  }
})
