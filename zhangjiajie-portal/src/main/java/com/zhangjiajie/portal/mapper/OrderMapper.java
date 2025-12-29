package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.portal.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询用户订单列表
     */
    @Select("SELECT * FROM portal_order " +
            "WHERE deleted = 0 AND user_id = #{userId} " +
            "ORDER BY create_time DESC")
    IPage<Order> selectOrdersByUserId(Page<Order> page, @Param("userId") Long userId);

    /**
     * 根据订单号查询
     */
    @Select("SELECT * FROM portal_order WHERE deleted = 0 AND order_no = #{orderNo}")
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 统计用户订单数量
     */
    @Select("SELECT COUNT(*) FROM portal_order WHERE deleted = 0 AND user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户某状态的订单数量
     */
    @Select("SELECT COUNT(*) FROM portal_order WHERE deleted = 0 AND user_id = #{userId} AND order_status = #{status}")
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
