export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  traceId: string
}

export interface PageResponse<T> {
  items: T[]
  page: number
  size: number
  total: number
  pages: number
}

export interface Post {
  id: number
  title: string
  slug: string
  summary: string
  contentMarkdown: string
  contentHtml: string
  coverUrl?: string
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
  featured: boolean
  categoryId?: number
  viewsCount: number
  likesCount: number
  publishedAt?: string
  createdAt: string
  updatedAt: string
}

export interface Category {
  id: number
  name: string
  slug: string
  description?: string
  sortOrder: number
}

export interface Tag {
  id: number
  name: string
  slug: string
}

export interface Comment {
  id: number
  postId: number
  parentId?: number
  nickname: string
  email?: string
  content: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED'
  createdAt: string
}

export interface UserProfile {
  id: number
  username: string
  displayName: string
  role: string
}

export interface DashboardStats {
  posts: number
  publishedPosts: number
  drafts: number
  comments: number
  pendingComments: number
  totalViews: number
}
