const cdn = "../.."
const editorJs = "/src/editor.js";
const workerJS = "/src/worker.js"

const editorApi = await import(cdn + editorJs);
const editor = await editorApi.newEditor({containerId: "editor", workerUrl: cdn + workerJS});

editor.setText("loaded from " + cdn)
