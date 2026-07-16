import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/AdminLogin.vue'),
    meta: { guest: true }
  },
  {
    path: '/',
    component: () => import('../views/AdminLayout.vue'),
    meta: { requiresAuth: true },
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: () => import('../views/OrderList.vue')
      },
      {
        path: 'orders/:id',
        name: 'OrderDetail',
        component: () => import('../views/OrderDetail.vue')
      },
      {
        path: 'products',
        name: 'ProductList',
        component: () => import('../views/ProductList.vue')
      },
      {
        path: 'products/edit/:id',
        name: 'ProductEdit',
        component: () => import('../views/ProductEdit.vue')
      },
      {
        path: 'users',
        name: 'UserList',
        component: () => import('../views/UserList.vue')
      },
      {
        path: 'marketing',
        name: 'MarketingList',
        component: () => import('../views/MarketingList.vue')
      },
      {
        path: 'reviews',
        name: 'ContentReview',
        component: () => import('../views/ContentReview.vue')
      },
      {
        path: 'cs',
        name: 'CustomerService',
        component: () => import('../views/CustomerService.vue')
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: () => import('../views/Analytics.vue')
      },
      {
        path: 'logistics',
        name: 'LogisticsList',
        component: () => import('../views/LogisticsList.vue')
      },
      {
        path: 'settings',
        name: 'SystemSettings',
        component: () => import('../views/SystemSettings.vue')
      },
      {
        path: 'refund',
        name: 'RefundManage',
        component: () => import('../views/RefundManage.vue')
      },
      {
        path: 'products/add',
        name: 'ProductAdd',
        component: () => import('../views/ProductEdit.vue')
      },
      {
        path: ':pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('admin_token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.guest && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
