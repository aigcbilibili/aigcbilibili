<!--用户中心
本页面初始传递的数据：
    id(up/user/other) 
    name
    avatar
    followingNum 关注数
    fansNum
    intro
    isSame 该用户与登录用户是否为同一人
-->
<template>
    <div class="user-panel">
        <SidebarPanel class="sidebar"/>
        <user-content>
            <router-view v-slot="{ Component }">
                <transition name="move" mode="out-in">
                    <component :is="Component"></component>
                </transition>
            </router-view>
        </user-content>
    </div> 
</template>

<script setup>
import SidebarPanel from '@/components/SidebarPanel'
import { useRouter } from "vue-router"
const router = useRouter()
router

</script>

<style scoped>
.user-panel{
    width: 100%;
    height: 100%;
    display: flex;
    flex-flow: row nowrap;
    position: relative;
}
.sidebar{
    position: absolute;
    top: 0rem;
    left: 0rem;
    width: auto;
    height: calc(87vh);
}
user-content{
    position: absolute;
    box-shadow: -0.3rem -0.2rem 25px 1px rgb(169, 157, 244);
    /* background-color: #3c7561; */
    border-radius: 20px;
    margin-left: 8rem;
    margin-right: 2rem;
    margin-top: 2rem;
    margin-bottom: 2rem;
    width: calc(79vw);
    height: calc(80vh);
}
@media screen and (min-width: 1020px) {
    user-content{
        width: calc(89vw);
    }
}
</style>