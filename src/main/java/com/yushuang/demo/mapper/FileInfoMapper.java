package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 增加文件下载次数
     * UPDATE语句，无法用LambdaQueryWrapper替代
     */
    @Update("UPDATE sys_file_info SET download_count = download_count + 1 WHERE deleted = 0 AND id = #{id}")
    int incrementDownloadCount(@Param("id") Long id);

    /**
     * 统计各文件类型的数量和大小
     * 聚合GROUP BY查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT file_type, COUNT(*) as count, COALESCE(SUM(file_size), 0) as total_size FROM sys_file_info " +
            "WHERE deleted = 0 AND file_size IS NOT NULL " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "GROUP BY file_type ORDER BY count DESC")
    List<FileTypeCount> countByFileType(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询总存储大小
     * 聚合SUM查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT COALESCE(SUM(file_size), 0) FROM sys_file_info WHERE deleted = 0 AND file_size IS NOT NULL " +
            "<if test='uploadUserId != null'> AND upload_user_id = #{uploadUserId} </if>")
    Long getTotalStorageSize(@Param("uploadUserId") Long uploadUserId);

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