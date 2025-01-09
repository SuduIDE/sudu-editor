const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/ssh_mjs.mjs',
    mode:'production',
    target: 'node',
    externals: {
        // '../build/Release/cpufeatures.node': 'cpufeatures.node',
        // './crypto/build/Release/sshcrypto.node': 'sshcrypto.node'
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'ssh_mjs.mjs',
        library: {
            type: "module",
        }
    },
    experiments: {
        outputModule: true,
    },
    module: {
        rules: [
            { test: /\.node$/, loader: 'node-loader',}
        ]
    },
    resolve: {
        extensions: ['.js']
    }
}
