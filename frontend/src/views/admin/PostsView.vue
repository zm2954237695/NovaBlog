<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { deletePost, getAdminPosts } from '@/api'
import type { Post } from '@/types'

const posts = ref<Post[]>([])
const total = ref(0)
const page = ref(1)
const keyword = ref('')
const status = ref('')
const loading = ref(true)

async function load() { loading.value = true; try { const result = await getAdminPosts({ page: page.value, size: 12, keyword: keyword.value || undefined, status: status.value || undefined }); posts.value = result.items; total.value = result.total } finally { loading.value = false } }
async function remove(post: Post) { if (!confirm(`确定删除「${post.title}」吗？`)) return; await deletePost(post.id); await load() }
onMounted(load)
</script>

<template>
  <div class="admin-page"><div class="page-title-row"><div><span class="eyebrow">CONTENT / 02</span><h1>文章管理</h1><p>把想法整理成可以被阅读的作品。</p></div><RouterLink to="/admin/posts/new" class="button button-primary">新建文章 <span>↗</span></RouterLink></div><div class="table-toolbar"><div class="admin-search"><span>⌕</span><input v-model="keyword" placeholder="搜索标题" @keyup.enter="page = 1; load()"></div><select v-model="status" @change="page = 1; load()"><option value="">全部状态</option><option value="PUBLISHED">已发布</option><option value="DRAFT">草稿</option><option value="ARCHIVED">已归档</option></select></div><div class="data-table-wrap"><div v-if="loading" class="admin-state">加载文章中…</div><table v-else class="data-table"><thead><tr><th>文章</th><th>状态</th><th>阅读</th><th>更新时间</th><th></th></tr></thead><tbody><tr v-for="post in posts" :key="post.id"><td><div class="table-title"><span v-if="post.featured" class="featured-dot">✳</span><strong>{{ post.title }}</strong><small>{{ post.slug }}</small></div></td><td><span class="status-pill" :class="post.status.toLowerCase()">{{ post.status === 'PUBLISHED' ? '已发布' : post.status === 'DRAFT' ? '草稿' : '已归档' }}</span></td><td>{{ post.viewsCount || 0 }}</td><td>{{ post.updatedAt?.slice(0, 10) }}</td><td class="table-actions"><RouterLink :to="`/admin/posts/${post.id}/edit`">编辑</RouterLink><button @click="remove(post)">删除</button></td></tr><tr v-if="!posts.length"><td colspan="5" class="empty-table">还没有文章。</td></tr></tbody></table></div><div class="pagination"><span>共 {{ total }} 篇</span><button :disabled="page <= 1" @click="page--; load()">←</button><span>{{ page }}</span><button :disabled="posts.length < 12" @click="page++; load()">→</button></div></div>
</template>
