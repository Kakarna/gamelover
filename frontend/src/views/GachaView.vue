<script setup lang="ts">
import { ref, computed } from 'vue'
import MainLayout from '@/components/MainLayout.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

interface GachaRecord {
  id: string
  time: string
  name: string
  rarity: number
  type: '角色' | '武器'
  pool: string
}

const selectedPool = ref<string>('all')
const pools = [
  { label: '全部记录', value: 'all' },
  { label: '角色活动唤取', value: 'character_event' },
  { label: '武器活动唤取', value: 'weapon_event' },
  { label: '常驻唤取', value: 'permanent' }
]

const gachaRecords = ref<GachaRecord[]>([
  { id: '1', time: '2026-05-24 14:30:22', name: '绯雪', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { id: '2', time: '2026-05-24 14:30:15', name: '达妮娅', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { id: '3', time: '2026-05-24 14:29:58', name: '通用谐波', rarity: 4, type: '武器', pool: '武器活动唤取' },
  { id: '4', time: '2026-05-24 14:29:45', name: '雪色所映千般未来', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { id: '5', time: '2026-05-24 14:28:30', name: '炽燃之炎', rarity: 3, type: '武器', pool: '武器活动唤取' },
  { id: '6', time: '2026-05-24 14:25:12', name: '折枝', rarity: 4, type: '角色', pool: '角色活动唤取' },
  { id: '7', time: '2026-05-24 14:24:58', name: '秋声', rarity: 4, type: '角色', pool: '角色活动唤取' },
  { id: '8', time: '2026-05-24 14:20:33', name: '无名笛', rarity: 4, type: '武器', pool: '武器活动唤取' },
  { id: '9', time: '2026-05-24 14:18:20', name: '冽', rarity: 5, type: '角色', pool: '角色活动唤取' },
  { id: '10', time: '2026-05-24 14:15:08', name: '霜白', rarity: 3, type: '武器', pool: '武器活动唤取' }
])

const stats = computed(() => {
  const total = gachaRecords.value.length
  const ssr = gachaRecords.value.filter(r => r.rarity === 5).length
  const sr = gachaRecords.value.filter(r => r.rarity === 4).length
  const rate = total > 0 ? ((ssr / total) * 100).toFixed(2) : '0.00'
  return { total, ssr, sr, rate }
})

const getRarityColor = (rarity: number) => {
  const colors: Record<number, string> = {
    5: 'text-orange-400',
    4: 'text-purple-400',
    3: 'text-blue-400',
    2: 'text-green-400'
  }
  return colors[rarity] || 'text-gray-400'
}

const getRarityBg = (rarity: number) => {
  const colors: Record<number, string> = {
    5: 'from-orange-500/20 to-yellow-500/20 border-orange-500/30',
    4: 'from-purple-500/20 to-pink-500/20 border-purple-500/30',
    3: 'from-blue-500/20 to-cyan-500/20 border-blue-500/30',
    2: 'from-green-500/20 to-emerald-500/20 border-green-500/30'
  }
  return colors[rarity] || 'from-gray-500/20 to-gray-600/20 border-gray-500/30'
}

const getRarityTagType = (rarity: number) => {
  const types: Record<number, any> = {
    5: 'warning',
    4: 'info',
    3: 'default'
  }
  return types[rarity] || 'default'
}
</script>

<template>
  <MainLayout>
    <div class="gacha-view">
      <div class="page-header">
        <div class="page-title-section">
          <h1 class="page-title">
            <span class="gradient-text">🎰 抽卡记录</span>
          </h1>
          <p class="page-subtitle">记录你的每一次抽卡，追踪你的欧气历程</p>
        </div>
      </div>

      <n-grid :cols="4" :x-gap="16" :y-gap="16" class="stats-grid">
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-purple-500 to-pink-500">
                🎰
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.total }}</div>
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
                <div class="stat-value">{{ stats.ssr }}</div>
                <div class="stat-label">SSR 数量</div>
              </div>
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card glass-card">
            <div class="stat-content">
              <div class="stat-icon bg-gradient-to-br from-purple-500 to-purple-400">
                🌟
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.sr }}</div>
                <div class="stat-label">SR 数量</div>
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
                <div class="stat-value">{{ stats.rate }}%</div>
                <div class="stat-label">SSR 概率</div>
              </div>
            </div>
          </n-card>
        </n-gi>
      </n-grid>

      <div class="filter-section glass-card">
        <div class="filter-row">
          <n-select
            v-model:value="selectedPool"
            :options="pools"
            size="large"
            class="pool-selector"
            :style="{ width: '200px' }"
          />
          <n-button type="primary" class="sync-button">
            🔄 同步数据
          </n-button>
        </div>
      </div>

      <n-card class="glass-card">
        <n-table :bordered="false" :single-line="false" class="gacha-table">
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
            <tr v-for="item in gachaRecords" :key="item.id">
              <td class="text-sm text-gray-400">{{ item.time }}</td>
              <td>
                <span :class="getRarityColor(item.rarity)" class="font-semibold">
                  {{ '★'.repeat(item.rarity) }} {{ item.name }}
                </span>
              </td>
              <td>
                <n-tag :type="getRarityTagType(item.rarity)" size="small">
                  {{ '★'.repeat(item.rarity) }}
                </n-tag>
              </td>
              <td>
                <span class="text-sm">{{ item.type }}</span>
              </td>
              <td class="text-sm text-gray-400">{{ item.pool }}</td>
            </tr>
          </tbody>
        </n-table>
      </n-card>
    </div>
  </MainLayout>
</template>

<style scoped>
.gacha-view {
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

.stats-grid {
  margin-bottom: 24px;
}

.stat-card {
  padding: 0;
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
  font-size: 28px;
  font-weight: 700;
  color: #f8fafc;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #94a3b8;
  margin-top: 4px;
}

.filter-section {
  padding: 16px 24px;
  margin-bottom: 24px;
}

.filter-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sync-button {
  background: linear-gradient(135deg, #6366f1, #ec4899) !important;
  border: none !important;
}

.gacha-table {
  margin-top: 16px;
}

.table-header {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
}
</style>
