# 智能客服工单管理系统 - 前端

基于 Vue 3 + Vite + Element Plus 的现代化前端应用。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Element Plus** - Vue 3 UI 组件库
- **Vue Router** - 官方路由管理器
- **Axios** - HTTP 客户端

## 功能模块

### 1. 工单列表
- 查看所有工单
- 按状态筛选工单
- 查看工单详情

### 2. 创建工单
- 填写标题、描述
- 设置优先级
- AI 自动分类和分配

### 3. 工单详情
- 查看完整工单信息
- 查看事件历史（全流程溯源）
- 关闭/升级工单

### 4. 管理员面板
- 查看未分类工单
- 手动分类工单
- 查看工单统计

### 5. 客服工作台
- 查看分配给自己的工单
- 处理工单
- 关闭工单

## 快速开始

### 安装依赖

```bash
cd frontend
npm install
```

### 启动开发服务器

```bash
npm run dev
```

应用将在 http://localhost:3000 启动

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口定义
│   │   ├── axios.js      # Axios 配置
│   │   └── index.js      # API 模块
│   ├── views/            # 页面组件
│   │   ├── TicketList.vue      # 工单列表
│   │   ├── CreateTicket.vue    # 创建工单
│   │   ├── TicketDetail.vue    # 工单详情
│   │   ├── AdminPanel.vue      # 管理员面板
│   │   └── CSWorkbench.vue     # 客服工作台
│   ├── router/           # 路由配置
│   ├── App.vue           # 根组件
│   └── main.js           # 应用入口
├── index.html            # HTML 模板
├── vite.config.js        # Vite 配置
└── package.json          # 依赖配置
```

## 页面路由

| 路径 | 组件 | 说明 |
|------|------|------|
| `/` | TicketList | 工单列表页 |
| `/create` | CreateTicket | 创建工单页 |
| `/ticket/:id` | TicketDetail | 工单详情页 |
| `/admin` | AdminPanel | 管理员面板 |
| `/cs` | CSWorkbench | 客服工作台 |

## API 代理配置

开发模式下，前端请求会自动代理到后端服务器：

```javascript
// vite.config.js
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 使用说明

### 1. 用户创建工单流程

1. 点击"创建工单"按钮
2. 填写工单信息（标题、描述、优先级）
3. 提交后，系统自动：
   - AI 识别意图并分类
   - 分配合适的客服
   - 记录创建事件

### 2. 管理员手动分类流程

1. 进入"管理员"页面
2. 查看"未分类工单"列表
3. 点击"分类"按钮
4. 选择合适的分类
5. 系统自动分配客服

### 3. 客服处理工单流程

1. 进入"客服工作台"
2. 输入客服ID查看分配的工单
3. 切换标签页查看不同状态的工单
4. 点击"处理"处理工单
5. 处理完成后点击"关闭"

### 4. 工单溯源

在工单详情页面，可以看到完整的事件历史：
- 工单创建
- AI分类（或人工分类）
- 客服分配
- 处理记录
- 关闭记录

## 测试数据

系统自带测试数据：
- 用户ID: 1, 2, 3
- 客服ID: 1-技术支持, 2-账户问题, 3-产品咨询, 4-账单问题
- 管理员ID: 1, 2

## 开发注意事项

1. **跨域处理**：开发环境使用 Vite 代理，生产环境需配置 Nginx 或后端 CORS
2. **用户认证**：当前版本使用测试ID，实际应用需集成完整的用户认证系统
3. **实时更新**：可集成 WebSocket 实现工单状态实时推送
4. **错误处理**：已配置全局错误拦截器，统一处理 API 错误

## 部署

### 前后端分离部署

1. **前端部署（Nginx）**

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

2. **后端部署**

```bash
cd ..
mvn clean package
java -jar target/softWareTest-0.0.1-SNAPSHOT.jar
```

### Docker 部署（可选）

创建 `Dockerfile`：

```dockerfile
# 前端构建
FROM node:16 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# 后端构建
FROM maven:3.8-openjdk-21 AS backend-build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# 运行时镜像
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
COPY --from=frontend-build /app/frontend/dist ./static
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

## 许可证

MIT License
