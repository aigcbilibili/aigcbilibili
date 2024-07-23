<template>
  <router-view/>
</template>

<script setup>
// import { provide } from 'vue'
// import * as echarts from 'echarts'
// provide('echarts', echarts)
import { onMounted  ,onUnmounted} from 'vue'

const handleBeforeUnload = ()=>{
  let Monitoring = localStorage.getItem('Monitoring') 
  localStorage.setItem('Monitoring',Number(Monitoring) - 1 )
  if(Number(Monitoring) === 1){
    let a = localStorage.getItem('a') || 0
    localStorage.setItem('a',Number(a) + 1 )
  }
}

onMounted(()=>{

  let Monitoring = localStorage.getItem('Monitoring') || 0
  localStorage.setItem('Monitoring',Number(Monitoring) +1 )
  window.addEventListener('beforeunload', handleBeforeUnload);
})
onUnmounted(()=>{
  window.removeEventListener('beforeunload', handleBeforeUnload);
})
</script>

<style lang="scss">
@import "@/assets/css/main.scss";
</style>