console.log(`Hello`);

import { Worker } from 'worker_threads';

let worker = new Worker("./worker.mjs");

let requests = 0;

function dumpFolder(path, list) {
  let s = "";
  for (let i = 0; i < list.length; i++) {
    let filename = list[i].filename;
    s += i + 1 < list.length ? filename + ", " : filename;
  }
  console.log("  directory '" + path +
      "', files[" + list.length + "]: " + s);
}

function closeConnection() {
  worker.postMessage({cmd: "close", ssh: ssh});
}

function maybeClose() {
  if (--requests === 0)
    closeConnection();
}

worker.on('message', function(message) {
  console.log("message from worker", message[0]);
  switch (message[0]) {
    case "finished":
      console.log("  worker.terminate() ....")
      worker.terminate();
      break;
    case "listed":
      console.log("  listed folder ", message[1]);
      dumpFolder(message[1], message[2]);
      maybeClose();
      break;
    case "data":
      console.log("  file data ", message[1]);

      maybeClose();
      break;

  }
});

let args = process.argv;

if (args.length < 6) {
  console.log("args: host port user password");
  process.exit(1);
}

const ssh = {
  host: args[2],
  port: args[3],
  username: args[4],
  password: args[5]
  //  privateKey: readFileSync('/path/to/my/key')
};

for (let i = 6; i + 1 < args.length; i++) {
  let cmd = args[i];
  let path = args[i + 1];
  console.log("cmd = " + cmd + ", path = " + path);
  requests++;
  worker.postMessage({cmd, path, ssh});
}

