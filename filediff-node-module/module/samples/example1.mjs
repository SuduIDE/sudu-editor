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

let module = await createDiffEngine(diffEngineWorker, nThreads);

let jobCount = 0;

function mayBeExit() {
  if (--jobCount === 0)
    module.dispose();
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

console.log("got module: ", module.constructor.name);

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

let args = process.argv;

function runTest() {
  switch (args.length) {
    case 2:
      return testFib();
    case 3:
      const dirname = args[2];
      return testFS(dirname);
    case 4: case 5:
      const dir1 = args[2];
      const dir2 = args[3];
      const content = args.length >= 5 && "content" === args[4];
      return testDiff(dir1, dir2, content);
    default:
      return "not running any test";
  }
}

const r = runTest();

if (r !== undefined) {
  console.log(r);
}

