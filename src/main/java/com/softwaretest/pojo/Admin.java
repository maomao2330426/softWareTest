package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Integer adminId;
    private String username;
    private String email;
    private String password;
    private Integer roleLevel; // 1-超级管理员, 2-普通管理员
    private String status; // ACTIVE, INACTIVE
    private LocalDateTime createdAt;
}
