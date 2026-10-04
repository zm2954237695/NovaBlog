<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getCategories, getProfile, getPublicPosts } from '@/api'
import PostCard from '@/components/PostCard.vue'
import SectionHeading from '@/components/SectionHeading.vue'
import type { Category, Post } from '@/types'

const posts = ref<Post[]>([])
const categories = ref<Category[]>([])
const profile = ref<Record<string, string>>({})
const keyword = ref('')
const selectedCategory = ref<number | undefined>()
const loading = ref(true)
const error = ref('')

async function loadPosts() {
  loading.value = true
  error.value = ''
  try {
    const result = await getPublicPosts({ page: 1, size: 9, keyword: keyword.value || undefined, categoryId: selectedCategory.value })
    posts.value = result.items
  } catch {
    error.value = '暂时无法连接 API，请确认后端已启动。'
  } finally {
    loading.value = false
  }
}

function search() { void loadPosts() }

onMounted(async () => {
  await Promise.allSettled([
    loadPosts(),
    getCategories().then((result) => { categories.value = result }),
    getProfile().then((result) => { profile.value = result }),
  ])
})
</script>

<template>
  <section class="hero container">
    <div class="hero-copy">
      <span class="eyebrow">PERSONAL KNOWLEDGE LOG / 2026</span>
      <h1>把复杂的事，<em>讲清楚。</em></h1>
      <p>NovaBlog 是一个关于工程、产品与持续学习的个人空间。记录正在发生的事，也记录那些值得反复推敲的答案。</p>
      <div class="hero-actions">
        <a href="#latest" class="button button-primary">开始阅读 <span>↗</span></a>
        <RouterLink to="/about" class="button button-ghost">认识作者</RouterLink>
      </div>
    </div>
    <div class="hero-orbit" aria-hidden="true">
      <div class="orbit orbit-outer"></div><div class="orbit orbit-inner"></div>
      <div class="orbit-core"><span>N</span></div>
      <span class="orbit-label label-top">JAVA / VUE</span><span class="orbit-label label-right">THINK · BUILD · SHARE</span>
    </div>
  </section>

  <section id="latest" class="container latest-section">
    <div class="latest-toolbar">
      <SectionHeading eyebrow="LATEST NOTES" title="最近写了什么" description="一些正在发生的思考，和已经落地的实践。" />
      <div class="search-box"><span>⌕</span><input v-model="keyword" placeholder="搜索文章" @keyup.enter="search"><button @click="search">搜索</button></div>
    </div>
    <div class="category-row"><button :class="{ active: !selectedCategory }" @click="selectedCategory = undefined; loadPosts()">全部</button><button v-for="category in categories" :key="category.id" :class="{ active: selectedCategory === category.id }" @click="selectedCategory = category.id; loadPosts()">{{ category.name }}</button></div>
    <div v-if="loading" class="state-card">正在加载最新文章…</div>
    <div v-else-if="error" class="state-card error-state">{{ error }}</div>
    <div v-else-if="!posts.length" class="state-card">还没有匹配的文章，换个关键词试试。</div>
    <div v-else class="post-grid"><PostCard v-for="post in posts" :key="post.id" :post="post" /></div>
  </section>

  <section class="container manifesto">
    <div class="manifesto-line"></div>
    <div><span class="eyebrow">THE PRINCIPLE</span><p>“写作不是把答案写下来，而是把思考走过一遍。”</p></div>
    <span class="manifesto-mark">✳</span>
  </section>

  <section class="container home-bottom">
    <div class="about-card"><span class="eyebrow">ABOUT THE AUTHOR</span><h2>{{ profile.name || 'NovaBlog 作者' }}</h2><p>{{ profile.bio || '用代码记录思考，用实践打磨产品。' }}</p><RouterLink to="/about" class="text-link">更多关于我 <span>↗</span></RouterLink></div>
    <div class="stack-card"><span class="eyebrow">CURRENT STACK</span><div class="stack-list"><span>JAVA 21</span><span>SPRING BOOT</span><span>VUE 3</span><span>MYSQL</span><span>REDIS</span></div></div>
  </section>
</template>

<script lang="ts">
import { RouterLink } from 'vue-router'
export default { components: { RouterLink } }
</script>
