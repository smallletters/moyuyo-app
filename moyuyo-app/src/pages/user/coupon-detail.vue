<template>
  <view class="coupon-detail-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-btn" @click="goBack">
        <text class="nav-back">‹</text>
      </view>
      <text class="nav-title">优惠券详情</text>
      <view class="nav-btn" />
    </view>

    <!-- 优惠券主视觉卡片 -->
    <view class="coupon-visual">
      <view class="coupon-visual-inner">
        <!-- 装饰圆 -->
        <view class="decor-circle top-right" />
        <view class="decor-circle bottom-left" />
        <!-- 面额展示 -->
        <view class="amount-row">
          <text class="amount-unit">$</text>
          <text class="amount-value">10</text>
          <text class="amount-off">OFF</text>
        </view>
        <text class="coupon-condition">满 $50 可用</text>
        <!-- 有效期 -->
        <view class="validity-row">
          <text class="validity-icon">📅</text>
          <text class="validity-text">2026.07.01 - 2026.07.31</text>
        </view>
        <!-- 券名称 -->
        <view class="coupon-name-section">
          <text class="coupon-name">MOYUYO 新品季满减券</text>
          <text class="coupon-scope">全品类通用 · 限用1张/单</text>
        </view>
        <!-- 锯齿装饰 -->
        <view class="notch-row">
          <view class="notch-circle left" />
          <view class="notch-line" />
          <view class="notch-circle right" />
        </view>
      </view>
    </view>

    <!-- 券编号 -->
    <view class="code-section">
      <view class="code-row">
        <view class="code-left">
          <text class="code-icon">🎟️</text>
          <text class="code-label">券编号</text>
        </view>
        <view class="code-btn" @click="copyCode">
          <text class="code-value">MOYU20260710</text>
          <text class="copy-icon">📋</text>
        </view>
      </view>
    </view>

    <!-- 分割线 -->
    <view class="divider" />

    <!-- 使用规则 -->
    <view class="rules-section">
      <text class="rules-title">使用规则</text>
      <view class="accordion-list">
        <view v-for="(rule, index) in rulesList" :key="index" class="accordion-item">
          <view class="accordion-header" @click="toggleRule(index)">
            <view class="accordion-left">
              <view class="rule-icon-wrap">
                <text class="rule-emoji">{{ rule.icon }}</text>
              </view>
              <text class="rule-title">{{ rule.title }}</text>
            </view>
            <text class="accordion-arrow" :class="{ rotated: rule.expanded }">›</text>
          </view>
          <view v-if="rule.expanded" class="accordion-body">
            <view class="rule-content">
              <view v-if="rule.items" class="rule-items">
                <view v-for="(item, i) in rule.items" :key="i" class="rule-item">
                  <view class="rule-dot" :style="{ background: item.color }" />
                  <text class="rule-item-text">{{ item.text }}</text>
                </view>
              </view>
              <view v-if="rule.content" class="rule-single">
                <text class="rule-emoji-sm">🌍</text>
                <view>
                  <text class="rule-content-title">{{ rule.content.title }}</text>
                  <text class="rule-content-desc">{{ rule.content.desc }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 分割线 -->
    <view class="divider" />

    <!-- 适用商品 -->
    <view class="products-section">
      <view class="products-header">
        <text class="products-title">适用商品</text>
        <view class="view-all" @click="goProductList">
          <text class="view-all-text">查看全部</text>
          <text class="view-all-arrow">›</text>
        </view>
      </view>
      <scroll-view scroll-x class="products-scroll">
        <view class="product-row">
          <view v-for="product in products" :key="product.name" class="product-card">
            <view class="product-img" :style="{ background: product.bg }" />
            <view class="product-info">
              <text class="product-name">{{ product.name }}</text>
              <view class="product-price-row">
                <text class="product-price">{{ product.price }}</text>
                <text class="product-original">{{ product.original }}</text>
              </view>
              <view class="add-cart-btn" @click="addToCart(product)">
                <text class="add-cart-text">加购</text>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 分割线 -->
    <view class="divider" />

    <!-- 使用说明 -->
    <view class="instructions-section">
      <text class="instructions-title">使用说明</text>
      <view class="step-list">
        <view v-for="(step, index) in steps" :key="index" class="step-item">
          <view class="step-num">
            <text class="step-num-text">{{ index + 1 }}</text>
          </view>
          <view class="step-content">
            <text class="step-title">{{ step.title }}</text>
            <text class="step-desc">{{ step.desc }}</text>
          </view>
        </view>
      </view>
      <!-- 注意事项 -->
      <view class="notice-section">
        <view class="notice-header">
          <text class="notice-icon">ℹ️</text>
          <text class="notice-title">注意事项</text>
        </view>
        <view class="notice-list">
          <view v-for="(item, index) in notices" :key="index" class="notice-item">
            <view class="notice-dot" />
            <text class="notice-text">{{ item }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部固定操作栏 -->
    <view class="bottom-bar">
      <view class="use-btn" @click="useCoupon">
        <text class="use-btn-text">🛍️ 立即使用</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 使用规则折叠面板
const rulesList = ref([
  {
    icon: '🏪',
    title: '适用范围',
    expanded: true,
    content: {
      title: '全场通用',
      desc: '适用于 MOYUYO 商城所有在售商品（虚拟商品、Gift Card 及订阅服务除外）',
    },
  },
  {
    icon: '📚',
    title: '叠加规则',
    expanded: false,
    items: [
      { text: '不可与其他满减券叠加使用', color: '#C96E5F' },
      { text: '可与会员折扣叠加使用', color: '#ABB9AD' },
      { text: '每单限用1张此券', color: '#ABB9AD' },
    ],
  },
  {
    icon: '🔄',
    title: '退款规则',
    expanded: false,
    items: [
      { text: '订单全额退款时，优惠券自动退回至您的账户', color: '#007aff' },
      { text: '部分退款时，优惠券不退回', color: '#007aff' },
      { text: '退回的券有效期不变，请在原有效期内使用', color: '#007aff' },
    ],
  },
])

// 适用商品
const products = ref([
  {
    name: '高端宠物洗护套装',
    price: '$29',
    original: '$39',
    bg: 'linear-gradient(135deg, #f2f2f7, #f7f7fa)',
  },
  {
    name: '舒适透气胸背带',
    price: '$24',
    original: '$32',
    bg: 'linear-gradient(135deg, #e8f2ff, #f2f2f7)',
  },
  {
    name: '互动益智玩具',
    price: '$15',
    original: '$22',
    bg: 'linear-gradient(135deg, #e9f9ee, #f2f2f7)',
  },
  {
    name: '宠物潮流外套',
    price: '$38',
    original: '$52',
    bg: 'linear-gradient(135deg, #f7f7fa, #e8f2ff)',
  },
  {
    name: '智能自动喂食器',
    price: '$59',
    original: '$79',
    bg: 'linear-gradient(135deg, #f2f2f7, #f7f7fa)',
  },
])

// 使用步骤
const steps = ref([
  { title: '选购商品', desc: '将符合使用门槛的商品加入购物车' },
  { title: '去结算', desc: '在结算页面确认订单金额' },
  { title: '选择优惠券', desc: '在支付方式上方点击「使用优惠券」，勾选本券' },
  { title: '完成支付', desc: '确认折扣金额后提交订单并支付' },
])

// 注意事项
const notices = ref([
  '请在有效期内使用，过期自动失效',
  '券不可转让、不可兑换现金',
  '如有疑问请联系客服支持',
])

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 复制券编号
const copyCode = () => {
  uni.setClipboardData({
    data: 'MOYU20260710',
    success: () => {
      uni.showToast({ title: '已复制券编号', icon: 'success' })
    },
  })
}

// 切换规则展开
const toggleRule = (index) => {
  rulesList.value[index].expanded = !rulesList.value[index].expanded
}

// 查看全部商品
const goProductList = () => {
  uni.navigateTo({ url: '/pages/goods/list' })
}

// 加购
const addToCart = (product) => {
  uni.showToast({ title: `已加入购物车`, icon: 'success' })
}

// 使用优惠券
const useCoupon = () => {
  uni.navigateTo({ url: '/pages/goods/list' })
}
</script>

<style lang="scss" scoped>
.coupon-detail-page {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 160rpx;
}

// 导航栏
.nav-bar {
  display: flex;
  align-items: center;
  height: 88rpx;
  padding: 0 32rpx;
  background: var(--color-text);
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
  color: #fff;
}
.nav-title {
  flex: 1;
  text-align: center;
  font-size: 34rpx;
  font-weight: 600;
  color: #fff;
}

// 优惠券主视觉
.coupon-visual {
  padding: 24rpx;
}
.coupon-visual-inner {
  border-radius: 24rpx;
  overflow: hidden;
  background: linear-gradient(135deg, var(--color-primary), #004fad);
  padding: 48rpx 40rpx;
  position: relative;
}
.decor-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}
.top-right {
  top: -40rpx;
  right: -40rpx;
  width: 200rpx;
  height: 200rpx;
}
.bottom-left {
  bottom: -48rpx;
  left: -48rpx;
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.06);
}
.amount-row {
  display: flex;
  align-items: baseline;
  gap: 4rpx;
  position: relative;
}
.amount-unit {
  font-size: 40rpx;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.8);
}
.amount-value {
  font-size: 100rpx;
  font-weight: 800;
  color: #fff;
  line-height: 1;
}
.amount-off {
  font-size: 32rpx;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.8);
  margin-left: 8rpx;
}
.coupon-condition {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.75);
  margin-top: 12rpx;
}
.validity-row {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 28rpx;
}
.validity-icon {
  font-size: 26rpx;
}
.validity-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.65);
}
.coupon-name-section {
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.2);
}
.coupon-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #fff;
}
.coupon-scope {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 8rpx;
}
.notch-row {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 36rpx;
  display: flex;
  align-items: center;
}
.notch-circle {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  background: var(--color-background);
}
.notch-circle.left {
  margin-left: -18rpx;
}
.notch-line {
  flex: 1;
  border-bottom: 2rpx dashed rgba(255, 255, 255, 0.3);
}
.notch-circle.right {
  margin-right: -18rpx;
}

// 券编号
.code-section {
  margin: 0 24rpx;
  padding: 24rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
}
.code-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.code-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.code-icon {
  font-size: 28rpx;
}
.code-label {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--color-text-secondary);
}
.code-btn {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 12rpx 24rpx;
  border-radius: 999rpx;
  background: var(--color-background);
}
.code-value {
  font-size: 26rpx;
  font-weight: 600;
  font-family: monospace;
  color: var(--color-text);
  letter-spacing: 4rpx;
}
.copy-icon {
  font-size: 24rpx;
}

// 分割线
.divider {
  height: 2rpx;
  background: var(--color-border);
  margin: 0 24rpx;
}

// 使用规则
.rules-section {
  padding: 24rpx;
}
.rules-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 20rpx;
}
.accordion-item {
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  margin-bottom: 16rpx;
  overflow: hidden;
}
.accordion-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
}
.accordion-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.rule-icon-wrap {
  width: 52rpx;
  height: 52rpx;
  border-radius: 16rpx;
  background: var(--color-background);
  display: flex;
  align-items: center;
  justify-content: center;
}
.rule-emoji {
  font-size: 28rpx;
}
.rule-title {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.accordion-arrow {
  font-size: 32rpx;
  color: var(--color-text-secondary);
  transition: transform 0.3s ease;
}
.accordion-arrow.rotated {
  transform: rotate(180deg);
}
.accordion-body {
  padding: 0 24rpx 24rpx;
}
.rule-content {
  padding: 20rpx;
  border-radius: 16rpx;
  background: var(--color-background);
}
.rule-items {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
.rule-item {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}
.rule-dot {
  width: 12rpx;
  height: 12rpx;
  border-radius: 50%;
  margin-top: 10rpx;
  flex-shrink: 0;
}
.rule-item-text {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}
.rule-single {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}
.rule-emoji-sm {
  font-size: 28rpx;
  margin-top: 4rpx;
}
.rule-content-title {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.rule-content-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}

// 适用商品
.products-section {
  padding: 24rpx;
}
.products-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}
.products-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}
.view-all {
  display: flex;
  align-items: center;
  gap: 4rpx;
}
.view-all-text {
  font-size: 24rpx;
  color: var(--color-primary);
}
.view-all-arrow {
  font-size: 28rpx;
  color: var(--color-primary);
}
.products-scroll {
  white-space: nowrap;
}
.product-row {
  display: inline-flex;
  gap: 20rpx;
}
.product-card {
  width: 260rpx;
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
  background: var(--color-card);
  overflow: hidden;
}
.product-img {
  aspect-ratio: 1;
}
.product-info {
  padding: 16rpx;
}
.product-name {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--color-text);
  white-space: normal;
}
.product-price-row {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-top: 8rpx;
}
.product-price {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-primary);
}
.product-original {
  font-size: 20rpx;
  text-decoration: line-through;
  color: var(--color-text-secondary);
}
.add-cart-btn {
  margin-top: 12rpx;
  height: 56rpx;
  border-radius: 12rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.add-cart-text {
  font-size: 24rpx;
  font-weight: 600;
  color: #fff;
}

// 使用说明
.instructions-section {
  margin: 0 24rpx;
  padding: 24rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  border: 1rpx solid var(--color-border);
}
.instructions-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 20rpx;
}
.step-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}
.step-item {
  display: flex;
  gap: 20rpx;
}
.step-num {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.step-num-text {
  font-size: 24rpx;
  font-weight: 700;
  color: #fff;
}
.step-content {
  padding-top: 4rpx;
}
.step-title {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.step-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

// 注意事项
.notice-section {
  margin-top: 28rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid var(--color-border);
}
.notice-header {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 16rpx;
}
.notice-icon {
  font-size: 26rpx;
}
.notice-title {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-text-secondary);
}
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}
.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
}
.notice-dot {
  width: 8rpx;
  height: 8rpx;
  border-radius: 50%;
  background: var(--color-text-secondary);
  margin-top: 12rpx;
  flex-shrink: 0;
}
.notice-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

// 底部操作栏
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 32rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.92);
  border-top: 1rpx solid var(--color-border);
}
.use-btn {
  height: 96rpx;
  border-radius: 24rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.use-btn-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}
</style>
