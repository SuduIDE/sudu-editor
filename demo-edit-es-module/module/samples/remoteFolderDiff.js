const editorApi = await import("../src/editor.js");
const codiconRef = "../../../codicon/src/main/resources/fonts/codicon.ttf";

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

const editor = await editorApi.newRemoteFolderDiff({    // newRemoteFolderDiff
  containerId: "editor", workerUrl: "../src/worker.js",
  codiconUrl : codiconRef
}, channel());

editor.focus();
