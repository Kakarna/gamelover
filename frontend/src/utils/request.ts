import axios, { type AxiosInstance, type AxiosResponse, type AxiosRequestConfig } from 'axios'
import { useUserStore } from '@/stores/user'

const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    if (userStore.userInfo?.userUid) {
      config.headers['X-User-Id'] = String(userStore.userInfo.userUid)
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request