<template>
  <div class="scenic-manage-page">
    <!-- 搜索和操作栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="景点名称">
          <el-input v-model="searchForm.scenicName" placeholder="请输入景点名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable style="width: 150px">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
          <el-icon><Plus /></el-icon> 新增景点
        </el-button>
        <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon> 批量删除
        </el-button>
      </div>
    </el-card>

    <!-- 景点表格 -->
    <el-card class="table-card">
      <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" sortable />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" :preview-src-list="[row.coverImage]" fit="cover" style="width: 60px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="scenicName" label="景点名称" width="180" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column label="评分" width="100">
          <template #default="{ row }">
            <el-rate :model-value="row.rating" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" sortable />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.isRecommend" :active-value="1" :inactive-value="0" @change="handleRecommendChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleManageTickets(row)">门票管理</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" @close="handleDialogClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="景点名称" prop="scenicName">
          <el-input v-model="form.scenicName" placeholder="请输入景点名称" />
        </el-form-item>
        <el-form-item label="景点编码" prop="scenicCode">
          <el-input v-model="form.scenicCode" placeholder="请输入景点编码" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverImage">
          <el-input v-model="form.coverImage" placeholder="请输入封面图片URL" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="开放时间" prop="openTime">
          <el-input v-model="form.openTime" placeholder="例如: 08:00-18:00" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="景点描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入景点描述" />
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="form.rating" show-score />
        </el-form-item>
        <el-form-item label="是否热门">
          <el-switch v-model="form.isHot" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="是否推荐">
          <el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
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
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增景点')
const formRef = ref()
const tableData = ref([])
const selectedRows = ref([])
const categories = ref([])

const searchForm = reactive({
  scenicName: '',
  categoryId: null,
  status: null
})

const page = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  scenicName: '',
  scenicCode: '',
  categoryId: null,
  coverImage: '',
  address: '',
  openTime: '',
  phone: '',
  description: '',
  rating: 5,
  isHot: 0,
  isRecommend: 0,
  status: 1
})

const rules = {
  scenicName: [{ required: true, message: '请输入景点名称', trigger: 'blur' }],
  scenicCode: [{ required: true, message: '请输入景点编码', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

onMounted(() => {
  loadCategories()
  loadData()
})

const loadCategories = async () => {
  try {
    const res = await request.get('/admin/scenic/categories')
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/scenic/list', {
      params: {
        current: page.current,
        size: page.size,
        ...searchForm
      }
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
  Object.assign(searchForm, { scenicName: '', categoryId: null, status: null })
  handleSearch()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  dialogTitle.value = '新增景点'
  Object.assign(form, { id: null, scenicName: '', scenicCode: '', categoryId: null, coverImage: '', address: '', openTime: '', phone: '', description: '', rating: 5, isHot: 0, isRecommend: 0, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑景点'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (form.id) {
      await request.put(`/admin/scenic/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/scenic', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('操作失败', error)
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该景点吗？', '提示', { type: 'warning' })
    await request.delete(`/admin/scenic/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条数据吗？`, '提示', { type: 'warning' })
    const ids = selectedRows.value.map(row => row.id)
    await request.delete('/admin/scenic/batch', { data: ids })
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败', error)
    }
  }
}

const handleRecommendChange = async (row) => {
  try {
    await request.put(`/admin/scenic/${row.id}`, { isRecommend: row.isRecommend })
    ElMessage.success('更新成功')
  } catch (error) {
    console.error('更新失败', error)
    row.isRecommend = row.isRecommend === 1 ? 0 : 1
  }
}

const handleManageTickets = (row) => {
  ElMessage.info(`管理【${row.scenicName}】的门票`)
  // 跳转到门票管理页面，传递景点ID
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.scenic-manage-page {
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
}
</style>
