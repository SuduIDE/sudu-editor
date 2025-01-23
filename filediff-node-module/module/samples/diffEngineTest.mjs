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

function testFileReadWrite(fileFrom, fileToS, fileToJ) {
  jobCount++;
  module.testFileReadWrite(fileFrom, fileToS, fileToJ,
      () => {
        console.log("testFileReadWrite.onComplete");
        mayBeExit();
      },
      (errorString) => {
        console.log("testFileWrite.onError: ", errorString);
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

function testSsh(ssh, path) {
  jobCount++;
  const fileInputSsh = { path, ssh };
  // ose_DiffTestApi_testSsh$exported$5
  module.testSsh(fileInputSsh);
  return "testSsh";
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
    case "testFileReadWrite": {
      const fileFrom = args[3];
      const fileToS = args[4];
      const fileToJ = args[5];
      console.log("fileFrom", fileFrom);
      console.log("fileToS", fileToS);
      console.log("fileToJ", fileToJ);
      return testFileReadWrite(fileFrom, fileToS, fileToJ);
    }
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
      const path = args[8];
      if (ssh && path) {
        return testSsh(ssh, path);
      } else {
        mayBeExit();
        return "error in args";
      }
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
