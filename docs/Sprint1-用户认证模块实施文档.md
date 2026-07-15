# Sprint 1 — 用户认证模块实施开发文档

> **项目**: MOYUYO 宠物用品商城  
> **版本**: V1.0  
> **创建日期**: 2026-07-15  
> **状态**: 待实施

***

## 一、现状分析

### 前端 (uni-app) — 页面已就绪，需切换后端

| 文件 | 状态 | 说明 |
|------|:----:|------|
| `src/pages/user/login.vue` | ✅ 已完成 | 表单 + 社交登录占位 |
| `src/pages/user/register.vue` | ✅ 已完成 | 含 GDPR 同意/年龄校验 |
| `src/pages/user/profile.vue` | ✅ 已完成 | 编辑资料 |
| `src/pages/user/security.vue` | ✅ 已完成 | 密码/2FA/社交绑定 |
| `src/pages/tabbar/user.vue` | ✅ 已完成 | "我的" Tab 页 |
| `src/store/user.js` | ✅ 已完成 | 但调用的是 WooCommerce API |
| `src/api/user.js` | ✅ 已完成 | 但对接的是 WP OAuth Server |
| `src/plugins/auth.js/ts` | ✅ 已完成 | 原生插件封装 |

### 后端 (Spring Boot) — 需新建

| 组件 | 状态 | 说明 |
|------|:----:|------|
| `UserEntity.java` | ⚠️ 已有骨架 | **缺 password_hash/dob/phone 等字段** |
| `UserMapper.java` | ✅ 已有 | 基础 CRUD，无自定义方法 |
| `JwtUtil.java` | ✅ 已有 | 生成/解析/验证，可用 |
| `Result.java` | ✅ 已有 | 统一返回体 |
| `GlobalExceptionHandler.java` | ✅ 已有 | 全局异常处理 |
| `RedisConfig.java` | ✅ 已有 | 可供 Token 黑名单使用 |
| **AuthController** | ❌ 无 | 需新建 |
| **AuthService** | ❌ 无 | 需新建 |
| **JwtAuthFilter** | ❌ 无 | 需新建 |
| **LoginDTO/RegisterDTO** | ❌ 无 | 需新建 |
| **2FA 相关** | ❌ 无 | 需新建 |

***

## 二、实施步骤

### 第1步：数据库层 — 补充 UserEntity

**目标**: 补齐 `mo_user` 表实体字段，使其与数据库完全一致。

**涉及文件**:
- [修改] `moyuyo-dao/.../entity/UserEntity.java`

**具体变更**:
```java
// 新增字段
private String passwordHash;      // BCrypt 加密密码
private String phone;              // 手机号
private LocalDate birthday;        // 生日
private String country;            // 国家 ISO 3166-1 alpha-2
private String locale;             // 区域
private String timezone;           // 时区
private Boolean twoFactorEnabled;  // 2FA 是否启用
private Boolean emailVerified;     // 邮箱是否验证
private Boolean marketingOptIn;    // 营销订阅
private String nickname;           // 昵称（已有但确认类型）
private Integer status;            // 状态 1正常 0禁用
private Integer points;            // 积分
private LocalDateTime lastLoginTime; // 最后登录时间
```

**验证**: 编译通过即可（无业务逻辑变更）。

***

### 第2步：DTO 定义 — 请求/响应体

**目标**: 创建认证模块的请求和响应数据传输对象。

**新建文件**:
- `moyuyo-common/.../dto/auth/LoginRequest.java`
- `moyuyo-common/.../dto/auth/RegisterRequest.java`
- `moyuyo-common/.../dto/auth/TokenResponse.java`
- `moyuyo-common/.../dto/auth/RefreshTokenRequest.java`

**规范**:
- 使用 Jakarta Validation 注解做参数校验
- 使用 Swagger 注解做文档标注

**验证**: 编译通过。

***

### 第3步：业务层 — AuthService

**目标**: 实现认证核心业务逻辑。

**新建文件**:
- `moyuyo-service/.../service/AuthService.java`
- `moyuyo-service/.../service/impl/AuthServiceImpl.java`

**功能清单**:

| 方法 | 功能 | 关键逻辑 |
|------|------|----------|
| `register(RegisterRequest req)` | 用户注册 | 校验邮箱唯一 → BCrypt 加密密码 → 写入 mo_user → 生成 JWT |
| `login(LoginRequest req)` | 用户登录 | 邮箱查询 → 校验密码(BCrypt) → 检查 status → 生成 TokenPair |
| `refreshToken(String refreshToken)` | 刷新 Token | 校验 refreshToken → 生成新的 accessToken |
| `sendVerificationEmail(String email)` | 发送邮箱验证 | 生成6位验证码 → 存入 Redis(5min) → 返回 "已发送" |
| `verifyEmail(String email, String code)` | 验证邮箱 | 校验 Redis 验证码 → 更新 emailVerified = true |
| `sendPasswordResetEmail(String email)` | 发送重置密码 | 生成 resetToken → 存入 Redis(30min) → 邮件发送 |
| `resetPassword(String resetToken, String newPassword)` | 重置密码 | 校验 resetToken → BCrypt 加密 → 更新密码 |
| `changePassword(Long userId, String oldPwd, String newPwd)` | 修改密码 | 校验旧密码 → BCrypt 加密新密码 → 更新 |
| `logout(Long userId, String accessToken)` | 退出登录 | accessToken 加入 Redis 黑名单(有效期至过期) |

**TokenPair 设计**:
```
accessToken  ← JWT，有效期 2h，包含 userId + email
refreshToken ← 随机 UUID(64位)，有效期 7天，存入 Redis
```

**验证**: 编写单元测试，启动应用后用 curl 测试接口。

***

### 第4步：控制层 — AuthController

**目标**: 暴露认证 REST API。

**新建文件**:
- `moyuyo-api/.../controller/AuthController.java`

**端点设计**:

| 方法 | URL | 功能 | 请求体 | 是否需认证 |
|------|-----|------|--------|:----------:|
| POST | `/api/v1/auth/register` | 注册 | `RegisterRequest` | ❌ |
| POST | `/api/v1/auth/login` | 登录 | `LoginRequest` | ❌ |
| POST | `/api/v1/auth/refresh` | 刷新 Token | `RefreshTokenRequest` | ❌ |
| POST | `/api/v1/auth/logout` | 退出 | Header: Bearer token | ✅ |
| POST | `/api/v1/auth/email/verify` | 发送验证邮件 | `{ email }` | ❌ |
| POST | `/api/v1/auth/email/verify-confirm` | 确认验证码 | `{ email, code }` | ❌ |
| POST | `/api/v1/auth/password/forgot` | 发送重置邮件 | `{ email }` | ❌ |
| POST | `/api/v1/auth/password/reset` | 重置密码 | `{ token, password }` | ❌ |
| POST | `/api/v1/auth/password/change` | 修改密码 | `{ oldPassword, newPassword }` | ✅ |

**验证**: 启动服务，用 Swagger UI 或 Postman 测试每个端点。

***

### 第5步：鉴权过滤器 — JwtAuthFilter

**目标**: 拦截请求，校验 JWT Token，设置用户上下文。

**新建文件**:
- `moyuyo-common/.../security/JwtAuthFilter.java`
- `moyuyo-common/.../security/UserContext.java` (ThreadLocal)
- `moyuyo-common/.../security/CurrentUser.java` (注解)
- `moyuyo-common/.../security/WebSecurityConfig.java`

**过滤器逻辑**:
```
请求 → 提取 Authorization: Bearer <token>
    → 校验 token 格式和签名
    → 检查 Redis 黑名单中是否存在此 token
    → 解析 userId → 设置 UserContext(userId, email)
    → 继续执行 Controller
    → 请求结束 → 清除 UserContext(ThreadLocal)
```

**白名单 URL**（不校验 Token）:
```
/api/v1/auth/**
/api/v3/**
/api/health
/swagger-ui.html
/api-docs/**
```

**验证**: 请求白名单 URL 通过，非白名单无 Token 返回 401。

***

### 第6步：前端切换后端地址

**目标**: 将前端认证 API 调用从 WooCommerce/OAuth 切换到 Spring Boot 后端。

**修改文件**:
- `src/api/user.js` — 替换所有接口地址和调用方式

**变更要点**:
```
注册: POST /wc/v3/customers  → POST /api/v1/auth/register
登录: POST /api/v1/token     → POST /api/v1/auth/login
刷新: POST /api/v1/token     → POST /api/v1/auth/refresh
获取用户: GET /api/v1/userinfo → GET /api/v1/users/me
更新用户: PUT /wc/v3/customers/{id} → PUT /api/v1/users/me
发验证邮件: 无 → POST /api/v1/auth/email/verify
确认验证: 无 → POST /api/v1/auth/email/verify-confirm
重置密码: POST /wp/v2/users/lostpassword → POST /api/v1/auth/password/forgot
```

**修改文件**:
- `src/store/user.js` — 适配新的 API 返回格式和字段名

**验证**: 前端页面登录/注册能正确调用后端接口。

***

### 第7步：前端 2FA/邮箱验证功能补齐

**目标**: 补充 security.vue 中 2FA 开关和邮箱验证功能的后端对接。

**修改文件**:
- `src/api/user.js` — 添加 2FA 相关接口
- `src/store/user.js` — 添加 2FA 状态管理
- `src/pages/user/security.vue` — 对接真实后端

**验证**: 2FA 开关可正常开启/关闭。

***

### 第8步：联调测试

**目标**: 全链路验证用户认证流程。

**测试用例**:

| # | 场景 | 步骤 | 预期结果 |
|:-:|------|------|----------|
| 1 | 注册成功 | 填写邮箱/密码/昵称 → 提交 | 返回 200 + JWT Token |
| 2 | 重复注册 | 用同一邮箱再次注册 | 返回 409 邮箱已存在 |
| 3 | 登录成功 | 正确邮箱密码 | 返回 200 + Token |
| 4 | 登录失败 | 错误密码 | 返回 401 |
| 5 | 刷新 Token | 用 refreshToken 调用 | 返回新的 TokenPair |
| 6 | 无 Token 访问 | 调用需要认证的接口 | 返回 401 |
| 7 | 无效 Token 访问 | 传伪造 Token | 返回 401 |
| 8 | 退出登录 | 调用 logout | 旧 Token 失效（黑名单） |

***

## 三、文件创建/修改清单

### 后端 (14 个文件)

| 操作 | 文件 | 预估时间 |
|:----:|------|:--------:|
| 🔧 | `UserEntity.java` — 补充字段 | 10min |
| 🆕 | `LoginRequest.java` — DTO | 5min |
| 🆕 | `RegisterRequest.java` — DTO | 5min |
| 🆕 | `TokenResponse.java` — DTO | 5min |
| 🆕 | `RefreshTokenRequest.java` — DTO | 5min |
| 🆕 | `AuthService.java` — 接口 | 5min |
| 🆕 | `AuthServiceImpl.java` — 实现 | 2h |
| 🆕 | `AuthController.java` — Controller | 30min |
| 🆕 | `JwtAuthFilter.java` — 过滤器 | 30min |
| 🆕 | `UserContext.java` — ThreadLocal | 5min |
| 🆕 | `UserContextHolder.java` — 上下文持有者 | 5min |
| 🆕 | `CurrentUser.java` — 参数注解 | 5min |
| 🆕 | `WebSecurityConfig.java` — 安全配置 | 15min |
| 🆕 | `UserService.java` + `UserServiceImpl.java` — 用户信息 | 30min |
| 🆕 | `UserController.java` — 用户信息接口 | 15min |
| | **小计** | **~4.5h** |

### 前端 (3 个文件)

| 操作 | 文件 | 预估时间 |
|:----:|------|:--------:|
| 🔧 | `src/api/user.js` — 切换后端地址 | 30min |
| 🔧 | `src/store/user.js` — 适配新格式 | 20min |
| 🔧 | `src/pages/user/security.vue` — 对接后端 | 20min |
| | **小计** | **~1.2h** |

### 总计：约 6h（含联调）

***

## 四、依赖与前提

**不需要前置依赖**：不依赖第三方账号（Stripe/PayPal/Apple 等）、不依赖 RocketMQ。

**需要本地服务运行**：
- ✅ MySQL（`moyuyo_dev` 数据库 + 已迁移 52 张表）
- ✅ Redis（用于 Token 黑名单/验证码存储）
- ✅ RocketMQ（暂不需要，可跳过）

***

## 五、实施原则

1. **每个步骤完成后可验证**：每完成一个步骤都可通过编译 + curl 测试验证
2. **可中途停止**：按编号步骤逐一实施，可在任意步骤后暂停
3. **前后端独立**：第 1-5 步纯后端（前端无需配合），第 6-7 步纯前端
4. **不做过度设计**：不引入 Spring Security（用简单 Filter 替代），不引入 GraphQL，不引入 Event Sourcing
