/**
 * 系统组件配置
 * 优先从后端 API 获取，失败时使用本地默认值
 */

import { configApi } from '@/api'

// 本地默认组件列表（后端不可用时的备用）
const defaultComponents = [
  { label: 'Dashboard - 仪表盘', value: 'Dashboard' },
  { label: 'Welcome - 欢迎页', value: 'Welcome' },
  { label: 'Users - 用户管理', value: 'Users' },
  { label: 'Roles - 角色管理', value: 'Roles' },
  { label: 'Permissions - 权限管理', value: 'Permissions' },
  { label: 'Menus - 菜单管理', value: 'Menus' },
  { label: 'Depts - 部门管理', value: 'Depts' },
  { label: 'Profile - 个人中心', value: 'Profile' },
  { label: 'Generator - 代码生成器', value: 'Generator' },
]

// 本地默认图标列表（后端不可用时的备用）
const defaultIcons = [
  { label: 'House - 首页', value: 'House' },
  { label: 'User - 用户', value: 'User' },
  { label: 'Setting - 设置', value: 'Setting' },
  { label: 'Menu - 菜单', value: 'Menu' },
  { label: 'Key - 钥匙', value: 'Key' },
  { label: 'OfficeBuilding - 组织', value: 'OfficeBuilding' },
  { label: 'Document - 文档', value: 'Document' },
  { label: 'Folder - 文件夹', value: 'Folder' },
]

// 本地默认权限标识列表
const defaultPermissions = [
  { label: '用户查看', value: 'user:view' },
  { label: '用户新增', value: 'user:create' },
  { label: '角色查看', value: 'role:view' },
  { label: '菜单查看', value: 'menu:view' },
]

// 缓存
let cachedComponents = null
let cachedIcons = null
let cachedPermissions = null

/**
 * 获取组件列表（从后端 API）
 */
export async function fetchComponents() {
  if (cachedComponents) return cachedComponents
  try {
    const res = await configApi.getComponents()
    cachedComponents = res.data || defaultComponents
    return cachedComponents
  } catch (error) {
    console.warn('获取组件列表失败，使用本地默认值:', error)
    return defaultComponents
  }
}

/**
 * 获取图标列表（从后端 API）
 */
export async function fetchIcons() {
  if (cachedIcons) return cachedIcons
  try {
    const res = await configApi.getIcons()
    cachedIcons = res.data || defaultIcons
    return cachedIcons
  } catch (error) {
    console.warn('获取图标列表失败，使用本地默认值:', error)
    return defaultIcons
  }
}

/**
 * 获取权限标识列表（从后端 API）
 */
export async function fetchPermissions() {
  if (cachedPermissions) return cachedPermissions
  try {
    const res = await configApi.getPermissions()
    cachedPermissions = res.data || defaultPermissions
    return cachedPermissions
  } catch (error) {
    console.warn('获取权限标识列表失败，使用本地默认值:', error)
    return defaultPermissions
  }
}

/**
 * 清除缓存（如需刷新配置时调用）
 */
export function clearConfigCache() {
  cachedComponents = null
  cachedIcons = null
  cachedPermissions = null
}

// 同步导出默认值（兼容旧代码）
export const viewComponents = defaultComponents
export const iconList = defaultIcons
export const permissionList = defaultPermissions
