<!--手机号登录-->
<template>
    <el-form :model="param" ref="login" label-width="0px" class="login-form">
        <div class="login-account">
            <!--TODO：待删-->
            <p style="margin-top: -1rem; margin-bottom: 1rem;">* 未注册用户登陆后将会自动注册</p>
            <el-form-item prop="username">
                <el-input class="login-input" v-model="param.phoneNumber" placeholder="请输入中国地区的手机号">
                    <template #prepend>
                        <el-button icon="Phone" class="login-icon"
                            style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
                    </template>
                </el-input>
            </el-form-item>
            <div style="display: flex; justify-content: space-between;">
                <el-input v-model="param.captcha" style="width: 18rem;" placeholder="请输入验证码"></el-input>
                <el-button type="primary" class="basic-btn get-message-captcha common-based-btn"
                    @click="updateCaptcha()">验证码</el-button>
            </div>
            <div class="login-area">
                <el-button type="primary" class="basic-btn login-btn common-based-btn" @click="phoneLogin()">登
                    录</el-button>
            </div>
        </div>
    </el-form>
</template>

<script setup>
import { reactive } from 'vue'
import { getPhoneCaptcha, sendPhoneLogin } from '@/api/login'
import { useRouter } from 'vue-router'
import { isMeetReg, setUser } from './loginCommon'
import Debounce from '@/static/debounce'
const captchaErrorStr = '短信验证码格式错误！'
const debounce = new Debounce() // 防抖
const router = useRouter()
const param = reactive({
    phoneNumber: '',
    captcha: ''
})
const updateCaptcha = async () => {
    if (isMeetReg(0, param.phoneNumber, "")) {
        await getPhoneCaptcha(param.phoneNumber)
    }
}
const phoneLogin = async () => {
    if (isMeetReg(0, param.phoneNumber, "") && isMeetReg(1, param.captcha, captchaErrorStr)) {
        await debounce.debounceEnd(5)
        const loginRes = await sendPhoneLogin(param.phoneNumber, param.captcha)
        let val = await setUser(loginRes)
        if (val) {
            router.push('/') // 跳转到首页
        }
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
</style>