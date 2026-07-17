<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="not-found-icon">404</div>
      <h2 class="not-found-title">页面不存在</h2>
      <p class="not-found-desc">抱歉，您访问的页面不存在或已被移除</p>
      <router-link to="/dashboard" class="btn btn-primary">返回首页</router-link>
    </div>
  </div>
</template>

<style scoped lang="css">
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
}

.not-found-content {
  text-align: center;
}

.not-found-icon {
  font-size: 96px;
  font-weight: 800;
  color: var(--background-200);
  line-height: 1;
  margin-bottom: 16px;
  letter-spacing: -4px;
}

.not-found-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-800);
  margin: 0 0 8px;
}

.not-found-desc {
  font-size: 14px;
  color: var(--text-400);
  margin: 0 0 24px;
}
</style>
