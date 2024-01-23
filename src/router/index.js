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
                component: () => import('@/pages/hot/HotPage.vue'),
            },{
                path: '/rank',
                component: () => import('@/pages/rank/RankPage.vue')
            },{
                path: '/live',
                component: () => import('@/pages/live/LivePage.vue')
            },{
                path: '/article',
                component: () => import('@/pages/article/ArticlePage.vue')
            },{
                path: '/forum',
                component: () => import('@/pages/forum/ForumPage.vue')
            },{
                path: '/userCenter/:userId',
                redirect: '/userCenter/:userId/myItem',
                component: () => import('@/pages/user/UserCenter.vue'),
                children: [{
                    path: '/userCenter/:userId/myItem',
                    component: () => import('@/pages/user/MyItem.vue')
                },{
                    path: '/userCenter/:userId/charts',
                    component: () => import('@/pages/user/DataCenter.vue')
                },{
                    path: '/userCenter/:userId/trends',
                    component: () => import('@/pages/user/NewTrend.vue')
                },{
                    path: '/userCenter/:userId/uploadVideo',
                    component: () => import('@/pages/user/UpVideo.vue')
                },{
                    path: '/userCenter/:userId/permission',
                    component: () => import('@/pages/user/PermissSet.vue')
                }]
            },{
                path: '/video/:videoId',
                name: 'videoDetail',
                component: () => import('@/pages/videoDetail/VideoDetail.vue')
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