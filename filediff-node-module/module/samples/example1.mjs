
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

const args = process.argv;

if (args.length < 3) {
  console.log("usage node script folder")
} else {
  const dirname = args[2];
  module.testFS(dirname);
}

module.startFolderDiff("left", "right", channel());






