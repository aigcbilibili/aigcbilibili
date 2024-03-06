  <template>
    <el-menu
      class="common-sidebar"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="rgba(94,73,238,1)"
      text-color="#ffffff"
      active-text-color="#f7da94"
      unique-opened
      :router=true
    >
      <template v-for="(item,index) in items" :key="index"
        ><!--NOTE v-for没有key属性会报错-->
        <template v-if="item.subs">
          <el-sub-menu v-if="item.permiss" :index="item.index">
            <template #title><!--NOTE template不会渲染元素而是包装，#/slot是命名插槽、可以控制特定内容-->
              <el-icon style="margin-top: 0.1rem; margin-left: 0.1rem">
                <component :is="item.icon"></component>
              </el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item v-for="(subItem, index_) in item.subs" :key="index_"
              :index="subItem.index"
              style="width: 7.8rem; height: 1.8rem; font-size: 1.2rem !important"
            >
              <div v-if="subItem.permiss">{{ subItem.title }}</div>
            </el-menu-item>
          </el-sub-menu>
        </template>
        <template v-else>
          <el-menu-item
            :index="item.index"
            v-if="item.permiss"
          >
            <el-icon>
              <component :is="item.icon"></component>
            </el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </template>

<script setup>
import { computed, ref, defineProps } from "vue"
import { useRoute } from "vue-router"
const props = defineProps({
  id: { // 用户id
    type: Number,
    required: true,
    default: 1
  },
  menuConfig: { // 菜单项的设置
    type: Object,
    required: true,
    default: undefined
  }
})
const route = useRoute()
const id = props.id
const items = props.menuConfig
const onRoutes = computed(() => {
  return route.path;
});
const collapse = ref(true); // 默认展开
</script>

<style lang="scss">
@import url(@/assets/css/menu.css);
</style>