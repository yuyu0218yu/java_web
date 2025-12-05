<template>
  <div class="login-container" :class="{ 'dark-mode': isDarkMode }">
    <div class="login-background">
      <div class="grid-pattern"></div>
    </div>
    
    <transition name="zoom-in" appear>
      <div class="login-box">
        <div class="login-header">
          <div class="logo-icon">
            <el-icon><Lock /></el-icon>
          </div>
          <h2>权限管理系统</h2>
          <p class="welcome-text">Enterprise Management Platform</p>
          <div class="typing-effect">
            <span>{{ typingText }}</span>
            <span class="cursor">|</span>
          </div>
        </div>
        
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          size="large"
        >
          <transition name="slide-right" appear>
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                :prefix-icon="User"
                clearable
                class="input-with-animation"
              />
            </el-form-item>
          </transition>
          
          <transition name="slide-right" appear style="animation-delay: 0.1s">
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                :prefix-icon="Lock"
                show-password
                clearable
                class="input-with-animation"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
          </transition>
          
          <transition name="slide-right" appear style="animation-delay: 0.2s">
            <div class="form-options">
              <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
              <a href="#" class="forgot-password">忘记密码？</a>
            </div>
          </transition>
          
          <transition name="slide-up" appear style="animation-delay: 0.3s">
            <el-form-item>
              <el-button
                type="primary"
                class="login-button"
                :class="{ 'is-loading': loading }"
                :loading="loading"
                @click="handleLogin"
              >
                <template v-if="!loading">
                  <el-icon class="button-icon"><Unlock /></el-icon>
                  <span>登 录</span>
                </template>
                <template v-else>
                  <span>登录中...</span>
                </template>
              </el-button>
            </el-form-item>
          </transition>
        </el-form>
        
        <div class="login-footer">
          <div class="divider">
            <span>其他登录方式</span>
          </div>
          <div class="social-login">
            <el-tooltip content="微信登录" placement="top">
              <div class="social-icon wechat">
                <el-icon><ChatDotRound /></el-icon>
              </div>
            </el-tooltip>
            <el-tooltip content="企业微信" placement="top">
              <div class="social-icon work-wechat">
                <el-icon><OfficeBuilding /></el-icon>
              </div>
            </el-tooltip>
            <el-tooltip content="钉钉登录" placement="top">
              <div class="social-icon dingtalk">
                <el-icon><Message /></el-icon>
              </div>
            </el-tooltip>
          </div>
          <p class="copyright">© 2025 用户权限管理系统 v1.0.0</p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { User, Lock, Unlock, ChatDotRound, OfficeBuilding, Message } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const loginFormRef = ref()
const typingText = ref('')
const isDarkMode = ref(false)

// 打字机效果文字
const fullText = '安全 · 高效 · 便捷'
let typingIndex = 0
let typingTimer = null

// 检测系统暗色模式
const checkDarkMode = () => {
  isDarkMode.value = window.matchMedia('(prefers-color-scheme: dark)').matches
}

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 打字机效果
const startTyping = () => {
  if (typingTimer) {
    clearInterval(typingTimer)
  }
  typingTimer = setInterval(() => {
    if (typingIndex < fullText.length) {
      typingText.value += fullText[typingIndex]
      typingIndex++
    } else {
      // 清除当前定时器，重置后重新开始
      clearInterval(typingTimer)
      typingTimer = null
      setTimeout(() => {
        typingText.value = ''
        typingIndex = 0
        startTyping()
      }, 2000)
    }
  }, 150)
}

// 方法
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const success = await authStore.login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    if (success) {
      router.push('/dashboard')
    }
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

// 监听系统主题变化
let mediaQuery = null

// 生命周期
onMounted(() => {
  if (authStore.isAuthenticated) {
    router.push('/dashboard')
  }
  startTyping()
  
  // 初始化暗色模式检测
  checkDarkMode()
  mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  mediaQuery.addEventListener('change', checkDarkMode)
})

onUnmounted(() => {
  if (typingTimer) {
    clearInterval(typingTimer)
  }
  if (mediaQuery) {
    mediaQuery.removeEventListener('change', checkDarkMode)
  }
})
</script>

<style scoped>
/* 浅色模式（默认） */
.login-container {
  --bg-primary: #f5f7fa;
  --bg-secondary: #ffffff;
  --text-primary: #1a1a1a;
  --text-secondary: #606266;
  --text-muted: #909399;
  --border-color: #e4e7ed;
  --accent-color: #1a1a1a;
  --accent-hover: #333333;
  --shadow-color: rgba(0, 0, 0, 0.08);
  --grid-color: rgba(0, 0, 0, 0.03);
}

/* 暗色模式 */
.login-container.dark-mode {
  --bg-primary: #0a0a0a;
  --bg-secondary: #1a1a1a;
  --text-primary: #ffffff;
  --text-secondary: #a0a0a0;
  --text-muted: #666666;
  --border-color: #333333;
  --accent-color: #ffffff;
  --accent-hover: #e0e0e0;
  --shadow-color: rgba(0, 0, 0, 0.3);
  --grid-color: rgba(255, 255, 255, 0.03);
}

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: var(--bg-primary);
  overflow: hidden;
  transition: background 0.3s ease;
}

.login-background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: 0;
}

/* 网格背景 */
.grid-pattern {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(var(--grid-color) 1px, transparent 1px),
    linear-gradient(90deg, var(--grid-color) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}

@keyframes gridMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(50px, 50px);
  }
}

/* 缩放入场动画 */
.zoom-in-enter-active {
  animation: zoomIn 0.5s ease-out;
}

@keyframes zoomIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 滑动动画 */
.slide-right-enter-active {
  animation: slideRight 0.5s ease-out both;
}

@keyframes slideRight {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.slide-up-enter-active {
  animation: slideUp 0.5s ease-out both;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-box {
  width: 420px;
  background: var(--bg-secondary);
  border-radius: 16px;
  box-shadow: 0 4px 24px var(--shadow-color);
  z-index: 1;
  padding: 48px 40px;
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  background: var(--accent-color);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: var(--bg-secondary);
  font-size: 32px;
  transition: all 0.3s ease;
}

.logo-icon:hover {
  transform: scale(1.05);
}

.login-header h2 {
  margin: 0 0 8px 0;
  font-size: 26px;
  color: var(--text-primary);
  font-weight: 600;
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

.welcome-text {
  margin: 0 0 8px 0;
  font-size: 13px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 2px;
  transition: color 0.3s ease;
}

.typing-effect {
  height: 20px;
  font-size: 13px;
  color: var(--text-secondary);
  letter-spacing: 3px;
  transition: color 0.3s ease;
}

.cursor {
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 50% {
    opacity: 1;
  }
  51%, 100% {
    opacity: 0;
  }
}

.login-form {
  margin-bottom: 24px;
}

.input-with-animation :deep(.el-input__wrapper) {
  transition: all 0.3s ease;
  border-radius: 8px;
  background: var(--bg-primary);
  box-shadow: none;
  border: 1px solid var(--border-color);
}

.input-with-animation :deep(.el-input__wrapper:hover) {
  border-color: var(--accent-color);
}

.input-with-animation :deep(.el-input__wrapper.is-focus) {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px rgba(26, 26, 26, 0.1);
}

.dark-mode .input-with-animation :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.1);
}

.input-with-animation :deep(.el-input__inner) {
  color: var(--text-primary);
}

.input-with-animation :deep(.el-input__inner::placeholder) {
  color: var(--text-muted);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-options :deep(.el-checkbox__label) {
  color: var(--text-secondary);
}

.forgot-password {
  font-size: 13px;
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: var(--accent-color);
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 8px;
  letter-spacing: 3px;
  background: var(--accent-color);
  border: none;
  color: var(--bg-secondary);
  transition: all 0.3s ease;
}

.login-button:hover {
  background: var(--accent-hover);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px var(--shadow-color);
}

.login-button.is-loading {
  pointer-events: none;
}

.button-icon {
  margin-right: 8px;
}

.login-footer {
  text-align: center;
}

.divider {
  position: relative;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 35%;
  height: 1px;
  background: var(--border-color);
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider span {
  color: var(--text-muted);
  font-size: 12px;
  padding: 0 12px;
  background: var(--bg-secondary);
  transition: all 0.3s ease;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin: 20px 0;
}

.social-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
  background: var(--bg-primary);
  color: var(--text-secondary);
}

.social-icon:hover {
  border-color: var(--accent-color);
  color: var(--accent-color);
  transform: translateY(-2px);
}

.social-icon.wechat:hover {
  border-color: #07c160;
  color: #07c160;
}

.social-icon.work-wechat:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.social-icon.dingtalk:hover {
  border-color: #0089ff;
  color: #0089ff;
}

.copyright {
  margin: 20px 0 0 0;
  color: var(--text-muted);
  font-size: 11px;
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    width: 100%;
    margin: 0 20px;
    padding: 36px 24px;
  }
  
  .login-header h2 {
    font-size: 22px;
  }
}
</style>
