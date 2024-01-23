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
                <commentPanel />
            </div>
        </div>
        <!--NOTE 视频推荐：（v-if实现）如果不是全屏，则不展示-->
        <div v-if="isFullScreen" class="video-right"><!--XXX 长度用于显示集数的覆盖-->
           <!-- <videoRecommend /> -->
        </div>
    </div>
</template>

<script setup>
import { onMounted, onBeforeMount, ref, reactive, defineAsyncComponent } from "vue"

import { getVideoDetail } from '@/api/video'
import { ElMessage } from "element-plus"
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
/**
 * 视频简介
 */
const videoInfo = {
    title:'HelloWorld', // 【通过跳转传过来】
    intro:'加载失败，使用测试数据', // 默认为空
    duration:'00:01:00',
    createtime:'2023-12-15',
    tags:['大学生', '计算机', '工作'],
    playcontent: 100,
    like: 1, // TODO 待加入
    collect: 1, // TODO 待加入
    commentNum: 999, // TODO 总评论数
}
/**
 * UP主信息
 */
const userInfo = {
    nickName:'匿名',
    avatar:'', // 对应的cover
    following: 100, // 关注数
    followers:100, // TODO 待加入
}
/**
 * 
 */
const relatedInfo = {
    videoInfo:{
        title:'HelloWorld', // 【通过跳转传过来】
        intro:'加载失败，使用测试数据', // 默认为空
        duration:'00:01:00',
        createtime:'2023-12-15',
        tags:['大学生', '计算机', '工作'],
        playcontent: 100,
        like: 1, // TODO 待加入
        collect: 1, // TODO 待加入
        commentNum: 999, // TODO 总评论数
    },
    userInfo:{ // TODO 询问UserInfo是否为大写
        nickName:'匿名',
        avatar:'', // 对应的cover
        gender:'',
        followers:100, // TODO 待加入
    },
    commentlist:[{
        id: 0,
        avatar: '@/assets/img/avater.png', // TODO
        nickname: '咸鱼2号',
        content: '测试数据，显示失败',
        createtime: '2023-12-16',
        reaplyId: ''
    }],
    similarvideo:[{
        authorname: '',
        videoname: 'html',
        playcontent: 1,
        createtime: '', // TODO
        url: 'http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4' // TODO    
    }]
}

/**
 * 点赞实现
 */
const thumbUp = ()=>{
    let add_num = "+"+1
    let thumb = document.getElementById('likeNum')
    let x = thumb.pageX, y = thumb.pageY
    isLike.value = true
}

/**
 * 评论发布功能
 */
let newComment = reactive({
    author: '',
    content: '',
    parentID: 0, // TODO 如果parentID是代表非回复
})  
// 添加的评论需要后端再加ID
const addComment = () => {
    // 先在前端显示
    if(newComment.author && newComment.content){
        relatedInfo.commentlist.push({
            id: relatedInfo.commentlist.length,
            avatar: userInfo.avatar,
            nickname: userInfo.nickName,
            content: newComment.content,
        })
        // 重置当前评论发布的状态
        newComment.author = " "
        newComment.content = " "
    }
}

/**
 * 动态加载视频和评论区
 */



/**
 * 判断当前是否为全屏 
 */
let isFullScreen = ref(false)
function checkScreenSize(){
    // NOTE 虽然script setup会自动暴露顶层变量给html（不用return），但需要使用ref和xx.value
    isFullScreen.value = window.innerWidth > 1020 // 设全屏是超过1020px
}

onMounted(() =>{
    // NOTE Vue3的setup使用组合式而非选项式，不能用this关键字
    checkScreenSize()
    window.addEventListener('resize', checkScreenSize)

})
onBeforeMount(()=>{
    window.removeEventListener('resize', checkScreenSize)
})

/**
 * 点击后修改isUpIntro“判断能否展开”
 */
let isUpIntro = ref(false)

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

</script>

<style scoped>
.video-detail-page{
    width: 100%;
    min-height: 85vh; /* 真奇怪，跳转后height不能用百分比或calc() */
    overflow: hidden;
}
.video-and-dissussion{
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
.discussion-panel{
    position: absolute;
    top: 70rem;
    width: 45rem;
    margin-left: 2.5rem;
}
@media screen and (min-width:1020px) {
    .video-left{
        margin-left: 3rem;
        width: 60rem; /* NOTE使用百分比会使子元素用不了百分比 */
        height: 50rem;
    }
    .video-right{
        margin: 3rem 67rem;
        width: 35rem;
    }
    .discussion-panel{
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
.discussion-panel{
    box-shadow: 0 2px 25px 0 #79b1eca9;
    min-height: 15rem;
    margin-top: 1rem;
    margin-bottom: 2rem;
    border-radius: 20px;
}
.video-recommend-item{
    width: 35rem;
}
</style>