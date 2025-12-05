<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6" v-for="(card, index) in statsCards" :key="card.title">
        <transition name="card-fade" appear>
          <el-card 
            class="stats-card" 
            shadow="hover"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="stats-content">
              <div class="stats-icon" :style="{ background: card.gradient }">
                <el-icon :size="28"><component :is="card.icon" /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-number">
                  <span class="count-up">{{ animatedValues[index] }}</span>
                  <span class="stats-trend" :class="card.trend > 0 ? 'up' : 'down'" v-if="card.trend">
                    <el-icon><component :is="card.trend > 0 ? 'CaretTop' : 'CaretBottom'" /></el-icon>
                    {{ Math.abs(card.trend) }}%
                  </span>
                </div>
                <div class="stats-title">{{ card.title }}</div>
              </div>
            </div>
            <div class="stats-footer">
              <span>较上月</span>
              <span :class="card.trend > 0 ? 'text-success' : 'text-danger'">
                {{ card.trend > 0 ? '+' : '' }}{{ card.trend }}%
              </span>
            </div>
          </el-card>
        </transition>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <transition name="slide-up" appear>
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <div class="header-title">
                  <el-icon class="header-icon"><TrendCharts /></el-icon>
                  <span>用户增长趋势</span>
                </div>
                <el-button-group>
                  <el-button size="small" :type="chartPeriod === 'week' ? 'primary' : ''" @click="chartPeriod = 'week'">周</el-button>
                  <el-button size="small" :type="chartPeriod === 'month' ? 'primary' : ''" @click="chartPeriod = 'month'">月</el-button>
                  <el-button size="small" :type="chartPeriod === 'year' ? 'primary' : ''" @click="chartPeriod = 'year'">年</el-button>
                </el-button-group>
              </div>
            </template>
            <div class="chart-content">
              <div class="mini-chart">
                <div 
                  v-for="(bar, idx) in chartData" 
                  :key="idx" 
                  class="chart-bar"
                  :style="{ 
                    height: `${bar.value}%`,
                    animationDelay: `${idx * 0.05}s`
                  }"
                >
                  <div class="bar-tooltip">{{ bar.label }}: {{ bar.count }}</div>
                </div>
              </div>
              <div class="chart-labels">
                <span v-for="(bar, idx) in chartData" :key="idx">{{ bar.label }}</span>
              </div>
            </div>
          </el-card>
        </transition>
      </el-col>
      <el-col :span="12">
        <transition name="slide-up" appear style="animation-delay: 0.1s">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <div class="header-title">
                  <el-icon class="header-icon"><PieChart /></el-icon>
                  <span>权限分布</span>
                </div>
                <el-tag type="info" size="small">共 {{ totalPermissions }} 个权限</el-tag>
              </div>
            </template>
            <div class="pie-chart-content">
              <div class="pie-chart-visual">
                <div 
                  class="pie-ring"
                  :style="{ 
                    background: `conic-gradient(${pieGradient})`
                  }"
                >
                  <div class="pie-center">
                    <span class="pie-total">{{ totalPermissions }}</span>
                    <span class="pie-label">总权限</span>
                  </div>
                </div>
              </div>
              <div class="pie-legend">
                <div 
                  v-for="item in permissionDistribution" 
                  :key="item.name" 
                  class="legend-item"
                >
                  <span class="legend-color" :style="{ backgroundColor: item.color }"></span>
                  <span class="legend-name">{{ item.name }}</span>
                  <span class="legend-value">{{ item.value }}</span>
                  <span class="legend-percent">{{ item.percent }}%</span>
                </div>
              </div>
            </div>
          </el-card>
        </transition>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <transition name="slide-up" appear style="animation-delay: 0.2s">
          <el-card class="activity-card">
            <template #header>
              <div class="card-header">
                <div class="header-title">
                  <el-icon class="header-icon pulse"><Bell /></el-icon>
                  <span>最近活动</span>
                  <el-badge :value="recentActivities.length" class="activity-badge" />
                </div>
                <el-button type="primary" link>
                  查看全部
                  <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                </el-button>
              </div>
            </template>
            <el-table 
              :data="recentActivities" 
              style="width: 100%"
              :row-class-name="tableRowClassName"
            >
              <el-table-column prop="time" label="时间" width="180">
                <template #default="scope">
                  <div class="time-cell">
                    <el-icon><Clock /></el-icon>
                    <span>{{ scope.row.time }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="user" label="用户" width="120">
                <template #default="scope">
                  <div class="user-cell">
                    <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(scope.row.user) }">
                      {{ scope.row.user.charAt(0).toUpperCase() }}
                    </el-avatar>
                    <span>{{ scope.row.user }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="action" label="操作">
                <template #default="scope">
                  <div class="action-cell">
                    <el-icon :style="{ color: getActionColor(scope.row.action) }">
                      <component :is="getActionIcon(scope.row.action)" />
                    </el-icon>
                    <span>{{ scope.row.action }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                  <el-tag 
                    :type="scope.row.status === '成功' ? 'success' : 'danger'" 
                    size="small"
                    effect="light"
                    round
                  >
                    <el-icon v-if="scope.row.status === '成功'"><CircleCheck /></el-icon>
                    <el-icon v-else><CircleClose /></el-icon>
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </transition>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { 
  User, UserFilled, Lock, Key, TrendCharts, PieChart, 
  CaretTop, CaretBottom, Bell, ArrowRight, Clock,
  CircleCheck, CircleClose, Connection, Edit, Unlock
} from '@element-plus/icons-vue'

// 图表周期
const chartPeriod = ref('month')

// 动画帧ID列表，用于清理
const animationFrameIds = ref([])

// 统计卡片数据
const statsCards = ref([
  {
    title: '总用户数',
    value: 156,
    icon: User,
    gradient: 'linear-gradient(135deg, #409EFF 0%, #53a8ff 100%)',
    trend: 12.5
  },
  {
    title: '活跃用户',
    value: 89,
    icon: UserFilled,
    gradient: 'linear-gradient(135deg, #67C23A 0%, #85ce61 100%)',
    trend: 8.2
  },
  {
    title: '角色数量',
    value: 12,
    icon: Lock,
    gradient: 'linear-gradient(135deg, #E6A23C 0%, #ebb563 100%)',
    trend: -2.1
  },
  {
    title: '权限数量',
    value: 45,
    icon: Key,
    gradient: 'linear-gradient(135deg, #F56C6C 0%, #f78989 100%)',
    trend: 15.8
  }
])

// 动画数值
const animatedValues = ref([0, 0, 0, 0])

// 图表数据
const chartData = ref([
  { label: '1月', value: 30, count: 45 },
  { label: '2月', value: 45, count: 68 },
  { label: '3月', value: 55, count: 82 },
  { label: '4月', value: 40, count: 60 },
  { label: '5月', value: 65, count: 98 },
  { label: '6月', value: 75, count: 112 },
  { label: '7月', value: 85, count: 128 },
  { label: '8月', value: 70, count: 105 },
  { label: '9月', value: 80, count: 120 },
  { label: '10月', value: 90, count: 135 },
  { label: '11月', value: 95, count: 143 },
  { label: '12月', value: 100, count: 156 }
])

// 权限分布数据
const permissionDistribution = ref([
  { name: '菜单权限', value: 18, color: '#409EFF' },
  { name: '按钮权限', value: 15, color: '#67C23A' },
  { name: '接口权限', value: 12, color: '#E6A23C' }
])

// 计算总权限数
const totalPermissions = computed(() => {
  return permissionDistribution.value.reduce((sum, item) => sum + item.value, 0)
})

// 计算百分比
permissionDistribution.value.forEach(item => {
  item.percent = Math.round((item.value / totalPermissions.value) * 100)
})

// 生成饼图渐变
const pieGradient = computed(() => {
  let gradient = ''
  let currentAngle = 0
  permissionDistribution.value.forEach((item, index) => {
    const angle = (item.value / totalPermissions.value) * 360
    gradient += `${item.color} ${currentAngle}deg ${currentAngle + angle}deg`
    currentAngle += angle
    if (index < permissionDistribution.value.length - 1) {
      gradient += ', '
    }
  })
  return gradient
})

// 最近活动数据
const recentActivities = ref([
  {
    time: '2025-12-05 09:10:00',
    user: 'admin',
    action: '登录系统',
    status: '成功'
  },
  {
    time: '2025-12-05 09:05:00',
    user: 'manager',
    action: '创建用户',
    status: '成功'
  },
  {
    time: '2025-12-05 08:58:00',
    user: 'user1',
    action: '修改密码',
    status: '成功'
  },
  {
    time: '2025-12-05 08:45:00',
    user: 'guest',
    action: '访问受限页面',
    status: '失败'
  }
])

// 表格行样式
const tableRowClassName = ({ row, rowIndex }) => {
  return `animate-row delay-${rowIndex}`
}

// 获取头像颜色
const getAvatarColor = (username) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
  const index = username.charCodeAt(0) % colors.length
  return colors[index]
}

// 获取操作图标
const getActionIcon = (action) => {
  const iconMap = {
    '登录系统': Connection,
    '创建用户': UserFilled,
    '修改密码': Edit,
    '访问受限页面': Unlock
  }
  return iconMap[action] || Connection
}

// 获取操作颜色
const getActionColor = (action) => {
  const colorMap = {
    '登录系统': '#409EFF',
    '创建用户': '#67C23A',
    '修改密码': '#E6A23C',
    '访问受限页面': '#F56C6C'
  }
  return colorMap[action] || '#909399'
}

// 数字动画效果
const animateValue = (index, targetValue, duration = 1500) => {
  const startTime = Date.now()
  const animate = () => {
    const elapsed = Date.now() - startTime
    const progress = Math.min(elapsed / duration, 1)
    // 使用 easeOutQuart 缓动函数
    const eased = 1 - Math.pow(1 - progress, 4)
    animatedValues.value[index] = Math.floor(eased * targetValue)
    if (progress < 1) {
      const frameId = requestAnimationFrame(animate)
      animationFrameIds.value[index] = frameId
    }
  }
  const frameId = requestAnimationFrame(animate)
  animationFrameIds.value[index] = frameId
}

onMounted(() => {
  // 启动数字动画
  statsCards.value.forEach((card, index) => {
    setTimeout(() => {
      animateValue(index, card.value)
    }, index * 100)
  })
})

onUnmounted(() => {
  // 清理动画帧
  animationFrameIds.value.forEach(frameId => {
    if (frameId) {
      cancelAnimationFrame(frameId)
    }
  })
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

/* 卡片入场动画 */
.card-fade-enter-active {
  animation: cardFadeIn 0.6s ease-out both;
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滑入动画 */
.slide-up-enter-active {
  animation: slideUp 0.5s ease-out both;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stats-card {
  margin-bottom: 20px;
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
}

.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
}

.stats-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;
}

.stats-card:hover .stats-icon {
  transform: scale(1.1) rotate(5deg);
}

.stats-info {
  flex: 1;
}

.stats-number {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.count-up {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.stats-trend {
  display: flex;
  align-items: center;
  font-size: 12px;
  font-weight: 500;
  padding: 2px 6px;
  border-radius: 4px;
}

.stats-trend.up {
  color: #67C23A;
  background: rgba(103, 194, 58, 0.1);
}

.stats-trend.down {
  color: #F56C6C;
  background: rgba(245, 108, 108, 0.1);
}

.stats-title {
  font-size: 14px;
  color: #909399;
  margin-top: 6px;
}

.stats-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  font-size: 12px;
  color: #909399;
}

.text-success {
  color: #67C23A;
}

.text-danger {
  color: #F56C6C;
}

.chart-card,
.activity-card {
  border-radius: 12px;
  min-height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.header-icon {
  font-size: 18px;
  color: #409EFF;
}

.header-icon.pulse {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

.activity-badge {
  margin-left: 8px;
}

/* 迷你柱状图 */
.chart-content {
  padding: 20px 0;
}

.mini-chart {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 250px;
  padding: 0 10px;
  gap: 8px;
}

.chart-bar {
  flex: 1;
  background: linear-gradient(180deg, #409EFF 0%, #79bbff 100%);
  border-radius: 4px 4px 0 0;
  min-height: 10px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  animation: barGrow 0.8s ease-out both;
}

@keyframes barGrow {
  from {
    height: 0 !important;
  }
}

.chart-bar:hover {
  background: linear-gradient(180deg, #337ecc 0%, #409EFF 100%);
  transform: scaleY(1.05);
}

.bar-tooltip {
  position: absolute;
  top: -30px;
  left: 50%;
  transform: translateX(-50%);
  background: #303133;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.2s;
  pointer-events: none;
}

.chart-bar:hover .bar-tooltip {
  opacity: 1;
}

.chart-labels {
  display: flex;
  justify-content: space-around;
  margin-top: 12px;
  font-size: 11px;
  color: #909399;
}

/* 饼图 */
.pie-chart-content {
  display: flex;
  align-items: center;
  padding: 20px;
  gap: 40px;
}

.pie-chart-visual {
  flex-shrink: 0;
}

.pie-ring {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  position: relative;
  animation: pieRotate 1s ease-out;
}

@keyframes pieRotate {
  from {
    transform: rotate(-90deg);
    opacity: 0;
  }
  to {
    transform: rotate(0);
    opacity: 1;
  }
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100px;
  height: 100px;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.05);
}

.pie-total {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.pie-label {
  font-size: 12px;
  color: #909399;
}

.pie-legend {
  flex: 1;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  transition: background 0.2s;
}

.legend-item:last-child {
  border-bottom: none;
}

.legend-item:hover {
  background: #f5f7fa;
  margin: 0 -12px;
  padding: 12px;
  border-radius: 8px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}

.legend-name {
  flex: 1;
  color: #606266;
}

.legend-value {
  font-weight: 600;
  color: #303133;
}

.legend-percent {
  color: #909399;
  font-size: 12px;
}

/* 表格样式 */
.time-cell,
.user-cell,
.action-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.time-cell .el-icon {
  color: #909399;
}

/* 表格行动画 */
:deep(.animate-row) {
  animation: rowFadeIn 0.4s ease-out both;
}

:deep(.delay-0) { animation-delay: 0s; }
:deep(.delay-1) { animation-delay: 0.1s; }
:deep(.delay-2) { animation-delay: 0.2s; }
:deep(.delay-3) { animation-delay: 0.3s; }

@keyframes rowFadeIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
