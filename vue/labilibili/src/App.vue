<template>
  <router-view />
</template>

<script setup>
// import { provide } from 'vue'
// import * as echarts from 'echarts'
// provide('echarts', echarts)
import { onMounted, onUnmounted } from 'vue'
import { addData } from '@/api/echarts'

const getCurrentDate = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0'); // getMonth() 返回的月份是从0开始的，所以需要+1
  const day = String(now.getDate()).padStart(2, '0');

  return {
    year: year,
    month: month,
    day: day
  };

}
let date = getCurrentDate();

const handleBeforeUnload = async () => {

  let Monitoring = localStorage.getItem('Monitoring')
  localStorage.setItem('Monitoring', Number(Monitoring) - 1)
  if (Number(Monitoring) === 1) {
    localStorage.setItem('a', 1)
    await addData({
      createTime: `${date.year}/${date.month}/${date.day}`
    })
  }
}

onMounted(() => {
  let Monitoring = localStorage.getItem('Monitoring') || 0
  localStorage.setItem('Monitoring', Number(Monitoring) + 1)
  window.addEventListener('beforeunload', handleBeforeUnload);
})
onUnmounted(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload);
})
</script>

<style lang="scss">
@import "@/assets/css/main.scss";
</style>