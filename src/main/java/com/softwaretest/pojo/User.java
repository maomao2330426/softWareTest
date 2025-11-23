package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String status; // ACTIVE, INACTIVE
    private LocalDateTime createdAt;
}
