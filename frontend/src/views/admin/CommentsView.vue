<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getAdminComments, updateCommentStatus } from '@/api'
import type { Comment } from '@/types'

const comments = ref<Comment[]>([]); const status = ref('PENDING'); const loading = ref(true)
async function load() { loading.value = true; try { comments.value = (await getAdminComments({ page: 1, size: 50, status: status.value || undefined })).items } finally { loading.value = false } }
async function update(comment: Comment, next: string) { await updateCommentStatus(comment.id, next); await load() }
onMounted(load)
</script>

<template>
  <div class="admin-page"><div class="page-title-row"><div><span class="eyebrow">COMMUNITY / 03</span><h1>评论审核</h1><p>让有价值的对话出现在文章下面。</p></div><select class="top-select" v-model="status" @change="load"><option value="PENDING">待审核</option><option value="APPROVED">已通过</option><option value="REJECTED">已拒绝</option><option value="">全部评论</option></select></div><div class="comment-admin-list"><div v-if="loading" class="admin-state">加载评论中…</div><div v-else-if="!comments.length" class="admin-state">这里暂时没有评论。</div><article v-for="comment in comments" :key="comment.id" class="comment-admin-item"><div class="comment-avatar">{{ comment.nickname.slice(0, 1) }}</div><div class="comment-admin-body"><div class="comment-meta"><strong>{{ comment.nickname }}</strong><span>{{ comment.createdAt.slice(0, 16).replace('T', ' ') }}</span><span class="status-pill" :class="comment.status.toLowerCase()">{{ comment.status }}</span></div><p>{{ comment.content }}</p><div class="comment-admin-actions"><button v-if="comment.status !== 'APPROVED'" @click="update(comment, 'APPROVED')">通过</button><button v-if="comment.status !== 'REJECTED'" @click="update(comment, 'REJECTED')">拒绝</button></div></div></article></div></div>
</template>
