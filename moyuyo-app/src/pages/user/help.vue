<template>
  <view class="help-center">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="header-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">帮助中心</text>
      <view class="header-btn" />
    </view>

    <!-- 头部插图占位 -->
    <view class="hero-section">
      <view class="hero-placeholder">
        <text class="hero-icon">❓</text>
        <text class="hero-text">有什么可以帮助你的？</text>
      </view>
    </view>

    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-field">
        <text class="search-icon">⌕</text>
        <input
          class="search-input"
          type="text"
          placeholder="搜索常见问题"
          placeholder-style="color: var(--color-text-tertiary)"
        >
      </view>
    </view>

    <!-- 快捷操作按钮 2x2 -->
    <view class="quick-actions">
      <view class="action-grid">
        <view class="action-item" @click="onOnlineCS">
          <text class="action-icon">💬</text>
          <text class="action-label">在线客服</text>
        </view>
        <view class="action-item" @click="onPhoneCS">
          <text class="action-icon">📞</text>
          <text class="action-label">电话客服</text>
        </view>
        <view class="action-item" @click="onFeedback">
          <text class="action-icon">✍️</text>
          <text class="action-label">意见反馈</text>
        </view>
        <view class="action-item" @click="onAfterSales">
          <text class="action-icon">🛡️</text>
          <text class="action-label">售后政策</text>
        </view>
      </view>
    </view>

    <!-- FAQ 分类列表 -->
    <view class="faq-section">
      <text class="faq-section-title">常见问题</text>

      <view v-for="category in faqData" :key="category.key" class="faq-category">
        <!-- 分类标题 -->
        <view class="faq-category-header" @click="toggleCategory(category.key)">
          <view class="faq-category-left">
            <text class="faq-category-icon">{{ category.icon }}</text>
            <text class="faq-category-name">{{ category.name }}</text>
          </view>
          <text class="faq-chevron" :class="{ 'chevron-open': category.expanded }">›</text>
        </view>

        <!-- 子项列表 -->
        <view class="faq-items" :class="{ 'items-collapsed': !category.expanded }">
          <view
            v-for="item in category.items"
            :key="item.id"
            class="faq-item"
            :class="{ 'item-active': item.expanded }"
          >
            <view class="faq-item-header" @click="toggleItem(category.key, item.id)">
              <text class="faq-item-title" :class="{ 'title-active': item.expanded }">
                {{ item.question }}
              </text>
              <text class="faq-item-chevron" :class="{ 'chevron-open': item.expanded }">›</text>
            </view>
            <view class="faq-item-answer" :class="{ 'answer-collapsed': !item.expanded }">
              <text class="answer-text">{{ item.answer }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部：没有找到答案 -->
    <view class="footer-section">
      <text class="footer-text">没有找到答案？</text>
      <text class="footer-link" @click="onContactCS">联系客服</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      faqData: [
        {
          key: 'order',
          icon: '📦',
          name: '订单相关',
          expanded: true,
          items: [
            {
              id: 1,
              question: '如何查看订单？',
              answer: '进入"我的"页面，点击"我的订单"即可查看全部历史订单，支持按状态筛选和搜索。',
              expanded: true,
            },
            {
              id: 2,
              question: '如何取消订单？',
              answer: '在订单详情页，未发货的订单可点击"取消订单"，款项将在1-3个工作日内原路退回。',
              expanded: false,
            },
            {
              id: 3,
              question: '订单状态说明',
              answer:
                '待付款、待发货、已发货、已完成、已取消，五种状态分别对应订单流转的不同阶段。',
              expanded: false,
            },
          ],
        },
        {
          key: 'payment',
          icon: '💳',
          name: '支付与退款',
          expanded: false,
          items: [
            {
              id: 1,
              question: '如何申请退款？',
              answer:
                '进入订单详情，点击"申请退款"并填写原因，审核通过后退款将在1-5个工作日内到账。',
              expanded: false,
            },
            {
              id: 2,
              question: '退款时效？',
              answer: '原路退款一般1-5个工作日，不同支付渠道到账时间略有差异，具体以银行通知为准。',
              expanded: false,
            },
            {
              id: 3,
              question: '支付方式说明',
              answer: '目前支持微信支付、支付宝、银行卡支付，后续将开放更多支付渠道。',
              expanded: false,
            },
          ],
        },
        {
          key: 'logistics',
          icon: '🚚',
          name: '物流配送',
          expanded: false,
          items: [
            {
              id: 1,
              question: '查看物流信息？',
              answer: '在订单详情页点击"查看物流"可实时追踪包裹位置和配送进度。',
              expanded: false,
            },
            {
              id: 2,
              question: '配送时效？',
              answer: '一般发货后1-3天送达，偏远地区3-5天，具体以实际物流信息为准。',
              expanded: false,
            },
            {
              id: 3,
              question: '配送范围',
              answer: '支持全国大部分地区配送，港澳台及海外地区暂不支持，结算时将自动校验地址。',
              expanded: false,
            },
          ],
        },
        {
          key: 'account',
          icon: '🔒',
          name: '账号与安全',
          expanded: false,
          items: [
            {
              id: 1,
              question: '修改密码？',
              answer: '前往"我的" > "设置" > "账号安全" > "修改密码"，验证原密码后即可设置新密码。',
              expanded: false,
            },
            {
              id: 2,
              question: '账号注销？',
              answer:
                '在"设置" > "账号安全" > "账号注销"提交申请，注销后数据将永久删除且无法恢复。',
              expanded: false,
            },
            {
              id: 3,
              question: '隐私设置',
              answer: '可在"设置" > "隐私设置"中管理个人信息展示权限、消息通知偏好和数据授权。',
              expanded: false,
            },
          ],
        },
        {
          key: 'member',
          icon: '👑',
          name: '会员与积分',
          expanded: false,
          items: [
            {
              id: 1,
              question: '积分规则？',
              answer: '每消费1元获得1积分，签到、评价、分享也可获取额外积分，积分有效期为12个月。',
              expanded: false,
            },
            {
              id: 2,
              question: '会员等级说明？',
              answer: '会员分为普通、银卡、金卡、钻石四个等级，等级越高享受的折扣和专属权益越多。',
              expanded: false,
            },
            {
              id: 3,
              question: '积分兑换',
              answer:
                '在"积分商城"中可使用积分兑换优惠券、宠物用品和专属礼品，不定期更新兑换商品。',
              expanded: false,
            },
          ],
        },
      ],
    }
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    toggleCategory(key) {
      const cat = this.faqData.find((c) => c.key === key)
      if (cat) {
        cat.expanded = !cat.expanded
      }
    },

    toggleItem(catKey, itemId) {
      const cat = this.faqData.find((c) => c.key === catKey)
      if (!cat) return
      const item = cat.items.find((i) => i.id === itemId)
      if (item) {
        item.expanded = !item.expanded
      }
    },

    onOnlineCS() {
      uni.showToast({ title: '在线客服', icon: 'none' })
    },

    onPhoneCS() {
      uni.showToast({ title: '电话客服', icon: 'none' })
    },

    onFeedback() {
      uni.showToast({ title: '意见反馈', icon: 'none' })
    },

    onAfterSales() {
      uni.showToast({ title: '售后政策', icon: 'none' })
    },

    onContactCS() {
      uni.showToast({ title: '联系客服', icon: 'none' })
    },
  },
}
</script>

<style lang="scss" scoped>
.help-center {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 48rpx;
}

/* 顶部导航栏 */
.header {
  position: sticky;
  top: 0;
  z-index: 30;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 88rpx;
  background: var(--color-surface);
  border-bottom: 1rpx solid var(--color-divider);
}

.header-btn {
  position: absolute;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72rpx;
  height: 72rpx;
}

.header-btn:first-child {
  left: 16rpx;
}

.header-btn:last-child {
  right: 16rpx;
}

.back-icon {
  font-size: 48rpx;
  color: var(--color-text);
  line-height: 1;
}

.header-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  letter-spacing: -0.02em;
}

/* 头部插图占位 */
.hero-section {
  padding: 24rpx 32rpx 16rpx;
}

.hero-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  width: 100%;
  aspect-ratio: 16 / 7;
  border-radius: var(--radius-lg);
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-primary));
}

.hero-icon {
  font-size: 64rpx;
}

.hero-text {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}

/* 搜索栏 */
.search-bar {
  padding: 8rpx 32rpx 16rpx;
}

.search-field {
  display: flex;
  align-items: center;
  gap: 12rpx;
  height: 72rpx;
  padding: 0 24rpx;
  border: 1rpx solid var(--color-divider);
  border-radius: var(--radius-pill);
  background: var(--color-surface);
  box-shadow: var(--shadow-sm);
}

.search-icon {
  font-size: 28rpx;
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  font-size: var(--font-size-base);
  color: var(--color-text);
  background: transparent;
  border: none;
  outline: none;
}

/* 快捷操作 2x2 */
.quick-actions {
  padding: 8rpx 32rpx 24rpx;
}

.action-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 28rpx 16rpx;
  border-radius: var(--radius-lg);
  background: var(--color-surface);
  border: 1rpx solid var(--color-divider);
  box-shadow: var(--shadow-sm);
  transition: transform 0.15s ease;
}

.action-item:active {
  transform: scale(0.97);
}

.action-icon {
  font-size: 40rpx;
}

.action-label {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

/* FAQ 分类列表 */
.faq-section {
  padding: 0 32rpx;
}

.faq-section-title {
  display: block;
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 20rpx;
}

.faq-category {
  margin-bottom: 16rpx;
  border: 1rpx solid var(--color-divider);
  border-radius: var(--radius-lg);
  background: var(--color-surface);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.faq-category-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24rpx;
  height: 80rpx;
  transition: background 0.15s ease;
}

.faq-category-header:active {
  background: var(--color-divider);
}

.faq-category-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.faq-category-icon {
  font-size: 32rpx;
}

.faq-category-name {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}

.faq-chevron {
  font-size: 36rpx;
  color: var(--color-text-tertiary);
  line-height: 1;
  transition: transform 0.24s cubic-bezier(0.3, 0, 0, 1);
}

.faq-chevron.chevron-open {
  transform: rotate(90deg);
}

/* 子项列表 */
.faq-items {
  overflow: hidden;
  transition: max-height 0.32s cubic-bezier(0.3, 0, 0, 1);
  max-height: 2000rpx;
}

.faq-items.items-collapsed {
  max-height: 0;
}

.faq-item {
  border-top: 1rpx solid var(--color-divider);
}

.faq-item-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 24rpx;
  transition: background 0.15s ease;
}

.faq-item-header:active {
  background: var(--color-divider);
}

.faq-item-title {
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  flex: 1;
  min-width: 0;
}

.faq-item-title.title-active {
  color: var(--color-primary-dark);
  font-weight: var(--font-weight-medium);
}

.faq-item-chevron {
  font-size: 32rpx;
  color: var(--color-text-tertiary);
  line-height: 1;
  flex-shrink: 0;
  margin-left: 16rpx;
  transition: transform 0.24s cubic-bezier(0.3, 0, 0, 1);
}

.faq-item-chevron.chevron-open {
  transform: rotate(90deg);
  color: var(--color-primary-dark);
}

.faq-item-answer {
  overflow: hidden;
  transition: max-height 0.28s cubic-bezier(0.3, 0, 0, 1);
  max-height: 400rpx;
}

.faq-item-answer.answer-collapsed {
  max-height: 0;
}

.answer-text {
  display: block;
  padding: 0 24rpx 24rpx;
  font-size: var(--font-size-sm);
  line-height: 1.6;
  color: var(--color-text-tertiary);
}

/* 底部 */
.footer-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  padding: 48rpx 32rpx 64rpx;
}

.footer-text {
  font-size: var(--font-size-base);
  color: var(--color-text-tertiary);
}

.footer-link {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-primary-dark);
  padding: 4rpx 8rpx;
}

.footer-link:active {
  opacity: 0.7;
}
</style>
