<template>
  <view class="edit-profile">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">编辑资料</text>
      <view class="save-link" @click="onSave">
        <text class="save-text">保存</text>
      </view>
    </view>

    <view class="content">
      <!-- 头像区域 -->
      <view class="avatar-section">
        <view class="avatar">
          <text class="avatar-text">🐕</text>
        </view>
        <view class="change-avatar-btn" @click="onChangeAvatar">
          <text class="change-avatar-text">更换头像</text>
        </view>
      </view>

      <!-- 基本信息 -->
      <view class="form-group">
        <text class="group-label">基本信息</text>
        <view class="form-card">
          <!-- 昵称 -->
          <view class="form-item" @click="onEditField('nickname')">
            <text class="item-label">昵称</text>
            <view class="item-right">
              <text class="item-value">{{ profile.nickname }}</text>
              <text class="chevron">›</text>
            </view>
          </view>
          <view class="divider indent" />
          <!-- 性别 -->
          <view class="form-item" @click="onEditField('gender')">
            <text class="item-label">性别</text>
            <view class="item-right">
              <view class="gender-group">
                <text class="gender-option" :class="{ active: profile.gender === 'male' }">男</text>
                <text class="gender-option" :class="{ active: profile.gender === 'female' }">
                  女
                </text>
                <text class="gender-option" :class="{ active: profile.gender === 'unset' }">
                  保密
                </text>
              </view>
              <text class="chevron">›</text>
            </view>
          </view>
          <view class="divider indent" />
          <!-- 生日 -->
          <view class="form-item" @click="onEditField('birthday')">
            <text class="item-label">生日</text>
            <view class="item-right">
              <text class="item-value">{{ profile.birthday }}</text>
              <text class="chevron">›</text>
            </view>
          </view>
          <view class="divider indent" />
          <!-- 个人简介 -->
          <view class="form-item" @click="onEditField('bio')">
            <text class="item-label">个人简介</text>
            <view class="item-right">
              <text class="item-value truncate">{{ profile.bio }}</text>
              <text class="chevron">›</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 联系方式 -->
      <view class="form-group">
        <text class="group-label">联系方式</text>
        <view class="form-card">
          <!-- 邮箱 -->
          <view class="form-item">
            <text class="item-label">邮箱</text>
            <view class="item-right">
              <text class="item-value">{{ profile.email }}</text>
              <view class="verified-badge">
                <text class="verified-icon">✅</text>
                <text class="verified-text">已验证</text>
              </view>
            </view>
          </view>
          <view class="divider indent" />
          <!-- 手机号 -->
          <view class="form-item" @click="onEditField('phone')">
            <text class="item-label">手机号</text>
            <view class="item-right">
              <text class="item-value">{{ profile.phone }}</text>
              <text class="chevron">›</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 偏好设置 -->
      <view class="form-group">
        <text class="group-label">偏好设置</text>
        <view class="form-card">
          <!-- 宠物偏好 -->
          <view class="form-item" @click="onEditField('petPreference')">
            <text class="item-label">宠物偏好</text>
            <view class="item-right">
              <view class="preference-tags">
                <view v-for="tag in profile.petPreferences" :key="tag" class="pref-tag active">
                  <text class="pref-tag-text">{{ tag }}</text>
                </view>
                <view v-for="tag in inactivePreferences" :key="tag" class="pref-tag inactive">
                  <text class="pref-tag-text">{{ tag }}</text>
                </view>
              </view>
              <text class="chevron">›</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const goBack = () => uni.navigateBack()

// 用户资料 mock 数据
const profile = ref({
  nickname: '宠物爱好者',
  gender: 'unset',
  birthday: '1995-06-15',
  bio: '热爱生活的铲屎官',
  email: 'm***@gmail.com',
  phone: '+1 ***-***-5678',
  petPreferences: ['猫', '狗'],
})

const inactivePreferences = computed(() => {
  const all = ['猫', '狗', '其他']
  return all.filter((t) => !profile.value.petPreferences.includes(t))
})

// 操作
const onChangeAvatar = () => uni.showToast({ title: '更换头像', icon: 'none' })
const onSave = () => uni.showToast({ title: '资料已保存', icon: 'success' })
const onEditField = (field) => uni.showToast({ title: '编辑 ' + field, icon: 'none' })
</script>

<style lang="scss" scoped>
.edit-profile {
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
  justify-content: space-between;
  height: 88rpx;
  padding: 0 24rpx;
  background: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
}

.back-btn {
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
.save-link {
  padding: 8rpx;
}
.save-text {
  font-size: 28rpx;
  font-weight: var(--font-weight-semibold);
  color: var(--color-primary);
}

/* 内容区 */
.content {
  padding: 40rpx 32rpx 64rpx;
  display: flex;
  flex-direction: column;
  gap: 40rpx;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20rpx;
}
.avatar {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  border: 6rpx solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 72rpx;
  overflow: hidden;
}

.avatar-text {
  font-size: 72rpx;
}
.change-avatar-btn {
  padding: 8rpx 16rpx;
}
.change-avatar-text {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-primary);
}

/* 表单分组 */
.form-group {
  display: flex;
  flex-direction: column;
}
.group-label {
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
  letter-spacing: 2rpx;
  padding: 0 8rpx 16rpx;
}

.form-card {
  background: var(--color-card);
  border-radius: 20rpx;
  overflow: hidden;
}

/* 表单行 */
.form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;
  height: 104rpx;
}

.item-label {
  font-size: 28rpx;
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}
.item-right {
  display: flex;
  align-items: center;
  gap: 8rpx;
}
.item-value {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}
.item-value.truncate {
  max-width: 280rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.chevron {
  font-size: 36rpx;
  color: #aeaeb2;
  line-height: 1;
}

/* 分割线 */
.divider {
  height: 1rpx;
  background: var(--color-border);
}
.divider.indent {
  margin-left: 64rpx;
}

/* 性别选项 */
.gender-group {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.gender-option {
  font-size: 28rpx;
  color: #8e8e93;
}
.gender-option.active {
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

/* 已验证标签 */
.verified-badge {
  display: flex;
  align-items: center;
  gap: 4rpx;
}
.verified-icon {
  font-size: 24rpx;
}
.verified-text {
  font-size: 20rpx;
  font-weight: var(--font-weight-semibold);
  color: var(--color-success);
}

/* 宠物偏好标签 */
.preference-tags {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.pref-tag {
  padding: 8rpx 20rpx;
  border-radius: 999rpx;
}

.pref-tag.active {
  background: var(--color-primary);
}
.pref-tag.active .pref-tag-text {
  color: #fff;
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
}

.pref-tag.inactive {
  background: var(--color-border);
  border: 1rpx solid var(--color-border);
}
.pref-tag.inactive .pref-tag-text {
  color: var(--color-text-secondary);
  font-size: 24rpx;
  font-weight: var(--font-weight-medium);
}
</style>
