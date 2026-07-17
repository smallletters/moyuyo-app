<template>
  <view class="dm-page">
    <!-- 导航栏 -->
    <view class="dm-header">
      <view class="nav-bar">
        <view class="nav-back" @tap="goBack">
          <text class="back-icon">‹</text>
          <text class="back-text">社区</text>
        </view>
        <text class="nav-title">{{ targetUser.name }}</text>
        <view class="nav-more" @tap="showMoreOptions">
          <text class="more-icon">⋯</text>
        </view>
      </view>
    </view>

    <!-- 聊天主体区域 -->
    <scroll-view
      class="dm-body"
      scroll-y
      :scroll-into-view="scrollToId"
      :scroll-with-animation="true"
    >
      <!-- 对方信息卡片（可折叠） -->
      <view class="profile-card">
        <view class="profile-toggle" @tap="toggleProfile">
          <view class="profile-left">
            <view class="avatar-circle">
              <text class="avatar-letter">{{ targetUser.avatar }}</text>
            </view>
            <view class="profile-info">
              <text class="profile-name">{{ targetUser.name }}</text>
              <view class="follow-tag">
                <text class="follow-tag-text">已关注</text>
              </view>
            </view>
          </view>
          <text class="chevron" :class="{ rotated: profileCollapsed }">‹</text>
        </view>
        <!-- 可折叠内容 -->
        <view class="profile-card-body" :class="{ collapsed: profileCollapsed }">
          <view class="view-profile-btn" @tap="viewProfile">
            <text class="view-profile-text">查看主页</text>
          </view>
        </view>
      </view>

      <!-- 消息列表 -->
      <view class="message-list">
        <!-- 时间戳分隔 -->
        <view class="time-divider">
          <text class="time-text">今天 14:30</text>
        </view>

        <!-- 消息项 -->
        <view
          v-for="(msg, index) in messages"
          :id="`msg-${index}`"
          :key="index"
          class="message-item"
          :class="{ 'message-self': msg.isSelf, 'message-remote': !msg.isSelf }"
        >
          <!-- 对方头像 -->
          <view v-if="!msg.isSelf" class="msg-avatar">
            <text class="avatar-letter-sm">{{ targetUser.avatar }}</text>
          </view>

          <!-- 商品卡片消息 -->
          <view v-if="msg.type === 'product'" class="product-card">
            <view class="product-image">
              <text class="product-emoji">🧴</text>
            </view>
            <view class="product-info">
              <text class="product-name">{{ msg.product.name }}</text>
              <text class="product-desc">{{ msg.product.desc }}</text>
              <view class="product-bottom">
                <text class="product-price">{{ msg.product.price }}</text>
                <view class="product-cta">
                  <text class="product-cta-text">查看详情</text>
                </view>
              </view>
            </view>
          </view>

          <!-- 普通文本消息 -->
          <view v-else class="bubble" :class="msg.isSelf ? 'bubble-self' : 'bubble-remote'">
            <text class="bubble-text">{{ msg.content }}</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 底部输入栏 -->
    <view class="dm-input-bar">
      <view class="input-row">
        <view class="add-btn" @tap="addAttachment">
          <text class="add-icon">＋</text>
        </view>
        <view class="input-wrapper">
          <input
            v-model="inputText"
            class="msg-input"
            placeholder="发送消息..."
            placeholder-class="msg-input-placeholder"
            confirm-type="send"
            @confirm="sendMessage"
          >
        </view>
        <view class="send-btn" @tap="sendMessage">
          <text class="send-icon">➤</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick } from 'vue'

// 目标用户信息
const targetUser = ref({
  name: 'PetLover_Kate',
  avatar: 'K',
})

// 信息卡片折叠状态
const profileCollapsed = ref(false)

// 输入框内容
const inputText = ref('')

// 滚动定位
const scrollToId = ref('')

// 消息列表 mock 数据
const messages = ref([
  { isSelf: false, type: 'text', content: 'Hi! 你的猫咪好可爱！是什么品种呀？' },
  { isSelf: true, type: 'text', content: '谢谢！她是一只英短金渐层，叫 Luna' },
  { isSelf: false, type: 'text', content: '我家也有一只金渐层！你平时用什么猫粮？' },
  { isSelf: true, type: 'text', content: '我最近在用 MOYUYO 的温和沐浴露，很不错' },
  { isSelf: false, type: 'text', content: '真的吗？我正好想给猫咪换沐浴露，链接发我看看' },
  { isSelf: true, type: 'text', content: '好的，稍等我找一下' },
  {
    isSelf: true,
    type: 'product',
    product: {
      name: 'MOYUYO 温和宠物沐浴露',
      desc: '植物配方 温和不刺激 500ml',
      price: '¥89',
    },
  },
  { isSelf: false, type: 'text', content: '看起来不错！有折扣吗？' },
])

// 切换信息卡片折叠
const toggleProfile = () => {
  profileCollapsed.value = !profileCollapsed.value
}

// 查看用户主页
const viewProfile = () => {
  uni.navigateTo({ url: '/pages/community/detail' })
}

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 显示更多选项
const showMoreOptions = () => {
  uni.showActionSheet({
    itemList: ['查看主页', '屏蔽用户', '举报'],
    success: (res) => {
      if (res.tapIndex === 0) viewProfile()
    },
  })
}

// 发送消息
const sendMessage = () => {
  const text = inputText.value.trim()
  if (!text) return

  messages.value.push({ isSelf: true, type: 'text', content: text })
  inputText.value = ''

  // 滚动到底部
  nextTick(() => {
    scrollToId.value = `msg-${messages.value.length - 1}`
  })
}

// 添加附件
const addAttachment = () => {
  uni.chooseImage({
    count: 1,
    success: () => {
      uni.showToast({ title: '图片发送功能开发中', icon: 'none' })
    },
  })
}
</script>

<style lang="scss" scoped>
.dm-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--color-background);
  overflow: hidden;
}

/* 导航栏 */
.dm-header {
  flex-shrink: 0;
  background: var(--color-background);
  border-bottom: 1rpx solid var(--color-border);
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 32rpx;
}

.nav-back {
  display: flex;
  align-items: center;
  gap: 4rpx;
}

.back-icon {
  font-size: 40rpx;
  color: var(--color-primary);
  line-height: 1;
}

.back-text {
  font-size: 28rpx;
  color: var(--color-primary);
}

.nav-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}

.nav-more {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.more-icon {
  font-size: 40rpx;
  color: var(--color-text);
}

/* 聊天主体 */
.dm-body {
  flex: 1;
  background: #f2f2f7;
  padding: 24rpx;
}

/* 信息卡片 */
.profile-card {
  margin-bottom: 16rpx;
  border-radius: 24rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
  overflow: hidden;
}

.profile-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
}

.profile-left {
  display: flex;
  align-items: center;
  gap: 24rpx;
}

.avatar-circle {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-letter {
  font-size: 28rpx;
  font-weight: 700;
  color: #ffffff;
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.profile-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.follow-tag {
  padding: 4rpx 12rpx;
  border-radius: 16rpx;
  background: rgba(171, 185, 173, 0.2);
}

.follow-tag-text {
  font-size: 20rpx;
  font-weight: 600;
  color: var(--color-success);
}

.chevron {
  font-size: 28rpx;
  color: var(--color-text-secondary);
  transition: transform 0.3s ease;
  transform: rotate(-90deg);
}

.chevron.rotated {
  transform: rotate(-270deg);
}

.profile-card-body {
  max-height: 200rpx;
  overflow: hidden;
  transition:
    max-height 0.3s ease,
    opacity 0.25s ease;
  opacity: 1;
  padding: 0 24rpx 24rpx;
}

.profile-card-body.collapsed {
  max-height: 0;
  opacity: 0;
  padding: 0 24rpx;
}

.view-profile-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 72rpx;
  border-radius: 36rpx;
  background: #f2f2f7;
}

.view-profile-text {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-text);
}

/* 消息列表 */
.message-list {
  padding-bottom: 32rpx;
}

.time-divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 32rpx 0;
}

.time-text {
  padding: 8rpx 24rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  color: var(--color-text-secondary);
  background: #e5e5ea;
}

.message-item {
  display: flex;
  align-items: flex-end;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.message-self {
  flex-direction: row-reverse;
}

.msg-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-letter-sm {
  font-size: 22rpx;
  font-weight: 700;
  color: #ffffff;
}

/* 消息气泡 */
.bubble {
  max-width: 75%;
  padding: 20rpx 28rpx;
  word-break: break-all;
}

.bubble-text {
  font-size: 26rpx;
  line-height: 1.6;
}

.bubble-remote {
  background: #ffffff;
  border-radius: 28rpx 28rpx 28rpx 8rpx;
}

.bubble-remote .bubble-text {
  color: var(--color-text);
}

.bubble-self {
  background: var(--color-primary);
  border-radius: 28rpx 28rpx 8rpx 28rpx;
}

.bubble-self .bubble-text {
  color: #ffffff;
}

/* 商品卡片 */
.product-card {
  max-width: 75%;
  border-radius: 28rpx;
  overflow: hidden;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
}

.product-image {
  height: 200rpx;
  background: linear-gradient(135deg, #f2f2f7, #ffffff);
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-emoji {
  font-size: 80rpx;
}

.product-info {
  padding: 24rpx;
}

.product-name {
  font-size: 26rpx;
  font-weight: 600;
  color: var(--color-text);
}

.product-desc {
  font-size: 22rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

.product-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;
}

.product-price {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--color-primary);
}

.product-cta {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  background: var(--color-primary);
}

.product-cta-text {
  font-size: 22rpx;
  font-weight: 600;
  color: #ffffff;
}

/* 底部输入栏 */
.dm-input-bar {
  flex-shrink: 0;
  background: rgba(255, 255, 255, 0.95);
  border-top: 1rpx solid var(--color-border);
  padding: 16rpx 32rpx;
  padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
}

.input-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.add-btn {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: #f2f2f7;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.add-icon {
  font-size: 36rpx;
  color: var(--color-text-secondary);
}

.input-wrapper {
  flex: 1;
  height: 72rpx;
  border-radius: 36rpx;
  background: #f2f2f7;
  border: 1rpx solid var(--color-border);
  padding: 0 28rpx;
  display: flex;
  align-items: center;
}

.msg-input {
  width: 100%;
  font-size: 28rpx;
  color: var(--color-text);
}

.msg-input-placeholder {
  color: var(--color-text-secondary);
}

.send-btn {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.send-icon {
  font-size: 32rpx;
  color: #ffffff;
}
</style>
