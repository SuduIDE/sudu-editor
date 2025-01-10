console.log(`Hello, from worker thread`);

import {newSshClient} from "./ssh_mjs.mjs";

import {parentPort} from 'node:worker_threads';

function testReadDir(config) {
  const conn = newSshClient();
  conn.on('ready', () => {
    console.log('Client :: ready');
    conn.sftp((err, sftp) => {
      if (err) throw err;
      sftp.readdir('/', (err, list) => {
        if (err) throw err;
        console.log("list.length = ", list.length);
        for (let i = 0; i < list.length; i++) {
          let file = list[i];
          console.log("file" + i + " :", file.filename);
        }
        // for (const file in list) {
        //   console.log(file.filename, file.longname)
        // }
        conn.end();
        parentPort.postMessage("finish");
      });
    });
  }).connect(config);
}


parentPort.onmessage = function(message) {
  let data = message.data;
  console.log(`Worker: `, data);
  if ('cmd' in data) {
    console.log(`cmd =`, data.cmd);
    switch (data.cmd) {
      case "readDir":
        console.log(`readDir, ssh=`, data.ssh);
        testReadDir(data.ssh);
        break;
    }
  }

}
