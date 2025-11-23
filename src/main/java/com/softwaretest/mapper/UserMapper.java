package com.softwaretest.mapper;


import com.softwaretest.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User findById(Integer userId);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user(phone, email, username, created_at) " +
            "VALUES(#{phone}, #{email}, #{username}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE user SET phone=#{phone}, email=#{email}, " +
            "username=#{username} WHERE user_id=#{userId}")
    int update(User user);

    @Delete("DELETE FROM user WHERE user_id = #{userId}")
    int delete(Integer userId);
}
