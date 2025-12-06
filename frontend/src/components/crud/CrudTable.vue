<template>
  <div class="crud-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon" :style="{ color: titleIconColor }">
              <component :is="titleIcon" />
            </el-icon>
            <span>{{ title }}</span>
            <el-tag type="info" size="small" effect="plain" round style="margin-left: 12px;">
              共 {{ totalCount }} {{ countUnit }}
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button 
              v-if="showAdd" 
              type="primary" 
              @click="handleAdd" 
              class="action-btn primary-gradient"
            >
              <el-icon><Plus /></el-icon>
              {{ addButtonText }}
            </el-button>
            <slot name="extra-buttons"></slot>
            <el-button v-if="showExport" type="info" @click="handleExport" class="action-btn">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 数据表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="data"
          style="width: 100%"
          table-layout="auto"
          :row-key="rowKey"
          :tree-props="treeProps"
          :default-expand-all="defaultExpandAll"
          :row-class-name="rowClassName"
          highlight-current-row
          stripe
          ref="tableRef"
        >
          <slot name="columns"></slot>
          
          <!-- 操作列 -->
          <el-table-column v-if="showOperations" label="操作" :width="operationWidth">
            <template #default="scope">
              <div class="operation-buttons">
                <slot name="operations" :row="scope.row">
                  <el-tooltip v-if="showEdit" content="编辑" placement="top">
                    <el-button type="primary" size="small" circle @click="handleEdit(scope.row)">
                      <el-icon><Edit /></el-icon>
                    </el-button>
                  </el-tooltip>
                  <el-tooltip v-if="showDelete" content="删除" placement="top">
                    <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </el-tooltip>
                </slot>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div v-if="showPagination" class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="pageSizes"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </transition>

    <!-- 表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      :width="dialogWidth"
      @close="handleDialogClose"
      class="crud-dialog"
      :close-on-click-modal="false"
    >
      <transition name="fade" appear>
        <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          :label-width="labelWidth"
          style="max-width: 500px; margin: 0 auto;"
        >
          <slot name="form" :form="formData" :is-edit="isEdit"></slot>
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
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Edit, Delete, Close, Check } from '@element-plus/icons-vue'

// Props 定义
const props = defineProps({
  // 页面标题
  title: { type: String, required: true },
  titleIcon: { type: [String, Object], default: 'Document' },
  titleIconColor: { type: String, default: '#409EFF' },
  
  // 数据相关
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  rowKey: { type: String, default: 'id' },
  
  // 树形表格
  treeProps: { type: Object, default: () => ({ children: 'children', hasChildren: 'hasChildren' }) },
  defaultExpandAll: { type: Boolean, default: false },
  
  // 统计
  totalCount: { type: Number, default: 0 },
  countUnit: { type: String, default: '条记录' },
  
  // 按钮控制
  showAdd: { type: Boolean, default: true },
  showEdit: { type: Boolean, default: true },
  showDelete: { type: Boolean, default: true },
  showExport: { type: Boolean, default: true },
  showOperations: { type: Boolean, default: true },
  showPagination: { type: Boolean, default: false },
  addButtonText: { type: String, default: '新增' },
  
  // 操作列宽度
  operationWidth: { type: [String, Number], default: 180 },
  
  // 分页
  total: { type: Number, default: 0 },
  pageSizes: { type: Array, default: () => [10, 20, 50, 100] },
  
  // 对话框
  dialogWidth: { type: String, default: '500px' },
  labelWidth: { type: String, default: '80px' },
  
  // 表单
  formRules: { type: Object, default: () => ({}) },
  formModel: { type: Object, default: () => ({}) },
  
  // 删除确认
  deleteConfirmField: { type: String, default: 'name' },
  deleteConfirmTitle: { type: String, default: '删除确认' },
  
  // 行样式
  rowClassName: { type: [String, Function], default: '' }
})

// Emits 定义
const emit = defineEmits([
  'add', 'edit', 'delete', 'export', 
  'submit', 'page-change', 'size-change',
  'update:formModel'
])

// 响应式数据
const tableRef = ref()
const formRef = ref()
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const isEdit = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

// 表单数据
const formData = ref({ ...props.formModel })

// 监听 formModel 变化
watch(() => props.formModel, (newVal) => {
  formData.value = { ...newVal }
}, { deep: true })

// 方法
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = props.addButtonText
  formData.value = { ...props.formModel }
  dialogVisible.value = true
  emit('add')
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑'
  formData.value = { ...row }
  dialogVisible.value = true
  emit('edit', row)
}

const handleDelete = async (row) => {
  const displayName = row[props.deleteConfirmField] || row.id
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${displayName}" 吗？`,
      props.deleteConfirmTitle,
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    emit('delete', row)
  } catch {
    // 用户取消
  }
}

const handleExport = () => {
  emit('export')
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    emit('submit', { data: formData.value, isEdit: isEdit.value })
  } catch {
    // 验证失败
  }
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleSizeChange = (size) => {
  emit('size-change', size)
}

const handleCurrentChange = (page) => {
  emit('page-change', page)
}

// 暴露方法给父组件
const closeDialog = () => {
  dialogVisible.value = false
  submitLoading.value = false
}

const openDialog = (title, data = null) => {
  dialogTitle.value = title
  isEdit.value = !!data
  formData.value = data ? { ...data } : { ...props.formModel }
  dialogVisible.value = true
}

const setSubmitLoading = (loading) => {
  submitLoading.value = loading
}

const getTableRef = () => tableRef.value

defineExpose({
  closeDialog,
  openDialog,
  setSubmitLoading,
  getTableRef,
  formData
})
</script>

<style scoped>
.crud-page {
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
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeSlideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.action-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.action-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.title-icon {
  font-size: 24px;
  margin-right: 8px;
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

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 对话框样式 */
.crud-dialog :deep(.el-dialog) {
  border-radius: 16px;
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
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>
