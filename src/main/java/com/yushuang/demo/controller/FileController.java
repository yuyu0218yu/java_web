package com.yushuang.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.annotation.AuditLog;
import com.yushuang.demo.common.Result;
import com.yushuang.demo.entity.FileInfo;
import com.yushuang.demo.exception.ResourceNotFoundException;
import com.yushuang.demo.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 *
 * @author yushuang
 * @since 2025-12-05
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "文件管理", description = "文件上传下载相关接口")
public class FileController {

    private final FileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "单个文件上传")
    @AuditLog(operation = "文件上传", module = "文件管理", saveRequestData = false)
    @PreAuthorize("hasAuthority('file:upload') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<FileInfo> uploadFile(@RequestParam("file") MultipartFile file,
                                     HttpServletRequest request) {
        try {
            // 获取当前用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Long userId = getUserIdFromAuthentication(authentication);

            FileInfo fileInfo = fileService.uploadFile(file, userId, username);
            return Result.success("文件上传成功", fileInfo);

        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 批量文件上传
     */
    @PostMapping("/batch-upload")
    @Operation(summary = "批量文件上传", description = "多个文件上传")
    @AuditLog(operation = "批量文件上传", module = "文件管理", saveRequestData = false)
    @PreAuthorize("hasAuthority('file:upload') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<FileInfo>> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                            HttpServletRequest request) {
        try {
            // 获取当前用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Long userId = getUserIdFromAuthentication(authentication);

            List<FileInfo> fileInfos = fileService.uploadFiles(files, userId, username);
            return Result.success("文件批量上传成功", fileInfos);

        } catch (Exception e) {
            log.error("批量文件上传失败: {}", e.getMessage(), e);
            return Result.error("批量文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 文件下载
     */
    @GetMapping("/{fileId}/download")
    @Operation(summary = "文件下载", description = "根据文件ID下载文件")
    @AuditLog(operation = "文件下载", module = "文件管理")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId,
                                                HttpServletRequest request) {
        try {
            FileInfo fileInfo = fileService.downloadFile(fileId);
            if (fileInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Result.notFound("文件不存在"));
            }
            
            Resource resource = loadFileAsResource(fileInfo.getFilePath());

            // 检查文件是否存在
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Result.notFound("文件不存在或已被删除"));
            }

            // 确定Content-Type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (Exception e) {
                log.warn("无法确定文件Content-Type: {}", e.getMessage());
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 设置响应头
            String encodedFileName = URLEncoder.encode(fileInfo.getOriginalName(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .header("X-File-Id", fileId.toString())
                    .header("X-File-Name", fileInfo.getOriginalName())
                    .header("X-File-Size", String.valueOf(fileInfo.getFileSize()))
                    .body(resource);

        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Result.error("文件下载失败: " + e.getMessage()));
        }
    }

    /**
     * 文件预览
     */
    @GetMapping("/{fileId}/preview")
    @Operation(summary = "文件预览", description = "根据文件ID预览文件")
    @AuditLog(operation = "文件预览", module = "文件管理")
    public ResponseEntity<?> previewFile(@PathVariable Long fileId,
                                              HttpServletRequest request) {
        try {
            FileInfo fileInfo = fileService.previewFile(fileId);
            if (fileInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Result.notFound("文件不存在"));
            }
            
            Resource resource = loadFileAsResource(fileInfo.getFilePath());

            // 检查文件是否存在
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Result.notFound("文件不存在或已被删除"));
            }

            // 确定Content-Type
            String contentType = fileInfo.getMimeType();
            if (contentType == null) {
                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (Exception e) {
                    log.warn("无法确定文件Content-Type: {}", e.getMessage());
                    contentType = "application/octet-stream";
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .header("X-File-Id", fileId.toString())
                    .header("X-File-Name", fileInfo.getOriginalName())
                    .body(resource);

        } catch (Exception e) {
            log.error("文件预览失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Result.error("文件预览失败: " + e.getMessage()));
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件", description = "根据文件ID删除文件")
    @AuditLog(operation = "文件删除", module = "文件管理")
    @PreAuthorize("hasAuthority('file:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<String> deleteFile(@PathVariable Long fileId) {
        try {
            boolean success = fileService.deleteFile(fileId);
            if (success) {
                return Result.success("文件删除成功");
            } else {
                return Result.error("文件不存在或删除失败");
            }
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取文件列表", description = "分页查询文件列表")
    @AuditLog(operation = "查询文件列表", module = "文件管理")
    @PreAuthorize("hasAuthority('file:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<IPage<FileInfo>> getFileList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @Parameter(description = "文件名") @RequestParam(required = false) String fileName,
            @Parameter(description = "原始文件名") @RequestParam(required = false) String originalName,
            @Parameter(description = "文件类型") @RequestParam(required = false) String fileType,
            @Parameter(description = "上传用户名") @RequestParam(required = false) String uploadUsername,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime) {

        try {
            Page<FileInfo> page = new Page<>(current, size);

            LocalDateTime start = null;
            LocalDateTime end = null;

            if (startTime != null && !startTime.isEmpty()) {
                start = LocalDateTime.parse(startTime);
            }
            if (endTime != null && !endTime.isEmpty()) {
                end = LocalDateTime.parse(endTime);
            }

            IPage<FileInfo> result = fileService.getFileList(page, fileName, originalName, fileType,
                    uploadUsername, status, start, end);

            return Result.success(result);

        } catch (Exception e) {
            log.error("获取文件列表失败: {}", e.getMessage(), e);
            return Result.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/{fileId}")
    @Operation(summary = "获取文件信息", description = "根据文件ID获取文件详细信息")
    @AuditLog(operation = "获取文件信息", module = "文件管理")
    public Result<FileInfo> getFileInfo(@PathVariable Long fileId) {
        try {
            FileInfo fileInfo = fileService.getFileById(fileId);
            if (fileInfo != null) {
                return Result.success(fileInfo);
            } else {
                return Result.error("文件不存在");
            }
        } catch (Exception e) {
            log.error("获取文件信息失败: {}", e.getMessage(), e);
            return Result.error("获取文件信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取文件统计", description = "获取文件上传统计信息")
    @AuditLog(operation = "获取文件统计", module = "文件管理")
    public Result<Map<String, Object>> getFileStatistics() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = getUserIdFromAuthentication(authentication);

            Map<String, Object> statistics = fileService.getFileStatistics(userId);
            return Result.success(statistics);

        } catch (Exception e) {
            log.error("获取文件统计失败: {}", e.getMessage(), e);
            return Result.error("获取文件统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门文件
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门文件", description = "获取下载次数最多的文件")
    @AuditLog(operation = "获取热门文件", module = "文件管理")
    public Result<List<FileInfo>> getHotFiles(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {

        try {
            List<FileInfo> hotFiles = fileService.getHotFiles(limit);
            return Result.success(hotFiles);

        } catch (Exception e) {
            log.error("获取热门文件失败: {}", e.getMessage(), e);
            return Result.error("获取热门文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取最新文件
     */
    @GetMapping("/latest")
    @Operation(summary = "获取最新文件", description = "获取最新上传的文件")
    @AuditLog(operation = "获取最新文件", module = "文件管理")
    public Result<List<FileInfo>> getLatestFiles(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {

        try {
            List<FileInfo> latestFiles = fileService.getLatestFiles(limit);
            return Result.success(latestFiles);

        } catch (Exception e) {
            log.error("获取最新文件失败: {}", e.getMessage(), e);
            return Result.error("获取最新文件失败: " + e.getMessage());
        }
    }

    /**
     * 加载文件作为资源
     */
    private Resource loadFileAsResource(String filePath) {
        try {
            // 这里需要根据实际的文件存储路径来加载文件
            java.io.File file = new java.io.File("uploads/" + filePath);
            return new FileSystemResource(file);
        } catch (Exception e) {
            log.error("加载文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件不存在或无法访问");
        }
    }

    /**
     * 从认证信息中获取用户ID
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        // 这里简化处理，实际应该从数据库或JWT中获取用户ID
        // 现在返回一个默认值
        return 1L; // 默认用户ID
    }
}