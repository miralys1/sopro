import Vue from 'vue'
import Router from 'vue-router'
import AdminPanel from '@/components/AdminPanel'
import Workspace from '@/components/Workspace'
import Editor from '@/components/Editor'
import Login from '@/components/Login'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Workspace',
      component: Workspace
    },
    {
      path: '/admin',
      name: 'Admin',
      component: AdminPanel
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/editor/:compId',
      name: 'Editor',
      component: Editor
    },
    // default route
    {
      path: '*',
      redirect: '/'
    }
  ]
})
