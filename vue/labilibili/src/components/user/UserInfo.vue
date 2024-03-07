<!--本页面用于个人详情页、搜索结果页的用户信息-->
<template>
    <div class="based-box user-info" :class="{'user-info-bg':isBgShow}">
        <div class="user-info-important user-info-item flex-left-container">
            <img v-if="!isEditInfo" :src="upInfo.avatar" class="common-avatar user-center-avatar" />
            <figure v-else class="change-color-btn">
                <img src="@/assets/img/user/black_user.jpeg" type="image" style="border: 1px solid #6b6b6b;"
                class="common-avatar user-center-avatar" @click="uploadAvatar()" />
                <figcaption @click="uploadAvatar()">上传头像</figcaption>
            </figure>
            <div class=" flex-column-left-max-container user-info-text">
                <p v-if="!isEditInfo" style="font-weight: 600; font-size: 1.2rem;">{{upInfo.name}}</p>
                <aInput v-else :definedPlaceholder="'请输入名称'" class="modified-input"></aInput>
                <div class="flex-based-container">
                    <p style="margin-right: 1rem;" class="change-color-btn" @click="isFollow=1">关注数 {{upInfo.followingNum}}</p>
                    <p style="margin-right: 1rem;" class="change-color-btn" @click="isFollow=2">粉丝数 {{upInfo.followersNum}}</p>
                    <p style="margin-right: 1rem;">播放量 {{upInfo.playNum}}</p>
                    <p style="margin-right: 1rem;">点赞量 {{upInfo.likeNum}}</p>
                </div>
                <el-tooltip v-if="!isEditInfo" effect="light" placement="bottom" popper-class="user-all-intro">
                    <template #content>{{upInfo.intro}}</template>
                    <p class="long-text-collapsed user-simple-intro">{{upInfo.intro}}</p>
                </el-tooltip>
                <aInput v-else :placeholder="'请输入修改后的简介'" style="font-weight: 600; font-size: 1.2rem;"></aInput>
            </div>
        </div>
        <div v-if="isConfigShow" class="user-tool user-info-item">
            <img src="@/assets/img/user/config.svg" @click="isEditInfo=~isEditInfo" id="test-avatar"
            class="user-info-config common-based-btn" style="margin-right: 1rem; width: 1.6rem; height: 1.6rem;" />
            <img src="@/assets/img/user/changeBg.svg" style="width: 1.6rem; height: 1.6rem;" />
        </div>
        <div class="flex-between-container">
            <el-button v-if="isEditInfo" type="primary" class="save-btn" @click="uploadFinal()">上传</el-button>
            <followAndMessage />
        </div>
        <!--上传头像的页面-->
        <div v-if="isuploadImg" class="" >
            <input id="upload-img" type="file" accept=".png, ,jpg" style="display: none" @input="handleFileChange">
        </div>
        <!--粉丝和关注列表-->
        <followVue v-if="isFollow!=0" v-model:followType="isFollow" :upId="upInfo.id"  @update:follower-relationship="updateFollowType" />
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
const currentURL = window.location.href // 获取当前页面的URL
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const upId = currentURL.match(/\/(\d+)$/)[1] // 获取当前个人页面的Id
const isConfigShow = ref(true) // 是否能修改用户信息
const isEditInfo = ref(false) // 是否修改了用户信息
const isFollow = ref(0) // 是否能查看关注和粉丝列表，0是否，1是关注，2是粉丝
const followVue = defineAsyncComponent(()=>
    import ("@/components/user/FollowerRel.vue")
)
const editData = new FormData() // 待上传数据
// 用户数据
const defaultUpInfo = {
  id: 1,
  name: "默认用户",
  avatar: require("@/assets/img/avater.png"),
  intro: "个人的主页默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据默认数据",
  followingNum: 0,
  fansNum: 333,
  followersNum: 999,
  playNum: 789,
  likeNum: 246,
  isFollowing: false, 
}
const upInfo = ref(defaultUpInfo)
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
    }
})
const isBgShow = props.isBgShow
// 刷新数据
const getUserData = async() => { // 用户数据

}
const handleFileChange = (e) => {
    // 数据结构
    const fileAvatar = e.target.files[0]
    if(fileAvatar) {
        editData.append("coverImg", fileAvatar)
    }
}
const uploadFinal = () => {
    editData.append("id", userId)
    editData.append("intro", "测试333号")
    editData.append("nickName", "nihao")
    // 上传后端
    const postURL = 'http://labilibili.com/userInfo/editUserInfo'
    axios.post(postURL, editData, {
        headers: {
            'Content-Type': 'application/multipart/form-data'
        }
    }).then(res=> {
        console.log("详细结果", res)
        console.log("获取数据", res.data.data)
    }).catch(err=>{
        console.log("错误信息", err)
    })
}
// 修改数据
const uploadAvatar = () => {
    isuploadImg.value = true
    let imgInput = document.getElementById("upload-img")
    imgInput.click()
}
onMounted(async()=>{
    // 如果不是本人使用，关闭权限
    if(userId!=upId) {
        isConfigShow.value = false
        // 验证权限
        
    }
    upInfo.value = await fetchUserInfo(userId, upId)
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
    &:hover, &:active{
        background-color: rgb(161, 150, 235) !important;
    }
}
@media screen and (min-width: 1020px){
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