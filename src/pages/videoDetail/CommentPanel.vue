<template>
    <!--排序选择-->
    <div class="comment-sort-select">
        <span style="display: flex; margin-right: 2rem; align-items: baseline;">
            <h3>评论</h3>
            <p class="comment-num font-second-color">{{videoInfo.commentNum}}</p>
        </span>
        <p>最新</p>
        <div class="part-item font-second-color">|</div>
        <p>最热</p>
    </div>
    <!--评论发布区-->
    <div class="create-comment-panel">
        <div class="comment-form flex-center-container">
                <img :src="userInfo.avatar" />
            <textarea class="comment-area" v-model="newComment.content" placeholder="和谐的评论更能促进交流~"></textarea>
            <button class="send-comment-btn common-btn-1 detail-btn" @click="addComment">发布评论</button>
        </div>
    </div>
    <!--评论展示区-->
    <div class="comment-panel">
        <div class="comment-item" v-for="(item,index) in commentList" :key="index">
            <div class="comment-item-img">
                <img :src="item.avatar" alt="avater"/>
            </div>
            <div class="comment-item-info">
                <div class="comment-item-name">{{item.nickname}}</div>
                <div class="comment-item-time">{{item.createtime}}</div>
                <div class="comment-item-content">{{item.content}}</div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onBeforeMount, ref, reactive, defineAsyncComponent } from "vue"
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
const userInfo = {
    nickName:'匿名',
    avatar:'', // 对应的cover
    following: 100, // 关注数
    followers:100, // TODO 待加入
}
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
 * 评论区展示内容
 */
const commonList = [{
    id: 0,
    avatar: '@/assets/img/avater.png', // TODO
    nickname: '咸鱼2号',
    content: '测试数据，显示失败',
    createtime: '2023-12-16',
    reaplyId: ''
},{
    id: 1,
    nickname: '张三',
    avatar: 'https://img.yzcdn.cn/vant/cat.jpg',
    createtime: '2021-01-01 12:00:00',
    content: '这是一条评论'
}]
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
</script>

<style lang="scss" scoped>
.comment-item-img{
    width: 3rem;
    height: 3rem;
    border-radius: 50%;
    // overflow: hidden;
}
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
.comment-form{
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1rem 1rem;
}
.comment-form .comment-area{
    width: 35rem;
    height: 10rem;
    border-radius: 10px;
}
.comment-form .send-comment-btn{
    width: auto;
    height: auto;
    padding: 0.4rem;
    margin-left: 0.5rem;
}
</style>