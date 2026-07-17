<template>
  <view class="annual-report">
    <!-- 顶部装饰光晕 -->
    <view class="top-glow" />

    <!-- 顶部年份标题 -->
    <view class="report-header">
      <text class="header-sub">Annual Report</text>
      <text class="header-title">2025 年度报告</text>
      <text class="header-desc">你和宠物的这一年</text>
    </view>

    <!-- 核心数据卡片 -->
    <view class="core-stats-card">
      <view v-for="item in coreStats" :key="item.label" class="stat-item">
        <text class="stat-number" :style="{ color: item.color }">
          {{ item.prefix }}{{ item.value }}
        </text>
        <text class="stat-label">{{ item.label }}</text>
        <text class="stat-sub">{{ item.sub }}</text>
      </view>
    </view>

    <!-- 消费品类分布 -->
    <view class="section">
      <text class="section-title">消费品类分布</text>
      <text class="section-subtitle">看看钱都花在哪儿了</text>
      <view class="donut-card">
        <!-- 环形图占位 -->
        <view class="donut-chart">
          <view class="donut-center">
            <text class="donut-label">全年消费</text>
            <text class="donut-value">4 个品类</text>
          </view>
        </view>
        <!-- 图例 -->
        <view class="legend-list">
          <view v-for="item in categories" :key="item.name" class="legend-item">
            <view class="legend-left">
              <view class="legend-dot" :style="{ background: item.color }" />
              <text class="legend-name">{{ item.name }}</text>
              <text class="legend-ip">{{ item.ip }}</text>
            </view>
            <view class="legend-right">
              <text class="legend-amount">¥{{ item.amount }}</text>
              <text class="legend-percent">{{ item.percent }}%</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 最爱 IP 统计 -->
    <view class="section">
      <text class="section-title">最爱 IP 排名</text>
      <text class="section-subtitle">你购买最多的联名款 IP</text>
      <view class="ip-rank-card">
        <view v-for="(item, index) in ipRanking" :key="item.name" class="ip-rank-item">
          <view class="rank-medal" :style="{ background: item.surfaceBg, color: item.color }">
            <text>{{ index + 1 }}</text>
          </view>
          <view class="rank-info">
            <view class="rank-header">
              <text class="rank-name">{{ item.name }}</text>
              <text class="rank-count">{{ item.count }} 件</text>
            </view>
            <view class="rank-bar">
              <view
                class="rank-fill"
                :style="{ width: item.percent + '%', background: item.color }"
              />
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 宠物成长回顾 -->
    <view class="section">
      <text class="section-title">宠物成长回顾</text>
      <text class="section-subtitle">记录毛孩子的健康足迹</text>
      <!-- 宠物信息卡 -->
      <view class="growth-card">
        <view class="growth-header">
          <view class="growth-avatar">
            <text class="growth-avatar-icon">🐕</text>
          </view>
          <view class="growth-info">
            <view class="growth-name-row">
              <text class="growth-name">团子</text>
              <view class="growth-breed">
                <text class="growth-breed-text">金毛</text>
              </view>
            </view>
            <text class="growth-meta">2 岁 · 男孩</text>
          </view>
        </view>
        <!-- 体重变化 -->
        <view class="weight-row">
          <view class="weight-item">
            <text class="weight-label">年初体重</text>
            <text class="weight-value">28.5 kg</text>
          </view>
          <view class="weight-arrow">
            <text class="arrow-icon">→</text>
            <view class="weight-change">
              <text class="change-icon">📈</text>
              <text class="change-text">+1.2 kg</text>
            </view>
          </view>
          <view class="weight-item">
            <text class="weight-label">年末体重</text>
            <text class="weight-value">29.7 kg</text>
          </view>
        </view>
        <!-- 护理统计 -->
        <view class="care-stats">
          <view v-for="item in careStats" :key="item.label" class="care-stat">
            <text class="care-icon">{{ item.icon }}</text>
            <text class="care-value">{{ item.value }}</text>
            <text class="care-label">{{ item.label }}</text>
          </view>
        </view>
      </view>
      <!-- 日记数量卡 -->
      <view class="diary-card">
        <view class="diary-icon-wrap">
          <text class="diary-icon">📖</text>
        </view>
        <view class="diary-info">
          <text class="diary-title">成长日记</text>
          <text class="diary-desc">记录了 86 个精彩瞬间</text>
        </view>
        <text class="diary-count">86</text>
      </view>
    </view>

    <!-- 月度消费趋势 -->
    <view class="section">
      <text class="section-title">月度消费趋势</text>
      <text class="section-subtitle">12 个月的消费节奏</text>
      <view class="bar-chart-card">
        <view class="bar-chart">
          <view v-for="item in monthlyData" :key="item.month" class="bar-col">
            <view class="bar-fill" :style="{ height: item.value + '%' }" />
            <text class="bar-month">{{ item.month }}</text>
          </view>
        </view>
        <view class="bar-footer">
          <text class="bar-hint">ℹ️ 月均消费 ¥720 · 最高月份 11月 (¥1,320)</text>
        </view>
      </view>
    </view>

    <!-- 底部操作区 -->
    <view class="bottom-section">
      <view class="share-btn primary" @click="onShare">
        <text>📤 分享我的年度报告</text>
      </view>
      <view class="share-btn secondary" @click="onViewDetail">
        <text>📄 查看详细报告</text>
      </view>
      <view class="brand-mark">
        <text class="brand-icon">🐾</text>
        <text class="brand-text">MOYUYO PET</text>
      </view>
      <text class="brand-date">2025 年度消费账单 · 数据截至 12 月 31 日</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 核心数据
const coreStats = [
  {
    label: '年度消费',
    value: '8,642',
    prefix: '¥',
    color: 'var(--color-primary)',
    sub: '较去年 +12%',
  },
  { label: '累计订单', value: '67', prefix: '', color: '#f5f5f7', sub: '平均每月 5.6 单' },
  {
    label: '累计节省',
    value: '1,280',
    prefix: '¥',
    color: 'var(--color-success)',
    sub: '会员 + 优惠券',
  },
]

// 消费品类
const categories = [
  { name: 'GEAR 出行装备', ip: 'MILO 蓝系', amount: '3,450', percent: 35, color: '#007aff' },
  { name: 'CARE 洗护护理', ip: 'LUNA 粉系', amount: '2,430', percent: 25, color: '#ff2d55' },
  { name: 'PLAY 玩具娱乐', ip: 'ATLAS 绿系', amount: '2,088', percent: 20, color: '#34c759' },
  { name: 'HOME 家居时尚', ip: 'OLIVE 暖系', amount: '1,764', percent: 20, color: '#ff9500' },
]

// IP 排名
const ipRanking = [
  {
    name: 'MILO 蓝系',
    count: 28,
    percent: 100,
    color: '#007aff',
    surfaceBg: 'rgba(0, 122, 255, 0.12)',
  },
  {
    name: 'LUNA 粉系',
    count: 21,
    percent: 75,
    color: '#ff2d55',
    surfaceBg: 'rgba(255, 45, 85, 0.12)',
  },
  {
    name: 'ATLAS 绿系',
    count: 12,
    percent: 43,
    color: '#34c759',
    surfaceBg: 'rgba(52, 199, 89, 0.12)',
  },
  {
    name: 'OLIVE 暖系',
    count: 6,
    percent: 21,
    color: '#ff9500',
    surfaceBg: 'rgba(255, 149, 0, 0.12)',
  },
]

// 护理统计
const careStats = [
  { icon: '💧', value: '24', label: '洗澡' },
  { icon: '💉', value: '3', label: '疫苗' },
  { icon: '🐛', value: '12', label: '驱虫' },
  { icon: '🩺', value: '4', label: '体检' },
]

// 月度消费数据
const monthlyData = [
  { month: '1月', value: 45 },
  { month: '2月', value: 30 },
  { month: '3月', value: 60 },
  { month: '4月', value: 72 },
  { month: '5月', value: 55 },
  { month: '6月', value: 90 },
  { month: '7月', value: 85 },
  { month: '8月', value: 65 },
  { month: '9月', value: 50 },
  { month: '10月', value: 78 },
  { month: '11月', value: 100 },
  { month: '12月', value: 68 },
]

// 操作
const onShare = () => uni.showToast({ title: '分享年度报告', icon: 'none' })
const onViewDetail = () => uni.showToast({ title: '查看详细报告', icon: 'none' })
</script>

<style lang="scss" scoped>
.annual-report {
  min-height: 100vh;
  background: linear-gradient(170deg, #000 0%, #1c1c1e 50%, #3a3a3c 100%);
  color: #f5f5f7;
  position: relative;
  padding: 40rpx 32rpx 128rpx;
  overflow: hidden;
}

.top-glow {
  position: absolute;
  top: -240rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 800rpx;
  height: 800rpx;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(219, 201, 138, 0.15) 0%, transparent 70%);
  pointer-events: none;
}

/* 顶部标题 */
.report-header {
  text-align: center;
  padding: 32rpx 0 48rpx;
  position: relative;
}
.header-sub {
  font-size: 24rpx;
  font-weight: var(--font-weight-semibold);
  letter-spacing: 4rpx;
  color: #dbc98a;
  display: block;
  margin-bottom: 16rpx;
}
.header-title {
  font-size: 56rpx;
  font-weight: 800;
  color: #f5f5f7;
  display: block;
}
.header-desc {
  font-size: 28rpx;
  color: #8e8e93;
  display: block;
  margin-top: 16rpx;
}

/* 核心数据 */
.core-stats-card {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 40rpx;
  margin-bottom: 40rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
}

.core-stats-card .stat-item {
  text-align: center;
}
.stat-number {
  font-size: 64rpx;
  font-weight: 800;
  display: block;
  line-height: 1;
}
.stat-label {
  font-size: 26rpx;
  color: #8e8e93;
  display: block;
  margin-top: 8rpx;
}
.stat-sub {
  font-size: 22rpx;
  color: #6e6e73;
  display: block;
  margin-top: 4rpx;
}

/* section 通用 */
.section {
  margin-bottom: 40rpx;
}
.section-title {
  font-size: 36rpx;
  font-weight: 700;
  color: #f5f5f7;
  display: block;
  margin-bottom: 8rpx;
}
.section-subtitle {
  font-size: 26rpx;
  color: #8e8e93;
  display: block;
  margin-bottom: 32rpx;
}

/* 环形图卡片 */
.donut-card {
  padding: 40rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
}

.donut-chart {
  width: 360rpx;
  height: 360rpx;
  border-radius: 50%;
  margin: 0 auto 40rpx;
  background: conic-gradient(
    #007aff 0deg 126deg,
    #ff2d55 126deg 216deg,
    #34c759 216deg 288deg,
    #ff9500 288deg 360deg
  );
  position: relative;
}

.donut-chart::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 220rpx;
  height: 220rpx;
  border-radius: 50%;
  background: #1c1c1e;
}

.donut-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
  text-align: center;
}

.donut-label {
  font-size: 22rpx;
  color: #8e8e93;
  display: block;
}
.donut-value {
  font-size: 44rpx;
  font-weight: 800;
  color: #f5f5f7;
  display: block;
  line-height: 1.2;
}

/* 图例 */
.legend-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}
.legend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.legend-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.legend-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  flex-shrink: 0;
}
.legend-name {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: #f5f5f7;
}
.legend-ip {
  font-size: 24rpx;
  color: #8e8e93;
  margin-left: 8rpx;
}
.legend-right {
  text-align: right;
}
.legend-amount {
  font-size: 28rpx;
  font-weight: var(--font-weight-semibold);
  color: #f5f5f7;
}
.legend-percent {
  font-size: 24rpx;
  color: #8e8e93;
  margin-left: 8rpx;
}

/* IP 排名 */
.ip-rank-card {
  padding: 40rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.ip-rank-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.rank-medal {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 800;
  flex-shrink: 0;
}

.rank-info {
  flex: 1;
  min-width: 0;
}
.rank-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}
.rank-name {
  font-size: 28rpx;
  font-weight: var(--font-weight-semibold);
  color: #f5f5f7;
}
.rank-count {
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
  color: #8e8e93;
}
.rank-bar {
  height: 12rpx;
  border-radius: 6rpx;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.06);
}
.rank-fill {
  height: 100%;
  border-radius: 6rpx;
}

/* 宠物成长 */
.growth-card {
  padding: 40rpx;
  margin-bottom: 24rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
}

.growth-header {
  display: flex;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 32rpx;
}
.growth-avatar {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  background: rgba(0, 122, 255, 0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.growth-avatar-icon {
  font-size: 56rpx;
}
.growth-name {
  font-size: 36rpx;
  font-weight: 700;
  color: #f5f5f7;
}
.growth-breed {
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  background: rgba(0, 122, 255, 0.12);
  margin-left: 16rpx;
}
.growth-breed-text {
  font-size: 24rpx;
  color: #007aff;
}
.growth-meta {
  font-size: 24rpx;
  color: #8e8e93;
  margin-top: 8rpx;
  display: block;
}

.weight-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
  border-radius: 28rpx;
  margin-bottom: 32rpx;
  background: rgba(255, 255, 255, 0.04);
}

.weight-item {
  text-align: center;
  flex: 1;
}
.weight-label {
  font-size: 24rpx;
  color: #8e8e93;
  display: block;
}
.weight-value {
  font-size: 36rpx;
  font-weight: 700;
  color: #f5f5f7;
  display: block;
  margin-top: 8rpx;
}
.weight-arrow {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 32rpx;
}
.arrow-icon {
  font-size: 40rpx;
  color: #8e8e93;
}
.weight-change {
  display: flex;
  align-items: center;
  gap: 4rpx;
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  background: rgba(52, 199, 89, 0.15);
  margin-top: 8rpx;
}
.change-icon {
  font-size: 20rpx;
}
.change-text {
  font-size: 24rpx;
  font-weight: 600;
  color: #34c759;
}

.care-stats {
  display: flex;
  gap: 16rpx;
}
.care-stat {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4rpx;
}
.care-icon {
  font-size: 40rpx;
  margin-bottom: 4rpx;
}
.care-value {
  font-size: 36rpx;
  font-weight: 700;
  color: #f5f5f7;
}
.care-label {
  font-size: 22rpx;
  color: #8e8e93;
}

/* 日记卡片 */
.diary-card {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 32rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
}

.diary-icon-wrap {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  background: rgba(255, 45, 85, 0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.diary-icon {
  font-size: 40rpx;
}
.diary-info {
  flex: 1;
}
.diary-title {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: #f5f5f7;
  display: block;
}
.diary-desc {
  font-size: 24rpx;
  color: #8e8e93;
  display: block;
  margin-top: 4rpx;
}
.diary-count {
  font-size: 40rpx;
  font-weight: 700;
  color: #ff2d55;
}

/* 柱状图 */
.bar-chart-card {
  padding: 40rpx;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1rpx solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md, 24rpx);
}

.bar-chart {
  display: flex;
  align-items: flex-end;
  gap: 12rpx;
  height: 240rpx;
}

.bar-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  height: 100%;
  justify-content: flex-end;
}

.bar-fill {
  width: 100%;
  max-width: 44rpx;
  border-radius: 8rpx 8rpx 4rpx 4rpx;
  background: linear-gradient(180deg, #dbc98a, #b5a66e);
  min-height: 8rpx;
}

.bar-month {
  font-size: 20rpx;
  color: #8e8e93;
  white-space: nowrap;
}

.bar-footer {
  margin-top: 32rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.08);
  text-align: center;
}

.bar-hint {
  font-size: 24rpx;
  color: #8e8e93;
}

/* 底部操作 */
.bottom-section {
  padding-top: 16rpx;
  text-align: center;
}

.share-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 96rpx;
  border-radius: 999rpx;
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 20rpx;
}

.share-btn.primary {
  background: var(--color-primary);
  color: #fff;
}
.share-btn.secondary {
  background: rgba(255, 255, 255, 0.06);
  color: #f5f5f7;
  border: 1rpx solid rgba(255, 255, 255, 0.08);
}

.brand-mark {
  margin-top: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
}
.brand-icon {
  font-size: 32rpx;
  opacity: 0.6;
}
.brand-text {
  font-size: 24rpx;
  font-weight: var(--font-weight-semibold);
  color: #8e8e93;
  letter-spacing: 2rpx;
  opacity: 0.6;
}
.brand-date {
  font-size: 22rpx;
  color: #6e6e73;
  display: block;
  margin-top: 8rpx;
  opacity: 0.4;
}
</style>
