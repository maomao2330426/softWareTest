# 项目交付文档

## 📦 项目信息

**项目名称：** 智能客服工单管理与溯源系统
**英文名称：** Intelligent Customer Service Ticket Management & Traceability System
**版本：** 1.0.0
**交付日期：** 2025年

---

## 🎯 项目概述

本项目是一个完整的企业级工单管理系统，核心特性包括：

1. **AI 智能分类** - 基于关键词和知识库的意图识别
2. **自动工单分配** - 根据分类自动分配给专业客服
3. **事件驱动架构** - 完整的工单生命周期溯源
4. **多角色支持** - 用户、客服、管理员三种角色

---

## 📊 代码统计

| 类型 | 数量 | 说明 |
|------|------|------|
| 后端 Java 文件 | 38 | 包含实体类、服务、控制器等 |
| 前端 Vue 组件 | 10 | 页面组件和配置文件 |
| 数据库表 | 7 | 完整的业务数据模型 |
| API 接口 | 20+ | RESTful API 设计 |

---

## 🏗️ 技术架构

### 后端技术栈
- **框架：** Spring Boot 3.5.7
- **Java 版本：** Java 21
- **数据库：** MySQL 8.0
- **ORM 框架：** MyBatis 3.0.5
- **工具库：** Lombok, Jackson

### 前端技术栈
- **框架：** Vue 3
- **构建工具：** Vite 4
- **UI 组件库：** Element Plus 2.4
- **路由：** Vue Router 4
- **HTTP 客户端：** Axios 1.5

---

## 📁 完整项目结构

```
softWareTest/
├── src/main/java/com/softwaretest/
│   ├── config/                        # 配置类
│   │   ├── AppConfig.java            # 应用配置（异步、Jackson）
│   │   └── WebConfig.java            # Web 配置（CORS）
│   ├── constant/                      # 常量定义
│   │   └── TicketConstants.java      # 工单常量
│   ├── controller/                    # API 控制器（4个）
│   │   ├── TicketController.java     # 工单接口
│   │   ├── AdminController.java      # 管理员接口
│   │   ├── CustomerServiceController.java  # 客服接口
│   │   └── EventController.java      # 事件溯源接口
│   ├── dto/                          # 数据传输对象（4个）
│   │   ├── CreateTicketRequest.java  # 创建工单请求
│   │   ├── ClassifyTicketRequest.java # 分类工单请求
│   │   ├── AIClassificationResult.java # AI分类结果
│   │   └── Response.java             # 统一响应包装
│   ├── enums/                        # 枚举类（3个）
│   │   ├── TicketStatus.java        # 工单状态枚举
│   │   ├── EventType.java           # 事件类型枚举
│   │   └── OperatorType.java        # 操作者类型枚举
│   ├── event/                        # 事件驱动（3个）
│   │   ├── TicketEvent.java         # 工单事件类
│   │   ├── TicketEventListener.java # 事件监听器
│   │   └── EventPublisher.java      # 事件发布器
│   ├── exception/                    # 异常处理
│   │   └── GlobalExceptionHandler.java # 全局异常处理器
│   ├── mapper/                       # 数据访问层（7个）
│   │   ├── UserMapper.java          # 用户数据访问
│   │   ├── TicketMapper.java        # 工单数据访问
│   │   ├── CustomerServiceMapper.java # 客服数据访问
│   │   ├── AdminMapper.java         # 管理员数据访问
│   │   ├── EventMapper.java         # 事件数据访问
│   │   ├── SessionMapper.java       # 会话数据访问
│   │   └── KBEntryMapper.java       # 知识库数据访问
│   ├── pojo/                         # 实体类（7个）
│   │   ├── User.java                # 用户实体
│   │   ├── Ticket.java              # 工单实体
│   │   ├── CustomerService.java     # 客服实体
│   │   ├── Admin.java               # 管理员实体
│   │   ├── Event.java               # 事件实体
│   │   ├── Session.java             # 会话实体
│   │   └── KBEntry.java             # 知识库实体
│   ├── service/                      # 业务逻辑层（2个）
│   │   ├── AIService.java           # AI意图识别服务
│   │   └── WorkflowService.java     # 工单工作流服务
│   ├── vo/                           # 视图对象（3个）
│   │   ├── TicketDetailVO.java      # 工单详情视图
│   │   ├── TicketListVO.java        # 工单列表视图
│   │   └── EventVO.java             # 事件视图
│   └── SoftWareTestApplication.java  # 应用入口
│
├── src/main/resources/
│   ├── application.yml               # 应用配置文件
│   └── schema.sql                    # 数据库初始化脚本
│
├── frontend/                         # Vue 前端项目
│   ├── src/
│   │   ├── api/                     # API 接口（2个）
│   │   │   ├── axios.js            # Axios 配置
│   │   │   └── index.js            # API 模块定义
│   │   ├── views/                   # 页面组件（5个）
│   │   │   ├── TicketList.vue      # 工单列表页
│   │   │   ├── CreateTicket.vue    # 创建工单页
│   │   │   ├── TicketDetail.vue    # 工单详情页
│   │   │   ├── AdminPanel.vue      # 管理员面板
│   │   │   └── CSWorkbench.vue     # 客服工作台
│   │   ├── router/                  # 路由配置
│   │   │   └── index.js            # 路由定义
│   │   ├── App.vue                 # 根组件
│   │   └── main.js                 # 应用入口
│   ├── index.html                   # HTML 模板
│   ├── vite.config.js              # Vite 配置
│   ├── package.json                # NPM 依赖配置
│   └── README.md                   # 前端文档
│
├── pom.xml                          # Maven 配置文件
├── .gitignore                       # Git 忽略配置
├── README.md                        # 项目主文档
└── QUICKSTART.md                   # 快速启动指南
```

---

## 🗄️ 数据库设计

### 数据表清单（7张表）

1. **user** - 用户表
   - 存储系统用户基本信息
   - 字段：user_id, phone, email, username, created_at

2. **customer_service** - 客服表
   - 存储客服人员信息及专长领域
   - 字段：cs_id, name, department, category, email, status, created_at

3. **admin** - 管理员表
   - 存储管理员账户及权限级别
   - 字段：admin_id, username, email, role_level, created_at

4. **ticket** - 工单表（核心表）
   - 存储工单完整信息
   - 字段：ticket_id, title, description, status, priority, category, creator_id, assigned_cs_id, created_at, updated_at

5. **session** - 会话表
   - 存储工单相关的对话会话
   - 字段：session_id, ticket_id, message_list(JSON), archived, created_at, updated_at

6. **event** - 事件表（溯源核心）
   - 记录所有工单操作事件
   - 字段：event_id, ticket_id, event_type, payload(JSON), operator_id, operator_type, timestamp

7. **kb_entry** - 知识库表
   - 存储AI分类的知识库条目
   - 字段：entry_id, question, answer, category, keywords, use_count, created_at, updated_at

---

## 🔥 核心功能实现

### 1. 智能工单创建与分类流程

**实现文件：**
- `WorkflowService.java` - 工单工作流服务
- `AIService.java` - AI 意图识别服务

**工作流程：**
```
用户提交工单
    ↓
创建工单记录（状态：OPEN）
    ↓
AI 意图识别
    ↓
置信度判断
    ├─ ≥70% → 自动分类 → 分配客服
    └─ <70% → 标记"未分类" → 通知管理员
    ↓
记录所有事件到 event 表
```

### 2. 事件驱动架构（EDA）

**实现文件：**
- `TicketEvent.java` - 自定义事件类
- `TicketEventListener.java` - 异步事件监听器
- `EventPublisher.java` - 事件发布器

**事件类型：**
- CREATED - 工单创建
- CLASSIFIED - 工单分类
- ASSIGNED - 客服分配
- REPLIED - 回复消息
- ESCALATED - 工单升级
- CLOSED - 工单关闭

### 3. AI 意图识别（Mock 实现）

**识别规则：**
- 技术支持：bug, 错误, 崩溃, 报错, 异常
- 账户问题：登录, 注册, 密码, 账号, 验证码
- 产品咨询：如何, 怎么, 功能, 使用, 教程
- 账单问题：付款, 退款, 发票, 账单, 价格

**置信度计算：**
- 知识库匹配：0.85 - 0.95
- 关键词匹配：0.70 - 0.85
- 无法识别：0.30 - 0.50

---

## 🌐 API 接口列表

### 工单管理 API
- `POST /api/tickets` - 创建工单
- `GET /api/tickets` - 获取所有工单
- `GET /api/tickets/{id}` - 获取工单详情
- `GET /api/tickets/status/{status}` - 按状态查询
- `GET /api/tickets/user/{userId}` - 查询用户工单
- `PUT /api/tickets/{id}/close` - 关闭工单
- `PUT /api/tickets/{id}/escalate` - 升级工单

### 管理员 API
- `PUT /api/admin/tickets/{id}/classify` - 手动分类工单
- `GET /api/admin/tickets/unclassified` - 获取未分类工单
- `GET /api/admin/tickets` - 获取所有工单
- `GET /api/admin/tickets/category/{category}` - 按分类查询

### 客服 API
- `GET /api/cs/{csId}/tickets` - 获取分配的工单
- `PUT /api/cs/tickets/{id}/handle` - 处理工单
- `PUT /api/cs/tickets/{id}/close` - 关闭工单
- `PUT /api/cs/tickets/{id}/escalate` - 升级工单

### 事件溯源 API
- `GET /api/events/ticket/{ticketId}` - 获取工单事件历史
- `GET /api/events/recent` - 获取最近事件
- `GET /api/events/type/{eventType}` - 按类型查询事件

---

## 🎨 前端页面列表

1. **工单列表页** (`TicketList.vue`)
   - 展示所有工单
   - 状态筛选
   - 跳转详情

2. **创建工单页** (`CreateTicket.vue`)
   - 表单输入
   - 实时验证
   - AI 自动分类提示

3. **工单详情页** (`TicketDetail.vue`)
   - 完整工单信息
   - 事件历史时间线
   - 操作按钮（关闭/升级）

4. **管理员面板** (`AdminPanel.vue`)
   - 未分类工单列表
   - 手动分类功能
   - 分类统计

5. **客服工作台** (`CSWorkbench.vue`)
   - 我的工单列表
   - 按状态分组
   - 处理和关闭功能

---

## 🚀 部署说明

### 环境要求
- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Node.js 16+

### 启动步骤

**1. 初始化数据库**
```bash
mysql -u root -p < src/main/resources/schema.sql
```

**2. 启动后端**
```bash
mvn spring-boot:run
# 运行在 http://localhost:8080
```

**3. 启动前端**
```bash
cd frontend
npm install
npm run dev
# 运行在 http://localhost:3000
```

---

## ✅ 测试数据

系统自动创建测试数据：
- **用户：** 3个（ID: 1, 2, 3）
- **客服：** 4个（ID: 1-技术支持, 2-账户问题, 3-产品咨询, 4-账单问题）
- **管理员：** 2个（ID: 1, 2）
- **知识库：** 6条
- **示例工单：** 3个

---

## 📋 功能清单

- ✅ 用户创建工单
- ✅ AI 智能分类（基于关键词和知识库）
- ✅ 自动分配客服
- ✅ 管理员手动分类低置信度工单
- ✅ 客服处理工单
- ✅ 工单升级
- ✅ 工单关闭
- ✅ 完整事件溯源
- ✅ 多状态工单管理
- ✅ 优先级管理
- ✅ RESTful API 设计
- ✅ 前后端分离架构
- ✅ 响应式 UI 设计
- ✅ 全局异常处理
- ✅ CORS 跨域支持
- ✅ 异步事件处理

---

## 🎯 项目亮点

1. **事件驱动架构** - 完整的工单生命周期追踪，支持审计和溯源
2. **AI 智能分类** - 自动化工单处理，提高效率
3. **模块化设计** - 清晰的分层架构，易于维护和扩展
4. **现代化技术栈** - 使用最新的 Spring Boot 3 和 Vue 3
5. **完整的文档** - README、QUICKSTART、API 文档齐全

---

## 🔮 未来扩展建议

- [ ] 集成真实 NLP 模型（BERT、GPT）
- [ ] 添加用户认证和 JWT
- [ ] WebSocket 实时通知
- [ ] 工单附件上传
- [ ] 统计报表和数据分析
- [ ] 工单评价系统
- [ ] 邮件通知
- [ ] Docker 容器化部署
- [ ] 单元测试和集成测试

---

## 📞 支持与联系

如有问题，请参考项目文档或提交 Issue。

**文档清单：**
- `README.md` - 项目主文档
- `QUICKSTART.md` - 快速启动指南
- `frontend/README.md` - 前端文档
- `DELIVERY.md` - 本交付文档

---

**项目交付完成！** ✨
