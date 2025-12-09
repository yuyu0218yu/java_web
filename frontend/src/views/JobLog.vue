<template>
  <div class="job-log-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-button type="text" @click="goBack" style="margin-right: 10px;">
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          <el-icon class="title-icon"><Document /></el-icon>
          <span>任务日志</span>
        </div>
        <div class="action-buttons">
          <el-input
            v-model="searchForm.jobName"
            placeholder="任务名称"
            clearable
            style="width: 150px; margin-right: 10px;"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 100px; margin-right: 10px;" @change="handleSearch">
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 250px; margin-right: 10px;"
            @change="handleDateChange"
          />
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button type="danger" @click="handleClean">
            <el-icon><Delete /></el-icon>
            清空
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
        <el-table-column prop="id" label="日志ID" width="80" />
        <el-table-column prop="jobName" label="任务名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="jobGroup" label="任务组" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.jobGroup === 'SYSTEM' ? 'danger' : 'info'" effect="light" size="small">
              {{ scope.row.jobGroup === 'SYSTEM' ? '系统' : '默认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="invokeTarget" label="调用目标" min-width="200" show-overflow-tooltip />
        <el-table-column prop="jobMessage" label="日志信息" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" effect="light">
              {{ scope.row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="elapsedTime" label="耗时(ms)" width="100" align="center" />
        <el-table-column prop="createTime" label="执行时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleDetail(scope.row)">
              <el-icon><View /></el-icon>
              详情
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="日志详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="任务ID">{{ currentLog.jobId }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ currentLog.jobName }}</el-descriptions-item>
        <el-descriptions-item label="任务组">
          <el-tag :type="currentLog.jobGroup === 'SYSTEM' ? 'danger' : 'info'" effect="light" size="small">
            {{ currentLog.jobGroup === 'SYSTEM' ? '系统' : '默认' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="调用目标" :span="2">{{ currentLog.invokeTarget }}</el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'" effect="light">
            {{ currentLog.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog.elapsedTime }} ms</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ currentLog.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ currentLog.endTime }}</el-descriptions-item>
        <el-descriptions-item label="日志信息" :span="2">
          <div class="log-content">{{ currentLog.jobMessage || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.exceptionInfo" label="异常信息" :span="2">
          <div class="error-content">{{ currentLog.exceptionInfo }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Search, Refresh, Delete, View, ArrowLeft } from '@element-plus/icons-vue'
import { jobLogApi } from '@/api'

const router = useRouter()

// 数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const detailVisible = ref(false)
const currentLog = ref({})
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  jobName: '',
  jobGroup: '',
  status: null,
  startTime: '',
  endTime: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 返回上一页
const goBack = () => {
  router.push('/jobs')
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await jobLogApi.getPage({
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

// 日期范围变化
const handleDateChange = (val) => {
  if (val && val.length === 2) {
    searchForm.startTime = val[0]
    searchForm.endTime = val[1]
  } else {
    searchForm.startTime = ''
    searchForm.endTime = ''
  }
  handleSearch()
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, { jobName: '', jobGroup: '', status: null, startTime: '', endTime: '' })
  dateRange.value = []
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

// 查看详情
const handleDetail = (row) => {
  currentLog.value = row
  detailVisible.value = true
}

// 清空日志
const handleClean = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有任务日志吗？此操作不可恢复！', '清空确认', {
      type: 'warning',
      confirmButtonText: '确定清空',
      cancelButtonText: '取消'
    })
    await jobLogApi.clean()
    ElMessage.success('清空成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空失败:', error)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.job-log-page {
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

.log-content,
.error-content {
  max-height: 200px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-all;
  font-family: monospace;
  font-size: 12px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
}

.error-content {
  color: #F56C6C;
  background: #fef0f0;
}
</style>
