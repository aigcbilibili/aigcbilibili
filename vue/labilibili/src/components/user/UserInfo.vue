<!--本页面用于个人详情页、搜索结果页的用户信息-->
<template>
    <div class="based-box user-info" :class="{ 'user-info-bg': isBgShow }">
        <div class="user-info-important user-info-item flex-left-container">
            <img v-if="!isEditInfo" :src="upInfo.avatar" class="common-avatar user-center-avatar" />
            <figure v-else class="change-color-btn" style="position: relative;">
                <img src="@/assets/img/user/black_user.jpeg" type="image" style="border: 1px solid #6b6b6b;"
                    class="common-avatar user-center-avatar" />
                <input type="file"
                    style="opacity: 0; position: absolute; z-index: 99999;top: 0;left: 0;width: 100%;height: 100%;"
                    @input="handleFileChange">
                <figcaption>上传头像</figcaption>
            </figure>
            <div class=" flex-column-left-max-container user-info-text">
                <p v-if="!isEditInfo" style="font-weight: 600; font-size: 1.2rem;">{{ upInfo.name }}</p>
                <input v-else :placeholder="'请输入名称'" class="modified-input-name" />
                <div class="flex-based-container">
                    <p v-if="upInfo.followingNum" style="margin-right: 1rem;" class="change-color-btn"
                        @click="isFollow = 1">关注数 {{ upInfo.followingNum }}</p>
                    <p v-if="upInfo.followersNum" style="margin-right: 1rem;" class="change-color-btn"
                        @click="isFollow = 2">粉丝数 {{ upInfo.followersNum }}</p>
                    <p v-if="upInfo.playNum" style="margin-right: 1rem;">播放量 {{ upInfo.playNum }}</p>
                    <p v-if="upInfo.likeNum" style="margin-right: 1rem;">点赞量 {{ upInfo.likeNum }}</p>
                </div>
                <el-tooltip v-if="!isEditInfo" effect="light" placement="bottom" popper-class="user-all-intro">
                    <template #content>{{ upInfo.intro }}</template>
                    <p class="long-text-collapsed user-simple-intro">{{ upInfo.intro }}</p>
                </el-tooltip>
                <input v-else :placeholder="'请输入修改后的简介'" class="modified-input-intro" />
            </div>
        </div>
        <div v-if="isConfigShow && isBgShow" class="user-tool user-info-item">
            <!-- <img src="@/assets/img/user/config.svg" @click="isEditInfo = ~isEditInfo" id="test-avatar"
                class="user-info-config common-based-btn" style="margin-right: 1rem; width: 1.6rem; height: 1.6rem;" /> -->
            <img src="@/assets/img/user/changeBg.svg" style="width: 1.6rem; height: 1.6rem;" />
        </div>
        <div class="flex-between-container">
            <el-button type="primary" class="save-btn" @click="uploadFinal()" v-if="isConfigShow && isBgShow">{{
                isEditInfo ?
                    '上传' :
                    '修改信息'
            }}</el-button>
            <followAndMessage :isFollowing="upInfo2.isFollowing" :upId="upId" :userInfo='upInfo2' />
        </div>
        <!--上传头像的页面-->
        <div v-if="isuploadImg" class="">
            <input id="upload-img" type="file" accept=".png, ,jpg" style="display: none" @input="handleFileChange">
        </div>
        <!--粉丝和关注列表-->
        <followVue :followType="isFollow" :upId="upId" />
    </div>
</template>

<script setup>
import { onMounted, defineProps, defineAsyncComponent, ref, watch } from 'vue'
import { fetchUserInfo } from '@/api/user'
import { fetchPermiss } from "@/api/permiss"
import { useUserInfo } from "@/store/userInfo"
import followAndMessage from "./FollowAndMessage.vue"
import aInput from '@/components/public/aInput.vue'
import axios from "axios"
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const isConfigShow = ref(true) // 是否能修改用户信息
const isEditInfo = ref(false) // 是否修改了用户信息
const upId = ref(0) // 查看的用户id：不能用const upId = currentURL.match(/\/(\d+)$/)[1] 获取当前个人页面的Id，因为只适配个人主页
const isFollow = ref(0) // 是否能查看关注和粉丝列表，0是否，1是关注，2是粉丝
const followVue = defineAsyncComponent(() =>
    import("@/components/user/FollowerRel.vue")
)
const editData = new FormData() // 待上传数据
editData.append("userId", userId)
// 用户数据
// const defaultUpInfo = {
//     id: 1,
//     name: "默认用户",
//     avatar: require("@/assets/img/avater.png"),
//     intro: "个人的主页默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据",
//     followingNum: 0,
//     followersNum: 999,
//     playNum: 789,
//     likeNum: 246,
//     isFollowing: false,
// }
const upInfo = ref({})

const upInfo2 = ref({})
// 能否设置
const props = defineProps({
    isBgShow: {
        type: Boolean,
        required: false,
        default: false
    },
    upId: {
        type: Number,
        required: true,
        default: false
    },
    upInfo: {
        type: Object,
        required: false,
        default: null
    },


})
const isBgShow = props.isBgShow
const handleFileChange = (e) => {
    // 数据结构
    const fileAvatar = e.target.files[0]
    if (fileAvatar) {
        editData.get('file') && editData.delete('file')
        editData.append("file", fileAvatar)
    }
}
const uploadFinal = () => {

    if (!isEditInfo.value) {

        isEditInfo.value = true

    } else {
        let nickname = document.getElementsByClassName("modified-input-name")[0].value;
        let intro = document.getElementsByClassName("modified-input-intro")[0].value;
        if (!nickname || !intro) { return isEditInfo.value = false }
        editData.get('intro') && editData.delete('intro')
        editData.get('nickname') && editData.delete('nickname')
        editData.append("intro", intro)
        editData.append("nickname", nickname)
        // 上传后端
        const postURL = '/api/userInfo/editUserInfo'

        axios.post(postURL, editData, {
            headers: {
                'Content-Type': 'application/multipart/form-data'
            }
        }).then(async (res) => {
            console.log("详细结果", res)
            console.log("获取数据", res.data.data)
            upInfo.value = await fetchUserInfo(userId, upId.value)

        }).catch(err => {
            console.log("错误信息", err)
        })
        isEditInfo.value = false
    }

}
const isuploadImg = ref(false)

onMounted(async () => {

    upId.value = props.upId
    // 如果不是本人使用，关闭权限
    if (userId != upId.value) {
        isConfigShow.value = false
        // 验证权限
    }


    // 如果有传入个人信息
    if (props.upInfo) {
        upInfo.value = props.upInfo
        console.log(`${JSON.stringify(props.upInfo)}\nuserInfo接收数据:${JSON.stringify(upInfo.value)}`)
    } else {
        upInfo.value = await fetchUserInfo(userId, upId.value)
        console.log('4444444', upInfo.value);
        upInfo2.value = {
            cover: upInfo.value.avatar,
            nickname: upInfo.value.name,
            userId: upInfo.value.id,
            isFollowing: upInfo.value.isFollowing,

        }
    }
    console.log(`验证关注情况${upInfo.value.isFollowing}`)
})
</script>

<style lang="scss">
.user-all-intro {
    max-width: 35rem;
    width: auto;
}

@media screen and (min-width: 1020px) {
    .user-all-intro {
        max-width: 65rem;
    }
}
</style>

<style lang="scss">
$user-box-first-color: rgb(161, 150, 235);
$user-box-second-color: rgb(192, 183, 249);
$user-box-padding-left-right: 2rem;

.user-info {
    position: relative;
}

.user-info-bg {
    background: url("@/assets/img/user/user-bg.jpg") no-repeat center center/cover;
}

.user-info-item {
    position: absolute;
}

.user-info-important {
    top: 0.5rem;
    left: $user-box-padding-left-right;
    height: 6.5rem;
    width: 37rem;

    .user-center-avatar {
        width: 5rem;
        height: 5rem;
    }

    .user-info-text {
        width: 30rem;
        margin-left: 1rem;
    }

    .user-simple-intro {
        margin-top: 0.5rem;
        cursor: pointer;
    }
}

.user-tool {
    right: 1rem;
    top: 1rem;

    .user-info-config {
        &:hover {
            animation: rotate 5s linear infinite;
        }
    }
}

.modified-input ::v-deep el-input__wrapper {
    font-weight: 600;
    font-size: 1.2rem;
    width: 10rem;
    height: 0.9rem;
}

.save-btn {
    position: absolute;
    left: 2.5rem;
    bottom: 1rem;
    border-radius: 20px !important;
    border: none !important;
    padding: 1.1rem !important;
    background-color: rgb(206 165 217) !important;

    &:hover,
    &:active {
        background-color: rgb(161, 150, 235) !important;
    }
}

@media screen and (min-width: 1020px) {
    .user-info-important {
        left: $user-box-padding-left-right+1rem;
        width: 72rem;

        .user-info-text {
            width: 65rem;
            margin-left: 2rem;
        }
    }

    .save-btn {
        left: 3.5rem;
    }
}
</style>