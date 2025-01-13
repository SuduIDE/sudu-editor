console.log(`Hello`);

import { Worker } from 'worker_threads';

let worker = new Worker("./worker.mjs");

function dumpFolder(path, list) {
  console.log("directory '" + path + "'list[" + list.length + "]");
  for (let i = 0; i < list.length; i++) {
    console.log("file" + i + " :", list[i].filename);
  }
}

worker.on('message', function(message) {
  console.log("message from worker", message[0]);
  switch (message[0]) {
    case "finished":
      worker.terminate();
       break;
    case "listed":
      console.log("listed folder ", message[1]);
      dumpFolder(message[1], message[2]);
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

worker.postMessage({cmd: "readDir", path: '/', ssh: ssh});
