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
                </div>
                <el-button type="primary" link @click="handleViewAllActivities">
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

    <!-- 全部活动对话框 -->
    <el-dialog 
      v-model="showAllActivitiesDialog" 
      title="全部活动记录" 
      width="95%"
      top="5vh"
      destroy-on-close
    >
      <el-table 
        v-loading="allActivitiesLoading"
        :data="allActivities" 
        style="width: 100%"
        max-height="60vh"
      >
        <el-table-column prop="createTime" label="时间" width="180">
          <template #default="scope">
            <div class="time-cell">
              <el-icon><Clock /></el-icon>
              <span>{{ formatTime(scope.row.createTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户" width="120">
          <template #default="scope">
            <div class="user-cell">
              <el-avatar :size="24" :style="{ backgroundColor: getAvatarColor(scope.row.username || '未知') }">
                {{ (scope.row.username || '?').charAt(0).toUpperCase() }}
              </el-avatar>
              <span>{{ scope.row.username || '未知' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="operation" label="操作" width="150" />
        <el-table-column prop="method" label="请求方法" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getMethodType(scope.row.method)">
              {{ scope.row.method }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestUrl" label="请求路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="executionTime" label="耗时" width="100">
          <template #default="scope">
            <span>{{ scope.row.executionTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag 
              :type="scope.row.status === 1 ? 'success' : 'danger'" 
              size="small"
              effect="light"
              round
            >
              {{ scope.row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="activitiesPagination.current"
        v-model:page-size="activitiesPagination.size"
        :total="activitiesPagination.total"
        :page-sizes="[20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 16px; justify-content: flex-end"
        @current-change="fetchAllActivities"
        @size-change="fetchAllActivities"
      />
      <template #footer>
        <el-button @click="showAllActivitiesDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { 
  User, UserFilled, Lock, Key, TrendCharts, PieChart, 
  CaretTop, CaretBottom, Bell, ArrowRight, Clock,
  CircleCheck, CircleClose, Connection, Edit, Unlock
} from '@element-plus/icons-vue'
import { dashboardApi } from '@/api'
import { ElMessage } from 'element-plus'

// 加载状态
const loading = ref(true)

// 图表周期
const chartPeriod = ref('month')

// 是否显示全部活动对话框
const showAllActivitiesDialog = ref(false)

// 全部活动数据
const allActivities = ref([])
const allActivitiesLoading = ref(false)
const activitiesPagination = ref({
  current: 1,
  size: 20,
  total: 0
})

// 动画帧ID列表，用于清理
const animationFrameIds = ref([])

// 统计卡片数据
const statsCards = ref([
  {
    title: '总用户数',
    value: 0,
    icon: User,
    gradient: 'linear-gradient(135deg, #409EFF 0%, #53a8ff 100%)',
    trend: 0
  },
  {
    title: '活跃用户',
    value: 0,
    icon: UserFilled,
    gradient: 'linear-gradient(135deg, #67C23A 0%, #85ce61 100%)',
    trend: 0
  },
  {
    title: '角色数量',
    value: 0,
    icon: Lock,
    gradient: 'linear-gradient(135deg, #E6A23C 0%, #ebb563 100%)',
    trend: 0
  },
  {
    title: '权限数量',
    value: 0,
    icon: Key,
    gradient: 'linear-gradient(135deg, #F56C6C 0%, #f78989 100%)',
    trend: 0
  }
])

// 动画数值
const animatedValues = ref([0, 0, 0, 0])

// 图表数据
const chartData = ref([])

// 权限分布数据
const permissionDistribution = ref([])

// 计算总权限数
const totalPermissions = computed(() => {
  return permissionDistribution.value.reduce((sum, item) => sum + item.value, 0)
})

// 生成饼图渐变
const pieGradient = computed(() => {
  if (totalPermissions.value === 0) return '#ccc'
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
const recentActivities = ref([])

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

// 获取仪表板数据
const fetchDashboardData = async (period = 'month') => {
  try {
    loading.value = true
    const res = await dashboardApi.getStatistics(period)
    
    if (res.code === 200 && res.data) {
      const data = res.data
      
      // 更新统计卡片数据
      if (data.stats) {
        statsCards.value[0].value = data.stats.totalUsers || 0
        statsCards.value[0].trend = data.stats.userTrend || 0
        statsCards.value[1].value = data.stats.activeUsers || 0
        statsCards.value[1].trend = data.stats.activeTrend || 0
        statsCards.value[2].value = data.stats.roleCount || 0
        statsCards.value[2].trend = data.stats.roleTrend || 0
        statsCards.value[3].value = data.stats.permissionCount || 0
        statsCards.value[3].trend = data.stats.permissionTrend || 0
      }
      
      // 更新图表数据
      updateChartData(data)
      
      // 更新权限分布数据
      if (data.permissionDistribution && data.permissionDistribution.length > 0) {
        permissionDistribution.value = data.permissionDistribution
      }
      
      // 更新最近活动数据
      if (data.recentActivities && data.recentActivities.length > 0) {
        recentActivities.value = data.recentActivities
      }
      
      // 启动数字动画
      statsCards.value.forEach((card, index) => {
        setTimeout(() => {
          animateValue(index, card.value)
        }, index * 100)
      })
    }
  } catch (error) {
    console.error('获取仪表板数据失败:', error)
    ElMessage.error('获取仪表板数据失败')
  } finally {
    loading.value = false
  }
}

// 更新图表数据（共享方法）
const updateChartData = (data) => {
  if (data.userGrowthData && data.userGrowthData.length > 0) {
    chartData.value = data.userGrowthData
  }
}

// 仅获取图表数据（用于切换周期时）
const fetchChartData = async (period) => {
  try {
    const res = await dashboardApi.getStatistics(period)
    
    if (res.code === 200 && res.data) {
      updateChartData(res.data)
    }
  } catch (error) {
    console.error('获取图表数据失败:', error)
    ElMessage.error('获取图表数据失败')
  }
}

// 监听图表周期变化
watch(chartPeriod, (newPeriod) => {
  fetchChartData(newPeriod)
})

// 查看全部活动
const handleViewAllActivities = async () => {
  showAllActivitiesDialog.value = true
  activitiesPagination.value.current = 1
  await fetchAllActivities()
}

// 获取全部活动数据
const fetchAllActivities = async () => {
  allActivitiesLoading.value = true
  try {
    const res = await dashboardApi.getAllActivities({
      current: activitiesPagination.value.current,
      size: activitiesPagination.value.size
    })
    if (res.code === 200 && res.data) {
      allActivities.value = res.data.records || []
      activitiesPagination.value.total = res.data.total || 0
    }
  } catch (error) {
    console.error('获取全部活动失败:', error)
    ElMessage.error('获取活动记录失败')
  } finally {
    allActivitiesLoading.value = false
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

// 获取请求方法类型颜色
const getMethodType = (method) => {
  const types = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger'
  }
  return types[method] || 'info'
}

onMounted(() => {
  // 从后端获取数据
  fetchDashboardData(chartPeriod.value)
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
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  box-shadow: var(--card-shadow);
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
  color: var(--text-primary);
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
  border-top: 1px solid var(--border-color);
  font-size: 12px;
  color: var(--text-secondary);
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
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  box-shadow: var(--card-shadow);
}

:deep(.el-card__header) {
  border-bottom: 1px solid var(--border-color);
  padding: 15px 20px;
}

:deep(.el-table) {
  background-color: transparent;
  --el-table-border-color: var(--border-color);
  --el-table-header-bg-color: var(--bg-secondary);
  --el-table-row-hover-bg-color: var(--hover-bg);
  color: var(--text-secondary);
}

:deep(.el-table tr) {
  background-color: transparent;
}

:deep(.el-table th.el-table__cell) {
  background-color: var(--bg-secondary);
  border-bottom: 1px solid var(--border-color);
  color: var(--text-primary);
}

:deep(.el-table td.el-table__cell) {
  border-bottom: 1px solid var(--border-color);
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell) {
  background-color: var(--hover-bg);
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
  background: var(--bg-secondary);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
}

.pie-total {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
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
  border-bottom: 1px solid var(--border-color);
  transition: background 0.2s;
}

.legend-item:last-child {
  border-bottom: none;
}

.legend-item:hover {
  background: var(--hover-bg);
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
  color: var(--text-secondary);
}

.legend-value {
  font-weight: 600;
  color: var(--text-primary);
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
