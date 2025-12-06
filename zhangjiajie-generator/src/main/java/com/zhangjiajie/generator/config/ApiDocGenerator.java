package com.zhangjiajie.generator.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * API文档生成器
 * 生成 Markdown 格式 API 文档、Postman Collection、接口调用示例
 *
 * 使用方法：
 *   ApiDocGenerator.generate("User", "用户", "用户管理");
 *   ApiDocGenerator.generateMarkdown("User", "用户", "用户管理");
 *   ApiDocGenerator.generatePostmanCollection("User", "用户", "用户管理");
 *
 * 生成内容：
 * 1. Markdown 格式 API 文档
 * 2. Postman Collection JSON
 * 3. cURL 调用示例
 * 4. JavaScript/TypeScript 调用示例
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class ApiDocGenerator {

    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        // 示例：生成User模块的API文档
        generate("User", "用户", "用户管理");
    }

    /**
     * 生成完整API文档
     */
    public static void generate(String entityName, String entityCnName, String moduleName) {
        try {
            generateMarkdownDoc(entityName, entityCnName, moduleName);
            generatePostmanCollection(entityName, entityCnName, moduleName);
            generateCurlExamples(entityName, entityCnName, moduleName);
            generateTypeScriptClient(entityName, entityCnName, moduleName);

            printSuccess("API文档生成", entityName,
                    entityName + "_API.md",
                    entityName + "_postman.json",
                    entityName + "_curl.sh",
                    entityName + "Api.ts");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成Markdown文档
     */
    public static void generateMarkdown(String entityName, String entityCnName, String moduleName) {
        try {
            generateMarkdownDoc(entityName, entityCnName, moduleName);
            printSuccess("Markdown文档生成", entityName, entityName + "_API.md");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成Postman Collection
     */
    public static void generatePostman(String entityName, String entityCnName, String moduleName) {
        try {
            generatePostmanCollection(entityName, entityCnName, moduleName);
            printSuccess("Postman Collection生成", entityName, entityName + "_postman.json");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 生成Markdown格式API文档
     */
    private static void generateMarkdownDoc(String entityName, String entityCnName,
                                            String moduleName) throws IOException {
        String urlPath = toKebabCase(entityName);
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                # %s API 文档

                > 生成时间: %s
                > 作者: %s
                > 基础路径: `/api/%s`

                ## 概述

                %s模块提供完整的 CRUD 操作接口，包括分页查询、详情查询、创建、更新、删除等功能。

                ## 认证

                所有接口需要在请求头中携带 JWT Token：

                ```
                Authorization: Bearer <your-jwt-token>
                ```

                ## 权限说明

                | 接口 | 所需权限 |
                |------|----------|
                | 分页查询 | `%s:view` |
                | 详情查询 | `%s:view` |
                | 查询列表 | `%s:view` |
                | 创建 | `%s:create` |
                | 更新 | `%s:update` |
                | 删除 | `%s:delete` |
                | 批量删除 | `%s:delete` |

                ---

                ## 1. 分页查询%s列表

                ### 请求

                ```http
                GET /api/%s/page?current=1&size=10
                ```

                ### 请求参数

                | 参数名 | 类型 | 必填 | 说明 |
                |--------|------|------|------|
                | current | Integer | 否 | 页码，默认1 |
                | size | Integer | 否 | 每页数量，默认10，最大100 |

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "操作成功",
                    "data": {
                        "records": [
                            {
                                "id": 1,
                                "name": "示例%s",
                                "status": 1,
                                "createTime": "2025-01-01 12:00:00"
                            }
                        ],
                        "total": 100,
                        "size": 10,
                        "current": 1,
                        "pages": 10
                    }
                }
                ```

                ---

                ## 2. 根据ID查询%s详情

                ### 请求

                ```http
                GET /api/%s/{id}
                ```

                ### 路径参数

                | 参数名 | 类型 | 必填 | 说明 |
                |--------|------|------|------|
                | id | Long | 是 | %sID |

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "操作成功",
                    "data": {
                        "id": 1,
                        "name": "示例%s",
                        "status": 1,
                        "createTime": "2025-01-01 12:00:00",
                        "updateTime": "2025-01-01 12:00:00"
                    }
                }
                ```

                ---

                ## 3. 查询所有%s

                ### 请求

                ```http
                GET /api/%s/list
                ```

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "操作成功",
                    "data": [
                        {
                            "id": 1,
                            "name": "示例%s1"
                        },
                        {
                            "id": 2,
                            "name": "示例%s2"
                        }
                    ]
                }
                ```

                ---

                ## 4. 创建%s

                ### 请求

                ```http
                POST /api/%s
                Content-Type: application/json
                ```

                ### 请求体

                ```json
                {
                    "name": "%s名称",
                    "description": "描述信息",
                    "status": 1
                }
                ```

                ### 请求参数

                | 参数名 | 类型 | 必填 | 说明 |
                |--------|------|------|------|
                | name | String | 是 | 名称 |
                | description | String | 否 | 描述 |
                | status | Integer | 否 | 状态(0-禁用,1-启用)，默认1 |

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "创建成功"
                }
                ```

                ---

                ## 5. 更新%s

                ### 请求

                ```http
                PUT /api/%s/{id}
                Content-Type: application/json
                ```

                ### 路径参数

                | 参数名 | 类型 | 必填 | 说明 |
                |--------|------|------|------|
                | id | Long | 是 | %sID |

                ### 请求体

                ```json
                {
                    "name": "更新后的名称",
                    "status": 1
                }
                ```

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "更新成功"
                }
                ```

                ---

                ## 6. 删除%s

                ### 请求

                ```http
                DELETE /api/%s/{id}
                ```

                ### 路径参数

                | 参数名 | 类型 | 必填 | 说明 |
                |--------|------|------|------|
                | id | Long | 是 | %sID |

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "删除成功"
                }
                ```

                ---

                ## 7. 批量删除%s

                ### 请求

                ```http
                DELETE /api/%s/batch
                Content-Type: application/json
                ```

                ### 请求体

                ```json
                [1, 2, 3]
                ```

                ### 响应示例

                ```json
                {
                    "code": 200,
                    "message": "批量删除成功"
                }
                ```

                ---

                ## 错误码说明

                | 错误码 | 说明 |
                |--------|------|
                | 200 | 成功 |
                | 400 | 请求参数错误 |
                | 401 | 未登录或Token过期 |
                | 403 | 无权限访问 |
                | 404 | 资源不存在 |
                | 500 | 服务器内部错误 |

                ---

                ## 更新日志

                | 版本 | 日期 | 描述 |
                |------|------|------|
                | v1.0.0 | %s | 初始版本 |
                """,
                moduleName, getCurrentDate(), getAuthor(), urlPath,
                moduleName,
                lowerEntityName, lowerEntityName, lowerEntityName,
                lowerEntityName, lowerEntityName, lowerEntityName, lowerEntityName,
                entityCnName, urlPath, entityCnName,
                entityCnName, urlPath, entityCnName, entityCnName,
                entityCnName, urlPath, entityCnName, entityCnName,
                entityCnName, urlPath, entityCnName,
                entityCnName, urlPath, entityCnName,
                entityCnName, urlPath, entityCnName,
                entityCnName, urlPath,
                getCurrentDate()
        );

        writeToDocFile(entityName + "_API.md", content);
    }

    /**
     * 生成Postman Collection
     */
    private static void generatePostmanCollection(String entityName, String entityCnName,
                                                   String moduleName) throws IOException {
        String urlPath = toKebabCase(entityName);
        String collectionId = java.util.UUID.randomUUID().toString();

        String content = String.format("""
                {
                    "info": {
                        "_postman_id": "%s",
                        "name": "%s API",
                        "description": "%s模块API接口集合\\n\\n生成时间: %s",
                        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
                    },
                    "auth": {
                        "type": "bearer",
                        "bearer": [
                            {
                                "key": "token",
                                "value": "{{jwt_token}}",
                                "type": "string"
                            }
                        ]
                    },
                    "variable": [
                        {
                            "key": "base_url",
                            "value": "%s"
                        },
                        {
                            "key": "jwt_token",
                            "value": "your-jwt-token-here"
                        }
                    ],
                    "item": [
                        {
                            "name": "1. 分页查询%s列表",
                            "request": {
                                "method": "GET",
                                "header": [],
                                "url": {
                                    "raw": "{{base_url}}/api/%s/page?current=1&size=10",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "page"],
                                    "query": [
                                        {"key": "current", "value": "1"},
                                        {"key": "size", "value": "10"}
                                    ]
                                }
                            }
                        },
                        {
                            "name": "2. 根据ID查询%s",
                            "request": {
                                "method": "GET",
                                "header": [],
                                "url": {
                                    "raw": "{{base_url}}/api/%s/1",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "1"]
                                }
                            }
                        },
                        {
                            "name": "3. 查询所有%s",
                            "request": {
                                "method": "GET",
                                "header": [],
                                "url": {
                                    "raw": "{{base_url}}/api/%s/list",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "list"]
                                }
                            }
                        },
                        {
                            "name": "4. 创建%s",
                            "request": {
                                "method": "POST",
                                "header": [
                                    {"key": "Content-Type", "value": "application/json"}
                                ],
                                "body": {
                                    "mode": "raw",
                                    "raw": "{\\n    \\"name\\": \\"%s名称\\",\\n    \\"description\\": \\"描述信息\\",\\n    \\"status\\": 1\\n}"
                                },
                                "url": {
                                    "raw": "{{base_url}}/api/%s",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s"]
                                }
                            }
                        },
                        {
                            "name": "5. 更新%s",
                            "request": {
                                "method": "PUT",
                                "header": [
                                    {"key": "Content-Type", "value": "application/json"}
                                ],
                                "body": {
                                    "mode": "raw",
                                    "raw": "{\\n    \\"name\\": \\"更新后的名称\\",\\n    \\"status\\": 1\\n}"
                                },
                                "url": {
                                    "raw": "{{base_url}}/api/%s/1",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "1"]
                                }
                            }
                        },
                        {
                            "name": "6. 删除%s",
                            "request": {
                                "method": "DELETE",
                                "header": [],
                                "url": {
                                    "raw": "{{base_url}}/api/%s/1",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "1"]
                                }
                            }
                        },
                        {
                            "name": "7. 批量删除%s",
                            "request": {
                                "method": "DELETE",
                                "header": [
                                    {"key": "Content-Type", "value": "application/json"}
                                ],
                                "body": {
                                    "mode": "raw",
                                    "raw": "[1, 2, 3]"
                                },
                                "url": {
                                    "raw": "{{base_url}}/api/%s/batch",
                                    "host": ["{{base_url}}"],
                                    "path": ["api", "%s", "batch"]
                                }
                            }
                        }
                    ]
                }
                """,
                collectionId, moduleName, moduleName, getCurrentDate(), BASE_URL,
                entityCnName, urlPath, urlPath,
                entityCnName, urlPath, urlPath,
                entityCnName, urlPath, urlPath,
                entityCnName, entityCnName, urlPath, urlPath,
                entityCnName, urlPath, urlPath,
                entityCnName, urlPath, urlPath,
                entityCnName, urlPath, urlPath
        );

        writeToDocFile(entityName + "_postman.json", content);
    }

    /**
     * 生成cURL调用示例
     */
    private static void generateCurlExamples(String entityName, String entityCnName,
                                             String moduleName) throws IOException {
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
                #!/bin/bash
                # ==========================================
                # %s API cURL 调用示例
                # 生成时间: %s
                # 作者: %s
                # ==========================================

                # 基础配置
                BASE_URL="%s"
                TOKEN="your-jwt-token-here"

                # 通用请求头
                AUTH_HEADER="Authorization: Bearer $TOKEN"
                CONTENT_TYPE="Content-Type: application/json"

                echo "===== %s API 调用示例 ====="

                # 1. 分页查询%s列表
                echo "\\n--- 1. 分页查询 ---"
                curl -X GET "$BASE_URL/api/%s/page?current=1&size=10" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE"

                # 2. 根据ID查询%s
                echo "\\n--- 2. 详情查询 ---"
                curl -X GET "$BASE_URL/api/%s/1" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE"

                # 3. 查询所有%s
                echo "\\n--- 3. 查询列表 ---"
                curl -X GET "$BASE_URL/api/%s/list" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE"

                # 4. 创建%s
                echo "\\n--- 4. 创建 ---"
                curl -X POST "$BASE_URL/api/%s" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE" \\
                    -d '{
                        "name": "%s名称",
                        "description": "描述信息",
                        "status": 1
                    }'

                # 5. 更新%s
                echo "\\n--- 5. 更新 ---"
                curl -X PUT "$BASE_URL/api/%s/1" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE" \\
                    -d '{
                        "name": "更新后的名称",
                        "status": 1
                    }'

                # 6. 删除%s
                echo "\\n--- 6. 删除 ---"
                curl -X DELETE "$BASE_URL/api/%s/1" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE"

                # 7. 批量删除%s
                echo "\\n--- 7. 批量删除 ---"
                curl -X DELETE "$BASE_URL/api/%s/batch" \\
                    -H "$AUTH_HEADER" \\
                    -H "$CONTENT_TYPE" \\
                    -d '[1, 2, 3]'

                echo "\\n===== 调用完成 ====="
                """,
                moduleName, getCurrentDate(), getAuthor(), BASE_URL,
                moduleName,
                entityCnName, urlPath,
                entityCnName, urlPath,
                entityCnName, urlPath,
                entityCnName, urlPath, entityCnName,
                entityCnName, urlPath,
                entityCnName, urlPath,
                entityCnName, urlPath
        );

        writeToDocFile(entityName + "_curl.sh", content);
    }

    /**
     * 生成TypeScript客户端
     */
    private static void generateTypeScriptClient(String entityName, String entityCnName,
                                                  String moduleName) throws IOException {
        String urlPath = toKebabCase(entityName);
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                /**
                 * %s API 客户端
                 * 生成时间: %s
                 * 作者: %s
                 */

                import axios from 'axios'
                import type { AxiosResponse } from 'axios'

                // ==================== 类型定义 ====================

                /** %s实体 */
                export interface %s {
                    id: number
                    name: string
                    description?: string
                    status: number
                    createTime: string
                    updateTime: string
                }

                /** 创建%s请求 */
                export interface Create%sRequest {
                    name: string
                    description?: string
                    status?: number
                }

                /** 更新%s请求 */
                export interface Update%sRequest {
                    name?: string
                    description?: string
                    status?: number
                }

                /** 分页结果 */
                export interface PageResult<%s> {
                    records: T[]
                    total: number
                    size: number
                    current: number
                    pages: number
                }

                /** API响应 */
                export interface Result<T> {
                    code: number
                    message: string
                    data: T
                }

                // ==================== API 基础配置 ====================

                const BASE_URL = '/api/%s'

                // ==================== API 方法 ====================

                /**
                 * 分页查询%s列表
                 */
                export function get%sPage(
                    current: number = 1,
                    size: number = 10
                ): Promise<AxiosResponse<Result<PageResult<%s>>>> {
                    return axios.get(`${BASE_URL}/page`, {
                        params: { current, size }
                    })
                }

                /**
                 * 根据ID查询%s
                 */
                export function get%sById(id: number): Promise<AxiosResponse<Result<%s>>> {
                    return axios.get(`${BASE_URL}/${id}`)
                }

                /**
                 * 查询所有%s
                 */
                export function get%sList(): Promise<AxiosResponse<Result<%s[]>>> {
                    return axios.get(`${BASE_URL}/list`)
                }

                /**
                 * 创建%s
                 */
                export function create%s(
                    data: Create%sRequest
                ): Promise<AxiosResponse<Result<void>>> {
                    return axios.post(BASE_URL, data)
                }

                /**
                 * 更新%s
                 */
                export function update%s(
                    id: number,
                    data: Update%sRequest
                ): Promise<AxiosResponse<Result<void>>> {
                    return axios.put(`${BASE_URL}/${id}`, data)
                }

                /**
                 * 删除%s
                 */
                export function delete%s(id: number): Promise<AxiosResponse<Result<void>>> {
                    return axios.delete(`${BASE_URL}/${id}`)
                }

                /**
                 * 批量删除%s
                 */
                export function delete%sBatch(ids: number[]): Promise<AxiosResponse<Result<void>>> {
                    return axios.delete(`${BASE_URL}/batch`, { data: ids })
                }

                // ==================== 导出默认对象 ====================

                export default {
                    getPage: get%sPage,
                    getById: get%sById,
                    getList: get%sList,
                    create: create%s,
                    update: update%s,
                    delete: delete%s,
                    deleteBatch: delete%sBatch
                }
                """,
                moduleName, getCurrentDate(), getAuthor(),
                entityCnName, entityName,
                entityCnName, entityName,
                entityCnName, entityName,
                entityName,
                urlPath,
                entityCnName, entityName, entityName,
                entityCnName, entityName, entityName,
                entityCnName, entityName, entityName,
                entityCnName, entityName, entityName,
                entityCnName, entityName, entityName,
                entityCnName, entityName,
                entityCnName, entityName,
                entityName, entityName, entityName, entityName, entityName, entityName, entityName
        );

        writeToDocFile(entityName + "Api.ts", content);
    }

    // ==================== 工具方法 ====================

    /**
     * 写入文档文件
     */
    private static void writeToDocFile(String fileName, String content) throws IOException {
        String docPath = getMainResourcesPath() + "/docs/api";
        writeToFile(docPath, "", fileName, content);
    }
}
