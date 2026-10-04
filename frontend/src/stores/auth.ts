import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { login } from '@/api'
import type { UserProfile } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('novablog_access_token'))
  const user = ref<UserProfile | null>(JSON.parse(localStorage.getItem('novablog_user') || 'null'))
  const isAuthenticated = computed(() => Boolean(token.value && user.value))

  async function signIn(username: string, password: string) {
    const result = await login(username, password)
    token.value = result.accessToken
    user.value = result.user
    localStorage.setItem('novablog_access_token', result.accessToken)
    localStorage.setItem('novablog_user', JSON.stringify(result.user))
  }

  function signOut() {
    token.value = null
    user.value = null
    localStorage.removeItem('novablog_access_token')
    localStorage.removeItem('novablog_user')
  }

  return { token, user, isAuthenticated, signIn, signOut }
})
