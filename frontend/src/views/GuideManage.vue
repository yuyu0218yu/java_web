<template>
  <div class="guide-manage-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.guideType" placeholder="请选择类型" clearable style="width: 150px">
            <el-option label="攻略" :value="1" />
            <el-option label="游记" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="待审核" :value="0" />
            <el-option label="已发布" :value="1" />
            <el-option label="已下架" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 新增攻略
        </el-button>
        <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon> 批量删除
        </el-button>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" sortable />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" :preview-src-list="[row.coverImage]" fit="cover" style="width: 60px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.guideType === 1 ? 'primary' : 'success'" size="small">
              {{ row.guideType === 1 ? '攻略' : '游记' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column prop="viewCount" label="浏览量" width="100" sortable />
        <el-table-column prop="likeCount" label="点赞数" width="100" sortable />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.isRecommend" :active-value="1" :inactive-value="0" @change="handleRecommendChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 0" type="success" link @click="handleApprove(row)">审核</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadData"
        @size-change="loadData"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 攻略详情对话框 -->
    <el-dialog v-model="detailVisible" title="攻略详情" width="800px">
      <div v-if="currentGuide">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="标题" :span="2">{{ currentGuide.title }}</el-descriptions-item>
          <el-descriptions-item label="类型">
            <el-tag :type="currentGuide.guideType === 1 ? 'primary' : 'success'" size="small">
              {{ currentGuide.guideType === 1 ? '攻略' : '游记' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="作者">{{ currentGuide.authorName }}</el-descriptions-item>
          <el-descriptions-item label="浏览量">{{ currentGuide.viewCount }}</el-descriptions-item>
          <el-descriptions-item label="点赞数">{{ currentGuide.likeCount }}</el-descriptions-item>
          <el-descriptions-item label="评论数">{{ currentGuide.commentCount }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentGuide.status)" size="small">
              {{ getStatusText(currentGuide.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentGuide.createTime }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <div class="guide-content" v-html="currentGuide.content"></div>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog v-model="approveVisible" title="审核攻略" width="500px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="approveForm.status">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="2">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="approveForm.remark" type="textarea" :rows="4" placeholder="审核备注（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitApprove">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const detailVisible = ref(false)
const approveVisible = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const currentGuide = ref(null)

const searchForm = reactive({
  title: '',
  guideType: null,
  status: null
})

const page = reactive({
  current: 1,
  size: 10,
  total: 0
})

const approveForm = reactive({
  id: null,
  status: 1,
  remark: ''
})

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待审核', 1: '已发布', 2: '已下架' }
  return texts[status] || '未知'
}

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/guide/list', {
      params: { current: page.current, size: page.size, ...searchForm }
    })
    tableData.value = res.data?.records || []
    page.total = res.data?.total || 0
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, { title: '', guideType: null, status: null })
  handleSearch()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  ElMessage.info('请到用户端发布攻略')
}

const handleView = (row) => {
  currentGuide.value = row
  detailVisible.value = true
}

const handleApprove = (row) => {
  approveForm.id = row.id
  approveForm.status = 1
  approveForm.remark = ''
  approveVisible.value = true
}

const handleSubmitApprove = async () => {
  try {
    await request.put(`/admin/guide/${approveForm.id}/audit`, null, {
      params: { status: approveForm.status }
    })
    ElMessage.success('审核成功')
    approveVisible.value = false
    loadData()
  } catch (error) {
    console.error('审核失败', error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该攻略吗？', '提示', { type: 'warning' })
    await request.delete(`/admin/guide/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('删除失败', error)
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条数据吗？`, '提示', { type: 'warning' })
    const ids = selectedRows.value.map(row => row.id)
    await request.delete('/admin/guide/batch', { data: ids })
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('批量删除失败', error)
  }
}

const handleRecommendChange = async (row) => {
  try {
    await request.put(`/admin/guide/${row.id}`, { isRecommend: row.isRecommend })
    ElMessage.success('更新成功')
  } catch (error) {
    console.error('更新失败', error)
    row.isRecommend = row.isRecommend === 1 ? 0 : 1
  }
}
</script>

<style scoped lang="scss">
.guide-manage-page {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .search-form {
    margin-bottom: 16px;
  }

  .action-buttons {
    display: flex;
    gap: 10px;
  }

  .table-card {
    min-height: 600px;
  }

  .guide-content {
    padding: 20px;
    background: #f5f7fa;
    border-radius: 4px;
    max-height: 400px;
    overflow-y: auto;

    :deep(img) {
      max-width: 100%;
      border-radius: 4px;
    }
  }
}
</style>
