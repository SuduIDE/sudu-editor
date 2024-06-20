import {
  createDiffEngine,
  setLogLevel,
  setLogOutput
} from "../src/diffEngine.mjs";

function logHandler(logLevel, text) {
  console.log("Logging at level " + logLevel + ": " + text);
}

setLogOutput(logHandler);
setLogLevel(5);

let module = await createDiffEngine("../src/diffEngineWorker.mjs");

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
  const fibFuture = module.fib(5).then(
      msg => {
        console.log("got foo result: " + msg);
        mayBeExit();
      }
  );
}

function testFS2(dir1, dir2) {
  jobCount++;
  module.testFS2(dir1, dir2, () => {
    console.log("testFS2.onComplete");
    mayBeExit();
  });
}

function testDiff(dir1, dir2) {
  jobCount++;
  module.testDiff(dir1, dir2, () => {
    console.log("testDiff.onComplete");
    mayBeExit();
  });
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
    case 4:
      const dir1 = args[2];
      const dir2 = args[3];
      return testDiff(dir1, dir2);
    case 5:
      const testId = args[2];
      switch (testId) {
        case "testFS2":
          const dir1 = args[3];
          const dir2 = args[4];
          return testFS2(dir1, dir2);
        default: return "unknown test id " + testId;
      }
    default:
      return "not running any test";
  }
}

const r = runTest();

if (r !== undefined) {
  console.log(r);
}

