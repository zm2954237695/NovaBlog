<script setup lang="ts">
import DOMPurify from 'dompurify'
import { marked } from 'marked'
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { createComment, getComments, getPublicPost } from '@/api'
import type { Comment, Post } from '@/types'

const route = useRoute()
const post = ref<Post | null>(null)
const comments = ref<Comment[]>([])
const loading = ref(true)
const error = ref('')
const commentForm = ref({ nickname: '', email: '', content: '' })
const commentMessage = ref('')

const renderedContent = () => post.value ? DOMPurify.sanitize(marked.parse(post.value.contentMarkdown || '') as string) : ''

async function submitComment() {
  if (!post.value || !commentForm.value.nickname || !commentForm.value.content) return
  try {
    await createComment(post.value.id, commentForm.value)
    commentForm.value = { nickname: '', email: '', content: '' }
    commentMessage.value = '评论已提交，审核通过后会显示。'
  } catch { commentMessage.value = '评论提交失败，请稍后重试。' }
}

onMounted(async () => {
  try {
    post.value = await getPublicPost(String(route.params.slug))
    comments.value = await getComments(post.value.id)
  } catch { error.value = '文章不存在或暂时无法加载。' }
  finally { loading.value = false }
})
</script>

<template>
  <div class="container article-page">
    <div v-if="loading" class="state-card">正在打开文章…</div>
    <div v-else-if="error" class="state-card error-state">{{ error }} <RouterLink to="/">回到首页</RouterLink></div>
    <template v-else-if="post">
      <div class="article-head"><RouterLink to="/" class="back-link">← 所有文章</RouterLink><span class="post-kicker">{{ post.featured ? 'FEATURED ARTICLE' : 'ARTICLE' }}</span><h1>{{ post.title }}</h1><p>{{ post.summary }}</p><div class="article-meta"><span>{{ post.publishedAt?.slice(0, 10) || post.createdAt.slice(0, 10) }}</span><span>阅读 {{ post.viewsCount }}</span><span>NovaBlog 编辑部</span></div></div>
      <article class="article-content" v-html="renderedContent()"></article>
      <div class="article-end"><span>END OF NOTE</span><span>✳</span><span>THANK YOU FOR READING</span></div>
      <section class="comments-section"><div class="comments-heading"><div><span class="eyebrow">CONVERSATION</span><h2>留下你的想法</h2></div><span>{{ comments.length }} 条评论</span></div><div v-if="comments.length" class="comment-list"><div v-for="comment in comments" :key="comment.id" class="comment-item"><div class="comment-avatar">{{ comment.nickname.slice(0, 1) }}</div><div><div class="comment-meta"><strong>{{ comment.nickname }}</strong><span>{{ comment.createdAt.slice(0, 10) }}</span></div><p>{{ comment.content }}</p></div></div></div><div class="comment-form"><input v-model="commentForm.nickname" placeholder="你的昵称"><input v-model="commentForm.email" placeholder="邮箱（可选）" type="email"><textarea v-model="commentForm.content" placeholder="写下你的评论…" rows="4"></textarea><div class="comment-submit"><span>{{ commentMessage }}</span><button class="button button-primary" @click="submitComment">提交评论 ↗</button></div></div></section>
    </template>
  </div>
</template>
