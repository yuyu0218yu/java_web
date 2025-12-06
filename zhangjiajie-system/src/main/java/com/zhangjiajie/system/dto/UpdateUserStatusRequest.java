package com.zhangjiajie.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户状态请求DTO
 */
@Data
public class UpdateUserStatusRequest {

    @NotNull(message = "状态值不能为空")
    @Min(value = 0, message = "状态值不合法")
    private Integer status;
}
