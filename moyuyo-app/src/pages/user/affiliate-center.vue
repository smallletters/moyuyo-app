<template>
  <view class="affiliate-page">
    <!-- 复制成功提示 -->
    <view class="toast-msg" :class="{ show: toastVisible }">
      <text class="toast-text">已复制到剪贴板</text>
    </view>

    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-btn" @click="goBack">
        <text class="nav-back">‹</text>
      </view>
      <text class="nav-title">推广中心</text>
      <view class="nav-btn" />
    </view>

    <!-- 身份状态卡片 -->
    <view class="identity-card">
      <view class="identity-header">
        <view class="identity-left">
          <text class="identity-icon">👑</text>
          <text class="identity-label">品牌大使</text>
        </view>
        <view class="tier-badge">
          <text class="tier-text">Macro</text>
        </view>
      </view>
      <view class="identity-footer">
        <view class="commission-info">
          <text class="commission-hint">当前佣金率</text>
          <text class="commission-rate">12%</text>
        </view>
        <view class="trend-info">
          <text class="trend-icon">📈</text>
          <text class="trend-text">较上月 +2%</text>
        </view>
      </view>
    </view>

    <!-- 收益概览 -->
    <view class="earnings-card">
      <view class="earnings-header">
        <text class="earnings-title">本月佣金</text>
        <view class="detail-link" @click="goCommissionDetail">
          <text class="detail-text">佣金明细</text>
          <text class="detail-arrow">›</text>
        </view>
      </view>
      <view class="earnings-amount">
        <text class="earnings-unit">¥</text>
        <text class="earnings-value">3,842.50</text>
      </view>
      <view class="earnings-row">
        <view class="earnings-stat">
          <text class="stat-hint">累计佣金</text>
          <text class="stat-val">¥28,650</text>
        </view>
        <view class="earnings-stat">
          <text class="stat-hint">待结算</text>
          <text class="stat-val stat-pending">¥1,260</text>
        </view>
      </view>
      <view class="withdraw-btn" @click="onWithdraw">
        <text class="withdraw-text">💰 立即提现</text>
      </view>
    </view>

    <!-- 推广数据看板 -->
    <view class="section-title">
      <text class="section-title-text">推广数据</text>
    </view>
    <view class="stats-grid">
      <view v-for="stat in statsData" :key="stat.label" class="stat-cell">
        <view class="stat-cell-header">
          <text class="stat-emoji">{{ stat.icon }}</text>
          <text class="stat-cell-label">{{ stat.label }}</text>
        </view>
        <text class="stat-cell-value">{{ stat.value }}</text>
      </view>
    </view>

    <!-- 专属推广码 -->
    <view class="section-title">
      <text class="section-title-text">专属推广码</text>
    </view>
    <view class="promo-card">
      <view class="promo-section">
        <text class="promo-hint">推广码</text>
        <view class="promo-code-box">
          <text class="promo-code-text">MOYU2025X</text>
          <view class="copy-btn" @click="copyText('MOYU2025X')">
            <text class="copy-text">📋 复制</text>
          </view>
        </view>
      </view>
      <view class="promo-section">
        <text class="promo-hint">推广链接</text>
        <view class="promo-code-box">
          <text class="promo-link-text">moyuyo.com/ref/moyu2025x</text>
          <view class="copy-btn" @click="copyText('https://moyuyo.com/ref/moyu2025x')">
            <text class="copy-text">📋 复制</text>
          </view>
        </view>
      </view>
      <text class="promo-hint" style="margin-top: 20rpx">分享到</text>
      <view class="share-row">
        <view
          v-for="share in shareList"
          :key="share.label"
          class="share-btn"
          @click="onShare(share.label)"
        >
          <view class="share-icon-wrap" :style="{ background: share.bg }">
            <text class="share-icon">{{ share.icon }}</text>
          </view>
          <text class="share-label">{{ share.label }}</text>
        </view>
      </view>
    </view>

    <!-- 推广素材库 -->
    <view class="section-title">
      <text class="section-title-text">推广素材库</text>
    </view>
    <view class="material-list">
      <view
        v-for="item in materialList"
        :key="item.title"
        class="material-item"
        @click="onMaterialClick(item)"
      >
        <view class="material-icon-wrap" :style="{ background: item.bg }">
          <text class="material-icon">{{ item.icon }}</text>
        </view>
        <view class="material-info">
          <text class="material-title">{{ item.title }}</text>
          <text class="material-desc">{{ item.desc }}</text>
        </view>
        <text class="material-count">{{ item.count }}</text>
        <text class="material-arrow">›</text>
      </view>
    </view>

    <!-- 排行榜入口 -->
    <view class="leaderboard-card" @click="goLeaderboard">
      <view class="leaderboard-header">
        <view class="leaderboard-left">
          <text class="leaderboard-icon">🏆</text>
          <text class="leaderboard-title">月度推广者排行</text>
        </view>
        <view class="leaderboard-more">
          <text class="more-text">查看全部</text>
          <text class="more-arrow">›</text>
        </view>
      </view>
      <view class="leaderboard-podium">
        <view v-for="player in topPlayers" :key="player.rank" class="podium-item">
          <view
            class="podium-avatar"
            :class="{ 'podium-first': player.rank === 1 }"
            :style="
              player.rank === 1 ? { background: 'linear-gradient(135deg, #ff9500, #e08600)' } : {}
            "
          >
            <text class="podium-rank" :style="player.rank === 1 ? { color: '#fff' } : {}">
              {{ player.rank }}
            </text>
          </view>
          <text class="podium-name">{{ player.name }}</text>
          <text class="podium-amount" :style="player.rank === 1 ? { color: '#ff9500' } : {}">
            {{ player.amount }}
          </text>
        </view>
      </view>
    </view>

    <!-- 升级进度 -->
    <view class="section-title">
      <text class="section-title-text">升级进度</text>
    </view>
    <view class="upgrade-card">
      <view class="upgrade-header">
        <view class="upgrade-left">
          <text class="upgrade-icon">✨</text>
          <text class="upgrade-title">下一等级：Macro Pro</text>
        </view>
        <view class="upgrade-badge">
          <text class="upgrade-badge-text">15%佣金</text>
        </view>
      </view>
      <text class="upgrade-desc">
        距离下一等级还需
        <text class="upgrade-highlight">18 笔订单</text>
        ，当前已累计 82 笔（目标 100 笔）
      </text>
      <view class="progress-track">
        <view class="progress-fill" :style="{ width: '82%' }" />
      </view>
      <view class="progress-info">
        <text class="progress-label">82 / 100 笔订单</text>
        <text class="progress-percent">82%</text>
      </view>
      <!-- 升级条件 -->
      <view class="upgrade-conditions">
        <text class="conditions-title">升级条件一览</text>
        <view v-for="cond in upgradeConditions" :key="cond.label" class="condition-item">
          <view class="condition-left">
            <text class="condition-icon">{{ cond.done ? '✅' : '⭕' }}</text>
            <text class="condition-label">{{ cond.label }}</text>
          </view>
          <text class="condition-status" :style="{ color: cond.statusColor }">
            {{ cond.status }}
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 复制提示状态
const toastVisible = ref(false)

// 推广数据
const statsData = ref([
  { icon: '🖱️', label: '点击量', value: '12,486' },
  { icon: '🛍️', label: '转化订单', value: '386' },
  { icon: '🎯', label: '转化率', value: '3.1%' },
  { icon: '🧾', label: '客单价', value: '¥268' },
])

// 分享列表
const shareList = ref([
  { icon: '📸', label: 'Instagram', bg: 'linear-gradient(135deg, #f09433, #dc2743)' },
  { icon: '🎵', label: 'TikTok', bg: '#1d1d1f' },
  { icon: '▶️', label: 'YouTube', bg: '#ff3b30' },
  { icon: '✍️', label: 'Blog', bg: '#007aff' },
])

// 素材库列表
const materialList = ref([
  {
    icon: '🖼️',
    title: 'Banner 素材',
    desc: '精选活动横幅、品牌海报',
    count: '24 张',
    bg: '#e8f2ff',
  },
  { icon: '🎬', title: '短视频素材', desc: '产品展示、种草视频', count: '12 个', bg: '#e9f9ee' },
  {
    icon: '📦',
    title: '商品实拍图',
    desc: '高清产品图、使用场景图',
    count: '56 张',
    bg: '#fff4e5',
  },
])

// 排行榜前3名
const topPlayers = ref([
  { rank: 2, name: 'PetLover', amount: '¥5,120' },
  { rank: 1, name: 'CatQueen', amount: '¥8,340' },
  { rank: 3, name: 'DogFans', amount: '¥4,680' },
])

// 升级条件
const upgradeConditions = ref([
  { label: '月推广点击 >= 5000', done: true, status: '已达成', statusColor: '#34c759' },
  { label: '累计推广订单 >= 100', done: false, status: '进行中', statusColor: '#ff9500' },
  { label: '连续 3 个月活跃推广', done: false, status: '2/3', statusColor: '#8e8e93' },
])

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 复制文本
const copyText = (text) => {
  uni.setClipboardData({
    data: text,
    success: () => {
      toastVisible.value = true
      setTimeout(() => {
        toastVisible.value = false
      }, 1800)
    },
  })
}

// 跳转佣金明细
const goCommissionDetail = () => {
  uni.navigateTo({ url: '/pages/user/wallet' })
}

// 提现
const onWithdraw = () => {
  uni.showToast({ title: '提现功能开发中', icon: 'none' })
}

// 分享
const onShare = (platform) => {
  uni.showToast({ title: `分享到 ${platform}`, icon: 'none' })
}

// 素材库点击
const onMaterialClick = (item) => {
  uni.showToast({ title: item.title, icon: 'none' })
}

// 排行榜
const goLeaderboard = () => {
  uni.showToast({ title: '排行榜开发中', icon: 'none' })
}
</script>

<style lang="scss" scoped>
.affiliate-page {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 180rpx;
}

// 导航栏
.nav-bar {
  display: flex;
  align-items: center;
  height: 88rpx;
  padding: 0 32rpx;
  background: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
  position: sticky;
  top: 0;
  z-index: 30;
}
.nav-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.nav-back {
  font-size: 40rpx;
  color: var(--color-primary);
}
.nav-title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}

// 身份卡片
.identity-card {
  margin: 24rpx;
  padding: 32rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, var(--color-primary), #c9a84c);
  color: #fff;
  position: relative;
  overflow: hidden;
}
.identity-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}
.identity-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.identity-icon {
  font-size: 36rpx;
}
.identity-label {
  font-size: 28rpx;
  font-weight: 600;
}
.tier-badge {
  padding: 6rpx 20rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.2);
}
.tier-text {
  font-size: 22rpx;
  font-weight: 700;
  color: #fff;
}
.identity-footer {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}
.commission-hint {
  font-size: 24rpx;
  opacity: 0.7;
}
.commission-rate {
  font-size: 48rpx;
  font-weight: 800;
  color: #fff;
}
.trend-info {
  display: flex;
  align-items: center;
  gap: 6rpx;
  opacity: 0.6;
}
.trend-icon {
  font-size: 24rpx;
}
.trend-text {
  font-size: 24rpx;
  color: #fff;
}

// 收益概览
.earnings-card {
  margin: 0 24rpx 24rpx;
  padding: 32rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
}
.earnings-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}
.earnings-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.detail-link {
  display: flex;
  align-items: center;
  gap: 4rpx;
}
.detail-text {
  font-size: 24rpx;
  color: var(--color-primary);
}
.detail-arrow {
  font-size: 28rpx;
  color: var(--color-primary);
}
.earnings-amount {
  margin-bottom: 20rpx;
}
.earnings-unit {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text-secondary);
}
.earnings-value {
  font-size: 56rpx;
  font-weight: 800;
  color: var(--color-text);
}
.earnings-row {
  display: flex;
  gap: 16rpx;
  margin-bottom: 28rpx;
}
.earnings-stat {
  flex: 1;
  padding: 20rpx;
  border-radius: 16rpx;
  background: var(--color-background);
}
.stat-hint {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.stat-val {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--color-text);
}
.stat-pending {
  color: #ff9500;
}
.withdraw-btn {
  height: 96rpx;
  border-radius: 24rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.withdraw-text {
  font-size: 30rpx;
  font-weight: 700;
  color: #fff;
}

// 区域标题
.section-title {
  padding: 0 24rpx;
  margin-bottom: 16rpx;
}
.section-title-text {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

// 推广数据看板
.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16rpx;
  padding: 0 24rpx;
  margin-bottom: 24rpx;
}
.stat-cell {
  background: var(--color-card);
  border-radius: 24rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
}
.stat-cell-header {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 8rpx;
}
.stat-emoji {
  font-size: 28rpx;
}
.stat-cell-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.stat-cell-value {
  font-size: 40rpx;
  font-weight: 700;
  color: var(--color-text);
}

// 专属推广码
.promo-card {
  margin: 0 24rpx 24rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
}
.promo-section {
  margin-bottom: 20rpx;
}
.promo-hint {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-bottom: 12rpx;
}
.promo-code-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--color-background);
  border-radius: 16rpx;
  padding: 20rpx 24rpx;
  border: 1rpx solid var(--color-border);
}
.promo-code-text {
  font-family: monospace;
  font-size: 30rpx;
  font-weight: 700;
  color: var(--color-text);
  letter-spacing: 4rpx;
}
.promo-link-text {
  font-size: 26rpx;
  font-weight: 500;
  color: var(--color-text);
}
.copy-btn {
  display: flex;
  align-items: center;
  gap: 6rpx;
  padding: 12rpx 20rpx;
  border-radius: 999rpx;
  border: 1rpx solid var(--color-border);
  background: var(--color-card);
}
.copy-text {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-primary);
}

// 社交分享
.share-row {
  display: flex;
  gap: 20rpx;
}
.share-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
  padding: 20rpx 12rpx;
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  background: var(--color-card);
}
.share-icon-wrap {
  width: 56rpx;
  height: 56rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.share-icon {
  font-size: 28rpx;
}
.share-label {
  font-size: 22rpx;
  font-weight: 600;
  color: var(--color-text-secondary);
}

// 素材库
.material-list {
  padding: 0 24rpx;
  margin-bottom: 24rpx;
}
.material-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
  margin-bottom: 16rpx;
}
.material-icon-wrap {
  width: 72rpx;
  height: 72rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.material-icon {
  font-size: 36rpx;
}
.material-info {
  flex: 1;
  min-width: 0;
}
.material-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.material-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}
.material-count {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.material-arrow {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

// 排行榜
.leaderboard-card {
  margin: 0 24rpx 24rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
}
.leaderboard-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24rpx;
}
.leaderboard-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.leaderboard-icon {
  font-size: 36rpx;
}
.leaderboard-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.leaderboard-more {
  display: flex;
  align-items: center;
  gap: 4rpx;
}
.more-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.more-arrow {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}
.leaderboard-podium {
  display: flex;
  align-items: center;
  gap: 24rpx;
}
.podium-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}
.podium-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: var(--color-background);
  display: flex;
  align-items: center;
  justify-content: center;
}
.podium-first {
  width: 88rpx;
  height: 88rpx;
}
.podium-rank {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--color-text-secondary);
}
.podium-name {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.podium-amount {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-text);
}

// 升级进度
.upgrade-card {
  margin: 0 24rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
}
.upgrade-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}
.upgrade-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.upgrade-icon {
  font-size: 28rpx;
}
.upgrade-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.upgrade-badge {
  padding: 6rpx 16rpx;
  border-radius: 999rpx;
  background: #e8f2ff;
}
.upgrade-badge-text {
  font-size: 22rpx;
  font-weight: 700;
  color: var(--color-primary);
}
.upgrade-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-bottom: 20rpx;
}
.upgrade-highlight {
  font-weight: 700;
  color: var(--color-primary);
}
.progress-track {
  height: 14rpx;
  border-radius: 999rpx;
  background: var(--color-background);
  overflow: hidden;
  margin-bottom: 16rpx;
}
.progress-fill {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, var(--color-primary), #66abff);
  transition: width 0.4s ease;
}
.progress-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.progress-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.progress-percent {
  font-size: 24rpx;
  font-weight: 700;
  color: var(--color-primary);
}
.upgrade-conditions {
  margin-top: 28rpx;
  padding-top: 28rpx;
  border-top: 1rpx solid var(--color-border);
}
.conditions-title {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-text-secondary);
  margin-bottom: 20rpx;
}
.condition-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}
.condition-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.condition-icon {
  font-size: 28rpx;
}
.condition-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.condition-status {
  font-size: 24rpx;
  font-weight: 600;
}

// 复制提示
.toast-msg {
  position: fixed;
  top: 120rpx;
  left: 50%;
  transform: translateX(-50%) translateY(-40rpx);
  padding: 20rpx 40rpx;
  border-radius: 999rpx;
  background: var(--color-text);
  opacity: 0;
  pointer-events: none;
  transition:
    opacity 0.25s ease,
    transform 0.25s ease;
  z-index: 100;
}
.toast-msg.show {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
}
.toast-text {
  font-size: 26rpx;
  font-weight: 600;
  color: #fff;
}
</style>
