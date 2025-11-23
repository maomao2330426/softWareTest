# 智能客服工单管理与溯源系统 - 快速启动指南

## 系统概述

一个完整的工单管理系统，集成了 AI 意图识别、自动分类、智能分配和全流程事件溯源功能。

**技术栈：**
- 后端：Spring Boot 3.5.7 + MyBatis + MySQL
- 前端：Vue 3 + Vite + Element Plus

## 快速启动（3 步）

### 第一步：初始化数据库

```bash
# 1. 启动 MySQL 服务（确保 MySQL 8.0+ 已安装）

# 2. 创建数据库并导入数据
mysql -u root -p < src/main/resources/schema.sql

# 3. 修改数据库配置（如需要）
# 编辑 src/main/resources/application.yml
```

### 第二步：启动后端服务

```bash
# 使用 Maven 启动
mvn spring-boot:run

# 或者使用 IDE 直接运行 SoftWareTestApplication

# 启动成功后，后端服务运行在 http://localhost:8080
```

### 第三步：启动前端服务

```bash
# 进入前端目录
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev

# 前端服务运行在 http://localhost:3000
```

## 功能演示流程

### 场景 1：用户创建工单 → AI 自动分类

1. 打开浏览器访问 http://localhost:3000
2. 点击"创建工单"
3. 填写工单信息：
   - 标题：无法登录账户
   - 描述：我输入了正确的密码但还是登录失败
   - 优先级：中
   - 用户ID：1
4. 提交后查看详情，观察：
   - AI 自动识别为"账户问题"
   - 自动分配给客服 ID=2（账户问题专员）
   - 事件历史记录完整流程

### 场景 2：管理员手动分类低置信度工单

1. 创建一个AI无法识别的工单（如"有个小问题"）
2. 进入"管理员"页面
3. 在"未分类工单"中找到该工单
4. 点击"分类"，选择合适的分类
5. 系统自动分配客服

### 场景 3：客服处理工单

1. 进入"客服工作台"
2. 输入客服 ID（如 2）
3. 查看分配给该客服的工单
4. 点击"处理"添加备注
5. 处理完成后点击"关闭"

### 场景 4：查看工单溯源

1. 点击任意工单的"详情"
2. 滚动到底部查看"事件历史"
3. 可以看到完整的事件链：
   - 工单创建（用户）
   - AI分类/人工分类（系统/管理员）
   - 客服分配（系统）
   - 处理记录（客服）
   - 关闭记录（客服/用户）

## 核心特性展示

### ✨ AI 意图识别（Mock）

系统会根据关键词自动分类：
- 包含"登录"、"密码"、"账号" → 账户问题
- 包含"bug"、"错误"、"崩溃" → 技术支持
- 包含"如何"、"功能"、"使用" → 产品咨询
- 包含"退款"、"账单"、"发票" → 账单问题

### 🎯 智能分配规则

- AI置信度 ≥ 70%：自动分类并分配客服
- AI置信度 < 70%：标记为"未分类"，通知管理员

### 📊 事件驱动架构

所有操作自动记录事件到数据库：
- 异步处理，不阻塞主流程
- 支持全流程溯源和审计
- 可追踪每个工单的完整生命周期

## API 接口测试

可以使用 Postman 或 curl 测试 API：

```bash
# 创建工单
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "测试工单",
    "description": "这是一个测试工单，包含登录问题",
    "priority": 2,
    "creatorId": 1
  }'

# 获取所有工单
curl http://localhost:8080/api/tickets

# 获取工单详情
curl http://localhost:8080/api/tickets/1

# 获取工单事件历史（溯源）
curl http://localhost:8080/api/events/ticket/1
```

## 常见问题

### Q1: 前端无法连接后端？
**A:** 检查：
1. 后端是否已启动（8080端口）
2. 前端代理配置是否正确（vite.config.js）
3. 浏览器控制台是否有 CORS 错误

### Q2: 数据库连接失败？
**A:** 检查：
1. MySQL 服务是否启动
2. application.yml 中的用户名密码是否正确
3. 数据库 ticket_system 是否已创建

### Q3: AI 分类不准确？
**A:** 当前是 Mock 实现，基于简单的关键词匹配。在工单标题或描述中包含明确的关键词可以提高准确率。实际应用可替换为真实的 NLP 模型。

### Q4: 如何添加新的客服或用户？
**A:** 直接在数据库中插入数据，或者扩展 API 添加用户管理功能。

## 项目结构总览

```
softWareTest/
├── src/main/java/com/softwaretest/   # 后端 Java 代码
│   ├── controller/                    # API 控制器
│   ├── service/                       # 业务逻辑
│   ├── mapper/                        # 数据访问层
│   ├── pojo/                          # 实体类
│   ├── event/                         # 事件驱动
│   └── ...
├── src/main/resources/
│   ├── application.yml                # 应用配置
│   └── schema.sql                     # 数据库脚本
├── frontend/                          # Vue 前端项目
│   ├── src/
│   │   ├── views/                     # 页面组件
│   │   ├── api/                       # API 接口
│   │   └── router/                    # 路由配置
│   └── package.json
├── pom.xml                            # Maven 配置
└── README.md                          # 项目文档
```

## 下一步开发建议

- [ ] 集成真实的 NLP 模型（如 BERT）提升 AI 分类准确率
- [ ] 添加用户认证和权限管理
- [ ] 实现 WebSocket 实时通知
- [ ] 添加统计报表和数据分析
- [ ] 支持工单附件上传
- [ ] 添加工单评价和满意度调查
- [ ] Docker 容器化部署

## 联系方式

如有问题，请查看项目文档或提交 Issue。

---

**祝您使用愉快！** 🎉
