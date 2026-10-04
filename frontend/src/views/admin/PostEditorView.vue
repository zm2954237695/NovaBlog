<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { getAdminPost, savePost } from '@/api'

const route = useRoute(); const router = useRouter()
const isEdit = Boolean(route.params.id)
const saving = ref(false); const loading = ref(isEdit); const message = ref('')
const form = ref({ id: undefined as number | undefined, title: '', slug: '', summary: '', contentMarkdown: '', status: 'DRAFT' as 'DRAFT' | 'PUBLISHED' | 'ARCHIVED', featured: false, coverUrl: '', categoryId: undefined as number | undefined })

onMounted(async () => { if (isEdit) { const post = await getAdminPost(Number(route.params.id)); form.value = { id: post.id, title: post.title, slug: post.slug, summary: post.summary || '', contentMarkdown: post.contentMarkdown || '', status: post.status, featured: post.featured, coverUrl: post.coverUrl || '', categoryId: post.categoryId } }; loading.value = false })
async function submit() { saving.value = true; message.value = ''; try { await savePost(form.value); message.value = '已保存'; setTimeout(() => router.push('/admin/posts'), 500) } catch { message.value = '保存失败，请检查接口或内容。' } finally { saving.value = false } }
</script>

<template>
  <div class="admin-page editor-page"><div class="page-title-row"><div><RouterLink to="/admin/posts" class="back-link">← 返回文章列表</RouterLink><h1>{{ isEdit ? '编辑文章' : '写一篇文章' }}</h1></div><div class="editor-actions"><span :class="{ 'save-success': message === '已保存' }">{{ message }}</span><button class="button button-ghost" @click="router.push('/admin/posts')">取消</button><button class="button button-primary" :disabled="saving || loading" @click="submit">{{ saving ? '保存中…' : '保存文章 ↗' }}</button></div></div><div v-if="loading" class="admin-state">打开文章中…</div><div v-else class="editor-grid"><section class="editor-main"><input v-model="form.title" class="title-input" placeholder="文章标题"><input v-model="form.slug" class="slug-input" placeholder="slug（留空自动生成）"><textarea v-model="form.contentMarkdown" class="markdown-input" placeholder="从这里开始写作，支持 Markdown…"></textarea></section><aside class="editor-side"><label>发布状态<select v-model="form.status"><option value="DRAFT">草稿</option><option value="PUBLISHED">立即发布</option><option value="ARCHIVED">归档</option></select></label><label>摘要<textarea v-model="form.summary" rows="4" placeholder="用一两句话介绍文章"></textarea></label><label>封面地址<input v-model="form.coverUrl" placeholder="https://..."></label><label class="checkbox-label"><input v-model="form.featured" type="checkbox"> 设为精选文章</label><div class="editor-tip"><span>✦</span><p>小提示：一篇好的文章从清晰的标题和摘要开始。</p></div></aside></div></div>
</template>
