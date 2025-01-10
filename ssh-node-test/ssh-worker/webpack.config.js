const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
  entry: './src/worker.js',
  mode: 'production',
  target: 'node',
  externals: {
    "./ssh_mjs.mjs": 'module ./ssh_mjs.mjs',
  },
  experiments: {outputModule: true},
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'worker.mjs'
  },
  module: {},
  plugins: [
    new CopyPlugin({
      patterns: [
        {from: '../ssh-module/dist/ssh_mjs.mjs', to: ''},
      ]
    })
  ],
  resolve: {
    extensions: ['.js']
  }
}
