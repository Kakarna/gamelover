<script setup lang="ts">
import { ref } from 'vue'
import MainLayout from '@/components/MainLayout.vue'
import { useUserStore } from '@/stores/user'
import { useMessage } from 'naive-ui'

const userStore = useUserStore()
const message = useMessage()

const gachaStats = ref({
  totalPulls: 1288,
  totalSpent: 0,
  ssrCount: 89,
  srCount: 456,
  rCount: 743,
  ssrRate: (89 / 1288 * 100).toFixed(2),
  mostUsedPool: '角色活动唤取',
  lastPullDate: '2026-05-24 14:30:22'
})

const poolStats = ref([
  { name: '角色活动唤取', pulls: 800, ssr: 56, rate: (56 / 800 * 100).toFixed(2) },
  { name: '武器活动唤取', pulls: 300, ssr: 20, rate: (20 / 300 * 100).toFixed(2) },
  { name: '常驻唤取', pulls: 188, ssr: 13, rate: (13 / 188 * 100).toFixed(2) }
])

const recentTrend = ref([
  { date: '05-20', pulls: 68, ssr: 5 },
  { date: '05-21', pulls: 45, ssr: 3 },
  { date: '05-22', pulls: 120, ssr: 8 },
  { date: '05-23', pulls: 90, ssr: 7 },
  { date: '05-24', pulls: 105, ssr: 6 }
])

const syncData = () => {
  message.success('数据同步成功')
}
</script>

<template>
  <MainLayout>
    <div class="statistics-view">
      <div class="page-header">
        <div class="page-title-section">
          <h1 class="page-title">
            <span class="gradient-text">📊 数据统计</span>
          </h1>
          <p class="page-subtitle">全面了解你的抽卡数据，洞察你的欧气分布</p>
        </div>
        <n-button type="primary" class="sync-button" @click="syncData">
          🔄 同步数据
        </n-button>
      </div>

      <n-grid :cols="4" :x-gap="16" :y-gap="16" class="overview-grid">
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-purple-500 to-pink-500">
                🎰
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ gachaStats.totalPulls.toLocaleString() }}</div>
                <div class="stat-label">总抽卡次数</div>
              </div>
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-orange-500 to-yellow-500">
                ⭐
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ gachaStats.ssrCount }}</div>
                <div class="stat-label">SSR 总数</div>
              </div>
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-pink-500 to-rose-500">
                🌟
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ gachaStats.ssrRate }}%</div>
                <div class="stat-label">SSR 概率</div>
              </div>
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-green-500 to-emerald-500">
                📈
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ gachaStats.mostUsedPool }}</div>
                <div class="stat-label">常用卡池</div>
              </div>
            </div>
          </n-card>
        </n-gi>
      </n-grid>

      <n-grid :cols="2" :x-gap="24" :y-gap="24">
        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">📊 卡池统计</div>
            </template>
            <n-table :bordered="false" :single-line="false">
              <thead>
                <tr>
                  <th>卡池名称</th>
                  <th>抽卡次数</th>
                  <th>SSR 数量</th>
                  <th>SSR 概率</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="pool in poolStats" :key="pool.name">
                  <td class="font-semibold">{{ pool.name }}</td>
                  <td class="text-gray-400">{{ pool.pulls }}</td>
                  <td class="text-orange-400">{{ pool.ssr }}</td>
                  <td class="text-green-400">{{ pool.rate }}%</td>
                </tr>
              </tbody>
            </n-table>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">📈 近期趋势</div>
            </template>
            <div class="trend-chart">
              <div
                v-for="(day, index) in recentTrend"
                :key="index"
                class="trend-item"
              >
                <div class="trend-bar-container">
                  <div
                    class="trend-bar"
                    :style="{
                      height: (day.pulls / 120 * 100) + '%',
                      background: `linear-gradient(180deg, rgba(99, 102, 241, 0.8), rgba(99, 102, 241, 0.2))`
                    }"
                  >
                    <span class="trend-value">{{ day.pulls }}</span>
                  </div>
                </div>
                <div class="trend-label">{{ day.date }}</div>
                <div class="trend-ssr">
                  <span class="ssr-star">⭐</span>
                  {{ day.ssr }}
                </div>
              </div>
            </div>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">🌟 稀有度分布</div>
            </template>
            <div class="rarity-distribution">
              <div class="rarity-item">
                <div class="rarity-bar">
                  <div
                    class="rarity-fill rarity-ssr"
                    :style="{ width: (gachaStats.ssrCount / gachaStats.totalPulls * 100) + '%' }"
                  ></div>
                </div>
                <div class="rarity-info">
                  <span class="rarity-name">SSR</span>
                  <span class="rarity-count">{{ gachaStats.ssrCount }}</span>
                  <span class="rarity-rate">({{ (gachaStats.ssrCount / gachaStats.totalPulls * 100).toFixed(2) }}%)</span>
                </div>
              </div>
              <div class="rarity-item">
                <div class="rarity-bar">
                  <div
                    class="rarity-fill rarity-sr"
                    :style="{ width: (gachaStats.srCount / gachaStats.totalPulls * 100) + '%' }"
                  ></div>
                </div>
                <div class="rarity-info">
                  <span class="rarity-name">SR</span>
                  <span class="rarity-count">{{ gachaStats.srCount }}</span>
                  <span class="rarity-rate">({{ (gachaStats.srCount / gachaStats.totalPulls * 100).toFixed(2) }}%)</span>
                </div>
              </div>
              <div class="rarity-item">
                <div class="rarity-bar">
                  <div
                    class="rarity-fill rarity-r"
                    :style="{ width: (gachaStats.rCount / gachaStats.totalPulls * 100) + '%' }"
                  ></div>
                </div>
                <div class="rarity-info">
                  <span class="rarity-name">R</span>
                  <span class="rarity-count">{{ gachaStats.rCount }}</span>
                  <span class="rarity-rate">({{ (gachaStats.rCount / gachaStats.totalPulls * 100).toFixed(2) }}%)</span>
                </div>
              </div>
            </div>
          </n-card>
        </n-gi>

        <n-gi>
          <n-card class="glass-card">
            <template #header>
              <div class="card-title">⏰ 最近抽卡</div>
            </template>
            <div class="last-pull">
              <div class="last-pull-info">
                <span class="last-pull-label">最后抽卡时间</span>
                <span class="last-pull-time">{{ gachaStats.lastPullDate }}</span>
              </div>
              <n-button type="primary" size="small">
                查看详情
              </n-button>
            </div>
          </n-card>
        </n-gi>
      </n-grid>
    </div>
  </MainLayout>
</template>

<style scoped>
.statistics-view {
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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

.sync-button {
  background: linear-gradient(135deg, #6366f1, #ec4899) !important;
  border: none !important;
}

.overview-grid {
  margin-bottom: 24px;
}

.stat-card :deep(.n-card__content) {
  padding: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #f8fafc;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #94a3b8;
  margin-top: 4px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
}

.trend-chart {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 200px;
  padding-top: 20px;
}

.trend-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.trend-bar-container {
  width: 40px;
  height: 150px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
}

.trend-bar {
  width: 100%;
  border-radius: 8px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  transition: height 0.3s ease;
}

.trend-value {
  font-size: 12px;
  font-weight: 600;
  color: #f8fafc;
  padding: 4px;
}

.trend-label {
  font-size: 12px;
  color: #94a3b8;
}

.trend-ssr {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #f59e0b;
}

.ssr-star {
  font-size: 10px;
}

.rarity-distribution {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.rarity-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.rarity-bar {
  height: 24px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  overflow: hidden;
}

.rarity-fill {
  height: 100%;
  border-radius: 12px;
  transition: width 0.5s ease;
}

.rarity-ssr {
  background: linear-gradient(90deg, #f59e0b, #fbbf24);
}

.rarity-sr {
  background: linear-gradient(90deg, #8b5cf6, #a78bfa);
}

.rarity-r {
  background: linear-gradient(90deg, #3b82f6, #60a5fa);
}

.rarity-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rarity-name {
  font-size: 14px;
  font-weight: 600;
  color: #f8fafc;
  width: 40px;
}

.rarity-count {
  font-size: 14px;
  color: #94a3b8;
}

.rarity-rate {
  font-size: 12px;
  color: #64748b;
}

.last-pull {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.last-pull-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.last-pull-label {
  font-size: 13px;
  color: #94a3b8;
}

.last-pull-time {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
}
</style>
