<template>
  <div class="user-guides">
    <div class="page-header">
      <h2 class="page-title">我的攻略</h2>
      <el-button type="primary" @click="goPublish">
        <el-icon><Edit /></el-icon> 发布攻略
      </el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待审核" name="pending" />
      <el-tab-pane label="已发布" name="published" />
      <el-tab-pane label="未通过" name="rejected" />
    </el-tabs>

    <div class="guide-list" v-loading="loading">
      <div v-for="guide in guides" :key="guide.id" class="guide-item">
        <img :src="guide.coverImage" class="guide-cover" @click="goToDetail(guide.id)" />
        <div class="guide-info">
          <h4 @click="goToDetail(guide.id)">{{ guide.title }}</h4>
          <div class="guide-meta">
            <el-tag :type="getStatusType(guide.status)" size="small">
              {{ getStatusText(guide.status) }}
            </el-tag>
            <span class="stats">
              <el-icon><View /></el-icon> {{ guide.viewCount }}
              <el-icon><Star /></el-icon> {{ guide.likeCount }}
            </span>
            <span class="time">{{ guide.createTime }}</span>
          </div>
        </div>
        <div class="guide-actions">
          <el-button v-if="guide.status !== 1" text @click="handleEdit(guide.id)">编辑</el-button>
          <el-button type="danger" text @click="handleDelete(guide.id)">删除</el-button>
        </div>
      </div>

      <div v-if="!loading && guides.length === 0" class="empty-state">
        <el-icon :size="48"><Document /></el-icon>
        <p>暂无攻略，快去发布你的第一篇攻略吧！</p>
      </div>
    </div>

    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadGuides"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyGuides, deleteGuide } from '@/api/guide'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const activeTab = ref('all')
const loading = ref(false)
const guides = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadGuides()
})

const loadGuides = async () => {
  loading.value = true
  try {
    const params = { current: currentPage.value, size: pageSize.value }
    if (activeTab.value !== 'all') {
      const statusMap = { pending: 0, published: 1, rejected: 2 }
      params.status = statusMap[activeTab.value]
    }
    const res = await getMyGuides(params)
    guides.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载攻略失败', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  loadGuides()
}

const goPublish = () => {
  router.push('/guide/publish')
}

const goToDetail = (id) => {
  router.push(`/guide/${id}`)
}

const handleEdit = (id) => {
  router.push({ path: '/guide/publish', query: { id } })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该攻略吗？', '提示', { type: 'warning' })
    await deleteGuide(id)
    ElMessage.success('删除成功')
    loadGuides()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待审核', 1: '已发布', 2: '未通过' }
  return texts[status] || '未知'
}
</script>

<style lang="scss" scoped>
.user-guides {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .page-title {
      font-size: 20px;
      font-weight: 600;
    }
  }

  .guide-list {
    min-height: 300px;
  }

  .guide-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #eee;

    .guide-cover {
      width: 160px;
      height: 100px;
      object-fit: cover;
      border-radius: 4px;
      cursor: pointer;
    }

    .guide-info {
      flex: 1;
      margin-left: 16px;

      h4 {
        font-size: 16px;
        margin-bottom: 12px;
        cursor: pointer;

        &:hover {
          color: #409eff;
        }
      }

      .guide-meta {
        display: flex;
        align-items: center;
        gap: 16px;
        font-size: 13px;
        color: #999;

        .stats {
          display: flex;
          align-items: center;
          gap: 8px;

          .el-icon {
            margin-right: 2px;
          }
        }
      }
    }

    .guide-actions {
      display: flex;
      gap: 8px;
    }
  }

  .empty-state {
    text-align: center;
    padding: 60px 0;
    color: #999;

    .el-icon {
      color: #ddd;
      margin-bottom: 16px;
    }
  }
}
</style>
