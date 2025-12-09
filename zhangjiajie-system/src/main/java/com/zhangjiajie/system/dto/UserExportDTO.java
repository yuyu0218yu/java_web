package com.zhangjiajie.system.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户导出DTO
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Data
@HeadRowHeight(25)
@ColumnWidth(20)
public class UserExportDTO {

    @ExcelProperty("用户ID")
    private Long id;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("真实姓名")
    private String realName;

    @ExcelProperty("昵称")
    private String nickname;

    @ExcelProperty("邮箱")
    @ColumnWidth(30)
    private String email;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("部门名称")
    private String deptName;

    @ExcelProperty("性别")
    private String genderText;

    @ExcelProperty("状态")
    private String statusText;

    @ExcelProperty("生日")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @ExcelProperty("最后登录时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    private LocalDateTime lastLoginTime;

    @ExcelProperty("最后登录IP")
    private String lastLoginIp;

    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    private LocalDateTime createTime;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
