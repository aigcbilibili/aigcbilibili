<!--input公共组件：实现过滤，xss防御-->
<template>
    <el-input 
    :type="type_" :placeholder="placeholder_" :maxlength="maxLen" 
    v-model="valueText" @change="handleInput" />
</template>

<script name="aInput" setup>
import xss from "xss"
import { defineProps, defineEmits, ref, computed } from "vue"
import { ElMessage } from "element-plus"
import { option } from "./option"
const props = defineProps({
    definedType: {
        type: String,
        default: "text",
        required: false
    },
    definedPlaceholder: {
        type: String,
        default: "输入默认placeholder",
        required: false
    },
    inputData: {
        type: String,
        default: "",
        required: true
    },
    regStr: { /* 正则表达式的约束 */
        type: String,
        default: /^/,
        required: false
    },
    errorMessage: {
        type: String,
        default: "输入的文字不符合要求!",
        required: false    
    }
})
const emit = defineEmits('update:inputValue')
const type_ = props.definedType // input中的类型：text, password, email
const placeholder_ = props.definedPlaceholder
const input_xss = new xss.FilterXSS(option)
// var html = input_xss.process(line)
const validateInput = (value) => {
  const regex = typeof props.regStr === 'string' ? new RegExp(props.regStr) : props.regStr;
  if (!regex.test(value)) {
    ElMessage.error(props.errorMessage);
    return false;
  }
  return true;
}
const defendXSS = (e) => {
    // 使用白名单防XSS攻击
    let XSSData = input_xss.process(e)
    // 使用正则表达式限制 
    if(!props.regStr.test(e)) {
        ElMessage.error(props.errorMessage)
    }
    return XSSData
}

const valueText = ref(defendXSS(props.inputData))
const handleInput = (value) => {
    const sanitizedValue = defendXSS(value)
    if (validateInput(sanitizedValue)) {
        valueText.value = sanitizedValue
        emit('update:inputValue', valueText.value)
    }
    console.log(`input的值呢：${sanitizedValue}`)
}
// watch(() => props.inputData, (newValue) => {
//   value.value = defendXSS(newValue);
// })
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