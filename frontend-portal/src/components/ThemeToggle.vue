<template>
  <div
    class="theme-toggle"
    @click="toggleTheme"
    :class="{ 'is-dark': isDark }"
    :title="isDark ? '切换亮色模式' : '切换暗色模式'"
  >
    <el-icon v-if="isDark"><Moon /></el-icon>
    <el-icon v-else><Sunny /></el-icon>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Moon, Sunny } from '@element-plus/icons-vue'

const isDark = ref(false)

const toggleTheme = () => {
  isDark.value = !isDark.value
  const html = document.documentElement
  if (isDark.value) {
    html.classList.add('dark')
    localStorage.setItem('theme', 'dark')
  } else {
    html.classList.remove('dark')
    localStorage.setItem('theme', 'light')
  }
}

const initTheme = () => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    isDark.value = savedTheme === 'dark'
    const html = document.documentElement
    if (isDark.value) {
      html.classList.add('dark')
    } else {
      html.classList.remove('dark')
    }
  } else {
    // 默认跟随系统
    isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    if (isDark.value) {
      document.documentElement.classList.add('dark')
    }
  }
}

onMounted(() => {
  initTheme()
})
</script>

<style scoped>
.theme-toggle {
  cursor: pointer;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  transition: all 0.3s;
}

.theme-toggle:hover {
  background: var(--bg-primary);
  color: var(--accent-color);
}
</style>
