import Vue from 'vue'
import Router from 'vue-router'
import Tokens from '@/components/Tokens'
import Login from '@/components/Login'
import Auth from '../auth'

Vue.use(Router)

let routes = [
  {
    path: '/login',
    name: 'Login',
    title: 'Login',
    component: Login
  },
  {
    path: '/tokens',
    name: 'Tokens',
    title: 'Tokens',
    component: Tokens,
    meta: {
      requiresAuth: true
    }
  }
]

var router = new Router({routes});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)
        && !Auth.checkAuth()) {
        next({path : '/login', query: {
          redirect: to.fullPath
      }})
   } else {
      next();
   }
})

export default router
