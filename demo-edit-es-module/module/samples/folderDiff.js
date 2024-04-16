const editorApi = await import("../src/editor.js");
const codiconRef = "../../../codicon/src/main/resources/fonts/codicon.ttf";
const editor = await editorApi.newFolderDiff({    // newFolderDiff
    containerId: "editor", workerUrl: "../src/worker.js",
    codiconUrl : codiconRef
});

editor.focus();
