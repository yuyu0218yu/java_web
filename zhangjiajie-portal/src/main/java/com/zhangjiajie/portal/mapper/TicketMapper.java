package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.portal.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 门票Mapper
 */
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {

    /**
     * 根据景点ID查询门票列表
     */
    @Select("SELECT t.*, s.scenic_name " +
            "FROM portal_ticket t " +
            "LEFT JOIN portal_scenic s ON t.scenic_id = s.id AND s.deleted = 0 " +
            "WHERE t.deleted = 0 AND t.status = 1 AND t.scenic_id = #{scenicId} " +
            "ORDER BY t.sort_order ASC")
    List<Ticket> selectTicketsByScenicId(@Param("scenicId") Long scenicId);

    /**
     * 减少库存
     */
    @Update("UPDATE portal_ticket SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    /**
     * 恢复库存
     */
    @Update("UPDATE portal_ticket SET stock = stock + #{quantity} WHERE id = #{id}")
    int increaseStock(@Param("id") Long id, @Param("quantity") int quantity);
}
