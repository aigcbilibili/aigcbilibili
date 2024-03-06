<!--日历组件-->
<template>
    <div class="calendar-wrap based-box">
        <!-- 日历信息 -->
        <div class="flex-center-container" style="margin-bottom: 1.5rem;font-size: 1.1rem;font-weight: 700;">
            <p style="margin-right: 0.5rem;">{{choseTime.year}}</p>
            -
            <p style="margin-left: 0.5rem;">{{choseTime.month}}</p>
        </div>
        <!-- 日历表头 -->
        <div class="calendarTop">
            <div class="calendar-tag" v-for="item in top" :key="item">
                <span>{{ `星期` + item }}</span>
            </div>
        </div>
        <!-- 具体内容 -->
        <div class="calendarBottom">
            <div class="calendar-content">
                <div 
                    class="calendar-item flex-column-center-container" 
                    :style="{
                        'background-color': item.isToday?'#e9f4ff':'#fff'
                    }"
                    v-for="(item, index) in visibleCalendar()" 
                    :key="index">
                    <!-- 号数 -->
                    <p class="calendar-item-number" :style="{
                        'color':item.thisMonth?'#000000':'#6b6b6b',
                        'font-weight':item.thisMonth?'600':''
                        }">
                        {{ item.day }}
                    </p>
                    <!-- 号数对应的需要展示的内容 -->
                </div>
		    </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue"
const top = ['一', '二', '三', '四', '五', '六', '日']
const choseTime = ref({
    year: 2024,
    month: 2,
    day: 1
})
const visibleCalendar = () => {
    // 获取今天的日期并将时间设置到 0分0秒0点
    const today = new Date()
    today.setHours(0)
    today.setMinutes(0)
    today.setSeconds(0)
    today.setMilliseconds(0)
    console.log("今天？", today.getFullYear())

    const calendarArr = []
    // 获取当前月份第一天
    const currentFirstDay = new Date(choseTime.value.year, choseTime.value.month-1, 1)
    // 获取第一天是周几，注意周日的时候getDay()返回的是0，要做特殊处理
    const weekDay = currentFirstDay.getDay() === 0 ? 7 : currentFirstDay.getDay()

    // 用当前月份第一天减去周几前面几天，就是看见的日历的第一天
    const startDay = currentFirstDay - (weekDay - 1) * 24 * 3600 * 1000
    // 我们统一用35天来显示当前显示日历
    for (let i = 0; i < 35; i++) {
        const date = new Date(startDay + i * 24 * 3600 * 1000)
        calendarArr.push({
            date: new Date(startDay + i * 24 * 3600 * 1000),
            year: date.getFullYear(),
            month: date.getMonth(),
            day: date.getDate(),
            // 是否在当月
            thisMonth:
                date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth()
                    ? true
                    : false,
            // 是否是今天
            isToday:
                date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth() &&
                date.getDate() === today.getDate()
                ? true
                : false,
            // 是否在今天之后
            afterToday: date.getTime() >= today.getTime() ? 'afterToday' : '',
            // 得到日期字符串
            timeStr: date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' 00:00:00',
            // 得到date类型日期
            timeDate: date
        })
    }
    return calendarArr
}

</script>

<style lang="scss" scoped>
.calendar-wrap {
    width: 30rem;
    height: 23rem;
    border-radius: 10px;
    padding: 1rem;
    display: flex; // flex流式布局
	flex-direction: column; // flex流式布局 竖向排列
}
.calendarTop {
	width: 100%;
	height: 30px; // 根据自己的页面固定好高度
	display: flex; // flex流式布局 默认是横向排列
	justify-content: space-evenly; // flex流式布局 相等间距
}
.calendar-content {
    width: 100%;
    height: 100%;
    display: flex;
    flex-wrap: wrap; // 超出宽度换行
    justify-content: space-evenly;
}
.calendar-item {
	width: 14%; // 这个宽度需要和 .calendar-tag 的宽度保持一致(和星期几对应)
	height: 16%; // 高度根据实际需求调整
	display: flex; // flex流式布局
    border-radius: 10px;
}
.calendar-item-number {
	width: 100%;
	height: 20px;
}
.calendar-item-content {
	width: 100%;
	height: calc(100% - 20px);
}
.isToday {
	border: 1px solid #50bfff;
}
.calendar-tag {
	width: 14%;
	height: 30px; // 高度根据自己页面调整 
}
.calendar-tag span {
	background-color: rgb(218 232 246); // 蓝底
    width: 100%; 
    height: 30px; // 高度根据自己页面调整 
    font-weight: bold; // 字体加粗
    padding: 0.4rem;
    border-radius: 8px;
}
.calendarBottom {
	width: 100%;
    margin-top: 0.5rem;
	height: calc(100% - 30px); // 高度是父级div 的高度减去.calendarTop 的高度
}
</style>