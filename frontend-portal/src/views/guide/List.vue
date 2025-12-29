<template>
  <div class="guide-list-page">
    <div class="container page-content">
      <!-- 顶部操作区 -->
      <div class="top-actions">
        <div class="filter-tabs">
          <span
            :class="['tab-item', { active: filters.guideType === null }]"
            @click="selectType(null)"
          >全部</span>
          <span
            :class="['tab-item', { active: filters.guideType === 1 }]"
            @click="selectType(1)"
          >攻略</span>
          <span
            :class="['tab-item', { active: filters.guideType === 2 }]"
            @click="selectType(2)"
          >游记</span>
        </div>
        <el-button type="primary" @click="goPublish">
          <el-icon><Edit /></el-icon> 发布攻略
        </el-button>
      </div>

      <!-- 攻略列表 -->
      <div class="guide-list" v-loading="loading">
        <div
          v-for="guide in guideList"
          :key="guide.id"
          class="card guide-card"
          @click="goToDetail(guide.id)"
        >
          <img :src="guide.coverImage" :alt="guide.title" class="guide-cover" @error="handleImageError" />
          <div class="guide-content">
            <h3 class="guide-title">{{ guide.title }}</h3>
            <p class="guide-summary">{{ extractText(guide.content) }}</p>
            <div class="guide-tags">
              <el-tag
                v-for="tag in guide.tags?.split(',')"
                :key="tag"
                size="small"
                type="info"
              >{{ tag }}</el-tag>
            </div>
            <div class="guide-meta">
              <div class="author">
                <el-avatar :size="28" :src="guide.authorAvatar">
                  {{ guide.authorName?.charAt(0) }}
                </el-avatar>
                <span>{{ guide.authorName }}</span>
              </div>
              <div class="stats">
                <span><el-icon><View /></el-icon> {{ guide.viewCount }}</span>
                <span><el-icon><Star /></el-icon> {{ guide.likeCount }}</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ guide.commentCount }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="!loading && guideList.length === 0" class="empty-state">
          <el-icon><Document /></el-icon>
          <p>暂无攻略数据</p>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getGuideList } from '@/api/guide'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import placeholderImg from '@/assets/placeholder-scenic.svg'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const guideList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filters = reactive({
  guideType: null,
  keyword: ''
})

onMounted(() => {
  loadGuideList()
})

const loadGuideList = async () => {
  loading.value = true
  try {
    const res = await getGuideList({
      current: currentPage.value,
      size: pageSize.value,
      guideType: filters.guideType,
      keyword: filters.keyword
    })
    guideList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载攻略列表失败', error)
  } finally {
    loading.value = false
  }
}

const selectType = (type) => {
  filters.guideType = type
  currentPage.value = 1
  loadGuideList()
}

const handlePageChange = () => {
  loadGuideList()
}

const goToDetail = (id) => {
  router.push(`/guide/${id}`)
}

const goPublish = () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  router.push('/guide/publish')
}

const extractText = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 150) + '...'
}

const handleImageError = (e) => {
  e.target.src = placeholderImg
}
</script>

<style lang="scss" scoped>
.guide-list-page {
  .top-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .filter-tabs {
      display: flex;
      gap: 24px;

      .tab-item {
        font-size: 16px;
        color: #666;
        cursor: pointer;
        padding-bottom: 8px;
        border-bottom: 2px solid transparent;
        transition: all 0.3s;

        &:hover, &.active {
          color: #409eff;
          border-bottom-color: #409eff;
        }
      }
    }
  }

  .guide-list {
    min-height: 400px;

    .guide-card {
      margin-bottom: 20px;

      .guide-tags {
        display: flex;
        gap: 8px;
        margin-bottom: 12px;
      }
    }
  }
}
</style>
