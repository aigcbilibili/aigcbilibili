<!--视频本体-->
<template>
    <div class="video-player">
        <!--引用第三方包-->
        <div id="player" class="dplayer">
        </div>
    </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref, defineProps, watch, onBeforeMount } from "vue"
import { fetchDanMu, addDanmu, getDanmuList } from "@/api/danmu.js"
import { useRoute, useRouter } from 'vue-router'
import { useUserInfo } from '@/store/userInfo'
import DPlayer from "dplayer"
let videoURL_ = ""
const route = useRoute()
const router = useRouter()
const dp = ref(null) // 将存储DPlayer实例
const videoId = route.params.videoId // 当前视频Id
const userId = useUserInfo().id
// 视频基本数据
const props = defineProps({
    videoUrl: {
        type: String,
        required: true,
        default: ""
    }
})
// 初始化dplayer.js实例
const initPlayer = () => {
    const opt = {
        container: document.getElementById('player'),
        autoplay: true,
        video: {
            url: props.videoUrl,
            type: 'auto',
        },
        danmaku: true,
        apiBackend: {
            send: async function (endpoint) {
                if (userId === 0) {
                    localStorage.setItem('path', route.fullPath)
                    return router.push({ path: '/login' })
                }
                console.log(endpoint);
                let obj = {
                    content: endpoint.data.text,
                    place: endpoint.data.time + 2,
                    userId: userId,
                    videoId: Number(videoId),
                    type: endpoint.data.type,
                    color: endpoint.data.color
                }
                // draw({
                //     "time": 30,
                //     "text": "1661309439313203200",
                //     "color": 16777215,
                //     "player": 'top',
                //     "type": 1,
                //     "author": "YU4324234234"

                // })
                await addDanmu(obj)

                endpoint.success()
                // draw(obj)


            },
            read: async (endpoint) => {
                let res = await getDanmuList(videoId)
                console.log('res', res);
                endpoint.success(res);
            }
        },


    }
    dp.value = new DPlayer(opt)
    // 进行监听
}


// 发送弹幕
watch(() => props.videoUrl, (newValue, oldValue) => {
    if (newValue !== '') {
        videoURL_ = newValue
        initPlayer() // 等获取到再更新实例

    }
})

const draw = (obj) => {
    console.log('ddssaa', dp.value.danmaku.draw);

    dp.value.danmaku.draw(obj);
}
onBeforeMount(() => {

})
onMounted(() => {
    // 获取弹幕数据
    // initPlayer()

})
onBeforeUnmount(() => {
    if (dp && dp.value !== null) {

        dp.value.destroy()
    }
})
</script>

<style scoped></style>