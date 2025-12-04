<template>
  <div class="login-container">
    <div class="login-background">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>
    
    <div class="login-box">
      <div class="login-header">
        <div class="logo-icon">
          <el-icon><Lock /></el-icon>
        </div>
        <h2>权限管理系统</h2>
        <p>Welcome Back</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        size="large"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <div class="form-options">
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
          <a href="#" class="forgot-password">忘记密码？</a>
        </div>
        
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2025 用户权限管理系统</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const loading = ref(false)
const loginFormRef = ref()

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
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-color: #f0f2f5;
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
}

.shape-1 {
  top: -100px;
  left: -100px;
  width: 500px;
  height: 500px;
  background: linear-gradient(to right, #8e2de2, #4a00e0);
  opacity: 0.7;
}

.shape-2 {
  bottom: -150px;
  right: -100px;
  width: 600px;
  height: 600px;
  background: linear-gradient(to left, #00c6ff, #0072ff);
  opacity: 0.6;
}

.shape-3 {
  top: 40%;
  left: 40%;
  width: 300px;
  height: 300px;
  background: linear-gradient(to bottom, #ff0099, #493240);
  opacity: 0.4;
  transform: translate(-50%, -50%);
}

.login-box {
  width: 420px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  z-index: 1;
  padding: 40px;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #409EFF 0%, #36D1DC 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
  font-size: 32px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.login-header h2 {
  margin: 0 0 10px 0;
  font-size: 28px;
  color: #303133;
  font-weight: 700;
}

.login-header p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.login-form {
  margin-bottom: 20px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.forgot-password {
  font-size: 14px;
  color: #409EFF;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  letter-spacing: 2px;
  background: linear-gradient(90deg, #409EFF 0%, #36D1DC 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.login-footer {
  text-align: center;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  padding-top: 20px;
}

.login-footer p {
  margin: 0;
  color: #909399;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    width: 100%;
    margin: 0 20px;
    padding: 30px 20px;
  }
}
</style>
