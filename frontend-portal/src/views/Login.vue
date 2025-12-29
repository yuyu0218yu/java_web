<template>
  <AuthLayout>
    <h2 class="login-title">欢迎登录</h2>
    <p class="login-subtitle">张家界旅游，发现最美风景</p>

    <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleLogin">
      <el-form-item prop="username">
        <el-input
          v-model="form.username"
          placeholder="请输入用户名"
          prefix-icon="User"
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

      <el-form-item>
        <el-button
          type="primary"
          size="large"
          :loading="loading"
          @click="handleLogin"
          class="login-btn"
        >
          登 录
        </el-button>
      </el-form-item>
    </el-form>

    <div class="login-footer">
      <span>还没有账号？</span>
      <router-link to="/register">立即注册</router-link>
    </div>
  </AuthLayout>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import AuthLayout from '@/layout/AuthLayout.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await authStore.login(form)
    if (res.code === 200) {
      ElMessage.success('登录成功')
      const redirect = route.query.redirect || '/'
      router.push(redirect)
    }
  } catch (error) {
    console.error('登录失败', error)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-title {
  font-size: 26px;
  font-weight: 600;
  color: var(--text-primary);
  text-align: center;
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.login-subtitle {
  font-size: 13px;
  color: var(--text-muted);
  text-align: center;
  margin-bottom: 32px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.login-btn {
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

.login-footer {
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
