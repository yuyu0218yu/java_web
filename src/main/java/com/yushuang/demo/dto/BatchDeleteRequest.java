package com.yushuang.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量删除请求DTO
 */
@Data
public class BatchDeleteRequest {

    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
}
