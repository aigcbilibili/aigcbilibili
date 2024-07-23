<!--创建新文件夹（收藏夹和视频合集）的弹窗-->
<template>
    <div class="new-collection-panel">
        <p class="new-collection-title">{{title}}</p>
        <div class="flex-column-left-max-container">
            <div class="flex-left-container">
                <p class="new-collection-name">收藏夹名称</p>
                <!-- <aInput class="new-collection-input" 
                v-model:inputData="collectCreateData.name" 
                @update:inputValue="handleCollectTitleUpdate"
                :definedPlaceholder="'请输入收藏夹名称'" /> -->
                <el-input placeholder="请输入收藏夹名称" v-model="collectCreateData.name"></el-input>
            </div>
            <div class="flex-left-container">
                <p class="new-collection-name">简介</p>
                <el-input type="textarea" v-model="collectCreateData.profile"></el-input>
            </div>
        </div>
        <el-button class="new-collection-btn 
        common-btn-center" @click="collectCreateProcess()">创建</el-button>
    </div>
</template>

<script setup>
import { ref, onMounted, inject } from "vue"
import { addCollection } from "@/api/like_and_collect"
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from "element-plus"
import aInput from "@/components/public/aInput"
import aTextarea from "@/components/public/aTextarea"
const updateCollectionVal = inject('isUpdateCollection')
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const collectCreateData = ref({
    id: 0,
    name: "",
    userId: userId,
    profile: "" // 不会用到收藏夹简介
})
const title = ref('新建文件夹')
let emit =  defineEmits(['getData'])
// 收藏夹创建
const collectCreateProcess = async() => {
    if(!collectCreateData.value.name){ 
        return ElMessage({
            message: "请输入收藏夹名称",
            type: "warning"
        
        })
    }
    const res = await addCollection(collectCreateData.value.name, userId,collectCreateData.value.id)
    if(res) {
        ElMessage.success( collectCreateData.value.id ? '收藏夹修改成功' : `收藏夹创建成功！`)
        // 返回到收藏夹创建
        // updateCollectionVal.updateCollection(false)
        emit('getData')
        
    }
}
// 收藏夹名称
const handleCollectTitleUpdate = (newValue) => {
    collectCreateData.value.name = newValue
}

const handleEdit = (row)=>{
    title.value = '编辑文件夹'
    // collectCreateData.value = Object.assign(collectCreateData.value,row)
    collectCreateData.value.id = row.collectGroupId;
    collectCreateData.value.name = row.collectGroupName;
}
const clearData = ()=>{
    delete collectCreateData.value.id;
    title.value = '新建文件夹'
   let keys =  Object.keys(collectCreateData.value);
   keys.forEach((item)=>{
    collectCreateData.value[item]= ''
   })
}

defineExpose({
    handleEdit,
    clearData
})



onMounted(()=>{
    
})
</script>

<style lang="scss" scoped>
.new-collection-panel {
    width: 15rem;
    height: 13.5rem;
    padding: 1rem;
    border-radius: 10px;
    background-color: #fff;
    position: relative;
}
.new-collection-title {
    font-weight: 800;
    font-size: 1.2rem;
    color: #3455ab;
    margin-bottom: 1rem;
}
.new-collection-name {
    text-align: left;
    width: 7rem;
    font-size: 0.9rem;
}
.new-collection-textarea {
    left: calc(-30%);
    ::v-deep textarea {
        width: 8rem;
    }
    ::v-deep p {
        top: 3.5rem;
    }
}
.new-collection-input {
    border: none;
}
.new-collection-btn {
    padding: 0.1rem 0.3rem;
    font-size: 0.8rem;
    position: absolute;
    bottom: 1rem;
    background-color: #3455ab;
    border: none;
    color: #fff;
    &:hover, &:active {
        background-color: #3455ab;
    }
}
</style>