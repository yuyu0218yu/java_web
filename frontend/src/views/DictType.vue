<template>
  <div class="dict-type-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-icon class="title-icon"><Collection /></el-icon>
          <span>字典类型管理</span>
        </div>
        <div class="action-buttons">
          <el-input
            v-model="searchForm.dictName"
            placeholder="字典名称"
            clearable
            style="width: 150px; margin-right: 10px;"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-input
            v-model="searchForm.dictType"
            placeholder="字典类型"
            clearable
            style="width: 150px; margin-right: 10px;"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-select v-model="searchForm.status" placeholder="状态" clearable style="width: 100px; margin-right: 10px;" @change="handleSearch">
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
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
        <el-table-column prop="dictName" label="字典名称" min-width="150" />
        <el-table-column prop="dictType" label="字典类型" min-width="150">
          <template #default="scope">
            <el-link type="primary" @click="goToData(scope.row)">
              {{ scope.row.dictType }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" effect="light">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="goToData(scope.row)">
              <el-icon><List /></el-icon>
              字典数据
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
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
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Collection, Search, Refresh, Plus, Edit, Delete, List } from '@element-plus/icons-vue'
import { dictTypeApi } from '@/api'

const router = useRouter()

// 数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const tableData = ref([])
const selectedRows = ref([])
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  dictName: '',
  dictType: '',
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
  dictName: '',
  dictType: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictType: [
    { required: true, message: '请输入字典类型', trigger: 'blur' },
    { pattern: /^[a-z_]+$/, message: '字典类型只能包含小写字母和下划线', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await dictTypeApi.getPage({
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
  Object.assign(searchForm, { dictName: '', dictType: '', status: null })
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

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增字典类型'
  Object.assign(form, { id: null, dictName: '', dictType: '', status: 1, remark: '' })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑字典类型'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除字典类型 [${row.dictName}] 吗？`, '删除确认', {
      type: 'warning'
    })
    await dictTypeApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (form.id) {
      await dictTypeApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await dictTypeApi.create(form)
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

// 跳转到字典数据页面
const goToData = (row) => {
  router.push({ path: '/dicts/data', query: { dictType: row.dictType, dictName: row.dictName } })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dict-type-page {
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
</style>
