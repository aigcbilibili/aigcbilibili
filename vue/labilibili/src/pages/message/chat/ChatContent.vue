<!--右侧聊天框-->
<template>
    <div class="chat-box">

        <div class="chat-history">
            <el-scrollbar v-if="chatData" style="height: 100%;" ref="scroll">
                <div ref="chatHistoryContent">
                    <div v-for="(chatItem, index) in chatData" :key="index"
                        v-show="chatItem.type == props.type || !chatItem.type">
                        <div class=" chat-history-item left-side flex-based-container" :class="{
                            'left-side': chatItem.upId !== userId,
                            'right-side': chatItem.upId === userId
                        }" :style="{ 'flexDirection': chatItem.upId === userId ? 'row-reverse' : 'row' }">
                            <ProfileCard class="chat-avatar" :avatar="currentPerson.avatar"
                                :id="currentPerson.userId" />
                            <div :class="{
                                'chat-item-bg-other': chatItem.upId !== userId,
                                'chat-item-bg-me': chatItem.upId === userId
                            }" class="chat-item-bg chat-item-bg-other">
                                <p v-if="chatItem.upId !== 0">{{ chatItem.content }}</p>
                                <div v-else>
                                    <template v-if="chatItem.type == '0'">
                                        <p>{{ chatItem.content }}</p>
                                    </template>
                                    <template v-if="chatItem.type == '1'">
                                        <img :src="chatItem.content" alt="" style="width: 100%;">
                                    </template>
                                    <template v-if="chatItem.type == '2'">
                                        <img :src="chatItem.imgSrc" alt="" style="width: 100%;">
                                        <template v-for="(i, index) in chatItem.content" :key="index">
                                            <h1 v-show="i.index.split('.').length == 1" style="font-size: 20px;">{{
                                                i.themeName }}</h1>
                                            <h2 v-show="i.index.split('.').length == 2" style="font-size: 16px;">{{
                                                i.themeName }}</h2>
                                            <p v-show="i.index.split('.').length == 2" style="font-size: 14px;">{{
                                                i.text }}
                                            </p>
                                        </template>
                                    </template>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </el-scrollbar>
            <div v-else>
                <img src="@/assets/img/utils/noData.png" />
            </div>
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
                <el-button type="primary" class="common-btn-center send-btn send-based-btn" @click="submit()"
                    :loading="loading">发送</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, watch, nextTick } from "vue"
import { fetchChatHistory, addReply, changeSessionTime, editChatToRead, getImage, getPPT, addNewChat } from "@/api/chat"
import { useChat } from "@/store/chat"
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from "element-plus"
import { useRoute } from "vue-router"
import { inject } from 'vue'
import ProfileCard from "@/components/user/ProfileCard.vue";
import axios from 'axios'
const chatSession = useChat() // 使用聊天信息
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const chatData = ref([]) // 用户聊天的记录
const bigModelData = ref('') // 大模型发的消息
const reply = ref('') // 发送消息框
const route = useRoute()
const scroll = ref()
const chatHistoryContent = ref()
// 对方用户
// const currentPerson = inject("chatting")
const props = defineProps({
    receiverContent: {
        type: String,
        required: false,
        default: null
    },
    type: {
        type: String,
        default: '0'
    },
    upId: {
        type: Number,
        // default: 0
    },
    currentPerson: {
        type: Object,
    },
    type2: {
        type: Number
    }
})
const bigModelTest = {
    upId: 0, // 聊天对象的id
    upName: "星火API", // 聊天对象的name
    intro: "星火大模型伴你同行",
    avatar: require("@/assets/img/avater.png"), // 聊天对象的头像
    leastMessage: "星火大模型伴你同行",
    leastMessageFrom: 0, // 0是不存在，1是自己，2是对方
    updatedUnreadFlag: false, // 是否有新的未读消息
    unreadNum: 0, // 未读新消息的条数 
    type: 0
}
const currentPerson = ref(bigModelTest)
// 获取分享的内容

/**
 * WebSocket连接
 */
// 获取当前用户聊天
// 初始化websocket
// if(socket) {
//     await socket.close()
// }
const socket = new WebSocket("wss://labilibili.com/wschat") // XXX 先用这个socket
let conId = ''
console.log('uoId', props.upId);
const getImageData = async (val) => {
    reply.value = ''
    let res = await axios.get('/api/chat/getImage/' + val);
    return res;
}

socket.onmessage = async (event) => {
    console.log('111', event);
    let message = JSON.parse(event.data)

    if (message.type === 'sessionId') {
        chatSession.setSessionId(message.sessionId)
    } else if (message.type === 'message') {
        // chatData.value.push({
        //     id: message.id,
        //     createTime: message.createTime,
        //     upId: currentPerson.value.upId,
        //     content: message.content,
        // })

        if (message.senderId == props.upId) {
            setTimeout(async () => {
                await getData()
                // await changeSessionTime(userId, props.upId, message.content)
                await editChatToRead(userId, Number(props.upId))
                emits('getList')
            }, 100);

        } else {
            setTimeout(async () => {
                console.log('当前不是这个对话框');
                emits('getList')
            }, 100)

            // await changeSessionTime(userId, Number(message.senderId), message.content)

        }

    } else if (message.type === 'bigmodel') {

        if (message.status === 0) {
            // bigModelData.value += message.content 
            conId = Date.now() + 123123131;
            chatData.value.push({
                id: 0,
                createTime: new Date(),
                upId: currentPerson.value.upId,
                content: message.content,
                uuid: conId,
                type: '0',
                success: false,
            })
            console.log('333', chatData.value);


        } else if (message.status === 1) {
            chatData.value.forEach((item) => {
                if (item.uuid == conId) {
                    item.content += message.content
                }
            })


        } else {
            chatData.value.forEach((item) => {
                if (item.uuid == conId) {
                    item.content += message.content;
                    item.success = true
                }
            })
            console.log('3', currentPerson.value.upId);
            conId = ''
            console.log(chatData.value);

            // bigModelData.value = ''
        }
    }
}
socket.onopen = (event) => { // 初始化时调用
    let initMessage = {
        userId: userId,
        sessionId: chatSession.getSessionId(),
        type: "init",
        chattingWith: "receiverId"
    }
    console.log(Date.now());
    socket.send(JSON.stringify(initMessage))
}
// 连接出错时的事件处理
socket.onerror = function (event) {
    ElMessage.error(`WebSocket error出错：${event}`)
    console.error("WebSocket error observed:", event)
}
// 连接关闭时的事件处理
socket.onclose = function (event) {
    console.log("Connection closed")
}

const emits = defineEmits(['getList'])
// 获取历史聊天数据
const getData = async () => {
    if (currentPerson.value.upId !== 0) {
        chatData.value = await fetchChatHistory(userId, currentPerson.value.upId)
        console.log('66', chatData.value);
        // let scoll = document.querySelector('.scross');
        // console.log('scoll', scoll);
        // scoll.scrollTop = scoll.scrollHeight;
        // currentPerson.value.upId = chatData.value[0].upId
        nextTick(() => {
            // console.log(chatHistoryContent.value.clientHeight);
            scroll.value && scroll.value.setScrollTop(chatHistoryContent.value.clientHeight)
        })

        // scroll.value && (scroll.value.wrap.scrollTo = )
    }
}
// 发送文本消息
/**
 *   chatData.value.push({
                    id: 99999,
                    createTime: new Date(),
                    upId: userId,
                    content: reply.value,
                    leastMessage: reply.value,
                    type:'0'
                })
 */
const submitReply = async () => {
    console.log('555', currentPerson.value);
    try {
        if (socket && socket.readyState === WebSocket.OPEN && reply.value !== '') {
            let message = {}
            if (currentPerson.value.upId === 0) {
                message = {
                    type: "bigmodel",
                    question: reply.value,
                    userId: userId.toString()
                }
            } else {
                message = {
                    type: "message", // 指定消息类型为"message"
                    content: reply.value,
                    userId: userId.toString(),
                    receiverId: parseInt(currentPerson.value.upId).toString() // 发送的对象
                }
            }
            socket.send(JSON.stringify(message)) // 将消息对象转换为JSON字符串并发送
            if (currentPerson.value.upId === 0) {
                chatData.value.push({
                    id: 99999,
                    createTime: new Date(),
                    upId: userId,
                    content: reply.value,
                    leastMessage: reply.value,
                    type: '0'
                })
                reply.value = ''
            } else {
                await addNewChat(userId, Number(currentPerson.value.upId), reply.value);
                await getData()
                // await changeSessionTime(userId, Number(currentPerson.value.upId), reply.value)
                emits('getList')
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
let loading = ref(false)
//发送图片消息
const subImage = async () => {
    if (!reply.value) return

    chatData.value.push({
        id: 99999,
        createTime: new Date(),
        upId: userId,
        content: reply.value,
        leastMessage: reply.value,
        type: '1'
    })


    let res = await getImageData(reply.value);

    console.log(res);
    if (res.status === 200) {
        // console.log(res.data);
        let message = 'data:image/png;base64,'
        chatData.value.push({
            id: 0,
            createTime: new Date(),
            upId: currentPerson.value.upId,
            // content: reply.value,
            content: message + res.data,
            type: '1'
        })

    }
}
watch(() => props.upId, (val, oldval) => {
    console.log('5566', val, oldval);
    if (oldval === 0) {
        sessionStorage.setItem('chatData', JSON.stringify(chatData.value))
        chatData.value = []
    }
    if (val === 0) {
        chatData.value = JSON.parse(sessionStorage.getItem('chatData')) || []
    }
    currentPerson.value.upId = val

})
watch(() => props.type2, (val) => {
    currentPerson.value.type = val
})

//获取PPT信息
const getPPtData = async (val) => {
    reply.value = ''
    let res = await axios.get('/api/chat/getPPT/' + val);
    return res
    // console.log(res);
}
// getPPtData('写一篇关于前端的ppt')
//发送PPT消息
const subPPt = async () => {
    if (!reply.value) return

    chatData.value.push({
        id: 99999,
        createTime: new Date(),
        upId: userId,
        content: reply.value,
        leastMessage: reply.value,
        type: '2'
    })
    let res = await getPPtData(reply.value);
    if (res.status == 200) {
        console.log(res.data);
        let imgSrc = res.data.coverImgSrc;

        chatData.value.push({
            id: 0,
            createTime: new Date(),
            upId: currentPerson.value.upId,
            // content: reply.value,
            content: res.data.pptWordList,
            type: '2',
            imgSrc: imgSrc
        })
    }
}

//发送用户提问信息
const submit = () => {
    if (props.type == '0') {
        //w文本

        submitReply()

    } else if (props.type == '1') {
        //图片

        loading.value = true
        subImage()
        loading.value = false

    } else if (props.type == '2') {
        //ppt
        subPPt()
    }
}

// 捕捉session缓存是否变化
watch(() => currentPerson, async () => {
    // currentPerson.value.upId = currentPerson[0]
    if (currentPerson.value.upId === 0) return
    await getData() // 重新渲染
    await editChatToRead(userId, currentPerson.value.upId)
}, { deep: true })
onMounted(async () => {
    // 获取当前
    if (route && route.query && route.query.receiverContent) {
        reply.value = route.query.receiverContent // TODO继续优化到下一步
    }
    // 获取聊天的历史数据
    // if (currentPerson.value.upId !== 0) {
    //     await getData()
    //     await editChatToRead(userId, currentPerson.value.upId) // 0时未读
    // }
})
onBeforeUnmount(async () => {
    try {
        await socket.close(); // 关闭 WebSocket
    } catch (error) {
        console.error("连接关闭:", error);
    }
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
$item-min-height: 2.6rem;

.chat-box {
    background: #f3edf5;
    border-radius: 15px;
    margin: 1rem 1rem 1rem 1rem;
    min-height: 35.5rem;
    width: 85%;
    height: 100%;
}

.chat-avatar {
    vertical-align: 0.5rem;
    margin: 0 0.5rem;

    ::v-deep img {
        width: $item-min-height !important;
        height: $item-min-height !important;
    }
}

.chat-history {
    height: 20rem;
    padding: 1rem;

    .chat-history-item {
        margin-bottom: 0.5rem;
        overflow: hidden;
        width: auto;
        min-width: 2rem;
        max-width: 80rem;
        height: auto;
        min-height: 1rem;
        font-size: 1.1rem;
        font-weight: 500;
        display: flex;
        justify-content: flex-start;

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
}

.chat-input-box {
    padding-top: 0.3rem;
    position: relative;
    height: 14rem;

    .chat-input-head {
        height: 2.5rem;
    }

    .chat-input-middle {
        width: 86%;
        margin: 0.5rem;
        min-height: 6.5rem;
        height: auto;
        border: none !important;
        background-color: #beb7eb85 !important;
    }
}

@media screen and (min-width: 1020px) {
    .chat-box {
        width: 96%;

        .chat-input-middle {
            width: 96.5%;
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

.send-btn {
    position: absolute;
    right: 0.5rem;
    bottom: 0.5rem;
    font-size: 1.2rem !important;
}
</style>