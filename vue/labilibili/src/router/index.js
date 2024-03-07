import {createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(), // NOTE Vue2才能在（）中加入import.meta.env.BASE_URL
    routes: [{ 
            path: '/login', 
            component: () => import('@/pages/login/LoginPage.vue') 
        },{
            path: '/',
            redirect: '/main', // 视频首页
            component: () => import('@/pages/home/HomePage.vue'),
            children: [{
                path: '/main',
                component: () => import('@/pages/main/MainPage.vue')
            },{
                path: '/hot',
                name: 'HotBig',
                component: () => import('@/pages/hot/HotPage.vue'),
            },{
                path: '/trend',
                name: 'TrendBig',
                component: () => import('@/pages/trend/TrendPage.vue')
            },{
                path: '/history',
                component: () => import('@/pages/history/History.vue')
            },{
                path: '/live',
                component: () => import('@/pages/live/LivePage.vue')
            },{
                path: '/article',
                component: () => import('@/pages/article/ArticlePage.vue')
            },{
                path: '/search',
                component: () => import('@/pages/search/SearchPage.vue')
            },{
                path: '/forum',
                component: () => import('@/pages/forum/ForumPage.vue')
            },{
                path: '/shop',
                component: () => import('@/pages/shop/ShopPage.vue')
            },{
                path: '/message',
                redirect: '/message/Reply/:userId',
                component: () => import('@/pages/message/MessagePage.vue'),
                children: [{
                    path: '/message/Reply/:userId',
                    component: () => import('@/pages/message/ReplyMe.vue')
                },{
                    path: '/message/At/:userId',
                    component: () => import('@/pages/message/AtMe.vue')
                },{
                    path: '/message/ThumbsUpMe/:userId',
                    component: () => import('@/pages/message/ThumbsUpMe.vue')
                },{
                    path: '/message/SystemNotice/:userId',
                    component: () => import('@/pages/message/SystemNotice.vue')
                },{
                    path: '/message/MyChat/:userId',
                    name: "chatPage",
                    component: () => import('@/pages/message/chat/ChatPage.vue'),
                    props: (route) => ({
                        receiverContent: route.query.receiverContent,
                        receiverId: route.query.receiverId
                    })
                }]
            },{
                path: '/userCenter',
                redirect: '/userCenter/myItem/:userId',
                component: () => import('@/pages/user/UserCenter.vue'),
                children: [{
                    path: '/userCenter/myItem/:userId',
                    component: () => import('@/pages/user/myItem/MyItem.vue')
                },{
                    path: '/userCenter/charts/:userId',
                    component: () => import('@/pages/user/DataCenter.vue')
                },{
                    path: '/userCenter/trends/:userId',
                    component: () => import('@/pages/user/NewTrend.vue')
                },{
                    path: '/userCenter/uploadVideo/:userId',
                    component: () => import('@/pages/user/UpVideo.vue')
                },{
                    path: '/userCenter/permission/:userId',
                    component: () => import('@/pages/user/PermissSet.vue')
                }]
            },{
                path: '/video/:videoId',
                name: 'videoDetail',
                component: () => import('@/pages/videoDetail/VideoDetail.vue'),
                props: (route) => ({
                    videoUrl: route.query.videoUrl,
                    upId: route.query.upId
                })
            }]
        },{
            path: '/404',
            name: '404',
            component: () => import('@/pages/404Pages.vue')
        },{
            path: '/403',
            name: '403',
            component: () => import('@/pages/403Pages.vue')
        }
    ]
})

// router.beforeEach((to, from, next) => {
// 	if (to.meta.loading) 
//         showLoading();
//     document.title = `FakeLi | ${to.meta.title}`;
// 	/**
// 	 *  token 是登录成功得到的。如果用户本地模拟token，也会调用接口，如果token过期或者被非法篡改，会在axios的拦截器中进行处理。
// 	 */
// 	if (to.path === "/login") {
// 		next();
// 		return;
// 	}
// 	if (getLocalKey("token")) {
// 		addRouters(next, to);
// 	} else {
// 		// 没token不是权限页面
// 		if (!to.meta.isRelease) {
// 			addRouters(next, to);
// 		} else {
// 			next({
// 				path: "/login",
// 				replace: true,
// 			});
// 		}
// 	}
// });

export default router