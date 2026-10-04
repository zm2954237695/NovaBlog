<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getDashboardStats } from '@/api'
import type { DashboardStats } from '@/types'

const stats = ref<DashboardStats>({ posts: 0, publishedPosts: 0, drafts: 0, comments: 0, pendingComments: 0, totalViews: 0 })
const loading = ref(true)
onMounted(async () => { try { stats.value = await getDashboardStats() } finally { loading.value = false } })
</script>

<template>
  <div class="admin-page"><div class="page-title-row"><div><span class="eyebrow">OVERVIEW / 01</span><h1>早上好，开始创造。</h1><p>这里是 NovaBlog 的运行概况。</p></div><RouterLink to="/admin/posts/new" class="button button-primary">写一篇文章 <span>↗</span></RouterLink></div><div v-if="loading" class="admin-state">同步数据中…</div><div v-else class="metric-grid"><div class="metric-card metric-dark"><span class="metric-label">全部文章</span><strong>{{ stats.posts }}</strong><small>篇内容资产</small><span class="metric-icon">✎</span></div><div class="metric-card"><span class="metric-label">已发布</span><strong>{{ stats.publishedPosts }}</strong><small>篇公开文章</small><span class="metric-icon">◉</span></div><div class="metric-card"><span class="metric-label">总阅读</span><strong>{{ stats.totalViews }}</strong><small>次页面访问</small><span class="metric-icon">↗</span></div><div class="metric-card metric-accent"><span class="metric-label">待审核评论</span><strong>{{ stats.pendingComments }}</strong><small>条需要你的回应</small><span class="metric-icon">☷</span></div></div><div class="dashboard-lower"><div class="insight-card"><div class="card-title"><span><span class="eyebrow">CONTENT PULSE</span><h2>保持节奏</h2></span><span class="pulse-dot">● LIVE</span></div><div class="pulse-chart"><i v-for="n in 18" :key="n" :style="{ height: `${20 + ((n * 17) % 62)}%` }"></i></div><div class="pulse-caption"><span>过去 30 天</span><strong>持续写作，持续分享。</strong></div></div><div class="quick-card"><span class="eyebrow">QUICK LINKS</span><RouterLink to="/admin/posts/new">新建文章 <span>↗</span></RouterLink><RouterLink to="/admin/comments">审核评论 <span>↗</span></RouterLink><RouterLink to="/" target="_blank">查看博客 <span>↗</span></RouterLink></div></div></div>
</template>
