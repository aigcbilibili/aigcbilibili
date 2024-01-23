<!--视频展示卡片-->
<template>
    <div class="video-exhbit"  @click="turnToDetail()">
        <!--视频本体-->
        <div class="video-wrap video-content">
            <picture v-if="!isShow" class="video-content cover-show" @mouseover="isShow=true" @mouseleave="isShow=false">
                <img v-lazy="videoOverview.imgUrl" :key="videoOverview.imgUrl" class="video-content"/>
                <div class="video-cover-info flex-based-container">
                    <span class="video-cover-info-item flex-based-container"><img src="@/assets/img/video/playCount.svg" class="item-icon"/>{{videoOverview.playCount}}</span>
                    <span class="video-cover-info-item flex-based-container"><img src="@/assets/img/video/danmuCount.svg" class="item-icon"/>{{videoOverview.danmukuCount}}</span>
                    <span class="video-cover-info-item">{{videoOverview.length}}</span>
                </div>
            </picture> 
            <div v-else @mouseleave="isShow=false">
                <video :src="videoOverview.url" controls autoplay controlslist="nodownload" class="video-content"
                loading="eager" onload="fsrCb" onerror="typeof window.imgOnError === 'function'&& window.imgOnError(this)"></video>
            </div>
        </div>
        <!--相关信息-->
        <div class="video-detail">
            <div class="video-detail-item video-name title-font-color">{{videoOverview.videoName}}</div>
            <div class="flex-between-container content-font-color video-detail-text">
                <span>{{videoOverview.authorName}}</span> 
                <span>{{videoOverview.createTime}}</span>
            </div>    
        </div>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { ref, defineProps } from 'vue'
import { useVideoInfo } from '@/store/video'
const isShow = ref(false) // 视频是否展示 
const props = defineProps({
    videoInfo: {
        type: Object,
        required: true
    }
})
// 视频显示数据
const videoOverview = ref(props.videoInfo)
/**
 * 跳转
 */
const router = useRouter()
const loadPinia = (videoId, videoUrl) => {
    try{
        const UVI_obj = useVideoInfo()
        UVI_obj.saveData(videoId, videoUrl)
         // saveData这里出错了
        return UVI_obj
    }catch(e){
        console.error('使用Pinia保存失败')
    }
}
const turnToDetail = ()=>{
    const thisVideo = loadPinia(videoOverview.value.id, videoOverview.value.url)
    const routeURL = router.resolve({
        path: `/video/${videoOverview.value.id}`,
        query: {
            info: thisVideo
        } // NOTE params是路由的一部分,必须要在路由后面添加参数名。query是拼接在url后面的参数，没有也没关系。
    })
    window.open(routeURL.href, '__blank')
}

// 图片加载错误
window.imgOnError = function(imgElement){
    console.log(imgElement, '：图片加载失败')
}
</script>

<style lang="scss" scoped>
.video-exhbit {
    width: 20rem;
    height: 14rem;
    box-shadow: 0.2rem 0.2rem 20px 1px #89c0e4;
    border-radius: 15px;
    overflow: hidden;
    cursor: pointer;
    display: inline-block;
    vertical-align: top;
}
.video-content{
    border-radius: 20px 20px 0 0;
    width: 100%; /*NOTE 使用百分比会溢出，在父元素中用overflow*/
    height: 10rem;
    margin-top: -0.4rem;
    size: cover;
}
.video-wrap{
    position: relative; /*使其成为父元素*/
}
.video-cover-info{
    position: absolute;
    z-index: 5;
    bottom: 1rem;
    color: #fff;
}
.video-detail{
    padding-left: 1.2rem;
    padding-right: 1.2rem;
    margin-top: 0.4rem;
    text-align: left;
    position: relative; 
}
.video-cover-info {
    .item-icon{
        width: 1.3rem;
        margin-right: 0.3rem;
    }
    .video-cover-info-item:not(:last-child){
        margin-left: 1rem;
    }
    .video-cover-info-item:last-child{
        margin-left: 8rem;
    }
}
.video-name{
    position: absolute;
    font-weight: 800;
    font-size: 1.2rem;
}
.video-detail-text{
    font-weight: 600;
    position: absolute;
    width: 18rem;
    margin-top: 2.2rem;
}
</style>