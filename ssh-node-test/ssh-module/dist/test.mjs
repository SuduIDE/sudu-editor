
import {newSshClient} from "./ssh_mjs.mjs";

const conn = newSshClient();

console.log(conn);

const config = {
  host: '172.29.85.42',
      port: 22,
      username: 'kirill',
      password: 'gbpltw'
};


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
      conn.end();
    });
  });
}).connect(config);
