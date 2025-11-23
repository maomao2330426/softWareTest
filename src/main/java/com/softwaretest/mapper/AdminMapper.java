package com.softwaretest.mapper;

import com.softwaretest.pojo.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 管理员数据访问层
 */
@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM admin WHERE admin_id = #{adminId}")
    Admin findById(Integer adminId);

    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin findByUsername(String username);

    @Select("SELECT * FROM admin")
    List<Admin> findAll();

    @Insert("INSERT INTO admin(username, email, role_level, created_at) " +
            "VALUES(#{username}, #{email}, #{roleLevel}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "adminId")
    int insert(Admin admin);

    @Update("UPDATE admin SET username=#{username}, email=#{email}, " +
            "role_level=#{roleLevel} WHERE admin_id=#{adminId}")
    int update(Admin admin);

    @Delete("DELETE FROM admin WHERE admin_id = #{adminId}")
    int delete(Integer adminId);
}
