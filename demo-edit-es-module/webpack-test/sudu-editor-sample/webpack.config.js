const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/index.js',
    mode:'production',
   
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'demo.js'
    },
    module: { },
    plugins: [
        new CopyPlugin({
            patterns: [
                { from: '../node_modules/sudu-editor-tmp/src/worker.js', to: '' },
                { from: 'src/index.html', to: '' },
            ]
        })
    ],
    resolve: {
        extensions: ['.js']
    }
}