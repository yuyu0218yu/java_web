<template>
  <div class="permissions-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon"><Key /></el-icon>
            <span>权限管理</span>
            <el-tag type="info" size="small" effect="plain" round style="margin-left: 12px;">
              共 {{ getTotalPermissions() }} 个权限
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAdd()" class="action-btn primary-gradient">
              <el-icon><Plus /></el-icon>
              新增权限
            </el-button>
            <el-button type="success" @click="handleExpandAll" class="action-btn">
              <el-icon><Expand /></el-icon>
              展开全部
            </el-button>
            <el-button type="warning" @click="handleCollapseAll" class="action-btn">
              <el-icon><Fold /></el-icon>
              折叠全部
            </el-button>
            <el-button type="info" @click="handleExport" class="action-btn">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 权限树形表格 -->
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
          <el-table-column prop="permissionName" label="权限名称" width="250">
            <template #default="scope">
              <div class="permission-name-cell">
                <div class="permission-icon" :class="getTypeClass(scope.row.resourceType)">
                  <el-icon v-if="scope.row.resourceType === 1"><Menu /></el-icon>
                  <el-icon v-else-if="scope.row.resourceType === 2"><Pointer /></el-icon>
                  <el-icon v-else><Connection /></el-icon>
                </div>
                <span class="permission-name">{{ scope.row.permissionName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="permissionCode" label="权限编码" width="180">
            <template #default="scope">
              <el-tag effect="plain" size="small" type="info">
                <el-icon style="margin-right: 4px;"><Tickets /></el-icon>
                {{ scope.row.permissionCode }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="resourceType" label="类型" width="100">
            <template #default="scope">
              <el-tag :type="getTypeTagType(scope.row.resourceType)" size="small" effect="light" round>
                {{ getTypeLabel(scope.row.resourceType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="path" label="路径" width="160">
            <template #default="scope">
              <span v-if="scope.row.path" class="path-text">{{ scope.row.path }}</span>
              <span v-else class="no-path">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="component" label="组件" width="160">
            <template #default="scope">
              <span v-if="scope.row.component" class="component-text">{{ scope.row.component }}</span>
              <span v-else class="no-component">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="icon" label="图标" width="80" align="center">
            <template #default="scope">
              <el-icon v-if="scope.row.icon" :size="20" class="icon-preview">
                <component :is="scope.row.icon" />
              </el-icon>
              <span v-else class="no-icon">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="80" align="center">
            <template #default="scope">
              <el-tag type="info" size="small" effect="plain" round>
                {{ scope.row.sortOrder }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90">
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
                <el-tooltip content="新增子权限" placement="top">
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

    <!-- 权限表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      @close="handleDialogClose"
      class="permission-dialog"
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
              <el-form-item label="上级权限" prop="parentId">
                <el-tree-select
                  v-model="form.parentId"
                  :data="permissionTreeOptions"
                  :props="treeSelectProps"
                  placeholder="请选择上级权限"
                  clearable
                  check-strictly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="权限类型" prop="resourceType">
                <el-radio-group v-model="form.resourceType" class="type-radio-group">
                  <el-radio :label="1" border>
                    <el-icon style="margin-right: 4px;"><Menu /></el-icon>
                    菜单
                  </el-radio>
                  <el-radio :label="2" border>
                    <el-icon style="margin-right: 4px;"><Pointer /></el-icon>
                    按钮
                  </el-radio>
                  <el-radio :label="3" border>
                    <el-icon style="margin-right: 4px;"><Connection /></el-icon>
                    接口
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限名称" prop="permissionName">
                <el-input v-model="form.permissionName" placeholder="请输入权限名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限编码" prop="permissionCode">
                <el-input v-model="form.permissionCode" placeholder="如：user:list" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.resourceType === 1">
              <el-form-item label="路径" prop="path">
                <el-input v-model="form.path" placeholder="如：/users" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.resourceType === 1">
              <el-form-item label="组件" prop="component">
                <el-input v-model="form.component" placeholder="如：views/Users" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.resourceType === 1">
              <el-form-item label="图标" prop="icon">
                <el-input v-model="form.icon" placeholder="图标名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序" prop="sortOrder">
                <el-input-number v-model="form.sortOrder" :min="0" :max="999" style="width: 100%;" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">
                    <el-icon style="color: #67C23A;"><CircleCheck /></el-icon>
                    启用
                  </el-radio>
                  <el-radio :label="0">
                    <el-icon style="color: #F56C6C;"><CircleClose /></el-icon>
                    禁用
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="remark">
                <el-input
                  v-model="form.remark"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入备注信息"
                  maxlength="200"
                  show-word-limit
                />
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
  Plus, Expand, Fold, Download, Edit, Delete, Menu, Pointer, 
  Key, Connection, Tickets, CircleCheck, CircleClose, Close, Check
} from '@element-plus/icons-vue'
import { permissionApi } from '@/api'

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
  parentId: null,
  resourceType: 1,
  permissionName: '',
  permissionCode: '',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  status: 1,
  remark: ''
})

// 表格数据
const tableData = ref([])
const permissionTreeOptions = ref([])

// 树形选择组件配置
const treeSelectProps = {
  children: 'children',
  label: 'permissionName',
  value: 'id'
}

// 表单验证规则
const rules = {
  resourceType: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  permissionName: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  permissionCode: [
    { required: true, message: '请输入权限编码', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 获取权限类型样式类
const getTypeClass = (resourceType) => {
  const classMap = {
    1: 'type-menu',
    2: 'type-button',
    3: 'type-api'
  }
  return classMap[resourceType] || ''
}

// HTML实体编码，防止XSS
const escapeHtml = (text) => {
  if (!text) return ''
  const div = document.createElement('div')
  div.textContent = text
  return div.innerHTML
}

// 获取权限总数
const getTotalPermissions = () => {
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
    const response = await permissionApi.getPermissionTree()
    const addStatusLoading = (nodes) => {
      return nodes.map(node => ({
        ...node,
        statusLoading: false,
        children: node.children ? addStatusLoading(node.children) : undefined
      }))
    }
    tableData.value = addStatusLoading(response.data || [])
    permissionTreeOptions.value = buildTreeSelectOptions(response.data || [])
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const buildTreeSelectOptions = (data) => {
  return [
    { id: 0, permissionName: '根权限', children: data }
  ]
}

const getTypeLabel = (resourceType) => {
  const typeMap = {
    1: '菜单',
    2: '按钮',
    3: '接口'
  }
  return typeMap[resourceType] || resourceType
}

const getTypeTagType = (resourceType) => {
  const typeMap = {
    1: 'primary',
    2: 'warning',
    3: 'success'
  }
  return typeMap[resourceType] || 'info'
}

const handleAdd = (row = null) => {
  dialogTitle.value = row ? '新增子权限' : '新增权限'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    parentId: row ? row.id : 0,
    resourceType: 1,
    permissionName: '',
    permissionCode: '',
    path: '',
    component: '',
    icon: '',
    sortOrder: 0,
    status: 1,
    remark: ''
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑权限'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    resourceType: row.resourceType,
    permissionName: row.permissionName,
    permissionCode: row.permissionCode,
    path: row.path,
    component: row.component,
    icon: row.icon,
    sortOrder: row.sortOrder,
    status: row.status,
    remark: row.remark
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await permissionApi.updatePermission(form.id, form)
      ElMessage.success('更新权限成功')
    } else {
      await permissionApi.createPermission(form)
      ElMessage.success('创建权限成功')
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
    const safePermissionName = escapeHtml(row.permissionName)
    await ElMessageBox.confirm(
      `<div style="text-align: center;">
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除权限 <strong>${safePermissionName}</strong> 吗？</p>
        ${hasChildren ? '<p style="color: #F56C6C; font-size: 13px;">⚠️ 该权限存在子权限，将一并删除</p>' : ''}
      </div>`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    await permissionApi.deletePermission(row.id)
    ElMessage.success('删除权限成功')
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
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success(`${row.status === 1 ? '启用' : '禁用'}权限成功`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    console.error('更新状态失败:', error)
  } finally {
    row.statusLoading = false
  }
}

const handleExpandAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      // 使用迭代方式避免递归深度问题
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
      // 使用迭代方式避免递归深度问题
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

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
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
.permissions-page {
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
  color: #E6A23C;
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

/* 权限名称单元格 */
.permission-name-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.permission-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.permission-icon.type-menu {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
}

.permission-icon.type-button {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
}

.permission-icon.type-api {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.permission-name {
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
.no-icon {
  color: #c0c4cc;
}

.icon-preview {
  color: #409EFF;
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
.permission-dialog :deep(.el-dialog) {
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
