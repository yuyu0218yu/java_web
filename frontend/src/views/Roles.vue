<template>
  <div class="roles-page">
    <!-- 操作栏 -->
    <el-card class="action-card">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
        <el-button type="success" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </div>
    </el-card>

    <!-- 角色表格 -->
    <el-card class="table-card">
      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="warning" size="small" @click="handlePermissions(scope.row)">
              <el-icon><Key /></el-icon>
              权限
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        style="max-width: 400px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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

    <!-- 权限配置对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="权限配置"
      width="600px"
      @close="handlePermissionDialogClose"
    >
      <el-form label-width="80px">
        <el-form-item label="角色名称">
          <span>{{ currentRole?.roleName }}</span>
        </el-form-item>
        <el-form-item label="权限列表">
          <el-tree
            ref="permissionTreeRef"
            :data="permissionTree"
            :props="treeProps"
            show-checkbox
            node-key="id"
            :default-expanded-keys="[1, 2, 3]"
            style="max-height: 300px; overflow-y: auto;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="permissionLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Edit, Key, Delete } from '@element-plus/icons-vue'
import { roleApi, permissionApi } from '@/api'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const permissionLoading = ref(false)
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRole = ref(null)
const formRef = ref()
const permissionTreeRef = ref()

// 表单数据
const form = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
})

// 表格数据
const tableData = ref([])
const permissionTree = ref([])

// 树形组件配置
const treeProps = {
  children: 'children',
  label: 'name'
}

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[A-Z_]+$/, message: '角色编码只能包含大写字母和下划线', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await roleApi.getRoleList()
    tableData.value = response.data || []
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadPermissionTree = async () => {
  try {
    const response = await permissionApi.getPermissionTree()
    permissionTree.value = response.data || []
  } catch (error) {
    console.error('加载权限树失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    roleName: '',
    roleCode: '',
    description: '',
    status: 1
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    roleName: row.roleName,
    roleCode: row.roleCode,
    description: row.description,
    status: row.status
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await roleApi.updateRole(form.id, form)
      ElMessage.success('更新角色成功')
    } else {
      await roleApi.createRole(form)
      ElMessage.success('创建角色成功')
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
    await ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await roleApi.deleteRole(row.id)
    ElMessage.success('删除角色成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handlePermissions = (row) => {
  currentRole.value = row
  permissionDialogVisible.value = true
  
  // 模拟加载角色已有权限
  if (permissionTreeRef.value) {
    // 这里应该调用API获取角色已有权限并设置选中状态
    // permissionTreeRef.value.setCheckedKeys(rolePermissions)
  }
}

const handleSavePermissions = async () => {
  if (!permissionTreeRef.value || !currentRole.value) return
  
  try {
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys]
    
    // 这里应该调用API保存角色权限
    // await roleApi.updateRolePermissions(currentRole.value.id, allCheckedKeys)
    
    permissionLoading.value = true
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('权限配置保存成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('保存权限失败:', error)
  } finally {
    permissionLoading.value = false
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handlePermissionDialogClose = () => {
  currentRole.value = null
  if (permissionTreeRef.value) {
    permissionTreeRef.value.setCheckedKeys([])
  }
}

// 生命周期
onMounted(() => {
  loadData()
  loadPermissionTree()
})
</script>

<style scoped>
.roles-page {
  padding: 0;
}

.action-card {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 10px;
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
