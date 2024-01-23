<!--登录页-->
<template>
	<div class="login-wrap">
		<vue-particles 
		id="tsparticles"
		:particlesInit="particlesInit"
		:particlesLoaded="particlesLoaded"
		:options="options" 
		/>
		<!--注册弹窗-->
		<el-dialog :visible="isEnroll" title="注册">
			<enrollVue />
		</el-dialog>
		<!--登录栏目-->
		<div class="login-panel">
			<!--TODO选择登录方式-->
			<div class="chose-login" @click="isChangeExpanse=true"><!--左上角点击后选择登录方式-->	
				<p class="chose-login-text">切换<br/>登录方式</p>
			</div>
			<!--切换登录的页面-->
			<div v-if="isChangeExpanse" class="chose-content"><!--TODO覆盖Panel-->
				<div class="return-btn" @click="isChangeExpanse=false">返回</div>
			</div>
			<!--登录页面-->
			<div class="title">FakeLi</div>
			<!--model：绑定时用的数据，ref：提交时用的数据-->
			<el-form :model="param" :rules="rules" ref="login" label-width="0px" class="login-form">
			<!--方式1-手机号登录-->
			<!--方式2-微信登录-->
			<!--方式3-账户登录-->
			<div class="login-account">
			<el-form-item prop="username">
				<el-input class="login-input" v-model="param.username" placeholder="username">
					<template #prepend>
						<el-button icon="User" style="color: #7ec3fb;"></el-button>
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
						<el-button icon="Lock" style="color: #7ec3fb; "></el-button>
					</template>
				</el-input>
			</el-form-item>
			<div style="display: flex; justify-content: space-between;">
				<el-input v-model="_captcha" style="width: 13rem;" placeholder="请输入验证码"></el-input>
				<el-image class="captcha-area" :src="captcha" @click="updateCaptcha()"></el-image>
			</div>
			<div class="login-area">
				<el-button type="primary" class="basic-btn login-btn" @click="isEnroll=true">注 册</el-button>
				<el-button type="primary" class="basic-btn login-btn" @click="submitForm(login)">登 录</el-button>
			</div>
			</div>
		</el-form>
		</div>
	</div>
</template>

<script setup>
import { onMounted, toRefs, defineAsyncComponent } from 'vue'
import { ref, reactive } from 'vue'
import { loadSlim } from "tsparticles-slim"
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { Lock, User } from '@element-plus/icons-vue'
import { particlesConfig } from './particlesConfig'
import { getCaptcha, verifyLogin } from '@/api/login'
import { request } from '@/api/index'
import { useUserInfo } from '@/store/userInfo'
import Debounce from '@/static/debounce'
import Throttle from '@/static/throttle'
const isEnroll = ref(false) // 选择注册
const enrollVue = defineAsyncComponent(()=>
	import ('./EnRoll')
)	
/**
 * 切换登陆方式
 *  */
const isChangeExpanse = ref(false)
// let currentloginMethod = ref(2) // Attention 三种登陆方式: 用0,1,2
// const diffLoginMethod = [{

// },{

// }]
// const choseLoginMethods = ()=>{
// 	currentloginMethod.value = (currentloginMethod.value+1)%3
// }
/**
 * 基础款粒子背景
 *  */ 
const data = reactive({
	options: particlesConfig
})
const { options } = toRefs(data) // 直接解构，使html部分不用写data.options
const particlesInit = async engine => {
	await loadSlim(engine)
}
const particlesLoaded = async container => {
	console.log('粒子背景加载成功，container：',container)
}

/**
 * 登录逻辑实现
 *  */ 
// 验证码获取
let captcha = ref('') 
// NOTE async&await用来将异步转为同步，前者表示有异步操作
const updateCaptcha = async()=>{
	try{
		captcha.value = await getCaptcha() // 别忘了 响应式！！
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

	// TODO 为了快速通过本页面，使用admin + 123456
	console.log('用户名', login.username)
	if(param.username==='admin' && param.password==='123456'){
		router.push('/')
		return
	}	

	// 发送请求到后端，获取响应数据
	const _captcha_ = window.JSON.stringify(_captcha.value)
	const username_ = window.JSON.stringify(param.username)
	const password_ = window.JSON.stringify(param.password)
	console.log('传递参数：\n','captcha', _captcha_,'\nusername', username_, '\npassword',password_)
	const token = await verifyLogin(username_, password_, _captcha_)
	console.log('token: ' + token)
	if(token && typeof token === 'string'){
		ElMessage.success('登录成功')
		// 保存本地信息
		request.headers.set('authorization', token) // TODO：不知道可不可以用
		useUserInfo
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

/**
 * 组件挂载JS
 */
onMounted(()=>{
	updateCaptcha()

})
</script>

<style scoped>
.login-wrap{
    width: 100%;
	height: 100%;
	position: relative;
	background: #ffffff;
}
.login-panel{
	width: 30rem;
	border-radius: 25px;
	background: rgba(255, 255, 255, 0.75);
	overflow: hidden;
	position: absolute;
	/* panel本身居中 */
	top: 50%;
	left: 50%;
	transform: translate(-50%, 20%);
	z-index: 99;
	padding-left: 2rem;
	padding-right: 2rem;
	/* panel中元素居中 */
	display: flex;
	justify-content: center;
	align-items: center;
	flex-flow: column wrap;
}
.chose-login, .chose-login:visited{
	background: #74ace8 ;
	position: absolute;
	border-radius: 75px 45px 100px 0; 
	top: -1rem; /* 从顶部开始 */
	left: -1rem; /* 从左侧开始 */
	width: 8rem;
	font-size: 1.2rem;
	height: 9rem;
	z-index: 100;
}
.chose-login:active{
	transition: 0.08s;
	font-size: 1.1rem;
	background: #5798f1;
} 
.chose-login-text{
	color: #ffffff;
	margin: 2rem 1.6rem;
	font-weight: 600;
	line-height: 2rem;
    text-align: center;
}
.login-account{
	margin-top: 2.5rem;
}
.login-account .login-input:first-child{
	margin-bottom: 1.3rem;
}
.captcha-area{
	margin-bottom: -1rem;
	border-radius: 10px;
	width: 10rem;
	height: 3rem;
}
.chose-content{
	float: inline-start;
}
.chose-content > .return-btn{
	width: 3rem;
	height: 2rem;
	background-color: #092864;
	color: #fff;
	cursor: pointer;
}
/* 样式穿透，修改elementUI */
.login-account ::v-deep(.el-form-item__error){
	margin-top: -0.6rem;
	font-size: 1rem;
	font-weight: 400;
}
.login-account ::v-deep(.login-input){
	width: 24rem;
	font-size: 1.2rem;
}
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
.login-area ::v-deep(.login-btn, .login-btn:visited){
	height: 2.8rem;
	border: none;
	font-size: 1.3rem;
	font-weight: 500;
	border-radius: 10px;
	background-color: #4a8fef;
	box-shadow: 0 0 25px 3px #ffffffe5; 
}
.login-area ::v-deep(.login-btn:hover){
	background: #467cc6;
}
.login-area ::v-deep(.login-btn:active){
  background: #19449a;
  /*缩小的动效*/
  transition: 0.05s;
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
  box-shadow: 0 0 0 0;
}
.title{
	font-weight: 800;
	font-size: 2.6rem;
	color: #092864;
	margin-top: 2rem;
	margin-bottom: 1rem;
	border-bottom: 1px solid #ddd;
}

</style>