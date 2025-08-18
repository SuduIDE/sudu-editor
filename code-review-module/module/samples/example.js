const editorApi = await import("../src/codereview.js");

const codeReview = await editorApi.newCodeReview({
    containerId: "editor", workerUrl: "../src/worker.js"
});

const initialText1 =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

const initialText2 =
    "This is a experimental project\n" +
    "to write an portable (Web + Desktop)\n" +
    "editor in kotlin and java";

let model1 = editorApi.newTextModel(initialText1, null, "urlNew")
let model2 = editorApi.newTextModel(initialText2, null, "urlNew")

codeReview.setModel(model1, model2);
codeReview.focus();
