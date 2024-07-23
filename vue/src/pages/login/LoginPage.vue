<!--登录页-->
<template>
	<div class="login-wrap">
		<vue-particles 
		id="tsparticles"
		:particlesInit="particlesInit"
		:particlesLoaded="particlesLoaded"
		:options="options" 
		/>
		<!--登录栏目-->
		<div class="login-panel self-center-box-first">
			<div class="chose-login" @click.stop="isChangeExpanse=~isChangeExpanse" 
			:class="{'change-login-background':isChangeExpanse}"><!--左上角点击后选择登录方式-->	
				<div class="chose-login-text">
					<p v-if="!isChangeExpanse">切换<br/>登录方式</p>
					<p class="chose-login-text-after" v-else>切换登录方式</p>
				</div>
			</div>
			<!--切换登录的页面-->
			<div v-if="isChangeExpanse" class="chose-content">
				<div v-for="(loginItem, index) in loginMethod" :key="index" @click.stop="choseLoginMethods(index)"
				class="login-method-item common-based-btn flex-based-container">
					<img :src="loginItem.icon" style="margin-right:2.5rem;"/>
					<p style="width">{{loginItem.name}}</p>
				</div>
				<div class="flex-center-container chose-tip">
					<img src="@/assets/img/login/circle_decoration.svg" />
					<p>点击非按钮位置返回</p>
				</div>
			</div>
			<div v-else>
				<!--登录页面-->
				<div class="title">LABiliBili</div>
				<!--方式1-手机号登录-->
				<div v-if="currentTypeIndex===0">
					<phoneLogin />
				</div>
				<!---->
				<!--方式2-邮箱登录-->
				<div v-else-if="currentTypeIndex===1">
					<mailLogin />
				</div>
				<!--方式3-账户登录-->
				<template v-else>
					<accountLogin />
				</template>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, reactive, toRefs,defineAsyncComponent } from 'vue'
import { loadSlim } from "tsparticles-slim"
import { particlesConfig } from './particlesConfig'
const isChangeExpanse = ref(false)
const currentTypeIndex = ref(0) // 三种登陆方式: 用0,1,2；先默认0
const accountLogin = defineAsyncComponent(()=>
	import ('./ModuleAccount')
)
const phoneLogin = defineAsyncComponent(()=>
	import ('./ModulePhone')
)
const mailLogin = defineAsyncComponent(()=>
	import ('./ModuleMail')
)
// 切换登录方式
const loginMethod = [{
	name: "手机号登录",
	icon: require("@/assets/img/login/phone.svg")
},{
	name: "邮箱登录",
	icon: require("@/assets/img/login/mail.svg")
},{
	name: "账户登录",
	icon: require("@/assets/img/login/account.svg")
}]
const choseLoginMethods = (val)=>{
	currentTypeIndex.value = val
	isChangeExpanse.value = false	
}
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
// watch(enrollData, (newValue) => {
// 	enrollData.value = newValue	
// })
</script>

<style lang="scss" scoped>
@import "@/assets/css/login.scss";
$small-border-radius: 10px;
.login-wrap{
    width: 100%;
	height: 100%;
	position: relative;
	background: #ffffff;
	z-index: 0;
}
.login-panel{
	width: 30rem;
	height: 433.8px; /* 直接F12中找出来的 */
	border-radius: 25px;
	background: rgba(255, 255, 255, 0.75);
	overflow: hidden;
	position: absolute;
	/* panel本身居中 */
	padding-left: 2rem;
	padding-right: 2rem;
	/* panel中元素居中 */
	display: flex;
	justify-content: center;
	align-items: center;
	flex-flow: column wrap;
}
.chose-login, .chose-login:visited{
	background: rgb(88 141 231 / 86%); //#74ace8
	position: absolute;
	border-radius: 75px 45px 100px 0; 
	top: -1rem; /* 从顶部开始 */
	left: -1rem; /* 从左侧开始 */
	width: 8rem;
	font-size: 1.2rem;
	height: 9rem;
	z-index: 100;
	overflow: hidden; 
	transition: transform 0.03s ease, background 0.03s ease; /*虽然background变红但能用 */
	cursor: pointer;
}
.chose-login:hover {
	background: rgb(88 141 231 / 75%);
}
.chose-login:active {
	transition: 0.08s;
	font-size: 1.1rem;
	background: rgb(88 141 231 / 68%); // #5798f1
}
.change-login-background {
	width: 35rem;
	height: 450px;
	background: rgb(88 141 231 / 68%);
	border-radius: 25px;
	top: 0;
	left: 0;
	z-index: 1;
	transition: transform 0.01s ease, background 0.01s ease;
} 
.chose-login-text{
	color: #ffffff;
	margin: 2rem 1.6rem;
	font-weight: 600;
	line-height: 2rem;
    text-align: center;
	z-index: 5;
	.chose-login-text-after {
		font-size: 1.7rem;	
	}
}
@mixin login-method-text {
	font-size: 1.2rem;
}
.chose-content{
	margin-top: 4rem;
	float: inline-start;
	z-index: 99;
	.login-method-item {
		background: #fff;
		border: 1px solid #4a8fef;
		font-weight: 600;
		width: 15rem;
		height: 3rem;
		margin-bottom: 1rem;
		border-radius: $small-border-radius;
		padding-left: 3.5rem;
		p {
			@include login-method-text;
			color: #4a73c2;
		}
	}
	.chose-tip {
		@include login-method-text;
		color: #fff;
		margin-top: 5rem;
		img {
			margin-right:0.4rem;
			animation:rotate 4s linear infinite;
		}
	}
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