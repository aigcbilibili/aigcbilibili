<!--回复我的-->
<template>
    <div class="wrap">
        <p class="component main-title font-fifth-color">回复我的</p>
        <el-scrollbar style="height: 92%;">
        <div class="component reply-content">
            <div v-for="(reply, index) in replyList" :key="index" class="reply-item-wrap">
                <!---->
                <div class="item flex-based-container">
                    <img :src="reply.avatar" class="first-common-avatar"/>
                    <div class="flex-column-left-max-container item-content">
                        <div class="reply-item-first-layer flex-based-container">
                            <p class="item-title">{{reply.name}}</p>回复了你的
                            <p v-if="reply.objType===0">视频</p>
                            <p v-else>评论</p>
                        </div>
                        <div class="item-content">{{reply.content}}</div>
                        <div class="reply-item-use">
                            <span class="change-color-btn">{{reply.createTime}}</span>
                            <span class="change-color-btn" style="margin-left: 0.3rem;">点赞</span>
                            <span class="change-color-btn" style="margin-left: 0.3rem;">回复</span>
                        </div>
                    </div>
                </div>
                <div class="horizontal-divided-line"></div>
            </div> 
        </div>
        </el-scrollbar>
    </div>     
</template>

<script setup>
import { onMounted } from "vue"
import { editReplyToRead } from "@/api/notice"
import { useUserInfo} from "@/store/userInfo"
const replyList = [{
    commentId: 1,
    name: "南",
    objType: 0, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id，或视频或评论
    avatar: require("@/assets/img/cover.jpeg"),
    content: "年少多轻狂",
    isLike: true,
    createTime: "2024-1-31 21:01"
},{
    commentId: 1,
    name: "北",
    objType: 1, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id
    avatar: require("@/assets/img/cover.jpeg"),
    content: "苦痛但何方",
    isLike: false,
    createTime: "2024-1-31 21:01"
},{
    commentId: 1,
    name: "东",
    objType: 0, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id
    avatar: require("@/assets/img/cover.jpeg"),
    content: "年少多轻狂",
    isLike: false,
    createTime: "2024-1-31 21:01"
},{
    commentId: 1,
    name: "西",
    objType: 0, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id
    avatar: require("@/assets/img/cover.jpeg"),
    content: "年少多轻狂",
    isLike: false,
    createTime: "2024-1-31 21:01"
},{
    commentId: 1,
    name: "西",
    objType: 0, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id
    avatar: require("@/assets/img/cover.jpeg"),
    content: "年少多轻狂",
    isLike: true,
    createTime: "2024-1-31 21:01"
},{
    commentId: 1,
    name: "西",
    objType: 0, // 0为评论视频，1为评论评论
    objId: 1, // 评论对象的id
    avatar: require("@/assets/img/cover.jpeg"),
    content: "年少多轻狂",
    isLike: true,
    createTime: "2024-1-31 21:01"
}]
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const thumbsUpData = []
// 获取点赞通知的数据
onMounted(async()=>{
    await editReplyToRead(userId)
})
</script>
 
<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
// .reply-item-name {
//     font-weight: 600;
//     margin-right: 0.3rem;
// }
</style>