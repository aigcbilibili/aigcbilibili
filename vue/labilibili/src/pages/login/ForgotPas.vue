<template>
    <el-dialog v-model="isShow" :show-close="true" :before-close="handleClose" class="dialog flex-column-between-container 
    self-center-box-second" style="
      width:40rem; 
      height:40rem;
      background-color: #fff !important;
      box-shadow: -5px 2px 25px 0 #79b1eca9 !important;
      border-radius: 10px !important;
      position: fixed;
      overflow: visible;
      z-index: 10;
    " append-to-body>
        <template #header>
            <p class="title">忘记密码</p>
        </template>
        <template #default>
            <!--注册表格-->
            <div class="flex-column-left-max-container">
                <div v-for="(item, index) in enrollInfo" :key="index" class="input-box flex-based-container">
                    <p class="input-name">{{ item.name }}</p>
                    <el-input class="login-input input-content" v-model="enrollInfo[index].data"
                        :placeholder="item.placeholder"></el-input>

                </div>
            </div>
        </template>
        <template #footer>
            <el-button type="primary" class="basic-btn common-based-btn 
            get-message-captcha" @click="enrollConfirm()">提交</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch, onBeforeUnmount, inject } from 'vue'
import { enroll, forget } from '@/api/login'
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from 'element-plus'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
// 注册的信息说明
const isEnrollProcess = inject('isEnrollVal')
const enrollStatusFlag = inject('enrollStatus')
// 获取到注册信息
const enrollEmitValue = inject('enrollData')
const emits = defineEmits('update:enrollValue')
const isShow = ref(false)
const enrollInfo = ref([{
    name: "用户名",
    placeholder: "请输入含字母/符号或汉字的2-10个字符",
    data: "",
    required: true
}, {
    name: "密码",
    placeholder: "请输入含大小写字母、符号和数字的6-16个字符",
    data: "",
    required: true
}, {
    name: "确认密码",
    placeholder: "请重新输入密码",
    data: "",
    required: true
},])
// 

// 注册确认：enrollData外除了
const enrollConfirm = async () => {
    // 注册
    const res = await forget(
        enrollInfo.value[0].data,
        enrollInfo.value[1].data,
        enrollInfo.value[2].data,
    )
    console.log(res);
    if (res) {
        // 带着信息进入首页
        ElMessage.success("重制成功")
        const enrollValueData = {
            username: enrollInfo.value[0].data,
            password: enrollInfo.value[2].data,
            userId: res.data
        }
        enrollEmitValue.setEnrollData(enrollValueData)
        userInfo.setId(res.data)
        isShow.value = false
        for (let i of enrollInfo.value) {
            i.data = ""
        }
        return
    }
}
// 删除关闭
const handleClose = (done) => {
    isShow.value = false
    isEnrollProcess.setIsEnroll(false)
    done()
}
onBeforeUnmount(() => {
    // emits('update:enrollConfirm', false)
})
watch(isEnrollProcess.isEnroll, (newValue) => {
    isShow.value = newValue
})
const handerOpen = () => {
    isShow.value = true
    for (let i of enrollInfo.value) {
        i.data = ""
    }
}
defineExpose({
    handerOpen
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
$here-height: 1.2rem;

.title {
    color: #000;
    font-size: 1.7rem;
    font-style: normal;
    font-weight: 600;
    line-height: normal;
}

.input-box {
    margin-bottom: 1rem;
    height: 3rem;

    .input-name {
        width: 7rem;
        margin-right: 1rem;
        text-align: left;
        font-size: $here-height;
    }

    .input-content {
        height: $here-height+1rem;
    }
}

.input-box ::v-deep .el-input__inner {
    font-size: 1rem;
}

.enroll-message-captcha {
    height: 2.4rem !important;
    margin-left: 2rem;
}
</style>