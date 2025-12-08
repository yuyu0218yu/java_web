<template>
  <div class="users-page">
    <!-- 搜索和操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="search-card">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="用户名">
            <el-input
              v-model="searchForm.username"
              placeholder="请输入用户名"
              clearable
              style="width: 200px"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input
              v-model="searchForm.email"
              placeholder="请输入邮箱"
              clearable
              style="width: 200px"
              :prefix-icon="Message"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px">
              <el-option label="启用" value="1">
                <el-icon style="color: #67C23A"><CircleCheck /></el-icon>
                <span style="margin-left: 8px;">启用</span>
              </el-option>
              <el-option label="禁用" value="0">
                <el-icon style="color: #F56C6C"><CircleClose /></el-icon>
                <span style="margin-left: 8px;">禁用</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" class="action-btn">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset" class="action-btn">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="action-buttons">
          <el-button type="primary" @click="handleAdd" class="action-btn primary-gradient">
            <el-icon><Plus /></el-icon>
            新增用户
          </el-button>
          <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete" class="action-btn">
            <el-icon><Delete /></el-icon>
            批量删除
            <el-badge v-if="selectedRows.length > 0" :value="selectedRows.length" class="batch-badge" />
          </el-button>
          <el-button type="success" @click="handleExport" class="action-btn">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>
      </el-card>
    </transition>

    <!-- 用户表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="tableData"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          :row-class-name="tableRowClassName"
          highlight-current-row
          stripe
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" sortable />
          <el-table-column prop="username" label="用户名" width="140">
            <template #default="scope">
              <div class="user-info-cell">
                <el-avatar 
                  :size="32" 
                  :style="{ backgroundColor: getAvatarColor(scope.row.username), marginRight: '10px' }"
                >
                  {{ scope.row.username.charAt(0).toUpperCase() }}
                </el-avatar>
                <span class="username-text">{{ scope.row.username }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="200">
            <template #default="scope">
              <div class="email-cell">
                <el-icon><Message /></el-icon>
                <span>{{ scope.row.email }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号" width="130">
            <template #default="scope">
              <div class="phone-cell">
                <el-icon><Phone /></el-icon>
                <span>{{ scope.row.phone || '-' }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="roleName" label="角色" min-width="120">
            <template #default="scope">
              <el-tag 
                :type="getRoleTagType(scope.row.roleName)" 
                effect="light"
                round
              >
                <el-icon style="margin-right: 4px;"><Avatar /></el-icon>
                {{ scope.row.roleName || '未分配' }}
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
          <el-table-column prop="createTime" label="创建时间" min-width="180">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ scope.row.createTime }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="编辑用户" placement="top">
                  <el-button type="primary" size="small" circle @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="重置密码" placement="top">
                  <el-button type="warning" size="small" circle @click="handleResetPassword(scope.row)">
                    <el-icon><Key /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除用户" placement="top">
                  <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            共 <span class="highlight">{{ pagination.total }}</span> 条记录，
            当前第 <span class="highlight">{{ pagination.current }}</span> / 
            <span class="highlight">{{ Math.ceil(pagination.total / pagination.size) || 1 }}</span> 页
          </div>
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
          />
        </div>
      </el-card>
    </transition>

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
      class="user-dialog"
      :close-on-click-modal="false"
    >
      <transition name="fade" appear>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          style="max-width: 500px; margin: 0 auto;"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" :prefix-icon="Message" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" :prefix-icon="Phone" />
          </el-form-item>
          <el-form-item label="密码" prop="password" v-if="!form.id">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password :prefix-icon="Lock" />
          </el-form-item>
          <el-form-item label="角色" prop="roleId">
            <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%">
              <el-option
                v-for="role in roleOptions"
                :key="role.id"
                :label="role.roleName"
                :value="role.id"
              >
                <el-icon style="margin-right: 8px;"><Avatar /></el-icon>
                <span>{{ role.roleName }}</span>
              </el-option>
            </el-select>
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
          <el-button @click="dialogVisible = false" class="cancel-btn">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading" class="submit-btn">
            <el-icon><Check /></el-icon>
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Delete, Download, Edit, Key, Lock, 
  User, Message, Phone, Avatar, Clock, CircleCheck, CircleClose,
  Close, Check
} from '@element-plus/icons-vue'
import { userApi, roleApi } from '@/api'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  username: '',
  email: '',
  status: ''
})

// 用户表单
const form = reactive({
  id: null,
  username: '',
  email: '',
  phone: '',
  password: '',
  roleId: null,
  status: 1
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref([])
const roleOptions = ref([])

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 获取头像颜色
const getAvatarColor = (username) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9b59b6', '#1abc9c']
  const index = username ? username.charCodeAt(0) % colors.length : 0
  return colors[index]
}

// 获取角色标签类型
const getRoleTagType = (roleName) => {
  const typeMap = {
    '管理员': 'danger',
    '超级管理员': 'danger',
    '编辑': 'warning',
    '普通用户': 'info',
    '访客': ''
  }
  return typeMap[roleName] || 'primary'
}

// 表格行样式
const tableRowClassName = ({ row, rowIndex }) => {
  return `animate-row delay-${rowIndex % 10}`
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    const response = await userApi.getUserPage(params)
    tableData.value = (response.data.records || []).map(item => ({
      ...item,
      statusLoading: false
    }))
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadRoles = async () => {
  try {
    const response = await roleApi.getRoleList()
    roleOptions.value = response.data || []
  } catch (error) {
    console.error('加载角色数据失败:', error)
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    email: '',
    status: ''
  })
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    username: '',
    email: '',
    phone: '',
    password: '',
    roleId: null,
    status: 1
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    username: row.username,
    email: row.email,
    phone: row.phone,
    password: '',
    roleId: row.roleId,
    status: row.status
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await userApi.updateUser(form.id, form)
      ElMessage.success('更新用户成功')
    } else {
      await userApi.createUser(form)
      ElMessage.success('创建用户成功')
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
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除用户 <strong>${row.username}</strong> 吗？</p>
        <p style="color: #909399; font-size: 13px;">此操作不可恢复</p>
      </div>`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
        customClass: 'delete-confirm-dialog'
      }
    )
    
    await userApi.deleteUser(row.id)
    ElMessage.success('删除用户成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    await userApi.batchDeleteUsers(ids)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

const handleResetPassword = async (row) => {
  try {
    const { value: newPassword } = await ElMessageBox.prompt('请输入新密码', '重置密码', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPlaceholder: '请输入6-20位密码',
      inputValidator: (value) => {
        if (!value || value.length < 6) {
          return '密码长度不能少于6位'
        }
        return true
      }
    })
    
    await userApi.resetPassword(row.id, newPassword)
    ElMessage.success('重置密码成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重置密码失败:', error)
    }
  }
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  try {
    await userApi.updateUserStatus(row.id, row.status)
    ElMessage.success(`${row.status === 1 ? '启用' : '禁用'}用户成功`)
  } catch (error) {
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
    console.error('更新状态失败:', error)
  } finally {
    row.statusLoading = false
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
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
  loadRoles()
})
</script>

<style scoped>
.users-page {
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

.search-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.search-form {
  margin-bottom: 16px;
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

.batch-badge {
  margin-left: 8px;
}

.table-card {
  min-height: 400px;
  border-radius: 12px;
}

/* 表格单元格样式 */
.user-info-cell {
  display: flex;
  align-items: center;
}

.username-text {
  font-weight: 500;
}

.email-cell,
.phone-cell,
.time-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.email-cell .el-icon,
.phone-cell .el-icon,
.time-cell .el-icon {
  color: #909399;
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
:deep(.delay-5) { animation-delay: 0.25s; }
:deep(.delay-6) { animation-delay: 0.3s; }
:deep(.delay-7) { animation-delay: 0.35s; }
:deep(.delay-8) { animation-delay: 0.4s; }
:deep(.delay-9) { animation-delay: 0.45s; }

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

/* 分页样式 */
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-info {
  color: #909399;
  font-size: 14px;
}

.pagination-info .highlight {
  color: #409EFF;
  font-weight: 600;
}

/* 对话框样式 */
.user-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.user-dialog :deep(.el-dialog__header) {
  border-bottom: 1px solid #f0f0f0;
  padding: 20px 24px;
}

.user-dialog :deep(.el-dialog__body) {
  padding: 30px 24px;
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

.cancel-btn,
.submit-btn {
  padding: 10px 24px;
  border-radius: 8px;
}

.submit-btn {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
  border: none;
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
