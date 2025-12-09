package com.zhangjiajie.system.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户导入DTO
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Data
public class UserImportDTO {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("真实姓名")
    private String realName;

    @ExcelProperty("昵称")
    private String nickname;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("部门名称")
    private String deptName;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("生日")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate birthday;

    @ExcelProperty("备注")
    private String remark;
}
