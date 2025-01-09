const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/worker.js',
    mode:'production',
    target: 'node',
    externals: {
        '../build/Release/cpufeatures.node': 'require("cpufeatures.node")',
        './crypto/build/Release/sshcrypto.node': 'require("sshcrypto.node")'
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'worker.js'
    },
    module: { },
    resolve: {
        extensions: ['.js']
    }
}
