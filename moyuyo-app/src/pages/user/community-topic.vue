<template>
  <view class="community-topic">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="header-left" @tap="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">话题广场</text>
      <view class="header-right">
        <text class="search-icon">⌕</text>
      </view>
    </view>

    <!-- 搜索入口 -->
    <view class="search-bar" @tap="handleSearch">
      <text class="search-icon-small">⌕</text>
      <text class="search-placeholder">搜索感兴趣的话题...</text>
    </view>

    <!-- 话题分类 Tab 横向滚动 -->
    <scroll-view scroll-x class="category-scroll" show-scrollbar="false">
      <view
        v-for="cat in categories"
        :key="cat.key"
        class="category-pill"
        :class="{ active: activeCategory === cat.key }"
        :style="{
          background: activeCategory === cat.key ? 'var(--primary)' : 'var(--background-200)',
          color: activeCategory === cat.key ? 'var(--primary-foreground)' : 'var(--text-500)',
        }"
        @tap="activeCategory = cat.key"
      >
        <text>{{ cat.label }}</text>
      </view>
    </scroll-view>

    <scroll-view scroll-y class="scroll">
      <!-- 热门话题榜 - 前3名大卡片 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">热门话题榜</text>
          <text class="section-more">更多 ›</text>
        </view>
        <view
          v-for="topic in hotTopics"
          :key="topic.rank"
          class="hero-card"
          :style="{ background: topic.bg }"
        >
          <view class="hero-deco" />
          <view class="hero-content">
            <view class="hero-top">
              <view class="rank-badge" :class="'rank-' + topic.rank">
                <text>{{ topic.rank }}</text>
              </view>
              <text class="hero-name">{{ topic.name }}</text>
            </view>
            <view class="hero-stats">
              <text class="hero-stat">{{ topic.posts }} 帖子</text>
              <text class="hero-stat">{{ topic.discussions }} 讨论</text>
            </view>
            <view class="hero-trend" :class="topic.trend > 0 ? 'up' : 'down'">
              <text>
                {{ topic.trend > 0 ? '↑' : '↓' }} 热度{{ topic.trend > 0 ? '上升' : '下降' }}
                {{ Math.abs(topic.trend) }}%
              </text>
            </view>
          </view>
        </view>
      </view>

      <!-- 话题排行列表 -->
      <view class="section">
        <view class="topic-list">
          <view v-for="topic in topicList" :key="topic.rank" class="topic-row">
            <view
              class="rank-num"
              :class="'rank-' + topic.rank"
              :style="
                topic.rank <= 3
                  ? {}
                  : { background: 'var(--background-200)', color: 'var(--text-500)' }
              "
            >
              <text>{{ topic.rank }}</text>
            </view>
            <view class="topic-info">
              <text class="topic-name">{{ topic.name }}</text>
              <text class="topic-stats">
                {{ topic.participants }} 参与 / {{ topic.posts }} 帖子
              </text>
            </view>
            <view class="trend-badge" :class="topic.trend > 0 ? 'up' : 'down'">
              <text>{{ topic.trend > 0 ? '↑' : '↓' }} {{ Math.abs(topic.trend) }}%</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 官方话题专区 -->
      <view class="section">
        <view class="section-header">
          <view class="section-header-left">
            <text class="section-title">官方话题</text>
            <view class="official-badge">认证</view>
          </view>
          <text class="section-more">全部 ›</text>
        </view>
        <scroll-view scroll-x class="official-scroll" show-scrollbar="false">
          <view v-for="topic in officialTopics" :key="topic.id" class="official-card">
            <view class="official-cover" :style="{ background: topic.bg }">
              <view class="official-cover-deco" />
              <text class="official-name">{{ topic.name }}</text>
            </view>
            <view class="official-footer">
              <text class="official-posts">{{ topic.posts }} 参与</text>
              <view class="official-tag">官方</view>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- 当前选中话题的帖子列表 -->
      <view class="section">
        <text class="section-title">最新帖子</text>
        <view
          v-for="post in filteredPosts"
          :key="post.id"
          class="post-card"
          @tap="handlePostTap(post)"
        >
          <view class="post-header">
            <view class="post-avatar">
              <text>{{ post.authorInitial }}</text>
            </view>
            <view class="post-author-info">
              <text class="post-author">{{ post.author }}</text>
              <text class="post-time">{{ post.time }}</text>
            </view>
            <view class="post-topic-tag">#{{ post.topic }}</view>
          </view>
          <text class="post-content">{{ post.content }}</text>
          <view v-if="post.images > 0" class="post-images">
            <view class="post-image-placeholder" />
            <view v-if="post.images > 1" class="post-image-placeholder" />
            <view v-if="post.images > 2" class="post-image-placeholder" />
          </view>
          <view class="post-actions">
            <text class="post-action">♥ {{ post.likes }}</text>
            <text class="post-action">💬 {{ post.comments }}</text>
          </view>
        </view>
      </view>

      <view class="bottom-spacer" />
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeCategory: 'hot',
      categories: [
        { key: 'hot', label: '热门' },
        { key: 'official', label: '官方' },
        { key: 'daily', label: '宠物日常' },
        { key: 'knowledge', label: '养宠知识' },
        { key: 'brand', label: '品牌活动' },
      ],
      hotTopics: [
        {
          rank: 1,
          name: '# 春日萌宠穿搭挑战',
          posts: '3,282',
          discussions: '12.5k',
          trend: 128,
          bg: 'linear-gradient(135deg, #667eea, #764ba2)',
        },
        {
          rank: 2,
          name: '# 萌宠日常记录',
          posts: '2,847',
          discussions: '9.8k',
          trend: 86,
          bg: 'linear-gradient(135deg, #f093fb, #f5576c)',
        },
        {
          rank: 3,
          name: '# 新手养猫必看指南',
          posts: '2,156',
          discussions: '7.2k',
          trend: -12,
          bg: 'linear-gradient(135deg, #4facfe, #00f2fe)',
        },
      ],
      topicList: [
        { rank: 4, name: '# 宠物健康饮食', participants: '1,893', posts: '1,756', trend: 32 },
        { rank: 5, name: '# 狗狗训练小技巧', participants: '1,642', posts: '1,523', trend: 18 },
        { rank: 6, name: '# 宠物美容分享', participants: '1,387', posts: '1,210', trend: -5 },
        { rank: 7, name: '# 领养代替购买', participants: '1,256', posts: '1,089', trend: 45 },
        { rank: 8, name: '# 猫咪搞笑瞬间', participants: '1,102', posts: '968', trend: -8 },
        { rank: 9, name: '# 宠物出行必备', participants: '978', posts: '856', trend: 15 },
        { rank: 10, name: '# 宠物生日派对', participants: '845', posts: '723', trend: -3 },
      ],
      officialTopics: [
        {
          id: 1,
          name: '#MoyuyoPet',
          posts: '5.6k',
          bg: 'linear-gradient(135deg, var(--brand-500), var(--brand-700))',
        },
        { id: 2, name: '#BathDay', posts: '3.2k', bg: 'linear-gradient(135deg, #43e97b, #38f9d7)' },
        {
          id: 3,
          name: '#VaccineTime',
          posts: '2.8k',
          bg: 'linear-gradient(135deg, #fa709a, #fee140)',
        },
        {
          id: 4,
          name: '#WalkWithMe',
          posts: '4.1k',
          bg: 'linear-gradient(135deg, #a18cd1, #fbc2eb)',
        },
        {
          id: 5,
          name: '#PetStyle',
          posts: '2.5k',
          bg: 'linear-gradient(135deg, #fccb90, #d57eeb)',
        },
      ],
      posts: [
        {
          id: 1,
          author: '萌宠达人',
          authorInitial: '萌',
          time: '2小时前',
          topic: '宠物日常',
          content: '今天带我家毛孩子去公园散步，遇到了好多可爱的狗狗朋友！',
          images: 2,
          likes: 45,
          comments: 12,
        },
        {
          id: 2,
          author: '猫咪控',
          authorInitial: '猫',
          time: '5小时前',
          topic: '养宠知识',
          content: '分享一个猫咪驱虫的小技巧，新手铲屎官一定要看！',
          images: 1,
          likes: 89,
          comments: 23,
        },
        {
          id: 3,
          author: '汪星人家长',
          authorInitial: '汪',
          time: '昨天',
          topic: '宠物日常',
          content: '我家金毛今天学会了新技能，太聪明了！',
          images: 3,
          likes: 156,
          comments: 34,
        },
      ],
    }
  },
  computed: {
    filteredPosts() {
      return this.posts
    },
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    handleSearch() {
      uni.showToast({ title: '打开搜索', icon: 'none' })
    },
    handlePostTap(post) {
      uni.navigateTo({ url: `/pages/community/detail?id=${post.id}` })
    },
  },
}
</script>

<style scoped lang="scss">
.community-topic {
  min-height: 100vh;
  background: var(--background);
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 30rpx;
  background: var(--background);
}
.header-left,
.header-right {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: var(--background-200);
  display: flex;
  align-items: center;
  justify-content: center;
}
.back-icon,
.search-icon {
  font-size: 36rpx;
  color: var(--foreground);
}
.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--text-800);
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin: 16rpx 30rpx;
  height: 88rpx;
  border-radius: 24rpx;
  padding: 0 28rpx;
  background: var(--background-200);
}
.search-icon-small {
  font-size: 32rpx;
  color: var(--icon-600);
}
.search-placeholder {
  font-size: 28rpx;
  color: var(--text-400);
}
.category-scroll {
  display: flex;
  flex-direction: row;
  white-space: nowrap;
  padding: 0 30rpx 24rpx;
}
.category-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 64rpx;
  padding: 0 32rpx;
  border-radius: 999rpx;
  font-size: 28rpx;
  margin-right: 16rpx;
  flex-shrink: 0;
}
.scroll {
  padding-bottom: 40rpx;
}
.section {
  padding: 0 30rpx 30rpx;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}
.section-header-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.section-title {
  font-size: 36rpx;
  font-weight: 600;
  color: var(--text-800);
}
.section-more {
  font-size: 24rpx;
  color: var(--text-400);
  display: flex;
  align-items: center;
}
.official-badge {
  font-size: 20rpx;
  font-weight: 700;
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  background: var(--brand-50);
  color: var(--brand-500);
}
.hero-card {
  border-radius: 24rpx;
  overflow: hidden;
  min-height: 240rpx;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  margin-bottom: 20rpx;
  position: relative;
}
.hero-deco {
  position: absolute;
  top: 30rpx;
  right: 30rpx;
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}
.hero-content {
  position: relative;
  z-index: 10;
  padding: 30rpx;
}
.hero-top {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 16rpx;
}
.rank-badge {
  width: 44rpx;
  height: 44rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 700;
}
.rank-badge.rank-1 {
  background: linear-gradient(135deg, #ff6b35, #ff3b30);
  color: #fff;
}
.rank-badge.rank-2 {
  background: linear-gradient(135deg, #ff9500, #ff6b35);
  color: #fff;
}
.rank-badge.rank-3 {
  background: linear-gradient(135deg, #ffcc00, #ff9500);
  color: #fff;
}
.hero-name {
  font-size: 32rpx;
  font-weight: 700;
  color: #ffffff;
}
.hero-stats {
  display: flex;
  gap: 24rpx;
  margin-bottom: 12rpx;
}
.hero-stat {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
}
.hero-trend {
  font-size: 24rpx;
  font-weight: 600;
}
.hero-trend.up {
  color: var(--state-error);
}
.hero-trend.down {
  color: var(--state-success);
}
.topic-list {
  border-radius: 24rpx;
  overflow: hidden;
  border: 2rpx solid var(--border);
}
.topic-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 28rpx 30rpx;
  background: var(--card);
  border-bottom: 2rpx solid var(--border);
}
.topic-row:last-child {
  border-bottom: none;
}
.rank-num {
  width: 48rpx;
  height: 48rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  font-weight: 700;
  flex-shrink: 0;
}
.rank-num.rank-1,
.rank-num.rank-2,
.rank-num.rank-3 {
  display: none;
}
.topic-info {
  flex: 1;
  min-width: 0;
}
.topic-name {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--text-800);
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.topic-stats {
  font-size: 24rpx;
  color: var(--text-400);
  display: block;
  margin-top: 4rpx;
}
.trend-badge {
  font-size: 22rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4rpx;
  flex-shrink: 0;
}
.trend-badge.up {
  color: var(--state-error);
}
.trend-badge.down {
  color: var(--state-success);
}
.official-scroll {
  display: flex;
  flex-direction: row;
  white-space: nowrap;
}
.official-card {
  width: 280rpx;
  border-radius: 24rpx;
  overflow: hidden;
  margin-right: 20rpx;
  flex-shrink: 0;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.official-cover {
  height: 224rpx;
  position: relative;
  display: flex;
  align-items: flex-end;
  padding: 24rpx;
}
.official-cover-deco {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
}
.official-name {
  position: relative;
  z-index: 10;
  font-size: 28rpx;
  font-weight: 700;
  color: #ffffff;
}
.official-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background: var(--card);
}
.official-posts {
  font-size: 24rpx;
  color: var(--text-400);
}
.official-tag {
  font-size: 18rpx;
  font-weight: 700;
  padding: 2rpx 12rpx;
  border-radius: 999rpx;
  background: var(--brand-50);
  color: var(--brand-500);
}
.post-card {
  background: var(--card);
  border: 2rpx solid var(--border);
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}
.post-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.post-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: var(--secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  color: var(--icon-muted);
  flex-shrink: 0;
}
.post-author-info {
  flex: 1;
}
.post-author {
  font-size: 26rpx;
  font-weight: 600;
  color: var(--foreground);
  display: block;
}
.post-time {
  font-size: 20rpx;
  color: var(--text-400);
}
.post-topic-tag {
  font-size: 20rpx;
  color: var(--primary);
  background: var(--brand-50);
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
}
.post-content {
  font-size: 28rpx;
  color: var(--text-600);
  margin-top: 20rpx;
  display: block;
  line-height: 1.6;
}
.post-images {
  display: flex;
  gap: 12rpx;
  margin-top: 16rpx;
}
.post-image-placeholder {
  width: 184rpx;
  height: 184rpx;
  border-radius: 16rpx;
  background: var(--background-200);
}
.post-actions {
  display: flex;
  gap: 32rpx;
  margin-top: 20rpx;
  padding-top: 16rpx;
  border-top: 2rpx solid var(--border);
}
.post-action {
  font-size: 24rpx;
  color: var(--text-400);
}
.bottom-spacer {
  height: 40rpx;
}
</style>
