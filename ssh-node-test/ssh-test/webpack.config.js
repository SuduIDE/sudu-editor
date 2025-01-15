const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');

module.exports = {
  entry: './src/index.js',
  mode: 'production',
  target: 'node',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'demo.mjs',
    library: {
      type: "module",
    }
  },
  experiments: {
    outputModule: true,
  },
  module: {},
  plugins: [
    new CopyPlugin({
      patterns: [
        {from: '../ssh-worker/dist/worker.mjs', to: ''},
        {from: '../ssh-worker/dist/ssh_mjs.mjs', to: ''},
      ]
    })
  ],
  resolve: {
    extensions: ['.js']
  }
}
