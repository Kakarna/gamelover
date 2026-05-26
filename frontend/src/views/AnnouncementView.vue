<script setup lang="ts">
import { ref, onMounted } from 'vue'
import MainLayout from '@/components/MainLayout.vue'
import { useAnnouncementStore } from '@/stores/announcement'
import { announcementApi } from '@/api/announcement'
import { useMessage } from 'naive-ui'
import { useUserStore } from '@/stores/user'
import { RefreshCw, Bell, Calendar, Clock, ChevronRight } from 'lucide-vue-next'

const announcementStore = useAnnouncementStore()
const userStore = useUserStore()
const message = useMessage()

const showModal = ref(false)

const selectedType = ref<string>('all')
const announcementTypes = ['全部', '版本更新', '活动', '维护', '角色']

const getTypeFromContent = (content: string): string => {
  if (!content) return '其他'
  const lowerContent = content.toLowerCase()
  if (lowerContent.includes('版本') || lowerContent.includes('维护') || lowerContent.includes('更新')) {
    return '版本更新'
  } else if (lowerContent.includes('活动') || lowerContent.includes('任务')) {
    return '活动'
  } else if (lowerContent.includes('修复') || lowerContent.includes('bug')) {
    return '维护'
  }
  return '其他'
}

const getTypeTagType = (type: string) => {
  const types: Record<string, any> = {
    '版本更新': 'warning',
    '活动': 'success',
    '维护': 'error',
    '角色': 'info',
    '其他': 'default'
  }
  return types[type] || 'default'
}

const getTypeColor = (type: string): string => {
  const colors: Record<string, string> = {
    '版本更新': 'var(--persona-gold)',
    '活动': '#10B981',
    '维护': '#EF4444',
    '角色': '#3B82F6',
    '其他': 'var(--persona-silver)'
  }
  return colors[type] || 'var(--persona-silver)'
}

const loadAnnouncements = async () => {
  try {
    announcementStore.setLoading(true)
    const res = await announcementApi.getList(userStore.selectedGame)
    if (res.code === 200) {
      announcementStore.setAnnouncements(res.data)
    } else {
      message.error(res.message || '获取公告失败')
    }
  } catch (error) {
    message.error('获取公告失败，请检查网络')
  } finally {
    announcementStore.setLoading(false)
  }
}

const handleSync = async () => {
  try {
    const res = await announcementApi.sync()
    if (res.code === 200) {
      message.success('同步成功')
      await loadAnnouncements()
    } else {
      message.error(res.message || '同步失败')
    }
  } catch (error) {
    message.error('同步失败，请检查网络')
  }
}

const filteredAnnouncements = () => {
  if (selectedType.value === 'all') {
    return announcementStore.announcements
  }
  return announcementStore.announcements.filter(item => {
    const type = getTypeFromContent(item.contentHtml)
    return type === selectedType.value
  })
}

const viewAnnouncement = (announcement: any) => {
  announcementStore.setSelectedAnnouncement(announcement)
  showModal.value = true
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<template>
  <MainLayout>
    <div class="announcement-view">
      <div class="page-header">
        <div class="page-title-section">
          <div class="title-icon">
            <Bell :size="32" />
          </div>
          <div class="title-content">
            <h1 class="page-title">
              <span class="gradient-text-mystic">游戏公告</span>
            </h1>
            <div class="title-divider"></div>
            <p class="page-subtitle">了解最新游戏动态，不错过任何重要信息</p>
          </div>
        </div>
        <n-button type="primary" class="sync-button" @click="handleSync">
          <template #icon>
            <RefreshCw :size="16" />
          </template>
          同步数据
        </n-button>
      </div>

      <div class="filter-section tarot-card">
        <div class="filter-label">
          <span class="filter-icon">◆</span>
          <span>筛选类型</span>
        </div>
        <div class="filter-tabs">
          <button
            v-for="(type, index) in announcementTypes"
            :key="index"
            :class="['filter-tab', { active: selectedType === (index === 0 ? 'all' : type) }]"
            @click="selectedType = index === 0 ? 'all' : type"
          >
            {{ type }}
          </button>
        </div>
      </div>

      <n-spin :show="announcementStore.loading">
        <div class="announcement-list">
          <div
            v-for="item in filteredAnnouncements()"
            :key="item.announcementId"
            class="announcement-item tarot-card"
            @click="viewAnnouncement(item)"
          >
            <div v-if="item.foldBanner" class="announcement-image-wrapper">
              <img :src="item.foldBanner" :alt="item.title" class="announcement-image" />
            </div>
            <div class="announcement-main">
              <div class="announcement-header">
                <div class="type-indicator" :style="{ backgroundColor: getTypeColor(getTypeFromContent(item.contentHtml)) }"></div>
                <n-tag
                  :type="getTypeTagType(getTypeFromContent(item.contentHtml))"
                  size="small"
                  class="type-tag"
                  :bordered="false"
                >
                  {{ getTypeFromContent(item.contentHtml) }}
                </n-tag>
                <n-badge v-if="item.isRedDot === 1" type="error" :value="'紧急'" :max="99" />
              </div>

              <h3 class="announcement-title">{{ item.title }}</h3>

              <div class="announcement-meta">
                <span class="meta-item">
                  <Calendar :size="14" class="meta-icon" />
                  {{ item.startTimeStr }}
                </span>
                <span class="meta-item" v-if="item.endTimeStr">
                  <Clock :size="14" class="meta-icon" />
                  {{ item.endTimeStr }}
                </span>
              </div>
            </div>

            <div class="announcement-arrow">
              <ChevronRight :size="24" />
            </div>
          </div>

          <div v-if="filteredAnnouncements().length === 0 && !announcementStore.loading" class="empty-state tarot-card">
            <div class="empty-icon">
              <Bell :size="48" />
            </div>
            <div class="empty-text">暂无公告</div>
            <div class="empty-subtext">敬请期待未来的更新</div>
          </div>
        </div>
      </n-spin>

      <n-modal
        v-model:show="showModal"
        preset="card"
        class="announcement-modal"
        :style="{ maxWidth: '800px' }"
        :mask-closable="true"
        :bordered="false"
      >
        <template #header>
          <div class="modal-header-custom">
            <div class="modal-title-icon">
              <Bell :size="20" />
            </div>
            <span class="gradient-text-mystic">公告详情</span>
          </div>
        </template>
        <div v-if="announcementStore.selectedAnnouncement">
          <div v-if="announcementStore.selectedAnnouncement.tabBanner" class="modal-banner">
            <img :src="announcementStore.selectedAnnouncement.tabBanner" :alt="announcementStore.selectedAnnouncement.title" class="banner-image" />
          </div>
          <div class="modal-header-info">
            <h2 class="modal-title">{{ announcementStore.selectedAnnouncement.title }}</h2>
            <div class="modal-meta">
              <n-tag :type="getTypeTagType(getTypeFromContent(announcementStore.selectedAnnouncement.contentHtml))" :bordered="false">
                {{ getTypeFromContent(announcementStore.selectedAnnouncement.contentHtml) }}
              </n-tag>
              <span class="modal-time">
                <Calendar :size="14" />
                {{ announcementStore.selectedAnnouncement.startTimeStr }}
              </span>
            </div>
          </div>
          <div class="divider-mystic"></div>
          <div class="modal-content" v-html="announcementStore.selectedAnnouncement.contentHtml"></div>
        </div>
      </n-modal>
    </div>
  </MainLayout>
</template>

<style scoped>
.announcement-view {
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

.page-title-section {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.title-icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(212, 168, 75, 0.2), rgba(74, 63, 107, 0.2));
  border: 1px solid rgba(212, 168, 75, 0.3);
  border-radius: 12px;
  color: var(--persona-gold);
}

.title-content {
  padding-top: 4px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px 0;
  line-height: 1.2;
}

.title-divider {
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, var(--persona-gold), transparent);
  margin-bottom: 8px;
}

.page-subtitle {
  color: var(--persona-silver);
  font-size: 14px;
  margin: 0;
}

.sync-button {
  background: linear-gradient(135deg, var(--persona-purple), var(--color-primary)) !important;
  border: 1px solid rgba(212, 168, 75, 0.3) !important;
  color: var(--persona-gold-light) !important;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
  font-size: 13px;
}

.sync-button:hover {
  border-color: var(--persona-gold) !important;
  box-shadow: 0 0 20px rgba(212, 168, 75, 0.3);
}

.filter-section {
  padding: 20px 24px;
  margin-bottom: 24px;
}

.filter-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 600;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 16px;
}

.filter-icon {
  color: var(--persona-gold);
  font-size: 8px;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-tab {
  padding: 8px 18px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(212, 168, 75, 0.15);
  color: var(--persona-silver);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

.filter-tab:hover {
  background: rgba(212, 168, 75, 0.08);
  border-color: rgba(212, 168, 75, 0.3);
  color: var(--persona-gold-light);
}

.filter-tab.active {
  background: linear-gradient(135deg, rgba(74, 63, 107, 0.5), rgba(212, 168, 75, 0.2));
  border-color: var(--persona-gold);
  color: var(--persona-gold-light);
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.announcement-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  cursor: pointer;
}

.announcement-image-wrapper {
  flex-shrink: 0;
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid rgba(212, 168, 75, 0.2);
  margin-right: 16px;
}

.announcement-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.announcement-main {
  flex: 1;
}

.announcement-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.type-indicator {
  width: 4px;
  height: 16px;
  border-radius: 2px;
}

.type-tag {
  font-size: 11px !important;
  letter-spacing: 0.5px;
}

.announcement-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--color-text);
  margin: 0 0 12px 0;
  transition: color 0.3s ease;
  letter-spacing: 0.5px;
}

.announcement-item:hover .announcement-title {
  color: var(--persona-gold);
}

.announcement-meta {
  display: flex;
  gap: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--persona-silver);
}

.meta-icon {
  opacity: 0.7;
}

.announcement-arrow {
  color: var(--persona-silver);
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.4s cubic-bezier(0.23, 1, 0.32, 1);
}

.announcement-item:hover .announcement-arrow {
  opacity: 1;
  transform: translateX(0);
  color: var(--persona-gold);
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  color: var(--persona-silver);
  opacity: 0.5;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--persona-gold);
  margin-bottom: 8px;
}

.empty-subtext {
  font-size: 14px;
  color: var(--persona-silver);
}

.modal-header-custom {
  display: flex;
  align-items: center;
  gap: 12px;
}

.modal-title-icon {
  color: var(--persona-gold);
}

.modal-banner {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 16px;
  border: 1px solid rgba(212, 168, 75, 0.2);
}

.banner-image {
  width: 100%;
  height: auto;
  display: block;
}

.modal-header-info {
  margin-bottom: 16px;
}

.modal-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text);
  margin: 0 0 12px 0;
}

.modal-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.modal-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--persona-silver);
}

.modal-content {
  color: var(--persona-silver);
  line-height: 1.8;
  font-size: 14px;
}

.modal-content :deep(div) {
  margin-bottom: 12px;
}

.modal-content :deep(strong) {
  color: var(--persona-gold-light);
}

.modal-content :deep(p) {
  margin-bottom: 8px;
}

:deep(.announcement-modal) {
  --n-border: 1px solid rgba(212, 168, 75, 0.2) !important;
  --n-header-border: none !important;
  --n-color: rgba(26, 26, 62, 0.95) !important;
  --n-title-text-color: var(--persona-gold) !important;
  --n-action-border-radius: 4px !important;
}
</style>