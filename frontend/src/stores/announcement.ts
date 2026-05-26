import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface AnnouncementVO {
  announcementId: string
  gameCode: string
  title: string
  contentHtml: string
  tabBanner: string
  foldBanner: string
  isRedDot: number
  startTimeMs: number
  endTimeMs: number
  startTimeStr: string
  endTimeStr: string
  announcementType: string
  priority: number
  createTime: string
}

export const useAnnouncementStore = defineStore('announcement', () => {
  const announcements = ref<AnnouncementVO[]>([])
  const selectedAnnouncement = ref<AnnouncementVO | null>(null)
  const loading = ref(false)

  function setAnnouncements(list: AnnouncementVO[]) {
    announcements.value = list
  }

  function setSelectedAnnouncement(announcement: AnnouncementVO | null) {
    selectedAnnouncement.value = announcement
  }

  function setLoading(isLoading: boolean) {
    loading.value = isLoading
  }

  return {
    announcements,
    selectedAnnouncement,
    loading,
    setAnnouncements,
    setSelectedAnnouncement,
    setLoading
  }
})