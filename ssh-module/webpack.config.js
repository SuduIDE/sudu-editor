const path = require('path');

module.exports = {
  entry: './src/sshLib.mjs',
  mode: 'production',
  target: 'node',
  externals: {
    '../build/Release/cpufeatures.node': 'commonjs cpufeatures.node',
    './crypto/build/Release/sshcrypto.node': 'commonjs sshcrypto.node'
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'sshLib.mjs',
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
