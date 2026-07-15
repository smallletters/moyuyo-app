# API 文档编写规范

## 1. SpringDoc / OpenAPI 注解使用规范

### 1.1 @Tag — 模块分类

每个 Controller 类必须使用 `@Tag` 注解，标注该模块的名称和描述。

```java
@Tag(name = "用户管理", description = "用户注册、登录、信息管理相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
}
```

- `name` 必填，使用简洁的中文模块名
- `description` 可选，简要说明该模块包含的功能范围

### 1.2 @Operation — 接口说明

每个公开接口方法必须使用 `@Operation` 注解。

```java
@Operation(
    summary = "获取用户信息",
    description = "根据用户 ID 获取用户基本信息，包括昵称、头像、邮箱等"
)
@GetMapping("/{id}")
public Result<UserVO> getUser(@PathVariable Long id) { ... }
```

- `summary` 必填，简要描述接口功能（20 字以内）
- `description` 可选，详细说明接口行为、注意事项
- `deprecated` — 接口废弃时设置为 `true`，并提供替代接口说明

### 1.3 @Parameter — 参数说明

用于对接口参数进行详细描述，尤其适用于路径参数和查询参数。

```java
@Operation(summary = "查询用户列表")
@GetMapping
public Result<PageResult<UserVO>> listUsers(
    @Parameter(description = "页码，从 1 开始", required = true)
    @RequestParam(defaultValue = "1") Integer page,
    @Parameter(description = "每页条数，最大 100", required = true)
    @RequestParam(defaultValue = "20") @Max(100) Integer pageSize,
    @Parameter(description = "用户名关键字模糊搜索")
    @RequestParam(required = false) String keyword
) { ... }
```

字段说明：
- `description` — 参数含义说明
- `required` — 是否必传
- `example` — 提供示例值
- `hidden` — 隐藏参数（如内部标记）

### 1.4 @Schema — 模型字段说明

用于请求体 DTO 和响应 VO 中的字段描述。

```java
@Data
@Schema(description = "用户注册请求参数")
public class UserRegisterDTO {

    @NotBlank
    @Schema(description = "用户名", example = "zhangsan", minLength = 4, maxLength = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 32)
    @Schema(description = "登录密码", example = "********", writeOnly = true)
    private String password;

    @NotBlank
    @Email
    @Schema(description = "电子邮箱", example = "user@example.com")
    private String email;
}
```

```java
@Data
@Schema(description = "用户信息响应")
public class UserVO {

    @Schema(description = "用户 ID", example = "10001")
    private Long id;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "头像 URL", example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private LocalDateTime createTime;
}
```

字段说明：
- `description` — 字段含义，必须填写
- `example` — 示例值，便于前端理解数据类型和格式
- `required` — 是否必填（配合 `@NotNull` 等注解使用）
- `readOnly` / `writeOnly` — 只读（如 ID）/ 只写（如密码）
- `hidden` — 隐藏内部字段
- `minLength` / `maxLength`、`minimum` / `maximum` — 限制说明

---

## 2. 请求体与响应体规范

### 2.1 请求体（DTO）

- 所有请求参数必须封装在 DTO 中，禁止使用 `Map<String, Object>`
- DTO 类名后缀统一为 `DTO` 或 `Command`
- 字段必须使用 `@Schema` 描述含义
- 传入时间字段统一使用 `LocalDateTime`，格式为 `yyyy-MM-dd HH:mm:ss`
- DTO 中不包含无法被前端传递的字段（如 `createTime`）

### 2.2 响应体（VO）

- 所有响应统一使用 `Result<T>` 泛型包装
- VO 类名后缀统一为 `VO`
- 只返回前端需要的字段，不返回敏感信息（密码、Token 等）
- 分页查询返回 `PageResult<T>`，包含 `total`、`pages`、`list` 等字段
- 字段类型尽量使用 String 或基本类型包装类（避免前端解析问题）

---

## 3. API 分组规范

按模块功能将 API 分为三类，通过 `@Tag` 分类展示：

| 分组 | 路径前缀 | 说明 | 示例 |
|------|----------|------|------|
| **Public** | `/api/public/**` | 无需登录即可访问 | 验证码、公开配置、首页数据 |
| **Internal** | `/api/internal/**` | 仅内部服务调用（IP 白名单） | 定时任务回调、服务间通信 |
| **Admin** | `/api/admin/**` | 后台管理接口，需要管理员权限 | 用户管理、配置管理、数据统计 |
| **User** | `/api/**` | 普通用户接口，需要登录 | 个人信息、业务操作 |

如果一个 Controller 包含多个分组的接口，拆分为多个 Controller 文件，分别标注 `@Tag`。

---

## 4. 接口文档必需字段

每个接口的 API 文档必须包含以下内容：

| 字段 | 说明 | 来源 |
|------|------|------|
| 请求方法 | GET / POST / PUT / DELETE / PATCH | `@RequestMapping` 派生 |
| 请求 URL | 完整路径，含路径变量 | `@RequestMapping` 派生 |
| 请求参数说明 | 每个参数的名称、类型、是否必填、说明 | `@Parameter` + `@Schema` |
| 请求体示例 | JSON 格式示例 | `@Schema(example=...)` 自动生成 |
| 响应示例 | 成功/失败响应的 JSON 示例 | `@Schema(example=...)` 自动生成 |
| 错误码说明 | 可能返回的业务错误码及含义 | `@Operation(description=...)` 中补充 |

### 错误码规范

```java
@Operation(
    summary = "用户登录",
    description = """
        登录成功返回 Token。
        错误码：
        - 1001: 用户名或密码错误
        - 1002: 账号已被禁用
        - 1003: 验证码错误或过期
        - 1004: 登录频率过高，请稍后重试
        """
)
@PostMapping("/login")
public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) { ... }
```

错误码建议在 `@Operation` 的 `description` 中以列表形式说明，或在独立的错误码枚举中统一维护并在文档中引用。

---

## 5. 其他规范

- **统一响应**：所有接口返回 `Result<T>`，不使用 `ResponseEntity` 直接返回
- **分页参数**：统一使用 `page`（从 1 开始）和 `pageSize`，通过 `PageParam` 基类封装
- **数据脱敏**：响应 VO 中敏感字段（手机号、邮箱）使用 `@JsonSerialize` 或 `@JsonProperty` 脱敏
- **枚举字段**：枚举类型返回 code/name/description 结构，或使用 `@JsonValue` 指定序列化方式
- **日期格式**：统一使用 `yyyy-MM-dd HH:mm:ss`，通过 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")` 指定
