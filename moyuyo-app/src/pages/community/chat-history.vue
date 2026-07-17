<template>
  <view class="chat-history-page">
    <!-- 顶部导航栏 -->
    <view class="nav-bar">
      <view class="nav-back" @tap="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="nav-title">客服会话</text>
      <view class="nav-placeholder" />
    </view>

    <!-- 筛选 Tab 栏 -->
    <view class="tab-bar">
      <view
        v-for="(tab, index) in tabs"
        :key="index"
        class="tab-item"
        :class="{ 'tab-active': activeTab === index }"
        @tap="switchTab(index)"
      >
        <text class="tab-text" :class="{ 'tab-text-active': activeTab === index }">{{ tab }}</text>
      </view>
    </view>

    <!-- 会话列表 -->
    <scroll-view class="chat-list" scroll-y>
      <view
        v-for="(chat, index) in filteredChats"
        :key="index"
        class="chat-item"
        @tap="openChat(chat)"
      >
        <!-- 客服头像 -->
        <view class="chat-avatar-wrap">
          <view class="chat-avatar">
            <text class="avatar-emoji">{{ chat.isAI ? '🤖' : '👩' }}</text>
          </view>
          <!-- 未读红点 -->
          <view v-if="chat.unread" class="unread-dot" />
        </view>

        <!-- 会话信息 -->
        <view class="chat-info">
          <view class="chat-info-top">
            <text class="chat-name">{{ chat.name }}</text>
            <view class="chat-type-tag" :class="chat.isAI ? 'tag-ai' : 'tag-human'">
              <text class="tag-text" :class="chat.isAI ? 'tag-text-ai' : 'tag-text-human'">
                {{ chat.type }}
              </text>
            </view>
          </view>
          <view class="chat-info-bottom">
            <text class="chat-preview">{{ chat.preview }}</text>
            <text class="chat-time">{{ chat.time }}</text>
          </view>
        </view>

        <!-- 状态标签 -->
        <view
          class="chat-status-tag"
          :class="chat.status === '进行中' ? 'status-active' : 'status-ended'"
        >
          <text
            class="status-text"
            :class="chat.status === '进行中' ? 'status-text-active' : 'status-text-ended'"
          >
            {{ chat.status }}
          </text>
        </view>
      </view>

      <!-- 底部提示 -->
      <view class="list-footer">
        <text class="footer-text">会话记录保留 180 天</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

// Tab 选项
const tabs = ['全部', '进行中', '已结束']
const activeTab = ref(0)

// 会话列表 mock 数据
const chats = ref([
  {
    id: 1,
    name: '客服小美',
    type: '人工客服',
    isAI: false,
    preview: '您好，您的包裹目前正在转运中，预计明天送达',
    time: '14:30',
    status: '进行中',
    unread: true,
  },
  {
    id: 2,
    name: 'MOYUYO智能助手',
    type: '智能客服',
    isAI: true,
    preview: '优惠券可在下单时自动抵扣，每笔订单限用一张',
    time: '昨天',
    status: '已结束',
    unread: false,
  },
  {
    id: 3,
    name: '客服小杰',
    type: '人工客服',
    isAI: false,
    preview: '退换货申请已处理完成，退款将在3个工作日内到账',
    time: '3天前',
    status: '已结束',
    unread: false,
  },
  {
    id: 4,
    name: 'MOYUYO智能助手',
    type: '智能客服',
    isAI: true,
    preview: '已为您找到3笔近期订单，请问需要查看哪一笔',
    time: '10:15',
    status: '进行中',
    unread: false,
  },
])

// 根据 Tab 筛选会话
const filteredChats = computed(() => {
  if (activeTab.value === 0) return chats.value
  const status = activeTab.value === 1 ? '进行中' : '已结束'
  return chats.value.filter((c) => c.status === status)
})

// 切换 Tab
const switchTab = (index) => {
  activeTab.value = index
}

// 打开会话
const openChat = (chat) => {
  uni.navigateTo({
    url: `/pages/user/customer-service?id=${chat.id}`,
  })
}

// 返回
const goBack = () => {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped>
.chat-history-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--color-background);
}

/* 导航栏 */
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 32rpx;
  background: var(--color-background);
  border-bottom: 1rpx solid var(--color-border);
}

.nav-back {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 44rpx;
  color: var(--color-primary);
}

.nav-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}

.nav-placeholder {
  width: 64rpx;
  height: 64rpx;
}

/* Tab 栏 */
.tab-bar {
  display: flex;
  border-bottom: 1rpx solid var(--color-border);
  background: var(--color-background);
}

.tab-item {
  flex: 1;
  padding: 20rpx 0;
  text-align: center;
  position: relative;
}

.tab-text {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

.tab-text-active {
  font-weight: 600;
  color: var(--color-primary);
}

.tab-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 64rpx;
  height: 4rpx;
  border-radius: 2rpx;
  background: var(--color-primary);
}

/* 会话列表 */
.chat-list {
  flex: 1;
  background: #f2f2f7;
}

.chat-item {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 24rpx 32rpx;
  background: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
}

.chat-avatar-wrap {
  position: relative;
  flex-shrink: 0;
}

.chat-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #f2f2f7;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1rpx solid var(--color-border);
}

.avatar-emoji {
  font-size: 36rpx;
}

.unread-dot {
  position: absolute;
  top: -4rpx;
  right: -4rpx;
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background: var(--color-error);
  border: 3rpx solid var(--color-card);
}

.chat-info {
  flex: 1;
  min-width: 0;
}

.chat-info-top {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.chat-name {
  font-size: 30rpx;
  font-weight: 600;
  color: var(--color-text);
}

.chat-type-tag {
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.tag-human {
  background: rgba(219, 201, 138, 0.15);
}

.tag-ai {
  background: #f2f2f7;
}

.tag-text {
  font-size: 22rpx;
  font-weight: 500;
}

.tag-text-human {
  color: var(--color-primary);
}

.tag-text-ai {
  color: var(--color-text-secondary);
}

.chat-info-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8rpx;
}

.chat-preview {
  font-size: 26rpx;
  color: var(--color-text-secondary);
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-right: 16rpx;
}

.chat-time {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  flex-shrink: 0;
}

.chat-status-tag {
  flex-shrink: 0;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  align-self: flex-start;
  margin-top: 4rpx;
}

.status-active {
  background: rgba(219, 201, 138, 0.15);
}

.status-ended {
  background: #f2f2f7;
}

.status-text {
  font-size: 22rpx;
  font-weight: 500;
}

.status-text-active {
  color: var(--color-primary);
}

.status-text-ended {
  color: var(--color-text-secondary);
}

/* 底部提示 */
.list-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48rpx 0;
}

.footer-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
</style>
