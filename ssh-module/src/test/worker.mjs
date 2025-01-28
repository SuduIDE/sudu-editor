console.log(`Hello, from worker thread`);

import {newSshClient, OPEN_MODE} from "../../dist/sshLib.mjs";

import {parentPort} from 'node:worker_threads';

console.log("sftp.OPEN_MODE = ", OPEN_MODE);

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
  const files = [];
  const folders = [];
  console.log("worker: listFiles, l =", list.length, "path:", path);
  for (let i = 0; i < list.length; i++) {
    const item = list[i];
    const filename = item.filename;
    const size = item.attrs.size;
    if (item.attrs.isDirectory()) {
      folders.push(filename);
    } else if (item.attrs.isFile()) {
      files.push({filename, size});
    }
    console.log("  [" + i + "]: " + item.filename + " -" +
        (item.attrs.isFile() ? " file" : "") +
        (item.attrs.isDirectory() ? " dir" : "") +
        (item.attrs.isSymbolicLink() ? " link" : ""));
  }
  parentPort.postMessage(["listed", path, folders, files]);
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
    pair.sftp.open(path, OPEN_MODE.READ, (error, handle) => {
      if (error) {
        console.log("sftp.open: path=" + path + ", error = " + error?.message);
        parentPort.postMessage(["data", path]);
      } else {
        console.log("sftp.open: path=" + path + ", handle = ", handle);
        pair.sftp.fstat(handle, (error, stats) => {
          console.log("sftp.fstat: path=" + path +
              ", isFile: " + stats.isFile() +
              ", stats.size = ", stats.size);

          if (stats && "size" in stats && stats.size > 3) {
            const array = new Uint8Array(stats.size - 3);
            const off = 0;
            const len = array.byteLength;
            const position = 3;
            const arrayBuffer = array.buffer;
            const buffer = Buffer.from(arrayBuffer);

            const isBuffer = Buffer.isBuffer(buffer);

            if (!isBuffer)
              throw new Error("!Buffer.isBuffer(buffer)");

            const readCb = (err, bytesRead, bufferAdjusted, position) => {
              const baseBuffer = bufferAdjusted.buffer;
              const array = new Int8Array(baseBuffer);
              const sameBuffer = baseBuffer === arrayBuffer;
              console.log("sftp.read complete: buffer is original", sameBuffer);
              if (err) {
                parentPort.postMessage(["data", path]);
              } else {
                console.log(
                    "sftp.read complete: bytesRead = ", bytesRead,
                    ", position", position,
                    ", buffer: ", bufferAdjusted.constructor.name, buffer.byteLength)
                const message = ["data", path, baseBuffer, bytesRead];
                const transferList = [baseBuffer];
                parentPort.postMessage(message, transferList);
                console.log("sftp.read complete: baseBuffer after transfer = ", baseBuffer);
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
