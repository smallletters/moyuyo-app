# Git 提交规范

> 项目采用 **Trunk-Based Development** 策略，配合 Conventional Commits 规范，确保提交历史清晰可追溯。

---

## 1. 提交信息格式

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

**强制要求：**
- `type` 和 `description` 为必填
- `scope` 为选填，但推荐填写
- 所有字段使用 **英文**

---

## 2. type 枚举

| type | 含义 | 是否触发版本号 |
|------|------|----------------|
| `feat` | 新功能（feature） | 是（minor） |
| `fix` | 修复 Bug | 是（patch） |
| `docs` | 仅文档变更 | 否 |
| `style` | 代码格式调整（空格、分号等），不影响逻辑 | 否 |
| `refactor` | 重构，既不是新增功能也不是修复 Bug | 否 |
| `perf` | 性能优化 | 否 |
| `test` | 添加或修改测试 | 否 |
| `chore` | 构建过程、辅助工具等杂项变更 | 否 |
| `ci` | CI/CD 配置或脚本变更 | 否 |

---

## 3. scope 定义

| scope | 适用模块 |
|-------|----------|
| `frontend` | 前端项目（H5 / 小程序 / Admin） |
| `backend` | 后端 API / Service 层 |
| `docs` | 文档仓库或项目文档 |
| `deploy` | 部署配置（Docker / Nginx / CI 脚本） |
| `plugin-ios` | iOS 原生插件 |
| `plugin-android` | Android 原生插件 |

**示例：** `feat(frontend): add user avatar upload component`

---

## 4. description 书写原则

- 使用 **英文**
- 采用 **祈使句** 时态（如 `add`、`fix`、`remove`，而非 `added`、`fixed`）
- 首字母 **小写**
- 末尾 **不加句号**
- **不超过 72 个字符**

```
✅ feat(backend): add rate limiter to login endpoint
✅ fix(frontend): resolve layout shift on mobile sidebar
✅ docs: update API reference for v2 endpoints

❌ feat(backend): Added rate limiter to login endpoint（首字母大写 + 过去式）
❌ fix: 修复了移动端侧边栏布局偏移的问题（非英文）
❌ feat(frontend): this is a very long commit message that exceeds the seventy two character limit and should be shortened（超过 72 字符）
```

---

## 5. 好的提交 vs 差的提交

### 好的提交

```
feat(backend): add phone number verification endpoint
fix(frontend): correct typo in password reset email link
refactor(deploy): extract common nginx config to snippet
test(backend): add unit tests for order calculation
```

### 差的提交

```
fix bugs（太模糊，没有具体说明）
update code（无意义）
feat: xxxx（信息不足，缺少 scope）
fix(backend): 修复了登录接口的 bug（description 应为英文）
WIP（工作在进行中的代码不应提交到主分支）
```

---

## 6. 分支命名规范

Trunk-Based Development 下，分支生命周期尽量短（不超过 2 天）。

| 分支类型 | 命名格式 | 示例 |
|----------|----------|------|
| 功能分支 | `feature/<简短描述>` | `feature/user-avatar-upload` |
| Bug 修复 | `bugfix/<简短描述>` | `bugfix/login-400-error` |
| 紧急修复 | `hotfix/<简短描述>` | `hotfix/payment-order-loss` |

**规范要求：**
- 从 `main` 拉出，完成后直接合并回 `main`
- 分支名全小写，单词间用连字符 `-` 分隔
- 描述应体现所做变更，不使用编号

---

## 7. PR 提交规范

### PR 标题格式

与 Commit Message 格式保持一致：

```
<type>(<scope>): <description>
```

### PR Body 模板

```markdown
## 背景

[为什么需要这个变更]

## 变更内容

- [变更点 1]
- [变更点 2]

## 测试验证

- [ ] 本地测试通过
- [ ] 测试用例覆盖

## 关联

Closes #ISSUE_NUMBER
```

### Review 要求

- 至少 **1 人 Review 通过** 后方可合入
- Hotfix 可豁免，但需在合并后 24 小时内补充 Review

---

## 8. 引用 PR 和 Issue

在 Commit Body 或 Footer 中引用：

```
# 引用 Issue
feat(backend): add phone login
Closes #42

# 引用 PR
fix(frontend): correct layout shift
Refs #128

# 关联多个
feat(deploy): add staging environment
Related-to: #45, #67
```

**关键词说明：**
- `Closes #xxx` — 合并后自动关闭对应 Issue
- `Refs #xxx` — 仅关联引用
- `Related-to: #xxx, #yyy` — 关联多个

---

## 9. Husky pre-commit hook 自动检查

项目通过 Husky 在 `pre-commit` 阶段自动执行以下检查：

1. **ESLint** — 代码质量与规范检查
   - 检查不通过则提交失败
   - 暂存区文件发生变更时只检查变更文件

2. **Prettier** — 代码格式化
   - 自动格式化暂存区文件
   - 格式化后自动 `git add`

**绕过检查：** 原则上不允许绕过。如确需跳过（如紧急 hotfix），使用：

```bash
git commit --no-verify -m "hotfix: ..."
```

事后需在 PR 中说明原因。

---

## 10. Breaking change 的标记方式

当提交包含不兼容的 API / 行为变更时，必须在描述中标记：

### 方式一：在 type 后添加 `!`

```
feat!(backend): redesign user API response structure
fix!(frontend): remove deprecated login method
```

### 方式二：在 Footer 中添加 `BREAKING CHANGE`

```
feat(backend): redesign user API response structure

BREAKING CHANGE: The `/api/v1/user` response format has changed.
Old format used `data.user`, new format uses `data.profile`.
```

两种方式可同时使用。Breaking change 会被自动识别为主版本号变更（major version bump）。
