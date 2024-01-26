import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';

mermaid.initialize({startOnLoad: false});

const initialText = "activity {\n" +
    "  select{A1,B1, C1};\n" +
    "  A1;\n" +
    "  select{\n" +
    "    sequence {\n" +
    "      select{(A1)E1, F1};\n" +
    "      G1;\n" +
    "      H1;\n" +
    "      select{\n" +
    "        (E1) X1, \n" +
    "        Y1, \n" +
    "        (A1 and (B1 or C1)) Z1\n" +
    "      }\n" +
    "    };\n" +
    "    sequence {\n" +
    "      G1;\n" +
    "      select{M1, N1};\n" +
    "      H1;\n" +
    "      select{\n" +
    "       ({A1->A1} and M1) A1;\n" +
    "       B1;\n" +
    "       ({A1,G1} and N1) C1\n" +
    "      }\n" +
    "    }\n" +
    "  }  \n" +
    "}";

//CDN
// const path = "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta12"

//LOCAL
const path = "./editor/";
const workerSrc = fetch(path + "worker.js").then(r => r.blob());
const editorApi = await import(path + "editor.js");
const workerUrl = URL.createObjectURL(await workerSrc);
const editor = await editorApi.newEditor({containerId: "editor", workerUrl: workerUrl});

let model = editorApi.newTextModel(initialText, "activity")
editor.setModel(model);

let session = null;

function setSession(newSession) {
  session = newSession;
  console.log("new session: " + session)
}

function sourceBody() {
  return { method: "POST", body: model.getText()};
}

function setSeedBody(seed) {
  return { method: "POST", body: String(seed)};
}

async function setDag1(dag1) {
  drawDagButton.enabled = true;
  // console.log("setDag1: " + dag1)
  const element = document.getElementById("mermaidPane");
  const graphDefinition = theme + "\n" + dag1;
  const {svg} = await mermaid.render('graphDiv', graphDefinition);
  element.innerHTML = svg;
}

function setSeed(next) {
    fetch("/setSeed?id=" + session, setSeedBody(seedNow()))
        .then(next);
}

async function setDag2(dag2) {
  let element = document.getElementById("mermaidPane2");
  const graphDefinition = theme + "\n" + dag2;
  const {svg} = await mermaid.render('graphDiv1', graphDefinition);
  element.innerHTML = svg;
}

function sendSrc() {
  if (session != null) {
    return fetch("/setSrc?id=" + session, sourceBody())
        .then(r => {
          console.log("setSrc status " + r.statusText);
        });
  } else {
    return fetch("/newSession", sourceBody())
        .then(r => r.text())
        .then(setSession)
  }
}

function fetchMermaid1() {
  fetch("/dag1?id=" + session)
      .then(r => r.text())
      .then(setDag1)
  fetch("/dag2?id=" + session)
      .then(r => r.text())
      .then(setDag2)
}


function getMermaid() {
  drawDagButton.enabled = false;
  return sendSrc().then(() => setSeed(fetchMermaid1));
}

const seedEdit = document.getElementById("seedText");
const drawDagButton = document.getElementById("drawDag");
const pathsButton = document.getElementById("calcPaths");
const pathsDiv = document.getElementById("paths");

drawDagButton.addEventListener('click', getMermaid)
pathsButton.addEventListener('click', calcPaths)
seedEdit.addEventListener('keypress', keyPressed)
document.getElementById("randomSeed").addEventListener('click', randomSeed)

function showPaths(text) {
  let paths = JSON.parse(text);
  console.log("calculatePaths: " + paths)
  for (let k = 0; k < paths.length; k++) {
    const pp = paths[k];
    let e = document.getElementById("p" + k);
    e.innerHTML = ''
    for (let i = 0; i < pp.length; i++) {
      const p = document.createElement("div");
      p.id = "p" + k + "-" + i
      p.className = "path"
      p.onclick = () => highlightPath(k, i);
      p.style.color = 'white'
      p.innerText = pp[i];
      e.appendChild(p)
      //console.log(pp[i]);
    }
  }
  pathsDiv.style.visibility = "visible";
}

function requestCalcPaths() {
  fetch("/calculatePaths?id=" + session)
      .then(r => r.text())
      .then(showPaths);
}

function calcPaths() {
  if (session == null) {
    getMermaid().then(requestCalcPaths)
  } else {
    requestCalcPaths();
  }
}

const theme = "%%{\n" +
    "  init: {\n" +
    "    'theme': 'dark',\n" +
    "    'themeVariables': {\n" +
    "      'primaryColor': '#BB2528',\n" +
    "      'primaryTextColor': '#fff',\n" +
    "      'primaryBorderColor': '#7C0000',\n" +
    "      'lineColor': '#F8B229',\n" +
    "      'secondaryColor': '#006100',\n" +
    "      'tertiaryColor': '#fff'\n" +
    "    }\n" +
    "  }\n" +
    "}%%"

function highlightPath(pathGroupIdx, idx) {
  //highlight div
  const allPaths = document.getElementsByClassName("path");
  for (let i = 0; i < allPaths.length; i++) {
    allPaths[i].style.color = 'white'
  }
  document.getElementById("p" + pathGroupIdx + "-" + idx).style.color = 'red'

  fetch("/highlight?id=" + session + "&pathGroupIdx=" + pathGroupIdx + "&idx=" + idx)
      .then(r => r.text())
      .then(setDag2);
}

function keyPressed(e) {
  if (e.keyCode === 13) {
    e.preventDefault();
    applySeed();

  } else if (e.charCode < 48 || e.charCode > 57) {
    e.preventDefault();
  }
}

function randomSeed() {
  seedEdit.value = Math.floor(Math.random() * 9999)
  applySeed();
}

function seedNow() {
  let seed = seedEdit.value;
  if (seed === '') {
    seedEdit.value = 42;
    seed = 42;
  }
  return seed;
}

function applySeed() {
  if (session == null) {
    console.log("there is no session to apply seed");
    return;
  }
  getMermaid();
}
