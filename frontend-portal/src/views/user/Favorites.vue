<template>
  <div class="user-favorites">
    <h2 class="page-title">我的收藏</h2>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="收藏的景点" name="scenic" />
      <el-tab-pane label="收藏的攻略" name="guide" />
    </el-tabs>

    <div class="favorite-list" v-loading="loading">
      <!-- 景点收藏 -->
      <template v-if="activeTab === 'scenic'">
        <el-row :gutter="16">
          <el-col :span="8" v-for="item in favorites" :key="item.id">
            <div class="favorite-card card" @click="goToScenic(item.targetId)">
              <div class="card-actions">
                <el-button
                  type="danger"
                  :icon="Delete"
                  circle
                  size="small"
                  @click.stop="handleRemove(item.id)"
                />
              </div>
              <img :src="item.targetImage" class="card-image" />
              <div class="card-info">
                <h4>{{ item.targetName }}</h4>
                <p>收藏于 {{ item.createTime }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </template>

      <!-- 攻略收藏 -->
      <template v-if="activeTab === 'guide'">
        <div v-for="item in favorites" :key="item.id" class="guide-favorite-item">
          <div class="item-content" @click="goToGuide(item.targetId)">
            <img :src="item.targetImage" class="item-cover" />
            <div class="item-info">
              <h4>{{ item.targetName }}</h4>
              <p>收藏于 {{ item.createTime }}</p>
            </div>
          </div>
          <el-button type="danger" text @click="handleRemove(item.id)">取消收藏</el-button>
        </div>
      </template>

      <div v-if="!loading && favorites.length === 0" class="empty-state">
        <el-icon :size="48"><Star /></el-icon>
        <p>暂无收藏</p>
      </div>
    </div>

    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadFavorites"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavoriteScenics, getFavoriteGuides, removeFavorite } from '@/api/favorite'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const activeTab = ref('scenic')
const loading = ref(false)
const favorites = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

onMounted(() => {
  loadFavorites()
})

const loadFavorites = async () => {
  loading.value = true
  try {
    const params = { current: currentPage.value, size: pageSize.value }
    const res = activeTab.value === 'scenic'
      ? await getFavoriteScenics(params)
      : await getFavoriteGuides(params)
    favorites.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载收藏失败', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  loadFavorites()
}

const handleRemove = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏吗？', '提示', { type: 'warning' })
    await removeFavorite(id)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消收藏失败', error)
    }
  }
}

const goToScenic = (id) => {
  router.push(`/scenic/${id}`)
}

const goToGuide = (id) => {
  router.push(`/guide/${id}`)
}
</script>

<style lang="scss" scoped>
.user-favorites {
  .page-title {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .favorite-list {
    min-height: 300px;
  }

  .favorite-card {
    position: relative;
    cursor: pointer;
    margin-bottom: 16px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-4px);

      .card-actions {
        opacity: 1;
      }
    }

    .card-actions {
      position: absolute;
      top: 8px;
      right: 8px;
      opacity: 0;
      transition: opacity 0.3s;
      z-index: 1;
    }

    .card-image {
      width: 100%;
      height: 150px;
      object-fit: cover;
    }

    .card-info {
      padding: 12px;

      h4 {
        font-size: 14px;
        margin-bottom: 4px;
      }

      p {
        font-size: 12px;
        color: #999;
      }
    }
  }

  .guide-favorite-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    border-bottom: 1px solid #eee;

    .item-content {
      display: flex;
      align-items: center;
      gap: 16px;
      cursor: pointer;

      &:hover h4 {
        color: #409eff;
      }

      .item-cover {
        width: 120px;
        height: 80px;
        object-fit: cover;
        border-radius: 4px;
      }

      .item-info {
        h4 {
          font-size: 15px;
          margin-bottom: 8px;
          transition: color 0.3s;
        }

        p {
          font-size: 12px;
          color: #999;
        }
      }
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
