import fs from 'node:fs';


export function testConfig(args) {
  if (args.length < 6) {
    console.log("args: host port user password");
    process.exit(1);
  }

  const keyOrPass = args[5];
  const stats = fs.statSync(keyOrPass, {throwIfNoEntry:false});
  if (stats && stats.isFile()) {
    const key = fs.readFileSync(keyOrPass, 'utf8');
    console.log("using key file " + keyOrPass +
        ", " + key.length + " bytes");
    return {
      host: args[2],
      port: args[3],
      username: args[4],
      privateKey: key
    };
  }
  console.log("using password");

  return {
    host: args[2],
    port: args[3],
    username: args[4],
    password: keyOrPass
  };
}

export function testSsh(connection, config) {
  connection.on('ready', () => {
    console.log('Client :: ready');
    connection.sftp((err, sftp) => {
      if (err) throw err;
      sftp.readdir('/', (err, list) => {
        if (err) throw err;
        console.log("list.length = ", list.length);
        for (let i = 0; i < list.length; i++) {
          let file = list[i];
          console.log("file" + i + " :", file.filename);
        }
        connection.end();
      });
    });
  }).connect(config);
}
