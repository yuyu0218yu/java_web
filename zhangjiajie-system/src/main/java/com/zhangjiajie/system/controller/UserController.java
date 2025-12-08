package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.dto.ChangePasswordRequest;
import com.zhangjiajie.system.dto.CreateUserRequest;
import com.zhangjiajie.system.dto.ResetPasswordRequest;
import com.zhangjiajie.system.dto.UpdateUserRequest;
import com.zhangjiajie.system.dto.UpdateUserStatusRequest;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.dto.UserWithRole;
import com.zhangjiajie.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author yushuang
 * @since 2025-12-05
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表")
    @PreAuthorize("hasAuthority('user:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<PageResult<UserWithRole>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {

        Page<UserWithRole> page = new Page<>(current, size);
        IPage<UserWithRole> result = userService.getUserPageWithRole(page);

        PageResult<UserWithRole> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );

        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户")
    @PreAuthorize("hasAuthority('user:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户")
    @PreAuthorize("hasAuthority('user:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<UserWithRole> getUserByUsername(@Parameter(description = "用户名") @PathVariable String username) {
        UserWithRole user = userService.getUserWithRoleByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasAuthority('user:create') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setRealName(request.getRealName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setGender(request.getGender());
            user.setBirthday(request.getBirthday());
            user.setAvatar(request.getAvatar());
            user.setRemark(request.getRemark());
            user.setDeptId(request.getDeptId());

            boolean success = userService.createUser(user, request.getRoleId());
            if (success) {
                return Result.success("用户创建成功");
            } else {
                return Result.error("用户创建失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            User user = new User();
            user.setId(id);
            user.setUsername(request.getUsername());
            user.setRealName(request.getRealName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setGender(request.getGender());
            user.setBirthday(request.getBirthday());
            user.setAvatar(request.getAvatar());
            user.setRemark(request.getRemark());
            user.setDeptId(request.getDeptId());

            boolean success = userService.updateUser(user, request.getRoleIds());
            if (success) {
                return Result.success("用户更新成功");
            } else {
                return Result.error("用户更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        try {
            Integer status = request.getStatus();
            boolean success = userService.updateUserStatus(id, status);
            if (success) {
                return Result.success("用户状态更新成功");
            } else {
                return Result.error("用户状态更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "修改密码")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> changePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            boolean success = userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/reset-password")
    @Operation(summary = "重置密码")
    @PreAuthorize("hasAuthority('user:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            boolean success = userService.resetPassword(id, request.getNewPassword());
            if (success) {
                return Result.success("密码重置成功");
            } else {
                return Result.error("密码重置失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取用户权限列表")
    public Result<List<String>> getUserPermissions(@Parameter(description = "用户ID") @PathVariable Long id) {
        List<String> permissions = userService.getUserPermissions(id);
        return Result.success(permissions);
    }

    @PostMapping("/check-username")
    @Operation(summary = "检查用户名是否存在")
    public Result<Map<String, Boolean>> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Long excludeId = null;
        if (request.containsKey("excludeId")) {
            excludeId = Long.valueOf(request.get("excludeId"));
        }

        boolean exists = userService.checkUsernameExists(username, excludeId);
        return Result.success(Map.of("exists", exists));
    }

    @PostMapping("/check-email")
    @Operation(summary = "检查邮箱是否存在")
    public Result<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Long excludeId = null;
        if (request.containsKey("excludeId")) {
            excludeId = Long.valueOf(request.get("excludeId"));
        }

        boolean exists = userService.checkEmailExists(email, excludeId);
        return Result.success(Map.of("exists", exists));
    }

    @PostMapping("/check-phone")
    @Operation(summary = "检查手机号是否存在")
    public Result<Map<String, Boolean>> checkPhone(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        Long excludeId = null;
        if (request.containsKey("excludeId")) {
            excludeId = Long.valueOf(request.get("excludeId"));
        }

        boolean exists = userService.checkPhoneExists(phone, excludeId);
        return Result.success(Map.of("exists", exists));
    }
}