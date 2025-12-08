# 数据权限系统使用说明

## 概述

数据权限系统允许根据用户的角色和部门限制其可以查看和操作的数据范围。系统支持4种数据权限级别：

1. **全部数据** (data_scope = 1): 可以查看所有数据
2. **本部门及下级** (data_scope = 2): 可以查看本部门及所有下级部门的数据
3. **本部门** (data_scope = 3): 只能查看本部门的数据
4. **仅本人** (data_scope = 4): 只能查看自己创建的数据

## 数据库变更

### 1. sys_role 表添加 data_scope 字段

```sql
ALTER TABLE sys_role ADD COLUMN data_scope TINYINT DEFAULT 1 COMMENT '数据范围：1-全部数据 2-本部门及下级 3-本部门 4-仅本人';
```

### 2. 更新现有角色的数据范围

```sql
UPDATE sys_role SET data_scope = 1 WHERE role_code = 'SUPER_ADMIN';  -- 超级管理员：全部数据
UPDATE sys_role SET data_scope = 2 WHERE role_code = 'ADMIN';        -- 管理员：本部门及下级
UPDATE sys_role SET data_scope = 4 WHERE role_code = 'USER';         -- 普通用户：仅本人
```

## 后端使用方法

### 1. 在 Service 方法上添加 @DataScope 注解

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @DataScope(deptIdColumn = "dept_id", userIdColumn = "user_id")
    public Page<User> getUserPage(PageRequest request) {
        Page<User> page = request.toMpPage();
        // DataScopeAspect 会自动添加数据权限过滤条件
        return this.page(page);
    }
}
```

### 2. @DataScope 注解参数说明

- `deptIdColumn`: 部门ID字段名，默认为 "dept_id"
- `userIdColumn`: 用户ID字段名，默认为 "user_id"
- `deptAlias`: 部门表别名，默认为 "d"
- `userAlias`: 用户表别名，默认为 "u"

### 3. 示例：带别名的复杂查询

```java
@DataScope(
    deptIdColumn = "u.dept_id",
    userIdColumn = "u.user_id",
    deptAlias = "d",
    userAlias = "u"
)
public List<User> getUsersWithDetails() {
    // 执行复杂的JOIN查询
    return userMapper.selectUsersWithDetails();
}
```

## 前端使用方法

### 1. 用户管理 - 选择部门

在创建或编辑用户时，可以选择用户所属的部门：

```vue
<el-form-item label="部门" prop="deptId">
  <el-tree-select
    v-model="form.deptId"
    :data="deptOptions"
    placeholder="请选择部门"
    :props="{ label: 'deptName', value: 'id' }"
  />
</el-form-item>
```

### 2. 角色管理 - 选择数据范围

在创建或编辑角色时，可以选择该角色的数据权限范围：

```vue
<el-form-item label="数据范围" prop="dataScope">
  <el-select v-model="form.dataScope" placeholder="请选择数据范围">
    <el-option :value="1" label="全部数据" />
    <el-option :value="2" label="本部门及下级" />
    <el-option :value="3" label="本部门" />
    <el-option :value="4" label="仅本人" />
  </el-select>
</el-form-item>
```

## 工作原理

### 1. DataScopeAspect 切面

DataScopeAspect 拦截所有标注了 `@DataScope` 注解的方法：

1. 获取当前登录用户
2. 查询用户的所有角色
3. 选择权限范围最大的角色（数值最小）
4. 根据角色的 data_scope 构建 SQL WHERE 条件
5. 将条件存储在 ThreadLocal 中供 MyBatis 使用

### 2. SQL 过滤示例

#### 全部数据 (data_scope = 1)
```sql
-- 不添加任何过滤条件
SELECT * FROM sys_user WHERE deleted = 0
```

#### 本部门及下级 (data_scope = 2)
```sql
-- 过滤本部门及所有子部门的数据
SELECT * FROM sys_user 
WHERE deleted = 0 
AND (dept_id IN (1, 2, 3, 4) OR user_id = 5)
```

#### 本部门 (data_scope = 3)
```sql
-- 只过滤本部门的数据
SELECT * FROM sys_user 
WHERE deleted = 0 
AND (dept_id = 1 OR user_id = 5)
```

#### 仅本人 (data_scope = 4)
```sql
-- 只能查看自己创建的数据
SELECT * FROM sys_user 
WHERE deleted = 0 
AND user_id = 5
```

## 测试场景

### 场景 1: 超级管理员
- 角色: SUPER_ADMIN
- 数据范围: 全部数据 (1)
- 期望结果: 可以查看所有用户的数据

### 场景 2: 部门经理
- 角色: DEPT_MANAGER
- 数据范围: 本部门及下级 (2)
- 所属部门: 技术部
- 期望结果: 可以查看技术部及其下级部门（如后端组、前端组）的用户数据

### 场景 3: 普通员工
- 角色: USER
- 数据范围: 仅本人 (4)
- 期望结果: 只能查看自己的数据

## 注意事项

1. **超级管理员豁免**: 拥有 SUPER_ADMIN 角色编码的用户不受数据权限限制
2. **多角色处理**: 用户如果有多个角色，取权限范围最大的（data_scope 数值最小）
3. **无部门用户**: 如果用户没有部门信息，本部门及本部门及下级权限会降级为仅本人权限
4. **线程安全**: DataScopeAspect 使用 ThreadLocal 存储过滤条件，保证线程安全
5. **性能考虑**: 数据权限过滤在应用层进行，对于大数据量场景需要优化索引

## 扩展功能

### 1. 自定义数据范围

可以扩展 DataScopeType 枚举，添加更多数据范围类型：

```java
public enum DataScopeType {
    ALL(1, "全部数据"),
    DEPT_AND_CHILD(2, "本部门及下级"),
    DEPT(3, "本部门"),
    SELF(4, "仅本人"),
    CUSTOM(5, "自定义");  // 新增：自定义数据范围
}
```

### 2. 部门数据权限缓存

对于频繁查询的部门层级关系，可以考虑使用 Redis 缓存：

```java
@Cacheable(value = "dept:children", key = "#deptId")
public List<Long> getChildDeptIds(Long deptId) {
    return deptMapper.selectChildDeptIds(deptId);
}
```

## 故障排查

### 问题 1: 数据权限不生效

**原因**: @DataScope 注解未被识别或 DataScopeAspect 未启动

**解决方案**:
1. 确认 DataScopeAspect 类上有 @Aspect 和 @Component 注解
2. 确认 Spring Boot 应用开启了 AOP: `@EnableAspectJAutoProxy`
3. 检查方法上的 @DataScope 注解是否正确

### 问题 2: 查询结果为空

**原因**: 用户没有部门或角色配置不正确

**解决方案**:
1. 检查用户的 dept_id 是否为空
2. 检查用户的角色是否配置了 data_scope
3. 查看日志中的 SQL 过滤条件是否正确

### 问题 3: 超级管理员受限制

**原因**: 角色编码不是 SUPER_ADMIN

**解决方案**:
1. 确认角色表中的 role_code 字段值为 'SUPER_ADMIN'
2. 确认用户分配了该角色

## 参考资料

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [Spring AOP 官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop)
- [Element Plus Tree Select](https://element-plus.org/zh-CN/component/tree-select.html)
