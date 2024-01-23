<!--视频本体-->
<template>
    <div class="video-player">
        <!--引用第三方包-->
        <div id="player" class="dplayer">
            
        </div>
    </div>
</template>
<script setup>
import { onBeforeMount, onMounted, ref } from "vue"
import DPlayer from "dplayer" 

const isShowDanMu = ref(false)
const isFullScreen = ref(0) // 默认为browser。两个类型：web 和 browser
const dp = ref(null) // 将存储DPlayer实例
// 视频基本数据
const videoInfo = ref({
    crrentTime: '10:00:00',
    duration: 10,
    isPause: false
})

// 处理跨域
onMounted(()=>{
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
        // danmaku: {
        //     id: '9E2E3368B56CDBB4',
        //     api: 'https://api.prprpr.me/dplayer/',
        //     token: 'tokendemo',
        //     maximum: 1000,
        //     addition: ['https://api.prprpr.me/dplayer/v3/bilibili?aid=4157142'],
        //     user: 'DIYgod',
        //     bottom: '15%',
        //     unlimited: true,
        //     speedRate: 0.5,
        // },
        // contextmenu: [{
        //         text: 'custom1',
        //         link: 'https://github.com/DIYgod/DPlayer',
        //     },
        //     {
        //         text: 'custom2',
        //         click: (player) => {
        //             console.log(player);
        //         },
        //     },
        // ],
        video: {
            url: 'http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4',
            type: 'auto'
        }
    }
    dp.value = new DPlayer(opt)

    /**
     * 定义dplayer操作
     */
    // 播放视频
    const play=()=>{
        dp.play()
    }
    // 切换清晰度
    const switchQuality = (qualityNum)=>{
        switch(qualityNum){
            case 0:
                dp.switchQuality(1080)
                break
            case 1:
                dp.switchQuality(720)
                break
            case 2:
                dp.switchQuality(480)
                break
            case 3:
                dp.switchQuality(360)
                break
        }
    }
    // 设置速度
    const switchSpeed = (speedNum)=>{
        switch(speedNum){
            case 0:
                dp.speed(2)
                break
            case 1:
                dp.speed(1.5)
                break
            case 2:
                dp.speed(1)
                break
            case 3:
                dp.speed(0.5)
                break
            case 4:
                dp.speed(0.25)
                break
        }
    }
    // 设置音量
    const setVolume = (per) =>{
        dp.volume(per, true, false)    
    }
    // 设置是否显示弹幕
    const switchDanMu = ()=>{
        isShowDanMu.value = !isShowDanMu.value
        if(isShowDanMu.value){ // 当前状态是显示
            dp.danmaku.show()
        }else{
            dp.danmaku.hide()
        }
    }
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
    // 全屏和取消全屏
    const switchFullScreen = ()=>{
        isFullScreen.value = (isFullScreen.value+1)%2
        if(isShowDanMu.value=== 1){ // 当前状态是全屏
            dp.fullScreen.request('web')
        }else{
            dp.fullScreen.cancel('web')
        }
    }
    /**
     * 使用dplayer操作
     * 
     */

})
onBeforeMount(()=>{
    if(dp.value !== null){
        dp.destroy()
    }  
})
</script>

<style scoped>

</style>