<template>
    <div class="search-panel">
      <input class="search-input" type="text" @mouseover="ifSearch=true"
         @mouseout="ifSearch=false" @input="getCurrentRes()" v-model="searchRes"
         placeholder="请输入视频标题/UP主名称"/>
      <button class="search-btn">
        <div class="search-btn-font"  @click="searchFinal()">搜索</div>
          <Transition name="search">
            <searchView v-if="ifSearch"  />
          </Transition>
      </button>
    </div>
</template>

<script setup>
import { ref, defineAsyncComponent } from 'vue'
const ifSearch = ref(false) // 是否进入搜索栏
const searchRes = ref('') // input框中的数据
const searchView = defineAsyncComponent(()=>
  import ('./SearchView') // XXX 这个报错不用管，能用
)

/**
 * 实时获取当前输入
 */
const getCurrentRes = ()=>{
    ifSearch.value = true
    // 更新推荐内容
    if(searchRes){
        console.log('进入推荐')
    }
}

/**
 * 搜索最终结果
 */
const searchFinal = () => {
    // 判断是
}


</script>
<style scoped>
.search-input{
  margin-left: 1rem;
  width: 30rem;
  height: 2.5rem;
  border-color: rgb(36, 144, 179);
  border-radius: 20px;
  font-size: 1rem;
  padding-left: 1rem;
}
.search-input:focus{
    border-color: #56509c;
}
.search-btn, .search-btn:visited{
  height: 3rem;
  width: 5rem;
  margin-left: 2rem;
  margin-right: 2rem;
  border: none;
  border-radius: 10px;
  flex: 1 auto 100%;
  background: #79b1ec;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: row wrap;
  font-size: 1.4rem;
}
.search-btn-font{
  height: 2.2rem;
  font-weight: 500;
  overflow: hidden;
  color: #ffffff;
}
.search-btn:hover{
  background: #467cc6;
}
.search-btn:active{
  background: #19449a;
  font-size: 1.4rem;
  transition: 0.05s;
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
}
.search-enter-active, .search-leave-active{
  transition: opacity 0.3s ease;
}
.search-enter-from, .search-leave-to{
  opacity: 0;
}

</style>