<template>
  <div class="scenic-list-page">
    <div class="container page-content">
      <!-- 筛选区域 -->
      <div class="filter-section card">
        <div class="filter-row">
          <span class="filter-label">分类：</span>
          <div class="filter-options">
            <span
              :class="['filter-item', { active: !filters.categoryId }]"
              @click="selectCategory(null)"
            >全部</span>
            <span
              v-for="cat in categories"
              :key="cat.id"
              :class="['filter-item', { active: filters.categoryId === cat.id }]"
              @click="selectCategory(cat.id)"
            >{{ cat.name }}</span>
          </div>
        </div>
        <div class="filter-row">
          <span class="filter-label">排序：</span>
          <div class="filter-options">
            <span
              :class="['filter-item', { active: filters.sortBy === 'default' }]"
              @click="selectSort('default')"
            >默认</span>
            <span
              :class="['filter-item', { active: filters.sortBy === 'price' }]"
              @click="selectSort('price')"
            >价格</span>
            <span
              :class="['filter-item', { active: filters.sortBy === 'rating' }]"
              @click="selectSort('rating')"
            >评分</span>
          </div>
        </div>
      </div>

      <!-- 景点列表 -->
      <div class="scenic-list" v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="6" v-for="scenic in scenicList" :key="scenic.id">
            <div class="card scenic-card" @click="goToDetail(scenic.id)">
              <img
                :src="scenic.coverImage"
                :alt="scenic.scenicName"
                class="scenic-image"
                @error="handleImageError"
              />
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

        <!-- 空状态 -->
        <div v-if="!loading && scenicList.length === 0" class="empty-state">
          <el-icon><Picture /></el-icon>
          <p>暂无景点数据</p>
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
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getScenicList, getScenicCategories } from '@/api/scenic'
import placeholderImg from '@/assets/placeholder-scenic.svg'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const categories = ref([])
const scenicList = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const filters = reactive({
  categoryId: null,
  sortBy: 'default'
})

// 监听路由参数
watch(() => route.query.categoryId, (val) => {
  if (val) {
    filters.categoryId = parseInt(val)
    loadScenicList()
  }
}, { immediate: true })

onMounted(async () => {
  await loadCategories()
  await loadScenicList()
})

const loadCategories = async () => {
  try {
    const res = await getScenicCategories()
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadScenicList = async () => {
  loading.value = true
  try {
    const res = await getScenicList({
      current: currentPage.value,
      size: pageSize.value,
      categoryId: filters.categoryId,
      sortBy: filters.sortBy
    })
    scenicList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载景点列表失败', error)
  } finally {
    loading.value = false
  }
}

const selectCategory = (id) => {
  filters.categoryId = id
  currentPage.value = 1
  loadScenicList()
}

const selectSort = (sort) => {
  filters.sortBy = sort
  currentPage.value = 1
  loadScenicList()
}

const handlePageChange = () => {
  loadScenicList()
}

const goToDetail = (id) => {
  router.push(`/scenic/${id}`)
}

const handleImageError = (e) => {
  e.target.src = placeholderImg
}
</script>

<style lang="scss" scoped>
.scenic-list-page {
  .filter-section {
    padding: 20px;
    margin-bottom: 20px;

    .filter-row {
      display: flex;
      align-items: center;
      margin-bottom: 12px;

      &:last-child {
        margin-bottom: 0;
      }

      .filter-label {
        width: 60px;
        font-size: 14px;
        color: #666;
      }

      .filter-options {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;

        .filter-item {
          padding: 6px 16px;
          font-size: 14px;
          color: #666;
          cursor: pointer;
          border-radius: 4px;
          transition: all 0.3s;

          &:hover {
            color: #409eff;
          }

          &.active {
            background: #409eff;
            color: #fff;
          }
        }
      }
    }
  }

  .scenic-list {
    min-height: 400px;

    .el-col {
      margin-bottom: 20px;
    }
  }
}
</style>
