package com.zhangjiajie.system.aspect;

import com.zhangjiajie.common.annotation.DataScope;
import com.zhangjiajie.common.enums.DataScopeType;
import com.zhangjiajie.common.security.LoginUser;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.system.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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

    private final DeptMapper deptMapper;

    private static final ThreadLocal<String> DATA_SCOPE_SQL = new ThreadLocal<>();

    /**
     * 数据权限过滤切入点 - 方法执行前
     */
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        clearDataScope();

        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            log.debug("未获取到当前用户，跳过数据权限过滤");
            return;
        }

        // 超级管理员不过滤数据
        if (loginUser.isSuperAdmin()) {
            log.debug("超级管理员，查看所有数据");
            return;
        }

        // 获取数据范围
        Integer dataScopeValue = loginUser.getDataScope();
        if (dataScopeValue == null) {
            log.debug("用户 {} 没有配置数据范围，应用最严格的数据权限(仅本人)", loginUser.getUsername());
            String sqlString = buildSelfOnlySql(loginUser, dataScope);
            DATA_SCOPE_SQL.set(sqlString);
            return;
        }

        // 根据数据范围构建SQL
        String sqlString = buildDataScopeSql(loginUser, dataScopeValue, dataScope);
        DATA_SCOPE_SQL.set(sqlString);
        log.debug("应用数据权限过滤: {}", sqlString);
    }

    /**
     * 方法执行后清理 ThreadLocal
     */
    @After("@annotation(dataScope)")
    public void doAfter(JoinPoint point, DataScope dataScope) {
        clearDataScope();
    }

    /**
     * 构建数据权限SQL
     */
    private String buildDataScopeSql(LoginUser loginUser, Integer dataScopeValue, DataScope dataScope) {
        DataScopeType scopeType = DataScopeType.getByCode(dataScopeValue);

        switch (scopeType) {
            case ALL:
                // 全部数据，不添加过滤条件
                return "";

            case DEPT_AND_CHILD:
                // 本部门及下级部门数据
                return buildDeptAndChildSql(loginUser, dataScope);

            case DEPT:
                // 仅本部门数据
                return buildDeptOnlySql(loginUser, dataScope);

            case SELF:
                // 仅本人数据
                return buildSelfOnlySql(loginUser, dataScope);

            default:
                log.warn("未知的数据范围类型: {}", dataScopeValue);
                return buildSelfOnlySql(loginUser, dataScope);
        }
    }

    /**
     * 构建本部门及下级部门SQL
     */
    private String buildDeptAndChildSql(LoginUser loginUser, DataScope dataScope) {
        Long deptId = loginUser.getDeptId();
        if (deptId == null) {
            log.debug("用户没有部门信息，使用仅本人权限");
            return buildSelfOnlySql(loginUser, dataScope);
        }

        // 获取所有子部门ID (已包含本部门)
        List<Long> deptIds = deptMapper.selectChildDeptIds(deptId);

        // 安全检查：如果返回null或空列表，降级为仅本人权限
        if (deptIds == null || deptIds.isEmpty()) {
            log.debug("未找到部门信息，降级为仅本人权限");
            return buildSelfOnlySql(loginUser, dataScope);
        }

        String deptIdsStr = deptIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String deptIdColumn = dataScope.deptIdColumn();
        String userIdColumn = dataScope.userIdColumn();

        // 部门数据或者自己创建的数据
        return String.format(" AND (%s IN (%s) OR %s = %d) ",
                deptIdColumn, deptIdsStr, userIdColumn, loginUser.getUserId());
    }

    /**
     * 构建仅本部门SQL
     */
    private String buildDeptOnlySql(LoginUser loginUser, DataScope dataScope) {
        Long deptId = loginUser.getDeptId();
        if (deptId == null) {
            log.debug("用户没有部门信息，使用仅本人权限");
            return buildSelfOnlySql(loginUser, dataScope);
        }

        String deptIdColumn = dataScope.deptIdColumn();
        String userIdColumn = dataScope.userIdColumn();

        // 本部门数据或者自己创建的数据
        return String.format(" AND (%s = %d OR %s = %d) ",
                deptIdColumn, deptId,
                userIdColumn, loginUser.getUserId());
    }

    /**
     * 构建仅本人SQL
     */
    private String buildSelfOnlySql(LoginUser loginUser, DataScope dataScope) {
        String userIdColumn = dataScope.userIdColumn();
        return String.format(" AND %s = %d ", userIdColumn, loginUser.getUserId());
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
