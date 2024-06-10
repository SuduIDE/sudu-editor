import { createDiffEngine } from "../src/diffEngine.mjs";

let module = await createDiffEngine("../src/diffEngineWorker.mjs");

function channel() {
  return {
    sendMessage: m => { console.log("sendMessage: ", m); },
    set onMessage(handler) { console.log("setOnMessage: ", handler); }
  };
}

console.log("got module: ", module.constructor.name);

const fibFuture = module.fib(5).then(
    msg => console.log("got foo result: " + msg)
);

if (process.argv.length == 3) {
  const dirname = process.argv[2];
  module.testFS(dirname, () => {
    console.log("testFs.onComplete");
  });
} else if (process.argv.length == 4) {
  const dir1 = process.argv[2];
  const dir2 = process.argv[3];
  module.testFS2(dir1, dir2, () => {
    console.log("testFS2.onComplete");
  });

} else {
  console.log("not running testFS");
}

module.startFolderDiff("left", "right", channel());

await fibFuture;
module.dispose();
