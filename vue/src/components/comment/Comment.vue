<!--单个评论的样式-->
<template>
    <div class="comment-wrap flex-based-container">
        <div class="comment-info">
            <div v-if="getCommentType === 'top'">
                <div class="flex-based-container">
                    <el-popover placement="top-start" :width="200" trigger="hover">
                        <template #reference>
                            <img :src="getCommentInfo.senderCoverUrl"
                                style="width: 40px; height: 40px; border-radius: 50%; " />
                        </template>
                        <div class="pop-up">
                            <div class="top">
                                <img :src="getCommentInfo.senderCoverUrl"
                                    style="width: 40px; height: 40px; border-radius: 50%; " />
                                <div>{{ getCommentInfo.senderName }}</div>
                            </div>
                            <div class="footer">
                                <el-button type="text" @click="handleFollow">关注</el-button>
                                <el-button type="text" @click="handleMessage">私信</el-button>
                            </div>

                        </div>
                    </el-popover>
                    <div class="info-item comment-username font-first-color">{{ getCommentInfo.senderName }}</div>
                    <div class="edit-function" v-if="isEdit">
                        <el-dropdown @command="handleEdit">
                            <img src="@/assets/img/utils/more.svg"
                                style="width: 0.8rem; margin-left: 0.2rem; margin-top: 0.15rem;" />
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
                <div class="info-item comment-content font-third-color" style="margin-left: 50px;">{{
                    getCommentInfo.content }}</div>
            </div>
            <div v-else class="flex-based-container" style="height: 60px;">
                <el-popover placement="top-start" :width="200" trigger="hover">
                    <template #reference>
                        <img :src="getCommentInfo.senderCoverUrl"
                            style="width: 40px; height: 40px; border-radius: 50%; " />
                    </template>
                    <div class="pop-up">
                        <div class="top">
                            <img :src="getCommentInfo.senderCoverUrl"
                                style="width: 40px; height: 40px; border-radius: 50%; " />
                            <div>{{ getCommentInfo.senderName }}</div>
                        </div>
                        <div class="footer">
                            <el-button type="text" @click="handleFollow">关注</el-button>
                            <el-button type="text" @click="handleMessage">私信</el-button>
                        </div>

                    </div>
                </el-popover>
                <div class="info-item comment-username">
                    <span class="font-second-color">{{ getCommentInfo.senderName }}</span> 回复 <span
                        class="font-second-color">{{
                            getCommentInfo.receiverName }}</span>
                </div>
                <div class="info-item comment-content reply-content font-fifth-color">{{ getCommentInfo.content }}</div>
            </div>
            <div class="info-item comment-others flex-based-container">
                <div>{{ getCommentInfo.createtime }}</div>
                <div class="thumbs-up common-based-btn flex-based-container" @click="changeThumbsUpStatus()">
                    <img v-if="!commentInfo.isLike" alt="未点赞" src="@/assets/img/comment/no_thumbsUp.svg"
                        style="width: 1rem;" />
                    <img v-else src="@/assets/img/comment/yes_thumbsUp.svg" alt="已点赞" style="width: 1rem;" />
                    <p>{{ commentInfo.likeCount }}</p>
                </div>
                <div class="reply-btn change-color-btn" @click="changeReplyStatus()">
                    回复
                </div>
            </div>
            <div v-if="isReply">
                <replyVue :value="sendUser" @isShow="isShow" />
            </div>
        </div>
        <!--如果回复评论-->
    </div>
</template>
<script setup>
import { ref, defineProps, defineAsyncComponent } from 'vue'
import { useUserInfo } from "@/store/userInfo"
import { addLike, deleteLike } from '@/api/like_and_collect'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
const userInfo = useUserInfo() // 使用登录信息
const isEdit = ref(false) // 是否能修改评论
const replyVue = defineAsyncComponent(() =>
    import("@/components/comment/Reply")
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
    },
    userId: {
        type: Number,
        default: 0,
    },
    topId: {
        typeof: Number
    },
    parentId: {
        typeof: Number
    }
})
const emits = defineEmits('getData')
const getCommentInfo = ref(props.commentInfo) // 评论内容
const getCommentType = props.type // 评论类型
const getVideoId = props.videoId // 视频Id
const isReply = ref(false) // 是否回复本评论
// 向下一级子组件传递的评论发布区（回复）数据
const sendUser = {
    senderId: props.userId,
    parentId: props.parentId,
    receiverId: props.topId,
    videoId: getVideoId,
    avatar: require("@/assets/img/avater.png")
}
// 切换点赞状态
const changeThumbsUpStatus = async () => {
    getCommentInfo.value.isLike = !getCommentInfo.value.isLike // 更改目前的前端状态
    // 调用后端接口
    if (getCommentInfo.value.isLike) {
        if (!await addLike(userInfo.getId(), getVideoId, '评论')) { // 测试
            ElMessage.error('评论点赞失败')
            getCommentInfo.value.isLike = false
        }
    } else {
        if (!await deleteLike(userInfo.getId(), getVideoId, '评论')) {
            ElMessage.error('取消点赞失败')
            getCommentInfo.value.isLike = true
        }
    }
}
const router = useRouter()
const route = useRoute()
// 切换评论状态
const changeReplyStatus = () => {
    if (props.userId === 0) {
        localStorage.setItem('path', route.fullPath)
        return router.push({ path: '/login' })
    }
    isReply.value = !isReply.value
}
const isShow = () => {
    changeReplyStatus();
    emits('getData')

}




// 操作评论
const handleEdit = (command) => {
    switch (command) {
        case 'edit':
            break
        case 'delete':
            break
    }
}


//私信
const handleMessage = () => {
    sessionStorage.setItem('commentInfo', JSON.stringify(getCommentInfo.value))
    router.push({ path: `/message/MyChat/${props.userId}` })
}
</script>

<style lang="scss" scoped>
.comment-wrap {
    width: 100%;
    height: 100%;
}

.user-avatar {
    margin-left: 0.5rem;
}

.comment-info {
    width: 100%;
    height: 90%;
    position: relative;
    text-align: left;
}

.info-item {
    position: static;
    margin-left: 1rem;
}

.comment-username {
    font-weight: 600;
}

.comment-content {
    height: auto;
    max-height: 8rem;
    margin-bottom: 0.5rem;
}

.reply-content {
    margin-top: 0.55rem;
    font-size: 0.95rem;
}

.comment-others {
    bottom: 0.3rem;
    font-size: 0.8rem;

    .thumbs-up {
        margin-left: 1rem;
    }

    .reply-btn {
        margin-left: 2rem;
    }
}

.edit-function {
    border-radius: 50%;
    width: 1.2rem;
    height: 1.2rem;
    background: #fff;
    border: 2px solid #79b1ec6e;
    box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
    position: absolute;
    right: 0.5rem;
    cursor: pointer;
}

.my-reply {
    width: 20rem;
    height: 10rem;
    background-color: #1f73a7;
}
</style>