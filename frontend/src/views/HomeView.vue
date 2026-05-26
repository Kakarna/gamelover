<script setup lang="ts">
import { ref } from 'vue'
import MainLayout from '@/components/MainLayout.vue'

const stats = ref([
  { label: '总抽卡次数', value: '1,288', icon: '🎰', color: 'from-purple-500 to-pink-500' },
  { label: 'SSR 数量', value: '89', icon: '⭐', color: 'from-orange-500 to-yellow-500' },
  { label: '角色数量', value: '45', icon: '🧝', color: 'from-blue-500 to-cyan-500' },
  { label: '绑定账号', value: '2', icon: '🎮', color: 'from-green-500 to-emerald-500' },
])

const recentGacha = ref([
  { time: '2026-05-24 14:30:22', name: '绯雪', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { time: '2026-05-24 14:30:15', name: '达妮娅', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { time: '2026-05-24 14:29:58', name: '通用谐波', rarity: 4, type: '武器', pool: '武器活动唤取' },
  { time: '2026-05-24 14:29:45', name: '雪色所映千般未来', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { time: '2026-05-24 14:28:30', name: '炽燃之炎', rarity: 3, type: '武器', pool: '武器活动唤取' },
])

const announcements = ref([
  {
    title: '「自星海尽处回响」3.3版本更新预告',
    date: '2026-04-28',
    type: '版本更新',
    urgent: true,
  },
  {
    title: '五一活动「漂泊者的假日」开启',
    date: '2026-04-25',
    type: '活动',
    urgent: false,
  },
  {
    title: '5月版本更新维护公告',
    date: '2026-04-20',
    type: '维护',
    urgent: false,
  },
])

const getRarityColor = (rarity: number) => {
  const colors = {
    5: 'text-orange-400',
    4: 'text-purple-400',
    3: 'text-blue-400',
    2: 'text-green-400',
    1: 'text-gray-400',
  }
  return colors[rarity as keyof typeof colors] || colors[1]
}

const getRarityBg = (rarity: number) => {
  const colors = {
    5: 'from-orange-500/20 to-yellow-500/20 border-orange-500/30',
    4: 'from-purple-500/20 to-pink-500/20 border-purple-500/30',
    3: 'from-blue-500/20 to-cyan-500/20 border-blue-500/30',
    2: 'from-green-500/20 to-emerald-500/20 border-green-500/30',
    1: 'from-gray-500/20 to-gray-600/20 border-gray-500/30',
  }
  return colors[rarity as keyof typeof colors] || colors[1]
}
</script>

<template>
  <MainLayout>
    <div class="home-view">
      <n-grid :cols="1" :x-gap="24" :y-gap="24">
        <n-gi>
          <n-card class="welcome-card glass-card">
            <div class="welcome-content">
              <div class="welcome-text">
                <h1 class="welcome-title">
                  <span class="gradient-text">欢迎回来，旅行者！✨</span>
                </h1>
                <p class="welcome-desc">
                  今天也要欧气满满哦~
                </p>
              </div>
              <div class="welcome-character">
                <div class="character-avatar">🌟</div>
              </div>
            </div>
          </n-card>
        </n-gi>

        <n-gi>
          <div class="section-title">
            <h2 class="section-title-text">📊 数据概览</h2>
          </div>
        </n-gi>

        <n-gi>
          <n-grid :cols="4" :x-gap="16" :y-gap="16">
            <n-gi v-for="(stat, index) in stats" :key="index">
              <n-card class="stat-card glass-card">
                <div class="stat-content">
                  <div
                    class="stat-icon"
                    :class="`bg-gradient-to-br ${stat.color}`"
                  >
                    {{ stat.icon }}
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ stat.value }}</div>
                    <div class="stat-label">{{ stat.label }}</div>
                  </div>
                </div>
              </n-card>
            </n-gi>
          </n-grid>
        </n-gi>

        <n-gi>
          <div class="section-title">
            <h2 class="section-title-text">🎰 最近抽卡</h2>
            <n-button quaternary type="primary" size="small">
              查看全部
            </n-button>
          </div>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <n-table :bordered="false" :single-line="false">
              <template #header>
                <div class="table-header">抽卡记录</div>
              </template>
              <thead>
                <tr>
                  <th>时间</th>
                  <th>名称</th>
                  <th>稀有度</th>
                  <th>类型</th>
                  <th>卡池</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in recentGacha" :key="index">
                  <td class="text-sm text-gray-400">{{ item.time }}</td>
                  <td>
                    <span :class="getRarityColor(item.rarity)" class="font-semibold">
                      {{ '★'.repeat(item.rarity) }} {{ item.name }}
                    </span>
                  </td>
                  <td>
                    <n-tag
                      :type="item.rarity === 5 ? 'warning' : item.rarity === 4 ? 'info' : 'default'"
                      size="small"
                    >
                      {{ '★'.repeat(item.rarity) }}
                    </n-tag>
                  </td>
                  <td>
                    <n-tag size="small">{{ item.type }}</n-tag>
                  </td>
                  <td class="text-sm text-gray-400">{{ item.pool }}</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>
        </n-gi>

        <n-gi>
          <div class="section-title">
            <h2 class="section-title-text">📢 最新公告</h2>
            <n-button quaternary type="primary" size="small">
              查看全部
            </n-button>
          </div>
        </n-gi>

        <n-gi>
          <n-grid :cols="1" :x-gap="16" :y-gap="16">
            <n-gi v-for="(item, index) in announcements" :key="index">
              <n-card class="announcement-card glass-card" hoverable>
                <div class="announcement-content">
                  <div class="announcement-main">
                    <div class="announcement-title">
                      <n-badge v-if="item.urgent" dot type="error">
                        <span class="announcement-text">{{ item.title }}</span>
                      </n-badge>
                      <span v-else class="announcement-text">{{ item.title }}</span>
                    </div>
                    <div class="announcement-meta">
                      <n-tag size="small" :type="item.type === '版本更新' ? 'primary' : 'default'">
                        {{ item.type }}
                      </n-tag>
                      <span class="announcement-date">{{ item.date }}</span>
                    </div>
                  </div>
                  <div class="announcement-arrow">→</div>
                </div>
              </n-card>
            </n-gi>
          </n-grid>
        </n-gi>
      </n-grid>
    </div>
  </MainLayout>
</template>

<style scoped>
.home-view {
  padding: 0;
}

.welcome-card {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(236, 72, 153, 0.1));
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.welcome-desc {
  color: #94a3b8;
  font-size: 14px;
}

.character-avatar {
  font-size: 64px;
  animation: float 3s ease-in-out infinite;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 24px 0 16px;
}

.section-title-text {
  font-size: 18px;
  font-weight: 600;
  color: #f8fafc;
}

.stat-card {
  cursor: pointer;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #f8fafc;
}

.stat-label {
  font-size: 12px;
  color: #94a3b8;
  margin-top: 4px;
}

.table-header {
  font-weight: 600;
  color: #f8fafc;
}

.announcement-card {
  cursor: pointer;
}

.announcement-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.announcement-title {
  margin-bottom: 8px;
}

.announcement-text {
  color: #f8fafc;
  font-weight: 500;
}

.announcement-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.announcement-date {
  color: #94a3b8;
  font-size: 12px;
}

.announcement-arrow {
  color: #6366f1;
  font-size: 20px;
  transition: transform 0.3s;
}

.announcement-card:hover .announcement-arrow {
  transform: translateX(4px);
}

:deep(.n-card) {
  background: transparent;
}

:deep(.n-card-header) {
  padding: 12px 16px;
}

:deep(.n-card__content) {
  padding: 16px;
}

:deep(.n-table) {
  background: transparent;
}

:deep(.n-table-thead th) {
  background: rgba(99, 102, 241, 0.1);
  color: #f8fafc;
  font-weight: 600;
}

:deep(.n-table-tbody tr) {
  transition: background 0.3s;
}

:deep(.n-table-tbody tr:hover) {
  background: rgba(99, 102, 241, 0.05);
}

:deep(.n-table-tbody td) {
  color: #f8fafc;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}
</style>
