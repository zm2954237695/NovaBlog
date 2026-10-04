import { createRouter, createWebHistory } from 'vue-router'
import PublicLayout from '@/layouts/PublicLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: [
    {
      path: '/',
      component: PublicLayout,
      children: [
        { path: '', name: 'home', component: () => import('@/views/public/HomeView.vue') },
        { path: 'posts/:slug', name: 'post-detail', component: () => import('@/views/public/PostDetailView.vue') },
        { path: 'about', name: 'about', component: () => import('@/views/public/AboutView.vue') },
      ],
    },
    { path: '/login', name: 'login', component: () => import('@/views/LoginView.vue') },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true },
      children: [
        { path: '', name: 'admin-dashboard', component: () => import('@/views/admin/DashboardView.vue') },
        { path: 'posts', name: 'admin-posts', component: () => import('@/views/admin/PostsView.vue') },
        { path: 'posts/new', name: 'admin-post-new', component: () => import('@/views/admin/PostEditorView.vue') },
        { path: 'posts/:id/edit', name: 'admin-post-edit', component: () => import('@/views/admin/PostEditorView.vue') },
        { path: 'comments', name: 'admin-comments', component: () => import('@/views/admin/CommentsView.vue') },
      ],
    },
  ],
})

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('novablog_access_token')) return '/login'
  if (to.name === 'login' && localStorage.getItem('novablog_access_token')) return '/admin'
})

export default router
