<template>
  <!-- 目录类型：有子菜单 -->
  <el-sub-menu v-if="item.menuType === 'M' && item.children && item.children.length > 0" :index="item.path || String(item.id)" class="menu-item-animated">
    <template #title>
      <el-icon class="menu-icon">
        <component :is="getIcon(item.icon)" />
      </el-icon>
      <span class="menu-title">{{ item.menuName }}</span>
    </template>
    <menu-item v-for="child in item.children" :key="child.id" :item="child" />
  </el-sub-menu>
  
  <!-- 菜单类型：叶子节点 -->
  <el-menu-item v-else-if="item.menuType === 'C'" :index="item.path" class="menu-item-animated">
    <el-icon class="menu-icon">
      <component :is="getIcon(item.icon)" />
    </el-icon>
    <template #title>
      <span class="menu-title">{{ item.menuName }}</span>
    </template>
  </el-menu-item>
</template>

<script setup>
import { defineProps, markRaw } from 'vue'
import {
  House, User, UserFilled, Setting, Key, Avatar, Menu, Document,
  HomeFilled, Folder, List, Edit, Delete, Search, Plus, Refresh,
  Lock, Unlock, View, Hide, Upload, Download, Share, Star, Bell
} from '@element-plus/icons-vue'

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

// 图标映射
const iconMap = {
  House: markRaw(House),
  User: markRaw(User),
  UserFilled: markRaw(UserFilled),
  Setting: markRaw(Setting),
  Key: markRaw(Key),
  Avatar: markRaw(Avatar),
  Menu: markRaw(Menu),
  Document: markRaw(Document),
  HomeFilled: markRaw(HomeFilled),
  Folder: markRaw(Folder),
  List: markRaw(List),
  Edit: markRaw(Edit),
  Delete: markRaw(Delete),
  Search: markRaw(Search),
  Plus: markRaw(Plus),
  Refresh: markRaw(Refresh),
  Lock: markRaw(Lock),
  Unlock: markRaw(Unlock),
  View: markRaw(View),
  Hide: markRaw(Hide),
  Upload: markRaw(Upload),
  Download: markRaw(Download),
  Share: markRaw(Share),
  Star: markRaw(Star),
  Bell: markRaw(Bell)
}

const getIcon = (iconName) => {
  return iconMap[iconName] || iconMap['Document']
}
</script>

<style scoped>
.menu-item-animated {
  margin-bottom: 4px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu-item-animated:hover {
  background: var(--hover-bg) !important;
}

.menu-icon {
  font-size: 18px;
  margin-right: 8px;
}

.menu-title {
  font-size: 14px;
  font-weight: 500;
}
</style>
