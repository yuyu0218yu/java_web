<template>
  <div class="guide-publish-page">
    <div class="container page-content">
      <div class="publish-card card">
        <h2 class="page-title">发布攻略</h2>

        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入攻略标题" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="类型" prop="guideType">
            <el-radio-group v-model="form.guideType">
              <el-radio :value="1">攻略</el-radio>
              <el-radio :value="2">游记</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="封面图" prop="coverImage">
            <el-input v-model="form.coverImage" placeholder="请输入封面图URL" />
          </el-form-item>

          <el-form-item label="标签" prop="tags">
            <el-input v-model="form.tags" placeholder="多个标签用逗号分隔，如：张家界,天门山,玻璃栈道" />
          </el-form-item>

          <el-form-item label="内容" prop="content">
            <div class="editor-container">
              <Toolbar
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                mode="default"
                style="border-bottom: 1px solid #ccc"
              />
              <Editor
                v-model="form.content"
                :defaultConfig="editorConfig"
                mode="default"
                style="height: 400px; overflow-y: hidden;"
                @onCreated="handleCreated"
              />
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">
              发布攻略
            </el-button>
            <el-button @click="handleSaveDraft">保存草稿</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, shallowRef, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { publishGuide } from '@/api/guide'
import { ElMessage } from 'element-plus'

const router = useRouter()

const formRef = ref()
const editorRef = shallowRef()
const submitting = ref(false)

const form = reactive({
  title: '',
  guideType: 1,
  coverImage: '',
  tags: '',
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  guideType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/upload/image',
      fieldName: 'file',
      maxFileSize: 5 * 1024 * 1024,
      allowedFileTypes: ['image/*'],
      customInsert(res, insertFn) {
        if (res.code === 200) {
          insertFn(res.data.url, res.data.name, res.data.url)
        }
      }
    }
  }
}

const handleCreated = (editor) => {
  editorRef.value = editor
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})

const handleSubmit = async () => {
  await formRef.value.validate()

  if (!form.content || form.content === '<p><br></p>') {
    ElMessage.warning('请输入内容')
    return
  }

  submitting.value = true
  try {
    await publishGuide(form)
    ElMessage.success('发布成功，等待审核')
    router.push('/user/guides')
  } catch (error) {
    console.error('发布失败', error)
  } finally {
    submitting.value = false
  }
}

const handleSaveDraft = () => {
  localStorage.setItem('guide_draft', JSON.stringify(form))
  ElMessage.success('已保存到草稿')
}
</script>

<style lang="scss" scoped>
.guide-publish-page {
  .publish-card {
    padding: 32px;

    .page-title {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 32px;
      text-align: center;
    }

    .editor-container {
      border: 1px solid #ccc;
      border-radius: 4px;
      overflow: hidden;
    }
  }
}
</style>
