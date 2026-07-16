<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="brand-icon">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <rect width="48" height="48" rx="12" fill="#2563eb" />
            <path d="M14 24L22 32L34 18" stroke="white" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </div>
        <h1 class="brand-title">管理后台</h1>
        <p class="brand-subtitle">MOYUYO Admin Console</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label>邮箱地址</label>
          <input
            v-model="form.email"
            type="email"
            class="form-input"
            placeholder="请输入邮箱地址"
            autocomplete="email"
          />
        </div>

        <div class="form-group">
          <label>密码</label>
          <div class="password-wrapper">
            <input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              class="form-input"
              placeholder="请输入密码"
              autocomplete="current-password"
            />
            <button type="button" class="eye-toggle" @click="showPassword = !showPassword">
              {{ showPassword ? '🙈' : '👁️' }}
            </button>
          </div>
        </div>

        <div class="form-options">
          <label class="remember-me">
            <input v-model="form.remember" type="checkbox" />
            <span>记住我</span>
          </label>
          <a href="#" class="forgot-link" @click.prevent>忘记密码？</a>
        </div>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>

        <div class="divider">
          <span>或</span>
        </div>

        <button type="button" class="btn sso-btn" @click="handleSSO">
          使用企业SSO登录
        </button>
      </form>

      <p class="login-version">v2.1.0</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  email: '',
  password: '',
  remember: false
})

const showPassword = ref(false)
const loading = ref(false)
const errorMsg = ref('')

async function handleLogin() {
  if (!form.email || !form.password) {
    errorMsg.value = '请输入邮箱和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''
  try {
    // 模拟登录请求
    await new Promise(resolve => setTimeout(resolve, 800))
    // 登录成功
    router.push('/dashboard')
  } catch (e) {
    errorMsg.value = '登录失败，请检查账号密码'
  } finally {
    loading.value = false
  }
}

function handleSSO() {
  // SSO登录占位
  console.log('SSO login triggered')
}
</script>

<style scoped lang="css">
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.brand-icon {
  margin-bottom: 16px;
}

.brand-title {
  font-size: 24px;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0 0 4px 0;
}

.brand-subtitle {
  font-size: 14px;
  color: #8e8e93;
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 6px;
}

.form-input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #e5e5ea;
  border-radius: 8px;
  font-size: 14px;
  color: #1d1d1f;
  background: #f9f9fb;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #2563eb;
  background: #fff;
}

.password-wrapper {
  position: relative;
}

.password-wrapper .form-input {
  padding-right: 40px;
}

.eye-toggle {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  padding: 4px;
  line-height: 1;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
}

.remember-me input {
  width: 16px;
  height: 16px;
  accent-color: #2563eb;
}

.forgot-link {
  font-size: 14px;
  color: #2563eb;
  text-decoration: none;
}

.forgot-link:hover {
  text-decoration: underline;
}

.error-msg {
  background: #fef2f2;
  color: #dc2626;
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 13px;
  text-align: center;
}

.login-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.divider {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #aeaeb2;
  font-size: 13px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e5e5ea;
}

.sso-btn {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: 1px solid #e5e5ea;
  background: #fff;
  color: #333;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.sso-btn:hover {
  background: #f5f5f7;
}

.login-version {
  text-align: center;
  font-size: 12px;
  color: #aeaeb2;
  margin-top: 24px;
}
</style>
