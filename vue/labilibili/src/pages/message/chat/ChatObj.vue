<!--聊天对象左侧-->
<template>
    <div style="position: relative; width: 100%; height: 100%;">
        <!--ChatGPT通道-->
        <div @click="bigModelTunnel()" class="chat-item flex-based-container"
            :class="{ 'chat-clicked': currentPerson.upId === 0 }">
            <img src="@/assets/img/utils/avatar_new_01.jpg" class="first-common-avatar chat-avatar" />
            <div class="flex-column-left-max-container">
                <p style="font-weight: 600;">星火API</p>
                <p style="font-size: 0.9rem;">星火大模型伴你同行</p>
            </div>
        </div>
        <!--聊天对象列表-->
        <div v-if="personList.length === 0" style="height: 99%;" class="flex-center-container">
            <p style="color: rgb(135 127 200); font-weight: 600; font-size: 1.2rem;">暂无好友~</p>
        </div>
        <div v-else v-for="(chatPerson, index) in personList" :key="index">
            <div @click="changeTunnel(chatPerson)" class="chat-item flex-based-container"
                :class="{ 'chat-clicked': currentPerson.upId === chatPerson.upId }">
                <img :src="chatPerson.avatar" class="first-common-avatar chat-avatar" />
                <div class="flex-column-left-max-container">
                    <p style="font-weight: 600;">{{ (chatPerson.upName && chatPerson.upName.length) >= 9
                        ? chatPerson.upName.substr(0, 8) + "..."
                        : chatPerson.upName }}</p>
                    <p style="font-size: 0.9rem;" :class="{
                        'me-message': chatPerson.leastMessageFrom === 1,
                        'other-message': chatPerson.leastMessageFrom === 2
                    }">{{ chatPerson.leastMessage.length >= 10 ?
                        chatPerson.leastMessage.substr(0, 9) + "..." : chatPerson.leastMessage }}</p>
                </div>
                <div v-if="!chatPerson.status && chatPerson.type !== 1" class="weidu">{{ chatPerson.count }}</div>
            </div>
        </div>
        <!--后续数据-->

    </div>
</template>

<script setup>
import { onMounted, onBeforeMount, ref, inject, defineProps, watch, } from 'vue'
import { fetchAllPeople, addNewChat, changeSessionTime, editChatToRead } from "@/api/chat"
import { fetchFollowingsList, fetchFollowersList } from "@/api/user"
import { useUserInfo } from "@/store/userInfo"
import { useChat } from "@/store/chat"
import { useRoute } from "vue-router"
import { ElMessage } from 'element-plus'
import { usemessageConter } from "@/store/messageConter"
import { fetchNoticeNum } from "@/api/notice"

const messageConter = usemessageConter()
const isAddSession = ref(false) // 是否添加会话
const route = useRoute()
const chatSession = useChat() // 使用聊天信息
const userInfo = useUserInfo() // 使用登录信息 
const userId = userInfo.getId() // 登录用户的id
const personList = ref([]) // 聊天对象列表
// const friendData = ref([]) // 好友列表
const defaultBigModel = { // 默认是大模型的id
    upId: 0, // 聊天对象的id
    upName: "星火API", // 聊天对象的name
    intro: "星火大模型伴你同行",
    avatar: require("@/assets/img/avater.png"), // 聊天对象的头像
    leastMessage: "星火大模型伴你同行",
    leastMessageFrom: 2, // 0是不存在，1是自己，2是对方
    updatedUnreadFlag: true, // 是否有新的未读消息
    unreadNum: 1, // 未读新消息的条数 
    type: 0, //0是正式聊天，1是临时的
}
const currentPerson = ref(defaultBigModel) // 当前用户

// 切到星火大模型
const bigModelTunnel = () => {
    currentPerson.value = defaultBigModel
}


// 修改当前用户的Tunnel
const changeTunnel = (Tunnel) => {
    console.log('Tunnel', Tunnel);
    currentPerson.value = Tunnel;

    // editChatToRead(userId,)

}

const getData = async () => {
    personList.value = await fetchAllPeople(userId)
    let list = JSON.parse(sessionStorage.getItem('commentInfo'))
    if (list) {
        let obj = personList.value.find((item) => {
            return item.upId == list.senderId
        })
        if (list && !obj) {
            personList.value && personList.value.unshift({
                avatar: list.senderCoverUrl,
                createTime: new Date(),
                upName: list.senderName,
                upId: list.senderId,
                leastMessage: "",
                leastMessageFrom: 0,
                type: 1

            })

        } else if (list && obj) {

        }
    }
}

defineExpose({
    getData
})

onMounted(() => {
    // 获得聊天对象列表

    fetchAllPeople(userId).then((res) => {
        personList.value = res
        let list = JSON.parse(sessionStorage.getItem('commentInfo'))

        if (list) {
            let obj = personList.value.find((item) => {
                return item.upId == list.senderId
            })
            if (list && !obj) {
                personList.value && personList.value.unshift({
                    avatar: list.senderCoverUrl,
                    createTime: new Date(),
                    upName: list.senderName,
                    upId: list.senderId,
                    leastMessage: "",
                    leastMessageFrom: 0,
                    type: 1

                })
                currentPerson.value = {
                    avatar: list.senderCoverUrl,
                    createTime: new Date(),
                    upName: list.senderName,
                    upId: list.senderId,
                    leastMessage: "",
                    leastMessageFrom: 0,
                    type: 1
                }
            } else if (list && obj) {
                currentPerson.value = obj
            }
        }

    })

})
/**
 *  upId: 0, // 聊天对象的id
    upName: "星火API", // 聊天对象的name
    intro: "星火大模型伴你同行",
    avatar: require("@/assets/img/avater.png"), // 聊天对象的头像
    leastMessage: "星火大模型伴你同行",
    leastMessageFrom: 2, // 0是不存在，1是自己，2是对方
    updatedUnreadFlag: true, // 是否有新的未读消息
    unreadNum: 1, // 未读新消息的条数 
 */
const emits = defineEmits(['handleChange'])
watch(() => currentPerson.value, async (val) => {
    console.log('222', val);
    if (val.upId == userId) {
        return ElMessage({
            type: 'warning',
            message: '您不能和自己聊天哦'
        })
    }
    if (!val.status) {
        await editChatToRead(userId, Number(val.upId));
        const data_tmp = await fetchNoticeNum(userId);
        messageConter.data = data_tmp
        personList.value.forEach((item) => {
            if (item.upId === val.upId) {
                item.status = true
            }
        })
    }
    emits('handleChange', val)


})
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";

@mixin func-btn {
    border-radius: 20px;

    &:hover,
    &:active {
        border-radius: 20px;
    }
}

.me-message {
    color: #4e6ba5;
}

.other-message {
    color: #0a725e;
}

.chat-avatar {
    margin: 0 1.3rem 0 1.2rem;
}

.chat-item {
    margin: 0.5rem 0 0.5rem 0.5rem;
    width: 100%;
    position: relative;
    // padding: 0.5rem;
    cursor: pointer;

    &:active {
        background: #d3def5;
        border-radius: 10px;
    }

    .weidu {
        position: absolute;
        right: 10px;
        top: 10px;
    }
}

.new-add {
    @include func-btn;
    background: #a2b7e1;

    &:hover,
    &:active {
        background: #4e6ba5;
    }
}

.get-more {
    @include func-btn;
    border: 1px solid #a2b7e1;
    position: absolute;
    bottom: 0.5rem;

    &:hover,
    &:active {
        background: #fff;
    }
}

.chat-clicked {
    background: #d3def5;
    margin: 0.5rem 0 0.5rem 0.5rem;
    border-radius: 10px;
}

.modal-mask {
    position: absolute;
    top: -0.5rem;
    left: 0;
    width: 103%;
    height: 101%;
    background-color: rgba(0, 0, 0, 0.5);
    /* 半透明黑色背景 */
    z-index: 999;
    /* 置于其他内容之上 */
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 20px;
}

.modal-container {
    background-color: white;
    padding: 20px;
    border-radius: 15px;
    min-height: 3rem;
    max-height: 20rem;
    width: 70%;

    .add-item {
        width: 100%;
        align-items: stretch;
        margin-bottom: 0.5rem;
    }
}

.add-title {
    font-weight: 600;
    font-size: 1.1rem;
}

@media screen and (min-width: 1020px) {
    .get-more {
        bottom: 1rem;
    }

    .chat-item {
        width: 93%;
    }
}

.send-btn {
    width: auto;
    height: auto;
    font-size: 0.9rem;
    padding: 0.1rem 0.2rem;
    color: #fff;
}

.weidu {
    width: 20px;
    height: 20px;
    text-align: center;
    line-height: 20px;
    background-color: red;
    color: #fff;
    border-radius: 50%;
    float: right;
}
</style>