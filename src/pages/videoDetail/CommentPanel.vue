<template>
    <!--选择最新/最热-->
    <div class="comment-sort-select">
        <span style="display: flex; margin-right: 2rem; align-items: baseline;">
            <h3>评论</h3>
            <p class="comment-num font-second-color">{{commentNum}}</p>
        </span>
        <p>最新</p>
        <div class="part-item font-second-color">|</div>
        <p>最热</p>
    </div>
    <!--评论发布区-->
    <div class="create-comment-panel">
        <replyVue :value="sendUser" />
    </div>
    <!--评论展示区-->
    <div class="comment-panel flex-column-center-container">
        <div class="comment-item" v-for="(item,index) in commentsList" :key="index">
            <commentVue :commentInfo="item" :type="'top'" :videoId="videoId" />
            <!--显示该评论的children-->
            <div v-if="item.replyIdList!=[]" class="comment-replys-panel">
                <div v-for="(child,index) in item.replyIdList" :key="index">
                    <commentVue :commentInfo="child" :type="'nest'" :videoId="videoId" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, reactive, defineAsyncComponent, defineProps } from "vue"
import { fetchAllComments } from "@/api/comment" 
const replyVue = defineAsyncComponent(()=>
    import ("@/components/comment/Reply")
)
const commentVue = defineAsyncComponent(()=>
    import ("@/components/comment/Comment")
)
const props = defineProps({
    videoId: {
        type: Number,
        required: true
    }
})
// 父组件传递的和上传评论相关的数据
const videoId = ref(props.videoId) 
const commentNum = ref(999) // 评论总数
// 向下一级子组件传递的评论发布区数据
const sendUser = {
    senderId: 0, // 当前登录用户id
    parentId: null,
    receiverId: null,
    videoId: videoId,
    avatar: require("@/assets/img/avater.png")
}
// 向下一级子组件传递的评论查看区数据
const data_test = [{
    id: 1, // 评论id
    avatar: require('@/assets/img/avater.png'), // TODO
    senderId: 2,
    senderName: '咸鱼2号',
    content: '测试数据，显示失败。测试数据，显示失败。测试数据，显示失败。测试数据，显示失败。测试数据，显示失败测试数据，显示失败测试数据，显示失败测试数据，显示失败',
    createtime: '2023-12-16 12:00:00',
    likeCount: 29, 
    isLike: true,
    replyIdList:[{
        senderName: '麻辣烧鸭',
        senderId: 11,
        receiverName: '清蒸鲫鱼',
        receiverId: 13,
        avatar: require('@/assets/img/avater.png'), 
        content: '看到了吗！',
        likeCount: 29, 
        isLike: true,
        createtime: '2023-1-16 18:24:36'
    },{
        senderName: '清蒸鲫鱼',
        senderId: 13,
        receiverName: '麻辣烧鸭',
        receiverId: 11,
        avatar: require('@/assets/img/avater.png'), 
        content: '看到了wwwww',
        likeCount: 31, 
        isLike: false,
        createtime: '2023-1-16 19:24:36'
    }]
},{
    id: 2,
    avatar: require('@/assets/img/avater.png'),
    senderName: '张三',
    senderId: 3,
    content: '这是一条评论',
    createtime: '2021-01-01 12:00:00',
    likeCount: 571, 
    isLike: false,
    replyIdList:[]
}]
let commentsList = reactive([])

onMounted(async()=>{
    const tmp = await fetchAllComments(1)
    console.log('测试测试', tmp)
    commentsList.splice(0, commentsList.length, ...(tmp == undefined ? data_test : tmp)) // 使用splice更新
})
</script>

<style lang="scss" scoped>
.comment-sort-select{
    display: flex;
    justify-content: left;
    padding: 1rem 1rem;
    align-items: center;
}
.comment-sort-select > h3{
    margin-right: 1rem;
}
.comment-sort-select > p{
    margin-right: 1rem;
}
.comment-num{
    margin-left: 0.5rem;
    display: inline-block;
    vertical-align: middle;
}
.part-item{
    margin-left: -0.5rem;
    margin-right: 0.5rem;
}
.comment-item{
    width: 40rem;
    height: auto;
    min-height: 3rem;
    margin-bottom: 1rem;
}
@media screen and (min-width: 1020px){
    .comment-item{
        width: 50rem;
    }
}
</style>