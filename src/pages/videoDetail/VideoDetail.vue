<!--视频详情页
本页面初始传递的数据：
    upId
    userId
    videoId
-->
<template>
    <div class="video-detail-page">
        <div class="video-and-dissussion">
            <!--视频播放-->
            <div class="video-left left-box">
                <videoMain :playInfo="playInfo" />
            </div>
            <!--评论区-->
            <div class="discussion-panel">
                <commentPanel :videoId="13" />
            </div>
        </div>
        <!--NOTE 视频推荐：如果不是全屏，则不展示-->
        <div class="video-right"><!--XXX 长度用于显示集数的覆盖-->
           <!-- <videoRecommend /> -->
        </div>
    </div>
</template>

<script setup>
import { onMounted, onBeforeMount, ref, reactive, defineAsyncComponent } from "vue"
import { useRoute } from 'vue-router'
const route = useRoute()
const isLike = ref(false) // 是否点赞过本视频
const playInfo = ref()
const baseInfo = ref()
const videoMain = defineAsyncComponent(()=>
    import ('@/pages/videoDetail/VideoMain')
)
const commentPanel = defineAsyncComponent(()=>
    import ('@/pages/videoDetail/CommentPanel')
)
const videoRecommend = defineAsyncComponent(()=>{
    import ('@/pages/videoDetail/VideoRecommend')
})

onMounted(() =>{
    const a = 1
})
onBeforeMount(()=>{
    const a = 1
})




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
}
/* NOTE float实现两栏，可用于视频模板!! */
.video-left{
    position: absolute;
    margin: 3rem 0 2rem 2.5rem;
    width: 45rem;
    height: 40rem;
    border-radius: 20px;
    text-align: left;
}
.video-right{
    display: none;
}
.discussion-panel{ /* 这里不能用absolute，否则会被父组件的min-height限制死*/
    width: 45rem;
    min-height: 20rem;
    height: auto;
    margin: 45rem 0 3rem 2.5rem;
    box-shadow: 0 2px 25px 0 #79b1eca9;
    border-radius: 20px;
}
@media screen and (min-width:1020px) {
    .video-left{
        margin-left: 3rem;
        width: 60rem; /* NOTE使用百分比会使子元素用不了百分比 */
        height: 50rem;
    }
    .video-right{
        display: flex;
        margin: 3rem 67rem;
        width: 35rem;
    }
    .discussion-panel{
        margin-top: 55rem;
        width: 60rem;
        margin-left: 3rem;
    }
}
.video-right{
    border-radius: 20px;
    text-align: center;
}
.video-right > .video-episodes{
    width: 35rem;
    height: 3rem;
    margin: 1rem 0;
    border-radius: 10px;
    box-shadow: -5px 2px 25px 0 #79b1eca9;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2rem;
    font-weight: 500;
    color: #052b54a9;
}
.video-right > .video-ad{
    width: 35rem;
    height: 10rem;
    margin: 1rem 0;
    border-radius: 10px;
    box-shadow: -5px 2px 25px 0 #79b1eca9;
}
.video-right > .video-category{
    width: 35rem;
    height: 3rem;
    margin: 1.5rem 1rem;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.video-category > .video-category-item:not(:last-child){
    padding: 0.5rem;
    height: 2rem;
    margin-right: 2rem;
    border-radius: 10px;
    box-shadow: -5px 2px 25px 0 #79b1eca9;
}
.video-recommend-item{
    width: 35rem;
}
</style>