<template>
  <div class="depts-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon"><OfficeBuilding /></el-icon>
            <span>部门管理</span>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAdd()" class="action-btn primary-gradient">
              <el-icon><Plus /></el-icon>
              新增部门
            </el-button>
            <el-button type="info" @click="toggleExpandAll" class="action-btn">
              <el-icon><Sort /></el-icon>
              展开/折叠
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 部门表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table 
          ref="tableRef"
          v-loading="loading" 
          :data="tableData" 
          style="width: 100%"
          row-key="id"
          :default-expand-all="isExpandAll"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          :row-class-name="tableRowClassName"
          highlight-current-row
          stripe
        >
          <el-table-column prop="deptName" label="部门名称" min-width="200">
             <template #default="scope">
               <span style="font-weight: 500;">{{ scope.row.deptName }}</span>
             </template>
          </el-table-column>
          <el-table-column prop="deptCode" label="部门编码" width="120">
            <template #default="scope">
              <el-tag size="small" type="info" effect="plain">{{ scope.row.deptCode }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" effect="light" size="small">
                {{ scope.row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" align="center">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ scope.row.createTime }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="新增子部门" placement="top">
                  <el-button type="primary" size="small" circle plain @click="handleAdd(scope.row)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-button type="primary" size="small" circle @click="handleEdit(scope.row)">
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

    <!-- 部门表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
      class="dept-dialog"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="上级部门" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="deptOptions"
                :props="{ label: 'deptName', value: 'id', children: 'children' }"
                value-key="id"
                placeholder="选择上级部门"
                check-strictly
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input v-model="form.deptCode" placeholder="请输入部门编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" :min="0" :max="9999" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Edit, Delete, Search, Refresh, 
  OfficeBuilding, Clock, Sort 
} from '@element-plus/icons-vue'
import { deptApi } from '@/api'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isExpandAll = ref(true)
const tableRef = ref()
const formRef = ref()

// 表格数据
const tableData = ref([])
// 部门下拉树选项
const deptOptions = ref([])

// 表单数据
const form = reactive({
  id: undefined,
  parentId: undefined,
  deptName: '',
  deptCode: '',
  leader: '',
  phone: '',
  email: '',
  sortOrder: 0,
  status: 1
})

// 表单校验规则
const rules = {
  parentId: [
    { required: true, message: '请选择上级部门', trigger: 'blur' }
  ],
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 展开/折叠
const toggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value
  toggleTableExpansion(tableData.value, isExpandAll.value)
}

const toggleTableExpansion = (data, expanded) => {
  data.forEach(item => {
    tableRef.value?.toggleRowExpansion(item, expanded)
    if (item.children && item.children.length > 0) {
      toggleTableExpansion(item.children, expanded)
    }
  })
}

// 获取部门列表
const loadData = async () => {
  loading.value = true
  try {
    const response = await deptApi.getDeptTree()
    tableData.value = response.data || []
  } catch (error) {
    console.error('加载部门列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取部门下拉选项
const loadDeptOptions = async () => {
  try {
    const response = await deptApi.getDeptTree() // 复用树接口，或者使用options接口
    deptOptions.value = [{
      id: 0,
      deptName: '主类目',
      children: response.data || []
    }]
  } catch (error) {
    console.error('加载部门选项失败:', error)
  }
}

// 新增按钮操作
const handleAdd = (row) => {
  dialogTitle.value = '新增部门'
  dialogVisible.value = true
  loadDeptOptions()
  nextTick(() => {
    formRef.value?.resetFields()
    form.id = undefined
    form.parentId = row ? row.id : 0
    form.deptName = ''
    form.deptCode = ''
    form.leader = ''
    form.phone = ''
    form.email = ''
    form.sortOrder = 0
    form.status = 1
  })
}

// 编辑按钮操作
const handleEdit = async (row) => {
  dialogTitle.value = '编辑部门'
  dialogVisible.value = true
  loadDeptOptions()
  
  // 获取最新详情
  try {
    const response = await deptApi.getDeptById(row.id)
    const data = response.data
    nextTick(() => {
       Object.assign(form, data)
    })
  } catch (error) {
    console.error('获取详情失败', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await deptApi.updateDept(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await deptApi.createDept(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 删除按钮操作
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '是否确认删除名称为"' + row.deptName + '"的部门项？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await deptApi.deleteDept(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 表格行样式
const tableRowClassName = ({ row, rowIndex }) => {
  return `animate-row delay-${rowIndex % 10}`
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.depts-page {
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

.time-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #606266;
}

.operation-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
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

.dept-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
