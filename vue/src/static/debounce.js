/**
 * vue防抖，兼容vue3,vue2和普通js
 * delay：延迟时间（毫秒）
 */

export default class Debounce {
    constructor(delay) {
        this.delay = delay ? delay : 500;
        this.timeOut = null;
    }

    debounceEnd (){
        return new Promise((resolve,reject) =>{
            if(this.timeout){
                clearTimeout(this.timeout)
            }
            this.timeout = setTimeout(()=>{
                resolve('success');
            },this.delay)
        })
    }

}

