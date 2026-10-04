<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()
const username = ref('admin')
const password = ref('admin123')
const error = ref('')
const loading = ref(false)

async function submit() {
  loading.value = true; error.value = ''
  try { await auth.signIn(username.value, password.value); router.push('/admin') }
  catch { error.value = '用户名或密码错误，请确认后端已启动。' }
  finally { loading.value = false }
}
</script>

<template>
  <div class="login-page"><div class="login-decoration"><div class="login-orbit"></div><span class="login-word">NOVA<br>CONTROL</span></div><div class="login-card"><RouterLink to="/" class="brand"><span class="brand-mark">N</span><span>Nova<span class="brand-accent">Blog</span></span></RouterLink><span class="eyebrow">PRIVATE WORKSPACE</span><h1>欢迎回来。</h1><p>登录管理后台，继续你的创作。</p><form @submit.prevent="submit"><label>用户名<input v-model="username" autocomplete="username" required></label><label>密码<input v-model="password" type="password" autocomplete="current-password" required></label><div v-if="error" class="form-error">{{ error }}</div><button class="button button-primary login-button" :disabled="loading">{{ loading ? '验证中…' : '进入工作台 ↗' }}</button></form><small>演示账号：admin / admin123</small></div></div>
</template>
