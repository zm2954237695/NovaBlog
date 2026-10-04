<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

function logout() {
  auth.signOut()
  router.push('/login')
}
</script>

<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <RouterLink to="/" class="admin-brand"><span class="brand-mark">N</span><span>NovaBlog</span></RouterLink>
      <div class="sidebar-label">WORKSPACE</div>
      <nav class="admin-nav">
        <RouterLink to="/admin"><span>◈</span>概览</RouterLink>
        <RouterLink to="/admin/posts"><span>✎</span>文章管理</RouterLink>
        <RouterLink to="/admin/comments"><span>☷</span>评论审核</RouterLink>
      </nav>
      <div class="sidebar-bottom">
        <RouterLink to="/" class="sidebar-home">← 返回博客</RouterLink>
        <button class="sidebar-logout" @click="logout"><span>⇥</span>退出登录</button>
      </div>
    </aside>
    <section class="admin-main">
      <header class="admin-topbar">
        <div><span class="eyebrow">NOVA / CONTROL CENTER</span></div>
        <div class="admin-user"><span class="avatar">{{ auth.user?.displayName?.slice(0, 1) || 'A' }}</span>{{ auth.user?.displayName || '管理员' }}</div>
      </header>
      <main class="admin-content"><RouterView /></main>
    </section>
  </div>
</template>
