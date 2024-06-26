<!--封装一个鼠标悬浮后会显示编辑、删除的定时器-->
<template>
    <div class="hover-item-container"
    @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
        <!--左右型-->
        <div class="hover-target flex-column-left-max-container">
            <p class="hover-main-title">{{data.collectGroupName}}</p><!--标题-->
            <p class="hover-sub-title">创建：{{data.createTime}}</p><!--数据-->
        </div><!--TODO：增加数量限制-->
        <div v-if="showEdit" class="edit-wrap flex-center-container">
            <el-button @click="editAction()" class="common-based-btn edit-item">编辑</el-button>
            <el-button @click="deleteAction()" class="common-based-btn edit-item">删除</el-button>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed } from 'vue'
const showEdit = ref(false) // 是否展示编辑
// const hovering = ref(false) // 当前是否
let timer = null;

const emits = defineEmits(['update:editData',
 'update:deleteData', 'update:addToData','updataEdit'])
// 获取信息
const props = defineProps({
    data: {
        type: Object,
        required: true,
        default: undefined
    }
})
// 数据
const data = computed({
    get: function() {
        return props.data
    },
    set: function(val) {
        emits('update:editData', val)
    }
})
const handleMouseEnter = () => {
    timer = setTimeout(() => {
        showEdit.value = true
    }, 10); // 0.01秒后显示菜单
}
const handleMouseLeave = () => {
    showEdit.value = false;
    if (timer) {
        clearTimeout(timer);
        timer = null;
    }
}

const editAction = () => {
    // 修改
    emits('updataEdit',data.value)
}

const deleteAction = () => {
    // 发出api后再删除
    emits('deleteData',data.value)
    
}
</script>

<style lang="scss">
</style>

<style lang="scss" scoped>
$second-color: #4183ca;
$here-color: #bfd7f0;
.hover-item-container {
    position: relative;
    padding: 15px;
    width: 24rem;
    &:hover {
        background-color: $here-color;
        border-radius: 10px;
    }
}
.hover-main-title {
    color: #235891;  
}
.edit-item {  
    text-align: center;
    font-size: 16px;
    border: none;
    border-radius: 15px;
    background-color: #fff; 
    color: $second-color;  
    font-weight: 600; 
} 
.hover-target {
    cursor: pointer;
    .hover-main-title {
        font-size: 1.2rem;
        font-weight: 700;
    }
    .hover-sub-title {
        font-size: 0.9rem;
    }
}
.edit-wrap {
    position: absolute;
    right: 1rem;
}
.item button {
    background: none;
    border: none;
    padding: 10px;
    text-align: left;
    cursor: pointer;
    width: 100%;
}
.item button:hover {
    background-color: #f0f0f0;
}
</style>