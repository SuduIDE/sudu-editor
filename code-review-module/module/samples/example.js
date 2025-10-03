
const editorApi = await import("../src/codereview.js");

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

console.log("threadPool.getNumThreads()=", threadPool.getNumThreads())

const codeReview = editorApi.newCodeReview({
    containerId: "editor", workers: threadPool
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

model1.setEditListener( m => console.log("model1 change event"))
model2.setEditListener( m => console.log("model2 change event"))

codeReview.setModel(model1, model2);
codeReview.focus();
