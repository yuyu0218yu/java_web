package com.yushuang.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.common.PageResult;
import com.yushuang.demo.common.Result;
import com.yushuang.demo.entity.User;
import com.yushuang.demo.mapper.UserMapper.UserWithRole;
import com.yushuang.demo.service.UserService;
import com.yushuang.demo.util.IpUtil;
import com.yushuang.demo.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表")
    public Result<PageResult<UserWithRole>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {

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
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
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
    public Result<Void> changePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {
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
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("newPassword");
            if (!newPassword.matches(".{6,20}")) {
                return Result.error("密码长度必须在6-20位之间");
            }

            boolean success = userService.resetPassword(id, newPassword);
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
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("用户删除失败");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            String loginIp = IpUtil.getClientIp(httpRequest);
            User user = userService.login(request.getUsername(), request.getPassword(), loginIp);

            if (user == null) {
                return Result.error("用户名或密码错误");
            }

            LoginResponse response = new LoginResponse();
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setRealName(user.getRealName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
            response.setAvatar(user.getAvatar());
            response.setLastLoginTime(user.getLastLoginTime());
            response.setLastLoginIp(user.getLastLoginIp());

            // 获取用户权限
            List<String> permissions = userService.getUserPermissions(user.getId());
            response.setPermissions(permissions);

            return Result.success("登录成功", response);
        } catch (Exception e) {
            return Result.error("登录失败: " + e.getMessage());
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

    // 请求和响应对象
    public static class CreateUserRequest {
        private String username;
        private String password;
        private String realName;
        private String email;
        private String phone;
        private Integer gender;
        private java.time.LocalDate birthday;
        private String avatar;
        private String remark;
        private Long roleId;

        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Integer getGender() { return gender; }
        public void setGender(Integer gender) { this.gender = gender; }
        public java.time.LocalDate getBirthday() { return birthday; }
        public void setBirthday(java.time.LocalDate birthday) { this.birthday = birthday; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
    }

    public static class UpdateUserRequest {
        private String username;
        private String realName;
        private String email;
        private String phone;
        private Integer gender;
        private java.time.LocalDate birthday;
        private String avatar;
        private String remark;
        private List<Long> roleIds;

        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Integer getGender() { return gender; }
        public void setGender(Integer gender) { this.gender = gender; }
        public java.time.LocalDate getBirthday() { return birthday; }
        public void setBirthday(java.time.LocalDate birthday) { this.birthday = birthday; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
        public List<Long> getRoleIds() { return roleIds; }
        public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
    }

    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private Long userId;
        private String username;
        private String realName;
        private String email;
        private String phone;
        private String avatar;
        private java.time.LocalDateTime lastLoginTime;
        private String lastLoginIp;
        private List<String> permissions;

        // getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public java.time.LocalDateTime getLastLoginTime() { return lastLoginTime; }
        public void setLastLoginTime(java.time.LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }
        public String getLastLoginIp() { return lastLoginIp; }
        public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    }
}