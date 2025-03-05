import {createDiffEngine, setLogLevel, setLogOutput} from "./../src/diffEngine.mjs";

import fs from 'node:fs';
import path from 'node:path';
import url from 'node:url';

const nThreads = 3;
const timeoutTime = 1000;

const __dirname = path.dirname(
    url.fileURLToPath(new URL(import.meta.url)));

function logHandler(logLevel, text) {
  // console.log("Logging at level " + logLevel + ": " + text);
}

setLogOutput(logHandler);
setLogLevel(5);

const diffEngineWorker = path.join(__dirname, "../src/diffEngineWorker.mjs")

let diffEngine = await createDiffEngine(diffEngineWorker, nThreads);
let module = diffEngine.testApi();

if (!module) {
  console.log("diffEngine.testApi() in not exposed");
  process.exit(-1);
}

console.log("got module: ", module.constructor.name);

let jobCount = 0;

function mayBeExit() {
  if (--jobCount <= 0) {
    if (timeoutTime == 0)
        diffEngine.dispose();
    else
      setTimeout( () => diffEngine.dispose(), timeoutTime);
  }
}

function channel() {
  return {
    onMessageHandler : null,
    sendMessage: function(m) {
      console.log("sendMessage: ", m);
      this.onMessageHandler(m);
      mayBeExit()
    },
    set onMessage(handler) {
      this.onMessageHandler = handler;
      console.log("setOnMessage: ", handler); }
  };
}

function testFib() {
  jobCount++;
  const fibFuture = module.testFib(5).then(
      msg => {
        console.log("got foo result: " + msg);
        mayBeExit();
      }
  );
}

function testDiff(dir1, dir2, content) {
  jobCount++;
  const onComplete = () => {
    console.log("testDiff.onComplete");
    mayBeExit();
  };
  module.testDiff(dir1, dir2, content, onComplete);
}

function testFS(args) {
  const dirname = args[3];
  console.log("dirname", dirname);
  jobCount++;
  module.testFS(dirname, () => {
    console.log("testFs.onComplete");
    mayBeExit();
  });
}

function testFileWrite(args) {
  const file = args[3];
  const encoding = args[4];
  const content = args[5];
  console.log("file", file);
  console.log("encoding", encoding);
  console.log("content", content);

  if (!file || !encoding || !content) {
    console.log();
    mayBeExit();
    return "bad args, usage: file encoding content";
  }

  jobCount++;
  module.testFileWrite(file, content, encoding,
      onComplete("testFileWrite"),
      onError("testFileWrite"));
}

function testFileReadWrite(args) {
  if (!args[3]) {
    mayBeExit();
    return "usage: testFileReadWrite file1 [file2] ..."
  }
  for (let i = 3; i < args.length; i++) {
    const fileFrom = args[i];
    const fileTo = fileFrom + '.testRW';
    console.log("fileFrom", fileFrom);
    console.log("fileTo", fileTo);
    jobCount++;
    module.testFileReadWrite(fileFrom, fileTo,
        onComplete("testFileReadWrite"),
        onError("testFileReadWrite"));
  }
}

function sshFile(ssh, path) {
  return { path, ssh };
}

function testFileReadWriteSsh(args) {
  const ssh = sshConfig(args, 3);
  const file0 = args[3+4];

  if (!args || !file0) {
    mayBeExit();
    return "args: ssh[4] file1 [file2 ...]";
  }

  console.log("ssh:", {host: ssh.host, username: ssh.username});

  for (let i = 3+4; i < args.length; i++) {
    const fileFrom = args[i];
    console.log("fileFrom", fileFrom);
    const fileTo = fileFrom + '.testRW';

    jobCount++;
    module.testFileReadWrite(
        sshFile(ssh, fileFrom), sshFile(ssh, fileTo),
        onComplete("testFileReadWriteSsh"),
        onError("testFileReadWriteSsh"));
  }
}

function onComplete(title) {
  return () => {
    console.log(title + ".onComplete");
    mayBeExit();
  };
}

function onError(title) {
  return (errorString) => {
    console.log(title + ".onError: ", errorString);
    mayBeExit();
  };
}

function testNodeFsCopyFile(args) {
  const src = args[3];
  const dest = args[4];
  console.log("testNodeFsCopyFile: src", src);
  console.log("testNodeFsCopyFile: dest", dest);
  jobCount++;
  module.testNodeFsCopyFile(src, dest,
      onComplete("testNodeFsCopyFile"),
      onError("testNodeFsCopyFile")
  )
}

function testNodeFsCopyDirectory(args) {
  const src = args[3];
  const dest = args[4];
  console.log("testNodeFsCopyDirectory: src", src);
  console.log("testNodeFsCopyDirectory: dest", dest);

  jobCount++;
  module.testNodeFsCopyDirectory(src, dest,
      onComplete("testNodeFsCopyDirectory"),
      onError("testNodeFsCopyDirectory"));
}

function testCopyFileToFolder(src, destDir, destFile) {
  jobCount++;
  module.testCopyFileToFolder(src, destDir, destFile,
      onComplete("testCopyFileToFolder"),
      onError("testCopyFileToFolder"));
}

function testGbkEncoder() {
  jobCount++;
  module.testGbkEncoder();
  mayBeExit();
  return "ok";
}

function testSsh(args) {
  const ssh = sshConfig(args, 3);

  if (!(ssh && args.length > 6)) {
    mayBeExit();
    return "test ssh: error in args: ssh[4] command args command arg";
  }

  const exitHandler = () => { mayBeExit(); };
  console.log("test ssh: ", ssh);

  for (let i = 7; i < args.length; i += 2) {
    const sshRef = { path: args[i+1], ssh:ssh };
    switch (args[i]) {
      case "dir": {
        jobCount++;
        module.testSshDir(sshRef, exitHandler);
        break;
      }
      case "file": {
        jobCount++;
        module.testSshFile(sshRef, exitHandler);
        break;
      }
      case "aDir": {
        jobCount++;
        module.testSshDirAsync(sshRef, exitHandler);
        break;
      }
      case "aFile": {
        jobCount++;
        module.testSshFileAsync(sshRef, exitHandler);
        break;
      }
      case "aaDir": case "aaFile": break;
      default :
        throw new Error("bad test arg " + args[i]);
    }
  }

  if (jobCount === 0) mayBeExit();

  return "testSsh";
}

function testNodeBuffer() {
  jobCount++;
  module.testNodeBuffer(() => {
    mayBeExit();
  });
}

function testFileDeleteSsh(args) {
  const ssh = sshConfig(args, 3);
  const file = args[3 + 4];

  if (!ssh || !file) {
    console.log("args: ssh[4] fileToDelete");
    mayBeExit();
    return;
  }

  console.log("ssh", ssh);
  console.log("file", file);

  jobCount++;
  module.testDeleteFile(
      sshFile(ssh, file), onComplete("testFileDeleteSsh"));
}

function runAppendTest(file, str1, str2) {
  console.log("file", JSON.stringify(file));

  jobCount++;
  module.testFileAppend(
      file, str1, str2,
      onComplete("testFileAppend"),
      onError("testFileAppend")
  );
}

function testFileAppend(args) {
  const file = args[3];
  const str1 = args[4];
  const str2 = args[5];

  if (!args || !file || !str1 ||!str2) {
    console.log("args: ssh[4] file string1 string2");
    mayBeExit();
    return;
  }

  // console.log("ssh", ssh);
  runAppendTest(file, str1, str2);
}

function testFileAppendSsh(args) {
  const ssh = sshConfig(args, 3);
  const file = args[3+4];
  const str1 = args[4+4];
  const str2 = args[5+4];

  if (!args || !file || !str1 ||!str2) {
    console.log("args: ssh[4] file string1 string2");
    mayBeExit();
    return;
  }

  // console.log("ssh", ssh);
  runAppendTest(sshFile(ssh, file), str1, str2);
}

function testListRemoteDirectory(args) {
  const ssh = sshConfig(args, 3);
  const dir = args[3 + 4];
  const withFiles = args[3 + 5] === 'true';

  if (!ssh || !dir) {
    console.log("args: ssh[4] dir");
    mayBeExit();
    return;
  }

  console.log("ssh", ssh);
  console.log("dir", dir);
  console.log("withFiles", withFiles);

  jobCount++;
  diffEngine.listRemoteDirectory(sshFile(ssh, dir), withFiles).then(
      list => {
        console.log("listRemoteDirectory: ", list);
        mayBeExit();
      }, error => {
        console.error("listRemoteDirectory error: ", error);
        mayBeExit();
      }
  );
}

// mkdir error.copy
// testCopyFileToFolder copyToDir zeroFile WIN32.ipch 3MbFile gb2312.txt error
function testCopyFileToFolderSsh(args) {
  const ssh = sshConfig(args, 3);
  const destDir = args[7];
  const file1 = args[8];
  console.log("testCopyFileToFolderSsh: host", ssh.host);
  console.log("   destDir", destDir);
  if (!destDir || !file1) {
    return "testCopyFileToFolderSsh usage: ssh[4] destDir file1 file2 file3";
  }
  const sshDir = sshFile(ssh, destDir);
  for (let i = 1; 7 + i < args.length; i++) {
    const file = args[7 + i];
    const src = sshFile(ssh, file);
    const destFile = sshFile(ssh, file + ".copy");
    console.log("testCopyFileToFolderSsh: file" + i, file, "dest file", destFile.path);
    testCopyFileToFolder(src, sshDir, destFile);
  }
}

function testCopyFileToFolderLocal(args) {
  const destDir = args[3];
  const file1 = args[4];
  if (!destDir || !file1) {
    mayBeExit();
    return "testCopyFileToFolderLocal usage: destDir file1 file2 file3";
  }
  console.log("testCopyFileToFolder: destDir", destDir);
  for (let i = 1; 3 + i < args.length; i++) {
    const file = args[3 + i];
    console.log("testCopyFileToFolder: file" + i, file);
    testCopyFileToFolder(file, destDir, file + ".copy");
  }
}

function testDiffSsh(args) {
  const ssh = sshConfig(args, 3);
  const sshRoot = args[7];
  const localRoot = args[8];
  if (ssh && sshRoot && localRoot) {
    const sshRef = {path: sshRoot, ssh: ssh};
    return testDiff(sshRef, localRoot, true);
  } else {
    mayBeExit();
    return "error in args";
  }
}

function testDiffLocal(args) {
  const dir1 = args[3];
  const dir2 = args[4];
  const content = args.length >= 5 && "content" === args[5];
  console.log("dir1", dir1);
  console.log("dir2", dir2);
  console.log("content", content);
  return testDiff(dir1, dir2, content);
}

function testMkDirSsh(args) {
  const ssh = sshConfig(args, 3);
  const dir = args[3 + 4];
  const name = args[4 + 4];

  if (!dir || !name) {
    mayBeExit();
    return "args: ssh[4] folder newName";
  }

  console.log("ssh", ssh);
  console.log("dir", dir);
  console.log("name", name);

  jobCount++;
  module.testMkDir(sshFile(ssh, dir), name,
      onComplete("testMkDirSsh"), onError("testMkDirSsh"));
}

function testMkDir(args) {
  const dir = args[3];
  const name = args[4];

  if (!dir || !name) {
    mayBeExit();
    return "args: folder newName";
  }

  console.log("dir", dir);
  console.log("name", name);

  jobCount++;
  module.testMkDir(dir, name,
      onComplete("testMkDirSsh"), onError("testMkDirSsh"));
}

function testRemoveDirSsh(args) {
  const ssh = sshConfig(args, 3);
  const dir = args[3 + 4];

  if (!dir) {
    mayBeExit();
    return "args: ssh[4] folder";
  }

  console.log("ssh", ssh);
  console.log("dir", dir);

  jobCount++;
  module.testRemoveDir(sshFile(ssh, dir),
      onComplete("testMkDirSsh"), onError("testMkDirSsh"));
}

function sshWithPass(host, port, username, password) {
  return {host, port, username, password};
}

function sshWithPassC(host, port, username, password) {
  return sshWithPass(cloneString(host), cloneString(port),
      cloneString(username), cloneString(password));
}

function sshWithKey(host, port, username, privateKey) {
  return {host, port, username, privateKey};
}

function sshWithKeyC(host, port, username, privateKey) {
  return sshWithKey(cloneString(host), cloneString(port),
      cloneString(username), cloneString(privateKey));
}

const te = new TextDecoder();
const td = new TextEncoder();

function cloneString(string) {
  return te.decode(td.encode(string));
}

function testSshHash(args) {

  function checkEquals(ssh1, ssh2, module, result) {
    const r = module.testSshHash(ssh1, ssh2);
    if ((r[0] && !result) || (!r[0] && result)) {
      console.log("checkEquals failed ssh1 =", ssh1, "ssh2 =", ssh2);
      throw "SshHash check equal failed";
      }
  }

  function checkHash(ssh1, ssh2, module, result) {
    const r = module.testSshHash(ssh1, ssh2);
    if ((r[1] === r[2]) !== result) {
      console.log("checkHash failed ssh1 =", ssh1);
      console.log("    ssh2 =", ssh2);
      throw "SshHash hash check failed";
    }
  }

  function checkHashDifferent(a, b, c, d, e, f, g, h, module) {
    const p1 = sshWithPassC(a, b, c, d);
    const p2 = sshWithPassC(e, f, g, h);
    checkHash(p1, p2, module, false);
    checkEquals(p1, p2, module, false);
    const c1 = sshWithKeyC(a, b, c, d);
    const c2 = sshWithKeyC(e, f, g, h);
    checkHash(c1, c2, module, false);
    checkEquals(c1, c2, module, false);
  }

  const ht = "host";
  const pt = "22";
  const us = "myUser";
  const ps = "pass";
  const ky = "myKey";
  try {
    checkEquals(
        sshWithPassC(ht, pt, us, ps),
        sshWithKeyC(ht, pt, us, ps),
        module, false);

    checkEquals(
        sshWithPassC(ht, pt, us, ps),
        sshWithPassC(ht, pt, us, ps),
        module, true);

    checkEquals(
        sshWithKeyC(ht, pt, us, ky),
        sshWithKeyC(ht, pt, us, ky),
        module, true);

    checkHash(
        sshWithPassC("a", "b", "c", "d"),
        sshWithPassC("a", "b", "c", "d"),
        module, true);

    checkHash(
        sshWithKeyC("a", "b", "c", "d"),
        sshWithKeyC("a", "b", "c", "d"),
        module, true);

    checkHash(
        sshWithPassC("a", "b", "c", "d"),
        sshWithKeyC("a", "b", "c", "d"),
        module, false);

    checkHashDifferent(
        "a1", "b", "c", "d",
        "a2", "b", "c", "d",
        module);

    checkHashDifferent(
        "a", "b1", "c", "d",
        "a", "b2", "c", "d",
        module);

    checkHashDifferent(
        "a", "b", "c1", "d",
        "a", "b", "c2", "d",
        module);

    checkHashDifferent(
        "a", "b", "c", "d1",
        "a", "b", "c", "d2",
        module);

    console.log("testSshHash passed");
  } catch (e) {
    console.log("testSshHash failed")
  }
  mayBeExit();
}

function runTest() {
  let args = process.argv;
  const cmd = args[2];
  switch (cmd) {
    case "fib":  return testFib();
    case "testFS": return testFS(args);
    case "testDiff": return testDiffLocal(args);
    case "testFileWrite": return testFileWrite(args);
    case "testFileReadWrite":return testFileReadWrite(args);
    case "testFileReadWriteSsh":return testFileReadWriteSsh(args);
    case "testNodeFsCopyFile": return testNodeFsCopyFile(args);
    case "testNodeFsCopyDirectory": return testNodeFsCopyDirectory(args);
    case "testCopyFileToFolder": return testCopyFileToFolderLocal(args);
    case "testCopyFileToFolderSsh": return testCopyFileToFolderSsh(args);
    case "testGbkEncoder": return testGbkEncoder();
    case "testSsh": return testSsh(args);
    case "testDiffSsh": return testDiffSsh(args);
    case "testNodeBuffer": return testNodeBuffer();
    case "testFileDeleteSsh": return testFileDeleteSsh(args);
    case "testFileAppend": return testFileAppend(args);
    case "testFileAppendSsh": return testFileAppendSsh(args);
    case "testListRemoteDirectory": return testListRemoteDirectory(args);
    case "testMkDir": return testMkDir(args);
    case "testMkDirSsh": return testMkDirSsh(args);
    case "testRemoveDirSsh": return testRemoveDirSsh(args);
    case "testSshHash": return testSshHash(args);

    default:
      mayBeExit();
      return "not running any test";
  }
}

const r = runTest();

if (r !== undefined) {
  console.log(r);
}

function sshConfig(args, s) {
  if (args.length < s + 4) {
    console.log("args: host port user password");
    return null;
  }

  const keyOrPass = args[s + 3];
  const stats = fs.statSync(keyOrPass, {throwIfNoEntry:false});
  if (stats && stats.isFile()) {
    const key = fs.readFileSync(keyOrPass, 'utf8');
    console.log("using key file " + keyOrPass +
        ", " + key.length + " bytes");
    return {
      host: args[s],
      port: args[s + 1],
      username: args[s + 2],
      privateKey: key
    };
  }

  return {
    host: args[s],
    port: args[s + 1],
    username: args[s + 2],
    password: keyOrPass
  };
}
