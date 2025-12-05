<template>
  <AuthLayout>
    <div class="login-header">
      <div class="logo-icon">
        <el-icon><UserFilled /></el-icon>
      </div>
      <h2>用户注册</h2>
      <p class="welcome-text">Create Your Account</p>
    </div>
    
    <el-form
      ref="registerFormRef"
      :model="registerForm"
      :rules="registerRules"
      class="login-form"
      size="large"
    >
      <transition name="slide-right" appear>
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
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
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            clearable
            class="input-with-animation"
          />
        </el-form-item>
      </transition>

      <transition name="slide-right" appear style="animation-delay: 0.15s">
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            :prefix-icon="Lock"
            show-password
            clearable
            class="input-with-animation"
          />
        </el-form-item>
      </transition>
      
      <transition name="slide-right" appear style="animation-delay: 0.2s">
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="邮箱（选填）"
            :prefix-icon="Message"
            clearable
            class="input-with-animation"
          />
        </el-form-item>
      </transition>

      <transition name="slide-right" appear style="animation-delay: 0.25s">
        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="手机号（选填）"
            :prefix-icon="Phone"
            clearable
            class="input-with-animation"
            @keyup.enter="handleRegister"
          />
        </el-form-item>
      </transition>
      
      <transition name="slide-up" appear style="animation-delay: 0.3s">
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :class="{ 'is-loading': loading }"
            :loading="loading"
            @click="handleRegister"
          >
            <template v-if="!loading">
              <el-icon class="button-icon"><UserFilled /></el-icon>
              <span>注 册</span>
            </template>
            <template v-else>
              <span>注册中...</span>
            </template>
          </el-button>
        </el-form-item>
      </transition>
    </el-form>
    
    <div class="login-footer">
      <p class="login-link">
        已有账号？ <router-link to="/login">立即登录</router-link>
      </p>
      <p class="copyright">© 2025 用户权限管理系统 v1.0.0</p>
    </div>
  </AuthLayout>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AuthLayout from '@/layout/AuthLayout.vue'
import { User, Lock, UserFilled, Message, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const registerFormRef = ref()

// 注册表单
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: ''
})

// 验证确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 方法
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    const success = await authStore.register({
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email || null,
      phone: registerForm.phone || null
    })
    
    if (success) {
      router.push('/login')
    }
  } catch (error) {
    console.error('注册失败:', error)
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
})

onUnmounted(() => {
})
</script>

<style scoped>
.login-header {
  text-align: center;
  margin-bottom: 32px;
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
  margin: 0;
  font-size: 13px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 2px;
  transition: color 0.3s ease;
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

.login-link {
  margin: 0 0 16px 0;
  color: var(--text-secondary);
  font-size: 14px;
}

.login-link a {
  color: var(--accent-color);
  text-decoration: none;
  font-weight: 500;
}

.login-link a:hover {
  text-decoration: underline;
}

.copyright {
  margin: 0;
  color: var(--text-muted);
  font-size: 11px;
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-header h2 {
    font-size: 22px;
  }
}
</style>
