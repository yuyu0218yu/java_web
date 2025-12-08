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
          highlight-current-row
          stripe
        >
          <el-table-column type="index" label="#" width="60" />
          <el-table-column prop="tableName" label="表名" width="180">
            <template #default="scope">
              <el-tag effect="plain" type="info">
                <el-icon style="margin-right: 4px;"><Grid /></el-icon>
                {{ scope.row.tableName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tableComment" label="表注释" min-width="150">
            <template #default="scope">
              <span>{{ scope.row.tableComment || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="className" label="实体类名" width="120" />
          <el-table-column prop="updateTime" label="更新时间" width="180">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ formatTime(scope.row.updateTime) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="预览代码" placement="top">
                  <el-button type="primary" size="small" circle @click="handlePreview(scope.row)">
                    <el-icon><View /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="下载代码" placement="top">
                  <el-button type="warning" size="small" circle @click="handleDownload(scope.row)">
                    <el-icon><Download /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="编辑配置" placement="top">
                  <el-button type="info" size="small" circle @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="同步表结构" placement="top">
                  <el-button type="success" size="small" circle @click="handleSync(scope.row)">
                    <el-icon><RefreshRight /></el-icon>
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
          <pre class="code-preview" :class="{ 'code-dark': isDark, 'code-light': !isDark }"><code v-html="highlightCode(code, name)"></code></pre>
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

    <!-- 编辑表配置对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑表配置"
      width="85%"
      top="3vh"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-tabs v-model="editActiveTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="editForm" label-width="120px" style="max-width: 800px; margin: 0 auto;">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="表名称">
                  <el-input v-model="editForm.tableName" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="表描述">
                  <el-input v-model="editForm.tableComment" placeholder="请输入表描述" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="实体类名称">
                  <el-input v-model="editForm.className" placeholder="请输入实体类名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作者">
                  <el-input v-model="editForm.functionAuthor" placeholder="请输入作者" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="生成包路径">
                  <el-input v-model="editForm.packageName" placeholder="com.xxx.xxx" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成模块名">
                  <el-input v-model="editForm.moduleName" placeholder="请输入模块名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="业务名">
                  <el-input v-model="editForm.businessName" placeholder="请输入业务名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="功能名">
                  <el-input v-model="editForm.functionName" placeholder="请输入功能名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="生成模板">
                  <el-select v-model="editForm.tplCategory" placeholder="请选择" style="width: 100%">
                    <el-option label="单表（CRUD）" value="crud" />
                    <el-option label="树表" value="tree" />
                    <el-option label="主子表" value="sub" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="前端类型">
                  <el-select v-model="editForm.tplWebType" placeholder="请选择" style="width: 100%">
                    <el-option label="Element Plus" value="element-plus" />
                    <el-option label="Element UI" value="element-ui" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 字段配置 -->
        <el-tab-pane label="字段配置" name="columns">
          <el-table :data="editForm.columns" border style="width: 100%" max-height="500">
            <el-table-column prop="columnName" label="字段名" width="120" fixed />
            <el-table-column prop="columnComment" label="字段描述" width="140">
              <template #default="scope">
                <el-input v-model="scope.row.columnComment" size="small" />
              </template>
            </el-table-column>
            <el-table-column prop="columnType" label="物理类型" width="100" />
            <el-table-column prop="javaType" label="Java类型" width="130">
              <template #default="scope">
                <el-select v-model="scope.row.javaType" size="small">
                  <el-option label="Long" value="Long" />
                  <el-option label="String" value="String" />
                  <el-option label="Integer" value="Integer" />
                  <el-option label="Double" value="Double" />
                  <el-option label="BigDecimal" value="BigDecimal" />
                  <el-option label="LocalDateTime" value="LocalDateTime" />
                  <el-option label="LocalDate" value="LocalDate" />
                  <el-option label="Boolean" value="Boolean" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="javaField" label="Java字段" width="120">
              <template #default="scope">
                <el-input v-model="scope.row.javaField" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="插入" width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isInsert" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isEdit" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="列表" width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isList" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="查询" width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isQuery" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="必填" width="60" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isRequired" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column prop="queryType" label="查询方式" width="110">
              <template #default="scope">
                <el-select v-model="scope.row.queryType" size="small">
                  <el-option label="=" value="EQ" />
                  <el-option label="!=" value="NE" />
                  <el-option label=">" value="GT" />
                  <el-option label=">=" value="GE" />
                  <el-option label="<" value="LT" />
                  <el-option label="<=" value="LE" />
                  <el-option label="LIKE" value="LIKE" />
                  <el-option label="BETWEEN" value="BETWEEN" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="htmlType" label="显示类型" width="130">
              <template #default="scope">
                <el-select v-model="scope.row.htmlType" size="small">
                  <el-option label="文本框" value="input" />
                  <el-option label="文本域" value="textarea" />
                  <el-option label="下拉框" value="select" />
                  <el-option label="单选框" value="radio" />
                  <el-option label="复选框" value="checkbox" />
                  <el-option label="日期控件" value="datetime" />
                  <el-option label="图片上传" value="image" />
                  <el-option label="文件上传" value="upload" />
                  <el-option label="富文本" value="editor" />
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm" :loading="editSubmitting">
          <el-icon><Check /></el-icon>
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Cpu, Refresh, RefreshRight, Grid, Clock, View, Download, Search, Plus, Edit, Delete, Check } from '@element-plus/icons-vue'
import { generatorApi } from '@/api'
import hljs from 'highlight.js/lib/core'
import java from 'highlight.js/lib/languages/java'
import xml from 'highlight.js/lib/languages/xml'
import javascript from 'highlight.js/lib/languages/javascript'
import sql from 'highlight.js/lib/languages/sql'

// 注册语言
hljs.registerLanguage('java', java)
hljs.registerLanguage('xml', xml)
hljs.registerLanguage('vue', xml)
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('sql', sql)

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

// 编辑对话框
const editDialogVisible = ref(false)
const editActiveTab = ref('basic')
const editForm = ref({})
const editSubmitting = ref(false)

// 主题状态
const isDark = ref(document.documentElement.classList.contains('dark'))

// 监听主题变化
const themeObserver = new MutationObserver(() => {
  isDark.value = document.documentElement.classList.contains('dark')
})

// 根据文件名获取语言
const getLanguage = (fileName) => {
  if (fileName.endsWith('.java')) return 'java'
  if (fileName.endsWith('.xml')) return 'xml'
  if (fileName.endsWith('.vue')) return 'vue'
  if (fileName.endsWith('.js')) return 'javascript'
  if (fileName.endsWith('.sql')) return 'sql'
  return 'plaintext'
}

// 高亮代码
const highlightCode = (code, fileName) => {
  const lang = getLanguage(fileName)
  try {
    if (lang === 'plaintext') return code
    return hljs.highlight(code, { language: lang }).value
  } catch {
    return code
  }
}

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
    const res = await generatorApi.getDbTableList({
      tableName: '',
      tableComment: ''
    })
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
    await generatorApi.downloadCode(row.tableId, row.tableName)
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
    await generatorApi.downloadCode(currentTable.value.tableId, currentTable.value.tableName)
    ElMessage.success('代码下载成功！')
  } catch (error) {
    console.error('下载失败', error)
    ElMessage.error('下载失败: ' + (error.message || '未知错误'))
  } finally {
    downloading.value = false
  }
}

const handleEdit = async (row) => {
  try {
    const res = await generatorApi.getTableById(row.tableId)
    editForm.value = res.data || {}
    editActiveTab.value = 'basic'
    editDialogVisible.value = true
  } catch (error) {
    console.error('获取表详情失败', error)
    ElMessage.error('获取表详情失败')
  }
}

const submitEditForm = async () => {
  editSubmitting.value = true
  try {
    await generatorApi.updateTable(editForm.value)
    ElMessage.success('保存成功！')
    editDialogVisible.value = false
    loadTables()
  } catch (error) {
    console.error('保存失败', error)
    ElMessage.error('保存失败: ' + (error.message || '未知错误'))
  } finally {
    editSubmitting.value = false
  }
}

const handleSync = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要同步表 "${row.tableName}" 的结构吗？这将从数据库重新读取字段信息。`,
      '同步确认',
      { type: 'warning' }
    )

    await generatorApi.syncDb(row.tableId)
    ElMessage.success('同步成功！')
    loadTables()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('同步失败', error)
      ElMessage.error('同步失败: ' + (error.message || '未知错误'))
    }
  }
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
  // 监听主题变化
  themeObserver.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['class']
  })
})

onUnmounted(() => {
  themeObserver.disconnect()
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

/* 代码预览基础样式 */
.code-preview {
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  max-height: 60vh;
  font-family: 'Consolas', 'Monaco', 'Fira Code', monospace;
  font-size: 13px;
  line-height: 1.6;
  margin: 0;
  tab-size: 2;
}

/* 暗黑模式 - VS Code Dark+ 风格 */
.code-dark {
  background: #1e1e1e;
  color: #d4d4d4;
}
.code-dark :deep(.hljs-keyword) { color: #569cd6; }
.code-dark :deep(.hljs-string) { color: #ce9178; }
.code-dark :deep(.hljs-number) { color: #b5cea8; }
.code-dark :deep(.hljs-comment) { color: #6a9955; font-style: italic; }
.code-dark :deep(.hljs-class),
.code-dark :deep(.hljs-title) { color: #4ec9b0; }
.code-dark :deep(.hljs-function) { color: #dcdcaa; }
.code-dark :deep(.hljs-variable),
.code-dark :deep(.hljs-attr) { color: #9cdcfe; }
.code-dark :deep(.hljs-tag) { color: #569cd6; }
.code-dark :deep(.hljs-name) { color: #569cd6; }
.code-dark :deep(.hljs-attribute) { color: #9cdcfe; }
.code-dark :deep(.hljs-built_in) { color: #4ec9b0; }
.code-dark :deep(.hljs-type) { color: #4ec9b0; }
.code-dark :deep(.hljs-params) { color: #9cdcfe; }
.code-dark :deep(.hljs-meta) { color: #c586c0; }
.code-dark :deep(.hljs-literal) { color: #569cd6; }

/* 白天模式 - GitHub 风格 */
.code-light {
  background: #f6f8fa;
  color: #24292e;
}
.code-light :deep(.hljs-keyword) { color: #d73a49; }
.code-light :deep(.hljs-string) { color: #032f62; }
.code-light :deep(.hljs-number) { color: #005cc5; }
.code-light :deep(.hljs-comment) { color: #6a737d; font-style: italic; }
.code-light :deep(.hljs-class),
.code-light :deep(.hljs-title) { color: #6f42c1; }
.code-light :deep(.hljs-function) { color: #6f42c1; }
.code-light :deep(.hljs-variable),
.code-light :deep(.hljs-attr) { color: #005cc5; }
.code-light :deep(.hljs-tag) { color: #22863a; }
.code-light :deep(.hljs-name) { color: #22863a; }
.code-light :deep(.hljs-attribute) { color: #6f42c1; }
.code-light :deep(.hljs-built_in) { color: #e36209; }
.code-light :deep(.hljs-type) { color: #005cc5; }
.code-light :deep(.hljs-params) { color: #24292e; }
.code-light :deep(.hljs-meta) { color: #d73a49; }
.code-light :deep(.hljs-literal) { color: #005cc5; }

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
