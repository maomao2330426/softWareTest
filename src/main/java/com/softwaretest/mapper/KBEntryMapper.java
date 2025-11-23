package com.softwaretest.mapper;


import com.softwaretest.pojo.KBEntry;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 知识库条目数据访问层
 */
@Mapper
public interface KBEntryMapper {

    @Select("SELECT * FROM kb_entry WHERE entry_id = #{entryId}")
    KBEntry findById(Integer entryId);

    @Select("SELECT * FROM kb_entry WHERE category = #{category}")
    List<KBEntry> findByCategory(String category);

    @Select("SELECT * FROM kb_entry WHERE keywords LIKE CONCAT('%', #{keyword}, '%') OR question LIKE CONCAT('%', #{keyword}, '%')")
    List<KBEntry> searchByKeyword(String keyword);

    @Select("SELECT * FROM kb_entry ORDER BY use_count DESC LIMIT #{limit}")
    List<KBEntry> findTopUsed(Integer limit);

    @Select("SELECT * FROM kb_entry")
    List<KBEntry> findAll();

    @Insert("INSERT INTO kb_entry(question, answer, category, keywords, use_count, created_at, updated_at) " +
            "VALUES(#{question}, #{answer}, #{category}, #{keywords}, #{useCount}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "entryId")
    int insert(KBEntry kbEntry);

    @Update("UPDATE kb_entry SET question=#{question}, answer=#{answer}, category=#{category}, " +
            "keywords=#{keywords}, use_count=#{useCount}, updated_at=#{updatedAt} WHERE entry_id=#{entryId}")
    int update(KBEntry kbEntry);

    @Update("UPDATE kb_entry SET use_count = use_count + 1 WHERE entry_id=#{entryId}")
    int incrementUseCount(Integer entryId);

    @Delete("DELETE FROM kb_entry WHERE entry_id = #{entryId}")
    int delete(Integer entryId);
}
