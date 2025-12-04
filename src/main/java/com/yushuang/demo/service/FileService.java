package com.yushuang.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 文件信息
     */
    FileInfo uploadFile(MultipartFile file, Long uploadUserId, String uploadUsername);

    /**
     * 批量文件上传
     *
     * @param files 上传的文件数组
     * @param uploadUserId 上传用户ID
     * @param uploadUsername 上传用户名
     * @return 文件信息列表
     */
    java.util.List<FileInfo> uploadFiles(MultipartFile[] files, Long uploadUserId, String uploadUsername);

    /**
     * 文件下载
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfo downloadFile(Long fileId);

    /**
     * 文件预览
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfo previewFile(Long fileId);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 删除结果
     */
    boolean deleteFile(Long fileId);

    /**
     * 分页查询文件列表
     *
     * @param page 分页参数
     * @param fileName 文件名
     * @param originalName 原始文件名
     * @param fileType 文件类型
     * @param uploadUsername 上传用户名
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<FileInfo> getFileList(Page<FileInfo> page, String fileName, String originalName,
                               String fileType, String uploadUsername, Integer status,
                               java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 根据ID获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfo getFileById(Long fileId);

    /**
     * 检查文件是否存在
     *
     * @param fileHash 文件哈希值
     * @return 文件信息
     */
    FileInfo checkFileExists(String fileHash);

    /**
     * 获取文件统计信息
     *
     * @param uploadUserId 用户ID
     * @return 统计信息
     */
    java.util.Map<String, Object> getFileStatistics(Long uploadUserId);

    /**
     * 获取热门文件列表
     *
     * @param limit 限制数量
     * @return 文件列表
     */
    java.util.List<FileInfo> getHotFiles(Integer limit);

    /**
     * 获取最新文件列表
     *
     * @param limit 限制数量
     * @return 文件列表
     */
    java.util.List<FileInfo> getLatestFiles(Integer limit);
}