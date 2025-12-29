package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门票实体
 */
@Data
@TableName("portal_ticket")
public class Ticket {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 景点ID
     */
    private Long scenicId;

    /**
     * 门票名称
     */
    private String ticketName;

    /**
     * 门票类型：1成人票 2儿童票 3老年票 4学生票 5套票
     */
    private Integer ticketType;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    private BigDecimal sellingPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 每日限购
     */
    private Integer dailyLimit;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 使用规则
     */
    private String useRules;

    /**
     * 退款规则
     */
    private String refundRules;

    /**
     * 购票须知
     */
    private String notice;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0禁用 1启用
     */
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
     * 删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 景点名称（非数据库字段）
     */
    @TableField(exist = false)
    private String scenicName;

    /**
     * 门票类型名称
     */
    public String getTicketTypeName() {
        if (ticketType == null) return "";
        return switch (ticketType) {
            case 1 -> "成人票";
            case 2 -> "儿童票";
            case 3 -> "老年票";
            case 4 -> "学生票";
            case 5 -> "套票";
            default -> "";
        };
    }
}
