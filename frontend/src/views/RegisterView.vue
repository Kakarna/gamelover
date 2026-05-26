<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '@/api/user'
import { useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  nickname: ''
})

const loading = ref(false)

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.password) {
    message.warning('请填写必填项')
    return
  }

  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    message.warning('两次密码输入不一致')
    return
  }

  if (registerForm.value.password.length < 6) {
    message.warning('密码长度不能少于6位')
    return
  }

  loading.value = true
  try {
    const res = await userApi.register({
      username: registerForm.value.username,
      password: registerForm.value.password,
      email: registerForm.value.email || undefined,
      phone: registerForm.value.phone || undefined,
      nickname: registerForm.value.nickname || undefined
    })
    if (res.code === 200) {
      message.success('注册成功，请登录')
      router.push('/login')
    } else {
      message.error(res.message || '注册失败')
    }
  } catch (error: any) {
    if (error.response?.data?.message) {
      message.error(error.response.data.message)
    } else {
      message.error('注册失败，请检查网络')
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
          <div class="auth-icon">✨</div>
          <h1 class="auth-title">
            <span class="gradient-text">加入我们</span>
          </h1>
          <p class="auth-subtitle">创建 GameLover 账号</p>
        </div>

        <n-form :model="registerForm" class="auth-form">
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item path="username" label="用户名">
                <n-input
                  v-model:value="registerForm.username"
                  placeholder="请输入用户名"
                  size="large"
                  class="auth-input"
                />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item path="nickname" label="昵称">
                <n-input
                  v-model:value="registerForm.nickname"
                  placeholder="请输入昵称"
                  size="large"
                  class="auth-input"
                />
              </n-form-item>
            </n-gi>
          </n-grid>

          <n-form-item path="password" label="密码">
            <n-input
              v-model:value="registerForm.password"
              type="password"
              placeholder="请输入密码（至少6位）"
              size="large"
              show-password-on="click"
              class="auth-input"
            />
          </n-form-item>

          <n-form-item path="confirmPassword" label="确认密码">
            <n-input
              v-model:value="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              size="large"
              show-password-on="click"
              class="auth-input"
              @keyup.enter="handleRegister"
            />
          </n-form-item>

          <n-form-item path="email" label="邮箱">
            <n-input
              v-model:value="registerForm.email"
              placeholder="请输入邮箱（选填）"
              size="large"
              class="auth-input"
            />
          </n-form-item>

          <n-form-item path="phone" label="手机号">
            <n-input
              v-model:value="registerForm.phone"
              placeholder="请输入手机号（选填）"
              size="large"
              class="auth-input"
            />
          </n-form-item>

          <n-button
            type="primary"
            size="large"
            block
            :loading="loading"
            class="auth-button"
            @click="handleRegister"
          >
            注册
          </n-button>

          <div class="auth-footer">
            <span class="text-gray-400">已有账号？</span>
            <n-button text type="primary" tag="a" href="/login">
              立即登录
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
  max-width: 480px;
  padding: 20px;
  max-height: 90vh;
  overflow-y: auto;
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

.auth-button {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #6366f1, #ec4899) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  transition: all 0.3s ease;
  margin-top: 16px;
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