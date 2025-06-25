# CloudCare云护智慧医养大数据公共服务平台

## 项目介绍

CloudCare云护智慧医养大数据公共服务平台是一个面向医养结合领域的综合管理系统，旨在通过大数据技术提升老年人健康管理和医疗服务质量。

## 系统功能

- 首页：系统概览和数据统计
- 老人档案：老年人基本信息管理
- 健康预警：健康异常监测和预警
- 评估报告：老年人健康评估报告管理
- 重点人群：重点关注老年人管理
- 报表统计：数据统计和报表生成
- 设备管理：智能设备管理
- 老人账户管理：老年人账户信息管理
- 医生账户管理：医护人员账户管理
- 大数据决策分析：数据分析和决策支持
- 个人中心：用户个人信息管理

## 技术栈

### 后端
- SpringBoot 2.7.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- Redis
- Spring Security

### 前端
- Vue 3
- Element Plus
- Axios
- Vue Router
- Pinia

## 项目结构

```
CloudCare_system/
├── cloudcare-backend/        # 后端项目
│   ├── src/                  # 源代码
│   │   ├── main/
│   │   │   ├── java/com/cloudcare/
│   │   │   │   ├── config/   # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── entity/   # 实体类
│   │   │   │   ├── mapper/   # MyBatis映射接口
│   │   │   │   ├── service/  # 服务接口和实现
│   │   │   │   ├── utils/    # 工具类
│   │   │   │   └── CloudCareApplication.java # 启动类
│   │   │   └── resources/    # 资源文件
│   │   │       ├── mapper/   # MyBatis XML映射文件
│   │   │       ├── application.yml # 应用配置
│   │   │       └── application-dev.yml # 开发环境配置
│   │   └── test/             # 测试代码
│   └── pom.xml               # Maven配置
└── cloudcare-frontend/       # 前端项目
    ├── public/               # 静态资源
    ├── src/                  # 源代码
    │   ├── api/              # API请求
    │   ├── assets/           # 资源文件
    │   ├── components/       # 组件
    │   ├── router/           # 路由
    │   ├── store/            # 状态管理
    │   ├── views/            # 页面
    │   ├── App.vue           # 根组件
    │   └── main.js           # 入口文件
    ├── .env.development      # 开发环境变量
    ├── .env.production       # 生产环境变量
    ├── package.json          # NPM配置
    └── vite.config.js        # Vite配置
```

## 快速开始

### 后端启动

1. 确保已安装JDK 11+和Maven
2. 创建MySQL数据库，名称为`cloudcare`
3. 修改`application-dev.yml`中的数据库连接信息
4. 在`cloudcare-backend`目录下执行：

```bash
mvn spring-boot:run
```

### 前端启动

1. 确保已安装Node.js 16+
2. 在`cloudcare-frontend`目录下执行：

```bash
npm install
npm run dev
```

## 默认账号

- 管理员：admin / admin123
- 医生：doctor / doctor123
- 老人：elder / elder123

## 数据库设计

系统使用MySQL数据库，主要包含以下表：

- user：用户表
- elder：老人信息表
- doctor：医生信息表
- health_record：健康记录表
- warning：健康预警表
- assessment：评估报告表
- device：设备信息表
- statistics：统计数据表

详细的数据库设计文档请参考项目文档。

## 接口文档

启动后端服务后，访问：http://localhost:8080/swagger-ui/index.html