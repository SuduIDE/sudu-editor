

export function testConfig(args) {
  if (args.length < 6) {
    console.log("args: host port user password");
    process.exit(1);
  }

  return {
    host: args[2],
    port: args[3],
    username: args[4],
    password: args[5]
    //  privateKey: readFileSync('/path/to/my/key')
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
