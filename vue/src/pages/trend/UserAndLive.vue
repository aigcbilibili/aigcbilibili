<template>
    <!--直播信息列表-->
    <div class="">
        <!--用户信息-->
        <div class="based-box trend-item login-user-info">
            <div class="">
                <div class="flex-left-container">
                    <img class="first-common-avatar" :src="userInfo.getAvatar()" style="margin: 0 2rem 0 1rem;" />
                    <div class="flex-column-left-max-container">
                        <p class="up-name">{{ loginUser.name }}</p>
                        <p>{{ loginUser.intro }}</p>
                    </div>
                </div>
            </div>
            <!--关注、粉丝、动态发布数量-->
            <div class="flex-center-container" style="margin-top: 1rem;">
                <div v-for="(item, index) in userDetailInfo" :key=index class="detail-num-wrap">
                    <p>{{ item.name }}</p>
                    <p v-if="item.num <= 999">{{ item.num }}</p>
                    <p v-else>999+</p>
                </div>
            </div>
        </div>
        <!--直播信息-->
        <div class="based-box trend-item live-wrap">
            <p class="live-wrap-title">正在直播</p>
            <div style="margin-top: 1rem;">
                <div v-for="(item, index) in liveData_" :key="index" class="live-item flex-center-container">
                    <figure>
                        <img v-if="item.avatar !== ''" :src="item.avatar" style="margin-right: 2rem;" />
                        <img v-else src="require('@/assets/img/avater.png')" style="margin-right: 2rem;" />
                        <img src="@/assets/img/live/on-live.gif"
                            style="position: absolute; width: 3rem; height: 1rem;" />
                    </figure>
                    <div class="flex-column-left-max-container">
                        <p class="up-name" style="font-size: 1.1rem;">{{ item.upName }}</p>
                        <p>{{ item.liveName }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useUserInfo } from "@/store/userInfo"
import { fetchUserInfo } from "@/api/user"
import liveData from '@/assets/data/liveData'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const loginUser = ref({}) // 完整登录用户数据  
const liveData_ = ref([]) // 直播中的信息
// 关注数、粉丝数和动态数
const userDetailInfo = [{
    name: "关注数",
    num: 7
}, {
    name: "粉丝数",
    num: 12
}, {
    name: "动态数",
    num: 3
}]
// 获取用户信息
onMounted(async () => {
    loginUser.value = await fetchUserInfo(userId, userId)
    console.log("我的用户呢", loginUser.value)
    liveData_.value = liveData["data"]
})
</script>

<style lang="scss" scoped>
@import url("@/assets/css/trend.scss");
$left-width: 18rem;
$here-font-color: #0f4377;

.login-user-info {
    width: $left-width;
    height: 8rem;
    padding: 1rem;
}

.trend-avatar {
    border-radius: 50%;
    width: 3rem;
    height: 3rem;
}

.up-name {
    font-weight: 600;
    font-size: 1.3rem;
}

.live-wrap {
    width: $left-width;
    padding: 1rem;
    min-height: 5rem;
    height: auto;

    .live-wrap-title {
        font-weight: 700;
        font-size: 1.5rem;
        color: $here-font-color;
    }

    .live-item {
        min-height: 4rem;
        height: auto;
        width: $left-width - 2rem;
        align-items: stretch;
    }
}

.detail-num-wrap {
    margin: 0.5rem 1.5rem;

    p {
        font-weight: 600;
        font-size: 1rem;
        color: $here-font-color;

        &:first-child {
            margin-bottom: 0.5rem;
        }
    }
}
</style>