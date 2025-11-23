-- 智能客服工单管理系统 - 数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ticket_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ticket_system;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `user_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_email (`email`),
    INDEX idx_phone (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 客服表
CREATE TABLE IF NOT EXISTS `customer_service` (
    `cs_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '客服ID',
    `name` VARCHAR(50) NOT NULL COMMENT '客服姓名',
    `department` VARCHAR(50) COMMENT '部门',
    `category` VARCHAR(50) COMMENT '处理类型/专长领域',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `status` VARCHAR(20) DEFAULT 'OFFLINE' COMMENT '状态: ONLINE, OFFLINE, BUSY',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_category (`category`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服表';

-- 3. 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
    `admin_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `role_level` INT DEFAULT 2 COMMENT '角色级别: 1-超级管理员, 2-普通管理员',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 4. 工单表
CREATE TABLE IF NOT EXISTS `ticket` (
    `ticket_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '工单ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` TEXT NOT NULL COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'OPEN' COMMENT '状态: OPEN, PENDING, CLOSED, UNCLASSIFIED',
    `priority` INT DEFAULT 2 COMMENT '优先级: 1-低, 2-中, 3-高, 4-紧急',
    `category` VARCHAR(50) COMMENT '分类',
    `creator_id` INT NOT NULL COMMENT '创建者用户ID',
    `assigned_cs_id` INT COMMENT '分配的客服ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`creator_id`) REFERENCES `user`(`user_id`),
    FOREIGN KEY (`assigned_cs_id`) REFERENCES `customer_service`(`cs_id`),
    INDEX idx_status (`status`),
    INDEX idx_category (`category`),
    INDEX idx_creator (`creator_id`),
    INDEX idx_assigned_cs (`assigned_cs_id`),
    INDEX idx_created_at (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

-- 5. 会话表
CREATE TABLE IF NOT EXISTS `session` (
    `session_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `ticket_id` INT NOT NULL COMMENT '关联的工单ID',
    `message_list` JSON COMMENT '消息列表（JSON格式）',
    `archived` BOOLEAN DEFAULT FALSE COMMENT '是否已归档',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`ticket_id`) REFERENCES `ticket`(`ticket_id`),
    INDEX idx_ticket (`ticket_id`),
    INDEX idx_archived (`archived`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话表';

-- 6. 事件表（溯源）
CREATE TABLE IF NOT EXISTS `event` (
    `event_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
    `ticket_id` INT NOT NULL COMMENT '关联的工单ID',
    `event_type` VARCHAR(50) NOT NULL COMMENT '事件类型: CREATED, CLASSIFIED, ASSIGNED, REPLIED, ESCALATED, CLOSED',
    `payload` JSON COMMENT '事件详细信息（JSON格式）',
    `operator_id` INT COMMENT '操作者ID',
    `operator_type` VARCHAR(20) COMMENT '操作者类型: USER, CS, ADMIN, SYSTEM',
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    FOREIGN KEY (`ticket_id`) REFERENCES `ticket`(`ticket_id`),
    INDEX idx_ticket (`ticket_id`),
    INDEX idx_event_type (`event_type`),
    INDEX idx_timestamp (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件表（工单溯源）';

-- 7. 知识库条目表
CREATE TABLE IF NOT EXISTS `kb_entry` (
    `entry_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '知识库条目ID',
    `question` VARCHAR(500) NOT NULL COMMENT '问题关键词',
    `answer` TEXT COMMENT '答案或解决方案',
    `category` VARCHAR(50) NOT NULL COMMENT '分类',
    `keywords` VARCHAR(500) COMMENT '关键词列表（逗号分隔）',
    `use_count` INT DEFAULT 0 COMMENT '使用次数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (`category`),
    FULLTEXT INDEX idx_keywords (`keywords`, `question`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库条目表';

-- 插入测试数据

-- 用户数据
INSERT INTO `user` (`phone`, `email`, `username`) VALUES
('13800138001', 'user1@example.com', '张三'),
('13800138002', 'user2@example.com', '李四'),
('13800138003', 'user3@example.com', '王五');

-- 客服数据
INSERT INTO `customer_service` (`name`, `department`, `category`, `email`, `status`) VALUES
('客服小王', '技术部', '技术支持', 'cs_wang@example.com', 'ONLINE'),
('客服小李', '客户服务部', '账户问题', 'cs_li@example.com', 'ONLINE'),
('客服小张', '销售部', '产品咨询', 'cs_zhang@example.com', 'ONLINE'),
('客服小刘', '财务部', '账单问题', 'cs_liu@example.com', 'OFFLINE');

-- 管理员数据
INSERT INTO `admin` (`username`, `email`, `role_level`) VALUES
('admin', 'admin@example.com', 1),
('manager', 'manager@example.com', 2);

-- 知识库数据
INSERT INTO `kb_entry` (`question`, `answer`, `category`, `keywords`) VALUES
('如何重置密码', '点击登录页面的"忘记密码"链接，输入您的邮箱，我们将发送重置链接到您的邮箱。', '账户问题', '密码,重置,找回,忘记'),
('登录失败怎么办', '请确认用户名和密码正确，检查大小写和空格。如果多次失败，账户可能被锁定，请联系客服。', '账户问题', '登录,失败,无法登录'),
('产品有哪些功能', '我们的产品提供工单管理、客服分配、AI智能分类、全流程溯源等核心功能。', '产品咨询', '功能,介绍,产品,特性'),
('如何提交工单', '在首页点击"创建工单"按钮，填写标题和描述，系统将自动分配客服处理。', '产品咨询', '工单,提交,创建'),
('应用崩溃怎么办', '请提供崩溃时的错误信息和操作步骤，我们的技术团队将尽快为您解决。', '技术支持', '崩溃,bug,错误,报错'),
('如何申请退款', '请在"我的订单"中找到需要退款的订单，点击"申请退款"按钮，填写退款原因。', '账单问题', '退款,退费,申请');

-- 测试工单数据（可选）
INSERT INTO `ticket` (`title`, `description`, `status`, `priority`, `category`, `creator_id`, `assigned_cs_id`) VALUES
('无法登录账户', '我尝试多次登录都失败了，显示密码错误，但我确认密码是正确的', 'PENDING', 2, '账户问题', 1, 2),
('产品功能咨询', '想了解一下工单系统的AI分类功能是如何工作的', 'PENDING', 1, '产品咨询', 2, 3),
('应用崩溃问题', '在提交工单时应用突然崩溃，无法继续操作', 'OPEN', 3, '技术支持', 3, 1);

COMMIT;
