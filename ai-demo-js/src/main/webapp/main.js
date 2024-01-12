const CDN = false
const path = CDN ? "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta7" : "./editor/"
const editorJs = "editor.js";
const workerJS = "worker.js"

const ep = import(path + editorJs)
const wp = fetch(path + workerJS).then(r => r.blob());
const editorApi = await ep;
const workerBlob = await wp;
let workerUrl = URL.createObjectURL(workerBlob);
const editor = await editorApi.newEditor({containerId: "codeEdit", workerUrl: workerUrl});
URL.revokeObjectURL(workerUrl);

// editor.setText("loaded from " + path)

const fileList = document.getElementById("fileList");
const respText1 = document.getElementById("respText1");
const respText2 = document.getElementById("respText2");

function fillFileList(text) {
  let split = text.split('\n');
  for (const file of split) {
    const fn = file.trim();
    if (fn.length > 0) addFile(fn);
  }
}

let selectedFile;
const modelMap = new Map();
const requestMap = new Map();

function setModel(model) {
  editor.setModel(model);
  editor.focus();
}


function putModel(fileName, text) {
  const model = editorApi.newTextModel(text, null, {path: fileName});
  modelMap.set(fileName, model);
  if (selectedFile === fileName) {
    setModel(model);
  }
}

function addFile(path) {
  const btn = document.createElement("button");
  btn.className = "fileButton";
  btn.innerText = path;
  fileList.appendChild(btn);

  btn.onclick = () => {
    selectedFile = path;
    const model = modelMap.get(selectedFile);
    if (model != null) {
      setModel(model);
    } else {
      if (requestMap.get(path) != null) {
        console.info("request in progress " + path);
      } else {
        console.info("fetch model " + path);
        fetch(path)
            .then(r => r.text())
            .then(text => putModel(path, text));
        requestMap.set(path, path)
      }
    }

    respText1.innerText = "clicked " + path;
  }
}

fetch("fileList.txt").then(r => r.text()).then(fillFileList);
