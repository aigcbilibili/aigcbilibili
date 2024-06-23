<!--微信登录-->
<template>
    <el-form :model="param" ref="login" label-width="0px" class="login-form">
        <div class="login-account">
            <el-form-item prop="username">
                <el-input class="login-input" v-model="param.mailNumber" placeholder="请输入邮箱">
                    <template #prepend>
                        <el-button icon="Message" class="login-icon"
                            style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
                    </template>
                </el-input>
            </el-form-item>
            <div style="display: flex; justify-content: space-between;">
                <el-input v-model="param.captcha" style="width: 18rem;" placeholder="请输入验证码"></el-input>
                <el-button type="primary" class="basic-btn get-message-captcha common-based-btn"
                    @click="getCaptcha()">验证码</el-button>
            </div>
            <div class="login-area">
                <el-button type="primary" class="basic-btn login-btn common-based-btn" @click="mailLogin()">登
                    录</el-button>
            </div>
        </div>
    </el-form>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { reactive } from "vue"
import { emailCaptcha, emailLogin } from "@/api/login"
import { isMeetReg, setUser } from "./loginCommon"
import Debounce from '@/static/debounce'
const router = useRouter()
const debounce = new Debounce() // 防抖
const param = reactive({
    mailNumber: '',
    captcha: ''
})
const emailErrorStr = "邮箱格式错误"
const captchaErrorStr = "邮箱验证码格式错误"
const getCaptcha = async () => {
    if (isMeetReg(5, param.mailNumber, emailErrorStr)) {
        await emailCaptcha(param.mailNumber)
    }
}
const mailLogin = async () => {
    await debounce.debounceEnd(5)
    isMeetReg(5, param.mailNumber, emailErrorStr)
    isMeetReg(6, param.captcha, captchaErrorStr)

    // 邮箱登录
    const loginRes = await emailLogin(param.mailNumber, param.captcha)
    if (f(loginRes)) {
        router.push('/') // 跳转到首页
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
</style>