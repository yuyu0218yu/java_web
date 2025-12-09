<template>
  <div class="menus-page">
    <!-- 操作栏 -->
    <transition name="fade-slide-down" appear>
      <el-card class="action-card">
        <div class="action-header">
          <div class="page-title">
            <el-icon class="title-icon"><Menu /></el-icon>
            <span>菜单管理</span>
            <el-tag type="info" size="small" effect="plain" round style="margin-left: 12px;">
              共 {{ getTotalMenus() }} 个菜单
            </el-tag>
            <el-tag type="success" size="small" effect="plain" round style="margin-left: 8px;">
              {{ getPermissionCount() }} 个权限
            </el-tag>
          </div>
          <div class="action-buttons">
            <el-button type="primary" @click="handleAdd()" class="action-btn primary-gradient">
              <el-icon><Plus /></el-icon>
              新增菜单
            </el-button>
            <el-dropdown @command="handleQuickAdd" style="margin-left: 10px;">
              <el-button type="success" class="action-btn">
                <el-icon><Lightning /></el-icon>
                快速添加
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="crud">批量添加 CRUD 按钮</el-dropdown-item>
                  <el-dropdown-item command="module">添加功能模块</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button type="info" @click="showPermStats = true" class="action-btn">
              <el-icon><DataAnalysis /></el-icon>
              权限统计
            </el-button>
            <el-button type="success" @click="handleExpandAll" class="action-btn">
              <el-icon><Expand /></el-icon>
              展开全部
            </el-button>
            <el-button type="warning" @click="handleCollapseAll" class="action-btn">
              <el-icon><Fold /></el-icon>
              折叠全部
            </el-button>
            <el-button type="info" @click="handleRefresh" class="action-btn">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </el-card>
    </transition>

    <!-- 菜单树形表格 -->
    <transition name="fade-slide-up" appear>
      <el-card class="table-card">
        <el-table
          v-loading="loading"
          :data="tableData"
          style="width: 100%"
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          :default-expand-all="false"
          ref="tableRef"
          highlight-current-row
          stripe
        >
          <el-table-column prop="menuName" label="菜单名称" width="220">
            <template #default="scope">
              <div class="menu-name-cell">
                <div class="menu-icon" :class="getTypeClass(scope.row.menuType)">
                  <el-icon v-if="getIconName(scope.row.icon)">
                    <component :is="getIconName(scope.row.icon)" />
                  </el-icon>
                  <el-icon v-else-if="scope.row.menuType === 'M'"><Folder /></el-icon>
                  <el-icon v-else-if="scope.row.menuType === 'C'"><Document /></el-icon>
                  <el-icon v-else><Pointer /></el-icon>
                </div>
                <span class="menu-name">{{ scope.row.menuName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="path" label="路由地址" min-width="150">
            <template #default="scope">
              <span v-if="scope.row.path" class="path-text">{{ scope.row.path }}</span>
              <span v-else class="no-path">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="component" label="组件路径" min-width="140">
            <template #default="scope">
              <span v-if="scope.row.component" class="component-text">{{ scope.row.component }}</span>
              <span v-else class="no-component">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="perms" label="权限标识" min-width="140">
            <template #default="scope">
              <el-tag v-if="scope.row.perms" effect="plain" size="small" type="info">
                {{ scope.row.perms }}
              </el-tag>
              <span v-else class="no-perms">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="menuType" label="类型" width="90" align="center">
            <template #default="scope">
              <el-tag :type="getTypeTagType(scope.row.menuType)" size="small" effect="light" round>
                {{ getTypeLabel(scope.row.menuType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderNum" label="排序" width="70" align="center">
            <template #default="scope">
              <el-tag type="info" size="small" effect="plain" round>
                {{ scope.row.orderNum }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visible" label="可见" width="80" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.visible === 1 ? 'success' : 'info'" size="small" effect="light">
                {{ scope.row.visible === 1 ? '显示' : '隐藏' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                active-color="#67C23A"
                inactive-color="#dcdfe6"
                @change="handleStatusChange(scope.row)"
                :loading="scope.row.statusLoading"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220">
            <template #default="scope">
              <div class="operation-buttons">
                <el-tooltip content="新增子菜单" placement="top">
                  <el-button type="success" size="small" circle @click="handleAdd(scope.row)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip v-if="scope.row.menuType === 'C'" content="批量添加CRUD" placement="top">
                  <el-button type="primary" size="small" circle @click="handleBatchAddCrud(scope.row)">
                    <el-icon><Lightning /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-button type="warning" size="small" circle @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </transition>

    <!-- 菜单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      @close="handleDialogClose"
      class="menu-dialog"
      :close-on-click-modal="false"
    >
      <transition name="fade" appear>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="90px"
          style="max-width: 550px; margin: 0 auto;"
        >
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="上级菜单" prop="parentId">
                <el-tree-select
                  v-model="form.parentId"
                  :data="menuTreeOptions"
                  :props="treeSelectProps"
                  placeholder="请选择上级菜单"
                  clearable
                  check-strictly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单类型" prop="menuType">
                <el-radio-group v-model="form.menuType" class="type-radio-group">
                  <el-radio label="M" border>
                    <el-icon style="margin-right: 4px;"><Folder /></el-icon>
                    目录
                  </el-radio>
                  <el-radio label="C" border>
                    <el-icon style="margin-right: 4px;"><Document /></el-icon>
                    菜单
                  </el-radio>
                  <el-radio label="F" border>
                    <el-icon style="margin-right: 4px;"><Pointer /></el-icon>
                    按钮
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="菜单名称" prop="menuName">
                <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序" prop="orderNum">
                <el-input-number v-model="form.orderNum" :min="0" :max="999" style="width: 100%;" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType !== 'F'">
              <el-form-item label="路由地址" prop="path">
                <el-input v-model="form.path" placeholder="如：/users" />
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType === 'C'">
              <el-form-item label="组件路径" prop="component">
                <el-select
                  v-model="form.component"
                  placeholder="请选择组件"
                  filterable
                  allow-create
                  clearable
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in componentOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限标识" prop="perms">
                <el-select
                  v-model="form.perms"
                  placeholder="请选择或输入权限"
                  filterable
                  allow-create
                  clearable
                  style="width: 100%"
                >
                  <el-option-group
                    v-for="group in permissionSuggestions"
                    :key="group.label"
                    :label="group.label"
                  >
                    <el-option
                      v-for="item in group.options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                      <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span>{{ item.label }}</span>
                        <span style="color: #909399; font-size: 12px;">{{ item.value }}</span>
                      </div>
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.menuType !== 'F'">
              <el-form-item label="图标" prop="icon">
                <el-select
                  v-model="form.icon"
                  placeholder="请选择图标"
                  filterable
                  allow-create
                  clearable
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in iconOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                    <div style="display: flex; align-items: center; gap: 8px;">
                      <el-icon><component :is="item.value" /></el-icon>
                      <span>{{ item.label }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否可见" prop="visible">
                <el-radio-group v-model="form.visible">
                  <el-radio :label="1">显示</el-radio>
                  <el-radio :label="0">隐藏</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">
                    <el-icon style="color: #67C23A;"><CircleCheck /></el-icon>
                    正常
                  </el-radio>
                  <el-radio :label="0">
                    <el-icon style="color: #F56C6C;"><CircleClose /></el-icon>
                    停用
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </transition>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            <el-icon><Check /></el-icon>
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 批量添加 CRUD 对话框 -->
    <el-dialog
      v-model="crudDialogVisible"
      title="批量添加 CRUD 按钮权限"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="crudForm" label-width="100px">
        <el-form-item label="目标菜单">
          <el-tag type="success" size="large">{{ crudForm.parentMenuName }}</el-tag>
        </el-form-item>
        <el-form-item label="权限前缀">
          <el-input v-model="crudForm.prefix" placeholder="如：user, role, menu">
            <template #prepend>权限格式</template>
            <template #append>:操作</template>
          </el-input>
          <div class="perm-preview">
            预览: <el-tag size="small" v-for="action in ['view', 'create', 'update', 'delete']" :key="action" style="margin: 2px;">
              {{ crudForm.prefix }}:{{ action }}
            </el-tag>
          </div>
        </el-form-item>
        <el-form-item label="选择操作">
          <el-checkbox-group v-model="crudForm.actions">
            <el-checkbox label="view">查询 (view)</el-checkbox>
            <el-checkbox label="create">新增 (create)</el-checkbox>
            <el-checkbox label="update">修改 (update)</el-checkbox>
            <el-checkbox label="delete">删除 (delete)</el-checkbox>
            <el-checkbox label="export">导出 (export)</el-checkbox>
            <el-checkbox label="import">导入 (import)</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="crudDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchCrud" :loading="crudLoading">
          批量创建
        </el-button>
      </template>
    </el-dialog>

    <!-- 快速添加模块对话框 -->
    <el-dialog
      v-model="moduleDialogVisible"
      title="快速添加功能模块"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-form :model="moduleForm" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="moduleForm.parentId"
            :data="menuTreeOptions"
            :props="treeSelectProps"
            placeholder="请选择上级菜单"
            clearable
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="模块名称">
          <el-input v-model="moduleForm.moduleName" placeholder="如：产品、订单、客户" />
        </el-form-item>
        <el-form-item label="权限前缀">
          <el-input v-model="moduleForm.prefix" placeholder="如：product, order, customer" />
        </el-form-item>
        <el-form-item label="路由路径">
          <el-input v-model="moduleForm.path" placeholder="如：/products">
            <template #prepend>/</template>
          </el-input>
        </el-form-item>
        <el-form-item label="组件名称">
          <el-input v-model="moduleForm.component" placeholder="如：Products" />
        </el-form-item>
        <el-form-item label="包含操作">
          <el-checkbox-group v-model="moduleForm.actions">
            <el-checkbox label="view">查询</el-checkbox>
            <el-checkbox label="create">新增</el-checkbox>
            <el-checkbox label="update">修改</el-checkbox>
            <el-checkbox label="delete">删除</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="moduleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitModule" :loading="moduleLoading">
          创建模块
        </el-button>
      </template>
    </el-dialog>

    <!-- 权限统计对话框 -->
    <el-dialog
      v-model="showPermStats"
      title="权限使用统计"
      width="700px"
    >
      <div class="perm-stats">
        <!-- 概览卡片 -->
        <el-row :gutter="20" class="stats-overview">
          <el-col :span="6">
            <div class="stat-card stat-total">
              <div class="stat-value">{{ permStats.total }}</div>
              <div class="stat-label">总权限数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card stat-dir">
              <div class="stat-value">{{ permStats.dir }}</div>
              <div class="stat-label">目录</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card stat-menu">
              <div class="stat-value">{{ permStats.menu }}</div>
              <div class="stat-label">菜单</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card stat-btn">
              <div class="stat-value">{{ permStats.button }}</div>
              <div class="stat-label">按钮</div>
            </div>
          </el-col>
        </el-row>

        <!-- 权限列表 -->
        <div class="perm-list-section">
          <div class="section-title">
            <el-icon><Key /></el-icon>
            所有权限标识
          </div>
          <div class="perm-tags">
            <el-tag
              v-for="perm in permStats.allPerms"
              :key="perm"
              effect="plain"
              size="small"
              style="margin: 4px;"
            >
              {{ perm }}
            </el-tag>
            <el-empty v-if="permStats.allPerms.length === 0" description="暂无权限" :image-size="60" />
          </div>
        </div>

        <!-- 按模块分组 -->
        <div class="perm-list-section">
          <div class="section-title">
            <el-icon><Collection /></el-icon>
            按模块分组
          </div>
          <el-collapse>
            <el-collapse-item
              v-for="(perms, module) in permStats.byModule"
              :key="module"
              :title="`${module} (${perms.length}个权限)`"
              :name="module"
            >
              <el-tag
                v-for="perm in perms"
                :key="perm"
                effect="light"
                size="small"
                style="margin: 4px;"
              >
                {{ perm }}
              </el-tag>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Expand, Fold, Refresh, Edit, Delete, Menu, Pointer,
  Folder, Document, CircleCheck, CircleClose, Close, Check,
  House, User, Setting, UserFilled, Avatar, Key, Lightning,
  ArrowDown, DataAnalysis, Collection
} from '@element-plus/icons-vue'
import { menuApi } from '@/api'
import { fetchComponents, fetchIcons, viewComponents, iconList } from '@/config/components'

// 获取图标名称（验证后返回，无效返回 null）
const getIconName = (iconName) => {
  if (!iconName || iconName === '#') return null
  return iconName
}

// HTML实体编码，防止XSS
const escapeHtml = (text) => {
  if (!text) return ''
  const div = document.createElement('div')
  div.textContent = text
  return div.innerHTML
}

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const tableRef = ref()
const formRef = ref()

// 权限标识建议模板
const permissionSuggestions = ref([
  {
    label: '用户管理',
    options: [
      { label: '用户查看', value: 'user:view' },
      { label: '用户新增', value: 'user:create' },
      { label: '用户修改', value: 'user:update' },
      { label: '用户删除', value: 'user:delete' },
      { label: '用户导出', value: 'user:export' },
    ]
  },
  {
    label: '角色管理',
    options: [
      { label: '角色查看', value: 'role:view' },
      { label: '角色新增', value: 'role:create' },
      { label: '角色修改', value: 'role:update' },
      { label: '角色删除', value: 'role:delete' },
    ]
  },
  {
    label: '菜单管理',
    options: [
      { label: '菜单查看', value: 'menu:view' },
      { label: '菜单新增', value: 'menu:create' },
      { label: '菜单修改', value: 'menu:update' },
      { label: '菜单删除', value: 'menu:delete' },
    ]
  },
  {
    label: '部门管理',
    options: [
      { label: '部门查看', value: 'dept:view' },
      { label: '部门新增', value: 'dept:create' },
      { label: '部门修改', value: 'dept:update' },
      { label: '部门删除', value: 'dept:delete' },
    ]
  },
  {
    label: '系统监控',
    options: [
      { label: '仪表盘', value: 'dashboard:view' },
      { label: '日志查看', value: 'log:view' },
      { label: '系统配置', value: 'config:view' },
    ]
  },
  {
    label: '通用操作',
    options: [
      { label: '导出', value: 'export' },
      { label: '导入', value: 'import' },
      { label: '打印', value: 'print' },
    ]
  }
])

// 批量添加 CRUD 相关
const crudDialogVisible = ref(false)
const crudLoading = ref(false)
const crudForm = reactive({
  parentId: null,
  parentMenuName: '',
  prefix: '',
  actions: ['view', 'create', 'update', 'delete']
})

// 快速添加模块相关
const moduleDialogVisible = ref(false)
const moduleLoading = ref(false)
const moduleForm = reactive({
  parentId: 0,
  moduleName: '',
  prefix: '',
  path: '',
  component: '',
  actions: ['view', 'create', 'update', 'delete']
})

// 权限统计相关
const showPermStats = ref(false)
const permStats = computed(() => {
  const stats = {
    total: 0,
    dir: 0,
    menu: 0,
    button: 0,
    allPerms: [],
    byModule: {}
  }

  const collectStats = (nodes) => {
    nodes.forEach(node => {
      stats.total++
      if (node.menuType === 'M') stats.dir++
      else if (node.menuType === 'C') stats.menu++
      else if (node.menuType === 'F') stats.button++

      if (node.perms) {
        stats.allPerms.push(node.perms)
        // 按模块分组
        const module = node.perms.split(':')[0]
        if (!stats.byModule[module]) {
          stats.byModule[module] = []
        }
        stats.byModule[module].push(node.perms)
      }

      if (node.children) {
        collectStats(node.children)
      }
    })
  }

  collectStats(tableData.value)
  return stats
})

// 表单数据
const form = reactive({
  id: null,
  parentId: 0,
  menuType: 'C',
  menuName: '',
  path: '',
  component: '',
  perms: '',
  icon: '',
  orderNum: 0,
  visible: 1,
  status: 1
})

// 表格数据
const tableData = ref([])
const menuTreeOptions = ref([])

// 可选组件列表（从后端 API 加载）
const componentOptions = ref(viewComponents)

// 可选图标列表（从后端 API 加载）
const iconOptions = ref(iconList)

// 验证图标是否在允许列表中
const isValidIcon = (iconName) => {
  if (!iconName || iconName === '#') return false
  return iconOptions.value.some(icon => icon.value === iconName)
}

// 加载配置选项
const loadConfigOptions = async () => {
  try {
    const [components, icons] = await Promise.all([
      fetchComponents(),
      fetchIcons()
    ])
    componentOptions.value = components
    iconOptions.value = icons
  } catch (error) {
    console.warn('加载配置选项失败:', error)
  }
}

// 树形选择组件配置
const treeSelectProps = {
  children: 'children',
  label: 'menuName',
  value: 'id'
}

// 表单验证规则
const rules = {
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 获取菜单类型样式类
const getTypeClass = (type) => {
  const classMap = {
    'M': 'type-dir',
    'C': 'type-menu',
    'F': 'type-button'
  }
  return classMap[type] || ''
}

// 获取菜单总数
const getTotalMenus = () => {
  const countNodes = (nodes) => {
    let count = 0
    nodes.forEach(node => {
      count += 1
      if (node.children) {
        count += countNodes(node.children)
      }
    })
    return count
  }
  return countNodes(tableData.value)
}

// 获取权限数量
const getPermissionCount = () => {
  return permStats.value.allPerms.length
}

// 快速添加处理
const handleQuickAdd = (command) => {
  if (command === 'crud') {
    // 选择一个菜单后批量添加 CRUD
    ElMessageBox.prompt('请输入目标菜单ID', '批量添加 CRUD', {
      confirmButtonText: '下一步',
      cancelButtonText: '取消',
      inputPlaceholder: '请先在表格中选择菜单，然后点击闪电图标'
    }).then(() => {
      ElMessage.info('请在菜单行点击闪电图标来批量添加 CRUD 按钮')
    }).catch(() => {})
  } else if (command === 'module') {
    moduleDialogVisible.value = true
    Object.assign(moduleForm, {
      parentId: 0,
      moduleName: '',
      prefix: '',
      path: '',
      component: '',
      actions: ['view', 'create', 'update', 'delete']
    })
  }
}

// 批量添加 CRUD 按钮
const handleBatchAddCrud = (row) => {
  crudDialogVisible.value = true
  // 从菜单的权限标识中提取前缀
  const prefix = row.perms ? row.perms.split(':')[0] : ''
  Object.assign(crudForm, {
    parentId: row.id,
    parentMenuName: row.menuName,
    prefix: prefix,
    actions: ['view', 'create', 'update', 'delete']
  })
}

// 提交批量 CRUD
const submitBatchCrud = async () => {
  if (!crudForm.prefix) {
    ElMessage.warning('请输入权限前缀')
    return
  }

  const actionLabels = {
    view: '查询',
    create: '新增',
    update: '修改',
    delete: '删除',
    export: '导出',
    import: '导入'
  }

  crudLoading.value = true
  try {
    let order = 1
    for (const action of crudForm.actions) {
      const menuData = {
        parentId: crudForm.parentId,
        menuType: 'F',
        menuName: `${crudForm.parentMenuName}${actionLabels[action]}`,
        perms: `${crudForm.prefix}:${action}`,
        orderNum: order++,
        visible: 1,
        status: 1
      }
      await menuApi.createMenu(menuData)
    }

    ElMessage.success(`成功创建 ${crudForm.actions.length} 个按钮权限`)
    crudDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('批量创建失败:', error)
    ElMessage.error('批量创建失败')
  } finally {
    crudLoading.value = false
  }
}

// 提交功能模块
const submitModule = async () => {
  if (!moduleForm.moduleName || !moduleForm.prefix) {
    ElMessage.warning('请填写模块名称和权限前缀')
    return
  }

  const actionLabels = {
    view: '查询',
    create: '新增',
    update: '修改',
    delete: '删除'
  }

  moduleLoading.value = true
  try {
    // 1. 创建菜单
    const menuData = {
      parentId: moduleForm.parentId,
      menuType: 'C',
      menuName: `${moduleForm.moduleName}管理`,
      path: moduleForm.path.startsWith('/') ? moduleForm.path : `/${moduleForm.path}`,
      component: moduleForm.component,
      perms: `${moduleForm.prefix}:view`,
      icon: 'Document',
      orderNum: 0,
      visible: 1,
      status: 1
    }
    const response = await menuApi.createMenu(menuData)
    const newMenuId = response.data?.id || response.data

    // 2. 创建按钮权限
    if (newMenuId) {
      let order = 1
      for (const action of moduleForm.actions) {
        const buttonData = {
          parentId: newMenuId,
          menuType: 'F',
          menuName: `${moduleForm.moduleName}${actionLabels[action]}`,
          perms: `${moduleForm.prefix}:${action}`,
          orderNum: order++,
          visible: 1,
          status: 1
        }
        await menuApi.createMenu(buttonData)
      }
    }

    ElMessage.success(`成功创建 ${moduleForm.moduleName} 模块及 ${moduleForm.actions.length} 个按钮权限`)
    moduleDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('创建模块失败:', error)
    ElMessage.error('创建模块失败')
  } finally {
    moduleLoading.value = false
  }
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await menuApi.getMenuTree()
    const addStatusLoading = (nodes) => {
      return nodes.map(node => ({
        ...node,
        statusLoading: false,
        children: node.children ? addStatusLoading(node.children) : undefined
      }))
    }
    tableData.value = addStatusLoading(response.data || [])
    menuTreeOptions.value = buildTreeSelectOptions(response.data || [])
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载菜单数据失败')
  } finally {
    loading.value = false
  }
}

const buildTreeSelectOptions = (data) => {
  return [
    { id: 0, menuName: '根菜单', children: data }
  ]
}

const getTypeLabel = (type) => {
  const typeMap = {
    'M': '目录',
    'C': '菜单',
    'F': '按钮'
  }
  return typeMap[type] || type
}

const getTypeTagType = (type) => {
  const typeMap = {
    'M': 'primary',
    'C': 'success',
    'F': 'warning'
  }
  return typeMap[type] || 'info'
}

const handleAdd = (row = null) => {
  dialogTitle.value = row ? '新增子菜单' : '新增菜单'
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    parentId: row ? row.id : 0,
    menuType: 'C',
    menuName: '',
    path: '',
    component: '',
    perms: '',
    icon: '',
    orderNum: 0,
    visible: 1,
    status: 1
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId,
    menuType: row.menuType,
    menuName: row.menuName,
    path: row.path,
    component: row.component,
    perms: row.perms,
    icon: row.icon,
    orderNum: row.orderNum,
    visible: row.visible,
    status: row.status
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await menuApi.updateMenu(form.id, form)
      ElMessage.success('更新菜单成功')
    } else {
      await menuApi.createMenu(form)
      ElMessage.success('创建菜单成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    const hasChildren = row.children && row.children.length > 0
    const safeMenuName = escapeHtml(row.menuName)
    await ElMessageBox.confirm(
      `<div style="text-align: center;">
        <p style="font-size: 16px; margin-bottom: 10px;">确定要删除菜单 <strong>${safeMenuName}</strong> 吗？</p>
        ${hasChildren ? '<p style="color: #F56C6C; font-size: 13px;">⚠️ 该菜单存在子菜单，将一并删除</p>' : ''}
      </div>`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    await menuApi.deleteMenu(row.id)
    ElMessage.success('删除菜单成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  try {
    await menuApi.updateMenu(row.id, { status: row.status })
    ElMessage.success(`${row.status === 1 ? '启用' : '停用'}菜单成功`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    console.error('更新状态失败:', error)
    ElMessage.error('更新状态失败')
  } finally {
    row.statusLoading = false
  }
}

const handleExpandAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      const queue = [...tableData.value]
      while (queue.length > 0) {
        const row = queue.shift()
        tableRef.value.toggleRowExpansion(row, true)
        if (row.children && row.children.length > 0) {
          queue.push(...row.children)
        }
      }
    }
  })
}

const handleCollapseAll = () => {
  nextTick(() => {
    if (tableRef.value) {
      const queue = [...tableData.value]
      while (queue.length > 0) {
        const row = queue.shift()
        tableRef.value.toggleRowExpansion(row, false)
        if (row.children && row.children.length > 0) {
          queue.push(...row.children)
        }
      }
    }
  })
}

const handleRefresh = () => {
  loadData()
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 生命周期
onMounted(() => {
  loadData()
  loadConfigOptions()  // 从后端加载组件和图标选项
})
</script>

<style scoped>
.menus-page {
  padding: 0;
}

/* 入场动画 */
.fade-slide-down-enter-active {
  animation: fadeSlideDown 0.5s ease-out;
}

.fade-slide-up-enter-active {
  animation: fadeSlideUp 0.5s ease-out;
  animation-delay: 0.1s;
  animation-fill-mode: both;
}

@keyframes fadeSlideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeSlideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.action-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.action-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.title-icon {
  font-size: 24px;
  margin-right: 8px;
  color: #409EFF;
}

.action-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.action-btn {
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
}

.primary-gradient {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
  border: none;
}

.table-card {
  min-height: 400px;
  border-radius: 12px;
}

/* 菜单名称单元格 */
.menu-name-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.menu-icon.type-dir {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
}

.menu-icon.type-menu {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.menu-icon.type-button {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
}

.menu-name {
  font-weight: 500;
}

.path-text,
.component-text {
  font-family: monospace;
  font-size: 13px;
  color: #606266;
}

.no-path,
.no-component,
.no-perms {
  color: #c0c4cc;
}

/* 对话框样式 */
.menu-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.type-radio-group {
  display: flex;
  gap: 16px;
}

.type-radio-group :deep(.el-radio.is-bordered) {
  padding: 10px 20px;
  border-radius: 8px;
  margin-right: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 淡入动画 */
.fade-enter-active {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 权限预览 */
.perm-preview {
  margin-top: 8px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  color: #606266;
}

/* 权限统计样式 */
.perm-stats {
  padding: 10px;
}

.stats-overview {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
  border-radius: 12px;
  color: white;
}

.stat-card.stat-total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.stat-dir {
  background: linear-gradient(135deg, #409EFF 0%, #53a8ff 100%);
}

.stat-card.stat-menu {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.stat-card.stat-btn {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 4px;
}

.perm-list-section {
  margin-top: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.perm-tags {
  max-height: 200px;
  overflow-y: auto;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 8px;
}

/* 操作按钮样式 */
.operation-buttons {
  display: flex;
  gap: 6px;
  align-items: center;
}
</style>
