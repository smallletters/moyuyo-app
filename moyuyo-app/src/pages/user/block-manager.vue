<template>
  <view class="block-manager">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">屏蔽管理</text>
    </view>

    <view class="content">
      <!-- Tab 切换栏 -->
      <scroll-view scroll-x class="tab-scroll">
        <view
          v-for="tab in tabs"
          :key="tab.key"
          class="tab-btn"
          :class="{ active: currentTab === tab.key }"
          @click="switchTab(tab.key)"
        >
          <text class="tab-text" :class="{ active: currentTab === tab.key }">{{ tab.label }}</text>
        </view>
      </scroll-view>

      <!-- 屏蔽用户 Tab -->
      <view v-if="currentTab === 'users'" class="tab-panel">
        <!-- 搜索框 -->
        <view class="search-bar">
          <text class="search-icon">🔍</text>
          <input v-model="searchKeyword" class="search-input" placeholder="按昵称搜索">
        </view>
        <!-- 已屏蔽用户列表 -->
        <view class="list-card">
          <view v-for="(user, index) in blockedUsers" :key="user.id" class="list-item">
            <view class="user-info">
              <view class="user-avatar" :style="{ background: user.avatarBg }">
                <text class="user-avatar-text">{{ user.initial }}</text>
              </view>
              <view class="user-detail">
                <text class="user-name">{{ user.name }}</text>
                <text class="user-date">{{ user.blockedDate }} 屏蔽</text>
              </view>
            </view>
            <view class="unblock-btn" @click="onUnblockUser(user.id)">
              <text class="unblock-text">解除屏蔽</text>
            </view>
            <view v-if="index < blockedUsers.length - 1" class="divider indent" />
          </view>
        </view>
        <text class="list-hint">已屏蔽 {{ blockedUsers.length }}/500 人</text>
      </view>

      <!-- 屏蔽话题 Tab -->
      <view v-if="currentTab === 'topics'" class="tab-panel">
        <view class="list-card">
          <view v-for="(topic, index) in blockedTopics" :key="topic.id" class="list-item">
            <view class="topic-info">
              <text class="topic-hash">#</text>
              <view class="topic-detail">
                <text class="topic-name">{{ topic.name }}</text>
                <text class="topic-date">{{ topic.blockedDate }} 屏蔽</text>
              </view>
            </view>
            <view class="unblock-btn" @click="onUnblockTopic(topic.id)">
              <text class="unblock-text">解除</text>
            </view>
            <view v-if="index < blockedTopics.length - 1" class="divider indent" />
          </view>
        </view>
        <!-- 添加屏蔽话题按钮 -->
        <view class="add-block-btn" @click="onAddTopic">
          <text class="add-icon">➕</text>
          <text class="add-text">添加屏蔽话题</text>
        </view>
      </view>

      <!-- 屏蔽关键词 Tab -->
      <view v-if="currentTab === 'keywords'" class="tab-panel">
        <!-- 输入框 + 添加按钮 -->
        <view class="keyword-input-row">
          <view class="keyword-input-wrap">
            <text class="input-icon">🔤</text>
            <input
              v-model="newKeyword"
              class="keyword-input"
              placeholder="输入关键词"
              @confirm="addKeyword"
            >
          </view>
          <view class="keyword-add-btn" @click="addKeyword">
            <text class="keyword-add-text">添加</text>
          </view>
        </view>
        <!-- 已屏蔽关键词标签列表 -->
        <view class="keyword-tags-card">
          <view v-for="(kw, index) in blockedKeywords" :key="index" class="keyword-tag">
            <text class="keyword-text">{{ kw }}</text>
            <view class="keyword-remove" @click="removeKeyword(index)">
              <text class="remove-icon">✕</text>
            </view>
          </view>
        </view>
        <text class="list-hint">已添加 {{ blockedKeywords.length }}/50 个关键词</text>
      </view>

      <!-- 静音账号 Tab -->
      <view v-if="currentTab === 'muted'" class="tab-panel">
        <view class="list-card">
          <view v-for="(account, index) in mutedAccounts" :key="account.id" class="list-item">
            <view class="user-info">
              <view class="user-avatar" :style="{ background: account.avatarBg }">
                <text class="user-avatar-text">{{ account.initial }}</text>
              </view>
              <view class="user-detail">
                <text class="user-name">{{ account.name }}</text>
                <text class="user-date">{{ account.status }}</text>
              </view>
            </view>
            <view class="unblock-btn" @click="onUnmuteAccount(account.id)">
              <text class="unblock-text">取消静音</text>
            </view>
            <view v-if="index < mutedAccounts.length - 1" class="divider indent" />
          </view>
        </view>
        <text class="list-hint muted-hint">
          静音的账号仍会出现在你的关注列表中，但其动态不会出现在你的信息流
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const goBack = () => uni.navigateBack()

// Tab 切换
const tabs = [
  { key: 'users', label: '屏蔽用户' },
  { key: 'topics', label: '屏蔽话题' },
  { key: 'keywords', label: '屏蔽关键词' },
  { key: 'muted', label: '静音账号' },
]
const currentTab = ref('users')

const switchTab = (key) => {
  currentTab.value = key
}

// 屏蔽用户
const searchKeyword = ref('')
const blockedUsers = ref([
  { id: 1, name: '张三家的猫咪', initial: '张', avatarBg: '#9fcbff', blockedDate: '2025-06-10' },
  { id: 2, name: '李四爱狗', initial: '李', avatarBg: '#e9f9ee', blockedDate: '2025-05-22' },
  { id: 3, name: '王五宠物店', initial: '王', avatarBg: '#ffecea', blockedDate: '2025-04-18' },
])

const onUnblockUser = (id) => {
  blockedUsers.value = blockedUsers.value.filter((u) => u.id !== id)
  uni.showToast({ title: '已解除屏蔽', icon: 'none' })
}

// 屏蔽话题
const blockedTopics = ref([
  { id: 1, name: '猫粮测评', blockedDate: '2025-06-01' },
  { id: 2, name: '宠物医院推荐', blockedDate: '2025-05-15' },
])

const onUnblockTopic = (id) => {
  blockedTopics.value = blockedTopics.value.filter((t) => t.id !== id)
  uni.showToast({ title: '已解除屏蔽', icon: 'none' })
}

const onAddTopic = () => uni.showToast({ title: '添加屏蔽话题', icon: 'none' })

// 屏蔽关键词
const newKeyword = ref('')
const blockedKeywords = ref(['虚假宣传', '广告推广', '低质内容'])

const addKeyword = () => {
  const val = newKeyword.value.trim()
  if (val && !blockedKeywords.value.includes(val)) {
    blockedKeywords.value.push(val)
    newKeyword.value = ''
    uni.showToast({ title: '已添加关键词', icon: 'none' })
  }
}

const removeKeyword = (index) => {
  blockedKeywords.value.splice(index, 1)
  uni.showToast({ title: '已移除关键词', icon: 'none' })
}

// 静音账号
const mutedAccounts = ref([
  { id: 1, name: '萌宠日记', initial: '萌', avatarBg: '#e5e5ea', status: '已关注 / 已静音' },
  { id: 2, name: '猫咪研究所', initial: '猫', avatarBg: '#f7f7fa', status: '已关注 / 已静音' },
])

const onUnmuteAccount = (id) => {
  mutedAccounts.value = mutedAccounts.value.filter((a) => a.id !== id)
  uni.showToast({ title: '已取消静音', icon: 'none' })
}
</script>

<style lang="scss" scoped>
.block-manager {
  min-height: 100vh;
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
  background: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
}

.back-btn {
  position: absolute;
  left: 24rpx;
  width: 72rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16rpx;
}

.back-icon {
  font-size: 48rpx;
  color: var(--color-primary);
  line-height: 1;
}
.header-title {
  font-size: 32rpx;
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

/* 内容区 */
.content {
  padding: 24rpx 32rpx 64rpx;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

/* Tab 切换 */
.tab-scroll {
  white-space: nowrap;
}
.tab-btn {
  display: inline-flex;
  padding: 16rpx 32rpx;
  border-radius: 999rpx;
  margin-right: 16rpx;
  background: var(--color-card);
}

.tab-btn.active {
  background: var(--color-primary);
}
.tab-text {
  font-size: 24rpx;
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-secondary);
}
.tab-text.active {
  color: #fff;
}

/* Tab 面板 */
.tab-panel {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

/* 搜索框 */
.search-bar {
  display: flex;
  align-items: center;
  gap: 12rpx;
  height: 80rpx;
  padding: 0 24rpx;
  border-radius: 20rpx;
  background: var(--color-card);
}

.search-icon {
  font-size: 28rpx;
}
.search-input {
  flex: 1;
  background: transparent;
  border: none;
  font-size: 28rpx;
  color: var(--color-text);
}

/* 列表卡片 */
.list-card {
  background: var(--color-card);
  border-radius: 20rpx;
  overflow: hidden;
}

.list-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;
  min-height: 136rpx;
  position: relative;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  gap: 20rpx;
  flex: 1;
  min-width: 0;
}
.user-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-avatar-text {
  font-size: 28rpx;
  font-weight: 700;
  color: #fff;
}
.user-detail {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}
.user-name {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}
.user-date {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

/* 话题信息 */
.topic-info {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
  flex: 1;
  min-width: 0;
}
.topic-hash {
  font-size: 28rpx;
  color: var(--color-primary);
  font-weight: 700;
}
.topic-detail {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}
.topic-name {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}
.topic-date {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

/* 解除按钮 */
.unblock-btn {
  padding: 12rpx 24rpx;
  border-radius: 999rpx;
  background: var(--color-border);
  flex-shrink: 0;
}

.unblock-text {
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-primary);
}

/* 分割线 */
.divider {
  height: 1rpx;
  background: var(--color-border);
  position: absolute;
  bottom: 0;
  left: 144rpx;
  right: 0;
}

/* 底部提示 */
.list-hint {
  text-align: center;
  font-size: 24rpx;
  color: var(--color-text-secondary);
  padding: 16rpx 0;
}
.muted-hint {
  line-height: 1.5;
  padding: 16rpx 32rpx;
}

/* 添加屏蔽话题按钮 */
.add-block-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  height: 96rpx;
  border-radius: 20rpx;
  background: var(--color-card);
  border: 2rpx dashed var(--color-border);
}

.add-icon {
  font-size: 28rpx;
}
.add-text {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-primary);
}

/* 关键词输入 */
.keyword-input-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.keyword-input-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12rpx;
  height: 80rpx;
  padding: 0 24rpx;
  border-radius: 20rpx;
  background: var(--color-card);
}

.input-icon {
  font-size: 28rpx;
}
.keyword-input {
  flex: 1;
  background: transparent;
  border: none;
  font-size: 28rpx;
  color: var(--color-text);
}

.keyword-add-btn {
  height: 80rpx;
  padding: 0 32rpx;
  border-radius: 20rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.keyword-add-text {
  font-size: 28rpx;
  font-weight: var(--font-weight-semibold);
  color: #fff;
}

/* 关键词标签 */
.keyword-tags-card {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 20rpx;
}

.keyword-tag {
  display: flex;
  align-items: center;
  gap: 10rpx;
  padding: 12rpx 24rpx;
  border-radius: 999rpx;
  background: var(--color-border);
}

.keyword-text {
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}
.keyword-remove {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  background: var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
}

.remove-icon {
  font-size: 18rpx;
  color: var(--color-text-secondary);
}
</style>
