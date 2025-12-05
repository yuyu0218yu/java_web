<template>
  <div class="profile-container">
    <!-- 个人信息卡片 -->
    <el-card class="info-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button
            type="primary"
            :icon="Edit"
            @click="showEditDialog = true"
            v-if="!editMode"
          >
            编辑资料
          </el-button>
        </div>
      </template>

      <div class="profile-content">
        <div class="avatar-section">
          <el-avatar :size="120" :src="profileData.avatar">
            {{ profileData.username?.charAt(0)?.toUpperCase() || 'U' }}
          </el-avatar>
          <h2>{{ profileData.username }}</h2>
          <p class="role-tag">{{ getRoleLabel(profileData.roleName) }}</p>
        </div>

        <el-divider />

        <div class="info-grid">
          <div class="info-item">
            <label>用户名</label>
            <span>{{ profileData.username }}</span>
          </div>
          <div class="info-item">
            <label>昵称</label>
            <span>{{ profileData.nickname || '未设置' }}</span>
          </div>
          <div class="info-item">
            <label>邮箱</label>
            <span>{{ profileData.email || '未设置' }}</span>
          </div>
          <div class="info-item">
            <label>手机号</label>
            <span>{{ profileData.phone || '未设置' }}</span>
          </div>
          <div class="info-item">
            <label>角色</label>
            <span>{{ getRoleLabel(profileData.roleName) }}</span>
          </div>
          <div class="info-item">
            <label>创建时间</label>
            <span>{{ formatDate(profileData.createdAt) }}</span>
          </div>
        </div>

        <el-divider />

        <div class="action-buttons">
          <el-button type="primary" :icon="Lock" @click="showPasswordDialog = true">
            修改密码
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 编辑个人信息对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑个人信息"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="80px">
        <el-form-item label="昵称" prop="nickname">
          <el-input
            v-model="editForm.nickname"
            placeholder="请输入昵称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="editForm.phone"
            placeholder="请输入手机号"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item label="头像链接" prop="avatar">
          <el-input
            v-model="editForm.avatar"
            placeholder="请输入头像URL"
            maxlength="255"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProfile" :loading="updating">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showPasswordDialog"
      title="修改密码"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码(6-50位)"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="changingPassword">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Lock } from '@element-plus/icons-vue'
import { authApi } from '@/api/index'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

// 个人信息数据
const profileData = ref({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
  roleName: '',
  createdAt: ''
})

// 编辑对话框
const showEditDialog = ref(false)
const editMode = ref(false)
const updating = ref(false)
const editFormRef = ref(null)
const editForm = reactive({
  nickname: '',
  phone: '',
  avatar: ''
})

// 编辑表单验证规则
const editRules = {
  nickname: [
    { max: 50, message: '昵称长度不能超过50个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  avatar: [
    { max: 255, message: '头像链接长度不能超过255个字符', trigger: 'blur' }
  ]
}

// 修改密码对话框
const showPasswordDialog = ref(false)
const changingPassword = ref(false)
const passwordFormRef = ref(null)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 50, message: '密码长度需在6-50之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取个人信息
const loadProfile = async () => {
  try {
    const response = await authApi.getProfile()
    profileData.value = response.data

    // 同步到编辑表单
    editForm.nickname = profileData.value.nickname || ''
    editForm.phone = profileData.value.phone || ''
    editForm.avatar = profileData.value.avatar || ''
  } catch (error) {
    ElMessage.error('获取个人信息失败: ' + (error.message || '未知错误'))
  }
}

// 更新个人信息
const handleUpdateProfile = async () => {
  if (!editFormRef.value) return

  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      updating.value = true
      try {
        await authApi.updateProfile(editForm)
        ElMessage.success('更新成功')
        showEditDialog.value = false

        // 重新加载个人信息
        await loadProfile()

        // 更新 store 中的用户信息
        await authStore.getUserInfo()
      } catch (error) {
        ElMessage.error('更新失败: ' + (error.message || '未知错误'))
      } finally {
        updating.value = false
      }
    }
  })
}

// 修改密码
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      changingPassword.value = true
      try {
        await authApi.changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码修改成功,请重新登录')
        showPasswordDialog.value = false

        // 重置表单
        passwordForm.oldPassword = ''
        passwordForm.newPassword = ''
        passwordForm.confirmPassword = ''

        // 延迟后退出登录
        setTimeout(() => {
          authStore.logout()
          window.location.href = '/login'
        }, 1500)
      } catch (error) {
        ElMessage.error('修改失败: ' + (error.message || '未知错误'))
      } finally {
        changingPassword.value = false
      }
    }
  })
}

// 获取角色标签
const getRoleLabel = (roleName) => {
  if (!roleName) return '未知角色'

  const roleMap = {
    'ROLE_ADMIN': '超级管理员',
    'ROLE_USER': '普通用户',
    'ROLE_GUEST': '访客'
  }

  return roleMap[roleName] || roleName
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '未知'

  const date = new Date(dateStr)
  if (isNaN(date.getTime())) return dateStr

  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.info-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  transition: all 0.3s;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.profile-content {
  padding: 20px 0;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.avatar-section :deep(.el-avatar) {
  background: var(--bg-primary);
  color: var(--text-primary);
  font-size: 48px;
  font-weight: 600;
  border: 3px solid var(--border-color);
}

.avatar-section h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
}

.role-tag {
  padding: 4px 16px;
  background: var(--bg-primary);
  color: var(--text-secondary);
  border-radius: 12px;
  font-size: 14px;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  padding: 20px 0;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item label {
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.info-item span {
  font-size: 15px;
  color: var(--text-primary);
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 20px;
}

/* 对话框样式 */
:deep(.el-dialog) {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid var(--border-color);
}

:deep(.el-dialog__title) {
  color: var(--text-primary);
}

:deep(.el-form-item__label) {
  color: var(--text-primary);
}

:deep(.el-input__inner) {
  background: var(--bg-primary);
  color: var(--text-primary);
  border-color: var(--border-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-container {
    padding: 10px;
  }

  .info-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .card-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
}
</style>
