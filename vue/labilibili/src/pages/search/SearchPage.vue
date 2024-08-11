<template>
    <div class="search-page">
        <!--搜索框-->
        <SearchPanel class="search-area flex-based-container" :fromSource="'searchPage'" :getRes="keyword" />
        <!--结果分类-->
        <!--一级分类-->
        <div class="first-category flex-based-container">
            <div v-for="item in searchCategory" :key="item.id" class="first-category-item change-color-btn"
                @click="changeChosenItem(searchCategory, item.id)">
                <p class="item-text" :class="{ 'item-chosen': item.isChosen }">{{ item.name }}</p>
            </div>
        </div>
        <div class="horizontal-divided-line search-line"></div>
        <!--二级分类-->
        <div class="second-category flex-based-container">
            <div v-for="item in secondCatgory" :key="item.id"
                class="detail-btn-chosen common-based-btn second-category-item"
                :class="{ 'tags-and-labels': item.isChosen }" @click="changeChosenItem(secondCatgory, item.id)">
                <p>{{ item.name }}</p>
            </div>
        </div>
        <!--结果-->
        <div v-if="currentIndex === 0">
            <div v-if="videoSearchData && videoSearchData.length > 0" class="video-search-wrap">
                <video-small-wrap v-for="(item) in videoSearchData" :key="item">
                    <VideoCard :videoInfo="item" style="margin-bottom: 2rem;position: relative;" />
                </video-small-wrap>
            </div>
            <div v-else class="flex-center-container" style="width: 100%">
                <img src="@/assets/img/utils/noData.png" style="width: 20rem; height: auto;" />
            </div>
        </div>
        <div v-else style="margin-top: 2rem;min-height: 15rem;">
            <div v-if="userSearchData && userSearchData.length > 0" class="flex-column-center-container">
                <div v-for="(item) in userSearchData" :key="item">
                    <userRectangleVue :isBgShow="false" :upId="item.id" :upInfo="item"
                        style="width: calc(80vw);height: 7rem; margin-bottom: 1rem;" />
                </div>
            </div>
            <div v-else class="flex-center-container" style="width: 100%">
                <img src="@/assets/img/utils/noData.png" style="width: 20rem; height: auto;" />
            </div>
        </div>
        <!--分页控件：total总数据量，page-size每页条数，current-page当前页数-->
        <div class="pagination-panel flex-center-container">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageInfo[currentIndex].pageSize"
                :small="small" :disabled="disabled" :background="background" layout="total, prev, pager, next, jumper"
                :total="pageInfo[currentIndex].totalNum" @current-change="handleCurrentChange" />
        </div>
    </div>
</template>

<script setup>
import SearchPanel from '@/components/search/SearchPanel'
import { ref, onMounted, watch, defineAsyncComponent } from "vue"
import { fetchVideoResNum, fetchVideoResFirst, fetchVideoResSecond, addSearchHistory } from "@/api/search"
import { useUserInfo } from "@/store/userInfo"
import { useRoute } from "vue-router"
import { ElMessage } from "element-plus"
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const videoSearchData = ref([]) // 获取到的搜索结果
const userSearchData = ref([]) // 获取到的用户结果
const currentIndex = ref(0) // 当前的一级分类序号
const currentSecondIndex = ref(0) // 当前的二级分类序号
const secondCatgory = ref([]) // 当前二级分类数组
const currentPage = ref(1) // 当前分页的数据页数
const VideoCard = defineAsyncComponent(() =>
    import("@/components/video/VideoCard")
)
const userRectangleVue = defineAsyncComponent(() =>
    import("@/components/user/UserInfo.vue")
)
let twoTypePageInfo = { // 两类
    total_user_num: 0,
    total_user_page: 0,
    total_video_num: 0,
    total_video_page: 0
}
const pageInfo = ref([{ // 0 -视频
    pageSize: 16,
    totalNum: 1000
}, {
    pageSize: 16,
    totalNum: 500
}]) // 分页相关的数据
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
    }, {
        id: 1,
        name: '最多播放',
        isChosen: false,
    }, {
        id: 2,
        name: '最新发布',
        isChosen: false,
    }, {
        id: 3,
        name: '最多弹幕',
        isChosen: false,
    }, {
        id: 4,
        name: '最多收藏',
        isChosen: false,
    }]
}, {
    id: 1,
    name: '用户',
    isChosen: false,
    children: [{
        id: 0,
        name: '默认排序',
        isChosen: true,
    }, {
        id: 1,
        name: '粉丝数由低到高',
        isChosen: false,
    }, {
        id: 2,
        name: '粉丝数由高到低',
        isChosen: false,
    }]
}])
// 获取二级分类
const getSecondCategory = () => {
    return searchCategory.value[currentIndex.value].children
}
// 获取数据
const getResData = async () => {
    if (currentIndex.value === 0) {
        // 获取视频
        videoSearchData.value = await fetchVideoResFirst(keyword, currentPage.value, currentSecondIndex.value + 1)
    } else {
        // 获取用户
        userSearchData.value = await fetchVideoResSecond(keyword, currentPage.value, currentSecondIndex.value + 1, userId)
        console.log(`如何：${JSON.stringify(userSearchData.value)}`)
    }
}
// 修改分类
const changeChosenItem = async (array, id) => {
    array.forEach(category =>
        category.isChosen = false
    )
    array[id].isChosen = true
    if (array === searchCategory.value) { // 如果是一级分类，进入以下
        currentIndex.value = id
        currentSecondIndex.value = 1
    } else { // 如果是二级分类
        currentSecondIndex.value = id
    }
    // 获取新数据
    currentPage.value = 1
    await getResData()
}
// 修改页码
const handleCurrentChange = async (val) => {
    if (val > pageInfo.value[currentIndex.value].pageSize || val < 1) {
        ElMessage.error("输入页码超出限制")
    }
    currentPage.value = val
    await getResData(currentPage.value) // 更新数据渲染
}
// 获取页码信息：初始时
const getPageInfo = async () => {
    const lenData = await fetchVideoResNum(keyword)
    await addSearchHistory(keyword)
    twoTypePageInfo = lenData
    pageInfo.value[0].pageSize = Math.ceil(twoTypePageInfo.total_video_num / twoTypePageInfo.total_video_page)
    pageInfo.value[0].totalNum = twoTypePageInfo.total_video_num
    pageInfo.value[1].pageSize = Math.ceil(twoTypePageInfo.total_user_num / twoTypePageInfo.total_user_page)
    pageInfo.value[1].totalNum = twoTypePageInfo.total_user_num
}
// 监听 currentIndex 变化，并在变化后获取新的二级分类
watch(currentIndex, () => {
    secondCatgory.value = getSecondCategory()
})
onMounted(async () => {
    await getPageInfo() // 首先获取页面数据
    // 默认视频数据
    if (pageInfo.value[0].totalNum !== 0) {
        await getResData()
    }
    secondCatgory.value = getSecondCategory() // 初始化渲染
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

.video-search-wrap {
    margin-top: 2rem;
    min-width: 100%;
    min-height: 80%;
    margin-left: 2%;
    display: grid;
    grid-template-columns: repeat(auto-fill, 47%);
    grid-gap: 0 2%;
}

.search-line {
    margin-left: 2.5%;
    width: 95%;
    margin-top: 0.3rem;
}

.user-box {
    min-height: 20rem;
}

.pagination-panel {
    margin-top: 1rem;
    margin-bottom: 3rem;
}

@media screen and (min-width: 1020px) {
    .video-search-wrap {
        grid-template-columns: repeat(auto-fill, 22.5%);
    }
}
</style>