<template>
  <div class="generator-page">
    <!-- 标题栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon" style="color: #E6A23C"><Cpu /></el-icon>
            <span>代码生成器</span>
            <el-tag type="warning" size="small" effect="plain" round style="margin-left: 12px;">
              已导入 {{ pagination.total }} 张表
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="success" @click="showImportDialog" class="action-btn">
              <el-icon><Plus /></el-icon>
              导入表
            </el-button>
            <el-button type="primary" @click="handleRefresh" class="action-btn">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 已导入表列表 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <div class="search-bar">
          <el-input
            v-model="searchParams.tableName"
            placeholder="搜索表名..."
            clearable
            style="width: 250px; margin-right: 10px"
            :prefix-icon="Search"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-input
            v-model="searchParams.tableComment"
            placeholder="搜索表注释..."
            clearable
            style="width: 250px; margin-right: 10px"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
        </div>

        <el-table
          v-loading="loading"
          :data="tableList"
          style="width: 100%; margin-top: 20px"
          table-layout="auto"
          highlight-current-row
          stripe
        >
          <el-table-column type="selection" width="55" />
          <el-table-column type="index" label="#" width="60" />
          <el-table-column prop="tableName" label="表名" min-width="200">
            <template #default="scope">
              <el-tag effect="plain" type="info">
                <el-icon style="margin-right: 4px;"><Grid /></el-icon>
                {{ scope.row.tableName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tableComment" label="表注释" min-width="200">
            <template #default="scope">
              <span>{{ scope.row.tableComment || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="className" label="实体类名" min-width="150" />
          <el-table-column prop="updateTime" label="更新时间" width="180">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ formatTime(scope.row.updateTime) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="320" fixed="right">
            <template #default="scope">
              <div class="operation-buttons">
                <el-button type="primary" size="small" @click="handlePreview(scope.row)">
                  <el-icon><View /></el-icon>
                  预览
                </el-button>
                <el-button type="warning" size="small" @click="handleDownload(scope.row)">
                  <el-icon><Download /></el-icon>
                  下载
                </el-button>
                <el-button type="info" size="small" @click="handleEdit(scope.row)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button type="danger" size="small" @click="handleDelete(scope.row)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          style="margin-top: 20px; justify-content: flex-end"
          @current-change="loadTables"
          @size-change="loadTables"
        />
      </el-card>
    </transition>

    <!-- 导入表对话框 -->
    <el-dialog
      v-model="importDialogVisible"
      title="导入表"
      width="70%"
      :close-on-click-modal="false"
    >
      <el-input
        v-model="dbSearchKeyword"
        placeholder="搜索表名..."
        clearable
        style="width: 300px; margin-bottom: 20px"
        :prefix-icon="Search"
      />
      <el-table
        v-loading="dbTableLoading"
        :data="filteredDbTables"
        style="width: 100%"
        max-height="400"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="tableName" label="表名" min-width="200" />
        <el-table-column prop="tableComment" label="表注释" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport" :disabled="selectedTables.length === 0">
          确定导入 ({{ selectedTables.length }})
        </el-button>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="代码预览"
      width="85%"
      top="3vh"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane
          v-for="(code, name) in previewCode"
          :key="name"
          :label="name"
          :name="name"
        >
          <pre class="code-preview"><code>{{ code }}</code></pre>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="warning" @click="downloadFromPreview" :loading="downloading">
          <el-icon><Download /></el-icon>
          下载ZIP
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Cpu, Refresh, Grid, Clock, View, Download, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { generatorApi } from '@/api'

// 响应式数据
const loading = ref(false)
const tableList = ref([])
const dbTableList = ref([])
const dbTableLoading = ref(false)
const selectedTables = ref([])
const dbSearchKeyword = ref('')

// 搜索参数
const searchParams = ref({
  tableName: '',
  tableComment: ''
})

// 分页
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 对话框
const importDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const previewCode = ref({})
const activeTab = ref('')
const downloading = ref(false)
const currentTable = ref(null)

// 计算属性
const filteredDbTables = computed(() => {
  if (!dbSearchKeyword.value) return dbTableList.value
  return dbTableList.value.filter(t =>
    t.tableName.toLowerCase().includes(dbSearchKeyword.value.toLowerCase())
  )
})

// 方法
const loadTables = async () => {
  loading.value = true
  try {
    const res = await generatorApi.getTablePage({
      current: pagination.value.current,
      size: pagination.value.size,
      tableName: searchParams.value.tableName,
      tableComment: searchParams.value.tableComment
    })
    tableList.value = res.data.records || []
    pagination.value.total = res.data.total || 0
  } catch (error) {
    console.error('加载表列表失败', error)
    ElMessage.error('加载表列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  loadTables()
}

const handleRefresh = () => {
  searchParams.value = { tableName: '', tableComment: '' }
  pagination.value.current = 1
  loadTables()
}

const showImportDialog = async () => {
  importDialogVisible.value = true
  dbTableLoading.value = true
  try {
    const res = await generatorApi.getDbTableList({})
    dbTableList.value = res.data || []
  } catch (error) {
    console.error('加载数据库表失败', error)
    ElMessage.error('加载数据库表失败')
  } finally {
    dbTableLoading.value = false
  }
}

const handleSelectionChange = (selection) => {
  selectedTables.value = selection.map(item => item.tableName)
}

const confirmImport = async () => {
  if (selectedTables.value.length === 0) {
    ElMessage.warning('请选择要导入的表')
    return
  }

  try {
    await generatorApi.importTable(selectedTables.value)
    ElMessage.success('导入成功！')
    importDialogVisible.value = false
    loadTables()
  } catch (error) {
    console.error('导入失败', error)
    ElMessage.error('导入失败: ' + (error.message || '未知错误'))
  }
}

const handlePreview = async (row) => {
  currentTable.value = row
  try {
    const res = await generatorApi.previewCode(row.tableId)
    previewCode.value = res.data || {}
    activeTab.value = Object.keys(previewCode.value)[0] || ''
    previewDialogVisible.value = true
  } catch (error) {
    console.error('预览失败', error)
    ElMessage.error('预览失败: ' + (error.message || '未知错误'))
  }
}

const handleDownload = async (row) => {
  downloading.value = true
  try {
    await generatorApi.downloadCode(row.tableId)
    ElMessage.success('代码下载成功！')
  } catch (error) {
    console.error('下载失败', error)
    ElMessage.error('下载失败: ' + (error.message || '未知错误'))
  } finally {
    downloading.value = false
  }
}

const downloadFromPreview = async () => {
  if (!currentTable.value) return
  downloading.value = true
  try {
    await generatorApi.downloadCode(currentTable.value.tableId)
    ElMessage.success('代码下载成功！')
  } catch (error) {
    console.error('下载失败', error)
    ElMessage.error('下载失败: ' + (error.message || '未知错误'))
  } finally {
    downloading.value = false
  }
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中...')
  // TODO: 实现编辑功能
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除表 "${row.tableName}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )

    await generatorApi.deleteTable([row.tableId])
    ElMessage.success('删除成功！')
    loadTables()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  loadTables()
})
</script>

<style scoped>
.generator-page {
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
}

.action-btn {
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.table-card {
  min-height: 400px;
  border-radius: 12px;
  position: relative;
}

.search-bar {
  display: flex;
  margin-bottom: 10px;
}

.time-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.operation-buttons {
  display: flex;
  gap: 8px;
}

/* 代码预览 */
.code-preview {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  max-height: 60vh;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 13px;
  line-height: 1.5;
  margin: 0;
}

/* 复选框组样式 */
.el-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.el-checkbox {
  margin-right: 0;
}

/* 折叠面板样式 */
:deep(.el-collapse-item__header) {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

:deep(.el-collapse-item__content) {
  padding: 16px 0;
}

/* 分割线样式 */
:deep(.el-divider__text) {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  color: var(--el-text-color-secondary);
}
</style>
