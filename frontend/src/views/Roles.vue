<template>
  <div class="roles-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon"><UserFilled /></el-icon>
            <span>角色管理</span>
            <el-tag type="info" size="small" effect="plain" round style="margin-left: 12px;">
              共 {{ tableData.length }} 个角色
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAdd" class="action-btn primary-gradient">
              <el-icon><Plus /></el-icon>
              新增角色
            </el-button>
            <el-button type="success" @click="handleExport" class="action-btn">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 角色表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table 
          v-loading="loading" 
          :data="tableData" 
          style="width: 100%"
          table-layout="auto"
          :row-class-name="tableRowClassName"
          highlight-current-row
          stripe
        >
          <el-table-column prop="id" label="ID" width="80" sortable />
          <el-table-column prop="roleName" label="角色名称" width="180">
            <template #default="scope">
              <div class="role-name-cell">
                <el-avatar 
                  :size="36" 
                  :style="{ backgroundColor: getRoleColor(scope.row.roleName) }"
                >
                  <el-icon><UserFilled /></el-icon>
                </el-avatar>
                <div class="role-info">
                  <span class="role-title">{{ scope.row.roleName }}</span>
                  <span class="role-code">{{ scope.row.roleCode }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述">
            <template #default="scope">
              <el-tooltip :content="scope.row.description" placement="top" v-if="scope.row.description">
                <span class="description-text">{{ scope.row.description }}</span>
              </el-tooltip>
              <span v-else class="no-description">暂无描述</span>
            </template>
          </el-table-column>
          <el-table-column prop="permissionCount" label="权限数量" width="120">
            <template #default="scope">
              <el-tag type="primary" effect="light" round>
                <el-icon style="margin-right: 4px;"><Key /></el-icon>
                {{ scope.row.permissionCount || 0 }} 个
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
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
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ scope.row.createTime }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="编辑角色" placement="top">
                  <el-button type="primary" size="small" circle @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="配置权限" placement="top">
                  <el-button type="warning" size="small" circle @click="handlePermissions(scope.row)">
                    <el-icon><Key /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除角色" placement="top">
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

    <!-- 角色表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
      class="role-dialog"
      :close-on-click-modal="false"
    >
      <transition name="fade" appear>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          style="max-width: 400px; margin: 0 auto;"
        >
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="form.roleName" placeholder="请输入角色名称" :prefix-icon="UserFilled" />
          </el-form-item>
          <el-form-item label="角色编码" prop="roleCode">
            <el-input v-model="form.roleCode" placeholder="请输入角色编码（如：ADMIN）" :prefix-icon="Key" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="3"
              placeholder="请输入角色描述"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status" class="status-radio-group">
              <el-radio :label="1">
                <el-icon style="color: #67C23A; margin-right: 4px;"><CircleCheck /></el-icon>
                启用
              </el-radio>
              <el-radio :label="0">
                <el-icon style="color: #F56C6C; margin-right: 4px;"><CircleClose /></el-icon>
                禁用
              </el-radio>
            </el-radio-group>
          </el-form-item>
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

    <!-- 权限配置对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="权限配置"
      width="650px"
      @close="handlePermissionDialogClose"
      class="permission-dialog"
      :close-on-click-modal="false"
    >
      <div class="permission-header">
        <div class="current-role">
          <el-avatar 
            :size="48" 
            :style="{ backgroundColor: getRoleColor(currentRole?.roleName) }"
          >
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <div class="role-detail">
            <span class="role-name">{{ currentRole?.roleName }}</span>
            <span class="role-desc">{{ currentRole?.description || '暂无描述' }}</span>
          </div>
        </div>
        <div class="permission-stats">
          <el-statistic title="已选权限" :value="selectedPermissionCount" />
        </div>
      </div>
      
      <el-divider />
      
      <div class="permission-tree-container">
        <div class="tree-actions">
          <el-button size="small" @click="handleCheckAll">
            <el-icon><Select /></el-icon>
            全选
          </el-button>
          <el-button size="small" @click="handleUncheckAll">
            <el-icon><Close /></el-icon>
            取消全选
          </el-button>
          <el-input
            v-model="permissionFilter"
            placeholder="搜索权限..."
            style="width: 200px; margin-left: auto;"
            clearable
            size="small"
            :prefix-icon="Search"
          />
        </div>
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          :props="treeProps"
          show-checkbox
          node-key="id"
          :default-expanded-keys="defaultExpandedKeys"
          :filter-node-method="filterNode"
          style="max-height: 350px; overflow-y: auto; margin-top: 16px;"
          highlight-current
        >
          <template #default="{ node, data }">
            <div class="tree-node">
              <el-icon v-if="data.type === 'menu'" style="color: #409EFF;"><Menu /></el-icon>
              <el-icon v-else-if="data.type === 'button'" style="color: #E6A23C;"><Pointer /></el-icon>
              <el-icon v-else style="color: #67C23A;"><Connection /></el-icon>
              <span style="margin-left: 8px;">{{ node.label }}</span>
              <el-tag v-if="data.code" size="small" type="info" effect="plain" style="margin-left: 8px;">
                {{ data.code }}
              </el-tag>
            </div>
          </template>
        </el-tree>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="permissionLoading">
            <el-icon><Check /></el-icon>
            保存配置
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Download, Edit, Key, Delete, UserFilled, Clock,
  CircleCheck, CircleClose, Close, Check, Menu, Pointer, 
  Connection, Search, Select
} from '@element-plus/icons-vue'
import { roleApi, permissionApi } from '@/api'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const permissionLoading = ref(false)
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRole = ref(null)
const formRef = ref()
const permissionTreeRef = ref()
const permissionFilter = ref('')
const selectedPermissionCount = ref(0)
const defaultExpandedKeys = ref([1, 2, 3])

// 表单数据
const form = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
})

// 表格数据
const tableData = ref([])
const permissionTree = ref([])

// 树形组件配置
const treeProps = {
  children: 'children',
  label: 'name'
}

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[A-Z_]+$/, message: '角色编码只能包含大写字母和下划线', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
}

// 获取角色颜色
const getRoleColor = (roleName) => {
  const colorMap = {
    '管理员': '#F56C6C',
    '超级管理员': '#E6A23C',
    '编辑': '#409EFF',
    '普通用户': '#67C23A'
  }
  return colorMap[roleName] || '#909399'
}

// 表格行样式
const tableRowClassName = ({ row, rowIndex }) => {
  return `animate-row delay-${rowIndex % 10}`
}

// 过滤权限树节点
const filterNode = (value, data) => {
  if (!value) return true
  return data.name.includes(value) || (data.code && data.code.includes(value))
}

// 监听过滤输入
watch(permissionFilter, (val) => {
  permissionTreeRef.value?.filter(val)
})

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await roleApi.getRoleList()
    tableData.value = (response.data || []).map(item => ({
      ...item,
      statusLoading: false
      // permissionCount 现在从后端获取，无需模拟数据
    }))
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadPermissionTree = async () => {
  try {
    const response = await permissionApi.getPermissionTree()
    permissionTree.value = response.data || []
  } catch (error) {
    console.error('加载权限树失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    roleName: '',
    roleCode: '',
    description: '',
    status: 1
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    roleName: row.roleName,
    roleCode: row.roleCode,
    description: row.description,
    status: row.status
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await roleApi.updateRole(form.id, form)
      ElMessage.success('更新角色成功')
    } else {
      await roleApi.createRole(form)
      ElMessage.success('创建角色成功')
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
    await ElMessageBox.confirm(
      `<div style="text-align: center;">
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除角色 <strong>${row.roleName}</strong> 吗？</p>
        <p style="color: #909399; font-size: 13px;">关联的用户将失去该角色权限</p>
      </div>`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    await roleApi.deleteRole(row.id)
    ElMessage.success('删除角色成功')
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
    ElMessage.success(`${row.status === 1 ? '启用' : '禁用'}角色成功`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    console.error('更新状态失败:', error)
  } finally {
    row.statusLoading = false
  }
}

const handlePermissions = (row) => {
  currentRole.value = row
  permissionDialogVisible.value = true
  selectedPermissionCount.value = 0
  
  // 模拟加载角色已有权限
  if (permissionTreeRef.value) {
    // 这里应该调用API获取角色已有权限并设置选中状态
    // permissionTreeRef.value.setCheckedKeys(rolePermissions)
  }
}

const handleCheckAll = () => {
  const allKeys = getAllKeys(permissionTree.value)
  permissionTreeRef.value?.setCheckedKeys(allKeys)
  selectedPermissionCount.value = allKeys.length
}

const handleUncheckAll = () => {
  permissionTreeRef.value?.setCheckedKeys([])
  selectedPermissionCount.value = 0
}

const getAllKeys = (nodes) => {
  let keys = []
  nodes.forEach(node => {
    keys.push(node.id)
    if (node.children) {
      keys = keys.concat(getAllKeys(node.children))
    }
  })
  return keys
}

const handleSavePermissions = async () => {
  if (!permissionTreeRef.value || !currentRole.value) return
  
  try {
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys]
    
    // 这里应该调用API保存角色权限
    // await roleApi.updateRolePermissions(currentRole.value.id, allCheckedKeys)
    
    permissionLoading.value = true
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('权限配置保存成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('保存权限失败:', error)
  } finally {
    permissionLoading.value = false
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handlePermissionDialogClose = () => {
  currentRole.value = null
  permissionFilter.value = ''
  if (permissionTreeRef.value) {
    permissionTreeRef.value.setCheckedKeys([])
  }
}

// 生命周期
onMounted(() => {
  loadData()
  loadPermissionTree()
})
</script>

<style scoped>
.roles-page {
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

/* 表格单元格样式 */
.role-name-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.role-info {
  display: flex;
  flex-direction: column;
}

.role-title {
  font-weight: 600;
  color: #303133;
}

.role-code {
  font-size: 12px;
  color: #909399;
  font-family: monospace;
}

.description-text {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.no-description {
  color: #c0c4cc;
  font-style: italic;
}

.time-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.time-cell .el-icon {
  color: #909399;
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

/* 表格行动画 */
:deep(.animate-row) {
  animation: rowFadeIn 0.4s ease-out both;
}

:deep(.delay-0) { animation-delay: 0s; }
:deep(.delay-1) { animation-delay: 0.05s; }
:deep(.delay-2) { animation-delay: 0.1s; }
:deep(.delay-3) { animation-delay: 0.15s; }
:deep(.delay-4) { animation-delay: 0.2s; }

@keyframes rowFadeIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 对话框样式 */
.role-dialog :deep(.el-dialog),
.permission-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.status-radio-group {
  display: flex;
  gap: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 权限配置对话框 */
.permission-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}

.current-role {
  display: flex;
  align-items: center;
  gap: 16px;
}

.role-detail {
  display: flex;
  flex-direction: column;
}

.role-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.role-desc {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.permission-stats {
  text-align: center;
}

.permission-tree-container {
  padding: 0 10px;
}

.tree-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.tree-node {
  display: flex;
  align-items: center;
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
