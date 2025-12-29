<template>
  <div class="order-success-page">
    <div class="container page-content">
      <div class="success-card card">
        <div class="success-icon">
          <el-icon :size="80" color="#67c23a"><SuccessFilled /></el-icon>
        </div>
        <h2 class="success-title">预订成功！</h2>
        <p class="success-desc">您的订单已提交成功，请按时前往景区游玩</p>

        <div class="order-info" v-if="order">
          <div class="info-item">
            <span class="label">订单编号</span>
            <span class="value">{{ order.orderNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">景点名称</span>
            <span class="value">{{ order.scenicName }}</span>
          </div>
          <div class="info-item">
            <span class="label">门票类型</span>
            <span class="value">{{ order.ticketName }}</span>
          </div>
          <div class="info-item">
            <span class="label">游玩日期</span>
            <span class="value">{{ order.visitDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">购买数量</span>
            <span class="value">{{ order.quantity }} 张</span>
          </div>
          <div class="info-item">
            <span class="label">订单金额</span>
            <span class="value price">¥{{ order.totalAmount }}</span>
          </div>
        </div>

        <div class="success-actions">
          <el-button type="primary" @click="goToOrders">查看订单</el-button>
          <el-button @click="goHome">返回首页</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderDetail } from '@/api/order'
import { SuccessFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const order = ref(null)

onMounted(async () => {
  const orderNo = route.query.orderNo
  if (orderNo) {
    try {
      const res = await getOrderDetail(orderNo)
      order.value = res.data
    } catch (error) {
      console.error('获取订单详情失败', error)
    }
  }
})

const goToOrders = () => {
  router.push('/user/orders')
}

const goHome = () => {
  router.push('/')
}
</script>

<style lang="scss" scoped>
.order-success-page {
  .success-card {
    max-width: 600px;
    margin: 0 auto;
    padding: 48px;
    text-align: center;

    .success-icon {
      margin-bottom: 24px;
    }

    .success-title {
      font-size: 28px;
      font-weight: 600;
      color: #333;
      margin-bottom: 12px;
    }

    .success-desc {
      font-size: 16px;
      color: #666;
      margin-bottom: 32px;
    }

    .order-info {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 24px;
      margin-bottom: 32px;
      text-align: left;

      .info-item {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px dashed #e4e7ed;

        &:last-child {
          border-bottom: none;
        }

        .label {
          color: #666;
        }

        .value {
          font-weight: 500;

          &.price {
            color: #f56c6c;
            font-size: 18px;
          }
        }
      }
    }

    .success-actions {
      display: flex;
      justify-content: center;
      gap: 16px;

      .el-button {
        width: 140px;
      }
    }
  }
}
</style>
