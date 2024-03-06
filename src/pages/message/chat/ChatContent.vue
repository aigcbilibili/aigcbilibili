<!--右侧聊天框-->
<template>
    <div class="chat-box">
        <div class="chat-history">
            <el-scrollbar style="height: 100%;">
            <div v-for="(chatItem, index) in chatData" :key="index">
                <div class="chat-history-item left-side flex-based-container"
                :class="{
                    'left-side': chatItem.upId!==userId,
                    'right-side': chatItem.upId===userId
                }"
                :style="{ 'flexDirection': chatItem.upId===userId ? 'row-reverse' : 'row' }"
                >
                    <!-- <ProfileCard class="chat-avatar" :avatar="currentPerson.avatar" :id="currentPerson.userId" /> -->
                    <div :class="{'chat-item-bg-other':  chatItem.upId!==userId,
                    'chat-item-bg-me': chatItem.upId===userId}"
                    class="chat-item-bg chat-item-bg-other"><p>{{ chatItem.content }}</p>
                    </div>
                </div>
            </div>
            </el-scrollbar>
        </div>
        <div class="message-horizental-divided"></div>
        <div class="chat-input-box flex-column-left-max-container">
            <!--第一行选择-->
            <div class="chat-input-head">
                <img slt="" src="@/assets/img/chat/sendPic.svg" style="margin-left: 0.4rem;margin-right: 1rem;" />
                <img slt="" src="@/assets/img/chat/emoji.svg" @click="openEmoji()" />
            </div>
            <!--输入-->
            <textarea class="chat-input-middle" v-model="reply"></textarea>
            <!--确认发送框-->
            <div class="chat-input-last">
                <el-button type="primary" class="detail-btn send-btn" @click="submitReply()">发送</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, watch } from "vue"
import { fetchChatHistory, addReply, changeSessionTime, editChatToRead } from "@/api/chat"
import { useChat } from "@/store/chat"
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from "element-plus"
import { route } from "vue-router"
import ProfileCard from "@/components/user/ProfileCard.vue"
const chatSession = useChat() // 使用聊天信息
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const chatData = ref([]) // 用户聊天的记录
const bigModelData = ref([]) 
const reply = ref('') // 发送消息框
let currentPerId = chatSession.getCurrentUp()[0]
// 对方用户
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
// 获取聊天数据
const getData = async() => {
    chatData.value = await fetchChatHistory(userId, currentPerId)
}
// 获取分享的内容
const props = defineProps({
    receiverContent: {
        type: String,
        required: false,
        default: null
    }
})
/**
 * WebSocket连接
 */
// 获取当前用户聊天
const tmp = chatSession.getCurrentUp()
currentPerson.value.upId = tmp[0]
currentPerson.value.upName = tmp[1]
// 初始化websocket
const socket = new WebSocket("ws://120.55.85.249:1688/ljl/bilibili/chat") // XXX 先用这个socket
socket.onmessage = (event) => {
    let message = JSON.parse(event.data)
    if(message.type === 'sessionId') {
        chatSession.setSessionId(message.sessionId)
    } else if(message.type === 'message') {
        chatData.value.push({
            id: message.id,
            createTime: message.createTime,
            upId: currentPerId,
            content: message.content
        })
    } else if(message.type === 'bigmodel') {
        console.log("看下大模型传来的数据", message)
        bigModelData.push({
            content: message.content
        })
    }
}
socket.onopen = (event) => { // 初始化时调用
    console.log(`如果${chatSession.getSessionId()}`)
    let initMessage = {
        userId: userId,
        sessionId: chatSession.getSessionId(),
        type: "init",
        chattingWith: "receiverId"
    }
    console.log(`发送过去的${JSON.stringify(initMessage)}`)
    socket.send(JSON.stringify(initMessage))
}
// 连接出错时的事件处理
socket.onerror = function(event) {
    ElMessage.error(`WebSocket error出错：${event}`)
    console.error("WebSocket error observed:", event)
}
// 连接关闭时的事件处理
socket.onclose = function(event) {
    console.log("Connection closed")
}
// 发送消息
const submitReply = () => {
    try {
        if (socket && socket.readyState === WebSocket.OPEN && reply.value!='') {
            let message = {
                type: "message", // 指定消息类型为"message"
                content: reply.value,
                receiverId: currentPerId// 发送的对象
            };
            socket.send(JSON.stringify(message)); // 将消息对象转换为JSON字符串并发送
            const replyRes = addReply(reply.value, userId, currentPerId)
            if(replyRes) {
                changeSessionTime(userId, currentPerId, reply.value)
                chatData.value.push({
                    id: 99999,
                    createTime: new Date(),
                    upId: userId,
                    content: reply.value
                })
                reply.value = ''
            }
        } else {
            ElMessage.error("socket未创建或回复为空")
            console.error("socket未创建或回复为空")
        }
    } catch (e) {
        ElMessage.error(`遇到问题${e}`)
        console.error(`遇到问题${e}`)
    }
}
// 捕捉session缓存是否变化
watch(chatSession.currentUpId, async() => {
    currentPerId = chatSession.getCurrentUp()[0]
    console.log("聊天数据的切换？", chatData.value)
    await getData() // 重新渲染
}, {deep: true})
onMounted(async()=>{
    // 获取当前
    if(props.receiverContent) {

    }
    // 获取当前focus的
    console.log("本机登录用户", userId)
    // 获取聊天的历史数据
    getData()
    // TODO 切换焦点时也需要做请求
    await editChatToRead(userId, currentPerId) // 0时未读
})
onBeforeUnmount(()=>{
    socket.close() // 关闭webSocket
})
</script>

<style lang="scss" scoped>
.chat-box {
    background: #f3edf5;
    border-radius: 15px;
    margin: 1rem 1rem 1rem 1rem;
    min-height: 35.5rem;
    width: 85%;
    height: 100%;
}
.chat-history {
    height: 20rem;
    padding: 1rem;
    .chat-history-item {
        margin-bottom: 0.5rem;
        min-width: 2rem;
        width: auto;
        max-width: 50rem;
        min-height: 1rem;
        font-size: 1.1rem;
        font-weight: 500;
        .chat-item-bg {
            padding: 0.5rem;
            border-radius: 15px;
            max-width: 10rem;
            height: auto;
        }
        .chat-item-bg-other {
            background: #fff;
        }
        .chat-item-bg-me {
            background: #3d4d7a;
            color: #fff; 
        }
    }
    .left-side {
        display: flex;
        justify-content: flex-start;
        // text-align: left;
        margin-left: 0.5rem;
    }
    .right-side {
        display: flex;
        justify-content: flex-end;
        // text-align: right;
        margin-right: 0.5rem;
    }
}
.chat-input-box {
    padding-top: 0.3rem;
    height: 13.4rem;  
    .chat-input-head {
        height: 2.5rem;
    }  
    .chat-input-middle {
        width: 86%;
        margin: 1rem 0.5rem;
        height: 6rem;
        border: none !important;
        background-color: #beb7eb85 !important;
    }
}
.send-btn {

}
@media screen and (min-width: 1020px) {
    .chat-box {
        width: 96%;
        .chat-input-middle {
            width: 95%;
        }
        .chat-history {
            .chat-history-item {
                .chat-item-bg {
                    max-width: 40rem;
                }
            }
        }
    }
    .chat-avatar {
        display: flex;
    }
}
</style>