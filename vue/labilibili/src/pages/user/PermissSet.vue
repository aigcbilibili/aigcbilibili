<template>
    <!--权限管理-->
    <!--权限：boolean true公开，false私有-->
    <div class="user-content-wrap">
        <div class="user-content-title permiss-title">权限设置</div>
        <div class="permiss-wrap flex-column-center-container">
            <div v-for="(item, index) in upPermiss" :key="index" class="permiss-item flex-based-container">
                <p style="margin-right: 2rem; color: #7b42b0;">{{ item.name }}</p>
                <div class="user-content-slide-btn">
                    <p class="btn-based" :class="{ 'btn-chose': !item.value }" @click="item.value = false">允许</p>
                    <p class="btn-based" :class="{ 'btn-chose': item.value }" @click="item.value = true">不允许</p>
                </div>
            </div>
        </div>
        <el-button type="primary" @click="handlerChange">保存</el-button>
    </div>
</template>

<script setup>
import { onMounted, onBeforeMount, reactive } from "vue"
import { fetchPermiss, editPermiss } from "@/api/permiss"
import { useUserInfo } from "@/store/userInfo"
import { onUnmounted } from "vue";
const isEditPermiss = reactive(false) // 是否有改动权限
const userInfo = useUserInfo()
// 权限配置
const upPermiss = reactive([{
    name: "允许其他用户查看关注列表",
    label: 'idolList',
    value: false,
}, {
    name: "允许其他用户查看粉丝列表",
    label: 'fansList',
    value: false,
}, {
    name: "允许其他用户查看收藏夹",
    label: 'collectGroup',
    value: false,
}, {
    name: "允许其他用户查看最近点赞",
    label: "remotelyLike",
    value: false,

}])




onMounted(() => {
    // 获取权限数据
    fetchPermiss(userInfo.id).then((res) => {
        // console.log('reeee',res);
        upPermiss.forEach((item) => {
            item.value = !!res[item.label]
        })
    })
})
onUnmounted(() => {


})
const handlerChange = () => {
    editPermiss({
        userId: userInfo.id,
        idolList: Number(upPermiss[0].value),
        fansList: Number(upPermiss[1].value),
        collectGroup: Number(upPermiss[2].value),
        remotelyLike: Number(upPermiss[3].value),
    })
}

</script>

<style lang="scss" scoped>
@import "@/assets/css/userPage.scss";

.permiss-title {
    margin-top: 2rem !important;
    color: #5d1d8b;
}

.permiss-wrap {
    margin: 3rem;

    .permiss-item {
        height: 5rem;

        p:first-child {
            width: 6rem;
        }
    }
}

@media screen and (min-width: 820px) {
    .permiss-wrap {
        flex-flow: row wrap !important;

        .permiss-item {
            margin: 1.5rem 1rem;
        }
    }
}

@media screen and (min-width: 1020px) {
    .permiss-wrap {
        .permiss-item {
            margin: 1.5rem 4rem;
        }
    }
}

@media screen and (min-width:1200px) {
    .permiss-wrap {
        .permiss-item {
            margin: 1.5rem 5rem;
        }
    }
}
</style>