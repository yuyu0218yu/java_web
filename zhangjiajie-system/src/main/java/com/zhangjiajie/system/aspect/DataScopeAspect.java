package com.zhangjiajie.system.aspect;

import com.zhangjiajie.common.annotation.DataScope;
import com.zhangjiajie.common.enums.DataScopeType;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.system.entity.Role;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.mapper.DeptMapper;
import com.zhangjiajie.system.mapper.RoleMapper;
import com.zhangjiajie.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限过滤切面
 * 根据用户角色的数据范围自动添加数据过滤条件
 *
 * @author yushuang
 * @since 2025-12-08
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DataScopeAspect {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final DeptMapper deptMapper;

    private static final ThreadLocal<String> DATA_SCOPE_SQL = new ThreadLocal<>();

    /**
     * 数据权限过滤切入点
     */
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        clearDataScope();
        
        // 获取当前用户
        String username = SecurityUtils.getUsername();
        if (username == null) {
            log.debug("未获取到当前用户，跳过数据权限过滤");
            return;
        }

        User currentUser = userMapper.selectByUsername(username);
        if (currentUser == null) {
            log.warn("用户不存在: {}", username);
            return;
        }

        // 超级管理员不过滤数据
        if (isSuperAdmin(currentUser)) {
            log.debug("超级管理员，查看所有数据");
            return;
        }

        // 获取用户的所有角色
        List<Role> roles = roleMapper.selectRolesByUserId(currentUser.getId());
        if (roles == null || roles.isEmpty()) {
            log.debug("用户 {} 没有角色，应用最严格的数据权限(仅本人)", username);
            String sqlString = buildSelfOnlySql(currentUser, dataScope);
            DATA_SCOPE_SQL.set(sqlString);
            return;
        }

        // 获取权限范围最大的角色（数值越小权限越大）
        Role maxScopeRole = roles.stream()
                .filter(r -> r.getDataScope() != null)
                .min((r1, r2) -> Integer.compare(r1.getDataScope(), r2.getDataScope()))
                .orElse(null);

        if (maxScopeRole == null || maxScopeRole.getDataScope() == null) {
            log.debug("用户 {} 的角色没有配置数据范围，默认仅本人", username);
            String sqlString = buildSelfOnlySql(currentUser, dataScope);
            DATA_SCOPE_SQL.set(sqlString);
            return;
        }

        // 根据数据范围构建SQL
        String sqlString = buildDataScopeSql(currentUser, maxScopeRole, dataScope);
        DATA_SCOPE_SQL.set(sqlString);
        log.debug("应用数据权限过滤: {}", sqlString);
    }

    /**
     * 判断是否为超级管理员
     */
    private boolean isSuperAdmin(User user) {
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        return roles.stream()
                .anyMatch(role -> "SUPER_ADMIN".equals(role.getRoleCode()));
    }

    /**
     * 构建数据权限SQL
     */
    private String buildDataScopeSql(User currentUser, Role role, DataScope dataScope) {
        DataScopeType scopeType = DataScopeType.getByCode(role.getDataScope());
        
        switch (scopeType) {
            case ALL:
                // 全部数据，不添加过滤条件
                return "";
                
            case DEPT_AND_CHILD:
                // 本部门及下级部门数据
                return buildDeptAndChildSql(currentUser, dataScope);
                
            case DEPT:
                // 仅本部门数据
                return buildDeptOnlySql(currentUser, dataScope);
                
            case SELF:
                // 仅本人数据
                return buildSelfOnlySql(currentUser, dataScope);
                
            default:
                log.warn("未知的数据范围类型: {}", role.getDataScope());
                return buildSelfOnlySql(currentUser, dataScope);
        }
    }

    /**
     * 构建本部门及下级部门SQL
     */
    private String buildDeptAndChildSql(User currentUser, DataScope dataScope) {
        if (currentUser.getDeptId() == null) {
            log.debug("用户没有部门信息，使用仅本人权限");
            return buildSelfOnlySql(currentUser, dataScope);
        }

        // 获取所有子部门ID (已包含本部门)
        List<Long> deptIds = deptMapper.selectChildDeptIds(currentUser.getDeptId());
        
        // 安全检查：如果返回null或空列表，降级为仅本人权限
        if (deptIds == null || deptIds.isEmpty()) {
            log.debug("未找到部门信息，降级为仅本人权限");
            return buildSelfOnlySql(currentUser, dataScope);
        }

        String deptIdsStr = deptIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String deptIdColumn = dataScope.deptIdColumn();
        String userIdColumn = dataScope.userIdColumn();
        
        // 部门数据或者自己创建的数据
        return String.format(" AND (%s IN (%s) OR %s = %d) ",
                deptIdColumn, deptIdsStr, userIdColumn, currentUser.getId());
    }

    /**
     * 构建仅本部门SQL
     */
    private String buildDeptOnlySql(User currentUser, DataScope dataScope) {
        if (currentUser.getDeptId() == null) {
            log.debug("用户没有部门信息，使用仅本人权限");
            return buildSelfOnlySql(currentUser, dataScope);
        }

        String deptIdColumn = dataScope.deptIdColumn();
        String userIdColumn = dataScope.userIdColumn();
        
        // 本部门数据或者自己创建的数据
        return String.format(" AND (%s = %d OR %s = %d) ",
                deptIdColumn, currentUser.getDeptId(), 
                userIdColumn, currentUser.getId());
    }

    /**
     * 构建仅本人SQL
     */
    private String buildSelfOnlySql(User currentUser, DataScope dataScope) {
        String userIdColumn = dataScope.userIdColumn();
        return String.format(" AND %s = %d ", userIdColumn, currentUser.getId());
    }

    /**
     * 获取数据权限SQL
     */
    public static String getDataScopeSql() {
        String sql = DATA_SCOPE_SQL.get();
        return sql == null ? "" : sql;
    }

    /**
     * 清除数据权限SQL
     */
    public static void clearDataScope() {
        DATA_SCOPE_SQL.remove();
    }
}
