<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <span class="sidebar-logo">MOYUYO Admin</span>
      </div>

      <nav class="sidebar-nav">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          active-class="nav-item--active"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <div class="admin-info">
          <div class="admin-avatar-small">A</div>
          <div class="admin-detail">
            <span class="admin-name">Admin</span>
            <span class="admin-role">超级管理员</span>
          </div>
        </div>
        <button class="logout-btn" @click="handleLogout">
          退出登录
        </button>
      </div>
    </aside>

    <!-- 主区域 -->
    <div class="admin-main">
      <!-- 顶栏 -->
      <header class="admin-topbar">
        <div class="topbar-left">
          <span class="breadcrumb">首页</span>
          <span class="breadcrumb-sep">/</span>
          <span class="breadcrumb breadcrumb--current">{{ currentPage }}</span>
        </div>
        <div class="topbar-right">
          <button class="notif-btn" @click="handleNotif">
            <span class="notif-icon">🔔</span>
            <span class="notif-badge">3</span>
          </button>
          <div class="topbar-avatar">A</div>
        </div>
      </header>

      <!-- 内容区 -->
      <div class="admin-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const navItems = [
  { path: '/dashboard', icon: '📊', label: '仪表盘' },
  { path: '/products', icon: '📦', label: '商品管理' },
  { path: '/orders', icon: '🛒', label: '订单管理' },
  { path: '/refund', icon: '💰', label: '退款管理' },
  { path: '/users', icon: '👥', label: '用户管理' },
  { path: '/marketing', icon: '📢', label: '营销管理' },
  { path: '/reviews', icon: '📝', label: '内容审核' },
  { path: '/cs', icon: '🎧', label: '客服管理' },
  { path: '/analytics', icon: '📈', label: '数据分析' },
  { path: '/logistics', icon: '🚚', label: '物流管理' },
  { path: '/settings', icon: '⚙️', label: '系统设置' }
]

const pageTitleMap = {
  '/dashboard': '仪表盘',
  '/products': '商品管理',
  '/orders': '订单管理',
  '/users': '用户管理',
  '/marketing': '营销管理',
  '/reviews': '内容审核',
  '/cs': '客服管理',
  '/analytics': '数据分析',
  '/logistics': '物流管理',
  '/settings': '系统设置'
}

const currentPage = computed(() => {
  return pageTitleMap[route.path] || '未知页面'
})

function handleLogout() {
  router.push('/login')
}

function handleNotif() {
  console.log('notifications')
}
</script>

<style scoped lang="css">
.admin-layout {
  display: flex;
  height: 100vh;
  background: #f5f5f7;
}

/* 侧边栏 */
.admin-sidebar {
  width: 240px;
  min-width: 240px;
  background: #1d1d1f;
  color: #fff;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar-header {
  padding: 20px 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.sidebar-logo {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.5px;
  color: #fff;
}

.sidebar-nav {
  flex: 1;
  padding: 12px 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 24px;
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.05);
}

.nav-item--active {
  color: #fff;
  background: rgba(37, 99, 235, 0.2);
  border-left-color: #2563eb;
}

.nav-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

.nav-label {
  font-size: 14px;
}

.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.admin-avatar-small {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #2563eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
}

.admin-detail {
  display: flex;
  flex-direction: column;
}

.admin-name {
  font-size: 13px;
  font-weight: 600;
  color: #fff;
}

.admin-role {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.45);
}

.logout-btn {
  padding: 8px;
  background: rgba(255, 255, 255, 0.08);
  border: none;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
  cursor: pointer;
  transition: background 0.2s;
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.3);
  color: #fff;
}

/* 主区域 */
.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶栏 */
.admin-topbar {
  height: 56px;
  min-height: 56px;
  background: #fff;
  border-bottom: 1px solid #e5e5ea;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
}

.topbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb {
  font-size: 14px;
  color: #8e8e93;
}

.breadcrumb--current {
  color: #1d1d1f;
  font-weight: 500;
}

.breadcrumb-sep {
  color: #aeaeb2;
  font-size: 12px;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notif-btn {
  position: relative;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.notif-icon {
  font-size: 20px;
}

.notif-badge {
  position: absolute;
  top: -2px;
  right: -4px;
  background: #ef4444;
  color: #fff;
  font-size: 10px;
  padding: 1px 5px;
  border-radius: 10px;
  font-weight: 600;
}

.topbar-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #2563eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
}

/* 内容区 */
.admin-content {
  flex: 1;
  overflow-y: auto;
  padding: 28px;
}
</style>
