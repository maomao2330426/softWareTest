package com.softwaretest.mapper;


import com.softwaretest.pojo.Event;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 事件数据访问层
 */
@Mapper
public interface EventMapper {

    @Select("SELECT * FROM event WHERE event_id = #{eventId}")
    Event findById(Integer eventId);

    @Select("SELECT * FROM event WHERE ticket_id = #{ticketId} ORDER BY timestamp DESC")
    List<Event> findByTicketId(Integer ticketId);

    @Select("SELECT * FROM event WHERE event_type = #{eventType} ORDER BY timestamp DESC")
    List<Event> findByEventType(String eventType);

    @Select("SELECT * FROM event ORDER BY timestamp DESC LIMIT #{limit}")
    List<Event> findRecent(Integer limit);

    @Insert("INSERT INTO event(ticket_id, event_type, payload, operator_id, operator_type, timestamp) " +
            "VALUES(#{ticketId}, #{eventType}, #{payload}, #{operatorId}, #{operatorType}, #{timestamp})")
    @Options(useGeneratedKeys = true, keyProperty = "eventId")
    int insert(Event event);

    @Delete("DELETE FROM event WHERE event_id = #{eventId}")
    int delete(Integer eventId);
}
