

function main() {
  window.editorApi = function (arg) {
    let worker = new Worker("worker.js");
    worker.postMessage(arg);

    const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

    return {
      setText: function (text) {
        worker.postMessage(text);
        console.log("setText: " + text)
      }
    }
  }
}

main();

export function newEditor(arg) {
  return window.editorApi(arg);
}


