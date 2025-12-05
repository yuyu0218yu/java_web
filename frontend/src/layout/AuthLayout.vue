<template>
  <div class="login-container">
    <div class="login-background">
      <div class="grid-pattern"></div>
    </div>
    
    <transition name="zoom-in" appear>
      <div class="login-box">
        <div class="theme-toggle-wrapper">
          <ThemeToggle />
        </div>
        <slot></slot>
      </div>
    </transition>
  </div>
</template>

<script setup>
import ThemeToggle from '@/components/ThemeToggle.vue'
</script>

<style scoped>
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

.login-box {
  width: 420px;
  background: var(--bg-secondary);
  border-radius: 16px;
  box-shadow: 0 4px 24px var(--shadow-color);
  z-index: 1;
  padding: 48px 40px;
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
  position: relative;
}

.theme-toggle-wrapper {
  position: absolute;
  top: 20px;
  right: 20px;
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

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    width: 100%;
    margin: 0 20px;
    padding: 36px 24px;
  }
}
</style>
