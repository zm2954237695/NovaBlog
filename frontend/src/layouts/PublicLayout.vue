<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'

const dark = ref(localStorage.getItem('novablog_theme') === 'dark')

function toggleTheme() {
  dark.value = !dark.value
  document.documentElement.dataset.theme = dark.value ? 'dark' : 'light'
  localStorage.setItem('novablog_theme', dark.value ? 'dark' : 'light')
}
</script>

<template>
  <div class="site-shell">
    <header class="site-header">
      <div class="container nav-wrap">
        <RouterLink to="/" class="brand">
          <span class="brand-mark">N</span>
          <span>Nova<span class="brand-accent">Blog</span></span>
        </RouterLink>
        <nav class="site-nav">
          <RouterLink to="/">首页</RouterLink>
          <RouterLink to="/#latest">文章</RouterLink>
          <RouterLink to="/about">关于我</RouterLink>
          <RouterLink to="/admin" class="nav-admin">管理后台 <span>↗</span></RouterLink>
          <button class="icon-button" :aria-label="dark ? '切换浅色模式' : '切换深色模式'" @click="toggleTheme">
            {{ dark ? '☼' : '☾' }}
          </button>
        </nav>
      </div>
    </header>
    <main><RouterView /></main>
    <footer class="site-footer">
      <div class="container footer-wrap">
        <span>© {{ new Date().getFullYear() }} NovaBlog</span>
        <span>用代码记录思考，用实践打磨产品。</span>
        <span class="footer-dot">●</span>
      </div>
    </footer>
  </div>
</template>
