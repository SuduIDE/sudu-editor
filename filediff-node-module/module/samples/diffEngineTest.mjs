import {
  createDiffEngine, setLogLevel, setLogOutput
} from "./../src/diffEngine.mjs";

import path from 'node:path';
import url from 'node:url';

const nThreads = 3;

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
  if (--jobCount <= 0)
    diffEngine.dispose();
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

function testFS(dirname) {
  jobCount++;
  module.testFS(dirname, () => {
    console.log("testFs.onComplete");
    mayBeExit();
  });
}

function testFileWrite(file, encoding, content) {
  jobCount++;
  module.testFileWrite(file, content, encoding,
      () => {
        console.log("testFileWrite.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileWrite.onError: ", errorString);
        mayBeExit();
      }
  )
}

function testFileReadWrite(args) {
  const fileFrom = args[3];
  const fileToS = args[4];
  const fileToJ = args[5];
  console.log("fileFrom", fileFrom);
  console.log("fileToS", fileToS);
  console.log("fileToJ", fileToJ);

  jobCount++;
  module.testFileReadWrite(fileFrom, fileToS, fileToJ,
      () => {
        console.log("testFileReadWrite.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileReadWrite.onError: ", errorString);
        mayBeExit();
      }
  )
}

function sshFile(ssh, path) {
  return { path, ssh };
}

function testFileReadWriteSsh(args) {
  const ssh = sshConfig(args, 3);
  const fileFrom = args[3+4];
  const fileToS = args[4+4];
  const fileToJ = args[5+4];

  if (!args || !fileFrom || !fileToS || !fileToJ) {
    console.log("args: ssh[4] fileFrom fileTo1 fileTo2");
    mayBeExit();
    return;
  }

  console.log("ssh", ssh);
  console.log("fileFrom", fileFrom);
  console.log("fileToS", fileToS);
  console.log("fileToJ", fileToJ);

  jobCount++;
  module.testFileReadWrite(sshFile(ssh, fileFrom),
      sshFile(ssh, fileToS), sshFile(ssh, fileToJ),
      () => {
        console.log("testFileReadWriteSsh.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileReadWriteSsh.onError: ", errorString);
        mayBeExit();
      }
  )
}

function testFileCopy(src, dest) {
  jobCount++;
  module.testFileCopy(src, dest,
      () => {
        console.log("testFileCopy.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileWrite.onError: ", errorString);
        mayBeExit();
      }
  )
}

function testDirCopy(src, dest) {
  jobCount++;
  module.testDirCopy(src, dest,
      () => {
        console.log("testFileCopy.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileWrite.onError: ", errorString);
        mayBeExit();
      }
  )
}

function testGbkEncoder() {
  jobCount++;
  module.testGbkEncoder();
  mayBeExit();
  return "ok";
}

function testSsh(ssh, args) {
  const exitHandler = () => { mayBeExit(); };
  console.log("test ssh: ", ssh);
  // ose_DiffTestApi_testSsh$exported$5

  for (let i = 0; i < args.length; i += 2) {
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

  if (jobCount == 0) mayBeExit();

  return "testSsh";
}

function testNodeBuffer() {
  jobCount++;
  module.testNodeBuffer(() => {
    mayBeExit();
  });
}

let args = process.argv;

function runTest() {
  const cmd = args[2];
  switch (cmd) {
    case "fib":
      return testFib();
    case "testFS":
      const dirname = args[3];
      console.log("dirname", dirname);
      return testFS(dirname);
    case "testDiff":
      const dir1 = args[3];
      const dir2 = args[4];
      const content = args.length >= 5 && "content" === args[5];
      console.log("dir1", dir1);
      console.log("dir2", dir2);
      console.log("content", content);
      return testDiff(dir1, dir2, content);
    case "testFileWrite": {
      const file = args[3];
      const encoding = args[4];
      const string = args[5];
      console.log("file", file);
      console.log("encoding", encoding);
      console.log("string", string);
      return testFileWrite(file, encoding, string);
    }
    case "testFileReadWrite":
      return testFileReadWrite(args);

    case "testFileReadWriteSsh":
      return testFileReadWriteSsh(args);

    case "testFileCopy": {
      const src = args[3];
      const dest = args[4];
      console.log("src", src);
      console.log("dest", dest);
      return testFileCopy(src, dest);
    }
    case "testDirCopy": {
      const src = args[3];
      const dest = args[4];
      console.log("testDirCopy: src", src);
      console.log("testDirCopy: dest", dest);
      return testDirCopy(src, dest);
    }
    case "testGbkEncoder": {
      return testGbkEncoder();
    }
    case "testSsh": {
      const ssh = sshConfig(args, 3);
      if (ssh && args.length > 6) {
        const slice = args.slice(7, args.length);
        return testSsh(ssh, slice);
      } else {
        mayBeExit();
        return "error in args";
      }
    }
    case "testDiffSsh": {
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
    case "testNodeBuffer": {
      testNodeBuffer();
      return undefined;
    }
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

  return {
    host: args[s],
    port: args[s + 1],
    username: args[s + 2],
    password: args[s + 3]
    //  privateKey: readFileSync('/path/to/my/key')
  };
}
