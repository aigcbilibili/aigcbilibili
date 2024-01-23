/**
 * 虚拟列表实现
 */
let count = Math.ceil(document.body.clientHeight / 120);
let startIndex = 0;
let endIndex = 0;

export const getVirtualList = new IntersectionObserver(function(entries){
    loadData(startIndex, count)
    // 标志位元素进入视口
    if(entries[0].isIntersecting) {
      // 更新列表数据起始和结束位置
      startIndex = startIndex += count;
      endIndex = startIndex + count;
      if(endIndex >= getDataList().length) {
        // 数据加载完取消观察
        io.unobserve(entries[0].target)
      }
      // requestAnimationFrame 由系统决定回调函数的执行时机
      requestAnimationFrame(() => {
        loadData(startIndex, endIndex)
        let num = Number(getDataList().length - startIndex)
        let info = ['还有', num , '条数据']
        $('.top').innerText = info.join(' ')
        if(num - count <= 0) {
        //    $('.top').classList.add('out')
         }
      })
    }
    getVirtualList.observe($('.sentinels'))
})