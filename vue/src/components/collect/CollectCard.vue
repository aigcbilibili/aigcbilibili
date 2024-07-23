<!--收藏夹-->
<template>
  <div style="width:30rem;height:40rem;position:fixed">
  <el-dialog
    v-model="isShow"
    :show-close="true"
    :before-close="handleClose"
    :key="componentKey"
    style="
      width:30rem;
      height:40rem;
      background-color: #fff !important;
      box-shadow: -5px 2px 25px 0 #79b1eca9 !important;
      border-radius: 10px !important;
    "
  >
    <template #header>
      <div class="title self-center-box-second">
        <p v-if="isCollected">收藏夹</p>
        <p v-else>视频合集</p>
      </div>
    </template>
    <template #default>
      <el-scrollbar style="height: 95%;">
      <div class="collect-content flex-column-center-container">
          <div v-if="collectData && collectData.length > 0">
            <div v-for="(item, index) in collectData" :key="index"
            class="flex-left-container collect-flex-wrap">
              <div v-if="isVideoDetail">
                <input :id="item.id" type="checkbox" name="collect" 
                style="display: flex; margin-right: 1.5rem;"  class="checkCollect" :data-id="item.id" />
              </div>
              <collectionItem class="collect-item flex-between-container" :data="item" @updataEdit = 'updataEdit' @deleteData="deleteData" >
                <div class="flex-center-container">
                  <el-checkbox v-model="item.checked" class="collect-item-checkbox">
                  <p class="collect-item-title">{{item.name}}</p> 
                  </el-checkbox>
                </div>
                <div class="flex-center-container">
                  <p class="collect-item-num" style="font-weight: 600;">{{item.num}}</p>
                  <p>/{{eachTotalNum}}</p>
                </div>
              </collectionItem>
            </div>
          </div>
          <div v-else>
            <img src="@/assets/img/utils/noData.png" />
          </div>
      </div>
    </el-scrollbar>
    </template>
    <template #footer>
      <div class="dialog-footer flex-column-center-container">
        <div class="add-wrap flex-center-container common-based-btn" 
        @click="showDom('add')">
          <img src="@/assets/img/utils/plus.svg" />
        </div>
        <div class="true flex-center-container detail-btn common-based-btn" 
        @click="onSubmit">确定</div>
      </div>
    </template>
  </el-dialog>
  <!--自定义弹窗：创建重新-->
  <div v-show="isNewCollection" 
  class="collection-mask self-center-box-first 
  flex-center-container" @click="handleMaskClick">
    <newCollectionVue class="createNewCollection" @click.stop 
    @update:isShow="upNewCollection()" :key="collectKey" ref="CollectionVue"  @getData='getData'/>
  </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, defineAsyncComponent, 
  ref, computed, onMounted, provide, 
nextTick} from 'vue'
import { fetchCollection, editVideoToCollect, fetchVideoInCollect,deleteCollections } from "@/api/like_and_collect"
import { useUserInfo } from "@/store/userInfo"
import collectFolder from '@/assets/data/collectFolder'
import { ElMessage } from 'element-plus'

const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const isNewCollection = ref(false) // 是否新建文件夹
const collectData = ref([]) // 收藏夹数据
const currentToCollection = ref([]) // 当前视频被哪些收藏夹，videoId对应collectionId
const eachTotalNum = ref(0)
const collectKey = ref(0)

let CollectionVue = ref()

const showDom=(type,row) =>{
  isNewCollection.value = true
  if(type == 'add'){
    nextTick(()=>{
      CollectionVue.value.clearData()
    })
  }else if(type === 'edit'){
    // console.log(row);
    nextTick(()=>{
       CollectionVue.value.handleEdit(row)

    })
   
  }
 

  
}
const updataEdit = (row)=>{
  showDom('edit',row)
}
const deleteData = async (row)=>{
   await deleteCollections(row.id)
  //  console.log(res);
  collectData.value = await fetchCollection(userId)
}

const getData = async() => {
  collectData.value = await fetchCollection(userId)
}

// 点击蒙版外的会退出弹窗
const handleMaskClick = (event) => {
  if (!event.target.classList.contains('createNewCollection')) {
    isNewCollection.value = false
  }
}
const newCollectionVue = defineAsyncComponent(()=>
  import ("./newCollection.vue")
)
const collectionItem = defineAsyncComponent(()=>
  import ('./CollectItem.vue')
)
// 获取文件夹的更新
provide('isUpdateCollection', {
  isNewCollection,
  updateCollection(val) {
    collectKey.value += 1
    isNewCollection.value = val
  },
  getCollection() {
    return isNewCollection.value
  }
})
const props = defineProps({
  isShow: {
    type: Boolean,
    require: false,
    default: false
  },
  isCollected: { // 是否为收藏夹
    type: Boolean,
    require: false,
    default: true
  },
  isAdded: { // 当前视频是否加入过收藏夹/视频合集
    type: Boolean,
    require: false,
    default: false
  },
  isVideoDetail: { // 是否存在于视频详情页
    type: Boolean,
    require: false,
    default: true
  },
  videoId: {
    type: Number,
    require: false,
    default: 1
  }
})
const emits = defineEmits(['update:isCollected', 'update:isShow',
 'change:isAdded'])
const isCollected = computed({
  get: function() {
    return props.isCollected
  },
  set: function(val) {
    emits('update:isCollected', val)
  }
})
const isShow = computed({ // 只读，不能实时修改；但使用computed会比ref快
  get:function(){
    return props.isShow
  },
  set:function(val) {
    emits('update:isShow', val)
  }
})
// 更新当前的Collection
const upNewCollection = () => {

  
}
// 计算当前videoId被哪些收藏夹收藏了
const whichCollections = (videoId, collectionArray) => {
  if (!Array.isArray(collectionArray)) {
    throw new Error("collectionArray must be an array");
  }
  let isCollectedArray = collectionArray
  .filter(item =>item.videoId===videoId)
  .map(item =>item.collectGroupId)
  return isCollectedArray
}
// 关闭该页面
const handleClose = () => {
  isShow.value = false
  isCollected.value = true
}
// 获取当前状态下将收藏的
const aboutToCollect = () => {
  // 更新收藏状态

}
// 上传
const onSubmit = () => {
  let inp = document.querySelectorAll('.checkCollect');
    let arr = [];
    inp.forEach
  console.log(inp);
  // let val = inp.find((item)=>{
  //   return item.checked
  // })
  // if(!val) return ElMessage({
  //   type:'warning',
  //   message:'请先勾选文件夹'
  // })
  // emits('checkDir',val)
  // emits(, ) 
  // 退出页面
  // isShow.set(false)
}


// const handleChange = (e)=>{
//   let dom = document.querySelectorAll('input')
//   dom.forEach((item)=>{
//     item.checked = false;
//   })
//   e.target.checked = true
// }

onMounted(async()=>{
  if(isCollected.value) { // 有被收藏过
    collectData.value = await fetchCollection(userId)
    const collections = await fetchVideoInCollect(userId)
    currentToCollection.value = new Array(collectData.value.length).fill(false)
    // whichCollections(props.videoId, collections)
  }
  eachTotalNum.value = collectFolder["eachLength"]
})
</script>

<style lang="scss">
.el-dialog__header {
  margin-right: 0;
}
.el-dialog__body {
  height: calc(70%);
}
</style>

<style lang="scss" scoped>
.collect-flex-wrap {
  width: 27rem;
}
input[type='checkbox'] {
  appearance: none;
  width: 1.1rem;
  height: 1.1rem;
  border: 1px #235891 solid;
  margin-right: 0.5rem !important;
  &::before {
      content: "";
      position: absolute;
  }
  &:checked, &:checked::after {
      content: "\2713";
      background-color: #235891;
      color: #fff;
      border-radius: 50%;
  }
}
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