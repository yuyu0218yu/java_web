<template>
  <div class="user-center-page">
    <div class="container page-content">
      <el-row :gutter="20">
        <!-- 侧边栏 -->
        <el-col :span="5">
          <div class="user-sidebar card">
            <div class="user-info">
              <el-avatar :size="64" :src="authStore.user?.avatar">
                {{ authStore.user?.nickname?.charAt(0) || 'U' }}
              </el-avatar>
              <h3 class="username">{{ authStore.user?.nickname || authStore.user?.username }}</h3>
            </div>
            <el-menu
              :default-active="activeMenu"
              @select="handleMenuSelect"
            >
              <el-menu-item index="profile">
                <el-icon><User /></el-icon>
                <span>个人资料</span>
              </el-menu-item>
              <el-menu-item index="orders">
                <el-icon><Tickets /></el-icon>
                <span>我的订单</span>
              </el-menu-item>
              <el-menu-item index="favorites">
                <el-icon><Star /></el-icon>
                <span>我的收藏</span>
              </el-menu-item>
              <el-menu-item index="guides">
                <el-icon><Document /></el-icon>
                <span>我的攻略</span>
              </el-menu-item>
            </el-menu>
          </div>
        </el-col>

        <!-- 内容区 -->
        <el-col :span="19">
          <div class="user-content card">
            <router-view />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activeMenu = computed(() => {
  const path = route.path
  if (path.includes('profile')) return 'profile'
  if (path.includes('orders')) return 'orders'
  if (path.includes('favorites')) return 'favorites'
  if (path.includes('guides')) return 'guides'
  return 'profile'
})

const handleMenuSelect = (index) => {
  router.push(`/user/${index}`)
}
</script>

<style lang="scss" scoped>
.user-center-page {
  .user-sidebar {
    padding: 24px;

    .user-info {
      text-align: center;
      padding-bottom: 20px;
      border-bottom: 1px solid #eee;
      margin-bottom: 16px;

      .el-avatar {
        margin-bottom: 12px;
      }

      .username {
        font-size: 16px;
        font-weight: 500;
      }
    }

    .el-menu {
      border-right: none;
    }
  }

  .user-content {
    min-height: 500px;
    padding: 24px;
  }
}
</style>
