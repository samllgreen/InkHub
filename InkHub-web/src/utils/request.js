import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例：baseURL 写 /api，靠 vite proxy 转发
const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

// 请求拦截器：自动带 token
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一处理 code / 401 跳登录
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res.data          // 直接返回 data，页面里不用再 res.data.data
    }
    if (res.code === 401) {
      ElMessage.error('登录已过期，请重新登录')
      // 退出 = 状态和存储全清（只清 token 会导致界面还显示旧登录态）
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'   // 整页跳转，强制重置内存状态
      return Promise.reject(new Error(res.msg))
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg))
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    }
    ElMessage.error(error.response?.data?.msg || '网络错误')
    return Promise.reject(error)
  }
)

export default request
