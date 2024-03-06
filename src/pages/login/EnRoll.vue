<template>
    <el-dialog
    v-model="isShow"
    :show-close="true"
    class="dialog flex-column-between-container"
    style="
      width: 100%;
      height: 100%;
      background-color: #fff !important;
      box-shadow: -5px 2px 25px 0 #79b1eca9 !important;
      border-radius: 10px !important;
    "
    >
        <template #header>
            <p class="title">注册</p>
        </template>
        <template #default>
            <!--注册表格-->
            <div class="flex-column-left-max-container">
                <div v-for="(item, index) in enrollInfo" :key="index" class="input-box flex-based-container">
                    <p class="input-name">{{item.name}}</p>
                    <el-input class="login-input input-content" v-model="enrollData[index]" 
                    :placeholder="item.placeholder" :style="{'width': item.name==='绑定手机号'? '16rem': ''}"></el-input>
                    <div v-if="item.name==='绑定手机号'">
                        <el-button type="primary" class="basic-btn get-message-captcha common-based-btn enroll-message-captcha"
                         @click="updateCaptcha()">验证码</el-button>
                    </div>
                </div>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, onBeforeUnmount } from 'vue'
const props = defineProps({
    enrollConfirm: {
      type: Boolean,
      required: true,
      default: false
    }
})
const emits = defineEmits(['update:enrollConfirm'])
const isShow = computed({
    get: function(){
        return props.enrollConfirm
    },
    set: function(val){
        emits('update:enrollConfirm', val)
    }
})
// 注册的信息说明
const enrollInfo = [{
    name: "用户名",
    placeholder: "请输入含字母、符号和汉字的5-10个字符",
    data: String,
    required: true
},{
    name: "昵称",
    placeholder: "请输入包含唯一的8位数字",
    data: String,
    required: true
},{
    name: "密码",
    placeholder: "请输入含大小写字母、符号和数字的6-16个字符",
    data: String,
    required: true
},{
    name: "确认密码",
    placeholder: "请重新输入密码",
    data: String,
    required: true
},{
    name: "绑定手机号",
    placeholder: "请输入中国地区的手机号",
    data: String,
    required: false
},{
    name: "获取验证码",
    data: String,
    required: false
}]
// 注册的数据
const enrollData = ref(["","","","",""]) // 5个
// 注册确认：enrollData外除了
const enrollConfirm = () => {
    
}
onBeforeUnmount(() => {
    emits('update:enrollConfirm', false)
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