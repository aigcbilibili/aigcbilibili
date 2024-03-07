<template>
    <div class="search-page">
        <!--搜索框-->
        <SearchPanel class="search-area flex-based-container" :fromSource="'searchPage'" :getRes="keyword"/>
        <!--结果分类-->
        <!--一级-->
        <div class="first-category flex-based-container">
            <div v-for="item in searchCategory" :key="item.id" 
            class="first-category-item change-color-btn" @click="changeChosenItem(searchCategory,item.id)">
                <p class="item-text" :class="{'item-chosen':item.isChosen}">{{item.name}}</p>
            </div>
        </div>
        <div class="horizontal-divided-line search-line"></div>
        <!--二级分类-->
        <div class="second-category flex-based-container">
            <div v-for="item in secondCatgory" :key="item.id" 
            class="detail-btn-chosen common-based-btn second-category-item"
            :class="{'tags-and-labels':item.isChosen}" @click="changeChosenItem(secondCatgory,item.id)">
                <p>{{item.name}}</p>
            </div>
        </div>
        <!--结果-->
        <div v-if="currentIndex===0">
            <template class="video-box" style="margin-top: 2rem;">
                <video-small-wrap v-for="(item, index) in videoSearchData" :key="index" >
                    <VideoCard :videoInfo="item" />
                </video-small-wrap>
            </template>
        </div>
        <div v-else style="margin-top: 2rem;">
            <div v-for="(item, index) in userSearchData" :key="index">
                <userRectangleVue :isBgShow="false" />
            </div>
        </div>
        <!--分页控件：total总数据量，page-size每页条数，current-page当前页数-->
        <div class="pagination-panel flex-center-container">
            <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageInfo.pageSize"
            :small="small"
            :disabled="disabled"
            :background="background"
            layout="total, prev, pager, next, jumper"
            :total="pageInfo.totalNum"
            @current-change="handleCurrentChange"
            />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch, defineAsyncComponent } from "vue"
import { useRoute } from "vue-router"
import { fetchVideoResNum, fetchVideoSearchRes } from "@/api/search"
import VideoCard from '@/components/video/VideoCard'
import SearchPanel from '@/components/search/SearchPanel.vue'
import VideoDetailList_page from '@/assets/data/videoViewList_page.json'
const videoSearchData = ref([]) // 获取到的搜索结果
const userSearchData = ref([]) // 获取到的用户结果
const currentIndex = ref(0) // 当前的一级分类序号
const currentSecondIndex = ref(0) // 当前的二级分类序号
const secondCatgory = ref([]) // 当前二级分类数组
const currentPage = ref(1) // 当前分页的数据页数
const userRectangleVue = defineAsyncComponent(()=>{
    import ("@/components/user/UserInfo.vue")
})
const pageInfo = ref({
    pageSize: 16,
    totalNum: 1000
}) // 分页相关的数据
// 获取router的query参数
const route = useRoute()
const keyword = route.query.keyword

// 分类
const searchCategory = ref([{
    id: 0,
    name: '视频',
    isChosen: true,
    children: [{
        id: 0,
        name: '综合排序',
        isChosen: true,
        type: 1, // 为了让Url设置的
    },{
        id: 1,
        name: '最多播放',
        isChosen: false,
        type: 2, // 为了让Url设置的
    },{
        id: 2,
        name: '最新发布',
        isChosen: false,
        type: 3, 
    },{
        id: 3,
        name: '最多弹幕',
        isChosen: false,
        type: 4, 
    },{
        id: 4,
        name: '最多收藏',
        isChosen: false,
        type: 5, 
    }]
},{
    id: 1,
    name: '用户',
    isChosen: false,
    children: [{
        id: 0,
        name: '默认排序',
        isChosen: true,
        type: 1, // 为了让Url设置的
    },{
        id: 1,
        name: '粉丝数由低到高',
        isChosen: false,
        type: 2, // 为了让Url设置的
    },{
        id: 2,
        name: '粉丝数由高到低',
        isChosen: false,
        type: 3, // 为了让Url设置的
    }]
}])

// 获取数据
const getResData = async(page) =>{
    const category_single = searchCategory.value[currentIndex.value]
    const askUrl_single = category_single.children[currentSecondIndex.value].askUrl
    const tmp = await fetchVideoSearchRes(askUrl_single, keyword, page)
    // json测试数据
    // TODO 修改条件
    if(tmp.length!=0){ // 真实数据存在
        pageInfo.value.pageSize = tmp.pageSize
        pageInfo.value.totalNum = tmp.pageNum
        return tmp.data
    }else{
        if(currentIndex.value === 0){
            return VideoDetailList_page['data']
        }else{
            return []
        }
    }
}
// 修改一级分类
const changeChosenItem = async(array, id) => {
    array.forEach(category => 
        category.isChosen = false
    )
    array[id].isChosen = true
    if(array === searchCategory.value){ // 如果是一级分类，进入以下
        currentIndex.value = id
        currentSecondIndex.value = 1
    }else{
        currentSecondIndex.value = id
    }
    // 获取新数据
    if(currentIndex.value === 0){ // 视频
        videoSearchData.value = await getResData(1)
    }else{

    }
}
// 获取二级分类
const getSecondCategory = () => {
    return searchCategory.value[currentIndex.value].children
}
// 修改页码
const handleCurrentChange = async(val) => {
    currentPage.value = val
    videoSearchData.value = await getResData(val)
}
// 获取页码信息
const getPageInfo = () => {
    const lenData = fetchVideoResNum(keyword)
    console.log("看看", lenData)
}
// 监听 currentIndex 变化，并在变化后获取二级分类
watch(currentIndex, (newIndex, oldIndex) => {
    if(newIndex!=oldIndex){
        secondCatgory.value = getSecondCategory()
    }
})
onMounted(() => {
    nextTick(async()=>{
        getPageInfo() // 
        // 获取数据
        videoSearchData.value = await getResData(1) // NOTE 这里要用await
        secondCatgory.value = getSecondCategory()
    })
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/videoBox.scss";
@import "@/assets/css/category.scss";
$left-distance: 2rem;
.search-area {
    min-width: 33rem;
    margin-top: 3rem;
    margin-left: $left-distance;
}
.search-line{
    margin-left: 2.5%;
    width: 95%;
    margin-top: 0.3rem;
}
.pagination-panel{
    margin-top: -6rem;
    margin-bottom: 3rem;
}
</style>