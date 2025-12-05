import { useMenuStore } from '@/stores/menu'

/**
 * 权限指令
 * 使用方式：v-permission="'user:create'" 或 v-permission="['user:create', 'user:update']"
 */
export const permission = {
  mounted(el, binding) {
    const menuStore = useMenuStore()
    const { value } = binding
    
    if (value) {
      const permissions = menuStore.permissions
      const hasPermission = checkPermission(permissions, value)
      
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  }
}

/**
 * 角色指令
 * 使用方式：v-role="'ADMIN'" 或 v-role="['ADMIN', 'SUPER_ADMIN']"
 */
export const role = {
  mounted(el, binding) {
    const { value } = binding
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    const roleCode = user.roleCode
    
    if (value) {
      const hasRole = Array.isArray(value) 
        ? value.includes(roleCode) 
        : value === roleCode
      
      if (!hasRole) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  }
}

/**
 * 检查权限
 */
function checkPermission(permissions, required) {
  if (!permissions || permissions.length === 0) {
    return false
  }
  
  // 超级权限
  if (permissions.includes('*')) {
    return true
  }
  
  // 数组形式：满足任一即可
  if (Array.isArray(required)) {
    return required.some(p => permissions.includes(p))
  }
  
  // 字符串形式
  return permissions.includes(required)
}

/**
 * 注册指令
 */
export function setupPermissionDirective(app) {
  app.directive('permission', permission)
  app.directive('role', role)
}

export default {
  install(app) {
    setupPermissionDirective(app)
  }
}
