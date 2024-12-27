const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
    entry: './src/index.js',
    mode:'production',
    target: 'node',
    externals: {
        //'fs': 'no_fs',
        //'worker_threads': 'no_worker_threads',
        // 'assert': 'no_assert',
        // 'buffer': 'no_buffer',
        // 'stream': 'no_stream',
        // 'net': 'no_net',
        // 'http': 'no_http',
        // 'https': 'no_https',
        // 'tls': 'no_tls',
        // 'dns': 'no_dns',
        // 'path': 'no_path',
        // 'child_process': 'no_child_process',
        // 'crypto': 'no_crypto',
        // 'util': 'no_util',
        // 'zlib': 'no_zlib',
        '../build/Release/cpufeatures.node': 'no_cpufeatures',
        './crypto/build/Release/sshcrypto.node': 'no_sshcrypto_node'
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
                { from: 'src/index.html', to: '' },
            ]
        })
    ],
    resolve: {
        extensions: ['.js']
    }
}
