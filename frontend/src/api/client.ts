import axios from 'axios'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/v1',
  timeout: 10000,
})

client.interceptors.request.use((config) => {
  const token = localStorage.getItem('novablog_access_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

client.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && location.pathname.startsWith('/admin')) {
      localStorage.removeItem('novablog_access_token')
      location.href = '/login'
    }
    return Promise.reject(error)
  },
)

export default client
