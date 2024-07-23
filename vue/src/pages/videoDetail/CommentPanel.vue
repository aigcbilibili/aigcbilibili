<template>
    <!--选择最新/最热-->
    <div class="comment-sort-select">
        <span style="display: flex; margin-right: 2rem; align-items: baseline;">
            <h3>评论</h3>
            <div class="comment-num font-second-color">
                <p v-if="commentNum<999">{{commentNum}}</p>
                <p v-else>999+</p>
            </div>
        </span>
        <p :style="{'color': hotAndNew===0?'rgba(17, 69, 125, 0.662745098)':'',
            'font-weight':hotAndNew===0?'600':''
        }" @click="hotAndNew=0" style="cursor: pointer;">最新</p>
        <div class="part-item font-second-color">|</div>
        <p :style="{'color': hotAndNew===1?'rgba(17, 69, 125, 0.662745098)':'',
            'font-weight':hotAndNew===1?'600':''
        }"  @click="hotAndNew=1" style="cursor: pointer;">最热</p>
    </div>
    <!--评论发布区-->
    <div class="create-comment-panel">
        <replyVue :value="sendUser" @send:returnReply="getNewComment()" @getData="getData"/>
    </div>
    <!--评论展示区-->
    <div v-if="commentsList&&commentsList.length>0" class="flex-column-center-container" >
        <div class="comment-item" v-for="(item,index) in commentsList" :key="index" style="border: 1px solid #f5f5f5;padding: 10px; border-radius: 10px;">
            <commentVue :commentInfo="item.topCommentResponse" :type="'top'" :videoId="videoId" :userId = "userId" :topId="item.topCommentResponse.id" :parentId="item.topCommentResponse.id" @getData="getData"/>
            <!--显示该评论的children-->
            <div v-if="item&&item.commentDetailResponses.length > 0" class="comment-replys-panel">
                <div class="children-bg">
                    <div v-for="(child,index) in item.commentDetailResponses" :key="index">
                        <commentVue :commentInfo="child" :type="'nest'" :videoId="videoId"  :userId="userId" :topId="item.topCommentResponse.id" :parentId="child.id" @getData="getData"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        <img src="@/assets/img/utils/noData.png"/>
    </div>
</template>

<script setup>
import { onMounted, ref, reactive, defineAsyncComponent } from "vue"
import { fetchAllComments,getComment } from "@/api/comment"
import { useUserInfo } from "@/store/userInfo"
import { useRoute } from 'vue-router'
const userInfo = useUserInfo() // 使用登录信息
const route = useRoute()
const videoId = route.params.videoId // 当前视频Id
const userId = userInfo.getId() // 登录用户的id
const hotAndNew = ref(0) // 0:最新，1:最热
const replyVue = defineAsyncComponent(()=>
    import ("@/components/comment/Reply")
)
const commentVue = defineAsyncComponent(()=>
    import ("@/components/comment/Comment")
)
const commentNum = ref(999) // 评论总数
// 向下一级子组件传递的评论发布区数据
const sendUser = {
    senderId: userId, // 当前登录用户id
    parentId: void 0, // null好像不行？
    receiverId: void 0, // null好像不行？
    videoId: videoId,
    avatar:  require("@/assets/img/avater.png")
}
// 向下一级子组件传递的评论查看区数据
// const data_test = [{
//     id: 1, // 评论id
//     avatar: require('@/assets/img/avater.png'), // TODO
//     senderId: 2,
//     senderName: '咸鱼2号',
//     content: '测试数据，显示失败。测试数据，显示失败。测试数据，显示失败。测试数据，显示失败。测试数据，显示失败测试数据，显示失败测试数据，显示失败测试数据，显示失败',
//     createtime: '2023-12-16 12:00:00',
//     likeCount: 29, 
//     isLike: true,
//     replyIdList:[{
//         senderName: '麻辣烧鸭',
//         senderId: 11,
//         receiverName: '清蒸鲫鱼',
//         receiverId: 13,
//         avatar: require('@/assets/img/avater.png'), 
//         content: '看到了吗！',
//         likeCount: 29, 
//         isLike: true,
//         createtime: '2023-1-16 18:24:36'
//     },{
//         senderName: '清蒸鲫鱼',
//         senderId: 13,
//         receiverName: '麻辣烧鸭',
//         receiverId: 11,
//         avatar: require('@/assets/img/avater.png'), 
//         content: '看到了wwwww',
//         likeCount: 31, 
//         isLike: false,
//         createtime: '2023-1-16 19:24:36'
//     }]
// },{
//     id: 2,
//     avatar: require('@/assets/img/avater.png'),
//     senderName: '张三',
//     senderId: 3,
//     content: '这是一条评论',
//     createtime: '2021-01-01 12:00:00',
//     likeCount: 571, 
//     isLike: false,
//     replyIdList:[]
// }]
let commentsList = ref([])
// const getNewComment = (toReply) => {
//     console.log(`获取到了什么评论：${toReply}`)
//     commentsList.push(toReply)
// }

const getData = async ()=>{
   let res =await getComment(videoId,userId,hotAndNew.value);
   console.log('111',res);
 commentNum.value = res.commentCount;
 commentsList.value = res.commentResponseList
}


onMounted(()=>{
    // const video_content = await fetchAllComments(videoId, userId)
    // const content_data = video_content=== undefined||null ? data_test : video_content
    // commentNum.value = content_data.commentCount
    // commentsList.splice(0, commentsList.length, ...(content_data.commentData)) // 使用splice更新
    getData()
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
.children-bg{
    background-color: #e2effd;
    padding: 0.5rem;
    border-radius: 10px;
}
@media screen and (min-width: 1020px){
    .comment-item{
        width: 55rem;
    }
}
</style>