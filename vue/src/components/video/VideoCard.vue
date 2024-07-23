<!--视频展示卡片-->
<template>
    <!--1. 视频的上下布局：首页和搜索-->
    <div :class="{
        'video-longitudinal': isUpDown,
        'video-horizental': !isUpDown,
        'video-border': isShadow
    }" @click="turnToDetail()">
        <!--视频本体-->
        <div class="video-wrap video-content">
            <picture v-if="!isShow" class="video-content cover-show" @mouseover="setVideoOver()"
                @mouseleave="setVideoOut()">
                <img v-if="videoOverview.imgUrl" v-lazy="videoOverview.imgUrl" :key="videoOverview.imgUrl"
                    class="video-content" />
                <div class="video-cover-info flex-based-container">
                    <span v-if="videoOverview.playCount" class="video-cover-info-item flex-based-container">
                        <img src="@/assets/img/video/playCount.svg" class="item-icon" />
                        {{ videoOverview.playCount }}
                    </span>
                    <span v-if="videoOverview.danmukuCount" class="video-cover-info-item flex-based-container">
                        <img src="@/assets/img/video/danmuCount.svg" class="item-icon" />
                        {{ videoOverview.danmukuCount }}
                    </span>
                    <span v-if="videoOverview.length" class="video-cover-info-item">{{ videoOverview.length }}</span>
                </div>
            </picture>
            <div v-else-if="isShow && videoOverview.url" @mouseleave="setVideoOut()">
                <video :src="videoOverview.url" controls autoplay controlslist="nodownload" class="video-content"
                    loading="eager" onload="fsrCb"
                    onerror="typeof window.imgOnError === 'function'&& window.imgOnError(this)"></video>
            </div>
        </div>
        <!--相关信息-->
        <div class="video-detail" :class="{ 'flex-column-left-max-container': !isUpDown }" :style="{
            'margin-left': isUpDown ? '' : '1rem',
            'justify-content': isUpDown ? '' : 'center'
        }">
            <div v-if="videoOverview.videoName" class="video-detail-item video-name title-font-color">
                {{ videoOverview.videoName }}</div>
            <div class="content-font-color video-detail-text" :class="{
                'flex-between-container': isUpDown,
                'flex-column-left-max-container': !isUpDown
            }">
                <span v-if="videoOverview.authorName" @click.stop="pushUserInfo(videoOverview.authorId)">{{
                    videoOverview.authorName }}</span>
                <span v-if="videoOverview.createTime">{{ videoOverview.createTime }}</span>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, defineProps, toRaw } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfo } from "@/store/userInfo"
const isShow = ref(false) // 视频是否展示 
const isUpDown = ref(true) // 视频是否是上下布局
const userInfo = useUserInfo()
const props = defineProps({
    videoInfo: {
        type: Object,
        required: true
    },
    boderShadow: {
        type: Boolean,
        required: false,
        default: true
    },
    sizeType: {
        type: String,
        required: false,
        default: 'middle'
    },
    isUpDownFlag: {
        type: Boolean,
        required: false,
        default: true
    }
})
// 视频显示数据
const videoOverview = ref(props.videoInfo)
// 是否需要外部阴影
const isShadow = ref(props.boderShadow)
isUpDown.value = props.isUpDownFlag
const setVideoOver = () => {
    if (videoOverview.imgUrl) {
        isShow.value = true
    }
}
const setVideoOut = () => {
    if (videoOverview.imgUrl) {
        isShow.value = false
    }
}
/**
 * 跳转
 */
const router = useRouter()
const turnToDetail = () => {
    const routeURL = router.resolve({
        name: 'videoDetail',
        params: {
            videoId: videoOverview.value.id,
        },
        query: {
            upId: videoOverview.value.authorId,
        }
    })
    window.open(routeURL.href, "_blank")
}

// 图片加载错误
window.imgOnError = function (imgElement) {
    console.log(imgElement, '：图片加载失败')
}

const pushUserInfo = (id) => {
    // console.log('pushUserInfo', id);
    router.push({
        path: `/userCenter/myItem/${id}`
    })
}

</script>

<style lang="scss" scoped>
@mixin base-attribute {
    overflow: hidden;
    cursor: pointer;
}

@mixin small-size {
    width: 20rem;
    height: 14rem;
}

@mixin middle-size {
    // 首页和搜索页，上下布局：video-longitudinal
    width: 20rem;
    height: 14rem;
}

@mixin big-size {
    // 排行处的大型布局
    width: 30rem;
    height: 20rem;
}

.video-content {
    width: 100%;
    /*NOTE 使用百分比会溢出，在父元素中用overflow*/
    height: 10rem;
    margin-top: -0.4rem;
    size: cover;
}

.video-wrap {
    position: relative;
    /*使其成为父元素*/
}

.video-cover-info {
    position: absolute;
    z-index: 5;
    width: 100%;
    bottom: 1rem;
    color: #fff;

    .item-icon {
        width: 1.3rem;
        margin-right: 0.3rem;
    }

    .video-cover-info-item:not(:last-child) {
        margin-left: 1rem;
    }

    .video-cover-info-item:last-child {
        position: absolute;
        right: 1rem;
    }
}

.video-longitudinal {
    @include base-attribute;
    @include middle-size;
    display: inline-block;
    vertical-align: top;

    .video-detail {
        padding-left: 1.2rem;
        padding-right: 1.2rem;
        margin-top: 0.4rem;
        text-align: left;
        position: relative;
    }

    .video-name {
        position: absolute;
        font-weight: 800;
        font-size: 1.2rem;
    }

    .video-detail-text {
        font-weight: 600;
        position: absolute;
        width: 18rem;
        margin-top: 2.2rem;
    }
}

.video-horizental {
    @include base-attribute;
    width: 100%;
    height: 100%;
    display: flex;
    flex-flow: row nowrap;

    .video-wrap {
        width: calc(55%);
        height: calc(100%);
        margin-top: 0.4rem;
    }

    .video-detail {
        height: calc(99%);
    }

    .video-name {
        font-size: 1.2rem !important;
        font-weight: 800;
        margin-bottom: 1rem;
    }
}

.video-border {
    box-shadow: 0.2rem 0.2rem 20px 1px #89c0e4;
    border-radius: 15px;
}
</style>