<template>
  <div class="permissions-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增权限
        </el-button>
        <el-button type="success" @click="handleExpandAll">
          <el-icon><Expand /></el-icon>
          展开全部
        </el-button>
        <el-button type="warning" @click="handleCollapseAll">
          <el-icon><Fold /></el-icon>
          折叠全部
        </el-button>
        <el-button type="info" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </div>
    </el-card>

    <!-- 权限树形表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="false"
        ref="tableRef"
      >
        <el-table-column prop="name" label="权限名称" width="200">
          <template #default="scope">
            <el-icon v-if="scope.row.type === 'menu'"><Menu /></el-icon>
            <el-icon v-else-if="scope.row.type === 'button'"><Pointer /></el-icon>
            <el-icon v-else><Key /></el-icon>
            <span style="margin-left: 8px;">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="权限编码" width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTagType(scope.row.type)" size="small">
              {{ getTypeLabel(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" width="150" />
        <el-table-column prop="component" label="组件" width="150" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="scope">
            <el-icon v-if="scope.row.icon"><component :is="scope.row.icon" /></el-icon>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleAdd(scope.row)">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        style="max-width: 500px"
      >
        <el-form-item label="上级权限" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="permissionTreeOptions"
            :props="treeSelectProps"
            placeholder="请选择上级权限"
            clearable
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="menu">菜单</el-radio>
            <el-radio label="button">按钮</el-radio>
            <el-radio label="api">接口</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="路径" prop="path" v-if="form.type === 'menu'">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件" prop="component" v-if="form.type === 'menu'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="form.type === 'menu'">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Expand, Fold, Download, Edit, Delete, Menu, Pointer, Key } from '@element-plus/icons-vue'
import { permissionApi } from '@/api'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const tableRef = ref()
const formRef = ref()

// 表单数据
const form = reactive({
  id: null,
  parentId: null,
  type: 'menu',
  name: '',
  code: '',
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  status: 1,
  remark: ''
})

// 表格数据
const tableData = ref([])
const permissionTreeOptions = ref([])

// 树形选择组件配置
const treeSelectProps = {
  children: 'children',
  label: 'name',
  value: 'id'
}

// 表单验证规则
const rules = {
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '请输入路径', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await permissionApi.getPermissionTree()
    tableData.value = response.data || []
    permissionTreeOptions.value = buildTreeSelectOptions(response.data || [])
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const buildTreeSelectOptions = (data) => {
  return [
    { id: 0, name: '根权限', children: data }
  ]
}

const getTypeLabel = (type) => {
  const typeMap = {
    menu: '菜单',
    button: '按钮',
    api: '接口'
  }
  return typeMap[type] || type
}

const getTypeTagType = (type) => {
  const typeMap = {
    menu: 'primary',
    button: 'warning',
    api: 'info'
  }
  return typeMap[type] || 'info'
}

const handleAdd = (row = null) => {
  dialogTitle.value = row ? '新增子权限' : '新增权限'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    parentId: row ? row.id : 0,
    type: 'menu',
    name: '',
    code: '',
    path: '',
    component: '',
    icon: '',
    sortOrder: 0,
    status: 1,
    remark: ''
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑权限'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    type: row.type,
    name: row.name,
    code: row.code,
    path: row.path,
    component: row.component,
    icon: row.icon,
    sortOrder: row.sortOrder,
    status: row.status,
    remark: row.remark
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await permissionApi.updatePermission(form.id, form)
      ElMessage.success('更新权限成功')
    } else {
      await permissionApi.createPermission(form)
      ElMessage.success('创建权限成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该权限吗？如果存在子权限，将一并删除。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await permissionApi.deletePermission(row.id)
    ElMessage.success('删除权限成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleExpandAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      tableRef.value.store.states.defaultExpandAll.value = true
      tableRef.value.store.toggleAllExpansion()
    }
  })
}

const handleCollapseAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      tableRef.value.store.states.defaultExpandAll.value = false
      tableRef.value.store.toggleAllExpansion()
    }
  })
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.permissions-page {
  padding: 0;
}

.action-card {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.table-card {
  min-height: 400px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
