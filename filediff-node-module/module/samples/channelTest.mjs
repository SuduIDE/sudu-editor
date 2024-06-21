import {
  newRemoteChannelTest,
  setLogLevel,
  setLogOutput
} from "../src/diffEngine.mjs";

function logHandler(logLevel, text) {
  console.log("Logging at level " + logLevel + ": " + text);
}

setLogOutput(logHandler);
setLogLevel(5);

let channelTest = await newRemoteChannelTest(channel());

function channel() {
  return {
    onMessageHandler : null,
    sendMessage: function(m) {
      console.log("sendMessage: ", m);
      this.onMessageHandler(m);
    },
    set onMessage(handler) {
      this.onMessageHandler = handler;
      console.log("setOnMessage: ", handler); }
  };
}

channelTest.foo();
