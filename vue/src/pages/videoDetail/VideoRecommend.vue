<!--接着播放的推荐-->
<template>
    <div class="flex-column-center-container">
        <!--显示集数-->
        <div v-if="otherCompilationsVideos !== null">
            <div v-if="!isExpanseExposides" @click="isExpanseExposides = true"
                class="video-episodes flex-center-container based-box">
                <p style="cursor: pointer;">显示视频集</p>
            </div>
            <div v-else class="video-episodes-panel flex-column-center-container based-box">
                <!--视频标题-->
                <div class="">
                </div>
                <p @click="isExpanseExposides = false" class="common-based-btn collapse-btn change-color-btn">收起</p>
            </div>
        </div>
        <!--广告位-->
        <div class="video-ad">
            <img :src="upInfo.img" alt="">
            <div class="right">
                <div class="top">
                    <div class="left">{{ upInfo.name }}</div>
                    <div class="right2">
                        <el-button type="text" link icon="ChatLineRound" @click="handleMessage">发消息</el-button>
                    </div>
                </div>
                <div class="main">
                    {{ upInfo.text }}

                </div>
                <div class="bottom" v-if="userId !== +route.query.upId">
                    <el-button :type=" upInfo.isFollowing ? 'info' : 'primary'" :icon="upInfo.isFollowing ? '' : 'Plus'" @click="changeFollow">{{ upInfo.isFollowing?'已关注' :'关注'}}</el-button>
                </div>
            </div>

        </div>
        <!--选择不同的栏目-->
        <aCarousel @updateIndex="updateIndex" :currentIndex="currentIndex" :itemsPerPage="itemsPerPage"
            :recommendCategory="recommendCategory"></aCarousel>
        <!--推荐视频-->
        <div v-if="recommendVideos && recommendVideos.length > 0" class="video-recommend-content">
            <div class="video-recommend-item" v-for="(item, index) in recommendVideos" :key="index">
                <recommendVue :videoInfo="item" :isUpDownFlag="false" class="recommend-item-detail" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineAsyncComponent } from "vue"
import { getRecommendVideos, fetchVideosFromCompilations,removeFollowing ,addFollowing} from "@/api/video"
import { useUserInfo } from "@/store/userInfo"
import { aCarousel } from "@/components/public/aCarousel.vue"
import { useRoute, useRouter } from 'vue-router'
import {
    ChatLineRound, Plus
} from '@element-plus/icons-vue'
import { fetchUserInfo } from '@/api/user'
const recommendVue = defineAsyncComponent(() =>
    import("@/components/video/VideoCard.vue")
)
const route = useRoute()
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const isExpanseExposides = ref(false)
const otherCompilationsVideos = ref(null) // 其他视频
const currentIndex = ref(0) // 当前显示的轮播图项索引
const itemsPerPage = 4 // 每页显示的轮播项数量 
const videoId = route.params.videoId // 当前视频Id

/**
 * 测试数据
 */
const recommendVideos = ref([])
/**
 * 推荐的分类 
 */
const recommendCategory = [{
    id: 1,
    name: '全部'
}, {
    id: 2, /* 默认号 */
    name: '来自作者'
}, {
    id: 3,
    name: '相关内容'
}, {
    id: 4,
    name: '热门视频'
}, {
    id: 5,
    name: '为你推荐'
}]
// 更新当前Index
const updateIndex = (newIndex) => {
    currentIndex.value = newIndex
}

const upInfo = ref({})

const router = useRouter()

const handleMessage = () => {
    let obj = {
        senderCoverUrl: upInfo.value.img,
        senderName: upInfo.value.name,
        senderId: upInfo.value.id,
    }
    sessionStorage.setItem('commentInfo', JSON.stringify(obj))
    router.push({ path: `/message/MyChat/${userId}` })
}

const changeFollow =async ()=>{
    if(upInfo.value.isFollowing){
       await removeFollowing(userId,+route.query.upId)
    }else {
        await addFollowing(userId,+route.query.upId)
    }
    upInfo.value.isFollowing = !upInfo.value.isFollowing

}


/**
 * 如果滚动到直播，则固定住
 */
onMounted(async () => {
    // 获取当前视频合集中其他视频
    otherCompilationsVideos.value = await fetchVideosFromCompilations(userId, videoId)
    // 调用推荐接口
    recommendVideos.value = await getRecommendVideos(videoId)
    console.log('route', route);
    let id = +route.query.upId
    let res = await fetchUserInfo(userId, id)
    console.log('44444', res);
    upInfo.value = {
        name: res.name,
        img: res.avatar,
        text: res.intro,
        id: res.id,
        isFollowing: res.isFollowing
    }

})
</script>

<style lang="scss" scoped>
@mixin video-episodes-based {
    width: 35rem;
    margin: 1rem 0;
}

.video-episodes {
    @include video-episodes-based;
    height: 3rem;
    font-size: 1.2rem;
    font-weight: 500;
    color: #052b54a9;
    cursor: pointer;
}

.video-episodes-panel {
    @include video-episodes-based;
    height: 25rem;
    position: relative;

    .collapse-btn {
        position: absolute;
        bottom: 1rem;
        font-weight: 600;
        font-size: 1.1rem;
    }
}

.video-ad {
    width: 35rem;
    height: 10rem;
    margin: 1rem 0;
    display: flex;
    align-items: center;

    img {
        // flex: 0.2;
        width: 48px;
        height: 48px;
        border-radius: 50%;
        padding: 20px;

    }

    .right {
        flex: 1;
        display: flex;
        flex-direction: column;
        width: 100%;

        .top {
            display: flex;
            align-items: center;

            .left {
                color: #FB7299;
                font-weight: 500;
                position: relative;
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
                margin-right: 12px;
                max-width: calc(100% - 12px - 56px);
            }


        }

        .main {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            width: 60%;
            margin: 10px 0;
            color: #9499a0;
            // width: 150px;

        }

        .bottom {
            width: 100%;
            text-align: left;
        }
    }
}

.video-category {
    width: 35rem;
    height: 3rem;
    margin: 1.5rem 1rem;
    z-index: 1;
}

.video-category-item {
    padding: 0.6rem;
    width: auto;
    max-width: 5rem;
    height: 1.3rem;
    margin-left: 0.8rem;
    margin-right: 0.8rem;
}

.recommend-labels {
    color: #052b54a9;
}

.video-category .video-category-controller {
    padding: 0.3rem;
    width: 4rem;
    height: 2.5rem;
    border: 1px solid #9ac9fb;
    background-color: #fff;
    position: absolute;
}

.controller-left {
    left: 1rem;
}

.controller-right {
    right: -1rem;
}

.video-recommend-content .video-recommend-item {
    width: 35rem;
}

/* 推荐视频卡片 */
.recommend-item-detail {
    width: 35rem !important;
    height: 10rem !important;
    margin-bottom: 1rem;
}
</style>