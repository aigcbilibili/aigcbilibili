<template>
    注册功能
    <!--TODO 将注册功能加上-->
</template>

<script setup>
// 1.注册
const enrollAccount=(()=>{
    // 1.1.获取数据
    const {username,password}=reactive({
        username:'',
        password:''
    })
    // 1.2.注册
    const register=async()=>{
        // 1.2.1.发送请求
        const {data}=await axios.post('/api/register',{
            username,
            password
        })
        // 1.2.2.判断请求是否成功
        if(data.code===200){
            // 1.2.2.1.跳转到登录页
            router.push('/login')
        }else{
            // 1.2.2.2.显示错误信息
            message.error(data.msg)
        }
    }
    return {
        username,
        password,
        register
    }
})

</script>

<style scoped>
</style>