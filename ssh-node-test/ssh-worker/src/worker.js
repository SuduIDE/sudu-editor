console.log(`Hello, from worker thread`);

const { readFileSync } = require('fs');

const { Client } = require('ssh2');

import { parentPort } from 'node:worker_threads';

function testClient() {
  const conn = new Client();
  conn.on('ready', () => {
    console.log('Client :: ready');
    conn.exec('uptime', (err, stream) => {
      if (err) throw err;
      stream.on('close', (code, signal) => {
        console.log('Stream :: close :: code: ' + code + ', signal: ' + signal);
        conn.end();
        parentPort.postMessage("finish");
      }).on('data', (data) => {
        console.log('STDOUT: ' + data);
      }).stderr.on('data', (data) => {
        console.log('STDERR: ' + data);
      });
    });
  }).connect({
    host: '172.29.85.42',
    port: 22,
    username: 'kirill',
    password: 'gbpltw'

    //  privateKey: readFileSync('/path/to/my/key')
  });
}


parentPort.onmessage = function(message) {
  console.log(`Worker: ` + message.data);
  console.log(`testClient123: `);
  testClient();
}
