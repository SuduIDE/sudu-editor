const editorApi = await import("../src/codereview.js");

const codeReview = await editorApi.newCodeReview({
    containerId: "editor", workerUrl: "../src/worker.js"
});

const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

let model1 = editorApi.newTextModel(initialText, null, "urlNew")
let model2 = editorApi.newTextModel(initialText, null, "urlNew")

codeReview.setModel(model1, model2);
codeReview.focus();
