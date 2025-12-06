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
              共 {{ tableList.length }} 张表
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleRefresh" class="action-btn">
              <el-icon><Refresh /></el-icon>
              刷新表列表
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 表列表 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="filteredTables"
          style="width: 100%"
          table-layout="auto"
          highlight-current-row
          stripe
          @row-click="handleSelectTable"
        >
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
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ formatTime(scope.row.createTime) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <div class="operation-buttons">
                <el-button type="primary" size="small" @click.stop="handlePreview(scope.row)">
                  <el-icon><View /></el-icon>
                  预览
                </el-button>
                <el-button type="success" size="small" @click.stop="handleGenerate(scope.row)">
                  <el-icon><Download /></el-icon>
                  生成
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 搜索 -->
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索表名..."
            clearable
            style="width: 300px"
            :prefix-icon="Search"
          />
        </div>
      </el-card>
    </transition>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="代码预览"
      width="80%"
      top="5vh"
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
        <el-button type="success" @click="confirmGenerate" :loading="generating">
          <el-icon><Download /></el-icon>
          确认生成代码
        </el-button>
      </template>
    </el-dialog>

    <!-- 生成配置对话框 -->
    <el-dialog
      v-model="configDialogVisible"
      title="生成配置"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="generateOptions" label-width="120px">
        <el-form-item label="表名">
          <el-tag type="info">{{ selectedTable?.tableName }}</el-tag>
        </el-form-item>
        <el-form-item label="移除表前缀">
          <el-input v-model="generateOptions.tablePrefix" placeholder="如: sys_, t_" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="generateOptions.author" />
        </el-form-item>
        <el-divider content-position="left">生成选项</el-divider>
        <el-form-item label="生成文件">
          <el-checkbox-group v-model="generateFiles">
            <el-checkbox label="entity">Entity</el-checkbox>
            <el-checkbox label="mapper">Mapper</el-checkbox>
            <el-checkbox label="service">Service</el-checkbox>
            <el-checkbox label="controller">Controller</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="代码选项">
          <el-checkbox v-model="generateOptions.enableSwagger">启用Swagger</el-checkbox>
          <el-checkbox v-model="generateOptions.enableLombok">启用Lombok</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doPreview" :loading="previewing">
          <el-icon><View /></el-icon>
          预览代码
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Cpu, Refresh, Grid, Clock, View, Download, Search } from '@element-plus/icons-vue'
import { generatorApi } from '@/api'

// 响应式数据
const loading = ref(false)
const tableList = ref([])
const searchKeyword = ref('')
const selectedTable = ref(null)

// 对话框
const configDialogVisible = ref(false)
const previewDialogVisible = ref(false)
const previewCode = ref({})
const activeTab = ref('Entity')
const previewing = ref(false)
const generating = ref(false)

// 生成选项
const generateOptions = ref({
  tablePrefix: 'sys_',
  author: 'yushuang',
  enableSwagger: true,
  enableLombok: true,
  generateEntity: true,
  generateMapper: true,
  generateService: true,
  generateController: true
})

const generateFiles = ref(['entity', 'mapper', 'service', 'controller'])

// 计算属性
const filteredTables = computed(() => {
  if (!searchKeyword.value) return tableList.value
  return tableList.value.filter(t => 
    t.tableName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 方法
const loadTables = async () => {
  loading.value = true
  try {
    const res = await generatorApi.getTableList()
    tableList.value = res.data || []
  } catch (error) {
    console.error('加载表列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleRefresh = () => {
  loadTables()
}

const handleSelectTable = (row) => {
  selectedTable.value = row
}

const handlePreview = (row) => {
  selectedTable.value = row
  configDialogVisible.value = true
}

const handleGenerate = (row) => {
  selectedTable.value = row
  configDialogVisible.value = true
}

const doPreview = async () => {
  if (!selectedTable.value) return
  
  previewing.value = true
  try {
    // 更新选项
    generateOptions.value.generateEntity = generateFiles.value.includes('entity')
    generateOptions.value.generateMapper = generateFiles.value.includes('mapper')
    generateOptions.value.generateService = generateFiles.value.includes('service')
    generateOptions.value.generateController = generateFiles.value.includes('controller')
    
    const res = await generatorApi.previewCode(
      selectedTable.value.tableName,
      generateOptions.value
    )
    previewCode.value = res.data || {}
    activeTab.value = Object.keys(previewCode.value)[0] || 'Entity'
    configDialogVisible.value = false
    previewDialogVisible.value = true
  } catch (error) {
    console.error('预览失败', error)
    ElMessage.error('预览失败')
  } finally {
    previewing.value = false
  }
}

const confirmGenerate = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要生成代码吗？这将覆盖已存在的同名文件。',
      '确认生成',
      { type: 'warning' }
    )
    
    generating.value = true
    await generatorApi.generateCode(
      selectedTable.value.tableName,
      generateOptions.value
    )
    ElMessage.success('代码生成成功！请刷新项目查看生成的文件。')
    previewDialogVisible.value = false
  } catch (error) {
    if (error !== 'cancel') {
      console.error('生成失败', error)
      ElMessage.error('生成失败')
    }
  } finally {
    generating.value = false
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
  position: absolute;
  top: 16px;
  right: 20px;
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
</style>
