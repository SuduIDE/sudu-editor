console.log(`Hello`);

import { Worker } from 'worker_threads';

let worker = new Worker("./worker.mjs");

worker.on('message', function(message) {
  console.log("message from worker", message);
  switch (message) {
    case "finish":
      worker.terminate();
       break;
    case "listed":
      worker.postMessage({cmd: "close", ssh: ssh});
      break;
  }
});

const ssh = {
  host: '172.29.85.42',
  port: 22,
  username: 'kirill',
  password: 'gbpltw'
  //  privateKey: readFileSync('/path/to/my/key')
};

worker.postMessage({cmd: "readDir", ssh: ssh});
