<template>
  <div class="search-page">
    <div class="container page-content">
      <div class="search-header">
        <h2>搜索结果：{{ keyword }}</h2>
        <p v-if="!loading">共找到 {{ totalCount }} 条结果</p>
      </div>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane :label="`景点 (${scenicTotal})`" name="scenic" />
        <el-tab-pane :label="`攻略 (${guideTotal})`" name="guide" />
      </el-tabs>

      <div class="search-results" v-loading="loading">
        <!-- 景点结果 -->
        <template v-if="activeTab === 'scenic'">
          <el-row :gutter="20">
            <el-col :span="6" v-for="scenic in scenicList" :key="scenic.id">
              <div class="card scenic-card" @click="goToScenic(scenic.id)">
                <img :src="scenic.coverImage" class="scenic-image" />
                <div class="scenic-info">
                  <h3 class="scenic-name">{{ scenic.scenicName }}</h3>
                  <p class="scenic-desc">{{ scenic.description }}</p>
                  <div class="scenic-footer">
                    <div class="scenic-rating">
                      <el-icon><Star /></el-icon>
                      <span>{{ scenic.rating || 5.0 }}</span>
                    </div>
                    <div class="scenic-price">
                      <span class="unit">¥</span>{{ scenic.minPrice || 0 }}<span class="unit">起</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </template>

        <!-- 攻略结果 -->
        <template v-if="activeTab === 'guide'">
          <div
            v-for="guide in guideList"
            :key="guide.id"
            class="card guide-card"
            @click="goToGuide(guide.id)"
          >
            <img :src="guide.coverImage" class="guide-cover" />
            <div class="guide-content">
              <h3 class="guide-title">{{ guide.title }}</h3>
              <p class="guide-summary">{{ extractText(guide.content) }}</p>
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
                </div>
              </div>
            </div>
          </div>
        </template>

        <div v-if="!loading && currentList.length === 0" class="empty-state">
          <el-icon :size="48"><Search /></el-icon>
          <p>未找到相关内容</p>
        </div>
      </div>

      <div class="pagination-wrapper" v-if="currentTotal > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="currentTotal"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getScenicList } from '@/api/scenic'
import { getGuideList } from '@/api/guide'

const route = useRoute()
const router = useRouter()

const keyword = ref('')
const activeTab = ref('scenic')
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)

const scenicList = ref([])
const scenicTotal = ref(0)
const guideList = ref([])
const guideTotal = ref(0)

const totalCount = computed(() => scenicTotal.value + guideTotal.value)
const currentList = computed(() => activeTab.value === 'scenic' ? scenicList.value : guideList.value)
const currentTotal = computed(() => activeTab.value === 'scenic' ? scenicTotal.value : guideTotal.value)

watch(() => route.query.keyword, (val) => {
  if (val) {
    keyword.value = val
    search()
  }
}, { immediate: true })

const search = async () => {
  loading.value = true
  currentPage.value = 1
  try {
    await Promise.all([searchScenic(), searchGuide()])
  } finally {
    loading.value = false
  }
}

const searchScenic = async () => {
  const res = await getScenicList({
    current: activeTab.value === 'scenic' ? currentPage.value : 1,
    size: pageSize.value,
    keyword: keyword.value
  })
  scenicList.value = res.data?.records || []
  scenicTotal.value = res.data?.total || 0
}

const searchGuide = async () => {
  const res = await getGuideList({
    current: activeTab.value === 'guide' ? currentPage.value : 1,
    size: pageSize.value,
    keyword: keyword.value
  })
  guideList.value = res.data?.records || []
  guideTotal.value = res.data?.total || 0
}

const handleTabChange = () => {
  currentPage.value = 1
  if (activeTab.value === 'scenic') {
    searchScenic()
  } else {
    searchGuide()
  }
}

const handlePageChange = () => {
  if (activeTab.value === 'scenic') {
    searchScenic()
  } else {
    searchGuide()
  }
}

const goToScenic = (id) => {
  router.push(`/scenic/${id}`)
}

const goToGuide = (id) => {
  router.push(`/guide/${id}`)
}

const extractText = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 150) + '...'
}
</script>

<style lang="scss" scoped>
.search-page {
  .search-header {
    margin-bottom: 20px;

    h2 {
      font-size: 22px;
      font-weight: 600;
      margin-bottom: 8px;
    }

    p {
      color: #666;
    }
  }

  .search-results {
    min-height: 400px;

    .el-col {
      margin-bottom: 20px;
    }
  }

  .empty-state {
    text-align: center;
    padding: 80px 0;
    color: #999;

    .el-icon {
      color: #ddd;
      margin-bottom: 16px;
    }
  }
}
</style>
