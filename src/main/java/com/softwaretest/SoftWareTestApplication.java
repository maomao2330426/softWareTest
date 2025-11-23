package com.softwaretest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 智能客服工单管理与溯源系统
 * 主应用程序入口
 */
@SpringBootApplication
@EnableAsync  // 启用异步支持（用于事件监听器）
public class SoftWareTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftWareTestApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("智能客服工单管理系统启动成功！");
        System.out.println("API文档地址: http://localhost:8080/");
        System.out.println("==============================================\n");
    }

}
