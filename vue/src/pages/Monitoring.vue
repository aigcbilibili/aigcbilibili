<template>
    <div style="width: 100%;height: 100%;display: flex;justify-content: center;">
        <div id="map"></div>
    </div>
</template>

<script setup>
import * as echarts from "echarts"
import { onMounted, ref } from "vue";

onMounted(() => {

    const chartInstance = echarts.init(document.querySelector('#map'));

    const option = {
        // ECharts 配置项
        title: {
            text: 'ECharts 示例'
        },
        tooltip: {},
        xAxis: {
            data: ['A', 'B', 'C', 'D', 'E', 'F']
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
        }]
    };

    chartInstance.setOption(option);
});

</script>
<style scoped lang="scss">
#map {
    width: 80vw;
    height: 90vh;

}
</style>