<!--视频本体-->
<template>
    <div class="flex-column-container">
        <playerVue class="dplayer-wrap" v-model:videoUrl="videoInfo.url" />
        <div class="video-info font-first-color">
            <div class="titleAndData flex-based-container">
                <h1>{{ videoInfo.title }}</h1>
                <div class="likeAndCollect flex-center-container">
                    <!--点赞-->
                    <div class="likeAndCollect-wrap" @click="thumbsUp()">
                        <div v-if="!videoInfo.isLiked" class="detail-btn likeAndCollect-item common-btn-between">
                            <img src="@/assets/img/utils/thumbsUp_notDone.svg" alt="未点赞" style="width: 1.3rem;" />
                            <span id="likeNum">{{ videoInfo.likeCount }}</span>
                        </div>
                        <div v-else class="detail-btn-chosen likeAndCollect-item common-btn-between">
                            <img src="@/assets/img/utils/thumbsUp_done.svg" alt="已点赞" style="width: 1.3rem;" />
                            <span id="likeNum">{{ videoInfo.likeCount }}</span>
                        </div>
                    </div>
                    <!--点赞和收藏-->
                    <div class="likeAndCollect-wrap" @click="collect()">
                        <div v-if="!videoInfo.isCollected" class="detail-btn likeAndCollect-item common-btn-between">
                            <img src="@/assets/img/utils/collect_notDone.svg" alt="收藏数" style="width: 1.3rem;" />
                            <!-- <span id="CollectNum">{{videoInfo.collectCount}}</span> -->
                        </div>
                        <div v-else class="detail-btn-chosen likeAndCollect-item common-btn-between">
                            <img src="@/assets/img/utils/collect_done.svg" alt="收藏数" style="width: 1.3rem;" />
                            <!-- <span id="CollectNum">{{videoInfo.collectCount}}</span> -->
                        </div>
                    </div>
                    <!--XXX 补充：分享-->
                    <!-- <span @click="isShare = true" class="share">
                        <p class="share-btn common-btn-center">分享</p>
                        <shareCard v-if="isShare" class="share-card" :isShare="isShare" />
                    </span> -->
                    <!--XXX 补充：其他功能-->
                    <!-- <span class="other-function common-btn-center">
                        <el-dropdown @command="handleMoreFunc">
                            <img src="@/assets/img/utils/more.svg" style="width: 0.8rem;" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="more">其他</el-dropdown-item>
                                    <el-dropdown-item command="report">举报</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
</el-dropdown>
</span> -->
                </div>
            </div>
            <div class="video-author">
                <profileCard v-if="isUpIntro" :avatar="upInfo.avatar" :id="userId" style="width:2rem; height:2rem;" />
                <span class="nameAndFollows">
                    <div class="video-author-name">{{ userInfo.getName() }}</div>
                </span>
            </div>
            <div class="introduction-panel">
                <div class="intro-area"><!--介绍的文字和数据-->
                    <div class="video-time">
                        <span>{{ videoInfo.playCount }}</span> 播放量
                        <span style="margin-left: 2rem;">{{ videoInfo.createtime }}</span> 发布
                    </div>
                    <div class="video-intro">
                        <!--如果不是全屏，则不展示-->
                        <p class="long-text-expanded">{{ videoInfo.intro }}</p>
                        <!-- <div v-if="!isExpandIntro">
                            <p class="long-text-collapsed intro-collapsed">{{ videoInfo.intro }}</p>
                            <div class="change-color-btn change-info-status-btn font-fifth-color"
                                @click="isExpandIntro = true">展开
                            </div>
                        </div>
                        <div v-else class="intro-expanse">
                            <p class="long-text-expanded">{{ videoInfo.intro }}</p>
                            <div class="change-color-btn change-info-status-btn font-fifth-color"
                                @click="isExpandIntro = false">收起
                            </div>
                        </div> -->
                    </div>
                </div>
                <!--视频的tags-->
                <!-- <div v-if="video_tags" class="video-tags">
                    <div class="video-tag-item tags-and-labels flex-based-container"
                        v-for="(tag, index) in transferToList(video_tags)" :key="index" @click="searchRes(tag, 'tag')">
                        {{ tag }}</div>
                </div> -->
            </div>
        </div>
        <!--收藏弹窗-->
        <collectCard v-model:isShow="isShoww" :isCollected="true" :videoId="videoId" @checkDir="checkDir"
            :style="{ 'display': isShoww === true ? '' : 'none' }" ref="collectCardRef" @getD="refreshVideo" />
    </div>
</template>

<script setup>
import { ref, defineProps, defineAsyncComponent, onMounted, onBeforeMount, nextTick } from "vue"
import { getVideoDetail, addVideoHistory } from '@/api/video'
import { addLike, deleteLike, fetchCollection } from "@/api/like_and_collect"
import { ElMessage } from "element-plus"
import { useRoute, useRouter } from 'vue-router'
import { useUserInfo } from "@/store/userInfo"
import playerVue from "@/components/video/Player.vue"
let video_tags = "" // 本页面的标签信息
const router = useRouter()
const route = useRoute()
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 用户Id
const videoId = route.params.videoId // 当前视频Id
// const isLiked = ref(false) // 是否点赞过本视频
// const isClickCollect = ref(false) // 是否点击过收藏按钮
const isShoww = ref(false) // 是否收藏过本视频
const isUpIntro = ref(false) // 是否展开更多
const isExpandIntro = ref(false) // 是否展开简介
const isShare = ref(false) // 是否分享本页面
const profileCard = defineAsyncComponent(() =>
    import('@/components/user/ProfileCard')
)
const collectCard = defineAsyncComponent(() =>
    import('@/components/collect/CollectCard2')
)
const shareCard = defineAsyncComponent(() =>
    import('@/components/ShareCard')
)
// 传递的
const props = defineProps({
    upId: {
        type: Number,
        required: true,
        default: 0
    }
})

const collectCardRef = ref()


const getD = () => {

}

// 用户信息
const upInfo = ref({
    id: 1,
    avatar: require("@/assets/img/avater.png"),
    followingersNum: 20,
    followersNum: 33,
    likeNum: 82,
    playNum: 1372
})
/**
 * 视频简介
 */
const videoInfo = ref({
    id: '',
    url: "",
    title: 'HelloWorld',
    intro: '加载失败，使用测试数据。高考大省的小城市考出来的，省排400，去了上海，感觉真的非常开阔眼界。如果不是因为在上海读的大学，我都不知道人生还有那么多种可能。就算早知道家庭条件不支持在上海买房，眼界的开阔也让我受益匪浅，最起码让我到了个买得起房的地方。', // 默认为空
    createtime: '2023-12-15',
    tags: "",
    playCount: 100,
    likeCount: 1,
    collectCount: 1,
    isLiked: true,
    isCollected: false,
})
// 将tags的string转为列表
const transferToList = (tags_str) => {
    let tags_array = []
    if (tags_str && tags_str !== '') {
        tags_array = tags_str.split(',').map(tag => tag.trim().replace(/'/g, ''))
    }
    return tags_array
}
/**
 * 点赞功能
 */
const thumbsUp = async () => {
    if (userId === 0) {
        localStorage.setItem('path', route.fullPath)
        return router.push({ path: '/login' })
    }
    console.log('videoInfo.value', videoInfo.value);
    if (!videoInfo.value.isLiked) {
        await addLike(userId, videoId, 'video')
        videoInfo.value.isLiked = !videoInfo.value.isLiked
        videoInfo.value.likeCount += 1

    } else {
        // isLiked.value = false
        await deleteLike(userId, videoId, 'video')
        videoInfo.value.likeCount -= 1
        videoInfo.value.isLiked = !videoInfo.value.isLiked

    }
}
/**
 * 收藏功能
 */
const collect = () => {
    if (userId === 0) {
        localStorage.setItem('path', route.fullPath)
        return router.push({ path: '/login' })
    }



    isShoww.value = true
    nextTick(() => {
        collectCardRef.value && collectCardRef.value.getData()
    })


}

const checkDir = (val) => {
    console.log(val.id);
}

/**
 * 下拉栏
 */
const handleMoreFunc = (command) => {
    switch (command) {
        case 'more':
            ElMessage.success('抱歉~功能开发中')
            break
        case 'report':
            ElMessage.error('举报成功')
            break
    }
}
const getCollectIdStr = async () => {
    let collectStr = ''
    const collectData = await fetchCollection(userId)
    collectData.forEach((item) => {
        collectStr += item.id
        collectStr += ','
    })
    collectStr = collectStr.slice(0, -1)
    return collectStr
}
/**
 * 获取视频数据
 */
const refreshVideo = async () => {
    let collectIds = await getCollectIdStr()
    if (!collectIds) {
        collectIds = '0'
    }
    // 收藏夹id列表
    const getData = await getVideoDetail(videoId, userId, collectIds)

    if (getData.url === "") {
        ElMessage.error("视频url获取失败")
        return
    }
    videoInfo.value = getData
    await addVideoHistory(videoId, userId)
}
/**
 * 跳转
 */
const searchRes = (keyword, fromSource) => {
    const routeURL = router.resolve({
        path: `/search/`,
        query: {
            keyword: keyword,
            from_source: fromSource
        } // NOTE params是路由的一部分,必须要在路由后面添加参数名。query是拼接在url后面的参数，没有也没关系。
    })
    window.open(routeURL.href, '_blank')
}
onBeforeMount(() => {
    // 获取up用户id
    upInfo.value.id = route.query.upId
    // 获取视频数据
    refreshVideo()
})
onMounted(() => {
    // 加入默认tags
    video_tags = "'大学生', '计算机', '工作', '张雪峰','这就是真实生活力量', '生活','日常','程序员','生活记录','记录','生活万花筒 9.0 拥抱真实生活'"
})
</script>

<style lang="scss" scoped>
@import url(@/assets/css/dropdown.css);
$right-distance: 1rem;
$btn-height: 1.8rem;

.video-info {
    margin-top: 5.5rem;
    margin-left: 1rem;
    position: static;
    height: auto;
    max-height: 60rem;
}

.dplayer-wrap {
    width: 100%;
    height: 20rem;
}

.introduction-panel {
    width: 42rem;
    min-height: 6rem;
    height: auto;
    padding: 0.5rem;
    margin-bottom: 0.5rem;

    // position: absolute;
    .intro-area {
        background: #d0e3f8a9;
        border-radius: 20px;
        padding: 1rem;
        min-height: 4.5rem;
        margin-right: $right-distance;
    }
}

.titleAndData {
    width: 43rem;
    position: relative;
}

.titleAndData .likeAndCollect {
    position: absolute;
    right: $right-distance;
}

.likeAndCollect .likeAndCollect-wrap:not(:last-child) {
    margin-right: 3rem;
}

.likeAndCollect-item {
    padding-left: 1rem;
    padding-right: 1rem;
    width: 3rem;
    height: $btn-height;
}

@media screen and (min-width:1020px) {
    .video-info {
        margin-top: 14rem;
    }

    .titleAndData {
        width: 58rem;
    }

    .titleAndData .likeAndCollect {
        right: 0.5rem;
    }

    .introduction-panel {
        width: auto;
        margin-right: 0;

        .intro-area {
            margin-right: $right-distance;
        }
    }
}

.share {
    margin-left: 1rem;
    width: 3rem;
    height: $btn-height+0.2rem;
    position: relative;

    .share-btn {
        width: 3rem;
        height: 1.2rem;
        padding: 0.3rem;
        background: #ffffff;
        border-radius: 10px;
        border: 2px solid #79b1ec6e;
        box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    }

    .share-card {
        position: absolute;
        bottom: 3rem;
        right: 0.01rem;
        width: 30rem;
        height: 22rem;
    }
}

.other-function {
    margin-right: $right-distance;
    background: #fff;
    border: 2px solid #79b1ec6e;
    box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    margin-left: 1.5rem;
    font-size: 1.1rem;
    width: 1.8rem;
    height: $btn-height;
    border-radius: 50%;
}

.video-author {
    display: flex;
    align-items: center;
    margin-top: 0.2rem;
}

.video-author-name {
    vertical-align: 1rem;
    font-weight: 550;
}

.video-author-avatar {
    width: 3rem;
    height: 3rem;
    margin-right: 1rem;
}

.video-intro {
    margin-top: 1rem;
    color: #052b54a9;

    .intro-collapsed {
        font-weight: 600;
    }

    .change-info-status-btn {
        margin-top: 0.5rem;
        font-weight: 500;
    }
}

.video-tags {
    display: flex;
    flex-flow: row wrap;
    margin-top: 1rem;
    margin-right: $right-distance;
    min-height: 2rem;
    height: auto;
}

.video-tag-item {
    width: auto;
    border-radius: 10px;
    padding: 0.4rem;
    max-width: 20rem;
    height: 1.2rem;
    margin-right: 0.5rem;
    margin-bottom: 0.3rem;
    cursor: pointer;
}
</style>