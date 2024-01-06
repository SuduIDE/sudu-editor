if (true) {
    const cdn = "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta7"
    const editorJs = "/src/editor.js";
    const workerJS = "/src/worker.js"

    const ep = import(cdn + editorJs)
    const wp = fetch(cdn + workerJS).then(r => r.blob());
    const editorApi = await ep;
    const workerBlob = await wp;
    let workerUrl = URL.createObjectURL(workerBlob);
    const editor = await editorApi.newEditor({containerId: "codeEdit", workerUrl: workerUrl});
    URL.revokeObjectURL(workerUrl);

    editor.setText("loaded from " + cdn)
}

const fileList = document.getElementById("fileList");
const respText1 = document.getElementById("respText1");
const respText2 = document.getElementById("respText2");

for (let i = 0; i < 100; i++) {
    const btn = document.createElement("button");
    btn.className = "fileButton";
    btn.innerText = "path/to/file " + String(i + 1);
    fileList.appendChild(btn);

    btn.onclick = () => {
        respText1.innerText = "file " + String(i + 1) + " response text 1\nNewLine"
        respText2.innerText = "file " + String(i + 1) + " response text 2\nNewLine"
    }
}


