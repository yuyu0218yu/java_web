<template>
  <div class="login-container">
    <div class="login-background">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="particles">
        <div v-for="n in 20" :key="n" class="particle" :style="getParticleStyle(n)"></div>
      </div>
    </div>
    
    <transition name="zoom-in" appear>
      <div class="login-box">
        <div class="login-header">
          <div class="logo-icon">
            <el-icon><Lock /></el-icon>
          </div>
          <h2>权限管理系统</h2>
          <p class="welcome-text">Welcome Back</p>
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

// 打字机效果文字
const fullText = '安全 · 高效 · 便捷'
let typingIndex = 0
let typingTimer = null

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

// 获取粒子样式
const getParticleStyle = (index) => {
  const size = Math.random() * 8 + 3
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 5}s`,
    animationDuration: `${Math.random() * 10 + 10}s`
  }
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

// 生命周期
onMounted(() => {
  if (authStore.isAuthenticated) {
    router.push('/dashboard')
  }
  startTyping()
})

onUnmounted(() => {
  if (typingTimer) {
    clearInterval(typingTimer)
  }
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

.login-background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: 0;
}

.shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  animation: float 20s infinite ease-in-out;
}

.shape-1 {
  top: -100px;
  left: -100px;
  width: 500px;
  height: 500px;
  background: linear-gradient(to right, #8e2de2, #4a00e0);
  opacity: 0.7;
  animation-delay: 0s;
}

.shape-2 {
  bottom: -150px;
  right: -100px;
  width: 600px;
  height: 600px;
  background: linear-gradient(to left, #00c6ff, #0072ff);
  opacity: 0.6;
  animation-delay: -5s;
}

.shape-3 {
  top: 40%;
  left: 40%;
  width: 300px;
  height: 300px;
  background: linear-gradient(to bottom, #ff0099, #493240);
  opacity: 0.4;
  transform: translate(-50%, -50%);
  animation-delay: -10s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(50px, -50px) rotate(5deg);
  }
  50% {
    transform: translate(-30px, 30px) rotate(-5deg);
  }
  75% {
    transform: translate(20px, 20px) rotate(3deg);
  }
}

/* 粒子效果 */
.particles {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  animation: particleFloat linear infinite;
}

@keyframes particleFloat {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) rotate(720deg);
    opacity: 0;
  }
}

/* 缩放入场动画 */
.zoom-in-enter-active {
  animation: zoomIn 0.5s ease-out;
}

@keyframes zoomIn {
  from {
    opacity: 0;
    transform: scale(0.8);
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
    transform: translateX(-30px);
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
  width: 440px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  z-index: 1;
  padding: 50px 40px;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: white;
  font-size: 36px;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
  animation: logoFloat 3s ease-in-out infinite;
}

@keyframes logoFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.login-header h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  color: #303133;
  font-weight: 700;
  letter-spacing: 2px;
}

.welcome-text {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #909399;
}

.typing-effect {
  height: 20px;
  font-size: 13px;
  color: #667eea;
  letter-spacing: 4px;
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
  border-radius: 10px;
}

.input-with-animation :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.input-with-animation :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.3) inset;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.forgot-password {
  font-size: 14px;
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: #764ba2;
  text-decoration: underline;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
  transition: left 0.5s;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.login-button.is-loading {
  pointer-events: none;
}

.button-icon {
  margin-right: 8px;
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-10deg);
  }
  75% {
    transform: rotate(10deg);
  }
}

.login-footer {
  text-align: center;
}

.divider {
  position: relative;
  margin: 20px 0;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 35%;
  height: 1px;
  background: #e4e7ed;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider span {
  color: #909399;
  font-size: 12px;
  padding: 0 12px;
  background: white;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 20px 0;
}

.social-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 20px;
  transition: all 0.3s ease;
}

.social-icon.wechat {
  background: #07c160;
  color: white;
}

.social-icon.work-wechat {
  background: #1890ff;
  color: white;
}

.social-icon.dingtalk {
  background: #0089ff;
  color: white;
}

.social-icon:hover {
  transform: translateY(-3px) scale(1.1);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.copyright {
  margin: 16px 0 0 0;
  color: #c0c4cc;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    width: 100%;
    margin: 0 20px;
    padding: 40px 24px;
  }
  
  .login-header h2 {
    font-size: 24px;
  }
}
</style>
