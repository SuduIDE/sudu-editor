document.addEventListener("DOMContentLoaded", () => console.log("DOMContentLoaded"))
const button = document.getElementById('doit');
button.onclick = () => {
    button.style.height = '100px';
};
const editorApi = await import("../src/editor.js");

console.log("main");
editorApi.newEditor({containerId: "editor", workerUrl: "../src/worker.js"})
    .then(useEditor, error => console.error(error));

const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

function modelChanged(modelChanged) {
    console.log("modelChanged"
        + ": old = " + modelChanged.oldModelUrl
        + ", new = " + modelChanged.newModelUrl);
}

function useEditor(editor) {
    editor.focus();

    let model = editorApi.newTextModel(initialText, "language", "urlNew")

    editor.onDidChangeModel(modelChanged);
    editor.setModel(model);
    // editor.setText("");

    let p31 = model.getPositionAt(31);
    let p32 = model.getPositionAt(32);
    let p18 = model.getPositionAt(18);
    console.log("p31 = " + JSON.stringify(p31));
    console.log("p32 = " + JSON.stringify(p32));
    console.log("p18 = " + JSON.stringify(p18));
}

