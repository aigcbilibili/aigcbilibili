<!--搜索时浮动的框-->
<template>
  <div class="search-panel-content based-box">
    <!--推荐-->
    <!-- <div v-if="historyTags.length!=0" class=""> -->
    <div class="search-area">
      <div v-if="!isInputFlag">
        <!--历史-->
        <div v-if="historyTags.length != 0" class="history-area">
          <p>☆* 历史搜索 *☆</p>
          <div class="flex-based-container" style="flex-wrap: wrap; margin-top: 0.5rem;">
            <div class="tags-and-labels history-item flex-center-container" v-for="(history_item, index) in historyTags"
              :key="index" @click="searchRes(history_item, 'history')">
              <p>{{ history_item }}</p>
              <p style="margin: 0 2px;">|</p>
              <p @click.stop="deletHistoryItem(0, index)">x</p>
            </div>
          </div>
          <div class="change-color-btn flex-center-container" style="margin: 0.8rem 0;"><!--清空历史记录-->
            <img src="@/assets/img/utils/rubbish.svg" style="width: 0.9rem; height: 0.9rem; margin-right: 0.2rem;" />
            <p class="delete-history" @click="deletHistoryItem(1, 0)">清空历史</p>
          </div>
        </div>
        <!--热点-->
        <!-- <div class="hot-area">
              <p>☆* 热点 *☆</p>
              <template class="hot-content">
                <div v-for="(item, index) in hotTest" :key="index" class="flex-based-container hot-item">
                  <div class="hot-item-info font-second-color" style="width:1rem;">{{item.id}}</div>
                  <div class="hot-item-info change-color-btn" @click="searchRes(item.name, 'hot')">{{item.name}}</div>
                  <div v-if="item.type!=0" class="hot-type hot-item-info">
                    <img :src="hotToIcon[item.type-1]" alt="item.type" style="height: 1.1rem; vertical-align:-0.25rem;" />
                  </div>
                </div>
              </template>
</div> -->
      </div>
      <div class="recommend-area" v-else>
        <div v-for="(item, index) in highLightRecData" :key="index" class="recommend-content flex-based-container">
          <p v-html="item" style="font-weight: 500; font-size: 1.1rem;"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, toRaw, defineProps, computed } from "vue"
import { useSearchKeys } from "@/store/searchKeys"
import { highlight } from "@/static/highlight"
import { useRouter } from 'vue-router'
const router = useRouter()
const keywordsStore = useSearchKeys()
const historyTags = ref(keywordsStore.getKeyWords())
const props = defineProps({
  isInput: {
    type: Boolean,
    required: true,
    default: false
  },
  keyword: {
    type: String,
    required: false,
    default: ''
  },
  recommendData: {
    type: Object,
    required: false,
    default: ['hello', 'world']
  }
})
const recommendData = computed({
  get: function () {
    return props.recommendData
  }
})
const isInputFlag = computed({ // 当前是否已经输入
  get: function () {
    return props.isInput
  }
})
const realKeyword = computed({ // 实时传递的keyword
  get: function () {
    return props.keyword
  }
})
const highLightRecData = computed({
  get: function () {
    let data = []
    const recData = recommendData.value
    if (Array.isArray(recData) && realKeyword.value) {
      recData.forEach(item => {
        const text_each = highlight(realKeyword.value, item)
        data.push(text_each)
      })
    }
    return data
  }
})
// 热点测试
const hotTest = [{
  id: 1,
  name: "幻兽帕鲁开发商新作爆料",
  type: 2 // common: 0, new: 1, hot: 2, live: 3
}, {
  id: 2,
  name: "新春联欢晚会",
  type: 3
}, {
  id: 3,
  name: "小小梦想家",
  type: 1
}, {
  id: 4,
  name: "研究表明本科生平均智商降低",
  type: 1
}, {
  id: 5,
  name: "哥哥回应妹妹8科成绩总分66",
  type: 2
}, {
  id: 6,
  name: "霍格沃茨之遗销量破2400万",
  type: 0
}, {
  id: 7,
  name: "退钱哥参加球迷亚洲杯",
  type: 0
}, {
  id: 8,
  name: "OMG LNG",
  type: 3
}, {
  id: 9,
  name: "河南发现1.07亿吨大油田",
  type: 1
}, {
  id: 10,
  name: "幻兽帕鲁破120亿",
  type: 2
}]
// 热点对应的图标
const hotToIcon = [require("@/assets/img/utils/new.png"), require('@/assets/img/utils/hot.png'), require('@/assets/img/live/on-live.gif')]
// NOTE 跳转，不能抽到公共组件
const searchRes = (keyword, fromSource) => {
  const routeURL = router.resolve({
    path: `/search/`,
    query: {
      keyword: keyword,
      from_source: fromSource
    } // NOTE params是路由的一部分,必须要在路由后面添加参数名。query是拼接在url后面的参数，没有也没关系。
  })
  window.open(routeURL.href, '_blank')
}
// 删除历史记录
const deletHistoryItem = (type, id) => { // type：0是单独，1是所有
  if (type === 0) {
    historyTags.value.splice(id, 1)
    keywordsStore.deleteOneKeyword(id)
  } else {
    historyTags.value = []
    keywordsStore.deletAllKeywords()
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/main.scss";

.search-panel-content {
  min-width: 25rem;
  min-height: 10rem;
  border-radius: 20px;
  height: auto;
  background: #ffffff;
  z-index: 30;
}

.search-panel-content p {
  font-size: 1.2rem;
  font-weight: 700;
  color: #2a6490;
}

.search-area {
  min-height: calc(45%);
  height: auto;
  padding: 0.6rem;
}

.history-area {
  min-height: 4.5rem;
  height: auto;
}

.hot-area {
  margin-bottom: 0.5rem;
}

.hot-item-info {
  margin-right: 1rem;
  margin-top: 0.5rem;
}

.hot-content {
  margin-top: 0.5rem;
  display: flex;
  flex-flow: column nowrap;
}

.history-item {
  width: auto;
  height: auto;
  padding: 0.2rem 0.8rem;
  border-radius: 10px;
  margin-right: 0.5rem;
  margin-bottom: 0.3rem;
  cursor: pointer;

  p {
    color: #ffffff;
    font-size: 0.9rem;
    font-weight: 500;
  }
}

.recommend-area {
  padding: 0.5rem;
}

.delete-history {
  font-size: 0.9rem !important;
}

@media screen and (min-width: 1020px) {
  .hot-content {
    display: flex;
    flex-flow: row wrap !important;

    .hot-item {
      width: 19rem;
    }
  }
}
</style>