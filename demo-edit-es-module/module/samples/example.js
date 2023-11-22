const editorApi = await import("../src/editor.js");

const editor = await editorApi.newEditor({
    containerId: "editor", workerUrl: "../src/worker.js"
});

const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

function modelChanged(modelChanged) {
    console.log("modelChanged"
        + ": old = " + modelChanged.oldModelUrl
        + ", new = " + modelChanged.newModelUrl);
}

editor.focus();

let model = editorApi.newTextModel(initialText, "language", "urlNew")

editor.onDidChangeModel(modelChanged);
editor.setModel(model);

let p31 = model.getPositionAt(31);
let p32 = model.getPositionAt(32);
let p18 = model.getPositionAt(18);
console.log("p31 = " + JSON.stringify(p31));
console.log("p32 = " + JSON.stringify(p32));
console.log("p18 = " + JSON.stringify(p18));

