import { createRouter, createWebHistory } from 'vue-router'
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from 'element-plus'

const router = createRouter({
    history: createWebHistory(), // NOTE Vue2才能在（）中加入import.meta.env.BASE_URL
    routes: [{
        path: '/login',
        meta: {
            title: "登录！AIGCBILIBILI"
        },
        component: () => import('@/pages/login/LoginPage.vue')
    }, {
        path: '/',
        redirect: '/main', // 视频首页
        component: () => import('@/pages/home/HomePage.vue'),
        children: [{
            path: '/main',
            meta: { // NOTE meta路由元信息，当前路由是否需要进一步处理
                requireAuth: false,
                title: "BILIBILI ╰(*°▽°*)╯"
            },
            component: () => import('@/pages/main/MainPage.vue')
        }, {
            path: '/hot',
            meta: {
                requireAuth: false,
                title: "热点"
            },
            name: 'HotBig',
            component: () => import('@/pages/hot/HotPage.vue'),
        }, {
            path: '/trend',
            meta: {
                requireAuth: true,
                title: "动态页"
            },
            name: 'TrendBig',
            component: () => import('@/pages/trend/TrendPage.vue')
        }, {
            path: '/history',
            meta: {
                requireAuth: true,
                title: "历史记录"
            },
            component: () => import('@/pages/history/History.vue')
        }, {
            path: '/live',
            meta: {
                requireAuth: false,
                title: "直播推荐"
            },
            component: () => import('@/pages/live/LivePage.vue')
        }, {
            path: '/article',
            meta: {
                requireAuth: false,
                title: "历史记录"
            },
            component: () => import('@/pages/article/ArticlePage.vue')
        }, {
            path: '/search',
            meta: {
                requireAuth: false,
                title: "搜索结果"
            },
            component: () => import('@/pages/search/SearchPage.vue')
        }, {
            path: '/forum',
            meta: {
                requireAuth: false,
                title: "论坛首页"
            },
            component: () => import('@/pages/forum/ForumPage.vue')
        }, {
            path: '/shop',
            meta: {
                requireAuth: false,
                title: "购物推荐"
            },
            component: () => import('@/pages/shop/ShopPage.vue')
        }, {
            path: '/message',
            meta: {
                requireAuth: true,
                title: "我的消息"
            },
            redirect: '/message/Reply/:userId',
            component: () => import('@/pages/message/MessagePage.vue'),
            children: [{
                path: '/message/Reply/:userId',
                component: () => import('@/pages/message/ReplyMe.vue')
            }, {
                path: '/message/At/:userId',
                component: () => import('@/pages/message/AtMe.vue')
            }, {
                path: '/message/ThumbsUpMe/:userId',
                component: () => import('@/pages/message/ThumbsUpMe.vue')
            }, {
                path: '/message/SystemNotice/:userId',
                component: () => import('@/pages/message/SystemNotice.vue')
            }, {
                path: '/message/MyChat/:userId',
                name: "chatPage",
                component: () => import('@/pages/message/chat/ChatPage.vue'),
                props: (route) => ({
                    receiverContent: route.query.receiverContent,
                    receiverId: route.query.receiverId
                })
            }]
        }, {
            path: '/userCenter',
            meta: {
                requireAuth: true,
                title: "个人主页"
            },
            redirect: '/userCenter/myItem/:userId',
            component: () => import('@/pages/user/UserCenter.vue'),
            children: [{
                path: '/userCenter/myItem/:userId',
                component: () => import('@/pages/user/myItem/MyItem.vue')
            }, {
                path: '/userCenter/charts/:userId',
                component: () => import('@/pages/user/DataCenter.vue')
            }, {
                path: '/userCenter/trends/:userId',
                component: () => import('@/pages/user/NewTrend.vue')
            }, {
                path: '/userCenter/uploadVideo/:userId',
                component: () => import('@/pages/user/upVideo/UpVideo.vue')
            }, {
                path: '/userCenter/permission/:userId',
                component: () => import('@/pages/user/PermissSet.vue')
            }]
        }, {
            path: '/videoDetail/:videoId',
            name: 'videoDetail',
            meta: {
                requireAuth: false,
                title: "视频详情页"
            },
            component: () => import('@/pages/videoDetail/VideoDetail.vue'),
            props: (route) => ({
                upId: route.query.upId
            })
        }]
    }, {
        path: '/404',
        meta: {
            title: "404错误QAQ"
        },
        name: '404',
        component: () => import('@/pages/404Pages.vue')
    }, {
        path: '/403',
        meta: {
            title: "403错误QAQ"
        },
        name: '403',
        component: () => import('@/pages/403Pages.vue')
    }, {
        path: '/monitor',
        meta: {
            title: "监控"
        },
        name: 'monitor',
        component: () => import('@/pages/Monitoring.vue')
    }
    ]
})

router.beforeEach((to, from, next) => {
    const userInfo = useUserInfo() // 使用登录信息
    const userId = userInfo.getId() // 登录用户的id

    // if (to.meta.loading) 
    //     showLoading()

    // 检查路由是否存在
    if (to.path === '/') { // 保留默认重定向
        next()
        return
    }
    const routesPaths = router.getRoutes().map(route => {
        if (route.path === '/')
            return null
        // 移除动态部分和数字
        if (route.path !== '/403' && route.path !== '/404')
            return route.path.replace(/:\w+/g, '').replace(/\d+/g, '')
        else
            return route.path
    }).filter(Boolean) // .filter(Boolean)过滤移除数组中所有假值（false, null, undefined, 0, NaN, ''）

    // 处理 to.path，移除查询字符串和哈希，以及数字
    let basePath = to.path // .split('?')[0].split('#')[0].replace(/\d+/g, '')
    // 判断 basePath 是否在 routesPaths 中有对应的路径
    const pathExists = routesPaths.some(path => basePath.startsWith(path))
    if (basePath !== '/' && !pathExists) { // 没有匹配到任何路由
        next('/404')
    } else {
        next()
    }

    document.title = `${to.meta.title}`

    if (to.path === "/login" || to.path === "/403" || to.path === "/404") {
        next()
        return
    }
    if (to.meta.requireAuth) {
        // 根据是否是新打开窗口来判断
        if (userId > 0) {
            next()
        } else {
            ElMessage.error(`请登录后再继续操作:${userId}`)
            router.push('/login')
            if (window.opener != null && !window.opener.closed) { // 通过window.open跳转
                router.push('/403') // XXX 新打开页面用next转换无效
            } else {
                next(false)
            }
            return
        }
    } else {
        next()
    }
})
// 视频详情页未登录只看30s的解决

export default router