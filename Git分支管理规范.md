# Git 分支管理规范

## 一、分支模型

本项目采用 Git Flow 分支管理模型。

### 主要分支

| 分支名称 | 说明 | 保护 |
|---------|------|------|
| `main` | 主分支，生产环境代码 | ✅ 保护 |
| `develop` | 开发分支，集成最新功能 | ✅ 保护 |

### 辅助分支

| 分支类型 | 分支命名 | 说明 | 来源 | 合并目标 |
|---------|---------|------|------|---------|
| 功能分支 | `feature/xxx` | 新功能开发 | `develop` | `develop` |
| 修复分支 | `hotfix/xxx` | 紧急bug修复 | `main` | `main` + `develop` |
| 发布分支 | `release/xxx` | 版本发布准备 | `develop` | `main` + `develop` |

---

## 二、分支详细说明

### 1. main 分支

- **用途**：稳定的生产环境代码
- **命名**：`main`
- **状态**：始终可部署
- **保护规则**：
  - 禁止直接推送
  - 必须通过 Pull Request 合并
  - 需要至少1人审核通过

### 2. develop 分支

- **用途**：集成最新开发功能的分支
- **命名**：`develop`
- **状态**：相对稳定，用于集成测试
- **保护规则**：
  - 禁止直接推送
  - 必须通过 Pull Request 合并

### 3. feature 分支

- **用途**：开发新功能
- **命名规范**：`feature/功能名-描述`
  - 示例：`feature/user-login`、`feature/gacha-analysis`
- **创建流程**：
  ```bash
  # 从 develop 分支创建
  git checkout develop
  git pull origin develop
  git checkout -b feature/xxx
  ```
- **合并流程**：
  - 开发完成后，提交 PR 到 `develop` 分支
  - Code Review 通过后合并
  - 合并后删除该 feature 分支

### 4. hotfix 分支

- **用途**：修复生产环境的紧急 bug
- **命名规范**：`hotfix/问题描述-日期`
  - 示例：`hotfix/fix-login-error-20260524`
- **创建流程**：
  ```bash
  # 从 main 分支创建
  git checkout main
  git pull origin main
  git checkout -b hotfix/xxx
  ```
- **合并流程**：
  - 修复完成后，提交 PR 到 `main` 分支
  - 同时需要将修复合并到 `develop` 分支
  - 合并后删除该 hotfix 分支
  - 需要打版本标签

### 5. release 分支

- **用途**：版本发布前的准备工作
- **命名规范**：`release/版本号`
  - 示例：`release/v1.0.0`
- **创建流程**：
  ```bash
  # 从 develop 分支创建
  git checkout develop
  git pull origin develop
  git checkout -b release/vx.x.x
  ```
- **合并流程**：
  - 测试通过后，先合并到 `main` 分支，打版本标签
  - 然后合并回 `develop` 分支
  - 合并后删除该 release 分支

---

## 三、完整工作流程示例

### 场景 1：开发新功能

```bash
# 1. 更新本地 develop 分支
git checkout develop
git pull origin develop

# 2. 创建功能分支
git checkout -b feature/user-center

# 3. 开发并提交代码（遵循提交规范）
git add .
git commit -m "feat: 添加用户中心功能"

# 4. 推送到远程
git push origin feature/user-center

# 5. 在 GitHub/GitLab 创建 Pull Request
#    目标分支: develop
#    进行 Code Review

# 6. 合并通过后，删除本地和远程分支
git checkout develop
git pull origin develop
git branch -d feature/user-center
git push origin --delete feature/user-center
```

### 场景 2：修复生产环境Bug

```bash
# 1. 更新本地 main 分支
git checkout main
git pull origin main

# 2. 创建 hotfix 分支
git checkout -b hotfix/fix-login-error-20260524

# 3. 修复并提交代码
git add .
git commit -m "fix: 修复登录失败问题"

# 4. 推送到远程
git push origin hotfix/fix-login-error-20260524

# 5. 创建 PR 到 main 分支
#    Code Review 通过后合并

# 6. 打版本标签
git checkout main
git pull origin main
git tag -a v1.0.1 -m "Release v1.0.1 - 修复登录问题"
git push origin v1.0.1

# 7. 将修复同步到 develop 分支
git checkout develop
git pull origin develop
git cherry-pick <hotfix提交的commit hash>
# 或者 git merge main
git push origin develop

# 8. 删除 hotfix 分支
git branch -d hotfix/fix-login-error-20260524
git push origin --delete hotfix/fix-login-error-20260524
```

### 场景 3：版本发布

```bash
# 1. 从 develop 创建 release 分支
git checkout develop
git pull origin develop
git checkout -b release/v1.0.0

# 2. 进行发布前的准备工作
# - 修改版本号
# - 更新变更日志
# - 最终测试
git add .
git commit -m "chore: 准备发布 v1.0.0"

# 3. 推送到远程
git push origin release/v1.0.0

# 4. 测试通过后，合并到 main
git checkout main
git pull origin main
git merge --no-ff release/v1.0.0
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin main
git push origin v1.0.0

# 5. 合并回 develop
git checkout develop
git pull origin develop
git merge --no-ff release/v1.0.0
git push origin develop

# 6. 删除 release 分支
git branch -d release/v1.0.0
git push origin --delete release/v1.0.0
```

---

## 四、Pull Request 规范

### PR 标题格式

```
[类型] 简短描述
```

类型可选值：
- `feat` - 新功能
- `fix` - 修复
- `docs` - 文档
- `style` - 代码格式调整
- `refactor` - 重构
- `test` - 测试相关
- `chore` - 构建/工具相关

示例：
```
[feat] 添加用户登录功能
[fix] 修复抽卡数据查询bug
[docs] 更新API文档
```

### PR 内容要求

- **描述**：清晰说明这个 PR 做了什么
- **关联Issue**：如果有关联的 Issue，需要提到
- **测试**：说明如何验证改动
- **截图**：如果是 UI 改动，需要截图

### Code Review 检查清单

- [ ] 代码符合项目规范
- [ ] 没有引入明显的bug
- [ ] 单元测试覆盖充分
- [ ] 相关文档已更新
- [ ] 命名清晰易懂
- [ ] 注释充分但不过度

---

## 五、版本号规范

采用语义化版本（Semantic Versioning）：`MAJOR.MINOR.PATCH`

- `MAJOR`：不兼容的API修改
- `MINOR`：向下兼容的功能性新增
- `PATCH`：向下兼容的问题修正

示例：
- `v1.0.0` - 首次发布
- `v1.0.1` - 修复bug
- `v1.1.0` - 新增功能
- `v2.0.0` - 重大变更，不兼容

---

## 六、常见问题

### Q1：多个 feature 同时开发怎么办？

A：各自创建独立的 feature 分支，互不影响，合并时有冲突再解决。

### Q2：feature 分支开发时间过长怎么办？

A：定期从 develop 分支 rebase 或 merge，保持分支最新，减少最终合并冲突。

### Q3：可以直接提交到 main 或 develop 吗？

A：不可以！main 和 develop 都是受保护分支，必须通过 PR 合并。
