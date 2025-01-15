console.log(`Hello, from worker thread`);

import {newSshClient, OPEN_MODE} from "./ssh_mjs.mjs";

import {parentPort} from 'node:worker_threads';

const connectMap = new Map();

function connect(config, key) {
  return new Promise(
      (resolve, reject) => {
        const connection = newSshClient();
        connection.on("ready", () => {
          console.log('worker: connection: ready ', key);
          connection.sftp((err, sftp) => {
            if (err) {
              reject(err);
            } else {
              console.log("worker: resolve sftp", sftp.constructor.name);
              resolve({connection, sftp});
            }
          });
        }).connect(config);
      }
  );
}

function getConnection(config) {
  const key = JSON.stringify(config);
  let promise = connectMap.get(key);
  if (!promise) {
    promise = connect(config, key);
    connectMap.set(key, promise);
  }
  return promise;
}

function finish(pair, key) {
  console.log("worker: connection closed: ", key);
  pair.connection.end();
  parentPort.postMessage(["finished"]);
}

function listFiles(error, list, path) {
  if (error)
    list = [];
  parentPort.postMessage(["listed", path, list]);
}

function readDir(config, path) {
  const conn = getConnection(config);
  conn.then(pair => {
    pair.sftp.readdir(path,
        (error, list) => listFiles(error, list, path));
  });
}

function readFile(config, path) {
  const conn = getConnection(config);
  conn.then(pair => {
    console.log("sftp.OPEN_MODE = ", OPEN_MODE);
    pair.sftp.open(path, OPEN_MODE.READ, (error, handle) => {
      if (error) {
        console.log("sftp.open: path=" + path + ", error = " + error?.message);
      } else {
        console.log("sftp.open: path=" + path + ", handle = ", handle);
      }
      parentPort.postMessage(["data", path]);
    });
  });
}

function close(config) {
  const key = JSON.stringify(config);
  let promise = connectMap.get(key);
  if (promise) {
    connectMap.delete(key);
    promise.then(pair => finish(pair, key));
  }
}

parentPort.onmessage = function (message) {
  let data = message.data;
  if ('cmd' in data) {
    console.log("worker: cmd =", data.cmd);
    switch (data.cmd) {
      case "readDir":
        console.log("worker: readDir '" + data.path +
            "', ssh=" + JSON.stringify(data.ssh));
        readDir(data.ssh, data.path);
        break;
      case "readFile":
        console.log("worker: readFile '" + data.path +
            "', ssh=" + JSON.stringify(data.ssh) + "\n");
        readFile(data.ssh, data.path);
        break;
      case "close":
        console.log("worker: closing ssh=", JSON.stringify(data.ssh));
        close(data.ssh);
        break;
    }
  }

}
