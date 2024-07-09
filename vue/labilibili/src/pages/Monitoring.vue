<template>
    <div
        style="width: 100%;height: 100%;display: flex;flex-direction: column;justify-content: space-around;align-items: center;">
        <div id="map"></div>
        <div id="map2"></div>
    </div>
</template>

<script setup>
import * as echarts from "echarts"
import { onMounted, ref } from "vue";
import { getData } from '@/api/echarts'

onMounted(async () => {
    let data_x_6 = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30']
    let data_x_7 = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31']
    let res = await getData()
    // console.log(res);
    let data_6_pv = Array.from(30);
    let data_6_uv = Array.from(30);
    let data_7_pv = Array.from(31);
    let data_7_uv = Array.from(31);
    res.forEach(item => {
        if (item.month === 6) {
            item.viewCount.forEach((i) => {
                let str = i.createTime.split('/')[2];
                let index = data_x_6.indexOf(str);
                data_6_pv[index] = i.pv;
                data_6_uv[index] = i.uv;
            })
        } else if (item.month === 7) {
            item.viewCount.forEach((i) => {
                let str = i.createTime.split('/')[2];
                let index = data_x_7.indexOf(str);
                data_7_pv[index] = i.pv;
                data_7_uv[index] = i.uv;
            })
        }
    })
    const chartInstance = echarts.init(document.querySelector('#map'));
    const chartInstance2 = echarts.init(document.querySelector('#map2'));

    const option = {
        // ECharts 配置项
        title: {
            text: '6月份系统使用量'
        },
        tooltip: {},
        xAxis: {
            data: data_x_6
        },
        yAxis: {
            interval: 5
        },
        series: [
            {
                name: '6月份PV',
                type: 'bar',
                data: data_6_pv
            },
            {
                name: '6月份UV',
                type: 'bar',
                data: data_6_uv
            },
        ]
    };
    const option2 = {
        // ECharts 配置项
        title: {
            text: '7月份系统使用量'
        },
        tooltip: {},
        xAxis: {
            data: data_x_7
        },
        yAxis: {
            interval: 5
        },
        series: [
            {
                name: '7月份PV',
                type: 'bar',
                data: data_7_pv
            },
            {
                name: '7月份UV',
                type: 'bar',
                data: data_7_uv
            }
        ]
    };


    chartInstance.setOption(option);
    chartInstance2.setOption(option2);
});

</script>
<style scoped lang="scss">
#map,
#map2 {
    width: 80vw;
    height: 45vh;
    margin-bottom: 10px;
    // background-color: pink;
}
</style>