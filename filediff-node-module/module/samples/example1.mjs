import { createDiffEngine } from "../src/module.mjs";

let module = await createDiffEngine("../src/worker.mjs");

function channel() {
  return {
    sendMessage: m => { console.log("sendMessage: ", m); },
    set onMessage(handler) { console.log("setOnMessage: ", handler); }
  };
}

console.log("got module: ", module.constructor.name);

module.fib(5).then(
    msg => console.log("got foo result: " + msg)
);

if (process.argv.length > 2) {
  const dirname = process.argv[2];
  module.testFS(dirname);
}

module.startFolderDiff("left", "right", channel());
