<!--关注的关系列表-->
<template>
    <div class="follow-wrap">
        <el-dialog v-model="isShow" :show-close="false" style="
                width: 30rem;
                height: 40rem;
                background-color: #fff !important;
                box-shadow: -5px 2px 25px 0 rgb(200 186 210 / 60%) !important;
                border-radius: 10px !important;
            ">
            <template #header>
                <p class="title">{{ followTitle }}</p>
            </template>
            <template #default>
                <el-scrollbar style="height: 98%;">
                    <div v-for="(item, index) in data" :key="index" style="cursor: pointer;"
                        @click="turnToUserCenter(item.userId)">
                        <div class="rel-user-item flex-left-container">
                            <img :src="item.cover" class="first-common-avatar" style="margin-right: 1rem;" />
                            <div class="flex-column-left-max-container">
                                <p style="font-weight: 600;">{{ item.nickname }}</p>
                                <div class="flex-based-container">
                                    <p style="margin-right: 1rem;">关注数 {{ item.fansCount }}</p>
                                    <p style="margin-right: 1rem;">粉丝数 {{ item.idolCount }}</p>
                                </div>
                            </div>
                            <div class="flex-center-container">
                                <followAndMessage class="follow-message-wrap" :userInfo="item" />
                            </div>
                        </div>
                    </div>
                </el-scrollbar>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { fetchUserInfo, fetchFollowingsList, fetchFollowersList } from "@/api/user"
import { useRouter } from 'vue-router'
import followAndMessage from "./FollowAndMessage.vue"
const router = useRouter()
const data = ref([]) // 待渲染的数据
// 转到新个人中心
const turnToUserCenter = (upId) => {
    const routeURL = router.resolve({
        path: `/userCenter/myItem/${upId}`
    })
    window.open(routeURL.href, '_blank')
}
// 关注者或粉丝的类型
const props = defineProps({
    followType: { // 1为关注列表，2粉丝
        type: Number,
        required: true,
        default: 0
    },
    upId: {
        type: Number,
        required: true,
        default: 1
    }
})
const upId = props.upId // 该用户的id
const isShow = ref(false)
const followTitle = props.followType === 1 ? "关注列表" : "粉丝列表"



watch(() => props.followType, (value) => {
    console.log("followType", value);
    if (value === 1) {
        isShow.value = true
        let fansListResponse = JSON.parse(localStorage.getItem('fansListResponse'));
        data.value = fansListResponse
    } else if (value === 2) {
        isShow.value = true
        let idolListResponse = JSON.parse(localStorage.getItem('idolListResponse'));
        data.value = idolListResponse
    }
})


</script>

<style lang="scss">
.el-dialog__header {
    margin-right: 0 !important;
}
</style>

<style lang="scss" scoped>
.follow-close {
    width: 3rem;
    height: 3rem;
    background: #fff;
    z-index: 99;
}

.title {
    color: #9e44b1;
    font-size: 1.4rem;
    font-weight: 600;
}

.rel-user-item {
    height: 4rem;
    position: relative;
}

.follow-message-wrap {
    margin-left: 2rem;
}

::v-deep .user-info-interact {
    right: 0 !important;
}

@media screen and (min-width: 1020px) {
    ::v-deep .user-info-interact {
        right: 0 !important;

        .user-interact-btn:first-child {
            margin-right: 0 !important;
        }
    }
}
</style>