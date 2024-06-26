import { createApp, watch } from 'vue'
import { createPinia } from 'pinia'
import { validateToken } from "@/api/login"
import { useRefreshToken } from './store/token' // 长短token的使用
import 'element-plus/dist/index.css'
import request from "@/api/index.js"
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import App from './App.vue'
import router from './router/index'
import Particles from "vue3-particles"
import VueLazyload from "vue-lazyload"
// import VueXSS from "vue-xss"
// import SkeletonPlugin from 'vue-skeleton-webpack-plugin'
import errImg from "./assets/img/lazyLoad/err.png"
import loadImg from "./assets/img/lazyLoad/loading.jpg"
import "@/assets/css/danmi.scss"
const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(Particles)
// app.use(SkeletonPlugin)
app.use(pinia)
app.use(router)
// 懒加载vue-lazyload的使用
app.use(VueLazyload, {
    preload: 2.5, // number：表示lazyload的元素, 距离页面底部距离的百分比。计算值为(preload - 1)
    error: errImg, // string：加载失败后图片地址
    loading: loadImg, // string: 加载时图片地址
    attempt: 1, // number：加载错误后最大尝试次数
    // listenEvents['scroll','wheel','mousewheel','resize','animationend','	transitionend','touchmove'],  //想让vue监听的事件
})
// xss框架
// app.use(VueXSS)
// element-plus的使用
app.use(ElementPlus, {
    locale: zhCn,
})
app.config.silent = true // 禁用生产环境的警告
// 注册elementplus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')

/**
 * 刷新长短token
 */
const refreshTokenStore = useRefreshToken()
const setTokenRefresh = (expirationTime) => {
    const refreshTime = expirationTime; // 过期前一分钟刷新
    const timeout = refreshTime - Date.now();
    setTimeout(async () => {
        // 获取后端
        await validateToken()
        console.log("重新发送了一次短token")
        setTokenRefresh(expirationTime); // 递归设置下一次刷新
    }, timeout)
}
const refreshToken = async () => {
    // 从cookie取出短token，判断是否为空
    if (refreshTokenStore.getData() != '' || request.defaults.headers.common['shortauthorization'] != '') {
        // 重新用本地的赋值
        request.defaults.headers.common['shortauthorization'] = refreshTokenStore.getData()
        await validateToken()
        // 发定时任务，请求令牌
        const expirationTime = Date.now() + 25 * 60 * 1000 // 假设过期时间为 25 分钟：25*60*1000
        setTokenRefresh(expirationTime)
    }
}
// 监听
watch(refreshTokenStore, (newValue, oldValue) => {
    // 在这里执行当 isTokenPolling 变为 true 时的操作
    if (newValue && refreshTokenStore.isTokenPolling) {
        console.log('isTokenPolling 变为 true 了！')
        // 设置刷新定时器
        refreshToken()
    }
})

/**
 * 长轮询更新登录用户的信息和消息通知
 */
watch()