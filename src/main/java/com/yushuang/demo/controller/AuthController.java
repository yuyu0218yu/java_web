package com.yushuang.demo.controller;

import com.yushuang.demo.common.Result;
import com.yushuang.demo.dto.LoginRequest;
import com.yushuang.demo.dto.LoginResponse;
import com.yushuang.demo.service.AuthService;
import com.yushuang.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取JWT token")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return Result.success("登录成功", response);
        } catch (Exception e) {
            log.warn("登录失败: {}", e.getMessage());
            return Result.error(401, "用户名或密码错误");
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public Result<Object> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Object userInfo = authService.getUserInfo(username);
            return Result.success(userInfo);
        } catch (Exception e) {
            log.warn("获取用户信息失败: {}", e.getMessage());
            return Result.error(401, "获取用户信息失败");
        }
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "刷新JWT token")
    public Result<String> refreshToken(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null) {
                return Result.error(401, "缺少Token");
            }

            String newToken = authService.refreshToken(token);
            return Result.success("Token刷新成功", newToken);
        } catch (Exception e) {
            log.warn("刷新Token失败: {}", e.getMessage());
            return Result.error(401, "Token刷新失败");
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "用户退出登录")
    public Result<String> logout() {
        // 由于使用JWT，客户端删除token即可
        // 这里可以记录日志或进行其他清理操作
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("用户 {} 退出登录", authentication.getName());
        }
        return Result.success("退出登录成功");
    }

    /**
     * 从请求中提取Token
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}