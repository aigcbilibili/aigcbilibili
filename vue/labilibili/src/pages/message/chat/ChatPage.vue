<!--和别人私聊-->
<template>
   <div class="chat-panel">
        <!--左侧用户列表-->
        <div class="chat-userList">
            <chatListVue />
        </div>
        <!--纵轴隔-->
        <div class="message-vertical-divided here-vertical-divided"></div>
        <!--右侧聊天框-->
        <div class="chat-content flex-column-left-max-container">
            <div class="main-title chat-content-item">{{currentPerson.upName}}</div>
            <div class="chat-content-item">{{currentPerson.intro}}</div>
            <chatContentVue />
        </div>
   </div>     
</template>

<script setup>
import { ref, watch, onMounted, defineAsyncComponent } from "vue"
import { useUserInfo } from "@/store/userInfo"
import { useChat } from "@/store/chat"
const chatSession = useChat() // 使用聊天信息
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const reply = ref() // 回复内容
const chatListVue = defineAsyncComponent(()=>
    import ("./ChatObj.vue")
)
const chatContentVue = defineAsyncComponent(()=>
    import ("./ChatContent.vue")
)
const currentPerson = ref({
    tunnelId: 0,
    upId: 1, // 聊天对象的id
    upName: "咸鱼1号", // 聊天对象的name
    intro: "睡眠！！！！",
    avatar: require("@/assets/img/avater.png"), // 聊天对象的头像
    leastMessage: "谢谢你的关注~",
    leastMessageFrom: 2, // 0是不存在，1是自己，2是对方
    updatedUnreadFlag: true, // 是否有新的未读消息
    unreadNum: 1, // 未读新消息的条数 
})
const chatData = ref([{
    chatId: 0,
    receiverId: 1, // chatToId
    senderId: 2,
    content: "如果说昨天是梦",
    createTime: "2024-2-2 10:46",
    status: "",
},{
    chatId: 0,
    receiverId: 2, 
    senderId: 1,
    content: "那么明天则是幻想",
    createTime: "2024-2-2 10:46",
    status: "",
},{
    chatId: 0,
    receiverId: 1, 
    senderId: 2,
    content: "谢谢你的关注~",
    createTime: "2024-2-2 10:46",
    status: "",
}])
const bigModelData = ref([]) // 大模型数据
// 打开emoji界面
const openEmoji = () => {

}
// 首先获取聊天对象
const getUserList = () => {

}
// 获取新数据
const getChatData = () => {

}
// 发送已输入文字
const submitReply = () => {
    console.log('回复内容', reply.value)
    socket.send(JSON.stringify())
}
// 监听
watch(chatSession.currentUpId, (newValue, oldValue) => {
    currentPerson.value.upId = newValue
    currentPerson.value.upName = chatSession.getCurrentUp()[1]
    console.log(`切换后${currentPerson.value.upName}`)
}, {deep: true})
// 接收消息时的处理
onMounted(()=>{
   
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
.chat-panel {
    display: flex;
    flex-flow: row nowrap;
    height: 99%; 
}
.chat-userList {
    flex: 0.1 0.1 15rem;
}
.message-vertical-divided {
    margin: 0.5rem 0.5rem;
    height: 99%;
}
.chat-content {
    flex: 0.9 0.9 8rem;
    .chat-content-item {
        margin-top: 0.5rem;
        margin-left: 0.8rem;
    }
}
</style>