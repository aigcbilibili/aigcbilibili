<!--接着播放的推荐-->
<template>
    <div class="flex-column-center-container">
        <!--显示集数-->
        <div v-if="otherCompilationsVideos!==null">
            <div v-if="!isExpanseExposides" @click="isExpanseExposides=true" class="video-episodes flex-center-container based-box">
                <p style="cursor: pointer;">显示视频集</p>
            </div>
            <div v-else class="video-episodes-panel flex-column-center-container based-box">
                <!--视频标题-->
                <div class="">

                </div>
                <p @click="isExpanseExposides=false" class="common-based-btn collapse-btn change-color-btn">收起</p>
            </div>
        </div>
        <!--广告位-->
        <div class="video-ad">
            广告占位
            <!-- <img src="@/assets/img/ad.png" alt="广告位" /> -->
        </div>
        <!--选择不同的栏目-->
        <div class="video-category flex-center-container">
            <button @click="prevSlide()" class="video-category-controller controller-left common-based-btn font-fifth-color">上一项</button>
            <div class="video-category-item based-box common-based-btn" 
            :class="item.id-1===currentIndex?'tags-and-labels':'recommend-labels'" 
            v-for="(item, index) in visibleCategory" :key="index"
            @click="changeRecommendType(item.id-1)"><!--NOTE 虽然@v-for不报错，但不能这么用。因为v-for是指令而非事件-->
                {{item.name}}
            </div>
            <button @click="nextSlide()" class="video-category-controller controller-right common-based-btn font-fifth-color">下一项</button>
        </div>
        <!--推荐视频-->
        <div v-if="recommendVideos.length>0" class="video-recommend-content">
            <div class="video-recommend-item" v-for="(item,index) in recommendVideos" :key="index">
                <recommendVue :videoInfo="item" :isUpDownFlag="false" class="recommend-item-detail" /> 
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineAsyncComponent } from "vue"
import { getRecommendVideos, fetchVideosFromCompilations } from "@/api/video"
import { useUserInfo } from "@/store/userInfo"
import { useRoute } from 'vue-router'
const recommendVue = defineAsyncComponent(()=>
    import ("@/components/video/VideoCard.vue")
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
},{
    id: 2, /* 默认号 */
    name: '来自作者'
},{
    id: 3,
    name: '相关内容'
},{
    id: 4,
    name: '热门视频'
},{
    id: 5,
    name: '为你推荐'
}]
const visibleCategory = computed(() => {
    // 根据当前索引和每页显示数量，计算轮播项
    const start = currentIndex.value
    const end = start + itemsPerPage;
    let visibleData = recommendCategory.slice(start, end)
    // 如果start过大
    let i = 0
    while(visibleData.length < itemsPerPage) {
        visibleData.push(recommendCategory[i++])
    } 
    return visibleData
})
/**
 * 推荐相关的交互
 */
// 切换推荐状态
const changeRecommendType = (index) => {
    currentIndex.value = index
}
// 推荐类型的滚动
const prevSlide = () => {
    currentIndex.value = (currentIndex.value - 1 + Math.ceil(recommendCategory.length)) % Math.ceil(recommendCategory.length )
}
const nextSlide = () => {
    currentIndex.value = (currentIndex.value + 1) % recommendCategory.length
}
// 
/**
 * 如果滚动到直播，则固定住
 */

onMounted(async()=>{
    // 获取当前视频合集中其他视频
    otherCompilationsVideos.value = await fetchVideosFromCompilations(userId, videoId)
    // 调用推荐接口
    recommendVideos.value = await getRecommendVideos(videoId)
    console.log(`看下推荐视频：${JSON.stringify(recommendVideos.value)}`)
})
</script>

<style lang="scss" scoped>
@mixin video-episodes-based{
    width: 35rem;
    margin: 1rem 0;
}
.video-episodes{
    @include video-episodes-based;
    height: 3rem;
    font-size: 1.2rem;
    font-weight: 500;
    color: #052b54a9;
    cursor: pointer;
}
.video-episodes-panel{
    @include video-episodes-based;    
    height: 25rem;
    position: relative;
    .collapse-btn{
        position: absolute;
        bottom: 1rem;
        font-weight: 600;
        font-size: 1.1rem;
    }
}
.video-ad{
    width: 35rem;
    height: 10rem;
    margin: 1rem 0;
}
.video-category{
    width: 35rem;
    height: 3rem;
    margin: 1.5rem 1rem;
    z-index: 1;
}
.video-category-item{
    padding: 0.6rem;
    width: auto;
    max-width: 5rem;
    height: 1.3rem;
    margin-left: 0.8rem;
    margin-right: 0.8rem;
}
.recommend-labels{
    color: #052b54a9;
}
.video-category .video-category-controller{
    padding: 0.3rem;
    width: 4rem;
    height: 2.5rem;
    border: 1px solid #9ac9fb;
    background-color: #fff;
    position: absolute;
}
.controller-left{
    left: 1rem;
}
.controller-right{
    right: -1rem;
}
.video-recommend-content .video-recommend-item{
    width: 35rem;
}
/* 推荐视频卡片 */
.recommend-item-detail {
    width: 35rem !important;
    height: 10rem !important;
    margin-bottom: 1rem;
}
</style>