<template>
    <div id="file">
        <div id="up_file" style="cursor: pointer;width: 100%;height: 100%;line-height: 200px;" v-show="status === 0">
            点击上传视频文件
        </div>
        <div id="up_progress" style="width: 80%;" v-show="status === 1">
            <el-progress :percentage="percentage" />
            <el-button style="margin-top: 30px;" @click="stopUpload">暂停</el-button>
            <el-button style="margin-top: 30px;" @click="staUpload">继续</el-button>
        </div>
        <div id="up_success" style="width: 80%;" v-show="status === 2">
            <div>上传成功</div>
            <el-button style="margin-top: 30px;" @click="againUpload">重新上传</el-button>
        </div>
        <div id="up_error" style="width: 80%;" v-show="status === 3">
            <div>上传失败</div>
            <el-button style="margin-top: 30px;" @click="againUpload">重新上传</el-button>
        </div>

    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Resumable from 'resumablejs';

import { ElMessage } from 'element-plus'

const resumable = ref(null);
const status = ref(0) //0:未开始上传，1：上传中，2：上传成功，3：上传失败
const percentage = ref(0)
const stopUpload = () => {
    resumable.value.pause();
}

const staUpload = () => {
    resumable.value.upload();
}

const againUpload = () => {
    resumable.value.removeFile();
    status.value = 0
    emit('handleFile', [], true)
}

const emit = defineEmits(['handleFile'])

onMounted(() => {
    resumable.value = new Resumable({
        target: '/api/createCenter/uploadPart', // 上传的服务器地址
        chunkSize: 6 * 1024 * 1024, // 分块大小，这里是6MB
        testChunks: true, // 在上传前测试分块是否已存在
        maxFiles: 1, // 一次只能上传一个文件
        // concurrentUploads: 3, 
        simultaneousUploads: 4, // 同时上传的分片数
        generateUniqueIdentifier: (file) => {
            return `${file.name}-${file.size}-${file.lastModified}`
        },
        testTarget: "/api/createCenter/getProcessor",
        // preprocess: (file) => {
        //     console.log('file', file);
        // }
    });
    console.log(resumable.value);
    resumable.value.assignBrowse(document.getElementById('up_file'));
    // 监听文件添加事件
    resumable.value.on('fileAdded', (file) => {
        console.log('file', file);
        const maxSize = 500 * 1024 * 1024
        if (file.file.size > maxSize) {
            return ElMessage.error('文件大小不能超过500M')
        }
        if (file.file.type !== "video/mp4") {
            return ElMessage.error('文件格式不正确')
        }
        // 当文件添加后开始上传
        resumable.value.upload();
        status.value = 1
        percentage.value = 0

    });

    // 监听上传进度事件
    resumable.value.on('progress', () => {
        // 可以在这里获取上传进度
        console.log('progress', resumable.value)
        const progress = (resumable.value.progress() * 100).toFixed(2);
        // console.log(`Progress: ${progress}%`);
        percentage.value = progress
    });

    // 监听分片测试结果
    resumable.value.on('chunkTest', (chunk) => {
        // axios.get('/api/getProcessor', {
        //     params: {
        //         resumableIdentifier: chunk.uniqueIdentifier,
        //         resumableFilename: chunk.fileName,

        //     }
        // }).then((res) => {
        //     console.log('res', res);
        // }).catch((err) => {
        //     console.log('error', err);
        // })
        console.log('chunk', chunk);

    });

    // 监听完成事件
    resumable.value.on('fileSuccess', (file, data) => {
        status.value = 2
        emit('handleFile', JSON.parse(data).data)
    });
    // 监听错误事件
    resumable.value.on('fileError', (error) => {
        status.value = 3
        console.error('Upload error:', error);
    });
    resumable.value.on('complete', (file) => {
        console.log('complete', file);
    })
});


function startUpload(event) {
    // 当文件选择变化时，开始上传
    resumable.value.addFile(event.target.files[0]);
}
</script>



<style scoped lang="scss">
#file {
    width: 100%;
    height: 200px;
    // background-color: aqua;
    border: 1px dashed rgb(73, 72, 72);
    border-radius: 10px;
    display: flex;
    // flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;

    align-items: center;

    &:hover {
        border: 1px dashed rgb(63, 146, 230);
    }

    .upload-box {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        flex-direction: column;
    }

}
</style>