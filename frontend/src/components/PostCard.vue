<script setup lang="ts">
import { RouterLink } from 'vue-router'
import type { Post } from '@/types'

defineProps<{ post: Post }>()
</script>

<template>
  <RouterLink :to="`/posts/${post.slug}`" class="post-card">
    <div class="post-card-top">
      <span class="post-kicker">{{ post.featured ? 'FEATURED' : 'ARTICLE' }}</span>
      <span class="post-date">{{ post.publishedAt?.slice(0, 10) || post.createdAt?.slice(0, 10) }}</span>
    </div>
    <h3>{{ post.title }}</h3>
    <p>{{ post.summary || '一篇关于工程实践与持续学习的文章。' }}</p>
    <div class="post-card-meta"><span>阅读 {{ post.viewsCount || 0 }}</span><span class="read-arrow">↗</span></div>
  </RouterLink>
</template>
