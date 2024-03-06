<template>
    <!--论坛页面-->
    <div class="left-right-panel forum-page">
        <!--论坛左侧页面-->
        <div class="left-content flex-column-center-container">
            <!--论坛分类-->
            <div class="forum-category">
                <!--一级分类-->
                <div class="second-category flex-based-container">
                    <div v-for="item in typeList" :key="item.id" :class="{'tags-and-labels':item.isChosen}"
                    class="second-category-item detail-btn-chosen common-based-btn">
                        {{item.name}}
                    </div>
                </div>
                <!--二级分类-->
                <div class="first-category">
                    <div v-for="subItem in currentSecondList" :key="subItem.id"  class="first-category-item change-color-btn">
                        {{subItem.name}}
                    </div>
                </div>
            </div>
            <!--论坛帖子-->
            <div class="forum-content">
                <div v-for="(post,index) in postData" :key="index">
                    <div class="flex-based-container">
                        <p>{{post.title}}</p>
                        <p>{{post.author}}</p>
                    </div>
                </div>
            </div>
        </div>
        <!--右侧管理-->
        <div class="right-content">
            <!--上传帖子和我的帖子-->
            <div class="forum-manage flex-center-container">
                <div class="forum-manage-item common-btn-center">发 布</div>
                <div class="forum-manage-item common-btn-center">我的帖子</div>
            </div>
            <!--签到部分-->
            <signVue />
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, defineAsyncComponent } from "vue"
const signVue = defineAsyncComponent(()=>
    import ("@/components/Calendar.vue")
)
const typeList = [{
    id: 1,
	icon: "",
    background: "",
	name: "综合热门",
	children: [{
		id: 1,
        icon: "",
        name: "最近一周",
    },{
		id: 2,
        icon: "",
        name: "测试测试",
    },{
		id: 3,
        icon: "",
        name: "如果说",
    }]
},{
	id: 2,
	icon: "",
    background: "",
	name: "综合热门",
	children: [{
		id: 1,
        icon: "",
        name: "最近一周",
    }]
}]
const currentSecondList = ref(typeList[0].children) // 二级分类的效果
const postData = ref([{
    id: 32371,
    avatar: "",
    author: "麻辣咸鱼",
    title: "你好",
    content: "",
    createTime: "2024-2-4 15:33",
    likeNum: 32,
    collectNum: 57,
    commentNum: 100 
},{
    id: 9191,
    avatar: "",
    author: "LA官方组",
    title: "地球",
    content: "",
    createTime: "2024-1-1 00:00:00",
    likeCount: 455,
    collectCount: 890,
    commentNum: 999  
}])
onMounted(()=>{
    
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/category.scss";
.forum-page {
    width: calc(96%);
    height: calc(100%);
}
.forum-manage {
    
}
</style>