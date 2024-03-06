<!--收藏夹-->
<template>
  <div style="width:30rem;height:40rem;position:relative;">
  <el-dialog
    v-model="isShow"
    :show-close="true"
    style="
      width:30rem;
      height:40rem;
      background-color: #fff !important;
      box-shadow: -5px 2px 25px 0 #79b1eca9 !important;
      border-radius: 10px !important;
    "
  >
    <template #header>
      <div class="title self-center-box-second">收藏夹</div>
    </template>
    <template #default>
      <div class="collect-content flex-column-center-container">
          <div v-if="collectData && collectData.length > 0">
            <el-scrollbar style="height: 95%;">
              <div v-for="(item, index) in collectData" :key="index" class="collect-item flex-between-container">
                <div class="flex-center-container">
                  <el-checkbox v-model="item.checked" class="collect-item-checkbox">
                  <p class="collect-item-title">{{item.name}}</p> 
                  </el-checkbox>
                </div>
                <div class="flex-center-container">
                  <p class="collect-item-num" style="font-weight: 600;">{{item.num}}</p>
                  <p>/{{eachTotalNum}}</p>
                </div>
              </div>
            </el-scrollbar>
          </div>
          <div v-else>
            <img src="@/assets/img/utils/noData.png" />
          </div>
      </div>
    </template>
    <template #footer>
      <div class="dialog-footer flex-column-center-container">
        <div class="add-wrap flex-center-container common-based-btn" @click="isNewCollection=true">
          <img src="@/assets/img/utils/plus.svg" />
        </div>
        <div class="true flex-center-container detail-btn common-based-btn" @click="onSubmit()">确定</div>
      </div>
    </template>
  </el-dialog>
  <!--自定义弹窗：创建重新-->
  <div v-if="isNewCollection" class="collection-mask self-center-box-first flex-center-container" @click="handleMaskClick">
    <newCollectionVue />
  </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, defineAsyncComponent, ref, computed, onMounted } from 'vue'
import { fetchCollection } from "@/api/like_and_collect"
import { useUserInfo } from "@/store/userInfo"
import collectFolder from '@/assets/data/collectFolder'
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const isNewCollection = ref(false) // 是否新建文件夹
const collectData = ref([]) // 收藏夹数据
const eachTotalNum = ref(0)
const newCollectionVue = defineAsyncComponent(()=>
  import ("./newCollection.vue")
)
const props = defineProps({
  isCollected: {
    type: Boolean,
    required: true,
    default: false
  }
})
const emits = defineEmits(['update:isCollected', 'onChangeDialog']);
const isShow = computed({ // 只读，不能实时修改；但使用computed会比ref快
    get:function(){
      return props.isCollected
    },
    set:function(val) {
      emits('update:isCollected', val)
    }
})

const onSubmit = (val) => {
  emits('onChangeDialog', true) 
  // 将收藏结果上传到后端

  // 退出页面
  isShow.set(false)
}

onMounted(()=>{
  collectData.value = fetchCollection(userId)
  eachTotalNum.value = collectFolder["eachLength"]
})
</script>

<style lang="scss">
.el-dialog__header {
  margin-right: 0;
}
</style>

<style lang="scss" scoped>

.title {
  color: #000;
  font-size: 20px;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
}
.collect-item {
  font-size: 1.2rem;
  width: 27rem;
  height: 2.5rem;
  .collect-item-checkbox {
    margin-top: 0.3rem; 
    margin-right: 0.5rem;
    width: 1rem;
    height: 1rem;
  }
  .collect-item-title {
    font-weight: 600;
  }
}
.collect-content {
  margin-top: 1.5rem;
}
.dialog-footer {
  position: absolute;
  bottom: 1rem;
  width: 90%;
  .add-wrap {
    width: 100%;
    height: 3rem;
    border-radius: 5px;
    margin-bottom: 1rem;
    background-color: #bfd7f0;
  }
  .true {
    width: 5rem;
    height: 2rem;
  }
}
.collection-mask {
  width:30rem;
  height:40.1rem;
  top: -0.2rem !important;
  position: fixed;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明黑色背景 */
  z-index: 999999999; /* 置于其他内容之上 */
  border-radius: 10px;
}
</style>