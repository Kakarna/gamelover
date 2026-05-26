import request from '@/utils/request'
import type { AnnouncementVO } from '@/stores/announcement'

export interface AnnouncementListResponse {
  code: number
  message: string
  data: AnnouncementVO[]
}

export const announcementApi = {
  getList(gameCode: string = 'WUWA') {
    return request.get<any, AnnouncementListResponse>(
      `/announcement/list`,
      { params: { gameCode } }
    )
  },

  sync() {
    return request.post<any, { code: number; message: string }>(
      `/announcement/sync`,
      {}
    )
  }
}
