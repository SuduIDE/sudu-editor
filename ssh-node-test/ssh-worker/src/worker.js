console.log(`Hello, from worker thread`);

import {newSshClient} from "./ssh_mjs.mjs";

import {parentPort} from 'node:worker_threads';

const connectMap = new Map();

function connect(config) {
  return new Promise(
      (resolve, reject) => {
        const connection = newSshClient();
        connection.on("ready", () => {
          console.log('connection: ready', config);
          connection.sftp((err, sftp) => {
            if (err) reject(err);
            else resolve({connection, sftp});
          });
        }).connect(config);
      }
  );
}

function getConnection(config) {
  const key = JSON.stringify(config);
  let promise = connectMap.get(key);
  if (!promise) {
    promise = connect(config);
    connectMap.set(key, promise);
  }
  return promise;
}

function doReadDir(sftp, onComplete) {
  sftp.readdir('/', listFiles);
}

function finish(pair) {
  pair.connect.end();
  parentPort.postMessage("finish");
}

function listFiles(error, list) {
  if (error) {

  } else {
    console.log("list.length = ", list.length);
    for (let i = 0; i < list.length; i++) {
      let file = list[i];
      console.log("file" + i + " :", file.filename);
    }
    parentPort.postMessage("listed");
  }
}

function readDir(config) {
  const conn = getConnection(config);
  conn.then(pair => {
    doReadDir(pair.sftp, () => finish(pair));
  });
}

function close(config) {
  const key = JSON.stringify(config);
  let promise = connectMap.get(key);
  if (promise) {
    promise.then(pair => {
      pair.connection.end();
      connectMap.delete(key);
      console.log("connection closed: ", key);
      parentPort.postMessage("finish");
    })
  }
}

parentPort.onmessage = function (message) {
  let data = message.data;
  console.log(`Worker: `, data);
  if ('cmd' in data) {
    console.log(`cmd =`, data.cmd);
    switch (data.cmd) {
      case "readDir":
        console.log(`readDir, ssh=`, data.ssh);
        readDir(data.ssh);
        break;
      case "close":
        console.log(`closing ssh=`, data.ssh);
        close(data.ssh);
    }
  }

}
