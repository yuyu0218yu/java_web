package com.zhangjiajie.portal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建订单请求
 */
@Data
public class OrderCreateRequest {

    /**
     * 景点ID
     */
    @NotNull(message = "景点ID不能为空")
    private Long scenicId;

    /**
     * 门票ID
     */
    @NotNull(message = "门票ID不能为空")
    private Long ticketId;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    private Integer quantity;

    /**
     * 游玩日期
     */
    @NotNull(message = "游玩日期不能为空")
    private LocalDate visitDate;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    /**
     * 联系人身份证
     */
    private String contactIdCard;

    /**
     * 备注
     */
    private String remark;
}
