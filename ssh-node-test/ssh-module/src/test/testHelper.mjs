

export function testConfig() {
  return {
    host: '172.29.85.42',
    port: 22,
    username: 'kirill',
    password: 'gbpltw'
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
