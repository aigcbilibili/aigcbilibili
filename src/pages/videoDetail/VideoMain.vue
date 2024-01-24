<!--视频本体-->
<template>
    <div class="flex-column-container">
    <playerVue /> 
    <div class="video-info font-first-color">
        <div class="titleAndData flex-based-container">
            <h1>{{videoInfo.title}}</h1>
            <div class="likeAndCollect">
                <div class="likeAndCollect-item detail-btn common-btn-between" @click="thumbUp()">
                    <img v-if="!isLike" src="@/assets/img/thumbUp.svg" alt="未点赞"
                    style="width: 1.3rem;"/>
                    <img v-else src="@/assets/img/thumbUp.svg" alt="已点赞"
                    style="width: 1.3rem;"/>
                    <span id="likeNum">{{videoInfo.like}}</span>
                </div>
                <div class="likeAndCollect-item detail-btn common-btn-between">
                    <img src="@/assets/img/collectVideo.svg" alt="收藏数" style="width: 1.3rem;" />
                    <span id="CollectNum">{{videoInfo.collect}}</span>
                </div>
                <!--XXX 补充：分享-->
                <span class="share common-btn-center">分享</span>
                <!--XXX 补充：其他功能-->
                <span class="other-function common-btn-center">
                    <el-dropdown @command="handleMoreFunc">
                        <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="8" focusable="false" style="pointer-events: none; display: block; width: 100%; height: 100%;">
                            <path d="M7.5 12c0 .83-.67 1.5-1.5 1.5s-1.5-.67-1.5-1.5.67-1.5 1.5-1.5 1.5.67 1.5 1.5zm4.5-1.5c-.83 0-1.5.67-1.5 1.5s.67 1.5 1.5 1.5 1.5-.67 1.5-1.5-.67-1.5-1.5-1.5zm6 0c-.83 0-1.5.67-1.5 1.5s.67 1.5 1.5 1.5 1.5-.67 1.5-1.5-.67-1.5-1.5-1.5z"></path>
                        </svg>
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
            <img class="video-author-avatar" src="@/assets/img/avater.png" alt="up主" @mouseover="expandUpIntro()"/>
            <ProfileCard v-if="isUpIntro" @mouseleave="expandUpIntro()" style="z-index: 5;" />
            <span class="nameAndFollows">
                <div class="video-author-name">{{userInfo.nickName}}</div>
                <div>{{userInfo.followers}}位粉丝</div>
            </span>
        </div>
        <div class="introduction-panel">
            <div class="video-time">
            <span>{{videoInfo.playcontent}}</span>
            播放量
            <span style="margin-left: 2rem;">{{videoInfo.createtime}}</span>
            <span>发布</span>
        </div>
        <div class="video-intro">
            <!--如果不是全屏，则不展示-->
            <p v-if="isFullScreen">{{videoInfo.intro}}</p>
            <div v-else>占位</div>
        </div>
        <!--视频的tags-->
        <div class="video-tags" @v-for="(item) in `videoInfo.tags`">
            <div class="video-tag-item">???{{item}}</div>
        </div>
        </div>
    </div>
    </div>
</template>
<script setup>
import {ref, defineAsyncComponent, onMounted } from "vue"
import { getVideoDetail } from '@/api/video'
import { ElMessage } from "element-plus"
const ProfileCard = defineAsyncComponent(()=>
    import ('@/components/ProfileCard')
) 
const playerVue  = defineAsyncComponent(()=>
    import ('@/components/Player.vue')
)
const isLike = ref(false) // 是否点赞过本视频
const isUpIntro = ref(false) // 是否展开更多

const playInfo = ref()

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

/**
 * 获取视频数据
 */
const refreshVideo = async() => {
    getVideoDetail()
}

onMounted(()=>{
    
})
</script>

<style lang="scss" scoped>
.video-info{
    top: calc(60%);
    left: 1rem;
    position: absolute;
}
.titleAndData{
    width: 43rem;
}
.introduction-panel{
    width: 42rem;
    height: 4.5rem;
    padding: 0.5rem;
    margin-top: 0.5rem;
}
@media screen and (min-width:1020px) {
    .video-info{
        top: calc(63%);
        left: 1rem;
        position: absolute;
    }
    .titleAndData{
        width: 55rem;
    }
}
.share{
    background: #ffffff;
    width: 3rem;
    height: 1rem;
    padding: 0.3rem;
    border-radius: 10px; 
    border: 2px solid #79b1ec6e;
    box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    cursor: pointer;
    margin-left: 1rem;
}
.other-function{
    margin-right: 1rem;
    background:  #fff;
    border: 2px solid #79b1ec6e;
    box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    margin-left: 1.5rem;
    font-size: 1.1rem;
    width: 1.5rem;
    height: 1.5rem;
    border-radius: 50%;
    cursor: pointer;
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
.likeAndCollect{
    display: flex;
    flex-wrap: nowrap;
}
.likeAndCollect-item{
    padding-left: 1rem;
    padding-right: 1rem;
    width: 3rem;
    height: 1.8rem;
    cursor: pointer;
}
.likeAndCollect .likeAndCollect-item:not(:last-child){
    margin-right: 3rem;
}

.introduction-panel{
    background: #d0e3f8a9;
    border-radius: 20px;
}
.video-intro{
    margin-top: 1rem;
    color: #052b54a9;
}
.video-tags{
    position: absolute;
    display: flex;
    flex-wrap: wrap;
    margin-top: 1rem;
}
.video-tag-item{
    width: auto;
    height: 1.2rem;
    background-color: #79b1eca9;
}
</style>