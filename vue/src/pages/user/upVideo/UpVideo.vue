<template>
    <!--发布视频-->
    <div class="flex-column-left-max-container"> 
        <!--视频标题-->
        <div class="upload-item">
            <aInput :definedPlaceholder="'请输入视频标题'" class="title-input" 
            v-model:inputData="upData.title" @update:inputValue="handleTitleUpdate"  />
        </div>
        <div class="upload-divided"></div>
        <!--文件上传位置-->
        <div class="upload-item video-here">
            <div v-if="isUploadedVideo===0"><!--XXX multiple参数是多选文件，用不到-->
                <el-upload class="video-uploader"
                    drag
                    action="/api/createCenter/getVideoCover"
                    auto-upload="true"
                    :headers="thisHeaders"
                    :data="upData.file"
                    :limit="1"
                    :accept="acceptFileType"
                    :before-upload="handleVideoUpload"
                    :on-progress="handleProgress"
                    :on-success="handleVideoSuccess"
                    :before-remove="beforeVideoRemove"
                >
                    <div class="el-upload__text">将视频文件拖到此处，或<em>点击这里上传</em></div>
                    <div class="el-upload__tip">只能上传.mp4, .wmv, .avi视频，视频最大是2GB</div>
                </el-upload>
                <!-- <input type="file" id="upload-video" @input="handleVideoTest"> -->
            </div>
            <div v-else-if="isUploadedVideo===2"> <!--已经传完-->
                <div class="flex-between-container"><!--如果-->
                    <div class="flex-center-container">
                        <img src="@/assets/img/video/videoFile.png" 
                        style="width: 2rem; height: 2rem;" />
                        <p>{{videoNameRaw}}</p>
                    </div> 
                    <div class="flex-center-container">
                        <div class="detail-btn-chosen common-btn-center upload-finish-btn" 
                        @click="reloadVideo()">重传</div>
                        <div class="detail-btn-chosen common-btn-center  upload-finish-btn" 
                        @click="download()">下载</div>
                    </div> 
                </div>
            </div>
            <div v-else> <!--正在上传-->
                <el-progress :stroke-width="10"
                :percentage = "uploadVideoProgress"
                style="width: 75rem;"
                class="video-upload-progress">
                </el-progress>
            </div>
        </div>
        <!--视频封面-->
       <div class="upload-item video-cover flex-left-container">
            <p class="upload-item-text">封面</p>
            <el-upload drag action="#"
                class="cover-uploader"
                auto-upload="false"
                accept="image/bmp,image/jpeg,image/jpg,image/png,image/webp"
                v-model:file="upData.cover"
                :limit="1"
                :http-request="handleCoverUpload"
                :on-change="handleChange"
                :on-exceed="onExceed"
                :on-remove="handleRemove"
            >
                <img v-if="upData.cover" :src="coverData" class="img-cover" />
                <div v-else>
                    <div class="el-upload__text" style="margin-top: -1.5rem;">将封面文件拖到此处，或<em>点击这里上传</em></div>
                    <div class="el-upload__tip">只能上传.png, .jpg, .jpeg文件，图片最大是5MB</div>
                </div>
                <div v-if="isCoverOver&&upData.cover" @mouseover="isCoverOver=true">
                    预览 下载 删除
                </div>
            </el-upload>
            <!--视频的前3帧-->
            <div v-if="threeCoversData.length>0" style="margin-left: 2rem;">
                <div v-for="(item, index) in threeCoversData" :key="index">
                    <img :src="item" class="video-alternate-cover" 
                    style="width: 14rem; height: 8rem;" alt="webp cover" />
                </div>
            </div>
        </div>
        <!--视频类型-->
        <div class="upload-item flex-left-container">
            <p class="upload-item-text">视频类型</p>
            <div class="user-content-slide-btn video-type-wrap">
                <span class="slider-tip flex-center-container" :class="{
                    'slider': upData.type===0
                }" 
                style="margin-right: 0.3rem;" @click.stop="changeType()">原创</span>
                <span class="slider-tip flex-center-container" :class="{
                    'slider': upData.type===1
                }" @click.stop="changeType()"
                >连载</span>
            </div>
        </div>
        <!--视频标签-->
        <div class="upload-item video-cover flex-left-container">
            <p class="upload-item-text">标签</p>
            <div class="flex-column-left-max-container">
                <!--自定义标签-->
                <aInput v-model="upData.tag" :definedPlaceholder="'输入视频标签'" />
                <div style="display: flex; flex-flow: row wrap;">
                    <div v-for="(item, index) in tagsRecData" :key="index"
                    class="tags-and-labels" style="padding: 0.2rem 0.5rem; 
                    border-radius: 10px;margin-right: 1rem;"><!--展示推荐标签-->
                        {{item}}
                    </div>
                </div>
            </div>
        </div>
        <!--视频简介-->
        <div class="upload-item flex-left-container video-profile">
            <p class="upload-item-text">简介</p>
            <aTextarea v-model:inputData="upData.intro" :maxLen="1000" 
            @update:modelValue="handleProfileUpdate" />
        </div>
        <!--加入合集设置-->
        <div class="upload-item">
            <p class="upload-item-text">加入合集</p>
            <div class=""><!--视频合集-->
            </div>
        </div>
        <!--保存和上传-->
        <div class="flex-center-container self-center-box-third" style="margin: 2rem 0;">
            <div class="common-btn-center detail-btn" 
            style="padding: 0.3rem 0.7rem; margin-right: 1rem;">保存</div>
            <div class="common-btn-center detail-btn" 
            @click="upRes()" style="padding: 0.3rem 0.7rem;">上传</div>
        </div>
    </div>
</template>

<script setup>
import aInput from '@/components/public/aInput'
import aTextarea from '@/components/public/aTextarea.vue'
import axios from "axios"
import { ref, watchEffect, onUpdated } from "vue" 
import { addUpVideo } from "@/api/video"
import { useUserInfo} from "@/store/userInfo"
import { ElMessage } from 'element-plus'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const isUploadedVideo = ref(0) // 是否上传了视频，0暂未、1上传中、1已传完
const isProgress = ref(false) // 是否打开进度条
const uploadVideoProgress = ref(0) // 上传进度条
const tagsData = ref([]) // 写入标签数据
const tagsRecData = ref(['你好','再见','永远的','鱼鱼']) // 推荐标签数据
const acceptFileType = '.mp4,.wmv,.avi' // 目前能接受的视频文件后缀
const videoNameRaw = ref('') // 视频上传后原本的名称
const coverData = ref('') // 封面在网页上的展示
const isCoverOver = ref(false) // 首图预览等
const acceptFileMaxSize = 2 * 1024 * 1024 * 1024 // 视频最大2GB，单位为字节
const acceptCoverMaxSize = 5 * 1024 * 1024 // 视频封面最大5MB
const threeCoversData = ref([]) // 视频前3帧
const thisHeaders = { // 视频前3帧接口的头
    // 'Content-Type': 'multipart/form-data', // el-upload会封装为formData，不需要Content-type
    'X-Requested-With': 'XMLHttpRequest',
    'authorization': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEiLCJyb2xlIjoidXNlciJ9.ybuiJ87Nq7xgcWrM50P_VgAF1P74fnEN8jCSH5daqR2re4hVTMYgkzMHWZlK104guM75RGWgVxNrtfnhinjR-g',
    'laBiliBiliHeader': 'test_method_1'
}
const defaultData = {
    title: "",
    cover: undefined,
    file: undefined, // 视频文件
    type: 0, // 0 自制，1转载
    tag: "",  // 分区通过一个字符串保存、使用,隔开
    intro: "",
    setTimeFlag: false, // 是否定时发布
    setTime: "", // 定时发布的时间
    setFolderFlag: false, // 是否加入合集
    setFolder: 0, // 加入的合集id
}
const upData = ref(defaultData)
// file转base64
const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}
// base64转file
const base64ToFile = (base64, filename) => {
    const arr = base64.split(',')
    const type = arr[0].match(/:(.*?);/)[1]
    const bstr = atob(arr[1])
    let n = bstr.length
    const u8arr = new Uint8Array(n)
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
    }
    const item = new File([u8arr], filename, { type: type }) //TODO 这段代码处理有问题
    console.log(`${base64}\n转换为file:${JSON.stringify(item)}`) 
    return item
}
// base64转blob
const dataURLtoBlob = (base64) => { 
    const byteCharacters = atob(base64.split(',')[1]);  
    const byteArrays = [];
    
    for (let i = 0; i < byteCharacters.length; i++) {
        byteArrays.push(byteCharacters.charCodeAt(i));
    }
    return new Blob([new Uint8Array(byteArrays)], { type: 'image/jpeg' })
}
// blob转为webp
const convertToWebP = async(file) => {
    try {
        if (!file ) {
            return null
        }
        const blobUrl = URL.createObjectURL(file)
        const img = new Image()
        img.src = blobUrl
        await img.decode()
        
        const canvas = document.createElement('canvas')
        canvas.width = img.width
        canvas.height = img.height
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0)
        const webpUrl = canvas.toDataURL('image/webp', 0.8)

        if (webpUrl) {
            coverData.value = webpUrl;
        } else {
            ElMessage.error('图片转换失败！');
        }

        URL.revokeObjectURL(blobUrl) // 释放对象URL
        return webpUrl
    } catch(error) {
        console.error('Error converting to WebP:', error)
        return null        
    }
} 
// 获取视频上传
const handleVideoUpload = (file) => {
    // 读取文件内容
    // let reader = new FileReader()
    if(file.size > acceptFileMaxSize) {
        alert('上传的视频文件大小超过限制（2GB）');
        return
    }
    
    isUploadedVideo.value = 1
    // console.log(`嘿嘿1-视频处理前：${JSON.stringify(file)}`)
    return file
}
// 进度条
const handleProgress = (event, file, fileList) => {
    isProgress.value = true; // 显示进度条
    uploadVideoProgress.value = parseInt(event.percent) // 动态获取文件上传进度
    if (uploadVideoProgress.value >= 100) {
        uploadVideoProgress.value = 100
        setTimeout( () => {isProgress.value = false}, 1000) // 一秒后关闭进度条
    }
    return file
}
// 重传
const reloadVideo = () => {
    upData.value.file = undefined
    isUploadedVideo.value = 0
    upData.value.cover = ""
    threeCoversData.value = []
}
// 下载
const download = () => {
    if(!upData.value.file)
        return
    const blob = this.dataURLtoBlob(upData.value.file.data);
    const url = URL.createObjectURL(blob);
    
    const link = document.createElement('a')
    link.href = url
    link.download = this.fileName
    document.body.appendChild(link)
    link.click();
    document.body.removeChild(link)

    URL.revokeObjectURL(url)
}
// 标题更新
const handleTitleUpdate = (newValue) => {
    upData.value.title = newValue
    console.log(`你好标题：${upData.value.title}`)
}
// 简介更新
const handleProfileUpdate = (newValue) => {
    upData.value.intro = newValue
    console.log(`你好简介：${upData.value.intro}`)
}
// 上传视频后的处理
const handleVideoSuccess = async(response, file, fileList) => {
    if(!response || response.length <= 0) { // 错误
        ElMessage.error("上传视频失败")
    }
    isUploadedVideo.value = 2
    upData.value.cover = response.data
    
    if(JSON.stringify(response)===JSON.stringify(file.response)) {
        console.log(`嘿嘿0`)
    } else {
        console.log(`嘿嘿1:${JSON.stringify(response)}\n妄${JSON.stringify(file.response)}`)
    }

    // 上传
    // let coverTitle = upData.value.title===''?'未命名图片By'+userId : upData.value.title+'视频封面'
    // 网页上显示：将base64转为webp
    let data = "data:image/jpg;base64," + response.data // 解析base64
    data = dataURLtoBlob(data)
    data = convertToWebP(data)
    threeCoversData.value.push(data)
    coverData.value = data // cover位置的回显覆盖

    // 处理视频
    delete file.response
    const videoBase64Data = await fileToBase64(file.raw)
    if(videoBase64Data){
        console.log(`嘿嘿不愧是我！`)
        const convertedFile = base64ToFile(videoBase64Data, upData.value.title)
        upData.value.file = convertedFile
    }
    console.log(`嘿嘿2-上传后:获取的file${JSON.stringify(file)}\n类型${typeof JSON.stringify(file)}`)
    return file
}
const beforeVideoRemove = () => {
    upData.value.cover = ""
}
// 图片拖拽上传
const handleChange = async(file) => {
    let extension = ''
    if(file.fileName) {
        extension = file.fileName.split('.').pop().toLowerCase()
        videoNameRaw.value = file.fileName
    } else {
        extension = file.name.split('.').pop().toLowerCase()
        videoNameRaw.value = file.name
    }
    if(!['png', 'jpeg', 'jpg'].includes(extension)) {
        ElMessage.error('上传的图片格式错误！请检查是否为png/jpeg/jpg')  
        return false
    }
    upData.value.cover = await fileToBase64(file.raw)
    coverData.value = convertToWebP(file.raw)
    // upData.value.cover = await convertToWebP(file.raw)
}
// 放大预览
const handlePicPreview = () => {

}
// 文件下载
const handleDownload = () => {

}
// 获取图片上传
const handleCoverUpload = (file) => {
    if(file.size > acceptCoverMaxSize) {
        alert('上传的图片文件大小超过限制（5MB）');
        return
    }   
}
const handleRemove = () => {
    ElMessage.success("图片删除成功！")
    upData.value.cover = ""
}
// 上传结果
const upRes = async() => {    
    // for (const pair of upFormData.entries()) {
    //     console.log(pair[0] + ', ' + JSON.stringify(pair[1]));
    // 
    const res = addUpVideo(upData.value.file, upData.value.intro,
    upData.value.title, userId, upData.value.cover)   
    if(res) {
        ElMessage.success(`${upData.value.title}视频上传成功！`)
        setTimeout(async() => { // 1s后页面强制刷新，刷新太快会导致异步上传丢失     
            window.location.reload()
        }, 1000)
    }
}
// 监听
// watchEffect(async() => { // NOTE watchEffect组件初始化立即执行，watch惰性执行
//     const promises = threeCoversData.value.map(file=> convertToWebP(file))
//     webpImages.value = (await Promise.all(promises)).filter(url=>url!==null)
// })
/** 超出最大上传数时触发 当超出限制时*/
const onExceed = () => {
    ElMessage.error("最多上传1张图片，请先删除在上传")
}
// 更换类型
const changeType = () => {
    upData.value.type = upData.value.type===1 ? 0: 1
}
onUpdated(()=>{
    console.log(`看下更新：${upData.value.intro}`)
})
</script>

<style lang="scss">

</style>

<style lang="scss" scoped>
$share-font-size: 1.1rem; // 本页面的字体统一大小
$light-blue-color: #9ac9fb;
.img-cover {
    width: 13.8rem;
    height: 8.5rem;
    margin: -3rem -8rem !important;
}
.upload-item {
    margin: 1rem 1rem;
    width: auto;
    .upload-item-text{
        margin-right: 1rem;
        min-width: 6rem;
        width: auto;
        height: 1.2rem;
        font-size: $share-font-size;    
    }
}
.upload-divided{
    background: rgb(169, 157, 244);
    margin: -0.6rem auto;
    max-width: 100rem;
    overflow: hidden;
    width: 96%;
    height: 0.1rem;
}
.title-input {
    max-width: 100rem;
    width: 76vw;
    margin: 0 auto;
    ::v-deep .el-input__inner {
        font-size: $share-font-size+0.2rem;
    }
}
.video-here{
    min-height: 3rem;
    max-height: 15rem;
    height: auto;
    width: calc(98%) !important;
    .upload-finish-btn {
        padding: 0.4rem;
        &:first-child {
            margin-right: 0.5rem;
        }
        &:last-child {
            margin-right: 0.3rem;
        }
    }
}
.video-uploader {
    // width: calc(98%);
    height: 7rem;
    margin: 1rem 0;
}
.cover-uploader {
    width: 14rem;
    height: 10rem;
    ::v-deep .el-upload-dragger {
        height: 8rem;
        background-color: #f5f9fe;
    }
}
$middle-width: 76vw;
$large-width: 87vw;
@media screen and (min-width: 820px) {
    .title-input {
        width: $middle-width;
    }
    .video-uploader {
        width: $middle-width;
    }
}
@media screen and (min-width: 1020px) {
    .title-input {
        width: $large-width;
    }
    .video-uploader {
        width: $large-width;
    }
    .upload-divided{
        height: 0.2rem; 
        width: 98%;       
    }
}
.video-profile {
    width: 30rem;
    height: 20rem;
    ::v-deep .self-textarea {
        width: 30rem;
        height: 18rem;
        top: -10rem;
    }
    ::v-deep p {
        bottom: -10.5rem;
    }
}
.video-type-wrap {
    border: 1.5px solid $light-blue-color;
    width: 6.5rem;
    height: 2.8rem;
    .slider-tip {
        width: 3rem;
        height: 2rem;
    }
}
</style>