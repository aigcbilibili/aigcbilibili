<!--账号密码登录-->
<template>
    <!--model：绑定时用的数据，ref：提交时用的数据-->
    <el-form :model="param" :rules="rules" ref="login" label-width="0px">
        <div class="login-account">
        <el-form-item prop="username">
            <el-input class="login-input" v-model="param.username" placeholder="username">
                <template #prepend>
                    <el-button icon="User" class="login-icon" style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
                </template>
            </el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input
                type="password"
                placeholder="password"
                class="login-input"
                v-model="param.password"
                @keyup.enter="submitForm(login)"
            >
                <template #prepend>
                    <el-button icon="Lock" class="login-icon" style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
                </template>
            </el-input>
        </el-form-item>
        <div style="display: flex; justify-content: space-between;">
            <el-input v-model="_captcha" style="width: 13rem;" placeholder="请输入验证码"></el-input>
            <el-image class="captcha-area" :src="captcha" @click="updateCaptcha()"></el-image>
        </div>
        <div class="login-area">
            <el-button type="primary" class="basic-btn login-btn common-based-btn" @click="isEnroll=true">注 册</el-button>
            <el-button type="primary" class="basic-btn login-btn common-based-btn" @click="submitForm(login)">登 录</el-button>
        </div>
        </div>
    </el-form>
    <!--注册弹窗-->
    <enrollCard v-if="isEnroll" v-model:enrollConfirm="isEnroll"  style="width:40rem; height:35rem;" />
</template>

<script setup>
import { ref, onMounted, reactive, defineAsyncComponent } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCaptcha, verifyLogin } from '@/api/login'
import { useUserInfo } from '@/store/userInfo'
import Debounce from '@/static/debounce'
import Throttle from '@/static/throttle'
const isEnroll = ref(false) // 选择注册
const userInfo = useUserInfo() // 保存登录信息
let captcha = ref('') 
const enrollCard = defineAsyncComponent(()=>
	import ('./EnRoll')
)	
/**
 * 登录逻辑实现
 *  */ 
const updateCaptcha = async()=>{
	try{
		captcha.value = await getCaptcha() 
	}catch(e){
		console.log('验证码错误',e)
	}finally{ // 不管是否异常，都会执行finally的
	}
}
const throttle = new Throttle(updateCaptcha, 100)
window.addEventListener("getCaptcha", throttle) 

// 确认登录
const login = ref('').value
const param = reactive({
	username: 'admin',
	password: '123456'
})
const _captcha = ref('') // 验证码输入的值
const router = useRouter()
const debounce = new Debounce() // 防抖

const submitForm = async formEL => {
	// 表单检验
	if(!formEL) return

	await debounce.debounceEnd(5)

	// 为了快速通过本页面，使用admin + 123456
	if(param.username==='admin' && param.password==='123456'){
		userInfo.setId(1)
		router.push('/')
		return
	}	

	// 发送请求到后端，获取响应数据
	const _captcha_ = window.JSON.stringify(_captcha.value)
	const username_ = window.JSON.stringify(param.username)
	const password_ = window.JSON.stringify(param.password)
	const loginRes = await verifyLogin(username_, password_, _captcha_)
    const userId = loginRes.userId
	if(userId>0 && typeof userId==='number'){
		ElMessage.success('登录成功')
		// 保存本地信息
		userInfo.setId(userId) 
		// 跳转到首页
		router.push('/')
	}else{
		ElMessage.error('登录失败，请检查用户名或密码')
		updateCaptcha()
		return false
	}
}
const rules = {
	username:[{
		required: true,
		message: '请输入用户名',
		trigger: 'blur' 
	}],
	password:[{
		required: true,
		message: '请输入密码',
		trigger: 'blur' 
	}]
}
onMounted(()=>{
	updateCaptcha()
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
$small-border-radius: 10px;
.login-account{
	margin-top: 2.5rem;
}
.captcha-area{
	margin-bottom: -1rem;
	border-radius: $small-border-radius;
	width: 10rem;
	height: 3rem;
}
.login-account ::v-deep(.el-form-item__error){
	margin-top: -0.6rem;
	font-size: 1rem;
	font-weight: 400;
}
/* 样式穿透，修改elementUI */
.login-account > .login-area{
	margin-top: 3rem;
	margin-bottom: 2.8rem;
	width: 100%;
	display: flex;
	flex-flow: row wrap;
	justify-content: center;
}
.login-area .login-btn:first-child{
	margin-right: 20%;
}
</style>