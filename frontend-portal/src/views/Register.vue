<template>
  <AuthLayout>
    <h2 class="register-title">用户注册</h2>
    <p class="register-subtitle">加入我们，开启美好旅程</p>

    <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleRegister">
      <el-form-item prop="username">
        <el-input
          v-model="form.username"
          placeholder="请输入用户名"
          prefix-icon="User"
          size="large"
        />
      </el-form-item>

      <el-form-item prop="nickname">
        <el-input
          v-model="form.nickname"
          placeholder="请输入昵称"
          prefix-icon="Avatar"
          size="large"
        />
      </el-form-item>

      <el-form-item prop="email">
        <el-input
          v-model="form.email"
          placeholder="请输入邮箱"
          prefix-icon="Message"
          size="large"
        />
      </el-form-item>

      <el-form-item prop="phone">
        <el-input
          v-model="form.phone"
          placeholder="请输入手机号"
          prefix-icon="Phone"
          size="large"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          prefix-icon="Lock"
          size="large"
          show-password
        />
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="请确认密码"
          prefix-icon="Lock"
          size="large"
          show-password
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          size="large"
          :loading="loading"
          @click="handleRegister"
          class="register-btn"
        >
          注 册
        </el-button>
      </el-form-item>
    </el-form>

    <div class="register-footer">
      <span>已有账号？</span>
      <router-link to="/login">立即登录</router-link>
    </div>
  </AuthLayout>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import AuthLayout from '@/layout/AuthLayout.vue'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await authStore.register(form)
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    }
  } catch (error) {
    console.error('注册失败', error)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.register-title {
  font-size: 26px;
  font-weight: 600;
  color: var(--text-primary);
  text-align: center;
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.register-subtitle {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  margin-bottom: 32px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 3px;
  background: var(--accent-color);
  border: none;
  color: var(--bg-secondary);
  border-radius: 8px;

  &:hover {
    background: var(--accent-hover);
  }
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-secondary);

  a {
    color: var(--text-primary);
    margin-left: 4px;
    font-weight: 500;

    &:hover {
      text-decoration: underline;
    }
  }
}

:deep(.el-input__wrapper) {
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  box-shadow: none;
  border-radius: 8px;

  &:hover, &.is-focus {
    border-color: var(--accent-color);
  }
}

:deep(.el-input__inner) {
  color: var(--text-primary);

  &::placeholder {
    color: var(--text-muted);
  }
}

:deep(.el-input__prefix) {
  color: var(--text-muted);
}
</style>
