import { ElMessage } from "element-plus"

/**
 * 节流
 * @param {function} fn 要绑定的事件
 * @param {number} interval 限制的间隔时间
 * 使用方法：
 *  const throttle = new Throttle(fn, 500)
 *  window.addEventListener('name', throttle)
 */
export default class Throttle{
    constructor(fn, interval){
        this.last = 0 // 上次触发回调的时间
        this.interval = interval
        this.fn = fn
    }
    execute(context, args){
        this.last = this.now
        this.fn.apply(context, args) // 若时间间隔大于我们设定的时间间隔阈值，则执行回调
    }
    throttle (){
        let context = this // 保留this上下文
        let args = arguments
        this.now =+ new Date()
        if(this.now - this.last >= this.interval){
            this.execute(context, args)
        }else{
            ElMessage.error('事件请求过于频繁')
            const timer = setTimeout(()=>{
                this.execute(context, args)
            }, this.interval)
            timer
        }
    }
}