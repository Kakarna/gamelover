import axios, { type AxiosInstance, type AxiosResponse } from 'axios'
import type { AxiosRequestConfig } from 'axios'

const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  (config) => {
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
    return Promise.reject(error)
  }
)

export default request
