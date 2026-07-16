<template>
  <view class="cs">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">客服中心</text>
      <view class="header-placeholder" />
    </view>

    <!-- 聊天消息区域 -->
    <scroll-view class="chat-area" scroll-y :scroll-into-view="scrollToId">
      <!-- 机器人欢迎 -->
      <view id="msg-top" class="msg-row msg-bot">
        <view class="bot-avatar">M</view>
        <view class="msg-content">
          <view class="msg-bubble msg-bot-bubble">
            <text class="msg-text">
              您好！我是MOYUYO智能客服小助手，很高兴为您服务！请问有什么可以帮您的？
            </text>
          </view>
          <text class="msg-time">14:30</text>
        </view>
      </view>

      <!-- 快捷回复按钮 -->
      <view class="quick-replies">
        <view
          v-for="reply in quickReplies"
          :key="reply.text"
          class="quick-btn"
          @click="onQuickReply(reply)"
        >
          <text class="quick-btn-text">{{ reply.text }}</text>
        </view>
      </view>

      <!-- 客服无法回复时的提示 -->
      <view class="offline-banner">
        <text class="offline-banner-text">当前为非工作时间，如需紧急帮助请拨打客服热线</text>
      </view>

      <!-- 用户消息 -->
      <view v-for="msg in messages" :key="msg.id" class="msg-row msg-user">
        <view class="msg-content msg-user-content">
          <view class="msg-bubble msg-user-bubble">
            <text class="msg-text msg-user-text">{{ msg.text }}</text>
          </view>
          <text class="msg-time msg-user-time">{{ msg.time }}</text>
        </view>
      </view>
    </scroll-view>

    <!-- 底部输入栏（禁用状态） -->
    <view class="bottom-bar">
      <view class="input-disabled">
        <text class="input-disabled-text">请拨打客服热线 400-888-MOYU</text>
      </view>
      <view class="action-btn">
        <text class="action-btn-icon">📞</text>
      </view>
    </view>
  </view>
</template>

<script>
import { feedbackApi } from '@/api'

export default {
  data() {
    return {
      scrollToId: 'msg-top',
      quickReplies: [
        { text: '查询订单', key: 'order' },
        { text: '退换货', key: 'return' },
        { text: '物流查询', key: 'logistics' },
        { text: '优惠活动', key: 'promo' },
        { text: '人工客服', key: 'human' },
      ],
      messages: [
        {
          id: 1,
          text: '我想查询一下订单MOY20260710002的物流信息',
          time: '14:31',
        },
      ],
    }
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    onQuickReply(reply) {
      if (reply.key === 'human') {
        uni.makePhoneCall({
          phoneNumber: '400-888-MOYU',
        })
        return
      }

      const msg = {
        id: Date.now(),
        text: reply.text,
        time: '现在',
      }
      this.messages.push(msg)

      uni.showToast({
        title: '已收到您的咨询，请拨打热线获取即时帮助',
        icon: 'none',
        duration: 2000,
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.cs {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--color-background);
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

.back-btn {
  position: absolute;
  left: 16rpx;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72rpx;
  height: 72rpx;
  border-radius: var(--radius-sm);
}

.back-icon {
  font-size: 48rpx;
  color: var(--color-primary);
  line-height: 1;
}

.header-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  letter-spacing: -0.02em;
}

.header-placeholder {
  position: absolute;
  right: 16rpx;
  width: 72rpx;
  height: 72rpx;
}

/* 聊天区域 */
.chat-area {
  flex: 1;
  padding: 24rpx 32rpx 32rpx;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

/* 消息行 */
.msg-row {
  display: flex;
  gap: 16rpx;
  align-items: flex-start;
}

.msg-bot {
  align-self: flex-start;
}

.msg-user {
  align-self: flex-end;
  justify-content: flex-end;
}

/* 机器人头像 */
.bot-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: var(--color-primary);
  color: #ffffff;
  font-size: 32rpx;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  line-height: 1;
}

/* 消息内容 */
.msg-content {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  max-width: 82%;
}

.msg-user-content {
  align-items: flex-end;
}

/* 气泡 */
.msg-bubble {
  padding: 20rpx 24rpx;
  border-radius: 24rpx;
  font-size: 28rpx;
  line-height: 1.55;
}

.msg-bot-bubble {
  background: var(--color-surface);
  color: var(--color-text);
  border-top-left-radius: 8rpx;
  border: 1rpx solid var(--color-divider);
}

.msg-user-bubble {
  background: var(--color-primary);
  color: #ffffff;
  border-top-right-radius: 8rpx;
}

.msg-text {
  display: block;
}

.msg-user-text {
  color: #ffffff;
}

.msg-time {
  font-size: 22rpx;
  color: var(--color-text-tertiary);
  padding: 0 8rpx;
}

.msg-user-time {
  text-align: right;
}

/* 快捷回复按钮 */
.quick-replies {
  display: flex;
  gap: 16rpx;
  overflow-x: auto;
  padding-left: 80rpx;
  padding-bottom: 8rpx;
}

.quick-btn {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 64rpx;
  padding: 0 24rpx;
  border-radius: 999rpx;
  border: 1rpx solid var(--color-divider);
  background: var(--color-surface);
}

.quick-btn-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  white-space: nowrap;
}

/* 非工作时间提示 */
.offline-banner {
  margin: 0 0 0 80rpx;
  padding: 20rpx 24rpx;
  background: #fff7e6;
  border-radius: 16rpx;
  border: 1rpx solid #ffe0b2;
}

.offline-banner-text {
  font-size: 24rpx;
  color: #b8860b;
  line-height: 1.5;
}

/* 底部输入栏 */
.bottom-bar {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 16rpx 24rpx;
  padding-bottom: calc(16rpx + env(safe-area-inset-bottom, 0));
  background: var(--color-surface);
  border-top: 1rpx solid var(--color-divider);
}

.input-disabled {
  flex: 1;
  display: flex;
  align-items: center;
  height: 80rpx;
  padding: 0 32rpx;
  border-radius: 999rpx;
  background: var(--color-background);
}

.input-disabled-text {
  font-size: 26rpx;
  color: var(--color-text-tertiary);
}

.action-btn {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary);
}

.action-btn-icon {
  font-size: 32rpx;
  line-height: 1;
}
</style>
