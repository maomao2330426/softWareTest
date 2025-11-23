import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'TicketList',
    component: () => import('../views/TicketList.vue')
  },
  {
    path: '/create',
    name: 'CreateTicket',
    component: () => import('../views/CreateTicket.vue')
  },
  {
    path: '/ticket/:id',
    name: 'TicketDetail',
    component: () => import('../views/TicketDetail.vue')
  },
  {
    path: '/admin',
    name: 'AdminPanel',
    component: () => import('../views/AdminPanel.vue')
  },
  {
    path: '/cs',
    name: 'CSWorkbench',
    component: () => import('../views/CSWorkbench.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
