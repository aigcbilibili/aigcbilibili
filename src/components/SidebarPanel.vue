<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="rgba(94,73,238,1)"
      text-color="#ffffff"
      active-text-color="#f7da94"
      unique-opened
      router
    >
      <template v-for="item in items" :key="item.index"
        ><!--NOTE v-for没有key属性会报错-->
        <template v-if="item.subs">
          <el-sub-menu :index="item.index" v-permiss="item.permiss">
            <template #title
              ><!--NOTE template不会渲染元素而是包装，#/slot是命名插槽、可以控制特定内容-->
              <el-icon style="margin-top: 0.1rem; margin-left: 0.1rem">
                <component :is="item.icon"></component>
              </el-icon>
              <span>{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs" :key="subItem.index">
              <el-menu-item
                :index="subItem.index"
                v-permiss="subItem.permiss"
                style="width: 3rem; height: 2rem; font-size: 1.2rem !important"
              >
                <div style="width: 3rem">{{ subItem.title }}</div>
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
        <template v-else>
          <el-menu-item
            :index="item.index"
            :key="item.index"
            v-permiss="item.permiss"
          >
            <el-icon>
              <component :is="item.icon"></component>
            </el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import { computed, ref } from "vue"
import { useRoute } from "vue-router"

const id = 1;
const items = [
  {
    icon: "Odometer",
    index: `/userCenter/${id}/myItem`,
    title: "主页",
    permiss: "1",
    subs: [
      {
        index: 1,
        title: "我的视频",
        permiss: "11",
      },
      {
        index: 2,
        title: "视频合集",
        permiss: "12",
      },
      { index: 3, title: "我的收藏", permiss: "13" },
      { index: 4, title: "最近点赞", permiss: "14" },
    ],
  },
  {
    icon: "DataAnalysis",
    index: `/userCenter/${id}/charts`,
    title: "数据中心",
    permiss: "2",
  },
  {
    icon: "MapLocation",
    index: `/userCenter/${id}/trends`,
    title: "动态",
    permiss: "3",
  },
  {
    icon: "Camera",
    index: `/userCenter/${id}/uploadVideo`,
    title: "视频投稿",
    permiss: "4",
  },
  {
    icon: "Compass",
    index: `/userCenter/${id}/permission`,
    title: "设置权限",
    permiss: "5",
  },
];

const route = useRoute();
const onRoutes = computed(() => {
  return route.path;
});

const collapse = ref(true); // 默认展开
</script>

<style>
@import url(@/assets/css/menu.css);
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}
.sidebar > ul {
  height: 100%;
}
</style>
