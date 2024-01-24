import { createApp } from 'vue'
import { createPinia } from 'pinia'
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

const app = createApp(App)
const pinia = createPinia()
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
