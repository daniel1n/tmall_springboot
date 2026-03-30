import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // Frontend routes
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/fore/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/fore/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/fore/Register.vue')
  },
  {
    path: '/product/:pid',
    name: 'Product',
    component: () => import('../views/fore/Product.vue')
  },
  {
    path: '/category/:cid',
    name: 'Category',
    component: () => import('../views/fore/Category.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/fore/Cart.vue')
  },
  {
    path: '/buy',
    name: 'Buy',
    component: () => import('../views/fore/Buy.vue')
  },
  {
    path: '/alipay/:oid',
    name: 'Alipay',
    component: () => import('../views/fore/Alipay.vue')
  },
  {
    path: '/bought',
    name: 'Bought',
    component: () => import('../views/fore/Bought.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/fore/Search.vue')
  },
  {
    path: '/review/:oid',
    name: 'Review',
    component: () => import('../views/fore/Review.vue')
  },

  // Admin routes
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/admin/Dashboard.vue')
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import('../views/admin/Category.vue')
  },
  {
    path: '/admin/product',
    name: 'AdminProduct',
    component: () => import('../views/admin/Product.vue')
  },
  {
    path: '/admin/order',
    name: 'AdminOrder',
    component: () => import('../views/admin/Order.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: () => import('../views/admin/User.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
