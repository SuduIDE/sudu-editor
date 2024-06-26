import { createDiffEngine } from "../src/diffEngine.mjs";

let module = await createDiffEngine("../src/diffEngineWorker.mjs");

function exit() {
  module.dispose()
}

function channel() {
  return {
    onMessageHandler : null,
    sendMessage: function(m) {
      console.log("sendMessage: ", m);
      this.onMessageHandler(m);
      exit()
    },
    set onMessage(handler) {
      this.onMessageHandler = handler;
      console.log("setOnMessage: ", handler);
    }
  };
}

if (process.argv.length === 4) {
  const dir1 = process.argv[2];
  const dir2 = process.argv[3];

  module.startFolderDiff(dir1, dir2, channel())
} else exit()
