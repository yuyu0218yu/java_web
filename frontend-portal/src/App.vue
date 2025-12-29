<template>
  <div class="app-container">
    <!-- 顶部导航 -->
    <header class="app-header">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <img src="@/assets/logo.svg" alt="logo" class="logo-img" />
          <span class="logo-text">张家界旅游</span>
        </div>

        <nav class="nav-menu">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/scenic" class="nav-item">景点门票</router-link>
          <router-link to="/guide" class="nav-item">旅游攻略</router-link>
        </nav>

        <div class="header-right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索景点、攻略..."
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <template v-if="authStore.isAuthenticated">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="authStore.user?.avatar">
                  {{ authStore.user?.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="username">{{ authStore.user?.nickname || authStore.user?.username }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                  <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
                  <el-dropdown-item command="guides">我的攻略</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="app-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 底部 -->
    <footer class="app-footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">联系方式</a>
          <a href="#">服务协议</a>
          <a href="#">隐私政策</a>
        </div>
        <div class="copyright">
          © 2024 张家界旅游 版权所有
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const searchKeyword = ref('')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
  }
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'orders':
      router.push('/user/orders')
      break
    case 'favorites':
      router.push('/user/favorites')
      break
    case 'guides':
      router.push('/user/guides')
      break
    case 'logout':
      authStore.logout()
      router.push('/')
      break
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;

  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .logo {
    display: flex;
    align-items: center;
    cursor: pointer;

    .logo-img {
      height: 36px;
      margin-right: 10px;
    }

    .logo-text {
      font-size: 20px;
      font-weight: bold;
      color: #409eff;
    }
  }

  .nav-menu {
    display: flex;
    gap: 32px;

    .nav-item {
      color: #333;
      text-decoration: none;
      font-size: 15px;
      padding: 8px 0;
      border-bottom: 2px solid transparent;
      transition: all 0.3s;

      &:hover,
      &.router-link-active {
        color: #409eff;
        border-bottom-color: #409eff;
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 16px;

    .search-input {
      width: 200px;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .username {
        font-size: 14px;
        color: #333;
      }
    }
  }
}

.app-main {
  flex: 1;
  background: #f5f7fa;
}

.app-footer {
  background: #333;
  color: #fff;
  padding: 30px 20px;

  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    text-align: center;
  }

  .footer-links {
    margin-bottom: 16px;

    a {
      color: #ccc;
      text-decoration: none;
      margin: 0 16px;
      font-size: 14px;

      &:hover {
        color: #fff;
      }
    }
  }

  .copyright {
    color: #999;
    font-size: 13px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
