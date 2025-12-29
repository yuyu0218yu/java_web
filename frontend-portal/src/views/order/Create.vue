<template>
  <div class="order-create-page">
    <div class="container page-content">
      <div class="order-card card">
        <h2 class="page-title">确认订单</h2>

        <div class="ticket-info" v-if="ticket">
          <div class="info-row">
            <span class="label">景点名称</span>
            <span class="value">{{ ticket.scenicName }}</span>
          </div>
          <div class="info-row">
            <span class="label">门票类型</span>
            <span class="value">{{ ticket.ticketName }}</span>
          </div>
          <div class="info-row">
            <span class="label">游玩日期</span>
            <span class="value">{{ visitDate }}</span>
          </div>
          <div class="info-row">
            <span class="label">门票单价</span>
            <span class="value price">¥{{ ticket.sellingPrice }}</span>
          </div>
          <div class="info-row">
            <span class="label">购买数量</span>
            <span class="value">{{ quantity }} 张</span>
          </div>
          <div class="info-row total">
            <span class="label">订单金额</span>
            <span class="value price">¥{{ totalAmount.toFixed(2) }}</span>
          </div>
        </div>

        <el-divider />

        <h3 class="section-title">游客信息</h3>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="联系人" prop="contactName">
            <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item label="手机号" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remark" type="textarea" placeholder="选填" />
          </el-form-item>
        </el-form>

        <div class="order-actions">
          <div class="total-display">
            <span>应付金额：</span>
            <span class="amount">¥{{ totalAmount.toFixed(2) }}</span>
          </div>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting">
            提交订单
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTicketDetail } from '@/api/scenic'
import { createOrder } from '@/api/order'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const formRef = ref()
const submitting = ref(false)
const ticket = ref(null)

const ticketId = parseInt(route.query.ticketId)
const quantity = parseInt(route.query.quantity) || 1
const visitDate = route.query.visitDate

const form = reactive({
  contactName: '',
  contactPhone: '',
  idCard: '',
  remark: ''
})

const rules = {
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '请输入正确的身份证号', trigger: 'blur' }
  ]
}

const totalAmount = computed(() => {
  return ticket.value ? ticket.value.sellingPrice * quantity : 0
})

onMounted(async () => {
  if (!ticketId || !visitDate) {
    ElMessage.error('订单参数错误')
    router.back()
    return
  }

  try {
    const res = await getTicketDetail(ticketId)
    ticket.value = res.data
  } catch (error) {
    console.error('获取门票信息失败', error)
    ElMessage.error('获取门票信息失败')
  }
})

const handleSubmit = async () => {
  await formRef.value.validate()

  submitting.value = true
  try {
    const res = await createOrder({
      ticketId,
      quantity,
      visitDate,
      ...form
    })
    ElMessage.success('订单提交成功')
    router.push({ path: '/order/success', query: { orderNo: res.data.orderNo } })
  } catch (error) {
    console.error('提交订单失败', error)
  } finally {
    submitting.value = false
  }
}
</script>

<style lang="scss" scoped>
.order-create-page {
  .order-card {
    max-width: 700px;
    margin: 0 auto;
    padding: 32px;

    .page-title {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 24px;
      text-align: center;
    }

    .ticket-info {
      .info-row {
        display: flex;
        justify-content: space-between;
        padding: 12px 0;
        border-bottom: 1px dashed #eee;

        .label {
          color: #666;
        }

        .value {
          font-weight: 500;

          &.price {
            color: #f56c6c;
          }
        }

        &.total {
          border-bottom: none;
          padding-top: 16px;

          .label {
            font-size: 16px;
            font-weight: 500;
          }

          .value {
            font-size: 24px;
          }
        }
      }
    }

    .section-title {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 16px;
    }

    .order-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 32px;
      padding-top: 24px;
      border-top: 1px solid #eee;

      .total-display {
        font-size: 16px;

        .amount {
          font-size: 28px;
          font-weight: bold;
          color: #f56c6c;
        }
      }

      .el-button {
        width: 200px;
        height: 48px;
        font-size: 16px;
      }
    }
  }
}
</style>
