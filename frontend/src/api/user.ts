import request from '@/utils/request'

export interface LoginDTO {
  account: string
  password: string
}

export interface RegisterDTO {
  username: string
  password: string
  email?: string
  phone?: string
  nickname?: string
}

export interface UserBasicVO {
  userUid: number
  username: string
  nickname: string
  avatarUrl?: string
}

export interface LoginVO {
  token: string
  userInfo: UserBasicVO
}

export interface RegisterVO {
  userUid: number
  username: string
  nickname: string
}

export interface UserInfoVO {
  userUid: number
  username: string
  nickname: string
  avatarUrl?: string
  email?: string
  phone?: string
  registerType: string
  lastLoginTime?: string
  status: number
}

export interface GameAccount {
  bindUid: number
  gameCode: string
  gameUid: string
  gameNickname: string
  serverName?: string
  isMain: number
  syncStatus: number
}

interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export const userApi = {
  login(data: LoginDTO) {
    return request.post<LoginDTO, ApiResponse<LoginVO>>('/user/login', data)
  },

  register(data: RegisterDTO) {
    return request.post<RegisterDTO, ApiResponse<RegisterVO>>('/user/register', data)
  },

  getUserInfo() {
    return request.get<ApiResponse<UserInfoVO>>('/user/info')
  },

  getGameAccounts() {
    return request.get<ApiResponse<GameAccount[]>>('/user/game-account/list')
  },
}