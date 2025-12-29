package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 分页查询门票列表（包含景点名称）
     */
    @Select("<script>" +
            "SELECT t.*, s.scenic_name " +
            "FROM portal_ticket t " +
            "LEFT JOIN portal_scenic s ON t.scenic_id = s.id AND s.deleted = 0 " +
            "WHERE t.deleted = 0 " +
            "<if test='scenicId != null'> AND t.scenic_id = #{scenicId}</if>" +
            "<if test='ticketType != null'> AND t.ticket_type = #{ticketType}</if>" +
            "<if test='status != null'> AND t.status = #{status}</if>" +
            " ORDER BY t.create_time DESC" +
            "</script>")
    IPage<Ticket> selectTicketPage(Page<Ticket> page,
                                   @Param("scenicId") Long scenicId,
                                   @Param("ticketType") Integer ticketType,
                                   @Param("status") Integer status);

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
