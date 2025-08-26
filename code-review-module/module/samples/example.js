const editorApi = await import("../src/codereview.js");

const codeReview = await editorApi.newCodeReview({
    containerId: "editor", workerUrl: "../src/worker.js"
});

function dup2exp(aString, power) {
  const dup = aString + aString;
  return power > 1 ? dup2exp(dup, power - 1) : dup;
}

const initialText1 = dup2exp(
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin\n", 7);

const initialText2 =
    dup2exp("This is a experimental project\n" +
    "to write an portable (Web + Desktop)\n" +
    "editor in kotlin and java\n", 7);

let model1 = editorApi.newTextModel(initialText1, null, "urlNew")
let model2 = editorApi.newTextModel(initialText2, null, "urlNew")

model1.setEditListener( m => console.log("model1 change event"))
model2.setEditListener( m => console.log("model2 change event"))

codeReview.setModel(model1, model2);
codeReview.focus();

function onTimer() {
  model1.setText(initialText1);
  setTimeout(onTimer, 2000)
}

setTimeout(onTimer, 2000)
