<template>
  <div class="user-profile">
    <h2 class="page-title">个人资料</h2>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" v-loading="loading">
      <el-form-item label="用户名">
        <el-input :value="form.username" disabled />
      </el-form-item>

      <el-form-item label="头像">
        <div class="avatar-uploader">
          <el-avatar :size="80" :src="form.avatar">{{ form.nickname?.charAt(0) }}</el-avatar>
          <el-input v-model="form.avatar" placeholder="请输入头像URL" style="margin-left: 16px; flex: 1;" />
        </div>
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入昵称" />
      </el-form-item>

      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio :value="0">保密</el-radio>
          <el-radio :value="1">男</el-radio>
          <el-radio :value="2">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saving">保存修改</el-button>
      </el-form-item>
    </el-form>

    <el-divider />

    <h3 class="section-title">修改密码</h3>
    <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
      <el-form-item label="原密码" prop="oldPassword">
        <el-input v-model="pwdForm.oldPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="pwdForm.newPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="warning" @click="handleChangePassword" :loading="changingPwd">
          修改密码
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getUserInfo, updateUserInfo, changePassword } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()

const formRef = ref()
const pwdFormRef = ref()
const loading = ref(true)
const saving = ref(false)
const changingPwd = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  avatar: '',
  gender: 0,
  email: '',
  phone: ''
})

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

onMounted(async () => {
  try {
    const res = await getUserInfo()
    Object.assign(form, res.data)
  } catch (error) {
    console.error('获取用户信息失败', error)
  } finally {
    loading.value = false
  }
})

const handleSave = async () => {
  await formRef.value.validate()
  saving.value = true
  try {
    await updateUserInfo(form)
    ElMessage.success('保存成功')
    authStore.fetchUserInfo()
  } catch (error) {
    console.error('保存失败', error)
  } finally {
    saving.value = false
  }
}

const handleChangePassword = async () => {
  await pwdFormRef.value.validate()
  changingPwd.value = true
  try {
    await changePassword({
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    authStore.logout()
  } catch (error) {
    console.error('修改密码失败', error)
  } finally {
    changingPwd.value = false
  }
}
</script>

<style lang="scss" scoped>
.user-profile {
  .page-title {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 24px;
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .avatar-uploader {
    display: flex;
    align-items: center;
  }

  .el-form {
    max-width: 500px;
  }
}
</style>
