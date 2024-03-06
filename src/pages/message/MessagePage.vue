<!--私聊/消息回复界面-->
<template>
    <div class="message-panel">
        <!--左栏选择-->
        <SidebarPanel class="message-sidebar" :id="1" :menuConfig="menuConfig"/>
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
import SidebarPanel from '@/components/Sidebar'
import { onMounted } from 'vue'
import { useRouter } from "vue-router"
import { useUserInfo} from "@/store/userInfo"
const userInfo = useUserInfo() // 使用登录信息
const router = useRouter()
const id = userInfo.getId()
const menuConfig = [{
    icon: "Odometer",
    index: `/message/Reply/${id}`,
    title: "回复我的",
    permiss: 0, // 本页面没有权限的配置
},{
    icon: "Odometer",
    index: `/message/At/${id}`,
    title: "@我的",
    permiss: 0, 
},{
    icon: "Odometer",
    index: `/message/ThumbsUpMe/${id}`,
    title: "点赞我的",
    permiss: 0, 
},{
    icon: "Odometer",
    index: `/message/SystemNotice/${id}`,
    title: "系统通知",
    permiss: 0, 
},{
    icon: "Odometer",
    index: `/message/MyChat/${id}`,
    title: "我的聊天",
    permiss: 0, 
}]
onMounted(()=>{
    router
})
</script>

<style lang="scss" scoped>
.message-panel {
    position: relative;
    display: flex;
}
.message-sidebar {
    position: fixed;
    top: 6.5rem; /* 没有这句代码无法显示 */
    left: 0;
    width: 12rem !important;
    height: calc(87vh) !important;
}
message-content{
    box-shadow: -0.3rem -0.2rem 25px 1px rgb(169, 157, 244);
    // position: absolute;
    /* background-color: #3c7561; */
    border-radius: 20px;
    margin: 2rem 2rem 2rem 14rem;
    width: calc(70vw); /* XXX不知道为什么用不了!important */
    height: calc(80vh);
}
@media screen and (min-width: 1020px){
    message-content{
        width: calc(85vw);
    }
}
</style>