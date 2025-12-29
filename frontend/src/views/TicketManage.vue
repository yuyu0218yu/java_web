<template>
  <div class="ticket-manage-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="景点">
          <el-select v-model="searchForm.scenicId" placeholder="请选择景点" clearable style="width: 200px" filterable>
            <el-option v-for="scenic in scenics" :key="scenic.id" :label="scenic.scenicName" :value="scenic.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="门票类型">
          <el-select v-model="searchForm.ticketType" placeholder="请选择类型" clearable style="width: 150px">
            <el-option label="成人票" :value="1" />
            <el-option label="儿童票" :value="2" />
            <el-option label="老年票" :value="3" />
            <el-option label="学生票" :value="4" />
            <el-option label="套票" :value="5" />
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
          <el-icon><Plus /></el-icon> 新增门票
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
        <el-table-column prop="scenicName" label="景点名称" width="180" />
        <el-table-column prop="ticketName" label="门票名称" width="180" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getTicketTypeName(row.ticketType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="价格" width="150">
          <template #default="{ row }">
            <div>
              <span style="color: #f56c6c; font-weight: bold;">¥{{ row.sellingPrice }}</span>
              <span v-if="row.originalPrice > row.sellingPrice" style="text-decoration: line-through; color: #999; margin-left: 8px; font-size: 12px;">
                ¥{{ row.originalPrice }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" sortable />
        <el-table-column prop="validDays" label="有效天数" width="100" />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" @close="handleDialogClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="景点" prop="scenicId">
          <el-select v-model="form.scenicId" placeholder="请选择景点" style="width: 100%" filterable>
            <el-option v-for="scenic in scenics" :key="scenic.id" :label="scenic.scenicName" :value="scenic.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="门票名称" prop="ticketName">
          <el-input v-model="form.ticketName" placeholder="例如：成人票" />
        </el-form-item>
        <el-form-item label="门票类型" prop="ticketType">
          <el-select v-model="form.ticketType" placeholder="请选择类型" style="width: 100%">
            <el-option label="成人票" :value="1" />
            <el-option label="儿童票" :value="2" />
            <el-option label="老年票" :value="3" />
            <el-option label="学生票" :value="4" />
            <el-option label="套票" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :min="0" :precision="2" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="售价" prop="sellingPrice">
          <el-input-number v-model="form.sellingPrice" :min="0" :precision="2" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="有效天数" prop="validDays">
          <el-input-number v-model="form.validDays" :min="1" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="使用规则">
          <el-input v-model="form.useRules" type="textarea" :rows="3" placeholder="请输入使用规则" />
        </el-form-item>
        <el-form-item label="购票须知">
          <el-input v-model="form.notice" type="textarea" :rows="3" placeholder="请输入购票须知" />
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
const dialogTitle = ref('新增门票')
const formRef = ref()
const tableData = ref([])
const selectedRows = ref([])
const scenics = ref([])

const searchForm = reactive({
  scenicId: null,
  ticketType: null
})

const page = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  scenicId: null,
  ticketName: '',
  ticketType: 1,
  originalPrice: 0,
  sellingPrice: 0,
  stock: 9999,
  validDays: 1,
  useRules: '',
  notice: '',
  status: 1
})

const rules = {
  scenicId: [{ required: true, message: '请选择景点', trigger: 'change' }],
  ticketName: [{ required: true, message: '请输入门票名称', trigger: 'blur' }],
  ticketType: [{ required: true, message: '请选择门票类型', trigger: 'change' }],
  sellingPrice: [{ required: true, message: '请输入售价', trigger: 'blur' }]
}

const getTicketTypeName = (type) => {
  const types = { 1: '成人票', 2: '儿童票', 3: '老年票', 4: '学生票', 5: '套票' }
  return types[type] || '未知'
}

onMounted(() => {
  loadScenics()
  loadData()
})

const loadScenics = async () => {
  try {
    const res = await request.get('/portal/scenic/list', { params: { current: 1, size: 1000, status: 1 } })
    scenics.value = res.data?.records || []
  } catch (error) {
    console.error('加载景点失败', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/ticket/list', {
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
  Object.assign(searchForm, { scenicId: null, ticketType: null })
  handleSearch()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleAdd = () => {
  dialogTitle.value = '新增门票'
  Object.assign(form, { id: null, scenicId: null, ticketName: '', ticketType: 1, originalPrice: 0, sellingPrice: 0, stock: 9999, validDays: 1, useRules: '', notice: '', status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑门票'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (form.id) {
      await request.put(`/admin/ticket/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await request.post('/admin/ticket', form)
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
    await ElMessageBox.confirm('确定要删除该门票吗？', '提示', { type: 'warning' })
    await request.delete(`/admin/ticket/${row.id}`)
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
    await request.delete('/admin/ticket/batch', { data: ids })
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
.ticket-manage-page {
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
