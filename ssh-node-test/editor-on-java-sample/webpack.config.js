const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/index.js',
    mode:'production',
    target: 'node',
    externals: {
        '../build/Release/cpufeatures.node': 'require("cpufeatures.node")',
        './crypto/build/Release/sshcrypto.node': 'require("sshcrypto.node")'
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'demo.js'
    },
    module: { },
    plugins: [
        new CopyPlugin({
            patterns: [
                { from: '../node_modules/editor-on-java/src/worker.js', to: '' },
                // { from: 'src/index.html', to: '' },
            ]
        })
    ],
    resolve: {
        extensions: ['.js']
    }
}
