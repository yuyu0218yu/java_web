/**
 * 数据范围常量
 * 与后端 DataScope 枚举保持一致
 */
export const DATA_SCOPE = {
  ALL: 1,              // 全部数据
  DEPT_AND_CHILD: 2,   // 本部门及下级
  DEPT_ONLY: 3,        // 仅本部门
  SELF_ONLY: 4         // 仅本人
}

/**
 * 数据范围标签
 */
export const DATA_SCOPE_LABELS = {
  [DATA_SCOPE.ALL]: '全部',
  [DATA_SCOPE.DEPT_AND_CHILD]: '本部门及下级',
  [DATA_SCOPE.DEPT_ONLY]: '仅本部门',
  [DATA_SCOPE.SELF_ONLY]: '仅本人'
}

/**
 * 数据范围标签类型（Element Plus Tag type）
 */
export const DATA_SCOPE_TAG_TYPES = {
  [DATA_SCOPE.ALL]: 'danger',
  [DATA_SCOPE.DEPT_AND_CHILD]: 'warning',
  [DATA_SCOPE.DEPT_ONLY]: 'primary',
  [DATA_SCOPE.SELF_ONLY]: 'info'
}

/**
 * 数据范围详细说明
 */
export const DATA_SCOPE_TOOLTIPS = {
  [DATA_SCOPE.ALL]: '可以查看和管理所有部门的数据',
  [DATA_SCOPE.DEPT_AND_CHILD]: '只能查看和管理用户所在部门及其下级部门的数据',
  [DATA_SCOPE.DEPT_ONLY]: '只能查看和管理用户所在部门的数据',
  [DATA_SCOPE.SELF_ONLY]: '只能查看和管理自己的数据'
}

/**
 * 数据范围完整说明（用于角色管理页面）
 */
export const DATA_SCOPE_HELP = {
  [DATA_SCOPE.ALL]: '该角色可以查看和管理系统中所有部门的数据，拥有最高的数据访问权限',
  [DATA_SCOPE.DEPT_AND_CHILD]: '该角色可以查看和管理用户所在部门及其所有下级部门的数据。例如：技术部的角色可以管理后端组、前端组等下级部门',
  [DATA_SCOPE.DEPT_ONLY]: '该角色只能查看和管理用户所在部门的数据，不能访问其他部门或下级部门的数据',
  [DATA_SCOPE.SELF_ONLY]: '该角色只能查看和管理自己创建的数据，无法访问其他用户的数据'
}

/**
 * 获取数据范围标签
 */
export function getDataScopeLabel(dataScope) {
  return DATA_SCOPE_LABELS[dataScope] || '未设置'
}

/**
 * 获取数据范围标签类型
 */
export function getDataScopeTagType(dataScope) {
  return DATA_SCOPE_TAG_TYPES[dataScope] || ''
}

/**
 * 获取数据范围提示信息
 */
export function getDataScopeTooltip(dataScope) {
  return DATA_SCOPE_TOOLTIPS[dataScope] || '未设置数据范围'
}

/**
 * 获取数据范围帮助信息
 */
export function getDataScopeHelp(dataScope) {
  return DATA_SCOPE_HELP[dataScope] || '请选择数据范围'
}
