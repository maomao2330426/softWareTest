package com.softwaretest.mapper;

import com.softwaretest.pojo.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 工单数据访问层
 */
@Mapper
public interface TicketMapper {

    @Select("SELECT * FROM ticket WHERE ticket_id = #{ticketId}")
    Ticket findById(Integer ticketId);

    @Select("SELECT * FROM ticket WHERE creator_id = #{creatorId}")
    List<Ticket> findByCreatorId(Integer creatorId);

    @Select("SELECT * FROM ticket WHERE assigned_cs_id = #{csId}")
    List<Ticket> findByAssignedCsId(Integer csId);

    @Select("SELECT * FROM ticket WHERE status = #{status}")
    List<Ticket> findByStatus(String status);

    @Select("SELECT * FROM ticket WHERE category = #{category}")
    List<Ticket> findByCategory(String category);

    @Select("SELECT * FROM ticket ORDER BY created_at DESC")
    List<Ticket> findAll();

    @Insert("INSERT INTO ticket(title, description, status, priority, category, " +
            "creator_id, assigned_cs_id, created_at, updated_at) " +
            "VALUES(#{title}, #{description}, #{status}, #{priority}, #{category}, " +
            "#{creatorId}, #{assignedCsId}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "ticketId")
    int insert(Ticket ticket);

    @Update("UPDATE ticket SET title=#{title}, description=#{description}, " +
            "status=#{status}, priority=#{priority}, category=#{category}, " +
            "assigned_cs_id=#{assignedCsId}, updated_at=#{updatedAt} " +
            "WHERE ticket_id=#{ticketId}")
    int update(Ticket ticket);

    @Update("UPDATE ticket SET status=#{status}, updated_at=NOW() WHERE ticket_id=#{ticketId}")
    int updateStatus(@Param("ticketId") Integer ticketId, @Param("status") String status);

    @Delete("DELETE FROM ticket WHERE ticket_id = #{ticketId}")
    int delete(Integer ticketId);
}
