package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    /**
     * 分页查询文件信息
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 " +
            "<if test='fileName != null and fileName != \"\"'> AND file_name LIKE CONCAT('%', #{fileName}, '%') </if>" +
            "<if test='originalName != null and originalName != \"\"'> AND original_name LIKE CONCAT('%', #{originalName}, '%') </if>" +
            "<if test='fileType != null and fileType != \"\"'> AND file_type = #{fileType} </if>" +
            "<if test='uploadUsername != null and uploadUsername != \"\"'> AND upload_username LIKE CONCAT('%', #{uploadUsername}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "ORDER BY create_time DESC")
    IPage<FileInfo> selectFileInfoPage(Page<FileInfo> page,
                                       @Param("fileName") String fileName,
                                       @Param("originalName") String originalName,
                                       @Param("fileType") String fileType,
                                       @Param("uploadUsername") String uploadUsername,
                                       @Param("status") Integer status,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 根据文件哈希查询文件信息
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 AND file_hash = #{fileHash} ORDER BY create_time DESC LIMIT 1")
    FileInfo selectByFileHash(@Param("fileHash") String fileHash);

    /**
     * 根据用户ID查询文件信息
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 AND upload_user_id = #{uploadUserId} ORDER BY create_time DESC")
    List<FileInfo> selectByUploadUserId(@Param("uploadUserId") Long uploadUserId);

    /**
     * 根据用户名查询文件信息
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 AND upload_username = #{uploadUsername} ORDER BY create_time DESC")
    List<FileInfo> selectByUploadUsername(@Param("uploadUsername") String uploadUsername);

    /**
     * 增加文件下载次数
     */
    @Update("UPDATE sys_file_info SET download_count = download_count + 1 WHERE deleted = 0 AND id = #{id}")
    int incrementDownloadCount(@Param("id") Long id);

    /**
     * 统计文件数量
     */
    @Select("SELECT COUNT(*) FROM sys_file_info WHERE deleted = 0 " +
            "<if test='uploadUserId != null'> AND upload_user_id = #{uploadUserId} </if>" +
            "<if test='fileType != null and fileType != \"\"'> AND file_type = #{fileType} </if>" +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>")
    Long countFiles(@Param("uploadUserId") Long uploadUserId,
                    @Param("fileType") String fileType,
                    @Param("startTime") LocalDateTime startTime,
                    @Param("endTime") LocalDateTime endTime);

    /**
     * 统计各文件类型的数量和大小
     */
    @Select("SELECT file_type, COUNT(*) as count, COALESCE(SUM(file_size), 0) as total_size FROM sys_file_info " +
            "WHERE deleted = 0 AND file_size IS NOT NULL " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "GROUP BY file_type ORDER BY count DESC")
    List<FileTypeCount> countByFileType(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询总存储大小
     */
    @Select("SELECT COALESCE(SUM(file_size), 0) FROM sys_file_info WHERE deleted = 0 AND file_size IS NOT NULL " +
            "<if test='uploadUserId != null'> AND upload_user_id = #{uploadUserId} </if>")
    Long getTotalStorageSize(@Param("uploadUserId") Long uploadUserId);

    /**
     * 查询热门文件（按下载次数排序）
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 AND status = 1 ORDER BY download_count DESC LIMIT #{limit}")
    List<FileInfo> selectHotFiles(@Param("limit") Integer limit);

    /**
     * 查询最新上传的文件
     */
    @Select("SELECT * FROM sys_file_info WHERE deleted = 0 AND status = 1 ORDER BY create_time DESC LIMIT #{limit}")
    List<FileInfo> selectLatestFiles(@Param("limit") Integer limit);

    /**
     * 文件类型统计结果
     */
    class FileTypeCount {
        private String fileType;
        private Long count;
        private Long totalSize;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(Long totalSize) {
            this.totalSize = totalSize;
        }
    }
}