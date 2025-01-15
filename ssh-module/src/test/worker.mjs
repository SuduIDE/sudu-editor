console.log(`Hello, from worker thread`);

import {newSshClient, OPEN_MODE} from "../../dist/ssh_mjs.mjs";

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

function closeHandle(sftp, handle, path) {
  sftp.close(handle, (error) => {
    console.log("sftp.close finished: ", path, ", error = ", error);
  })
}

function readFile(config, path) {
  const conn = getConnection(config);
  conn.then(pair => {
    console.log("sftp.OPEN_MODE = ", OPEN_MODE);
    pair.sftp.open(path, OPEN_MODE.READ, (error, handle) => {
      if (error) {
        console.log("sftp.open: path=" + path + ", error = " + error?.message);
        parentPort.postMessage(["data", path]);
      } else {
        console.log("sftp.open: path=" + path + ", handle = ", handle);
        pair.sftp.fstat(handle, (error, stats) => {
          console.log("sftp.fstat: path=" + path + ", stats = ", stats);
          if (stats && "size" in stats && stats.size > 0) {
            const array = new Uint8Array(1024);
            const off = 0;
            const len = array.byteLength;
            const position = 0;
            const arrayBuffer = array.buffer;
            const buffer = Buffer.from(arrayBuffer);

            const isBuffer = Buffer.isBuffer(buffer);

            if (!isBuffer)
              throw new Error("!Buffer.isBuffer(buffer)");

            const readCb = (err, bytesRead, bufferAdjusted, position) => {
              const baseBuffer = bufferAdjusted.buffer;
              const sameBB = baseBuffer === arrayBuffer;
              if (err) {
                parentPort.postMessage(["data", path]);
              } else {
                console.log("sftp.read complete: bytesRead = ",
                    bytesRead, ", position", position, ", buffer: ", bufferAdjusted)
                parentPort.postMessage(["data", path, bytesRead]);
              }
              closeHandle(pair.sftp, handle, path);
            };

            pair.sftp.read(handle, buffer, off, len, position, readCb);
          } else {
            parentPort.postMessage(["data", path]);
            closeHandle(pair.sftp, handle, path);
          }
        });
      }
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
