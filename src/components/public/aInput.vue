<!--input公共组件：实现过滤，xss防御-->
<template>
    <el-input :type="type_" :placeholder="placeholder_" v-model="inputData" @keyup.enter="defendXSS" />
</template>

<script name="aInput" setup>
import xss from "xss"
import { defineProps, defineEmits, ref } from "vue"
import { ElMessage } from "element-plus"
import { option } from "./option"
const inputData = ref('')
const props = defineProps({
    definedType: {
        type: String,
        default: "text",
        require: false
    },
    definedPlaceholder: {
        type: String,
        default: "?",
        require: false
    },
    regStr: { /* 正则表达式的约束 */
        type: String,
        default: /^/,
        require: false
    },
    errorMessage: {
        type: String,
        default: "输入的文字不符合要求!",
        require: false    
    }
})
const data = defineEmits({
    
})
const type_ = props.definedType // input中的类型：text, password, email
const placeholder_ = props.definedPlaceholder
const input_xss = new xss.FilterXSS(option)
// var html = input_xss.process(line);
const defendXSS = (e) => {
    // 使用白名单防XSS攻击
    inputData.value = input_xss.process(e.target.value)
    // 使用正则表达式限制 
    if(!props.regStr.test(inputData.value)) {
        ElMessage.error(props.errorMessage)
    }
}
</script>

<style lang="scss">
.el-input__wrapper {
    border: none !important;
    box-shadow: none !important;
    padding: 0px !important;
}
.el-input__inner {
    padding: 0px 5px !important;
}
</style>