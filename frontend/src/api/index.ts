import client from './client'
import type { ApiResponse, Category, Comment, DashboardStats, PageResponse, Post, Tag, UserProfile } from '@/types'

export async function getPublicPosts(params: { page?: number; size?: number; keyword?: string; categoryId?: number }) {
  const { data } = await client.get<ApiResponse<PageResponse<Post>>>('/public/posts', { params })
  return data.data
}

export async function getPublicPost(slug: string) {
  const { data } = await client.get<ApiResponse<Post>>(`/public/posts/${slug}`)
  return data.data
}

export async function getCategories() {
  const { data } = await client.get<ApiResponse<Category[]>>('/public/categories')
  return data.data
}

export async function getTags() {
  const { data } = await client.get<ApiResponse<Tag[]>>('/public/tags')
  return data.data
}

export async function getProfile() {
  const { data } = await client.get<ApiResponse<Record<string, string>>>('/public/profile')
  return data.data
}

export async function getComments(postId: number) {
  const { data } = await client.get<ApiResponse<Comment[]>>(`/public/posts/${postId}/comments`)
  return data.data
}

export async function createComment(postId: number, payload: { nickname: string; email?: string; content: string; parentId?: number }) {
  const { data } = await client.post<ApiResponse<Comment>>(`/comments/posts/${postId}`, payload)
  return data.data
}

export async function login(username: string, password: string) {
  const { data } = await client.post<ApiResponse<{ accessToken: string; expiresIn: number; user: UserProfile }>>('/auth/login', { username, password })
  return data.data
}

export async function getDashboardStats() {
  const { data } = await client.get<ApiResponse<DashboardStats>>('/admin/dashboard/stats')
  return data.data
}

export async function getAdminPosts(params: { page?: number; size?: number; status?: string; keyword?: string }) {
  const { data } = await client.get<ApiResponse<PageResponse<Post>>>('/admin/posts', { params })
  return data.data
}

export async function getAdminPost(id: number) {
  const { data } = await client.get<ApiResponse<Post>>(`/admin/posts/${id}`)
  return data.data
}

export async function savePost(payload: Partial<Post> & { title: string; contentMarkdown: string }) {
  const body = {
    title: payload.title,
    slug: payload.slug,
    summary: payload.summary,
    contentMarkdown: payload.contentMarkdown,
    contentHtml: payload.contentHtml,
    coverUrl: payload.coverUrl,
    status: payload.status || 'DRAFT',
    featured: payload.featured || false,
    categoryId: payload.categoryId,
  }
  const { data } = payload.id
    ? await client.put<ApiResponse<Post>>(`/admin/posts/${payload.id}`, body)
    : await client.post<ApiResponse<Post>>('/admin/posts', body)
  return data.data
}

export async function deletePost(id: number) {
  await client.delete(`/admin/posts/${id}`)
}

export async function getAdminComments(params: { page?: number; size?: number; status?: string }) {
  const { data } = await client.get<ApiResponse<PageResponse<Comment>>>('/admin/comments', { params })
  return data.data
}

export async function updateCommentStatus(id: number, status: string) {
  await client.put(`/admin/comments/${id}/status`, { status })
}
