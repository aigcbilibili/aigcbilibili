import Vue from 'vue'
import skeleton from './Skeleton'

export default new Vue({
    components: {
        skeleton
    },
    template:'<div><skeleton id="skeleton" style="display:none;" /></div>'
})