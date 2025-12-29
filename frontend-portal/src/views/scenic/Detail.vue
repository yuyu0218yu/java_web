<template>
  <div class="scenic-detail-page" v-loading="loading">
    <div class="container page-content" v-if="scenic">
      <!-- 面包屑 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/scenic' }">景点门票</el-breadcrumb-item>
        <el-breadcrumb-item>{{ scenic.scenicName }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 景点信息 -->
      <div class="scenic-header card">
        <div class="scenic-gallery">
          <el-carousel height="400px" :autoplay="false">
            <el-carousel-item v-for="(img, idx) in images" :key="idx">
              <img :src="img" :alt="scenic.scenicName" class="gallery-image" @error="handleImageError" />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="scenic-info">
          <h1 class="scenic-name">{{ scenic.scenicName }}</h1>
          <div class="scenic-rating">
            <el-rate v-model="scenic.rating" disabled />
            <span class="score">{{ scenic.rating }}分</span>
          </div>
          <div class="scenic-meta">
            <p><el-icon><Location /></el-icon> {{ scenic.address }}</p>
            <p><el-icon><Clock /></el-icon> {{ scenic.openTime }}</p>
          </div>
          <div class="scenic-tags">
            <el-tag v-for="tag in scenic.tags?.split(',')" :key="tag" size="small">{{ tag }}</el-tag>
          </div>
          <div class="scenic-actions">
            <el-button
              :type="isFavorited ? 'warning' : 'default'"
              @click="toggleFavorite"
            >
              <el-icon><Star /></el-icon>
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
            <el-button><el-icon><Share /></el-icon> 分享</el-button>
          </div>
        </div>
      </div>

      <!-- 门票选择 -->
      <div class="ticket-section card">
        <h2 class="section-title">门票预订</h2>
        <div class="ticket-list">
          <div
            v-for="ticket in tickets"
            :key="ticket.id"
            :class="['ticket-item', { selected: selectedTicket?.id === ticket.id }]"
            @click="selectTicket(ticket)"
          >
            <div class="ticket-info">
              <h3 class="ticket-name">{{ ticket.ticketName }}</h3>
              <p class="ticket-desc">{{ ticket.useRules }}</p>
              <p class="ticket-notice">{{ ticket.notice }}</p>
            </div>
            <div class="ticket-price">
              <span class="price">¥{{ ticket.sellingPrice }}</span>
              <span class="original" v-if="ticket.originalPrice > ticket.sellingPrice">
                ¥{{ ticket.originalPrice }}
              </span>
            </div>
          </div>
        </div>

        <!-- 购买数量 -->
        <div class="buy-section" v-if="selectedTicket">
          <div class="buy-row">
            <span class="label">游玩日期</span>
            <el-date-picker
              v-model="visitDate"
              type="date"
              placeholder="选择日期"
              :disabled-date="disabledDate"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </div>
          <div class="buy-row">
            <span class="label">购买数量</span>
            <el-input-number v-model="quantity" :min="1" :max="10" />
          </div>
          <div class="buy-total">
            <span>合计：</span>
            <span class="total-price">¥{{ (selectedTicket.sellingPrice * quantity).toFixed(2) }}</span>
          </div>
          <el-button type="primary" size="large" @click="handleBuy">立即预订</el-button>
        </div>
      </div>

      <!-- 景点介绍 -->
      <div class="intro-section card">
        <h2 class="section-title">景点介绍</h2>
        <div class="intro-content" v-html="scenic.description"></div>
      </div>

      <!-- 游客评价 -->
      <div class="comment-section card">
        <h2 class="section-title">游客评价</h2>
        <!-- 评论列表组件 -->
        <div class="comment-list">
          <p class="empty-tip" v-if="!comments.length">暂无评价</p>
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <el-avatar :size="40" :src="comment.userAvatar">{{ comment.userName?.charAt(0) }}</el-avatar>
              <div class="comment-user">
                <span class="username">{{ comment.userName }}</span>
                <el-rate v-model="comment.rating" disabled size="small" />
              </div>
              <span class="comment-time">{{ comment.createTime }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getScenicDetail, getScenicTickets } from '@/api/scenic'
import { toggleFavorite as toggleFav, checkFavorite } from '@/api/favorite'
import { getComments } from '@/api/comment'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import placeholderScenic from '@/assets/placeholder-scenic.svg'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const scenic = ref(null)
const tickets = ref([])
const comments = ref([])
const selectedTicket = ref(null)
const quantity = ref(1)
const visitDate = ref('')
const isFavorited = ref(false)

const images = computed(() => {
  if (!scenic.value) return []
  const imgs = scenic.value.images?.split(',') || []
  return imgs.length ? imgs : [scenic.value.coverImage]
})

onMounted(async () => {
  const id = route.params.id
  await Promise.all([
    loadScenicDetail(id),
    loadTickets(id),
    loadComments(id)
  ])
  if (authStore.isAuthenticated) {
    await checkFavoriteStatus(id)
  }
  loading.value = false
})

const loadScenicDetail = async (id) => {
  const res = await getScenicDetail(id)
  scenic.value = res.data
}

const loadTickets = async (id) => {
  const res = await getScenicTickets(id)
  tickets.value = res.data || []
  if (tickets.value.length) {
    selectedTicket.value = tickets.value[0]
  }
}

const loadComments = async (id) => {
  const res = await getComments({ targetType: 1, targetId: id, current: 1, size: 10 })
  comments.value = res.data?.records || []
}

const checkFavoriteStatus = async (id) => {
  try {
    const res = await checkFavorite(1, id)
    isFavorited.value = res.data
  } catch (e) {}
}

const selectTicket = (ticket) => {
  selectedTicket.value = ticket
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const toggleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  const res = await toggleFav(1, scenic.value.id)
  isFavorited.value = res.data.isFavorited
  ElMessage.success(res.data.message)
}

const handleBuy = () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  if (!visitDate.value) {
    ElMessage.warning('请选择游玩日期')
    return
  }
  router.push({
    path: '/order/create',
    query: {
      ticketId: selectedTicket.value.id,
      quantity: quantity.value,
      visitDate: visitDate.value
    }
  })
}

const handleImageError = (e) => {
  e.target.src = placeholderScenic
}
</script>

<style lang="scss" scoped>
.scenic-detail-page {
  .el-breadcrumb {
    margin-bottom: 20px;
  }

  .scenic-header {
    display: flex;
    gap: 24px;
    padding: 20px;
    margin-bottom: 20px;

    .scenic-gallery {
      width: 500px;
      flex-shrink: 0;

      .gallery-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .scenic-info {
      flex: 1;

      .scenic-name {
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 12px;
      }

      .scenic-rating {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 16px;

        .score {
          font-size: 16px;
          color: #ff9800;
        }
      }

      .scenic-meta {
        margin-bottom: 16px;

        p {
          display: flex;
          align-items: center;
          gap: 8px;
          color: #666;
          margin-bottom: 8px;
        }
      }

      .scenic-tags {
        display: flex;
        gap: 8px;
        margin-bottom: 20px;
      }
    }
  }

  .section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid #eee;
  }

  .ticket-section {
    padding: 20px;
    margin-bottom: 20px;

    .ticket-list {
      margin-bottom: 20px;
    }

    .ticket-item {
      display: flex;
      justify-content: space-between;
      padding: 16px;
      border: 1px solid #eee;
      border-radius: 8px;
      margin-bottom: 12px;
      cursor: pointer;
      transition: all 0.3s;

      &:hover, &.selected {
        border-color: #409eff;
        background: #f0f7ff;
      }

      .ticket-name {
        font-size: 16px;
        font-weight: 500;
        margin-bottom: 8px;
      }

      .ticket-desc, .ticket-notice {
        font-size: 13px;
        color: #999;
      }

      .ticket-price {
        text-align: right;

        .price {
          font-size: 24px;
          font-weight: bold;
          color: #f56c6c;
        }

        .original {
          font-size: 14px;
          color: #999;
          text-decoration: line-through;
          margin-left: 8px;
        }
      }
    }

    .buy-section {
      padding: 20px;
      background: #fafafa;
      border-radius: 8px;

      .buy-row {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .label {
          width: 80px;
          color: #666;
        }
      }

      .buy-total {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        margin-bottom: 16px;

        .total-price {
          font-size: 28px;
          font-weight: bold;
          color: #f56c6c;
          margin-left: 8px;
        }
      }

      .el-button {
        width: 100%;
      }
    }
  }

  .intro-section, .comment-section {
    padding: 20px;
    margin-bottom: 20px;
  }

  .comment-item {
    padding: 16px 0;
    border-bottom: 1px solid #eee;

    .comment-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 12px;

      .comment-user {
        flex: 1;

        .username {
          display: block;
          font-weight: 500;
          margin-bottom: 4px;
        }
      }

      .comment-time {
        font-size: 12px;
        color: #999;
      }
    }

    .comment-content {
      color: #666;
      line-height: 1.6;
    }
  }

  .empty-tip {
    text-align: center;
    color: #999;
    padding: 40px 0;
  }
}
</style>
