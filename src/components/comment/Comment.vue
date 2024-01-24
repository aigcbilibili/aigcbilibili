<!--单个评论的样式-->
<template>
    <div class="comment-wrap flex-based-container">
        <div class="comment-info">
            <div v-if="getCommentType==='top'">
                <img v-lazy="getCommentInfo.avatar" class="first-common-avatar" />
                <div class="info-item comment-username font-first-color">{{getCommentInfo.senderName}}</div>
            </div>
            <div v-else>
                <img v-lazy="getCommentInfo.avatar" class="second-common-avatar" />
                <div class="info-item comment-username font-forth-color">
                    <span>{{getCommentInfo.senderName}}</span> 回复 <span>{{getCommentInfo.receiverName}}</span> </div>
                <div class="info-item comment-content font-third-color">{{getCommentInfo.content}}</div>
            </div>
            <div class="info-item comment-others flex-based-container">
                <div>{{getCommentInfo.createtime}}</div>
                <div class="thumbs-up common-based-btn" @click="changeThumbsUpStatus()">
                    <img v-if="!commentInfo.isLike" alt="未点赞" src="@/assets/img/comment/no_thumbsUp.svg" style="width: 1rem;" />
                    <img v-else src="@/assets/img/comment/yes_thumbsUp.svg" alt="已点赞" style="width: 1rem;" />
                </div>
                <div class="reply-btn" @click="changeReplyStatus()">
                    回复
                </div>
            </div>
        </div>
        <!--如果回复评论-->
        <div v-if="isReply" class="my-reply">
            <replyVue :value="sendUser" />
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineAsyncComponent } from 'vue'
import { addThumbsUp, deleteThumbsUp } from '@/api/comment';
import { ElMessage } from 'element-plus';
const replyVue = defineAsyncComponent(()=>
    import ("@/components/comment/Reply")
)
const props = defineProps({
    videoId: {
        type: Number,
        required: true
    },
    commentInfo: {
        type: Object,
        required: true
    },
    type: {
        type: String,
        required: true
    }
})
const getCommentInfo = ref(props.commentInfo) // 评论内容
const getCommentType = props.type // 评论类型
const getVideoId = props.videoId // 视频Id
const isReply = ref(false) // 是否回复本评论
// 向下一级子组件传递的评论发布区（回复）数据
const sendUser = {
    senderId: 0,
    parentId: 0,
    receiverId: 0,
    videoId: getVideoId,
    avatar: require("@/assets/img/avater.png")
}
// 切换点赞状态
const changeThumbsUpStatus = async() =>{
    getCommentInfo.value.isLike = !getCommentInfo.value.isLike // 更改目前的前端状态
    // 调用后端接口
    if(getCommentInfo.value.isLike){
        if(!await addThumbsUp(1, 1, 1)){ // 测试
            ElMessage.error('点赞失败')
            getCommentInfo.value.isLike = false
        }
    }else{
        if(!await deleteThumbsUp()){
            ElMessage.error('取消点赞失败')
            getCommentInfo.value.isLike = true
        }
    }
}
// 切换评论状态
const changeReplyStatus = () => {
    isReply.value = !isReply.value
}
</script>

<style lang="scss" scoped>
.comment-wrap {
    width: 100%;
    height: 100%;
}
.comment-info{
    width: 85%;
    height: 90%;
    position: relative;
    text-align: left;
}
.info-item{
    position: static;
    margin-left: 1rem;
}
.comment-username{
    font-weight: 600;
}
.comment-content{
    height: auto;
    max-height: 8rem;
    margin-bottom: 0.5rem;
}
.comment-others{
    bottom: 0.3rem;
    font-size: 0.8rem;
    .thumbs-up{
        margin-left: 1rem;
        cursor: pointer;
    }
    .reply-btn{
        margin-left: 2rem;
        cursor: pointer;
        &:hover, &:active{
            color:#1f73a7;
        }
    }
}
.my-reply{
    width: 20rem;
    height: 10rem;
    background-color: #1f73a7;
}
</style>