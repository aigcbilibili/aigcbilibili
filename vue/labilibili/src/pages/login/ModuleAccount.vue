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
				<el-input type="password" placeholder="password" class="login-input" v-model="param.password"
					@keyup.enter="submitForm()">
					<template #prepend>
						<el-button icon="Lock" class="login-icon" style="margin-top: -0.3rem; color: #7ec3fb;"></el-button>
					</template>
				</el-input>
			</el-form-item>
			<div style="display: flex; justify-content: space-between;">
				<el-input v-model="_captcha" style="width: 13rem;" class="login-input" placeholder="请输入验证码"></el-input>
				<el-image class="captcha-area" :src="captcha" @click="updateCaptcha()"></el-image>
			</div>
			<div class="login-area">
				<el-button type="primary" class="basic-btn login-btn common-based-btn" @click="TrueEnroll()">注
					册</el-button>
				<el-button type="primary" class="basic-btn login-btn common-based-btn" @click="forget">忘记密码</el-button>
				<el-button type="primary" class="basic-btn login-btn common-based-btn" @click="submitForm()">登
					录</el-button>
			</div>
		</div>
	</el-form>
	<!--注册弹窗-->
	<enrollCard /><!--v-model:enrollConfirm=XXX-->
	<ForgotPas ref="forgotPas" />

</template>

<script setup>
import {
	ref, onMounted, reactive, inject,
	defineAsyncComponent, watch, provide
} from 'vue'
import { isMeetReg, setUser } from "./loginCommon"
import { useRouter } from 'vue-router'
import { getCaptcha, verifyLogin } from '@/api/login'
import { useUserInfo } from '@/store/userInfo'
import { ElMessage } from "element-plus"
import Debounce from '@/static/debounce'
import Throttle from '@/static/throttle'
import ForgotPas from './ForgotPas.vue'
let captcha = ref('')
// const isEnrollProcess = inject('isEnrollVal') // 选择注册
const isEnrollUpdate = ref(false) // 注册转递结果
// const enrollEmitValue = inject('enrollData') // 注册信息
// const enrollEmitValueData = ref(enrollEmitValue.getEnrollData())
const forgotPas = ref(null)
const userInfo = useUserInfo() // 保存登录信息
const _captcha = ref('') // 验证码输入的值
const router = useRouter()
const debounce = new Debounce() // 防抖
const captchaErrorStr = "账号验证码格式错误"
const emailErrorStr = "账号格式错误"
const isEnroll = ref(false) // 选择注册
const enrollCard = defineAsyncComponent(() =>
	import('./EnRoll')
)
// 注册状态的通信
provide('isEnrollVal', {
	isEnroll,
	setIsEnroll(val) {
		isEnroll.value = val
	},
	getIsEnroll() {
		return isEnroll.value
	}
})
const forget = () => {
	forgotPas.value.handerOpen()
}

// 注册信息
const enrollData = ref({
	username: '',
	password: '',
	userId: 1
})
// 注册信息获取
provide('enrollData', {
	enrollData,
	setEnrollData(val) {
		enrollData.value = val
	},
	getEnrollData() {
		return enrollData.value
	}
})
provide('enrollStatus', {
	upDateEnrollStatus(val) { // 是否在登录页完成
		isEnrollUpdate.value = val
	},
	getEnrollStatus() {
		return isEnrollUpdate.value
	}
})
/**
 * 登录逻辑实现
 *  */
// 获取验证码
const updateCaptcha = async () => { // 更新验证码
	try {
		captcha.value = await getCaptcha() // 图片captcha, 用户答案_captcha
	} catch (e) {
		console.log('验证码错误', e)
	} finally { // 不管是否异常，都会执行finally的

	}
}
const throttle = new Throttle(updateCaptcha, 100)
window.addEventListener("getCaptcha", throttle)

// 开始注册
const TrueEnroll = () => {
	isEnroll.value = true
}
// 确认登录
const param = ref({
	username: '',
	password: ''
})

const submitForm = async () => {
	// 表单检验
	if (param.username === '' || param.password === '' || _captcha.value === '') {
		ElMessage.error("登录信息为空，请完整填写账号、密码和验证码！")
		return
	}

	await debounce.debounceEnd(5)



	// 发送请求到后端，获取响应数据
	const _captcha_ = _captcha.value
	const username_ = param.value.username // window.JSON.stringify(xxx)
	const password_ = param.value.password
	console.log(`这里怎么处理的${username_}, ${password_}`)
	const loginRes = await verifyLogin(username_, password_, _captcha_);
	// 如果传递了注册信息
	let val = await setUser(loginRes)
	if (!val) {
		updateCaptcha()
	} else {
		let path = localStorage.getItem('path') || '/'
		let upId = path.split('?')[1] ? path.split('?')[1].slice(5) : 0
		if (upId) {
			router.push({ path: path, query: { upId } })
		} else {
			router.push({ path: path })
		}
		// 跳转到首页
		localStorage.removeItem('path')
	}
}
const rules = {
	username: [{
		required: true,
		message: '请输入用户名',
		trigger: 'blur'
	}],
	password: [{
		required: true,
		message: '请输入密码',
		trigger: 'blur'
	}]
}
onMounted(() => {
	updateCaptcha()
})
// 获取注册信息   
watch(enrollData, (newValue) => {
	// 更新到页面上
	console.log(`看下新值:${JSON.stringify(newValue)}`)
	param.value.username = newValue.username
	param.value.password = newValue.password
	// enrollEmitValueData.value.userId
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
$small-border-radius: 10px;

.login-account {
	margin-top: 2.5rem;
}

.captcha-area {
	margin-bottom: -1rem;
	border-radius: $small-border-radius;
	width: 10rem;
	height: 3rem;
}

.login-account ::v-deep(.el-form-item__error) {
	margin-top: -0.6rem;
	font-size: 1rem;
	font-weight: 400;
}

/* 样式穿透，修改elementUI */
.login-account>.login-area {
	margin-top: 3rem;
	margin-bottom: 2.8rem;
	width: 100%;
	display: flex;
	flex-flow: row wrap;
	justify-content: center;
}
</style>