package com.softwaretest.mapper;

import com.softwaretest.pojo.CustomerService;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 客服数据访问层
 */
@Mapper
public interface CustomerServiceMapper {

    @Select("SELECT * FROM customer_service WHERE cs_id = #{csId}")
    CustomerService findById(Integer csId);

    @Select("SELECT * FROM customer_service WHERE category = #{category} AND status = 'ONLINE' LIMIT 1")
    CustomerService findAvailableByCategory(String category);

    @Select("SELECT * FROM customer_service WHERE username = #{username}")
    CustomerService findByUsername(String username);

    @Select("SELECT * FROM customer_service WHERE department = #{department}")
    List<CustomerService> findByDepartment(String department);

    @Select("SELECT * FROM customer_service")
    List<CustomerService> findAll();

    @Insert("INSERT INTO customer_service(name, department, category, email, status, created_at) " +
            "VALUES(#{name}, #{department}, #{category}, #{email}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "csId")
    int insert(CustomerService customerService);

    @Update("UPDATE customer_service SET name=#{name}, department=#{department}, " +
            "category=#{category}, email=#{email}, status=#{status} WHERE cs_id=#{csId}")
    int update(CustomerService customerService);

    @Update("UPDATE customer_service SET status=#{status} WHERE cs_id=#{csId}")
    int updateStatus(@Param("csId") Integer csId, @Param("status") String status);

    @Delete("DELETE FROM customer_service WHERE cs_id = #{csId}")
    int delete(Integer csId);

    @Delete("DELETE FROM customer_service WHERE username = #{username}")
    int deleteByUsername(String username);
}
