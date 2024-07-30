const { defineConfig } = require('@vue/cli-service') // NOTE 本文件不是JS模块，不能直接使用ES6的import

// const SkeletonWebpackPlugin = require('vue-skeleton-webpack-plugin')
// const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false, // 关闭eslint校验
  css: {
    extract: true, // 拆分ExtractTextPlugin插件，默认true - 骨架屏需要为true
    loaderOptions: {
      sass: {
        implementation: require('sass')
      }
    }
  },
  assetsDir: './',

  devServer: {
    host: 'localhost',
    port: 2023,
    // client:{
    //   webSocketURL: 'ws://127.0.0.12:2023/ws'
    // },
    headers: {
      'Access-Control-Allow-Origin': '*',
    },
    open: true, //自动打开项目
    // NOTE 跨域配置
    proxy: {
      '/api': {
        target: "******",
        // 允许跨域：如果不开的话，会拦截响应
        changeOrigin: true,
      },
      '/video-test': {
        target: 'http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4',
        changeOrigin: true,
        pathRewrite: {
          '^/video-show': ''
        }
      },
      '/wschat': {
        target: '*******',
        ws: true,
        changeOrigin: true,
      }

    }
  }
})