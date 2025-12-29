<template>
  <div class="banner-manage-page">
    <el-card class="search-card">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 新增轮播图
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
        <el-table-column label="轮播图" width="250">
          <template #default="{ row }">
            <el-image :src="row.imageUrl" :preview-src-list="[row.imageUrl]" fit="cover" style="width: 200px; height: 100px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column label="链接类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getLinkTypeColor(row.linkType)" size="small">
              {{ getLinkTypeText(row.linkType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="linkUrl" label="跳转链接" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="100" sortable />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="handleDialogClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片URL" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
          <el-image v-if="form.imageUrl" :src="form.imageUrl" fit="cover" style="width: 100%; margin-top: 10px; border-radius: 4px;" />
        </el-form-item>
        <el-form-item label="链接类型" prop="linkType">
          <el-select v-model="form.linkType" placeholder="请选择链接类型" style="width: 100%">
            <el-option label="景点" :value="1" />
            <el-option label="攻略" :value="2" />
            <el-option label="外链" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="跳转链接" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" controls-position="right" style="width: 100%" />
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
import { Plus, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增轮播图')
const formRef = ref()
const tableData = ref([])
const selectedRows = ref([])

const page = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  linkType: 1,
  linkUrl: '',
  sortOrder: 0,
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请输入图片URL', trigger: 'blur' }],
  linkType: [{ required: true, message: '请选择链接类型', trigger: 'change' }]
}

const getLinkTypeText = (type) => {
  const types = { 1: '景点', 2: '攻略', 3: '外链' }
  return types[type] || '未知'
}

const getLinkTypeColor = (type) => {
  const colors = { 1: 'primary', 2: 'success', 3: 'warning' }
  return colors[type] || 'info'
}

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/banner/list', {
      params: { current: page.current, size: page.size }
    })
    tableData.value = res.data?.records || []
    page.total = res.data?.total || 0
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  dialogTitle.value = '新增轮播图'
  Object.assign(form, { id: null, title: '', imageUrl: '', linkType: 1, linkUrl: '', sortOrder: 0, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑轮播图'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (form.id) {
      await request.put(`/admin/banner/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/banner', form)
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
    await ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', { type: 'warning' })
    await request.delete(`/admin/banner/${row.id}`)
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
    await request.delete('/admin/banner/batch', { data: ids })
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('批量删除失败', error)
  }
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.banner-manage-page {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
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
