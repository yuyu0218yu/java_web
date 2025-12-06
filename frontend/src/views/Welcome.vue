<template>
  <div class="welcome-page">
    <div class="hero">
      <h1>欢迎使用用户权限管理系统</h1>
      <p class="subtitle">
        这是公共欢迎页，登录后将根据权限展示可访问的功能。
      </p>
      <div class="actions">
        <el-button
          v-if="!isAuthenticated"
          type="primary"
          @click="$router.push('/login')"
        >
          前往登录
        </el-button>
        <template v-else>
          <el-button
            v-if="canAccessDashboard"
            type="primary"
            @click="$router.push('/dashboard')"
          >
            进入仪表盘
          </el-button>
          <el-tag v-else type="info" effect="plain">已登录，暂无仪表盘权限</el-tag>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const isAuthenticated = computed(() => authStore.isAuthenticated)
const canAccessDashboard = computed(() => {
  const userRole = authStore.user?.roleCode
  const userPermissions = authStore.permissions || []
  // 与路由守卫规则保持一致
  const hasRole = userRole === 'SUPER_ADMIN'
  const hasPerm = userPermissions.includes('dashboard:view') || userPermissions.includes('*')
  return hasRole || hasPerm
})
</script>

<style scoped>
.welcome-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 120px);
  padding: 40px 20px;
}
.hero {
  max-width: 720px;
  width: 100%;
  text-align: center;
  background: var(--el-bg-color);
  padding: 48px 36px;
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  border: 1px solid var(--el-border-color-light);
}
.hero h1 {
  margin: 0 0 16px;
  font-size: 28px;
  font-weight: 700;
  color: var(--el-text-color-primary);
}
.subtitle {
  margin: 0 0 32px;
  font-size: 16px;
  color: var(--el-text-color-regular);
}
.actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}
</style>
