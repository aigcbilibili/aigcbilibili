<!--手机号登录-->
<template>
    <el-form :model="param" ref="login" label-width="0px" class="login-form">
        <div class="login-account">
        <el-form-item prop="username">
            <el-input class="login-input" v-model="param.phoneNumber" placeholder="请输入中国地区的手机号">
                <template #prepend>
                    <el-button icon="Phone" class="login-icon" style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
                </template>
            </el-input>
        </el-form-item>
        <div style="display: flex; justify-content: space-between;">
            <el-input v-model="param.captcha" style="width: 18rem;" placeholder="请输入验证码"></el-input>
            <el-button type="primary" class="basic-btn get-message-captcha common-based-btn" @click="updateCaptcha()">验证码</el-button>
        </div>
        <div class="login-area">
            <el-button type="primary" class="basic-btn login-btn common-based-btn" @click="phoneLogin()">登 录</el-button>
        </div>
        </div>
    </el-form>
</template>

<script setup>
import { reactive } from 'vue'
import { getPhoneCaptcha, sendPhoneLogin } from '@/api/login'
import { ElMessage } from 'element-plus'
const param = reactive({
    phoneNumber: '',
    captcha: ''
})
const phoneRegStr = /^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}$/
const captchaRegStr = /^\d{6}$/
// 测试是否满足手机号正则表达式
const isPhoneMeetReg = (phoneNumber) => {
    // 验证手机号是否满足
    if(phoneRegStr.test(phoneNumber) && phoneNumber.length === 11){
        return true
    } else {
        ElMessage.error("手机号不满足大陆地区的规格！")
        return false
    }
}
// 验证码是否满足规格
const isCaptchaMeetReg = (captcha) => {
    if(captchaRegStr.test(captcha)) {
        return true
    } else {
        ElMessage.error("验证码格式错误！")
        return false
    }
}
const updateCaptcha = () => {
    if(isPhoneMeetReg(param.phoneNumber)) {
        getPhoneCaptcha(param.phoneNumber)
    } 
}
const phoneLogin = () => {
    if(isPhoneMeetReg(param.phoneNumber) && isCaptchaMeetReg(param.captcha)) {
        sendPhoneLogin(param.phoneNumber, param.captcha)
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
.login-form {
    padding-top: 1rem;
}
.login-area {
    margin-top: 4rem;
}
</style>