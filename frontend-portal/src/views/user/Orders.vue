<template>
  <div class="user-orders">
    <h2 class="page-title">我的订单</h2>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部订单" name="all" />
      <el-tab-pane label="待使用" name="pending" />
      <el-tab-pane label="已完成" name="completed" />
      <el-tab-pane label="已取消" name="cancelled" />
    </el-tabs>

    <div class="order-list" v-loading="loading">
      <div v-for="order in orders" :key="order.id" class="order-item">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-time">{{ order.createTime }}</span>
          <el-tag :type="getStatusType(order.orderStatus)" size="small">
            {{ getStatusText(order.orderStatus) }}
          </el-tag>
        </div>
        <div class="order-body">
          <div class="order-info">
            <h4>{{ order.scenicName }} - {{ order.ticketName }}</h4>
            <p>游玩日期：{{ order.visitDate }}</p>
            <p>数量：{{ order.quantity }} 张</p>
          </div>
          <div class="order-price">
            <span class="amount">¥{{ order.totalAmount }}</span>
          </div>
        </div>
        <div class="order-footer">
          <el-button
            v-if="order.orderStatus === 0 || order.orderStatus === 1"
            type="danger"
            text
            @click="handleCancel(order.orderNo)"
          >取消订单</el-button>
          <el-button v-if="order.orderStatus === 2" type="primary" text>
            发表评价
          </el-button>
        </div>
      </div>

      <div v-if="!loading && orders.length === 0" class="empty-state">
        <el-icon :size="48"><Tickets /></el-icon>
        <p>暂无订单</p>
      </div>
    </div>

    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadOrders"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyOrders, cancelOrder } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('all')
const loading = ref(false)
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value
    }
    if (activeTab.value !== 'all') {
      const statusMap = { pending: 1, completed: 2, cancelled: 4 }
      params.orderStatus = statusMap[activeTab.value]
    }
    const res = await getMyOrders(params)
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载订单失败', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  loadOrders()
}

const handleCancel = async (orderNo) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      type: 'warning'
    })
    await cancelOrder(orderNo)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败', error)
    }
  }
}

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'info', 4: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待支付', 1: '已支付', 2: '已完成', 3: '已退款', 4: '已取消' }
  return texts[status] || '未知'
}
</script>

<style lang="scss" scoped>
.user-orders {
  .page-title {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .order-list {
    min-height: 300px;
  }

  .order-item {
    border: 1px solid #eee;
    border-radius: 8px;
    margin-bottom: 16px;
    overflow: hidden;

    .order-header {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 12px 16px;
      background: #f5f7fa;
      font-size: 13px;

      .order-no {
        font-weight: 500;
      }

      .order-time {
        color: #999;
        flex: 1;
      }
    }

    .order-body {
      display: flex;
      justify-content: space-between;
      padding: 16px;

      .order-info {
        h4 {
          font-size: 16px;
          margin-bottom: 8px;
        }

        p {
          font-size: 13px;
          color: #666;
          margin-bottom: 4px;
        }
      }

      .order-price {
        text-align: right;

        .amount {
          font-size: 20px;
          font-weight: bold;
          color: #f56c6c;
        }
      }
    }

    .order-footer {
      display: flex;
      justify-content: flex-end;
      padding: 12px 16px;
      border-top: 1px solid #eee;
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
