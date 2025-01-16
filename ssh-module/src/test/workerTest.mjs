console.log(`Hello`);

import { Worker } from 'worker_threads';

let worker = new Worker("./worker.mjs");

let requests = 0;

function dumpFolder(path, folders, files) {
  let s = "";
  for (let i = 0; i < folders.length; i++) {
    s += i + (1 < folders.length || files.length > 0) ?
        folders[i] + ", " : folders[i];
  }
  for (let i = 0; i < files.length; i++) {
    const file = files[i].filename + "(" + files[i].size + ")";
    s += i + 1 < files.length ? file + ", " : file;
  }
  console.log("  directory '" + path + "', folders[" + folders.length +
      "], files[" + files.length + "]: " + s);
}

function closeConnection() {
  worker.postMessage({cmd: "close", ssh: ssh});
}

function maybeClose() {
  if (--requests === 0)
    closeConnection();
}

function dumpData(message) {
  console.log("  file data path:", message[1]);
  if (message.length < 4) {
    console.log("  error:", message[2]);
  }

  if (message.length === 4) {
    const data = message[2];
    const bytesRead = message[3];
    console.log("  data.byteLength = ", data.byteLength,
        ", bytes read:", bytesRead);
  }
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
      if (message.length == 4) {
        dumpFolder(message[1], message[2], message[3]);
      }

      maybeClose();
      break;
    case "data":
      dumpData(message);
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

for (let i = 6; i + 1 < args.length; i += 2) {
  let cmd = args[i];
  let path = args[i + 1];
  console.log("cmd = " + cmd + ", path = " + path);
  requests++;
  worker.postMessage({cmd, path, ssh});
}

