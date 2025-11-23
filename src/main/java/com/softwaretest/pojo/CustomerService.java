package com.softwaretest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客服实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerService {
    private Integer csId;
    private String name;
    private String username;
    private String password;
    private String department; // 部门
    private String category; // 处理类型/专长领域（技术支持、账户问题、产品咨询等）
    private String email;
    private String status; // ONLINE, OFFLINE, BUSY, ACTIVE, INACTIVE
    private LocalDateTime createdAt;

    public Integer getId() {
        return csId;
    }
}
