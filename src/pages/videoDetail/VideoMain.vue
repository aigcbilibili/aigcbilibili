<!--视频本体-->
<template>
    <div class="flex-column-container">
    <playerVue class="dplayer-wrap" :videoUrl="videoUrl" /> 
    <div class="video-info font-first-color">
        <div class="titleAndData flex-based-container">
            <h1>{{videoInfo.title}}</h1>
            <div class="likeAndCollect flex-center-container">
                <!--点赞-->
                <div class="likeAndCollect-wrap" @click="thumbsUp()">
                    <div v-if="!isLiked" class="detail-btn likeAndCollect-item common-btn-between" >
                        <img src="@/assets/img/utils/thumbsUp_notDone.svg" alt="未点赞" style="width: 1.3rem;"/>
                        <span id="likeNum">{{videoInfo.likeCount}}</span>
                    </div>
                    <div v-else class="detail-btn-chosen likeAndCollect-item common-btn-between">
                        <img src="@/assets/img/utils/thumbsUp_done.svg" alt="已点赞" style="width: 1.3rem;"/>
                        <span id="likeNum">{{videoInfo.likeCount}}</span>
                    </div>
                </div>
                <!--点赞和收藏-->
                <div class="likeAndCollect-wrap" @click="collect()">
                    <div v-if="!isCollected" class="detail-btn likeAndCollect-item common-btn-between">
                        <img src="@/assets/img/utils/collect_notDone.svg" alt="收藏数" style="width: 1.3rem;" />
                        <span id="CollectNum">{{videoInfo.collectCount}}</span>
                    </div>
                    <div v-else class="detail-btn-chosen likeAndCollect-item common-btn-between">
                        <img src="@/assets/img/utils/collect_done.svg" alt="收藏数" style="width: 1.3rem;" />
                        <span id="CollectNum">{{videoInfo.collectCount}}</span>
                    </div>
                </div>
                <!--XXX 补充：分享-->
                <span @click="isShare=true" class="share"><!--  @mouseleave="isShare=false"-->
                    <p class="share-btn common-btn-center">分享</p>
                    <shareCard v-if="isShare" class="share-card" :isShare="isShare" />
                </span>
                <!--XXX 补充：其他功能-->
                <span class="other-function common-btn-center">
                    <el-dropdown @command="handleMoreFunc">
                        <img src="@/assets/img/utils/more.svg" style="width: 0.8rem;" />
                        <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="more">其他</el-dropdown-item>
                            <el-dropdown-item command="report">举报</el-dropdown-item>
                        </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </span>
            </div>
        </div>
        <div class="video-author">
            <profileCard v-if="isUpIntro" :avatar="upInfo.avatar" :id="userId" style="width:2rem; height:2rem;" />
            <span class="nameAndFollows">
                <div class="video-author-name">{{userInfo.getName()}}</div>
            </span>
        </div>
        <div class="introduction-panel">
            <div class="intro-area"><!--介绍的文字和数据-->
                <div class="video-time">
                    <span>{{videoInfo.playCount}}</span> 播放量
                    <span style="margin-left: 2rem;">{{videoInfo.createtime}}</span> 发布
                </div>
                <div class="video-intro">
                    <!--如果不是全屏，则不展示-->
                    <div v-if="!isExpandIntro">
                        <p class="long-text-collapsed intro-collapsed">{{videoInfo.intro}}</p>
                        <div class="change-color-btn change-info-status-btn font-fifth-color" @click="isExpandIntro=true">展开</div>
                    </div>
                    <div v-else class="intro-expanse">
                        <p class="long-text-expanded">{{videoInfo.intro}}</p>
                        <div class="change-color-btn change-info-status-btn font-fifth-color" @click="isExpandIntro=false">收起</div>
                    </div>
                </div>
            </div>
            <!--视频的tags-->
            <div v-if="videoInfo.tags" class="video-tags">
                <div class="video-tag-item tags-and-labels flex-based-container" 
                v-for="(tag, index) in transferToList(videoInfo.tags)" :key="index" @click="searchRes(tag, 'tag')">{{tag}}</div>
            </div>
        </div>
    </div>
    <!--弹窗-->
    <collectCard :style="{'display':isClickCollect?'flex':'none'}" v-model:isCollected="isClickCollect" />
    </div>
</template>

<script setup>
import { ref,defineProps, defineAsyncComponent, onMounted, onUpdated } from "vue"
import { getVideoDetail, addVideoHistory } from '@/api/video'
import { addLike, deleteLike, fetchCollection } from "@/api/like_and_collect"
import { ElMessage } from "element-plus"
import { useRoute, useRouter } from 'vue-router'
import { useUserInfo} from "@/store/userInfo"
const router = useRouter()
const route = useRoute()
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 用户Id
const currentURL = window.location.href
const videoUrl = ref("") // 视频url
const videoId = route.params.videoId // 当前视频Id
const isLiked = ref(false) // 是否点赞过本视频
const isClickCollect = ref(false) // 是否点击过收藏按钮
const isCollected = ref(false) // 是否收藏过本视频
const isUpIntro = ref(false) // 是否展开更多
const isExpandIntro = ref(false) // 是否展开简介
const isShare = ref(false) // 是否分享本页面
const profileCard = defineAsyncComponent(()=>
    import ('@/components/user/ProfileCard.vue')
) 
const playerVue  = defineAsyncComponent(()=>
    import ('@/components/video/Player.vue')
)
const collectCard = defineAsyncComponent(()=>
    import ('@/components/collect/CollectCard.vue')
)
const shareCard = defineAsyncComponent(()=>
    import ('@/components/ShareCard.vue')
)
// 传递的
const props = defineProps({
    upId: {
        type: Number,
        required: true,
        default: 0
    },
    videoUrl: {
        type: String, 
        required: true,
        default: ""
    }
})
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
    title:'HelloWorld', 
    intro:'加载失败，使用测试数据。高考大省的小城市考出来的，省排400，去了上海，感觉真的非常开阔眼界。如果不是因为在上海读的大学，我都不知道人生还有那么多种可能。就算早知道家庭条件不支持在上海买房，眼界的开阔也让我受益匪浅，最起码让我到了个买得起房的地方。', // 默认为空
    createtime:'2023-12-15',
    tags:"",
    playCount: 100,
    likeCount: 1, 
    collectCount: 1, 
    isLike: true,
    isCollected: false,
})
// 将tags的string转为列表
const transferToList = (tags_str) => {
    let tags_array = []
    if(tags_str && tags_str!=='') {
        tags_array = tags_str.split(',').map(tag => tag.trim().replace(/'/g, ''))
    }
    return tags_array
}
/**
 * 点赞功能
 */
const thumbsUp = async() => {
    isLiked.value = !isLiked.value
    if(isLiked.value) {
        isLiked.value = true
        videoInfo.value.likeCount += 1
        await addLike(userId, 1, 'video')
    }else{
        isLiked.value = false
        videoInfo.value.likeCount -= 1
        await deleteLike(userId, 1, 'video')
    }
}
/**
 * 收藏功能
 */
const collect = () => {
    isClickCollect.value = true
    if(isCollected.value){
        isCollected.value = true
        videoInfo.value.collect += 1
    }else{
        isCollected.value = false
        videoInfo.value.collect -= 1
    }
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
const getCollectIdStr = async() => {
    let collectStr = ''
    const collectData = await fetchCollection(userId)
    collectData.forEach((item)=>{
        collectStr += item.id
        collectStr += ','
    })
    collectStr = collectStr.slice(0, -1)
    return collectStr
}
/**
 * 获取视频数据
 */
const refreshVideo = async() => {
    let collectIds = await getCollectIdStr()
    if(!collectIds){
        collectIds = '0'
    }
    // 收藏夹id列表
    const getData = await getVideoDetail(videoId, userId, collectIds)
    if(getData) { // 加载好了后
        videoInfo.value = getData
        console.log(`获取视频详情数据${getData}`)
        await addVideoHistory(videoId,userId)
    }
    console.log("视频详情页测试", getData)
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
onUpdated(()=>{

})
onMounted(()=>{
    // 获取用户id和视频url    
    videoUrl.value = route.query.videoUrl
    upInfo.value.id = route.query.upId
    // 获取视频数据
    refreshVideo()
})
</script>

<style lang="scss" scoped>
@import url(@/assets/css/dropdown.css);
$right-distance: 1rem;
$btn-height: 1.8rem;
.video-info{
    margin-top: 3.5rem;
    margin-left: 1rem;
    position: static;
    height: auto;
    max-height: 60rem;
}
.dplayer-wrap{
    width: 100%;
    height: 20rem;
}
.introduction-panel{
    width: 42rem;
    min-height: 6rem;
    height: auto;
    padding: 0.5rem;
    margin-bottom: 0.5rem;
    // position: absolute;
    .intro-area{
        background: #d0e3f8a9;
        border-radius: 20px;
        padding: 1rem;
        min-height: 4.5rem;
        margin-right: $right-distance;
    }
}
.titleAndData{
    width: 43rem;
    position: relative;
}
.titleAndData .likeAndCollect{
    position: absolute;
    right: $right-distance;
}
.likeAndCollect .likeAndCollect-wrap:not(:last-child){
    margin-right: 3rem;
}
.likeAndCollect-item{
    padding-left: 1rem;
    padding-right: 1rem;
    width: 3rem;
    height: $btn-height;
}
@media screen and (min-width:1020px) {
    .video-info{
        margin-top: 11rem;
    }
    .titleAndData{
        width: 58rem;
    }
    .titleAndData .likeAndCollect{
        right: 0.5rem;
    }
    .introduction-panel{
        width: auto;
        margin-right: 0;
        .intro-area{
            margin-right: $right-distance;
        }
    }
}
.share{
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
.other-function{
    margin-right: $right-distance;
    background:  #fff;
    border: 2px solid #79b1ec6e;
    box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    margin-left: 1.5rem;
    font-size: 1.1rem;
    width: 1.8rem;
    height: $btn-height;
    border-radius: 50%;
}
.video-author{
    display: flex;
    align-items: center;
    margin-top: 0.2rem;
}
.video-author-name{
    vertical-align: 1rem;
    font-weight: 550;
}
.video-author-avatar{
    width: 3rem;
    height: 3rem;
    margin-right: 1rem;
}
.video-intro{
    margin-top: 1rem;
    color: #052b54a9;
    .intro-collapsed{
        font-weight: 600;
    }
    .change-info-status-btn{
        margin-top: 0.5rem;
        font-weight: 500;
    }
}
.video-tags{
    display: flex;
    flex-flow: row wrap;
    margin-top: 1rem;
    margin-right: $right-distance;
    min-height: 2rem;
    height: auto;
}
.video-tag-item{
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