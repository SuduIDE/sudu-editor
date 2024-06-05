import fs from 'node:fs';
import path from 'path';

const noThrow = { throwIfNoEntry: false };
const O_RDONLY = fs.constants.O_RDONLY;

function isDirectory(dirName) {
  const stats = fs.lstatSync(dirName, noThrow);
  return stats ? stats.isDirectory() : false;
}

function traverseDir(dirName) {
  console.log("dirName: ", dirName);
  const result = fs.readdirSync(dirName);
  for (let i = 0; i < result.length; i++) {
    console.log("  [" + i + "]" + result[i]);
  }
  // for (const entry of result) {
  //   console.log("entry: " + entry);
  // }

  const readBuffer = new Uint8Array(1024 * 64 | 0);

  for (let i = 0; i < result.length; i++) {
    const child = dirName + path.sep + result[i];
    const stats = fs.lstatSync(child);
    if (stats.isDirectory()) {
      traverseDir(child);
    } else {
      let fd = fs.openSync(child, O_RDONLY);
      let read = 0, total = 0;
      do {
        read = fs.readSync(fd, readBuffer, 0, readBuffer.byteLength);
        if (read > 0) total += read;
      } while(read > 0);
      fs.closeSync(fd);
      console.log("  read file " + result[i] + " => " + total + " bytes");
    }
  }
}

const start = performance.now()

const args = process.argv;

if (args.length < 3) {
  console.log("usage node script folder")
} else {

  const dirname = args[2];

  console.log("args[2] = ", dirname);
  console.log("isDirectory = ", isDirectory(dirname));

  if (isDirectory(dirname)) {
    traverseDir(dirname);
  } else {
    console.error(dirname + " is not a directory")
  }

  let dT = (performance.now() - start);
  console.log("dT = ", dT);
}
