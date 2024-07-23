const path = require('path')
const copyPlugin = require('copy-webpack-plugin')

new copyPlugin({
    patterns: [{
        "from": "./src/assets/img", "to": path.join(dirname, "./dist/img/")
    }, {
        "from": "./src/assets/css", "to": path.join(dirname, "./dist/css/")
    }, {
        "from": "./src/assets/biliBg", "to": path.join(dirname, "./dist/biliBg/")
    }, {
        "from": "./src/assets/data", "to": path.join(dirname, "./dist/data/")
    }, {
        "from": "./src/assets/other", "to": path.join(dirname, "./dist/other/")
    }]
})

module.exports = {
    // 入口
    entry: './src/main.js',
    mode: 'development',
    rules: [{
        test: /\.css/,
        use: ['style-loader', 'css-loader',]
    }, {
        test: /\.less$/,
        use: [
            'style-loader',
            'css-loader',
            'less-loader'
        ]
    }
    ],
    // 出口
    output: {
        path: path.resolve(__dirname, 'dist'),
    }
}