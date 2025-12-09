import axios from 'axios'
import { ElMessage } from 'element-plus'
import { resetTokenValidation } from '@/utils/tokenState'

// 网络错误消息防抖
let networkErrorShown = false
let networkErrorTimer = null

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    const { suppressMessage } = response.config || {}

    if (res.code === 200) {
      return res
    } else {
      if (!suppressMessage) {
        ElMessage.error(res.message || '请求失败')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    // 对响应错误做点什么
    if (error.response) {
      const { status, data } = error.response
      const message = data?.message || ''
      
      switch (status) {
        case 401:
          ElMessage.error(message || '未授权，请登录')
          // 清除本地存储的token和用户信息
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          localStorage.removeItem('permissions')
          // 重置 token 验证状态
          resetTokenValidation()
          // 跳转到登录页
          if (window.location.pathname !== '/login') {
            window.location.href = '/login'
          }
          break
        case 403:
          ElMessage.error(message || '没有权限访问此资源')
          break
        case 404:
          ElMessage.error(message || '请求地址不存在')
          break
        case 400:
          ElMessage.error(message || '参数错误')
          break
        case 500:
          ElMessage.error(message || '服务器内部错误')
          break
        default:
          ElMessage.error(message || `连接错误${status}`)
      }
    } else {
      // 网络错误防抖：5秒内只显示一次
      if (!networkErrorShown) {
        networkErrorShown = true
        ElMessage.error('连接到服务器失败，请检查后端服务是否启动')
        if (networkErrorTimer) clearTimeout(networkErrorTimer)
        networkErrorTimer = setTimeout(() => {
          networkErrorShown = false
        }, 5000)
      }
    }
    return Promise.reject(error)
  }
)

export default request
