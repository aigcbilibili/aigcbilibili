<!--历史记录-->
<template>
    <!--时间轴-->
    <div class="history-wrap">
        <p class="title-font-color history-title">我的历史记录</p>
        <div class="change-color-btn flex-center-container" style="margin: 0.8rem 0;"><!--清空历史记录-->
            <img src="@/assets/img/utils/rubbish.svg" style="width: 0.9rem; height: 0.9rem; margin-right: 0.2rem;" />
            <p class="delete-history" @click="deleteHistory(1,0,0)" >清空历史</p>
        </div>
        <div class="flex-center-container" style="margin-top: 2rem;">
        <!--日历-->
            <calendarVue v-if="historyData.length!==0" class="calendar-panel" />
            <!--记录纵轴-->
            <div v-if="historyData.length!==0" class="flex-center-container history-important">
                <div class="time-based-line"><!--时间轴-->
                </div>
                <div class="flex-column-left-max-container history-items">
                    <img src="" />
                    <div v-for="(item, index) in historyData" :key="index" @mousemove="deleteSingle(index, item.id)"
                    class="flex-based-container based-box history-item" @click="turnToDetail(item.id)"><!--上面的点缀-->
                        <img :src="item.imgUrl" style="width: 10rem; height: 10rem;" />
                        <div class="flex-column-left-max-container">
                            <p style="font-size: 2rem; font-weight: 600; margin-left: 1rem;">{{ item.title }}</p>
                            <p style="font-size: 0.9rem; margin:2rem 1rem; ">{{ item.upName }}</p>
                            <p style="position: absolute; bottom: 1rem; left: 11rem;">{{ item.recordTime }}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div v-else>
                <img src="@/assets/img/utils/noData.png" />
            </div>
        </div>
    </div>    
</template>

<script setup>
import { getVideoSmall, deleteVideoHistory } from "@/api/video"
import { onMounted, ref, defineAsyncComponent } from "vue"
import { useUserInfo} from "@/store/userInfo"
import { useRouter } from 'vue-router'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const historyData = ref([])
const router = useRouter()
const calendarVue = defineAsyncComponent(()=>
    import ("@/components/Calendar.vue")
)
// 跳转
const turnToDetail = (videoId) => {
    const routeURL = router.resolve({
        path: `/video/${videoId}`
    })
    window.open(routeURL.href, '_blank')
}
// 删除单项播放历史
const deleteSingle = (hereIndex, videoId) => {
    if(false) { // 等实现拖拽
        deleteHistory(0, videoId, hereIndex)
    }
}
// 删除播放历史
const deleteHistory = (type, id, index) => { // type: 0单个，1为所有
    if(type === 0) {
        historyData.value.splice(index, 1)
        deleteVideoHistory(id, userId)
    } else if(type) { // 多选

    } else {
        historyData.value.forEach((his) => {
            deleteVideoHistory(his.id, userId)
        })
        historyData.value = []
    }
}
onMounted(async()=>{
    historyData.value = await getVideoSmall("history", userId, 0) // 没有收藏夹
    historyData.value = historyData.value.reverse()
})
</script>

<style lang="scss" scoped>
.calendar-panel {
    // display: none !important;
}
.history-wrap {
    min-width: 42rem;
    min-height: 38rem;
    width: auto;
    height: auto;
    margin: 2.5rem 2rem;
    padding: 1.5rem;
    overflow: hidden;
}
.history-important {
    align-items: stretch; /* 使所有子元素填满容器的交叉轴（此处为纵轴） */
}
.time-based-line {
    width: 1.2rem;
    background-color: #deeaf7;
    margin-top: 1rem !important;
    border-radius: 10px;
}
.history-title {
    font-weight: 600;
    font-size: 2rem;
}
.history-items {
    margin-top: 2rem;
    .history-item {
        min-height: 4rem;
        height: auto;
        min-width: 30rem;
        width: auto;
        margin: 1rem;
        cursor: pointer;
        position: relative;
    }
}
$apart-distance: 35rem;
@media screen and (min-width:1200px) {
    .calendar-panel {
        display: flex !important;
        position: absolute;
        top: 11rem;
        left: 20rem;
    }    
    .history-important {
        position: absolute;
        top: 10rem;
        left: $apart-distance + 20rem;
    }
}
</style>