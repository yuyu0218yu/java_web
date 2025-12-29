<template>
  <div class="order-manage-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="searchForm.contactName" placeholder="请输入联系人" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.orderStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="待支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已使用" :value="2" />
            <el-option label="已取消" :value="3" />
            <el-option label="已退款" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="action-buttons">
        <el-button type="success" @click="handleExport">
          <el-icon><Download /></el-icon> 导出订单
        </el-button>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="scenicName" label="景点" width="150" />
        <el-table-column prop="ticketName" label="门票" width="120" />
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="visitDate" label="游玩日期" width="120" />
        <el-table-column label="数量/金额" width="120">
          <template #default="{ row }">
            <div>{{ row.quantity }}张</div>
            <div style="color: #f56c6c; font-weight: bold;">¥{{ row.totalAmount }}</div>
          </template>
        </el-table-column>
        <el-table-column label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.orderStatus)" size="small">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button v-if="row.orderStatus === 1" type="success" link @click="handleConfirmUse(row)">核销</el-button>
            <el-button v-if="row.orderStatus === 1" type="warning" link @click="handleRefund(row)">退款</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadData"
        @size-change="loadData"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 订单详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <el-descriptions v-if="currentOrder" :column="2" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.orderStatus)" size="small">
            {{ getStatusText(currentOrder.orderStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="景点名称">{{ currentOrder.scenicName }}</el-descriptions-item>
        <el-descriptions-item label="门票类型">{{ currentOrder.ticketName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentOrder.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentOrder.contactIdCard }}</el-descriptions-item>
        <el-descriptions-item label="游玩日期">{{ currentOrder.visitDate }}</el-descriptions-item>
        <el-descriptions-item label="购买数量">{{ currentOrder.quantity }} 张</el-descriptions-item>
        <el-descriptions-item label="单价">¥{{ currentOrder.unitPrice }}</el-descriptions-item>
        <el-descriptions-item label="总金额">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ currentOrder.totalAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ currentOrder.payTime || '未支付' }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const detailVisible = ref(false)
const tableData = ref([])
const currentOrder = ref(null)

const searchForm = reactive({
  orderNo: '',
  contactName: '',
  orderStatus: null
})

const page = reactive({
  current: 1,
  size: 10,
  total: 0
})

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'info', 4: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待支付', 1: '已支付', 2: '已使用', 3: '已取消', 4: '已退款' }
  return texts[status] || '未知'
}

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/order/list', {
      params: { current: page.current, size: page.size, ...searchForm }
    })
    tableData.value = res.data?.records || []
    page.total = res.data?.total || 0
  } catch (error) {
    console.error('加载数据失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, { orderNo: '', contactName: '', orderStatus: null })
  handleSearch()
}

const handleView = (row) => {
  currentOrder.value = row
  detailVisible.value = true
}

const handleConfirmUse = async (row) => {
  try {
    await ElMessageBox.confirm('确认核销该订单吗？', '提示', { type: 'warning' })
    await request.put(`/admin/order/${row.id}/verify`)
    ElMessage.success('核销成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('核销失败', error)
  }
}

const handleRefund = async (row) => {
  try {
    await ElMessageBox.confirm('确认退款该订单吗？', '提示', { type: 'warning' })
    await request.put(`/admin/order/${row.id}/refund`)
    ElMessage.success('退款成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('退款失败', error)
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}
</script>

<style scoped lang="scss">
.order-manage-page {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .search-form {
    margin-bottom: 16px;
  }

  .action-buttons {
    display: flex;
    gap: 10px;
  }

  .table-card {
    min-height: 600px;
  }
}
</style>
