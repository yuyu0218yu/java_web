<template>
  <div class="menus-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon"><Menu /></el-icon>
            <span>菜单管理</span>
            <el-tag type="info" size="small" effect="plain" round style="margin-left: 12px;">
              共 {{ getTotalMenus() }} 个菜单
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAdd()" class="action-btn primary-gradient">
              <el-icon><Plus /></el-icon>
              新增菜单
            </el-button>
            <el-button type="success" @click="handleExpandAll" class="action-btn">
              <el-icon><Expand /></el-icon>
              展开全部
            </el-button>
            <el-button type="warning" @click="handleCollapseAll" class="action-btn">
              <el-icon><Fold /></el-icon>
              折叠全部
            </el-button>
            <el-button type="info" @click="handleRefresh" class="action-btn">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 菜单树形表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="tableData"
          style="width: 100%"
          table-layout="auto"
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          :default-expand-all="false"
          ref="tableRef"
          highlight-current-row
          stripe
        >
          <el-table-column prop="menuName" label="菜单名称" width="220">
            <template #default="scope">
              <div class="menu-name-cell">
                <div class="menu-icon" :class="getTypeClass(scope.row.menuType)">
                  <el-icon v-if="getSafeIcon(scope.row.icon)">
                    <component :is="getSafeIcon(scope.row.icon)" />
                  </el-icon>
                  <el-icon v-else-if="scope.row.menuType === 'M'"><Folder /></el-icon>
                  <el-icon v-else-if="scope.row.menuType === 'C'"><Document /></el-icon>
                  <el-icon v-else><Pointer /></el-icon>
                </div>
                <span class="menu-name">{{ scope.row.menuName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="path" label="路由地址" min-width="150">
            <template #default="scope">
              <span v-if="scope.row.path" class="path-text">{{ scope.row.path }}</span>
              <span v-else class="no-path">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="component" label="组件路径" min-width="140">
            <template #default="scope">
              <span v-if="scope.row.component" class="component-text">{{ scope.row.component }}</span>
              <span v-else class="no-component">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="perms" label="权限标识" min-width="140">
            <template #default="scope">
              <el-tag v-if="scope.row.perms" effect="plain" size="small" type="info">
                {{ scope.row.perms }}
              </el-tag>
              <span v-else class="no-perms">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="menuType" label="类型" width="90" align="center">
            <template #default="scope">
              <el-tag :type="getTypeTagType(scope.row.menuType)" size="small" effect="light" round>
                {{ getTypeLabel(scope.row.menuType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderNum" label="排序" width="70" align="center">
            <template #default="scope">
              <el-tag type="info" size="small" effect="plain" round>
                {{ scope.row.orderNum }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visible" label="可见" width="80" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.visible === 1 ? 'success' : 'info'" size="small" effect="light">
                {{ scope.row.visible === 1 ? '显示' : '隐藏' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                active-color="#67C23A"
                inactive-color="#dcdfe6"
                @change="handleStatusChange(scope.row)"
                :loading="scope.row.statusLoading"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="新增子菜单" placement="top">
                  <el-button type="success" size="small" circle @click="handleAdd(scope.row)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-button type="warning" size="small" circle @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </transition>

    <!-- 菜单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      @close="handleDialogClose"
      class="menu-dialog"
      :close-on-click-modal="false"
    >
      <transition name="fade" appear>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="90px"
          style="max-width: 550px; margin: 0 auto;"
        >
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="上级菜单" prop="parentId">
                <el-tree-select
                  v-model="form.parentId"
                  :data="menuTreeOptions"
                  :props="treeSelectProps"
                  placeholder="请选择上级菜单"
                  clearable
                  check-strictly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单类型" prop="menuType">
                <el-radio-group v-model="form.menuType" class="type-radio-group">
                  <el-radio label="M" border>
                    <el-icon style="margin-right: 4px;"><Folder /></el-icon>
                    目录
                  </el-radio>
                  <el-radio label="C" border>
                    <el-icon style="margin-right: 4px;"><Document /></el-icon>
                    菜单
                  </el-radio>
                  <el-radio label="F" border>
                    <el-icon style="margin-right: 4px;"><Pointer /></el-icon>
                    按钮
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="菜单名称" prop="menuName">
                <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序" prop="orderNum">
                <el-input-number v-model="form.orderNum" :min="0" :max="999" style="width: 100%;" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType !== 'F'">
              <el-form-item label="路由地址" prop="path">
                <el-input v-model="form.path" placeholder="如：/users" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType === 'C'">
              <el-form-item label="组件路径" prop="component">
                <el-input v-model="form.component" placeholder="如：Users" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限标识" prop="perms">
                <el-input v-model="form.perms" placeholder="如：user:view" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType !== 'F'">
              <el-form-item label="图标" prop="icon">
                <el-input v-model="form.icon" placeholder="图标名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否可见" prop="visible">
                <el-radio-group v-model="form.visible">
                  <el-radio :label="1">显示</el-radio>
                  <el-radio :label="0">隐藏</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">
                    <el-icon style="color: #67C23A;"><CircleCheck /></el-icon>
                    正常
                  </el-radio>
                  <el-radio :label="0">
                    <el-icon style="color: #F56C6C;"><CircleClose /></el-icon>
                    停用
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </transition>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            <el-icon><Check /></el-icon>
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Expand, Fold, Refresh, Edit, Delete, Menu, Pointer, 
  Folder, Document, CircleCheck, CircleClose, Close, Check,
  House, User, Setting, UserFilled, Avatar, Key
} from '@element-plus/icons-vue'
import { menuApi } from '@/api'

// 允许的图标白名单
const allowedIcons = [
  'House', 'User', 'Setting', 'UserFilled', 'Avatar', 'Key', 'Menu',
  'Folder', 'Document', 'Pointer', 'Plus', 'Edit', 'Delete', 'Refresh',
  'CircleCheck', 'CircleClose', 'Close', 'Check', 'Expand', 'Fold'
]

// 验证并获取安全的图标名称
const getSafeIcon = (iconName) => {
  if (!iconName || iconName === '#') return null
  return allowedIcons.includes(iconName) ? iconName : null
}

// HTML实体编码，防止XSS
const escapeHtml = (text) => {
  if (!text) return ''
  const div = document.createElement('div')
  div.textContent = text
  return div.innerHTML
}

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const tableRef = ref()
const formRef = ref()

// 表单数据
const form = reactive({
  id: null,
  parentId: 0,
  menuType: 'C',
  menuName: '',
  path: '',
  component: '',
  perms: '',
  icon: '',
  orderNum: 0,
  visible: 1,
  status: 1
})

// 表格数据
const tableData = ref([])
const menuTreeOptions = ref([])

// 树形选择组件配置
const treeSelectProps = {
  children: 'children',
  label: 'menuName',
  value: 'id'
}

// 表单验证规则
const rules = {
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 获取菜单类型样式类
const getTypeClass = (type) => {
  const classMap = {
    'M': 'type-dir',
    'C': 'type-menu',
    'F': 'type-button'
  }
  return classMap[type] || ''
}

// 获取菜单总数
const getTotalMenus = () => {
  const countNodes = (nodes) => {
    let count = 0
    nodes.forEach(node => {
      count += 1
      if (node.children) {
        count += countNodes(node.children)
      }
    })
    return count
  }
  return countNodes(tableData.value)
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await menuApi.getMenuTree()
    const addStatusLoading = (nodes) => {
      return nodes.map(node => ({
        ...node,
        statusLoading: false,
        children: node.children ? addStatusLoading(node.children) : undefined
      }))
    }
    tableData.value = addStatusLoading(response.data || [])
    menuTreeOptions.value = buildTreeSelectOptions(response.data || [])
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载菜单数据失败')
  } finally {
    loading.value = false
  }
}

const buildTreeSelectOptions = (data) => {
  return [
    { id: 0, menuName: '根菜单', children: data }
  ]
}

const getTypeLabel = (type) => {
  const typeMap = {
    'M': '目录',
    'C': '菜单',
    'F': '按钮'
  }
  return typeMap[type] || type
}

const getTypeTagType = (type) => {
  const typeMap = {
    'M': 'primary',
    'C': 'success',
    'F': 'warning'
  }
  return typeMap[type] || 'info'
}

const handleAdd = (row = null) => {
  dialogTitle.value = row ? '新增子菜单' : '新增菜单'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    parentId: row ? row.id : 0,
    menuType: 'C',
    menuName: '',
    path: '',
    component: '',
    perms: '',
    icon: '',
    orderNum: 0,
    visible: 1,
    status: 1
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    menuType: row.menuType,
    menuName: row.menuName,
    path: row.path,
    component: row.component,
    perms: row.perms,
    icon: row.icon,
    orderNum: row.orderNum,
    visible: row.visible,
    status: row.status
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await menuApi.updateMenu(form.id, form)
      ElMessage.success('更新菜单成功')
    } else {
      await menuApi.createMenu(form)
      ElMessage.success('创建菜单成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    const hasChildren = row.children && row.children.length > 0
    const safeMenuName = escapeHtml(row.menuName)
    await ElMessageBox.confirm(
      `<div style="text-align: center;">
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除菜单 <strong>${safeMenuName}</strong> 吗？</p>
        ${hasChildren ? '<p style="color: #F56C6C; font-size: 13px;">⚠️ 该菜单存在子菜单，将一并删除</p>' : ''}
      </div>`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    await menuApi.deleteMenu(row.id)
    ElMessage.success('删除菜单成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  try {
    await menuApi.updateMenu(row.id, { status: row.status })
    ElMessage.success(`${row.status === 1 ? '启用' : '停用'}菜单成功`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    console.error('更新状态失败:', error)
    ElMessage.error('更新状态失败')
  } finally {
    row.statusLoading = false
  }
}

const handleExpandAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      const queue = [...tableData.value]
      while (queue.length > 0) {
        const row = queue.shift()
        tableRef.value.toggleRowExpansion(row, true)
        if (row.children && row.children.length > 0) {
          queue.push(...row.children)
        }
      }
    }
  })
}

const handleCollapseAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      const queue = [...tableData.value]
      while (queue.length > 0) {
        const row = queue.shift()
        tableRef.value.toggleRowExpansion(row, false)
        if (row.children && row.children.length > 0) {
          queue.push(...row.children)
        }
      }
    }
  })
}

const handleRefresh = () => {
  loadData()
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.menus-page {
  padding: 0;
}

/* 入场动画 */
.fade-slide-down-enter-active {
  animation: fadeSlideDown 0.5s ease-out;
}

.fade-slide-up-enter-active {
  animation: fadeSlideUp 0.5s ease-out;
  animation-delay: 0.1s;
  animation-fill-mode: both;
}

@keyframes fadeSlideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeSlideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.action-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.action-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.title-icon {
  font-size: 24px;
  margin-right: 8px;
  color: #409EFF;
}

.action-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.action-btn {
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.primary-gradient {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
  border: none;
}

.table-card {
  min-height: 400px;
  border-radius: 12px;
}

/* 菜单名称单元格 */
.menu-name-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.menu-icon.type-dir {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
}

.menu-icon.type-menu {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.menu-icon.type-button {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
}

.menu-name {
  font-weight: 500;
}

.path-text,
.component-text {
  font-family: monospace;
  font-size: 13px;
  color: #606266;
}

.no-path,
.no-component,
.no-perms {
  color: #c0c4cc;
}

.operation-buttons {
  display: flex;
  gap: 8px;
}

.operation-buttons .el-button {
  transition: all 0.3s ease;
}

.operation-buttons .el-button:hover {
  transform: scale(1.1);
}

/* 对话框样式 */
.menu-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.type-radio-group {
  display: flex;
  gap: 16px;
}

.type-radio-group :deep(.el-radio.is-bordered) {
  padding: 10px 20px;
  border-radius: 8px;
  margin-right: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 淡入动画 */
.fade-enter-active {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
