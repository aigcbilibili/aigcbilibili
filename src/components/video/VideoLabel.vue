<!--对video标签的封装-->
<template>
    <div class="">
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
</template>

<script setup>
import { ref, defineProps } from 'vue'
const isShow = ref(false) // 视频是否展示 
const props = defineProps({
    videoInfo: {
        type: Object,
        required: true
    }
})
// 视频显示数据
const videoOverview = ref(props.videoInfo)
// 图片加载错误
window.imgOnError = function(imgElement){
    console.log(imgElement, '：图片加载失败')
}
</script>

<style lang="scss" scoped>
.video-content{
    border-radius: 20px 20px 0 0;
    width: 100%; /*NOTE 使用百分比会溢出，在父元素中用overflow*/
    height: 10rem;
    margin-top: -0.4rem;
    size: cover;
}
</style>