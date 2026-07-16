<template>
  <div class="system-settings">
    <h2 class="page-title">系统设置</h2>

    <!-- 管理员信息 -->
    <div class="admin-profile card">
      <div class="card-body">
        <div class="profile-content">
          <div class="profile-avatar">A</div>
          <div class="profile-info">
            <div class="profile-name">
              Admin
              <span class="tag tag-blue" style="margin-left: 8px;">超级管理员</span>
            </div>
            <div class="profile-email">admin@moyuyo.com</div>
          </div>
        </div>
      </div>
    </div>

    <div class="two-col">
      <!-- 左栏 -->
      <div class="col-left">
        <!-- 角色管理 -->
        <div class="card">
          <div class="card-header">
            <h3>角色管理</h3>
            <button class="btn btn-sm btn-outline">添加角色</button>
          </div>
          <div class="card-body">
            <div class="role-list">
              <div class="role-item" v-for="role in roles" :key="role.name">
                <div class="role-info">
                  <span class="role-name">{{ role.name }}</span>
                  <span class="role-count">{{ role.count }} 人</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作日志 -->
        <div class="card">
          <div class="card-header">
            <h3>最近操作日志</h3>
          </div>
          <div class="card-body">
            <div class="log-list">
              <div class="log-item" v-for="log in recentLogs" :key="log.time">
                <div class="log-action">{{ log.action }}</div>
                <div class="log-meta">{{ log.operator }} · {{ log.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右栏 -->
      <div class="col-right">
        <!-- 权限开关 -->
        <div class="card">
          <div class="card-header">
            <h3>模块权限</h3>
          </div>
          <div class="card-body">
            <div class="toggle-list">
              <div class="toggle-item" v-for="perm in permissions" :key="perm.key">
                <span>{{ perm.label }}</span>
                <label class="toggle-switch">
                  <input type="checkbox" v-model="perm.enabled" />
                  <span class="toggle-slider"></span>
                </label>
              </div>
            </div>
          </div>
        </div>

        <!-- 安全设置 -->
        <div class="card">
          <div class="card-header">
            <h3>安全设置</h3>
          </div>
          <div class="card-body">
            <div class="security-list">
              <div class="security-item">
                <div class="security-info">
                  <div class="security-label">双因素认证</div>
                  <div class="security-desc">额外安全保护</div>
                </div>
                <span class="tag tag-green">已开启</span>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <div class="security-label">IP白名单</div>
                  <div class="security-desc">限制登录IP地址</div>
                </div>
                <span class="tag tag-gray">未配置</span>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <div class="security-label">会话超时</div>
                  <div class="security-desc">30分钟无操作自动退出</div>
                </div>
                <span class="tag tag-green">已开启</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 系统信息 -->
        <div class="card">
          <div class="card-header">
            <h3>系统信息</h3>
          </div>
          <div class="card-body">
            <div class="sys-info">
              <div class="sys-row"><span>系统版本</span><span>v1.0.0</span></div>
              <div class="sys-row"><span>数据库状态</span><span class="status-ok">正常</span></div>
              <div class="sys-row"><span>缓存状态</span><span class="status-ok">正常</span></div>
              <div class="sys-row"><span>上次备份</span><span>2026-07-08 03:00</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const roles = reactive([
  { name: '超级管理员', count: 2 },
  { name: '运营管理员', count: 5 },
  { name: '客服人员', count: 8 },
  { name: '商品编辑', count: 4 },
  { name: '数据分析师', count: 3 }
])

const recentLogs = reactive([
  { action: '修改了系统配置', operator: 'Admin', time: '10分钟前' },
  { action: '新增运营管理员账号', operator: 'Admin', time: '1小时前' },
  { action: '修改商品分类结构', operator: '运营_张三', time: '2小时前' }
])

const permissions = reactive([
  { key: 'product', label: '商品管理', enabled: true },
  { key: 'order', label: '订单管理', enabled: true },
  { key: 'user', label: '用户管理', enabled: true },
  { key: 'marketing', label: '营销管理', enabled: true },
  { key: 'analytics', label: '数据分析', enabled: true },
  { key: 'settings', label: '系统设置', enabled: false },
  { key: 'finance', label: '财务管理', enabled: false },
  { key: 'review', label: '内容审核', enabled: true }
])
</script>

<style scoped lang="css">
.page-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-800);
  margin: 0 0 20px;
}

/* 管理员信息 */
.admin-profile .profile-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.profile-avatar {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--brand-500), var(--primary));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  flex-shrink: 0;
}

.profile-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-800);
  display: flex;
  align-items: center;
}

.profile-email {
  font-size: 13px;
  color: var(--text-400);
  margin-top: 4px;
}

/* 两栏 */
.col-left,
.col-right {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 角色列表 */
.role-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.role-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: var(--background-100);
  border-radius: var(--radius-sm);
}

.role-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-700);
}

.role-count {
  font-size: 12px;
  color: var(--text-400);
}

/* 操作日志 */
.log-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.log-item {
  padding-bottom: 12px;
  border-bottom: 1px solid var(--background-100);
}

.log-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.log-action {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-700);
  margin-bottom: 2px;
}

.log-meta {
  font-size: 11px;
  color: var(--text-400);
}

/* 开关列表 */
.toggle-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.toggle-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid var(--background-100);
  font-size: 13px;
  color: var(--text-600);
}

.toggle-item:last-child {
  border-bottom: none;
}

/* 开关样式 */
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 22px;
  cursor: pointer;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  inset: 0;
  background: var(--background-200);
  border-radius: 11px;
  transition: all 0.2s ease;
}

.toggle-slider::before {
  content: '';
  position: absolute;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  top: 2px;
  left: 2px;
  background: #fff;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0,0,0,0.15);
}

.toggle-switch input:checked + .toggle-slider {
  background: var(--state-success);
}

.toggle-switch input:checked + .toggle-slider::before {
  transform: translateX(18px);
}

/* 安全设置 */
.security-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid var(--background-100);
}

.security-item:last-child {
  border-bottom: none;
}

.security-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-700);
}

.security-desc {
  font-size: 11px;
  color: var(--text-400);
  margin-top: 2px;
}

/* 系统信息 */
.sys-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sys-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: var(--text-600);
}

.status-ok {
  color: var(--state-success);
  font-weight: 600;
}
</style>
