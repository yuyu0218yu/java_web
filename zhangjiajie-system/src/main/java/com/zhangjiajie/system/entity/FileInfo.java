package com.zhangjiajie.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhangjiajie.common.enums.CodeEnum;
import com.zhangjiajie.common.enums.EnableStatus;
import com.zhangjiajie.common.util.FileSizeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息实体类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_file_info")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 原始文件名
     */
    @TableField("original_name")
    private String originalName;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * MIME类型
     */
    @TableField("mime_type")
    private String mimeType;

    /**
     * 文件哈希值(SHA256)
     */
    @TableField("file_hash")
    private String fileHash;

    /**
     * 上传用户ID
     */
    @TableField("upload_user_id")
    private Long uploadUserId;

    /**
     * 上传用户名
     */
    @TableField("upload_username")
    private String uploadUsername;

    /**
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态枚举
     * 使用通用启用状态枚举，保留此内部类以维持向后兼容
     */
    public enum Status implements CodeEnum<Integer> {
        DISABLED(EnableStatus.DISABLED.getCode(), EnableStatus.DISABLED.getDesc()),
        ENABLED(EnableStatus.ENABLED.getCode(), EnableStatus.ENABLED.getDesc());

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static Status getByCode(Integer code) {
            return CodeEnum.getByCode(Status.class, code, DISABLED);
        }
    }

    /**
     * 获取文件大小的友好显示
     */
    public String getFileSizeDisplay() {
        return FileSizeUtil.formatFileSize(fileSize);
    }
}