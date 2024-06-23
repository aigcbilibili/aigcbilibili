<template>
    <div class="my-item">
        <!--各项小模块-->
        <el-scrollbar style="height: 92%;">
            <div class="my-item-single flex-column-left-container" v-for="(videoPanel, index) in personVideoData"
                :key="index">
                <div class="my-single-title-wrap flex-between-container"><!--标题和展开-->
                    <div class="flex-left-container">
                        <p class="my-single-title font-first-color">{{ videoPanel.title }}</p>
                        <div v-if="upId === userId && (videoPanel.title === '我的收藏')" class="common-btn-center"
                            style="width:1.48rem;height:1.48rem;border-radius:50%;margin-left:2rem;background-color: #6674f6;"
                            @click="openFolder(index)">
                            <img src="@/assets/img/utils/plus.svg" style="width:0.8rem;height: 0.8rem;" />
                        </div>
                    </div>
                    <img src="@/assets/img/utils/rightArrow.svg" class="item-btn common-btn-center"
                        @click="turnToMyDetail(index)" />
                </div>
                <div class="my-single-content">
                    <div v-show="data[index] && data[index].length > 0">
                        <el-scrollbar style="width:88%;" ref="scrollbar">
                            <div class="flex-left-container" ref="chScrollbar">
                                <div v-for="(item, subIndex) in data[index]" :key="subIndex">
                                    <el-card style="width: 280px;height: 220px;margin: 0 10px;padding: 0;"
                                        shadow="never" @click="pushVideo(item)">
                                        <div class="view-card">
                                            <img :src="item.cover" alt="" style="width: 100%;height: 150px;">
                                            <span class="play-count">
                                                <el-icon>
                                                    <VideoPlay />
                                                </el-icon>
                                                <span class="playCount">
                                                    {{ item.playCount }}
                                                </span>

                                            </span>
                                            <span class="time">
                                                {{ item.length }}
                                            </span>
                                        </div>

                                        <div class="view-card-info">
                                            <span class="name">{{ item.name }}</span>
                                            <span class="createTime">{{ item.createTime }}</span>
                                        </div>
                                    </el-card>
                                </div>
                            </div>

                        </el-scrollbar>
                    </div>
                    <div v-show="data[index] && data[index].length === 0" class="flex-center-container">
                        <img src="@/assets/img/utils/noData.png" style="width:15rem;height:15rem;" />
                    </div>
                </div>
            </div>
        </el-scrollbar>
        <!--打开合集和收藏夹新建-->
        <div v-if="isOpenNew">
            <newCollectionVue v-model:isShow="isOpenNew" v-model:isCollected="isCollectedVal" :isVideoDetail="false" />
        </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed, defineAsyncComponent } from "vue"
import { fetchUpVideo, fetchVideoCompilations, fetchRecentLikeVideo, getUserInfoVideo } from "@/api/video"
import { fetchCollection } from "@/api/like_and_collect"
import { useUserInfo } from '@/store/userInfo'
import { useRoute, useRouter } from 'vue-router'
// import { ElMessage } from "element-plus"
const videoCardVue = defineAsyncComponent(() =>
    import("@/components/video/VideoCard.vue")
)
const newCollectionVue = defineAsyncComponent(() =>
    import("@/components/collect/CollectCard2.vue")
)
const currentURL = window.location.href
const parts = currentURL.split('/') // 当前url的分割数组
const upId = parseInt(parts[parts.length - 1]) // 最后一位upId
const isOpenNew = ref(false) // 是否打开合集和收藏夹的新建
const isCollectedVal = ref(true) // 打开的是否为收藏夹
const data = ref([[], [], [], []]) // 渲染的数据
const userInfo = useUserInfo() // 保存登录信息
const userId = userInfo.getId()
const isSelf = upId === userId // 是否为自己
let route = useRoute()
const scrollbar = ref()
const chScrollbar = ref()
// const upId = useRoute().params.userId
// const upId = route.pa.upId
const personVideoData = reactive([{
    "isAllowed": true,
    "title": "我的投稿",
},
{
    "isAllowed": true,
    "title": "我的收藏",
},
{
    "isAllowed": true,
    "title": "最近点赞",
}])

const openFolder = (index) => {
    if (index === 1) {
        isCollectedVal.value = false
    }
    isOpenNew.value = true
}
// 获取数据
const getData = async (index) => {
    let res = await getUserInfoVideo(userId, upId)

    data.value[0] = res.selfVideoResponse;
    data.value[1] = res.selfCollectResponse;
    data.value[2] = res.remotelyLikeVideoResponse;
    localStorage.setItem('fansListResponse', JSON.stringify(res.fansListResponse));
    localStorage.setItem('idolListResponse', JSON.stringify(res.idolListResponse));
    console.log('33333', data.value);
}
// 跳转到本页面的详细
const turnToMyDetail = (index) => {
    let Fwidth = scrollbar.value[index].wrapRef.scrollLeft

    scrollbar.value[index].setScrollLeft(Fwidth + 100)
}
/**
 * 对视频的管理
 */
// 测试视频数据
onMounted(() => {
    getData()
})

const router = useRouter()
const pushVideo = (item) => {
    // router.push({
    //     path: `/videoDetail/${item.videoId}?upId=${userId}`
    // })
    const routeURL = router.resolve({
        name: 'videoDetail',
        params: {
            videoId: item.videoId,
        },
        query: {
            upId: upId,
        }
    })
    window.open(routeURL.href, "_blank")
}

</script>

<style lang="scss" scoped>
.my-item {
    padding: 2rem;
    width: 100%;
    height: 100%;
}

.my-item-single {
    width: 90%;
    min-height: 18rem;
    height: auto;
    text-align: left;

    &:not(:first-child) {
        margin-top: 2rem;
    }

    .my-single-title-wrap {
        width: 85%;
        margin-bottom: 1rem;

        .my-single-title {
            font-size: 1.3rem;
            font-weight: 600;
        }
    }

    .my-single-content {
        width: 90%;
        min-height: 5rem;
        height: auto;
    }
}

@media screen and (min-width:1020px) {
    .my-item-single {
        width: 96%;

        .my-single-title-wrap {
            width: 90%;
        }
    }
}

:deep(.el-card__body) {
    padding: 0;
}

.view-card {
    width: 100%;
    height: 150px;
    position: relative;

    .play-count {
        position: absolute;
        left: 0;
        bottom: 0;
        margin: 10px 10px;
        color: #fff;
        display: flex;
        align-items: center;
        display: none;

        :deep(.el-icon) {
            font-size: 16px !important;
        }

        .playCount {
            font-size: 16px;
            margin-left: 5px;
        }
    }


    .time {
        position: absolute;
        right: 0;
        bottom: 0;
        margin: 10px;
        color: #fff;
        display: none;
    }

    &:hover>.play-count,
    &:hover>.time {
        display: block;
    }
}

.view-card-info {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 10px;

    .name {
        font-size: 20px;
        color: #093957;

    }
}
</style>