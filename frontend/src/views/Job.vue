<template>
  <div class="job-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-icon class="title-icon"><Timer /></el-icon>
          <span>定时任务</span>
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
          <el-select v-model="searchForm.jobGroup" placeholder="任务组" clearable style="width: 120px; margin-right: 10px;" @change="handleSearch">
            <el-option label="默认" value="DEFAULT" />
            <el-option label="系统" value="SYSTEM" />
          </el-select>
          <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 100px; margin-right: 10px;" @change="handleSearch">
            <el-option label="正常" :value="1" />
            <el-option label="暂停" :value="0" />
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
          <el-button type="info" @click="goToLogs">
            <el-icon><Document /></el-icon>
            日志
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
        <el-table-column prop="id" label="任务ID" width="80" />
        <el-table-column prop="jobName" label="任务名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="jobGroup" label="任务组" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.jobGroup === 'SYSTEM' ? 'danger' : 'info'" effect="light">
              {{ scope.row.jobGroup === 'SYSTEM' ? '系统' : '默认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="invokeTarget" label="调用目标" min-width="200" show-overflow-tooltip />
        <el-table-column prop="cronExpression" label="cron表达式" width="150" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleRun(scope.row)">
              <el-icon><VideoPlay /></el-icon>
              执行
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button type="info" size="small" @click="handleDetail(scope.row)">
              <el-icon><View /></el-icon>
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
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务组" prop="jobGroup">
          <el-select v-model="form.jobGroup" placeholder="请选择任务组" style="width: 100%;">
            <el-option label="默认" value="DEFAULT" />
            <el-option label="系统" value="SYSTEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="调用目标" prop="invokeTarget">
          <el-input v-model="form.invokeTarget" placeholder="如：taskService.executeTask" />
          <div class="form-tip">Bean调用示例：beanName.methodName()</div>
        </el-form-item>
        <el-form-item label="cron表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" placeholder="如：0 0/5 * * * ?">
            <template #append>
              <el-button @click="checkCron">校验</el-button>
            </template>
          </el-input>
          <div class="form-tip">秒 分 时 日 月 周 [年]</div>
        </el-form-item>
        <el-form-item label="执行策略" prop="misfirePolicy">
          <el-radio-group v-model="form.misfirePolicy">
            <el-radio :label="1">立即执行</el-radio>
            <el-radio :label="2">执行一次</el-radio>
            <el-radio :label="3">放弃执行</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否并发" prop="concurrent">
          <el-radio-group v-model="form.concurrent">
            <el-radio :label="0">允许</el-radio>
            <el-radio :label="1">禁止</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">暂停</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
      title="任务详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务ID">{{ currentJob.id }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ currentJob.jobName }}</el-descriptions-item>
        <el-descriptions-item label="任务组">
          <el-tag :type="currentJob.jobGroup === 'SYSTEM' ? 'danger' : 'info'" effect="light">
            {{ currentJob.jobGroup === 'SYSTEM' ? '系统' : '默认' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentJob.status === 1 ? 'success' : 'info'" effect="light">
            {{ currentJob.status === 1 ? '正常' : '暂停' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="调用目标" :span="2">{{ currentJob.invokeTarget }}</el-descriptions-item>
        <el-descriptions-item label="cron表达式">{{ currentJob.cronExpression }}</el-descriptions-item>
        <el-descriptions-item label="执行策略">
          {{ getMisfireText(currentJob.misfirePolicy) }}
        </el-descriptions-item>
        <el-descriptions-item label="是否并发">{{ currentJob.concurrent === 0 ? '允许' : '禁止' }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ currentJob.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentJob.createTime }}</el-descriptions-item>
        <el-descriptions-item v-if="currentJob.remark" label="备注" :span="2">{{ currentJob.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Timer, Search, Refresh, Plus, Edit, Delete, View, VideoPlay, Document } from '@element-plus/icons-vue'
import { jobApi } from '@/api'

const router = useRouter()

// 数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const dialogTitle = ref('')
const tableData = ref([])
const selectedRows = ref([])
const currentJob = ref({})
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  jobName: '',
  jobGroup: '',
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
  jobName: '',
  jobGroup: 'DEFAULT',
  invokeTarget: '',
  cronExpression: '',
  misfirePolicy: 1,
  concurrent: 1,
  status: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  jobName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  invokeTarget: [{ required: true, message: '请输入调用目标', trigger: 'blur' }],
  cronExpression: [{ required: true, message: '请输入cron表达式', trigger: 'blur' }]
}

// 获取执行策略文本
const getMisfireText = (policy) => {
  const map = { 1: '立即执行', 2: '执行一次', 3: '放弃执行' }
  return map[policy] || '-'
}

// 跳转到日志页面
const goToLogs = () => {
  router.push('/jobs/logs')
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await jobApi.getPage({
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
  Object.assign(searchForm, { jobName: '', jobGroup: '', status: null })
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

// 状态变化
const handleStatusChange = async (row) => {
  try {
    if (row.status === 1) {
      await jobApi.resume(row.id)
      ElMessage.success('任务已恢复')
    } else {
      await jobApi.pause(row.id)
      ElMessage.success('任务已暂停')
    }
  } catch (error) {
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
    console.error('状态更新失败:', error)
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增任务'
  Object.assign(form, {
    id: null,
    jobName: '',
    jobGroup: 'DEFAULT',
    invokeTarget: '',
    cronExpression: '',
    misfirePolicy: 1,
    concurrent: 1,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑任务'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 查看详情
const handleDetail = (row) => {
  currentJob.value = row
  detailVisible.value = true
}

// 执行任务
const handleRun = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要立即执行任务 [${row.jobName}] 吗？`, '执行确认', {
      type: 'warning'
    })
    await jobApi.run(row.id)
    ElMessage.success('任务执行请求已发送')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('执行失败:', error)
    }
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除任务 [${row.jobName}] 吗？`, '删除确认', {
      type: 'warning'
    })
    await jobApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 校验cron表达式
const checkCron = async () => {
  if (!form.cronExpression) {
    ElMessage.warning('请先输入cron表达式')
    return
  }

  try {
    const response = await jobApi.checkCron(form.cronExpression)
    if (response.data.valid) {
      ElMessage.success('cron表达式有效')
    } else {
      ElMessage.error('cron表达式无效')
    }
  } catch (error) {
    ElMessage.error('校验失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (form.id) {
      await jobApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await jobApi.create(form)
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
.job-page {
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

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
