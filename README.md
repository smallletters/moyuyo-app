# MOYUYO ATELIER

> **A Modern Luxury Pet Lifestyle House.** 陪伴每一次共同旅程（Every Journey Together.）

基于 **uni-app (Vue 3)** + **Spring Boot 3** 的跨端宠物电商平台。

---

## 项目结构

```
MOYUYOWPC/
├── moyuyo-app/           # 前端 App（uni-app + Vue 3 + Pinia）
├── moyuyo-server/        # 后端服务（Spring Boot 3 + MyBatis-Plus + Maven 多模块）
├── APPdocs/              # 原型页面（HTML 管理后台 + 用户端界面）
├── docs/                 # 项目文档
│   ├── README.md         # 文档索引（阅读从这里开始）
│   ├── 需求文档.md        # 产品需求
│   ├── 接口文档.md        # REST API 定义
│   ├── 数据库设计.md      # 数据库表结构
│   ├── 技术架构方案.md    # 架构设计
│   ├── 后端SSO模块.md     # 认证模块设计
│   ├── 生产实施计划.md    # 上线计划
│   ├── db-migration/     # 数据库迁移脚本
│   └── deploy/           # 部署配置（Docker/Nginx/1Panel）
├── .github/
│   └── workflows/        # CI/CD（GitHub Actions）
├── .env.example          # 环境变量模板
└── docker-compose.yml    # Docker 部署配置
```

## 快速开始

### 后端启动

```bash
cd moyuyo-server
mvn spring-boot:run
```

### 前端启动

```bash
cd moyuyo-app
npm install
npm run dev:h5
```

## 文档

所有项目文档位于 [docs/](docs/) 目录，建议从 [docs/README.md](docs/README.md) 开始阅读。

## 技术栈

| 层 | 技术 |
|:-|:-|
| 前端框架 | uni-app (Vue 3 + TypeScript) |
| 状态管理 | Pinia |
| UI 组件 | uView Plus |
| 后端框架 | Spring Boot 3.5 + Java 25 |
| ORM | MyBatis-Plus 3.5 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis 7 |
| 消息队列 | RocketMQ |
| 部署 | Docker Compose / 1Panel |

## 品牌

| 维度 | 内容 |
|:---|:---|
| Slogan | Every Journey Together. / 陪伴每一次共同旅程 |
| 主色 | Sand Gold `#DBC98A` |
| 4 大 IP | MILO（探险家）· LUNA（策展家）· ATLAS（守护者）· OLIVE（艺术家） |
| 4 大产品线 | Gear / Care / Play / Home & Atelier |

完整品牌规范见 [docs/MOYUYO_BrandSystem.md](docs/MOYUYO_BrandSystem.md)。
