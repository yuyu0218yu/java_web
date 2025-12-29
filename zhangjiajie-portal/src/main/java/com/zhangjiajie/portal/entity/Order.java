package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("portal_order")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 景点ID
     */
    private Long scenicId;

    /**
     * 景点名称
     */
    private String scenicName;

    /**
     * 门票ID
     */
    private Long ticketId;

    /**
     * 门票名称
     */
    private String ticketName;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 联系人身份证
     */
    private String contactIdCard;

    /**
     * 游玩日期
     */
    private LocalDate visitDate;

    /**
     * 订单状态：0待支付 1已支付 2已使用 3已取消 4已退款
     */
    private Integer orderStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式：1微信 2支付宝
     */
    private Integer payType;

    /**
     * 电子票二维码
     */
    private String qrCode;

    /**
     * 备注
     */
    private String remark;

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
     * 删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 订单状态名称
     */
    public String getOrderStatusName() {
        if (orderStatus == null) return "";
        return switch (orderStatus) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已使用";
            case 3 -> "已取消";
            case 4 -> "已退款";
            default -> "";
        };
    }
}
