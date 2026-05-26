<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api/user'
import { useMessage } from 'naive-ui'

const router = useRouter()
const userStore = useUserStore()
const message = useMessage()

const loginForm = ref({
  account: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.account || !loginForm.value.password) {
    message.warning('请输入账号和密码')
    return
  }

  loading.value = true
  try {
    const res = await userApi.login(loginForm.value)
    if (res.code === 200) {
      userStore.setToken(res.data.token)
      userStore.setUserInfo(res.data.userInfo)
      message.success('登录成功')
      router.push('/')
    } else {
      message.error(res.message || '登录失败')
    }
  } catch (error: any) {
    if (error.response?.data?.message) {
      message.error(error.response.data.message)
    } else {
      message.error('登录失败，请检查网络')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="animated-background">
      <div class="gradient-circle circle-1"></div>
      <div class="gradient-circle circle-2"></div>
      <div class="gradient-circle circle-3"></div>
    </div>

    <div class="auth-container">
      <n-card class="auth-card glass-card">
        <div class="auth-header">
          <div class="auth-icon">🎮</div>
          <h1 class="auth-title">
            <span class="gradient-text">欢迎回来</span>
          </h1>
          <p class="auth-subtitle">登录 GameLover 开始探索</p>
        </div>

        <n-form :model="loginForm" class="auth-form">
          <n-form-item path="account" label="账号">
            <n-input
              v-model:value="loginForm.account"
              placeholder="请输入用户名或邮箱"
              size="large"
              class="auth-input"
            />
          </n-form-item>

          <n-form-item path="password" label="密码">
            <n-input
              v-model:value="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password-on="click"
              class="auth-input"
              @keyup.enter="handleLogin"
            />
          </n-form-item>

          <div class="auth-options">
            <n-checkbox>记住我</n-checkbox>
            <n-button text type="primary" tag="a" href="#">忘记密码？</n-button>
          </div>

          <n-button
            type="primary"
            size="large"
            block
            :loading="loading"
            class="auth-button"
            @click="handleLogin"
          >
            登录
          </n-button>

          <div class="auth-footer">
            <span class="text-gray-400">还没有账号？</span>
            <n-button text type="primary" tag="a" href="/register">
              立即注册
            </n-button>
          </div>
        </n-form>
      </n-card>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.auth-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: 20px;
}

.auth-card {
  padding: 40px 32px;
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.auth-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.auth-subtitle {
  color: #94a3b8;
  font-size: 14px;
}

.auth-form {
  margin-top: 24px;
}

.auth-input :deep(.n-input__input-el) {
  background: rgba(255, 255, 255, 0.05) !important;
}

.auth-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.auth-button {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #6366f1, #ec4899) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  transition: all 0.3s ease;
}

.auth-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.5);
}

.auth-footer {
  margin-top: 24px;
  text-align: center;
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>