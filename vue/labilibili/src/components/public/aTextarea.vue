<!--输入框组件-->
<template>
    <div class="flex-column-left-max-container" style="position: relative;">
        <textarea required v-model="valueText"  
        :maxlength="maxLen" @input="handleInput"
        class="self-textarea">
        </textarea>
        <p>{{currentLen}}/{{maxLen}}：{{remainingChars}}字剩余</p>
    </div>
</template>

<script setup>
import { computed, defineProps, defineEmits, ref } from "vue"
const props = defineProps({
    definedPlaceholder: {
        type: String,
        default: "输入默认placeholder",
        require: false
    },
    maxLen: {
        type: Number,
        default: 0,
        require: false
    },
    inputData: {
        type: String,
        default: "",
        require: true
    }
})
const sanitizeInput = (input) => {
    return input.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}
const valueText = ref(sanitizeInput(props.inputData))
const maxLen = props.maxLen
const currentLen = computed(() => valueText.value.length)
const emit = defineEmits('update:modelValue')
const handleInput = (event) => {
    valueText.value = sanitizeInput(event.target.value);   
    emit('update:modelValue', valueText.value); // Emit the update to the parent component
};
const remainingChars = computed(() => {
    return maxLen - currentLen.value
})
// watch(()=>props.inputData, (newValue) => {
//     value.value = sanitizeInput(newValue)
//     console.log(`当前的输入2:${value.value}`)
// })
</script>

<style lang="scss" scoped>
.self-textarea {
    position: absolute;
}
p {
    position: absolute;
    font-size: 0.8rem;
    width: 7.5rem;
}
</style>