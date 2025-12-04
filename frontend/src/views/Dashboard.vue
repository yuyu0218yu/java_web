<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6" v-for="card in statsCards" :key="card.title">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" :style="{ backgroundColor: card.color }">
              <el-icon :size="24"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ card.value }}</div>
              <div class="stats-title">{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>用户增长趋势</span>
            </div>
          </template>
          <div class="chart-placeholder">
            <el-icon :size="48"><TrendCharts /></el-icon>
            <p>用户增长趋势图表</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>权限分布</span>
            </div>
          </template>
          <div class="chart-placeholder">
            <el-icon :size="48"><PieChart /></el-icon>
            <p>权限分布图表</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="activity-card">
          <template #header>
            <div class="card-header">
              <span>最近活动</span>
              <el-button type="text">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentActivities" style="width: 100%">
            <el-table-column prop="time" label="时间" width="180" />
            <el-table-column prop="user" label="用户" width="120" />
            <el-table-column prop="action" label="操作" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === '成功' ? 'success' : 'danger'" size="small">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, UserFilled, Lock, Key, TrendCharts, PieChart } from '@element-plus/icons-vue'

// 统计卡片数据
const statsCards = ref([
  {
    title: '总用户数',
    value: 156,
    icon: User,
    color: '#409EFF'
  },
  {
    title: '活跃用户',
    value: 89,
    icon: UserFilled,
    color: '#67C23A'
  },
  {
    title: '角色数量',
    value: 12,
    icon: Lock,
    color: '#E6A23C'
  },
  {
    title: '权限数量',
    value: 45,
    icon: Key,
    color: '#F56C6C'
  }
])

// 最近活动数据
const recentActivities = ref([
  {
    time: '2025-12-05 01:10:00',
    user: 'admin',
    action: '登录系统',
    status: '成功'
  },
  {
    time: '2025-12-05 01:05:00',
    user: 'manager',
    action: '创建用户',
    status: '成功'
  },
  {
    time: '2025-12-05 00:58:00',
    user: 'user1',
    action: '修改密码',
    status: '成功'
  },
  {
    time: '2025-12-05 00:45:00',
    user: 'guest',
    action: '访问受限页面',
    status: '失败'
  }
])

onMounted(() => {
  // 这里可以加载实际的统计数据
  console.log('Dashboard mounted')
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-card {
  margin-bottom: 20px;
}

.stats-content {
  display: flex;
  align-items: center;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 16px;
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-title {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.chart-card,
.activity-card {
  height: 400px;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.chart-placeholder p {
  margin-top: 16px;
  font-size: 14px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
