package com.yushuang.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.config.FileUploadConfig;
import com.yushuang.demo.entity.FileInfo;
import com.yushuang.demo.mapper.FileInfoMapper;
import com.yushuang.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件服务实现类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileInfoMapper fileInfoMapper;
    private final FileUploadConfig fileUploadConfig;

    @Value("${file.upload.max-size:10485760}")
    private Long maxFileSize;

    @Value("${file.upload.allowed-types:jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,txt,zip,rar}")
    private String allowedTypes;

    @Override
    public FileInfo uploadFile(MultipartFile file, Long uploadUserId, String uploadUsername) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("文件大小超过限制：" + (maxFileSize / 1024 / 1024) + "MB");
        }

        // 检查文件类型
        String fileType = getFileExtension(file.getOriginalFilename());
        if (!isAllowedFileType(fileType)) {
            throw new RuntimeException("不支持的文件类型：" + fileType);
        }

        try {
            // 计算文件哈希值
            String fileHash = calculateFileHash(file.getInputStream());

            // 检查文件是否已存在
            FileInfo existingFile = fileInfoMapper.selectByFileHash(fileHash);
            if (existingFile != null) {
                log.info("文件已存在，返回现有文件信息: {}", existingFile.getOriginalName());
                return existingFile;
            }

            // 生成唯一文件名
            String fileName = generateUniqueFileName(file.getOriginalFilename());

            // 创建文件路径
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = datePath + "/" + fileName;
            Path targetPath = Paths.get(fileUploadConfig.getUploadPath(), relativePath);

            // 确保目录存在
            Files.createDirectories(targetPath.getParent());

            // 保存文件
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 保存文件信息到数据库
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(file.getOriginalFilename());
            fileInfo.setFilePath(relativePath);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(fileType);
            fileInfo.setMimeType(file.getContentType());
            fileInfo.setFileHash(fileHash);
            fileInfo.setUploadUserId(uploadUserId);
            fileInfo.setUploadUsername(uploadUsername);
            fileInfo.setDownloadCount(0);
            fileInfo.setStatus(FileInfo.Status.ENABLED.getCode());

            fileInfoMapper.insert(fileInfo);

            log.info("文件上传成功: {}", file.getOriginalFilename());
            return fileInfo;

        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public List<FileInfo> uploadFiles(MultipartFile[] files, Long uploadUserId, String uploadUsername) {
        List<FileInfo> resultList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileInfo fileInfo = uploadFile(file, uploadUserId, uploadUsername);
                resultList.add(fileInfo);
            } catch (Exception e) {
                log.error("批量上传中文件 {} 失败: {}", file.getOriginalFilename(), e.getMessage());
                // 继续处理其他文件
            }
        }

        return resultList;
    }

    @Override
    public FileInfo downloadFile(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null || fileInfo.getDeleted() == 1) {
            throw new RuntimeException("文件不存在");
        }

        if (fileInfo.getStatus() != FileInfo.Status.ENABLED.getCode()) {
            throw new RuntimeException("文件已被禁用");
        }

        // 增加下载次数
        fileInfoMapper.incrementDownloadCount(fileId);

        return fileInfo;
    }

    @Override
    public FileInfo previewFile(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null || fileInfo.getDeleted() == 1) {
            throw new RuntimeException("文件不存在");
        }

        if (fileInfo.getStatus() != FileInfo.Status.ENABLED.getCode()) {
            throw new RuntimeException("文件已被禁用");
        }

        return fileInfo;
    }

    @Override
    public boolean deleteFile(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            return false;
        }

        try {
            // 逻辑删除数据库记录
            int result = fileInfoMapper.deleteById(fileId);

            // 可选：物理删除文件
            // Path filePath = Paths.get(fileUploadConfig.getUploadPath(), fileInfo.getFilePath());
            // Files.deleteIfExists(filePath);

            return result > 0;
        } catch (Exception e) {
            log.error("删除文件失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public IPage<FileInfo> getFileList(Page<FileInfo> page, String fileName, String originalName,
                                      String fileType, String uploadUsername, Integer status,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        return fileInfoMapper.selectFileInfoPage(page, fileName, originalName, fileType,
                                               uploadUsername, status, startTime, endTime);
    }

    @Override
    public FileInfo getFileById(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo != null && fileInfo.getDeleted() == 0) {
            return fileInfo;
        }
        return null;
    }

    @Override
    public FileInfo checkFileExists(String fileHash) {
        return fileInfoMapper.selectByFileHash(fileHash);
    }

    @Override
    public Map<String, Object> getFileStatistics(Long uploadUserId) {
        Map<String, Object> statistics = new HashMap<>();

        // 文件总数
        Long fileCount = fileInfoMapper.countFiles(uploadUserId, null, null, null);
        statistics.put("fileCount", fileCount);

        // 总存储大小
        Long totalSize = fileInfoMapper.getTotalStorageSize(uploadUserId);
        statistics.put("totalSize", totalSize);
        statistics.put("totalSizeDisplay", formatFileSize(totalSize));

        // 按类型统计
        List<FileInfoMapper.FileTypeCount> typeStats = fileInfoMapper.countByFileType(null, null);
        statistics.put("typeStatistics", typeStats);

        return statistics;
    }

    @Override
    public List<FileInfo> getHotFiles(Integer limit) {
        return fileInfoMapper.selectHotFiles(limit != null ? limit : 10);
    }

    @Override
    public List<FileInfo> getLatestFiles(Integer limit) {
        return fileInfoMapper.selectLatestFiles(limit != null ? limit : 10);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }

        return "";
    }

    /**
     * 检查是否为允许的文件类型
     */
    private boolean isAllowedFileType(String fileType) {
        Set<String> allowedTypeSet = Arrays.stream(allowedTypes.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        return allowedTypeSet.contains(fileType.toLowerCase());
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uuid = UUID.randomUUID().toString().replace("-", "");

        return timestamp + "_" + uuid + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    /**
     * 计算文件SHA256哈希值
     */
    private String calculateFileHash(InputStream inputStream) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            // 如果SHA256不可用，使用MD5作为后备
            return DigestUtils.md5DigestAsHex(inputStream);
        } finally {
            inputStream.close();
        }
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }

        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double fileSize = size.doubleValue();

        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }
}