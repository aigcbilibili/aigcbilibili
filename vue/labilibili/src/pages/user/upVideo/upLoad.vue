<template>
    <div>
        <input type="file" @change="startUpload" />
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Resumable from 'resumablejs';

const resumable = ref(null);

onMounted(() => {
    resumable.value = new Resumable({
        target: '/api/createCenter/getVideoCover', // 上传的服务器地址
        chunkSize: 1 * 1024 * 1024, // 分块大小，这里是1MB
        testChunks: true, // 在上传前测试分块是否已存在
    });

    // 监听文件添加事件
    resumable.value.on('fileAdded', (file) => {
        // 当文件添加后开始上传
        resumable.value.upload();
    });

    // 监听上传进度事件
    resumable.value.on('progress', () => {
        // 可以在这里获取上传进度
        const progress = (resumable.value.progress() * 100).toFixed(2);
        console.log(`Progress: ${progress}%`);
    });

    // 监听完成事件
    resumable.value.on('complete', () => {
        console.log('Upload completed.');
    });
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