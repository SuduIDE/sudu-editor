const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/index.js',
    mode:'production',
    target: 'node',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'demo.js'
    },
    module: { },
    plugins: [
        new CopyPlugin({
            patterns: [
                { from: '../node_modules/ssh-worker/dist/worker.mjs', to: '' },
                { from: '../node_modules/ssh-worker/dist/ssh_mjs.mjs', to: '' },
            ]
        })
    ],
    resolve: {
        extensions: ['.js']
    }
}
