<template>
  <div class="login-log-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-icon class="title-icon"><Key /></el-icon>
          <span>登录日志</span>
        </div>
        <div class="action-buttons">
          <el-input
            v-model="searchForm.username"
            placeholder="用户名"
            clearable
            style="width: 120px; margin-right: 10px;"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-input
            v-model="searchForm.ip"
            placeholder="登录IP"
            clearable
            style="width: 140px; margin-right: 10px;"
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
        <el-table-column prop="id" label="日志编号" width="100" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ip" label="登录IP" width="140" />
        <el-table-column prop="location" label="登录地点" width="140" />
        <el-table-column prop="browser" label="浏览器" width="120" show-overflow-tooltip />
        <el-table-column prop="os" label="操作系统" width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" effect="light">
              {{ scope.row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorMsg" label="错误信息" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.errorMsg" class="error-text">{{ scope.row.errorMsg }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="登录时间" width="180" />
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
      title="登录日志详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="日志编号">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentLog.username }}</el-descriptions-item>
        <el-descriptions-item label="登录IP">{{ currentLog.ip }}</el-descriptions-item>
        <el-descriptions-item label="登录地点">{{ currentLog.location || '-' }}</el-descriptions-item>
        <el-descriptions-item label="浏览器">{{ currentLog.browser }}</el-descriptions-item>
        <el-descriptions-item label="操作系统">{{ currentLog.os }}</el-descriptions-item>
        <el-descriptions-item label="登录状态">
          <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'" effect="light">
            {{ currentLog.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="登录时间">{{ currentLog.createTime }}</el-descriptions-item>
        <el-descriptions-item label="用户代理" :span="2">
          <div class="user-agent-content">{{ currentLog.userAgent || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMsg" label="错误信息" :span="2">
          <div class="error-content">{{ currentLog.errorMsg }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Key, Search, Refresh, Delete, View } from '@element-plus/icons-vue'
import { loginLogApi } from '@/api'

// 数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const detailVisible = ref(false)
const currentLog = ref({})
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  username: '',
  ip: '',
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

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await loginLogApi.getPage({
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
  Object.assign(searchForm, { username: '', ip: '', status: null, startTime: '', endTime: '' })
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
    await ElMessageBox.confirm('确定要清空所有登录日志吗？此操作不可恢复！', '清空确认', {
      type: 'warning',
      confirmButtonText: '确定清空',
      cancelButtonText: '取消'
    })
    await loginLogApi.clean()
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
.login-log-page {
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

.error-text {
  color: #F56C6C;
}

.user-agent-content,
.error-content {
  max-height: 150px;
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
