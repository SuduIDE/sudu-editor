const CDN = false
const path = CDN ? "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta7" : "./editor/"
const editorJs = "editor.js";
const workerJS = "worker.js"

const ep = import(path + editorJs)
const wp = fetch(path + workerJS).then(r => r.blob());
const editorApi = await ep;
const params = {
  containerId: "codeEdit",
  workerUrl: URL.createObjectURL(await wp)
};
const editor = await editorApi.newEditor(params);

const fileList = document.getElementById("fileList");
const respText1 = document.getElementById("respText1");
const respText2 = document.getElementById("respText2");

function fillFileList(text) {
  let split = text.split('\n');
  for (const file of split) {
    const fn = file.trim();
    if (fn.length > 0) {
      fileList.appendChild(fileButton(fn));
    }
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

function selectFile(path) {
  respText1.innerText = "selectedFile = " + path;
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
}

function fileButton(path) {
  const btn = document.createElement("button");
  btn.className = "fileButton";
  btn.innerText = path;
  btn.onclick = () => selectFile(path);
  return btn;
}

fetch("fileList.txt").then(r => r.text()).then(fillFileList);
