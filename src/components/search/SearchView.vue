<!--搜索时浮动的框-->
<template>
    <div class="search-panel-content based-box">
        <!--推荐-->
        <div class="">
        <p>☆* 推荐 *☆</p>
        </div>
        <!--热点-->
        <div class="">
        <p>☆* 热点 *☆</p>
        </div>
    </div>
</template>

<script setup>
const a = 1
</script>

<style lang="scss" scoped>
@import "@/assets/css/main.scss";
.search-panel-content{
  position: absolute;
  top: 5rem;
  left: 15rem;
  min-width: 25rem;
  min-height: 10rem;
  border-radius: 20px;
  height: 20rem;
  background: #ffffff;
  z-index: 30;
}
.search-panel-content p{
  font-size: 1.2rem;
  font-weight: 700;
  color: #2a6490;
}
@media screen and (min-width: 900px){
    .search-panel-content{
        left: 20rem;
        width: 50rem;
    }
}
</style>