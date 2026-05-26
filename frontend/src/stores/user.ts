import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface UserInfo {
  userUid: number
  username: string
  nickname: string
  avatarUrl?: string
  email?: string
  phone?: string
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

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const gameAccounts = ref<GameAccount[]>([])
  const selectedGame = ref<string>('WUWA')
  const currentTheme = ref<'night' | 'sunset' | 'day'>(
    (localStorage.getItem('theme') as 'night' | 'sunset' | 'day') || 'night'
  )

  function setToken(newToken: string) {
    token.value = newToken
  }

  function setUserInfo(info: UserInfo) {
    userInfo.value = info
  }

  function setGameAccounts(accounts: GameAccount[]) {
    gameAccounts.value = accounts
  }

  function setSelectedGame(game: string) {
    selectedGame.value = game
  }

  function setTheme(theme: 'night' | 'sunset' | 'day') {
    currentTheme.value = theme
    localStorage.setItem('theme', theme)
    document.documentElement.setAttribute('data-theme', theme === 'day' ? 'day' : theme === 'sunset' ? 'sunset' : '')
  }

  function toggleTheme() {
    const themes: ('night' | 'sunset' | 'day')[] = ['night', 'sunset', 'day']
    const currentIndex = themes.indexOf(currentTheme.value)
    const nextTheme = themes[(currentIndex + 1) % themes.length]
    setTheme(nextTheme)
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    gameAccounts.value = []
  }

  return {
    token,
    userInfo,
    gameAccounts,
    selectedGame,
    currentTheme,
    setToken,
    setUserInfo,
    setGameAccounts,
    setSelectedGame,
    setTheme,
    toggleTheme,
    logout,
  }
})
