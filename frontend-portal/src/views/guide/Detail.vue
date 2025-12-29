<template>
  <div class="guide-detail-page" v-loading="loading">
    <div class="container page-content" v-if="guide">
      <!-- 文章头部 -->
      <div class="article-header">
        <h1 class="article-title">{{ guide.title }}</h1>
        <div class="article-meta">
          <div class="author-info">
            <el-avatar :size="48" :src="guide.authorAvatar">
              {{ guide.authorName?.charAt(0) }}
            </el-avatar>
            <div class="author-detail">
              <span class="author-name">{{ guide.authorName }}</span>
              <span class="publish-time">{{ guide.createTime }}</span>
            </div>
          </div>
          <div class="article-stats">
            <span><el-icon><View /></el-icon> {{ guide.viewCount }}</span>
            <span><el-icon><Star /></el-icon> {{ guide.likeCount }}</span>
          </div>
        </div>
      </div>

      <!-- 封面图 -->
      <div class="article-cover" v-if="guide.coverImage">
        <img :src="guide.coverImage" :alt="guide.title" @error="handleImageError" />
      </div>

      <!-- 文章内容 -->
      <div class="article-content card">
        <div class="content-body" v-html="guide.content"></div>

        <!-- 标签 -->
        <div class="article-tags" v-if="guide.tags">
          <el-tag
            v-for="tag in guide.tags.split(',')"
            :key="tag"
            size="small"
          >{{ tag }}</el-tag>
        </div>

        <!-- 操作按钮 -->
        <div class="article-actions">
          <el-button
            :type="isLiked ? 'warning' : 'default'"
            size="large"
            @click="handleLike"
          >
            <el-icon><Star /></el-icon> {{ isLiked ? '已点赞' : '点赞' }} {{ guide.likeCount }}
          </el-button>
          <el-button
            :type="isFavorited ? 'warning' : 'default'"
            size="large"
            @click="handleFavorite"
          >
            <el-icon><Collection /></el-icon> {{ isFavorited ? '已收藏' : '收藏' }}
          </el-button>
          <el-button size="large">
            <el-icon><Share /></el-icon> 分享
          </el-button>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="comment-section card">
        <h2 class="section-title">评论 ({{ comments.length }})</h2>

        <!-- 发表评论 -->
        <div class="comment-form" v-if="authStore.isAuthenticated">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
          />
          <el-button type="primary" @click="submitComment" :loading="submitting">
            发表评论
          </el-button>
        </div>
        <div class="login-tip" v-else>
          <router-link to="/login">登录</router-link> 后参与评论
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <el-avatar :size="40" :src="comment.userAvatar">
              {{ comment.userName?.charAt(0) }}
            </el-avatar>
            <div class="comment-body">
              <div class="comment-header">
                <span class="username">{{ comment.userName }}</span>
                <span class="time">{{ comment.createTime }}</span>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-actions">
                <span @click="likeComment(comment.id)">
                  <el-icon><Star /></el-icon> {{ comment.likeCount }}
                </span>
                <span @click="replyTo(comment)">
                  <el-icon><ChatDotRound /></el-icon> 回复
                </span>
              </div>
            </div>
          </div>

          <p class="empty-tip" v-if="!comments.length">暂无评论，快来发表第一条评论吧~</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getGuideDetail, likeGuide } from '@/api/guide'
import { toggleFavorite, checkFavorite } from '@/api/favorite'
import { getComments, addComment, likeComment as likeCommentApi } from '@/api/comment'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import placeholderImg from '@/assets/placeholder-scenic.svg'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const guide = ref(null)
const comments = ref([])
const commentContent = ref('')
const submitting = ref(false)
const isLiked = ref(false)
const isFavorited = ref(false)

onMounted(async () => {
  const id = route.params.id
  await loadGuideDetail(id)
  await loadComments(id)
  if (authStore.isAuthenticated) {
    await checkFavoriteStatus(id)
  }
  loading.value = false
})

const loadGuideDetail = async (id) => {
  const res = await getGuideDetail(id)
  guide.value = res.data
}

const loadComments = async (id) => {
  const res = await getComments({ targetType: 2, targetId: id, current: 1, size: 50 })
  comments.value = res.data?.records || []
}

const checkFavoriteStatus = async (id) => {
  try {
    const res = await checkFavorite(2, id)
    isFavorited.value = res.data
  } catch (e) {}
}

const handleLike = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  await likeGuide(guide.value.id)
  guide.value.likeCount++
  isLiked.value = true
  ElMessage.success('点赞成功')
}

const handleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  const res = await toggleFavorite(2, guide.value.id)
  isFavorited.value = res.data.isFavorited
  ElMessage.success(res.data.message)
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  submitting.value = true
  try {
    await addComment({
      targetType: 2,
      targetId: guide.value.id,
      content: commentContent.value
    })
    ElMessage.success('评论成功')
    commentContent.value = ''
    await loadComments(guide.value.id)
  } catch (error) {
    console.error('评论失败', error)
  } finally {
    submitting.value = false
  }
}

const likeComment = async (id) => {
  await likeCommentApi(id)
  const comment = comments.value.find(c => c.id === id)
  if (comment) comment.likeCount++
}

const replyTo = (comment) => {
  commentContent.value = `@${comment.userName} `
}

const handleImageError = (e) => {
  e.target.src = placeholderImg
}
</script>

<style lang="scss" scoped>
.guide-detail-page {
  .article-header {
    text-align: center;
    margin-bottom: 24px;

    .article-title {
      font-size: 32px;
      font-weight: 600;
      color: #333;
      margin-bottom: 20px;
    }

    .article-meta {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 40px;

      .author-info {
        display: flex;
        align-items: center;
        gap: 12px;

        .author-detail {
          text-align: left;

          .author-name {
            display: block;
            font-weight: 500;
          }

          .publish-time {
            font-size: 12px;
            color: #999;
          }
        }
      }

      .article-stats {
        display: flex;
        gap: 16px;
        color: #666;

        span {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }

  .article-cover {
    margin-bottom: 24px;
    border-radius: 8px;
    overflow: hidden;

    img {
      width: 100%;
      max-height: 500px;
      object-fit: cover;
    }
  }

  .article-content {
    padding: 32px;
    margin-bottom: 24px;

    .content-body {
      line-height: 1.8;
      font-size: 16px;
      color: #333;

      :deep(img) {
        max-width: 100%;
        border-radius: 8px;
        margin: 16px 0;
      }

      :deep(p) {
        margin-bottom: 16px;
      }
    }

    .article-tags {
      display: flex;
      gap: 8px;
      margin: 24px 0;
      padding-top: 24px;
      border-top: 1px solid #eee;
    }

    .article-actions {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-top: 32px;
    }
  }

  .comment-section {
    padding: 24px;

    .section-title {
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 20px;
    }

    .comment-form {
      margin-bottom: 24px;

      .el-button {
        margin-top: 12px;
      }
    }

    .login-tip {
      text-align: center;
      padding: 20px;
      background: #f5f7fa;
      border-radius: 8px;
      margin-bottom: 24px;

      a {
        color: #409eff;
      }
    }

    .comment-item {
      display: flex;
      gap: 12px;
      padding: 16px 0;
      border-bottom: 1px solid #eee;

      .comment-body {
        flex: 1;

        .comment-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 8px;

          .username {
            font-weight: 500;
          }

          .time {
            font-size: 12px;
            color: #999;
          }
        }

        .comment-content {
          color: #666;
          line-height: 1.6;
          margin-bottom: 8px;
        }

        .comment-actions {
          display: flex;
          gap: 16px;
          font-size: 13px;
          color: #999;

          span {
            display: flex;
            align-items: center;
            gap: 4px;
            cursor: pointer;

            &:hover {
              color: #409eff;
            }
          }
        }
      }
    }

    .empty-tip {
      text-align: center;
      color: #999;
      padding: 40px 0;
    }
  }
}
</style>
