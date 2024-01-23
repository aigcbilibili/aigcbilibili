<template>
    <!--TODO：搜索结果页面，待改-->
    <div class="">
        <div>
            <li v-for="video in searchResults" :key="video.id"><!--使用videoCard-->
                <VideoCard />
            </li>
        </div>
    </div>
</template>

<script setup>
import VideoCard from '@/components/VideoCard'
return ({
    props: data
})
</script>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}

li {
  margin: 10px 0;
}
</style>