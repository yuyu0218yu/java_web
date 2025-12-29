<template>
  <div class="home-page">
    <!-- Banner轮播 -->
    <section class="banner-section">
      <el-carousel height="400px" :interval="5000">
        <el-carousel-item v-for="banner in homeData.banners" :key="banner.id">
          <a :href="banner.linkUrl" target="_blank">
            <img :src="banner.imageUrl" :alt="banner.title" @error="handleImageError" />
          </a>
        </el-carousel-item>
      </el-carousel>
    </section>

    <div class="container page-content">
      <!-- 热门景点 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">热门景点</h2>
          <router-link to="/scenic" class="more-link">
            查看更多 <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="scenic in homeData.hotScenics" :key="scenic.id">
            <div class="card scenic-card" @click="goToScenic(scenic.id)">
              <img :src="scenic.coverImage" :alt="scenic.scenicName" class="scenic-image" @error="handleImageError" />
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
      </section>

      <!-- 推荐攻略 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">精选攻略</h2>
          <router-link to="/guide" class="more-link">
            查看更多 <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
        <el-row :gutter="20">
          <el-col :span="12" v-for="guide in homeData.recommendGuides" :key="guide.id">
            <div class="card guide-card" @click="goToGuide(guide.id)">
              <img :src="guide.coverImage" :alt="guide.title" class="guide-cover" @error="handleImageError" />
              <div class="guide-content">
                <h3 class="guide-title">{{ guide.title }}</h3>
                <p class="guide-summary">{{ extractText(guide.content) }}</p>
                <div class="guide-meta">
                  <div class="author">
                    <el-avatar :size="24" :src="guide.authorAvatar">
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
          </el-col>
        </el-row>
      </section>

      <!-- 景点分类 -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">景点分类</h2>
        </div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="category in homeData.categories" :key="category.id">
            <div class="category-card" @click="goToCategory(category.id)">
              <el-icon :size="32"><component :is="getCategoryIcon(category.icon)" /></el-icon>
              <span class="category-name">{{ category.name }}</span>
              <span class="category-count">{{ category.scenicCount || 0 }}个景点</span>
            </div>
          </el-col>
        </el-row>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeData } from '@/api/home'
import { Location, Tickets, Sunny, Camera } from '@element-plus/icons-vue'
import placeholderImg from '@/assets/placeholder-scenic.svg'

const router = useRouter()

const homeData = ref({
  banners: [],
  hotScenics: [],
  recommendGuides: [],
  categories: []
})

const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getHomeData()
    homeData.value = res.data
  } catch (error) {
    console.error('获取首页数据失败', error)
  } finally {
    loading.value = false
  }
})

const goToScenic = (id) => {
  router.push(`/scenic/${id}`)
}

const goToGuide = (id) => {
  router.push(`/guide/${id}`)
}

const goToCategory = (id) => {
  router.push({ path: '/scenic', query: { categoryId: id } })
}

const extractText = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 100) + '...'
}

const getCategoryIcon = (icon) => {
  const icons = { Location, Tickets, Sunny, Camera }
  return icons[icon] || Location
}

const handleImageError = (e) => {
  e.target.src = placeholderImg
}
</script>

<style lang="scss" scoped>
.home-page {
  .banner-section {
    :deep(.el-carousel__item) {
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }

  .section {
    margin-bottom: 40px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .section-title {
        font-size: 22px;
        font-weight: 600;
        color: #333;
        position: relative;
        padding-left: 16px;

        &::before {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 4px;
          height: 20px;
          background: #409eff;
          border-radius: 2px;
        }
      }

      .more-link {
        display: flex;
        align-items: center;
        gap: 4px;
        color: #409eff;
        font-size: 14px;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .category-card {
    background: #fff;
    border-radius: 8px;
    padding: 24px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);

      .el-icon {
        color: #409eff;
      }
    }

    .el-icon {
      color: #666;
      transition: color 0.3s;
    }

    .category-name {
      font-size: 16px;
      font-weight: 500;
      color: #333;
    }

    .category-count {
      font-size: 12px;
      color: #999;
    }
  }
}
</style>
