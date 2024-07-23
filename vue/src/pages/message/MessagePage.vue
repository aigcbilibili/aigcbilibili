<!--私聊/消息回复界面-->
<template>
    <div class="message-panel">
        <!--左栏选择-->
        <SidebarPanel class="message-sidebar" :id="1" :menuConfig="menuConfig" :isCollapseVal="isHalf" />
        <!--中间-->
        <message-content>
            <router-view v-slot="{ Component }">
                <transition name="move" mode="out-in">
                    <component :is="Component"></component>
                </transition>
            </router-view>
        </message-content>
    </div>
</template>

<script setup>
import { onMounted, ref, watch, onBeforeUnmount } from 'vue'
import { useRouter } from "vue-router"
import { useUserInfo } from "@/store/userInfo"
import SidebarPanel from '@/components/Sidebar'
import Debounce from '@/static/debounce'
const isHalf = ref(true) // 是否为半屏
const width = ref(720) // 需要监听宽度
const debounce = new Debounce() // 防抖
const userInfo = useUserInfo() // 使用登录信息
const router = useRouter()
const id = userInfo.getId()
const menuConfig = [{
    icon: "MessageBox",
    index: `/message/Reply/${id}`,
    title: "回复我的",
    permiss: true, // 本页面没有权限的配置
}, {
    icon: "Present",
    index: `/message/ThumbsUpMe/${id}`,
    title: "点赞我的",
    permiss: true,
}, {
    icon: "ChatDotRound",
    index: `/message/MyChat/${id}`,
    title: "我的聊天",
    permiss: true,
}]
// },{
//     icon: "ScaleToOriginal",
//     index: `/message/SystemNotice/${id}`,
//     title: "系统通知",
//     permiss: true, 
// },{
const getHalf = (newValue) => {
    if (parseInt(newValue) < 720) {
        isHalf.value = true
    } else {
        isHalf.value = false
    }
}
// 判断当前是否为半屏
watch(width, async (newValue, oldValue) => {
    getHalf(newValue)
    await debounce.debounceEnd(100)
    window.addEventListener("updateWhenNewSize", debounce)
})
// 监听是否是半屏
window.onresize = () => {
    return (() => {
        width.value = document.documentElement.clientWidth
    })()
}
getHalf(document.documentElement.clientWidth)
onBeforeUnmount(() => {
    window.removeEventListener("updateWhenNewSize", debounce)
})
onMounted(() => {
    router
})
</script>

<style lang="scss" scoped>
.message-panel {
    position: relative;
    display: flex;
    max-height: 100%;
}

.message-sidebar {
    max-width: 12rem !important;
    width: auto;
}

message-content {
    box-shadow: -0.3rem -0.2rem 25px 1px rgb(169, 157, 244);
    // position: absolute;
    /* background-color: #3c7561; */
    border-radius: 20px;
    margin: 2rem 2rem 2rem 2rem;
    width: auto;
    height: calc(80vh);
    flex: 1;
}

@media screen and (min-width: 1020px) {
    message-content {
        width: calc(85vw);
    }
}
</style>