<template>
  <div class="dict-data-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-header">
        <div class="page-title">
          <el-button type="text" @click="goBack" style="margin-right: 10px;">
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          <el-icon class="title-icon"><List /></el-icon>
          <span>字典数据管理</span>
          <el-tag v-if="currentDictName" type="info" style="margin-left: 10px;">
            {{ currentDictName }}
          </el-tag>
        </div>
        <div class="action-buttons">
          <el-input
            v-model="searchForm.dictLabel"
            placeholder="字典标签"
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
        <el-table-column prop="dictLabel" label="字典标签" min-width="150" />
        <el-table-column prop="dictValue" label="字典值" min-width="120" />
        <el-table-column prop="dictSort" label="排序" width="80" align="center" />
        <el-table-column prop="cssClass" label="样式属性" width="120" />
        <el-table-column prop="listClass" label="回显样式" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.listClass" :type="scope.row.listClass" effect="light">
              {{ scope.row.listClass }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" effect="light">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
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
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" disabled />
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入字典标签" />
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入字典值" />
        </el-form-item>
        <el-form-item label="排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" :min="0" :max="999" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="回显样式" prop="listClass">
          <el-select v-model="form.listClass" placeholder="请选择回显样式" clearable style="width: 100%;">
            <el-option label="默认(default)" value="" />
            <el-option label="主要(primary)" value="primary" />
            <el-option label="成功(success)" value="success" />
            <el-option label="信息(info)" value="info" />
            <el-option label="警告(warning)" value="warning" />
            <el-option label="危险(danger)" value="danger" />
          </el-select>
        </el-form-item>
        <el-form-item label="样式属性" prop="cssClass">
          <el-input v-model="form.cssClass" placeholder="请输入样式属性" />
        </el-form-item>
        <el-form-item label="是否默认" prop="isDefault">
          <el-radio-group v-model="form.isDefault">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { List, Search, Refresh, Plus, Edit, Delete, ArrowLeft } from '@element-plus/icons-vue'
import { dictDataApi } from '@/api'

const route = useRoute()
const router = useRouter()

// 从路由获取参数
const currentDictType = computed(() => route.query.dictType || '')
const currentDictName = computed(() => route.query.dictName || '')

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
  dictLabel: '',
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
  dictType: '',
  dictLabel: '',
  dictValue: '',
  dictSort: 0,
  cssClass: '',
  listClass: '',
  isDefault: 0,
  status: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典值', trigger: 'blur' }]
}

// 返回上一页
const goBack = () => {
  router.push('/dicts')
}

// 加载数据
const loadData = async () => {
  if (!currentDictType.value) {
    ElMessage.warning('请先选择字典类型')
    return
  }

  loading.value = true
  try {
    const response = await dictDataApi.getPage({
      current: pagination.current,
      size: pagination.size,
      dictType: currentDictType.value,
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
  Object.assign(searchForm, { dictLabel: '', status: null })
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
  dialogTitle.value = '新增字典数据'
  Object.assign(form, {
    id: null,
    dictType: currentDictType.value,
    dictLabel: '',
    dictValue: '',
    dictSort: 0,
    cssClass: '',
    listClass: '',
    isDefault: 0,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑字典数据'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除字典数据 [${row.dictLabel}] 吗？`, '删除确认', {
      type: 'warning'
    })
    await dictDataApi.delete(row.id)
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
      await dictDataApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await dictDataApi.create(form)
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
  if (currentDictType.value) {
    loadData()
  }
})
</script>

<style scoped>
.dict-data-page {
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
