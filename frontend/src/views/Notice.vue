<template>
  <div class="notice-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-icon class="title-icon"><Bell /></el-icon>
          <span>通知公告</span>
        </div>
        <div class="action-buttons">
          <el-input
            v-model="searchForm.title"
            placeholder="公告标题"
            clearable
            style="width: 150px; margin-right: 10px;"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-select v-model="searchForm.noticeType" placeholder="公告类型" clearable style="width: 120px; margin-right: 10px;" @change="handleSearch">
            <el-option label="通知" :value="1" />
            <el-option label="公告" :value="2" />
          </el-select>
          <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 100px; margin-right: 10px;" @change="handleSearch">
            <el-option label="正常" :value="1" />
            <el-option label="关闭" :value="0" />
          </el-select>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button type="success" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增
          </el-button>
          <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="序号" width="80" />
        <el-table-column prop="title" label="公告标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="noticeType" label="公告类型" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.noticeType === 1 ? 'warning' : 'primary'" effect="light">
              {{ scope.row.noticeType === 1 ? '通知' : '公告' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="light">
              {{ scope.row.status === 1 ? '正常' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建者" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleDetail(scope.row)">
              <el-icon><View /></el-icon>
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告类型" prop="noticeType">
          <el-radio-group v-model="form.noticeType">
            <el-radio :label="1">通知</el-radio>
            <el-radio :label="2">公告</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="8"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="公告详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="公告标题" :span="2">{{ currentNotice.title }}</el-descriptions-item>
        <el-descriptions-item label="公告类型">
          <el-tag :type="currentNotice.noticeType === 1 ? 'warning' : 'primary'" effect="light">
            {{ currentNotice.noticeType === 1 ? '通知' : '公告' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentNotice.status === 1 ? 'success' : 'info'" effect="light">
            {{ currentNotice.status === 1 ? '正常' : '关闭' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建者">{{ currentNotice.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentNotice.createTime }}</el-descriptions-item>
        <el-descriptions-item label="公告内容" :span="2">
          <div class="notice-content">{{ currentNotice.content || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentNotice.remark" label="备注" :span="2">
          {{ currentNotice.remark }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, Search, Refresh, Plus, Edit, Delete, View } from '@element-plus/icons-vue'
import { noticeApi } from '@/api'

// 数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const dialogTitle = ref('')
const tableData = ref([])
const selectedRows = ref([])
const currentNotice = ref({})
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  title: '',
  noticeType: null,
  status: null
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表单数据
const form = reactive({
  id: null,
  title: '',
  noticeType: 1,
  status: 1,
  content: '',
  remark: ''
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  noticeType: [{ required: true, message: '请选择公告类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await noticeApi.getPage({
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, { title: '', noticeType: null, status: null })
  handleSearch()
}

// 选择变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 分页变化
const handleSizeChange = () => {
  pagination.current = 1
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增公告'
  Object.assign(form, {
    id: null,
    title: '',
    noticeType: 1,
    status: 1,
    content: '',
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑公告'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 查看详情
const handleDetail = (row) => {
  currentNotice.value = row
  detailVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除公告 [${row.title}] 吗？`, '删除确认', {
      type: 'warning'
    })
    await noticeApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条公告吗？`, '批量删除确认', {
      type: 'warning'
    })
    const ids = selectedRows.value.map(row => row.id)
    await noticeApi.batchDelete(ids)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (form.id) {
      await noticeApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await noticeApi.create(form)
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

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.notice-page {
  padding: 0;
}

.action-card {
  margin-bottom: 20px;
}

.action-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.title-icon {
  font-size: 24px;
  margin-right: 8px;
  color: #409EFF;
}

.action-buttons {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 5px;
}

.table-card {
  min-height: 400px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.notice-content {
  max-height: 300px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.6;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
