<template>
    <!--发布视频-->
    <div class="flex-column-left-max-container"> 
        <!--视频标题-->
        <div class="upload-item">
            <aInput :definedPlaceholder="'请输入视频标题'" class="title-input" />
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
                    :data="threeCoversData"
                    :limit="1"
                    :accept="acceptFileType"
                    :before-upload="handleVideoUpload"
                    :on-success="handleVideoSuccess"
                    :before-remove="beforeVideoRemove"
                >
                    <div class="el-upload__text">将视频文件拖到此处，或<em>点击这里上传</em></div>
                    <div class="el-upload__tip">只能上传.mp4, .wmv, .avi视频，视频最大是2GB</div>
                </el-upload>
                <!-- <input type="file" id="upload-video" @input="handleVideoTest"> -->
            </div>
            <div v-else-if="isUploadedVideo===2"> <!--已经传完-->
            </div>
            <div v-else> <!--正在上传-->
                <el-progress>

                </el-progress>
            </div>
        </div>
        <!--视频封面-->
       <div class="upload-item video-cover flex-left-container">
            <p class="upload-item-text">封面</p>
            <el-upload drag action="#"
                class="cover-uploader"
                auto-upload="false"
                accept=".png, .jpg, .jpeg"
                :limit="1"
                :http-request="handleCoverUpload"
                :before-remove="beforeCoverRemove"
            >
            <div class="el-upload__text" style="margin-top: -1.5rem;">将封面文件拖到此处，或<em>点击这里上传</em></div>
            <div class="el-upload__tip">只能上传.png, .jpg, .jpeg文件，图片最大是5MB</div>
            </el-upload>
            <!--视频的前3帧-->
            <div v-if="threeCoversData.length>0" style="margin-left: 2rem;">
                <div v-for="(item, index) in threeCoversData" :key="index">
                    <img :src="item" class="video-alternate-cover" style="width: 14rem; height: 8rem;" />
                </div>
            </div>
        </div>
        <!--视频类型-->
        <div class="upload-item video-cover flex-left-container">
            <p class="upload-item-text">视频类型</p>
            <input type="checkbox" name="原创" checked="false" >
            <input type="checkbox" name="搬运" checked="false" >
        </div>
        <!--视频标签-->
        <div class="upload-item video-cover flex-left-container">
            <p class="upload-item-text">标签</p>
            <div class="flex-column-left-max-container">
                <!--自定义标签-->
                <aInput v-model="upData.tag" />
                <div style="display: flex; flex-flow: row wrap;">
                    <div v-for="(item, index) in tagsRecData" :key="index"
                    class="tags-and-labels" style="padding: 0.2rem 0.5rem; border-radius: 10px;margin-right: 1rem;"><!--展示推荐标签-->
                        {{item}}
                    </div>
                </div>
            </div>
        </div>
        <!--视频简介-->
        <div class="upload-item flex-left-container">
            <p class="upload-item-text">简介</p>
            <aInput v-model="upData.cover" />
        </div>
        <!--定时发布设置-->
        <div class="upload-item">
            <p class="upload-item-text">定时发布</p>
        </div>
        <!--加入合集设置-->
        <div class="upload-item">
            <p class="upload-item-text">加入合集</p>
        </div>
        <!--保存和上传-->
        <div class="flex-center-container self-center-box-third" style="margin: 2rem 0;">
            <div class="common-btn-center detail-btn" style="padding: 0.3rem 0.7rem; margin-right: 1rem;">保存</div>
            <div class="common-btn-center detail-btn" @click="upRes()" style="padding: 0.3rem 0.7rem;">上传</div>
        </div>
    </div>
</template>

<script setup>
import aInput from '@/components/public/aInput'
import aTextarea from '@/components/public/aTextarea.vue'
import axios from "axios"
import { onMounted, ref,  } from "vue" 
import { addUpVideo, getThreeVideoCovers } from "@/api/video"
import { useUserInfo} from "@/store/userInfo"
import { ElMessage } from 'element-plus'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const isUploadedVideo = ref(0) // 是否上传了视频，0暂未、1上传中、1已传完
const tagsData = ref([]) // 写入标签数据
const tagsRecData = ref(['你好','再见','永远的','鱼鱼']) // 推荐标签数据
const acceptFileType = '.mp4,.wmv,.avi' // 目前能接受的视频文件后缀
const acceptFileMaxSize = 2 * 1024 * 1024 * 1024 // 视频最大2GB，单位为字节
const acceptCoverMaxSize = 5 * 1024 * 1024 // 视频封面最大5MB
const threeCoversData = ref([]) // 视频前3帧
const thisHeaders = { // 视频前3帧接口的头
    // 'Content-Type': 'multipart/form-data', // el-upload会封装为formData，不需要Content-type
    'X-Requested-With': 'XMLHttpRequest',
    'authorization': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEiLCJyb2xlIjoidXNlciJ9.ybuiJ87Nq7xgcWrM50P_VgAF1P74fnEN8jCSH5daqR2re4hVTMYgkzMHWZlK104guM75RGWgVxNrtfnhinjR-g',
    'laBiliBiliHeader': 'test_method_1'
}
const upData = ref({
    title: "",
    cover: "",
    file: undefined, // 视频文件
    type: 0, // 0 自制，1转载
    tag: "",  // 分区通过一个字符串保存、使用,隔开
    intro: "",
    setTimeFlag: false, // 是否定时发布
    setTime: "", // 定时发布的时间
    setFolderFlag: false, // 是否加入合集
    setFolder: 0, // 加入的合集id
})
// 获取视频上传
const handleVideoUpload = (file) => {
    // 读取文件内容
    // let reader = new FileReader()
    if(file.size > acceptFileMaxSize) {
        alert('上传的视频文件大小超过限制（2GB）');
        return
    }
    upData.value.file = file
    return file
}
const handleVideoSuccess = (response, file, fileList) => {
    if(response.length <= 0) { // 错误
        ElMessage.error("上传视频失败")
    }
    let data = "data:image/jpg;base64," + response.data
    threeCoversData.value.push(data)
}
const beforeVideoRemove = () => {
    upData.value.cover
}
// 获取图片上传
const handleCoverUpload = (file) => {
    if(file.size > acceptCoverMaxSize) {
        alert('上传的图片文件大小超过限制（5MB）');
        return
    }
    return file
}
const handleCoverSuccess = (response, file, fileList) => {
    if(response.length <= 0) { // 错误
        ElMessage.error("图片上传错误")
    }
    upData.value.cover = file
    console.log("拿到了什么!", upData.value.cover)
}
const beforeCoverRemove = () => {

}
// 上传结果
const upRes = () => {
    // 数据结构
    const upFormData = new FormData()
    upFormData.append('file', upData.value.file)
    upFormData.append('intro', "测试中") // upData.value.intro
    upFormData.append('name', "夏至！")
    upFormData.append('userId', userId)
    upFormData.append('videoCover', upData.value.cover)
    // 上传逻辑
    const res = addUpVideo(upFormData)    
    console.log("上传是否成功？", res)
}
onMounted(()=>{
    
})
</script>

<style lang="scss">

</style>

<style lang="scss" scoped>
$share-font-size: 1.1rem; // 本页面的字体统一大小
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
    width: 39.5rem !important;
}
.video-uploader {
    width: calc(98%);
    height: 7rem;
    margin: 1rem 0;
}
.cover-uploader {
    width: 14rem;
    height: 8rem;
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
</style>