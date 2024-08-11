<template>
  <div class="search-panel" @mouseleave="isShow = false">
    <div class="search-wrap">
      <input class="search-input" type="text" placeholder="请输入视频标题/UP主名称" @mouseover="isShow = true" v-model="searchRes"
        @input="isShow = true" @keyup.enter="searchFinal()" />
      <Transition name="search">
        <searchView v-if="isShow" class="search-recommend" :isInput="isInput" :keyword="searchRes"
          :recommendData="recommendData" />
      </Transition>
    </div>
    <button class="search-btn flex-center-container">
      <div class="search-btn-font" @click="searchFinal()">搜索</div>
    </button>
  </div>
</template>

<script setup>
import { ref, defineAsyncComponent, defineProps, onMounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchKeys } from "@/store/searchKeys"
import { useRefreshToken } from "@/store/token"
import { ElMessage } from "element-plus"
import axios from "axios"
import Debounce from "@/static/debounce"
const tokenStore = useRefreshToken() // 存储的token实例
const isShow = ref(false) // 是否点击搜索栏
const isInput = ref(false) // 是否正在输入
const searchRes = ref() // input框中的数据
const keywordsStore = useSearchKeys()
const searchView = defineAsyncComponent(() =>
  import('./SearchView')
)
const debounce = new Debounce() // 防抖
const recommendData = ref([]) // 推荐数据
const router = useRouter()
const props = defineProps({
  type: {
    type: String, // video, shop, chat
    required: false,
    default: "video"
  },
  fromSource: {
    type: String,
    required: true,
    default: 'mainPage'
  },
  getRes: {
    type: String,
    required: false,
    default: ''
  }
})
/**
 * 实时获取当前输入
 */
// 获得联想推荐
const getRecommend = async (keyword) => {
  await debounce.debounceEnd(5)
  if (keyword !== "") {
    // XXX 正常而言
    // const res = await fetchVideoRelatedKeywords(keyword) 
    axios.get(`/api/search/likelyKeyWordSearch/${keyword}`, {
      keyword: keyword
    }, {
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'shortauthorization': tokenStore.getData(),
        'laBiliBiliHeader': 'test_method_1',
        'Access-Control-Allow-Origin': '*'
      }
    }).then(
      response => {
        recommendData.value = response.data.data
      }).catch(error => console.error('Error loading captcha:', error))
  }
}
watch(searchRes, (newValue, oldValue) => {
  if (newValue !== '') {
    isInput.value = true
    recommendData.value = getRecommend(newValue)
  } else {
    isInput.value = false
  }
})

/**
 * 搜索最终结果
 */
const searchFinal = () => {
  if (!searchRes.value) {
    ElMessage.error("请输入内容！")
    return
  }
  // 保存keywords
  keywordsStore.setKeywords(searchRes.value)
  const routeURL = router.resolve({
    path: `/search/`,
    query: {
      keyword: searchRes.value,
      from_source: props.fromSource
    } // NOTE params是路由的一部分,必须要在路由后面添加参数名。query是拼接在url后面的参数，没有也没关系。
  })
  window.open(routeURL.href, '_blank')
}
onMounted(() => {
  // fetchVideoRelatedKeywords(" ") // 首次测试链接
  // 获取事先的输入
  nextTick(() => {
    if (props.getRes) {
      searchRes.value = props.getRes
    }
  })
})
</script>

<style lang="scss" scoped>
$search-outline-color: rgb(36, 144, 179);
$detail-first-color: #79b1ec;
$textarea-focus-color: #093957;

.search-wrap {
  position: relative;

  .search-recommend {
    position: absolute;
    top: 3.5rem;
    left: 1rem;
    min-width: 21rem;
    width: auto;
    z-index: 99;
  }
}

.search-input {
  margin-left: 1rem;
  width: 20rem;
  height: 2.5rem;
  outline: none;
  border: 1px solid $detail-first-color;
  // border-color: $search-outline-color; /* 使用本条虽然能实现gradient效果，但focus时会偏移 */
  font-size: 1rem;
  color: $textarea-focus-color;
  padding-left: 1rem;
}

input:focus {
  outline: 1.5px solid $search-outline-color;
}

.search-btn,
.search-btn:visited {
  height: 3rem;
  width: 5rem;
  margin-left: 2rem;
  margin-right: 2rem;
  border: none;
  border-radius: 10px;
  flex: 1 auto 100%;
  background: #79b1ec;
  font-size: 1.4rem;
  overflow: hidden;
}

.search-btn-font {
  height: 2.2rem;
  font-weight: 500;
  overflow: hidden;
  color: #ffffff;
}

.search-btn:hover {
  background: #467cc6;
}

.search-btn:active {
  background: #19449a;
  font-size: 1.4rem;
  transition: 0.05s;
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
}

.search-enter-active,
.search-leave-active {
  transition: opacity 0.3s ease;
}

.search-enter-from,
.search-leave-to {
  opacity: 0;
}

@media screen and (min-width:1020px) {
  .search-input {
    width: 39rem;
  }

  .search-wrap {
    .search-recommend {
      width: 40rem;
    }
  }
}

.search-panel {
  margin: auto;
}
</style>