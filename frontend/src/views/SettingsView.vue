<script setup lang="ts">
import { ref } from 'vue'
import MainLayout from '@/components/MainLayout.vue'
import { useUserStore } from '@/stores/user'
import { useMessage } from 'naive-ui'

const userStore = useUserStore()
const message = useMessage()

const userInfo = ref({
  username: userStore.userInfo?.username || 'traveler',
  nickname: userStore.userInfo?.nickname || '旅行者',
  email: 'traveler@example.com',
  phone: '138****8888'
})

const boundAccounts = ref([
  { game: 'WUWA', server: '世界服', uid: '123456789', bindTime: '2026-05-01' },
  { game: 'HSR', server: '星神世界', uid: '987654321', bindTime: '2026-05-10' }
])

const notificationSettings = ref({
  gachaNotification: true,
  announcementNotification: true,
  dailyReminder: false,
  emailNotification: true
})

const saveUserInfo = () => {
  message.success('保存成功')
}

const unbindAccount = (game: string) => {
  message.warning(`确定要解除绑定 ${game} 账号吗？`)
}

const testNotification = () => {
  message.success('测试通知发送成功')
}
</script>

<template>
  <MainLayout>
    <div class="settings-view">
      <div class="page-header">
        <div class="page-title-section">
          <h1 class="page-title">
            <span class="gradient-text">⚙️ 设置</span>
          </h1>
          <p class="page-subtitle">管理你的账号设置和偏好配置</p>
        </div>
      </div>

      <n-grid :cols="2" :x-gap="24" :y-gap="24">
        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">👤 基本信息</div>
            </template>
            <n-form :model="userInfo" label-placement="top">
              <n-form-item label="用户名">
                <n-input v-model:value="userInfo.username" placeholder="请输入用户名" />
              </n-form-item>
              <n-form-item label="昵称">
                <n-input v-model:value="userInfo.nickname" placeholder="请输入昵称" />
              </n-form-item>
              <n-form-item label="邮箱">
                <n-input v-model:value="userInfo.email" placeholder="请输入邮箱" />
              </n-form-item>
              <n-form-item label="手机号">
                <n-input v-model:value="userInfo.phone" placeholder="请输入手机号" />
              </n-form-item>
              <n-button type="primary" block @click="saveUserInfo">
                保存修改
              </n-button>
            </n-form>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">🔔 通知设置</div>
            </template>
            <div class="notification-settings">
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">抽卡通知</span>
                  <span class="setting-desc">当获得SSR时发送通知</span>
                </div>
                <n-switch v-model:value="notificationSettings.gachaNotification" />
              </div>
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">公告通知</span>
                  <span class="setting-desc">收到新公告时发送通知</span>
                </div>
                <n-switch v-model:value="notificationSettings.announcementNotification" />
              </div>
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">每日提醒</span>
                  <span class="setting-desc">每日提醒签到和活动</span>
                </div>
                <n-switch v-model:value="notificationSettings.dailyReminder" />
              </div>
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">邮件通知</span>
                  <span class="setting-desc">通过邮件接收重要通知</span>
                </div>
                <n-switch v-model:value="notificationSettings.emailNotification" />
              </div>
              <n-button type="primary" block class="mt-4" @click="testNotification">
                发送测试通知
              </n-button>
            </div>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">🎮 绑定账号</div>
            </template>
            <div class="bound-accounts">
              <div
                v-for="account in boundAccounts"
                :key="account.game"
                class="account-item"
              >
                <div class="account-icon">
                  {{ account.game === 'WUWA' ? '🌊' : '🚂' }}
                </div>
                <div class="account-info">
                  <div class="account-game">{{ account.game === 'WUWA' ? '鸣潮' : '崩铁' }}</div>
                  <div class="account-detail">
                    {{ account.server }} · UID: {{ account.uid }}
                  </div>
                  <div class="account-time">绑定时间: {{ account.bindTime }}</div>
                </div>
                <n-button size="small" type="error" ghost @click="unbindAccount(account.game)">
                  解除
                </n-button>
              </div>
              <n-button type="primary" block class="mt-4">
                + 添加游戏账号
              </n-button>
            </div>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">🔒 安全设置</div>
            </template>
            <div class="security-settings">
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">修改密码</span>
                  <span class="setting-desc">定期更换密码保护账号安全</span>
                </div>
                <n-button size="small" type="primary">修改</n-button>
              </div>
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">两步验证</span>
                  <span class="setting-desc">启用两步验证提高账号安全</span>
                </div>
                <n-button size="small" type="info">启用</n-button>
              </div>
              <div class="setting-item">
                <div class="setting-info">
                  <span class="setting-label">登录日志</span>
                  <span class="setting-desc">查看账号的登录记录</span>
                </div>
                <n-button size="small" type="default">查看</n-button>
              </div>
            </div>
          </n-card>
        </n-gi>
      </n-grid>

      <div class="danger-zone">
        <n-card class="glass-card danger-card">
          <template #header>
            <div class="card-title danger-title">⚠️ 危险区域</div>
          </template>
          <div class="danger-actions">
            <div class="danger-item">
              <div class="danger-info">
                <span class="danger-label">清除缓存</span>
                <span class="danger-desc">清除本地缓存的抽卡记录和游戏数据</span>
              </div>
              <n-button size="small" type="warning">清除</n-button>
            </div>
            <div class="danger-item">
              <div class="danger-info">
                <span class="danger-label">注销账号</span>
                <span class="danger-desc">永久删除你的账号和所有数据</span>
              </div>
              <n-button size="small" type="error">注销</n-button>
            </div>
          </div>
        </n-card>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.settings-view {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.page-subtitle {
  color: #94a3b8;
  font-size: 14px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
}

.notification-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.setting-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.setting-label {
  font-size: 14px;
  font-weight: 600;
  color: #f8fafc;
}

.setting-desc {
  font-size: 12px;
  color: #64748b;
}

.mt-4 {
  margin-top: 16px;
}

.bound-accounts {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.account-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
}

.account-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(236, 72, 153, 0.2));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.account-info {
  flex: 1;
}

.account-game {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
}

.account-detail {
  font-size: 13px;
  color: #94a3b8;
  margin-top: 2px;
}

.account-time {
  font-size: 12px;
  color: #64748b;
  margin-top: 4px;
}

.security-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.danger-zone {
  margin-top: 24px;
}

.danger-card {
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.danger-title {
  color: #ef4444;
}

.danger-actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.danger-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.danger-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.danger-label {
  font-size: 14px;
  font-weight: 600;
  color: #f8fafc;
}

.danger-desc {
  font-size: 12px;
  color: #64748b;
}
</style>
