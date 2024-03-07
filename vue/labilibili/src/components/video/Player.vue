<!--视频本体-->
<template>
    <div class="video-player">
        <!--引用第三方包-->
        <div id="player" class="dplayer">
        </div>
    </div>
</template>

<script setup>
import { onBeforeMount, onMounted, ref, reactive, defineProps } from "vue"
import { fetchDanMu } from "@/api/danmu.js"
import { useRoute } from 'vue-router'
import DPlayer from "dplayer" 
const route = useRoute()
const isShowDanMu = ref(false)
const isFullScreen = ref(0) // 默认为browser。两个类型：web 和 browser
const dp = ref(null) // 将存储DPlayer实例
const videoInfo = reactive({
    id: route.params.videoId, // 当前视频Id
    url: ""
})
// 视频基本数据
const props = defineProps({
    videoUrl: {
        type: String,
        required: true,
        default: ""  
    }
})
const danmuList = ref([])
const getDanMuList = async()=>{
    const danmuList_ = await fetchDanMu(videoInfo.id)
    return danmuList_
}
// 处理跨域
onMounted(()=>{
    // 获取视频的url
    videoInfo.url = props.videoUrl
    console.log("获取到的视频id", videoInfo.url)
    /**
     * 获取弹幕数据
     */
    danmuList.value = getDanMuList()
    /**
     * dplayer初始化
     *  */ 
    const opt = {
        container: document.getElementById('player'),
        autoplay: true,
        theme: '#FADFA3',
        loop: true,
        lang: 'zh-cn',
        screenshot: true,
        hotkey: true,
        preload: 'auto',
        volume: 0.7,
        mutex: true,
        danmaku: {
            id: '1',
            // api: '/danmaku/getDanmaku/',
            // token: 'tokendemo',
            authorization: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEiLCJyb2xlIjoidXNlciJ9.ybuiJ87Nq7xgcWrM50P_VgAF1P74fnEN8jCSH5daqR2re4hVTMYgkzMHWZlK104guM75RGWgVxNrtfnhinjR-g',
            maximum: 1000,
            addition: ['/danmaku/getDanmaku/1'],
            // user: 'DIYgod',
            bottom: '15%',
            unlimited: true,
            speedRate: 0.5,
        },
        video: {
            url: videoInfo.url,
            type: 'auto'
        }
    }
    dp.value = new DPlayer(opt)

    /**
     * 定义dplayer操作
     */
    // 发送弹幕
    const sendDanMu =(text, colorString) =>{
        dp.danmaku.send({
            text: text || '我是弹幕',
            color: colorString || '#ff0000',
            size: 20,
            speed: 1000,
            opacity: 0.5,
        },dp.danmaku.draw({
        text: text,
        color: colorString
        }))
        // 向后端发送axios
    }
    // 删除弹幕
    const deleteDanMu = () => {

    }
})
onBeforeMount(()=>{
    if(dp.value !== null){
        dp.destroy()
    }  
})
</script>

<style scoped>

</style>