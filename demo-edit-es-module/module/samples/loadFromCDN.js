const cdn = "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.8-beta17"
const editorJs = "/src/editor.js";
const workerJS = "/src/worker.js"

const ep = import(cdn + editorJs)
const wp = fetch(cdn + workerJS).then(r => r.blob());
const editorApi = await ep;
const workerBlob = await wp;
let workerUrl = URL.createObjectURL(workerBlob);
const editor = await editorApi.newEditor({containerId: "editor", workerUrl: workerUrl});
URL.revokeObjectURL(workerUrl);

editor.setText("loaded from " + cdn + workerJS)
