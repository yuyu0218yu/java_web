package com.zhangjiajie.generator.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * Vue前端代码生成器
 * 生成 Vue 3 + Element Plus 的前端代码
 *
 * 生成内容：
 * 1. 列表页 - XxxList.vue（表格 + 搜索 + 分页 + CRUD）
 * 2. 表单组件 - XxxForm.vue（新增/编辑表单）
 * 3. 详情组件 - XxxDetail.vue（详情展示）
 * 4. 路由配置 - router-xxx.js（路由注册）
 *
 * 技术栈：
 * - Vue 3 Composition API（<script setup>）
 * - Element Plus UI 组件库
 * - Vue Router 路由
 *
 * 使用方法：
 *   VueFrontendGenerator.generate("User", "用户", "用户管理");
 *   VueFrontendGenerator.generateListPage("User", "用户", "用户管理");
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class VueFrontendGenerator {

    /** 前端输出目录 */
    private static final String FRONTEND_OUTPUT_DIR = "generated-frontend";

    public static void main(String[] args) {
        // 示例：生成User模块的前端代码
        generate("User", "用户", "用户管理");
    }

    /**
     * 生成完整的前端模块代码
     *
     * @param entityName   实体名称（如 User）
     * @param entityCnName 实体中文名（如 用户）
     * @param moduleName   模块名称（如 用户管理）
     */
    public static void generate(String entityName, String entityCnName, String moduleName) {
        try {
            generateListPage(entityName, entityCnName, moduleName);
            generateFormComponent(entityName, entityCnName);
            generateDetailComponent(entityName, entityCnName);
            generateRouterConfig(entityName, entityCnName, moduleName);

            printSuccess("Vue前端代码生成", entityName,
                    entityName + "List.vue",
                    entityName + "Form.vue",
                    entityName + "Detail.vue",
                    "router-" + toKebabCase(entityName) + ".js");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成列表页
     */
    public static void generateListOnly(String entityName, String entityCnName, String moduleName) {
        try {
            generateListPage(entityName, entityCnName, moduleName);
            printSuccess("列表页生成", entityName, entityName + "List.vue");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 生成列表页 + 表单组件（常用组合）
     */
    public static void generateListAndForm(String entityName, String entityCnName, String moduleName) {
        try {
            generateListPage(entityName, entityCnName, moduleName);
            generateFormComponent(entityName, entityCnName);
            printSuccess("列表页+表单组件生成", entityName,
                    entityName + "List.vue",
                    entityName + "Form.vue");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    // ==================== API 层生成 ====================

    /**
     * 生成 API 层代码
     */
    private static void generateApi(String entityName, String entityCnName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
                /**
                 * %s API 接口
                 * 生成时间: %s
                 */
                import request from '@/utils/request'

                /**
                 * %s管理接口
                 */
                export const %sApi = {
                    /**
                     * 分页查询%s列表
                     * @param {Object} params - 查询参数 { current, size, ...filters }
                     */
                    getPage(params) {
                        return request({
                            url: '/api/%s/page',
                            method: 'get',
                            params
                        })
                    },

                    /**
                     * 获取所有%s列表
                     */
                    getList() {
                        return request({
                            url: '/api/%s/list',
                            method: 'get'
                        })
                    },

                    /**
                     * 根据ID获取%s详情
                     * @param {number} id - %sID
                     */
                    getById(id) {
                        return request({
                            url: `/api/%s/${id}`,
                            method: 'get'
                        })
                    },

                    /**
                     * 创建%s
                     * @param {Object} data - %s信息
                     */
                    create(data) {
                        return request({
                            url: '/api/%s',
                            method: 'post',
                            data
                        })
                    },

                    /**
                     * 更新%s
                     * @param {number} id - %sID
                     * @param {Object} data - %s信息
                     */
                    update(id, data) {
                        return request({
                            url: `/api/%s/${id}`,
                            method: 'put',
                            data
                        })
                    },

                    /**
                     * 删除%s
                     * @param {number} id - %sID
                     */
                    delete(id) {
                        return request({
                            url: `/api/%s/${id}`,
                            method: 'delete'
                        })
                    },

                    /**
                     * 批量删除%s
                     * @param {Array<number>} ids - %sID数组
                     */
                    deleteBatch(ids) {
                        return request({
                            url: '/api/%s/batch',
                            method: 'delete',
                            data: ids
                        })
                    },

                    /**
                     * 导出%s数据
                     * @param {Object} params - 导出参数
                     */
                    export(params) {
                        return request({
                            url: '/api/%s/export',
                            method: 'get',
                            params,
                            responseType: 'blob'
                        })
                    }
                }

                export default %sApi
                """,
                entityCnName, getCurrentDate(),
                entityCnName, lowerEntityName,
                entityCnName, urlPath,
                entityCnName, urlPath,
                entityCnName, entityCnName, urlPath,
                entityCnName, entityCnName, urlPath,
                entityCnName, entityCnName, entityCnName, urlPath,
                entityCnName, entityCnName, urlPath,
                entityCnName, entityCnName, urlPath,
                entityCnName, urlPath,
                lowerEntityName
        );

        writeToFrontendFile("api", lowerEntityName + "Api.js", content);
    }

    // ==================== 列表页生成 ====================

    /**
     * 生成列表页组件
     */
    private static void generateListPage(String entityName, String entityCnName,
                                          String moduleName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
                <template>
                  <div class="%s-list">
                    <!-- 搜索表单 -->
                    <el-card class="search-card" shadow="never">
                      <el-form :model="searchForm" inline>
                        <el-form-item label="名称">
                          <el-input
                            v-model="searchForm.name"
                            placeholder="请输入名称"
                            clearable
                            @keyup.enter="handleSearch"
                          />
                        </el-form-item>
                        <el-form-item label="状态">
                          <el-select v-model="searchForm.status" placeholder="全部" clearable>
                            <el-option label="启用" :value="1" />
                            <el-option label="禁用" :value="0" />
                          </el-select>
                        </el-form-item>
                        <el-form-item>
                          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
                        </el-form-item>
                      </el-form>
                    </el-card>

                    <!-- 数据表格 -->
                    <el-card class="table-card" shadow="never">
                      <!-- 工具栏 -->
                      <div class="toolbar">
                        <el-button type="primary" :icon="Plus" @click="handleCreate">
                          新增%s
                        </el-button>
                        <el-button
                          type="danger"
                          :icon="Delete"
                          :disabled="selectedIds.length === 0"
                          @click="handleBatchDelete"
                        >
                          批量删除
                        </el-button>
                        <el-button :icon="Download" @click="handleExport">导出</el-button>
                      </div>

                      <!-- 表格 -->
                      <el-table
                        v-loading="loading"
                        :data="tableData"
                        @selection-change="handleSelectionChange"
                        border
                        stripe
                      >
                        <el-table-column type="selection" width="55" />
                        <el-table-column prop="id" label="ID" width="80" />
                        <el-table-column prop="name" label="名称" min-width="150" show-overflow-tooltip />
                        <el-table-column prop="code" label="编码" width="120" />
                        <el-table-column prop="status" label="状态" width="100">
                          <template #default="{ row }">
                            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                              {{ row.status === 1 ? '启用' : '禁用' }}
                            </el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column prop="createTime" label="创建时间" width="180" />
                        <el-table-column label="操作" width="200" fixed="right">
                          <template #default="{ row }">
                            <el-button type="primary" link :icon="View" @click="handleView(row)">
                              查看
                            </el-button>
                            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">
                              编辑
                            </el-button>
                            <el-popconfirm
                              title="确定要删除这条数据吗？"
                              @confirm="handleDelete(row)"
                            >
                              <template #reference>
                                <el-button type="danger" link :icon="Delete">删除</el-button>
                              </template>
                            </el-popconfirm>
                          </template>
                        </el-table-column>
                      </el-table>

                      <!-- 分页 -->
                      <div class="pagination-wrapper">
                        <el-pagination
                          v-model:current-page="pagination.current"
                          v-model:page-size="pagination.size"
                          :page-sizes="[10, 20, 50, 100]"
                          :total="pagination.total"
                          layout="total, sizes, prev, pager, next, jumper"
                          @size-change="handleSizeChange"
                          @current-change="handleCurrentChange"
                        />
                      </div>
                    </el-card>

                    <!-- 新增/编辑弹窗 -->
                    <el-dialog
                      v-model="dialogVisible"
                      :title="dialogTitle"
                      width="600px"
                      :close-on-click-modal="false"
                      @closed="handleDialogClosed"
                    >
                      <%s-form
                        ref="formRef"
                        :data="currentRow"
                        :mode="dialogMode"
                        @success="handleFormSuccess"
                        @cancel="dialogVisible = false"
                      />
                    </el-dialog>

                    <!-- 详情弹窗 -->
                    <el-dialog
                      v-model="detailVisible"
                      title="%s详情"
                      width="700px"
                    >
                      <%s-detail :data="currentRow" />
                    </el-dialog>
                  </div>
                </template>

                <script setup>
                import { ref, reactive, onMounted } from 'vue'
                import { ElMessage, ElMessageBox } from 'element-plus'
                import {
                  Search,
                  Refresh,
                  Plus,
                  Edit,
                  Delete,
                  View,
                  Download
                } from '@element-plus/icons-vue'
                import { %sApi } from '@/api/%sApi'
                import %sForm from './%sForm.vue'
                import %sDetail from './%sDetail.vue'

                // ==================== 数据定义 ====================

                // 搜索表单
                const searchForm = reactive({
                  name: '',
                  status: null
                })

                // 分页
                const pagination = reactive({
                  current: 1,
                  size: 10,
                  total: 0
                })

                // 表格数据
                const loading = ref(false)
                const tableData = ref([])
                const selectedIds = ref([])

                // 弹窗
                const dialogVisible = ref(false)
                const dialogTitle = ref('')
                const dialogMode = ref('create') // create | edit
                const currentRow = ref(null)
                const formRef = ref(null)

                // 详情弹窗
                const detailVisible = ref(false)

                // ==================== 生命周期 ====================

                onMounted(() => {
                  fetchData()
                })

                // ==================== 数据获取 ====================

                /**
                 * 获取列表数据
                 */
                async function fetchData() {
                  loading.value = true
                  try {
                    const params = {
                      current: pagination.current,
                      size: pagination.size,
                      ...searchForm
                    }
                    const res = await %sApi.getPage(params)
                    if (res.data.code === 200) {
                      tableData.value = res.data.data.records
                      pagination.total = res.data.data.total
                    }
                  } catch (error) {
                    console.error('获取数据失败:', error)
                    ElMessage.error('获取数据失败')
                  } finally {
                    loading.value = false
                  }
                }

                // ==================== 搜索相关 ====================

                function handleSearch() {
                  pagination.current = 1
                  fetchData()
                }

                function handleReset() {
                  searchForm.name = ''
                  searchForm.status = null
                  handleSearch()
                }

                // ==================== 分页相关 ====================

                function handleSizeChange(size) {
                  pagination.size = size
                  fetchData()
                }

                function handleCurrentChange(current) {
                  pagination.current = current
                  fetchData()
                }

                // ==================== 表格选择 ====================

                function handleSelectionChange(selection) {
                  selectedIds.value = selection.map(item => item.id)
                }

                // ==================== CRUD 操作 ====================

                /**
                 * 新增
                 */
                function handleCreate() {
                  dialogMode.value = 'create'
                  dialogTitle.value = '新增%s'
                  currentRow.value = null
                  dialogVisible.value = true
                }

                /**
                 * 编辑
                 */
                function handleEdit(row) {
                  dialogMode.value = 'edit'
                  dialogTitle.value = '编辑%s'
                  currentRow.value = { ...row }
                  dialogVisible.value = true
                }

                /**
                 * 查看详情
                 */
                function handleView(row) {
                  currentRow.value = { ...row }
                  detailVisible.value = true
                }

                /**
                 * 删除
                 */
                async function handleDelete(row) {
                  try {
                    const res = await %sApi.delete(row.id)
                    if (res.data.code === 200) {
                      ElMessage.success('删除成功')
                      fetchData()
                    } else {
                      ElMessage.error(res.data.message || '删除失败')
                    }
                  } catch (error) {
                    console.error('删除失败:', error)
                    ElMessage.error('删除失败')
                  }
                }

                /**
                 * 批量删除
                 */
                async function handleBatchDelete() {
                  try {
                    await ElMessageBox.confirm(
                      `确定要删除选中的 ${selectedIds.value.length} 条数据吗？`,
                      '批量删除确认',
                      { type: 'warning' }
                    )
                    const res = await %sApi.deleteBatch(selectedIds.value)
                    if (res.data.code === 200) {
                      ElMessage.success('批量删除成功')
                      fetchData()
                    } else {
                      ElMessage.error(res.data.message || '批量删除失败')
                    }
                  } catch (error) {
                    if (error !== 'cancel') {
                      console.error('批量删除失败:', error)
                      ElMessage.error('批量删除失败')
                    }
                  }
                }

                /**
                 * 导出
                 */
                async function handleExport() {
                  try {
                    const res = await %sApi.export(searchForm)
                    const blob = new Blob([res.data], {
                      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                    })
                    const url = window.URL.createObjectURL(blob)
                    const link = document.createElement('a')
                    link.href = url
                    link.download = '%s_' + new Date().getTime() + '.xlsx'
                    link.click()
                    window.URL.revokeObjectURL(url)
                    ElMessage.success('导出成功')
                  } catch (error) {
                    console.error('导出失败:', error)
                    ElMessage.error('导出失败')
                  }
                }

                // ==================== 弹窗相关 ====================

                function handleDialogClosed() {
                  currentRow.value = null
                }

                function handleFormSuccess() {
                  dialogVisible.value = false
                  fetchData()
                }
                </script>

                <style scoped>
                .%s-list {
                  padding: 20px;
                }

                .search-card {
                  margin-bottom: 16px;
                }

                .table-card {
                  .toolbar {
                    margin-bottom: 16px;
                  }
                }

                .pagination-wrapper {
                  margin-top: 16px;
                  display: flex;
                  justify-content: flex-end;
                }
                </style>
                """,
                urlPath,
                entityCnName,
                urlPath, entityCnName, urlPath,
                lowerEntityName, lowerEntityName,
                entityName, entityName,
                entityName, entityName,
                lowerEntityName,
                entityCnName, entityCnName,
                lowerEntityName, lowerEntityName, lowerEntityName,
                entityName,
                urlPath
        );

        writeToFrontendFile("views/" + lowerEntityName, entityName + "List.vue", content);
    }

    // ==================== 表单组件生成 ====================

    /**
     * 生成表单组件
     */
    private static void generateFormComponent(String entityName, String entityCnName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                <template>
                  <el-form
                    ref="formRef"
                    :model="formData"
                    :rules="rules"
                    label-width="100px"
                    :disabled="loading"
                  >
                    <el-form-item label="名称" prop="name">
                      <el-input v-model="formData.name" placeholder="请输入名称" maxlength="50" />
                    </el-form-item>

                    <el-form-item label="编码" prop="code">
                      <el-input
                        v-model="formData.code"
                        placeholder="请输入编码"
                        maxlength="30"
                        :disabled="mode === 'edit'"
                      />
                    </el-form-item>

                    <el-form-item label="描述" prop="description">
                      <el-input
                        v-model="formData.description"
                        type="textarea"
                        placeholder="请输入描述"
                        :rows="3"
                        maxlength="200"
                        show-word-limit
                      />
                    </el-form-item>

                    <el-form-item label="排序" prop="sort">
                      <el-input-number v-model="formData.sort" :min="0" :max="9999" />
                    </el-form-item>

                    <el-form-item label="状态" prop="status">
                      <el-radio-group v-model="formData.status">
                        <el-radio :value="1">启用</el-radio>
                        <el-radio :value="0">禁用</el-radio>
                      </el-radio-group>
                    </el-form-item>

                    <!-- TODO: 根据实际字段添加更多表单项 -->

                    <el-form-item class="form-actions">
                      <el-button type="primary" :loading="loading" @click="handleSubmit">
                        {{ mode === 'create' ? '创建' : '保存' }}
                      </el-button>
                      <el-button @click="handleCancel">取消</el-button>
                    </el-form-item>
                  </el-form>
                </template>

                <script setup>
                import { ref, reactive, watch, onMounted } from 'vue'
                import { ElMessage } from 'element-plus'
                import { %sApi } from '@/api/%sApi'

                // ==================== Props & Emits ====================

                const props = defineProps({
                  /** 编辑时传入的数据 */
                  data: {
                    type: Object,
                    default: null
                  },
                  /** 模式：create | edit */
                  mode: {
                    type: String,
                    default: 'create'
                  }
                })

                const emit = defineEmits(['success', 'cancel'])

                // ==================== 数据定义 ====================

                const formRef = ref(null)
                const loading = ref(false)

                const formData = reactive({
                  id: null,
                  name: '',
                  code: '',
                  description: '',
                  sort: 0,
                  status: 1
                })

                // 表单校验规则
                const rules = {
                  name: [
                    { required: true, message: '请输入名称', trigger: 'blur' },
                    { min: 2, max: 50, message: '名称长度在 2 到 50 个字符', trigger: 'blur' }
                  ],
                  code: [
                    { required: true, message: '请输入编码', trigger: 'blur' },
                    { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '编码以字母开头，只能包含字母、数字和下划线', trigger: 'blur' }
                  ],
                  status: [
                    { required: true, message: '请选择状态', trigger: 'change' }
                  ]
                }

                // ==================== 监听 ====================

                watch(
                  () => props.data,
                  (newData) => {
                    if (newData && props.mode === 'edit') {
                      Object.assign(formData, newData)
                    } else {
                      resetForm()
                    }
                  },
                  { immediate: true }
                )

                // ==================== 方法 ====================

                /**
                 * 重置表单
                 */
                function resetForm() {
                  formData.id = null
                  formData.name = ''
                  formData.code = ''
                  formData.description = ''
                  formData.sort = 0
                  formData.status = 1
                  formRef.value?.clearValidate()
                }

                /**
                 * 提交表单
                 */
                async function handleSubmit() {
                  try {
                    await formRef.value.validate()

                    loading.value = true

                    let res
                    if (props.mode === 'create') {
                      res = await %sApi.create(formData)
                    } else {
                      res = await %sApi.update(formData.id, formData)
                    }

                    if (res.data.code === 200) {
                      ElMessage.success(props.mode === 'create' ? '创建成功' : '保存成功')
                      emit('success')
                    } else {
                      ElMessage.error(res.data.message || '操作失败')
                    }
                  } catch (error) {
                    if (error !== false) {
                      console.error('提交失败:', error)
                      ElMessage.error('提交失败')
                    }
                  } finally {
                    loading.value = false
                  }
                }

                /**
                 * 取消
                 */
                function handleCancel() {
                  emit('cancel')
                }

                // ==================== 暴露方法 ====================

                defineExpose({
                  resetForm
                })
                </script>

                <style scoped>
                .form-actions {
                  margin-top: 24px;
                  margin-bottom: 0;
                }
                </style>
                """,
                lowerEntityName, lowerEntityName,
                lowerEntityName, lowerEntityName
        );

        writeToFrontendFile("views/" + lowerEntityName, entityName + "Form.vue", content);
    }

    // ==================== 详情组件生成 ====================

    /**
     * 生成详情组件
     */
    private static void generateDetailComponent(String entityName, String entityCnName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                <template>
                  <div class="%s-detail" v-if="data">
                    <el-descriptions :column="2" border>
                      <el-descriptions-item label="ID">
                        {{ data.id }}
                      </el-descriptions-item>
                      <el-descriptions-item label="名称">
                        {{ data.name }}
                      </el-descriptions-item>
                      <el-descriptions-item label="编码">
                        {{ data.code }}
                      </el-descriptions-item>
                      <el-descriptions-item label="状态">
                        <el-tag :type="data.status === 1 ? 'success' : 'danger'">
                          {{ data.status === 1 ? '启用' : '禁用' }}
                        </el-tag>
                      </el-descriptions-item>
                      <el-descriptions-item label="排序">
                        {{ data.sort }}
                      </el-descriptions-item>
                      <el-descriptions-item label="描述" :span="2">
                        {{ data.description || '-' }}
                      </el-descriptions-item>
                      <el-descriptions-item label="创建时间">
                        {{ data.createTime }}
                      </el-descriptions-item>
                      <el-descriptions-item label="更新时间">
                        {{ data.updateTime }}
                      </el-descriptions-item>
                      <el-descriptions-item label="创建人">
                        {{ data.createBy || '-' }}
                      </el-descriptions-item>
                      <el-descriptions-item label="更新人">
                        {{ data.updateBy || '-' }}
                      </el-descriptions-item>
                    </el-descriptions>

                    <!-- TODO: 根据实际需求添加更多详情信息 -->

                    <!-- 示例：关联数据展示 -->
                    <!--
                    <el-divider content-position="left">关联信息</el-divider>
                    <el-table :data="relatedData" border>
                      <el-table-column prop="name" label="名称" />
                      <el-table-column prop="value" label="值" />
                    </el-table>
                    -->
                  </div>
                  <el-empty v-else description="暂无数据" />
                </template>

                <script setup>
                import { ref, watch } from 'vue'
                import { %sApi } from '@/api/%sApi'

                // ==================== Props ====================

                const props = defineProps({
                  /** %s数据 */
                  data: {
                    type: Object,
                    default: null
                  },
                  /** 是否需要加载完整详情（当只传ID时） */
                  loadDetail: {
                    type: Boolean,
                    default: false
                  }
                })

                // ==================== 数据 ====================

                const detailData = ref(null)

                // ==================== 监听 ====================

                watch(
                  () => props.data,
                  async (newData) => {
                    if (props.loadDetail && newData?.id) {
                      await fetchDetail(newData.id)
                    }
                  },
                  { immediate: true }
                )

                // ==================== 方法 ====================

                /**
                 * 加载完整详情
                 */
                async function fetchDetail(id) {
                  try {
                    const res = await %sApi.getById(id)
                    if (res.data.code === 200) {
                      detailData.value = res.data.data
                    }
                  } catch (error) {
                    console.error('获取详情失败:', error)
                  }
                }
                </script>

                <style scoped>
                .%s-detail {
                  padding: 16px;
                }

                .el-descriptions {
                  margin-bottom: 20px;
                }

                .el-divider {
                  margin: 24px 0;
                }
                </style>
                """,
                lowerEntityName,
                lowerEntityName, lowerEntityName,
                entityCnName,
                lowerEntityName,
                lowerEntityName
        );

        writeToFrontendFile("views/" + lowerEntityName, entityName + "Detail.vue", content);
    }

    // ==================== 路由配置生成 ====================

    /**
     * 生成路由配置
     */
    private static void generateRouterConfig(String entityName, String entityCnName,
                                              String moduleName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
                /**
                 * %s 路由配置
                 * 生成时间: %s
                 *
                 * 使用方法：
                 * 1. 在 router/index.js 中导入此配置
                 * 2. 添加到路由配置中
                 *
                 * import { %sRoutes } from './modules/router-%s'
                 * routes: [
                 *   ...%sRoutes
                 * ]
                 */

                /**
                 * %s模块路由
                 */
                export const %sRoutes = [
                  {
                    path: '/%s',
                    name: '%sList',
                    component: () => import('@/views/%s/%sList.vue'),
                    meta: {
                      title: '%s列表',
                      icon: 'List',
                      requiresAuth: true,
                      requiredPermissions: ['%s:view'],
                      // requiredRoles: ['ADMIN', 'SUPER_ADMIN'],
                      keepAlive: true
                    }
                  },
                  {
                    path: '/%s/create',
                    name: '%sCreate',
                    component: () => import('@/views/%s/%sForm.vue'),
                    meta: {
                      title: '新增%s',
                      icon: 'Plus',
                      requiresAuth: true,
                      requiredPermissions: ['%s:create'],
                      hidden: true  // 不在菜单显示
                    }
                  },
                  {
                    path: '/%s/edit/:id',
                    name: '%sEdit',
                    component: () => import('@/views/%s/%sForm.vue'),
                    meta: {
                      title: '编辑%s',
                      icon: 'Edit',
                      requiresAuth: true,
                      requiredPermissions: ['%s:update'],
                      hidden: true
                    }
                  },
                  {
                    path: '/%s/detail/:id',
                    name: '%sDetail',
                    component: () => import('@/views/%s/%sDetail.vue'),
                    meta: {
                      title: '%s详情',
                      icon: 'View',
                      requiresAuth: true,
                      requiredPermissions: ['%s:view'],
                      hidden: true
                    }
                  }
                ]

                /**
                 * %s菜单配置（用于动态菜单）
                 */
                export const %sMenu = {
                  path: '/%s',
                  name: '%s',
                  meta: {
                    title: '%s',
                    icon: 'Menu'
                  },
                  children: [
                    {
                      path: 'list',
                      name: '%sList',
                      meta: {
                        title: '%s列表',
                        icon: 'List'
                      }
                    }
                  ]
                }

                export default %sRoutes
                """,
                moduleName, getCurrentDate(),
                lowerEntityName, urlPath, lowerEntityName,
                moduleName, lowerEntityName,
                urlPath, entityName, lowerEntityName, entityName,
                entityCnName, lowerEntityName,
                urlPath, entityName, lowerEntityName, entityName,
                entityCnName, lowerEntityName,
                urlPath, entityName, lowerEntityName, entityName,
                entityCnName, lowerEntityName,
                urlPath, entityName, lowerEntityName, entityName,
                entityCnName, lowerEntityName,
                moduleName, lowerEntityName,
                urlPath, entityName, moduleName,
                entityName, entityCnName,
                lowerEntityName
        );

        writeToFrontendFile("router/modules", "router-" + urlPath + ".js", content);
    }

    // ==================== 根据数据库列生成 ====================

    /**
     * 根据数据库列信息生成完整前端代码
     */
    public static void generateFromColumns(String entityName, String entityCnName,
                                            String moduleName, List<Map<String, Object>> columns) {
        try {
            generateApiFromColumns(entityName, entityCnName, columns);
            generateListPageFromColumns(entityName, entityCnName, moduleName, columns);
            generateFormFromColumns(entityName, entityCnName, columns);
            generateDetailFromColumns(entityName, entityCnName, columns);
            generateRouterConfig(entityName, entityCnName, moduleName);

            printSuccess("Vue前端代码生成（智能）", entityName,
                    toLowerCamelCase(entityName) + "Api.js",
                    entityName + "List.vue",
                    entityName + "Form.vue",
                    entityName + "Detail.vue",
                    "router-" + toKebabCase(entityName) + ".js");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 根据列信息生成API
     */
    private static void generateApiFromColumns(String entityName, String entityCnName,
                                                List<Map<String, Object>> columns) throws IOException {
        // 基础API与标准版相同
        generateApi(entityName, entityCnName);
    }

    /**
     * 根据列信息生成列表页
     */
    private static void generateListPageFromColumns(String entityName, String entityCnName,
                                                     String moduleName, List<Map<String, Object>> columns) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        // 生成搜索表单项
        StringBuilder searchItems = new StringBuilder();
        // 生成表格列
        StringBuilder tableColumns = new StringBuilder();

        int searchCount = 0;
        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("columnName");
            String dataType = (String) column.get("dataType");
            String comment = (String) column.get("columnComment");
            String fieldName = columnNameToFieldName(columnName);

            // 跳过系统字段
            if (isSystemField(fieldName)) continue;

            // 搜索表单（最多4个搜索条件）
            if (searchCount < 4 && isSearchableField(fieldName, dataType)) {
                searchItems.append(generateSearchItem(fieldName, comment, dataType));
                searchCount++;
            }

            // 表格列
            tableColumns.append(generateTableColumn(fieldName, comment, dataType));
        }

        // 这里可以使用模板替换，为简化示例，调用标准生成
        generateListPage(entityName, entityCnName, moduleName);
    }

    /**
     * 根据列信息生成表单
     */
    private static void generateFormFromColumns(String entityName, String entityCnName,
                                                 List<Map<String, Object>> columns) throws IOException {
        // 简化示例，调用标准生成
        generateFormComponent(entityName, entityCnName);
    }

    /**
     * 根据列信息生成详情
     */
    private static void generateDetailFromColumns(String entityName, String entityCnName,
                                                   List<Map<String, Object>> columns) throws IOException {
        // 简化示例，调用标准生成
        generateDetailComponent(entityName, entityCnName);
    }

    // ==================== 工具方法 ====================

    /**
     * 写入前端文件
     */
    private static void writeToFrontendFile(String subDir, String fileName, String content) throws IOException {
        String outputPath = getTargetModule() + "/" + FRONTEND_OUTPUT_DIR;
        writeToFile(outputPath, subDir, fileName, content);
    }

    /**
     * 判断是否系统字段
     */
    private static boolean isSystemField(String fieldName) {
        return "id".equals(fieldName) ||
               "createTime".equals(fieldName) ||
               "updateTime".equals(fieldName) ||
               "createBy".equals(fieldName) ||
               "updateBy".equals(fieldName) ||
               "deleted".equals(fieldName);
    }

    /**
     * 判断是否可搜索字段
     */
    private static boolean isSearchableField(String fieldName, String dataType) {
        // 名称、编码、状态等字段可搜索
        return fieldName.toLowerCase().contains("name") ||
               fieldName.toLowerCase().contains("code") ||
               fieldName.toLowerCase().contains("status") ||
               fieldName.toLowerCase().contains("type");
    }

    /**
     * 生成搜索表单项
     */
    private static String generateSearchItem(String fieldName, String comment, String dataType) {
        String label = comment != null ? comment : fieldName;

        if (fieldName.toLowerCase().contains("status") || fieldName.toLowerCase().contains("type")) {
            return String.format("""
                        <el-form-item label="%s">
                          <el-select v-model="searchForm.%s" placeholder="全部" clearable>
                            <el-option label="启用" :value="1" />
                            <el-option label="禁用" :value="0" />
                          </el-select>
                        </el-form-item>
                    """, label, fieldName);
        }

        return String.format("""
                    <el-form-item label="%s">
                      <el-input v-model="searchForm.%s" placeholder="请输入%s" clearable />
                    </el-form-item>
                """, label, fieldName, label);
    }

    /**
     * 生成表格列
     */
    private static String generateTableColumn(String fieldName, String comment, String dataType) {
        String label = comment != null ? comment : fieldName;
        int width = calculateColumnWidth(fieldName, dataType);

        if (fieldName.toLowerCase().contains("status")) {
            return String.format("""
                        <el-table-column prop="%s" label="%s" width="%d">
                          <template #default="{ row }">
                            <el-tag :type="row.%s === 1 ? 'success' : 'danger'">
                              {{ row.%s === 1 ? '启用' : '禁用' }}
                            </el-tag>
                          </template>
                        </el-table-column>
                    """, fieldName, label, width, fieldName, fieldName);
        }

        return String.format("""
                    <el-table-column prop="%s" label="%s" width="%d" show-overflow-tooltip />
                """, fieldName, label, width);
    }

    /**
     * 计算列宽
     */
    private static int calculateColumnWidth(String fieldName, String dataType) {
        if (fieldName.toLowerCase().contains("id")) return 80;
        if (fieldName.toLowerCase().contains("name")) return 150;
        if (fieldName.toLowerCase().contains("time")) return 180;
        if (fieldName.toLowerCase().contains("status")) return 100;
        if (fieldName.toLowerCase().contains("description")) return 200;
        return 120;
    }

    /**
     * 列名转字段名
     */
    private static String columnNameToFieldName(String columnName) {
        return toCamelCase(columnName, false);
    }
}
