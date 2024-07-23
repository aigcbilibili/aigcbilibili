<template>
    <div class="video-detail-page">
        <div class="video-and-dissussion">
            <!--视频播放-->
            <div class="video-left left-box">
                <videoMain />
            </div>
            <!--评论区-->
            <div class="discussion-panel">
                <commentPanel />
            </div>
        </div>
        <!--NOTE 视频推荐：如果不是全屏，则不展示-->
        <div class="video-right"><!--XXX 长度用于显示集数的覆盖-->
           <videoRecommend />
        </div>
    </div>
</template>

<script setup>
import { ref, defineAsyncComponent } from "vue"
const videoMain = defineAsyncComponent(()=>
    import ('@/pages/videoDetail/VideoMain')
)
const commentPanel = defineAsyncComponent(()=>
    import ('@/pages/videoDetail/CommentPanel')
)
const videoRecommend = defineAsyncComponent(()=>
    import ('@/pages/videoDetail/VideoRecommend')
)
</script>

<style lang="scss" scoped>
.video-detail-page{
    width: 100%; 
    min-height: 120vh; /* 真奇怪，跳转后height不能用百分比或calc() */
}
.video-and-dissussion{
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    position: relative;
    margin-bottom: 2rem;
}
/* NOTE float实现两栏，可用于视频模板!! */
.video-left{
    margin: 3rem 0 2rem 2.5rem;
    width: 45rem;
    min-height: 35rem;
    height: auto;
    border-radius: 20px;
    text-align: left;
}
/*开发中设置 */
.video-right{
    float: right;
    position: absolute;
    display: flex;
    top: 2rem;
    left: 67rem;
    width: 35rem;
    border-radius: 20px;
}
.discussion-panel{ /* 这里不能用absolute，否则会被父组件的min-height限制死*/
    width: 45rem;
    min-height: 20rem;
    height: auto;
    margin: 1rem 0 3rem 2.5rem;
    box-shadow: 0 2px 25px 0 #79b1eca9;
    border-radius: 20px;
}
@media screen and (min-width:1020px) {
    .video-left{
        margin-left: 3rem;
        width: 60rem; /* NOTE使用百分比会使子元素用不了百分比 */
        min-height: 45rem;
        height: auto;
    }
    .discussion-panel{
        width: 60rem;
        margin-left: 3rem;
    }
}
</style>