package com.softwaretest.mapper;


import com.softwaretest.pojo.Session;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 会话数据访问层
 */
@Mapper
public interface SessionMapper {

    @Select("SELECT * FROM session WHERE session_id = #{sessionId}")
    Session findById(Integer sessionId);

    @Select("SELECT * FROM session WHERE ticket_id = #{ticketId}")
    Session findByTicketId(Integer ticketId);

    @Select("SELECT * FROM session WHERE archived = #{archived}")
    List<Session> findByArchived(Boolean archived);

    @Select("SELECT * FROM session")
    List<Session> findAll();

    @Insert("INSERT INTO session(ticket_id, message_list, archived, created_at, updated_at) " +
            "VALUES(#{ticketId}, #{messageList}, #{archived}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "sessionId")
    int insert(Session session);

    @Update("UPDATE session SET message_list=#{messageList}, archived=#{archived}, " +
            "updated_at=#{updatedAt} WHERE session_id=#{sessionId}")
    int update(Session session);

    @Delete("DELETE FROM session WHERE session_id = #{sessionId}")
    int delete(Integer sessionId);
}
