import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'user', name: 'User', meta: { name: '用户信息' }, component: () => import('../views/manager/User') },
      { path: 'business', name: 'Business', meta: { name: '商家信息' }, component: () => import('../views/manager/Business') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'businessPerson', name: 'BusinessPerson', meta: { name: '商家信息' }, component: () => import('../views/manager/BusinessPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'eventCategory', name: 'EventCategory', meta: { name: '日程事件分类信息' }, component: () => import('../views/manager/EventCategory') },
      { path: 'event', name: 'Event', meta: { name: '日程事件信息' }, component: () => import('../views/manager/Event') },
      { path: 'scheduleCategory', name: 'ScheduleCategory', meta: { name: '计划表事件分类信息' }, component: () => import('../views/manager/ScheduleCategory') },
      { path: 'schedule', name: 'Schedule', meta: { name: '计划表信息' }, component: () => import('../views/manager/Schedule') },
      { path: 'team', name: 'Team', meta: { name: '团队信息' }, component: () => import('../views/manager/Team') },
      { path: 'comment', name: 'Comment', meta: { name: '评论信息' }, component: () => import('../views/manager/Comment') },
      { path: 'templateCategory', name: 'TemplateCategory', meta: { name: '计划模板分类信息' }, component: () => import('../views/manager/TemplateCategory') },
      { path: 'template', name: 'Template', meta: { name: '计划模板信息' }, component: () => import('../views/manager/Template') },
      { path: 'order', name: 'Order', meta: { name: '订单信息' }, component: () => import('../views/manager/Order') },
      { path: 'collect', name: 'Collect', meta: { name: '收藏信息' }, component: () => import('../views/manager/Collect') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
      { path: 'eventDetail', name: 'EventDetail', meta: { name: '事件详情页' }, component: () => import('../views/front/EventDetail') },
      { path: 'eventCategory', name: 'EventCategory', meta: { name: '分类计划页' }, component: () => import('../views/front/EventCategory') },
      { path: 'business', name: 'Business', meta: { name: '商家店铺页' }, component: () => import('../views/front/Business') },
      { path: 'schedule', name: 'Schedule', meta: { name: '计划表页' }, component: () => import('../views/front/Schedule') },
      { path: 'teamHome', name: 'TeamHome', meta: { name: '团队首页' }, component: () => import('../views/front/TeamHome') },
      { path: 'order', name: 'Order', meta: { name: '我的订单' }, component: () => import('../views/front/Order') },
      { path: 'collect', name: 'Collect', meta: { name: '我的收藏' }, component: () => import('../views/front/Collect') },
      { path: 'repository', name: 'Repository', meta: { name: '日程仓库' }, component: () => import('../views/front/Repository') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
