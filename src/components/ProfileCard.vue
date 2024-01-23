<!--移到用户头像上浮出的关注框
本页面初始传递的数据：
    id(up/user/other)
    name
    avatar
-->
<template>
    <div class="profile-pop-up">
        <div class="profile-pop-up-name font-first-color">{{testData.name}}</div>
        <div class="follow-data">
            <span style="vertical-align: -0.1rem;" @click="follow()">关注数{{testData.followingNum}}</span>
            <span>粉丝数{{testData.fansNum}}</span>
        </div>
        <div class="profile-pop-up-intro">
            <p>{{testData.intro}}</p>
        </div>
        <div class="concern-group">
            <button v-if="!infoData.isFocus" class="concern common-btn-1 main-first-color">关注</button>
            <button v-else-if="infoData.isSame">无法关注</button>
            <button class="sendMessage common-btn-1">消息</button>
         </div>
    </div>
</template>

<script setup>
import { getProfile } from '@/api/info'
// import { addFollowing, removeFollowing } from '@/api/follow'
import { reactive } from 'vue'
import { onMounted } from 'vue'
onMounted(()=>{
    // 获取信息数据
    getProfile(1).then(res=>{
        console.log(res)
    })
})
// 要放上去的数据
const infoData = reactive({
    name: '咸鱼',
    avatar: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3119999999,1391111111&fm=26&gp=0.jpg',
    followingNum: 100, // 关注人数
    fansNum: 100, // 粉丝人数
    intro: '[读取失败] 测试：做个快乐的咸鱼',
    isFocus: false,
    isSame: false
})
// 没数据时的测试
const testData = {
    name: '咸鱼',
    avatar: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3119999999,1391111111&fm=26&gp=0.jpg',
    followingNum: 100, // 关注人数
    fansNum: 100, // 粉丝人数
    intro: '测试数据：做个快乐的咸鱼',
    isFocus: false
}

/**
 * 关注逻辑的实现
 */
const follow = ()=>{
    // 判断是否登录对象和
    infoData.isFocus = ~infoData.isFocus
    if(infoData.isFocus){
        // 取消关注
        // const res = removeFollowing()
        alert('关注成功')
    }else{
        // 关注
        // const res = addFollowing()
        alert('关注成功')
        
    }
}

</script>

<style scoped>
.profile-pop-up{
    position: absolute;
    top: 2.5rem; /* position: absolute定位 */
    left: 3rem; 
    max-width: 20rem;
    height: 7.2rem;
    padding: 1rem;
    border-radius: 10px;
    z-index: 5;
    background: #fff;
    box-shadow: 0 2px 25px 0 #79b1eca9;
}
button{
    width: 3.5rem;
    height: 1.6rem;
    border-radius: 10px;
    border: none;
}
.profile-pop-up-name{
    font-weight: 600;
}
.profile-pop-up-intro{
    margin-top: 0.5rem;
    height: 2.5rem;
    overflow: hidden;
}
.follow-data span:first-child{
    margin-right: 1rem;
}
.concern-group{
    position: absolute;
    right: 1rem;
}
.concern-group button:first-child{
    margin-right: 1rem;
}
.concern{
    font-size: 0.9rem;
    color: #fff;
}
.concern:hover{
    background: #6ba2dca9;
}
.concern:active{
    background: #4e7caea9;
}
.not-concern{
    background: #fff;
    color: #79b1eca9;
    border: 1px solid #79b1eca9;
    font-size: 0.9rem;
}
</style>