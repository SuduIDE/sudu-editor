import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';
mermaid.initialize({ startOnLoad: false });

const initialText = "activity {\n" +
    "  select{A1,B1};\n" +
    "  A2;\n" +
    "  repeat(2){A4};\n" +
    "  if ({A1->A2}) {A4};\n" +
    "  if (A4) {A4};\n" +
    "  else {A5}\n" +
    "}";

//CDN
// const cdn = "https://cdn.jsdelivr.net/npm/sudu-editor-tmp@0.0.9-beta7"
// const editorJs = "/src/editor.js";
// const workerJS = "/src/worker.js";


// const ep = import(cdn + editorJs)
// const wp = fetch(cdn + workerJS).then(r => r.blob());
// const editorApi = await ep;
// const workerBlob = await wp;
// let workerUrl = URL.createObjectURL(workerBlob);
// const editor = await editorApi.newEditor({containerId: "editor", workerUrl: workerUrl});
// URL.revokeObjectURL(workerUrl);

//LOCAL
const editorApi = await import("../src/editor.js");
const editor = await editorApi.newEditor({containerId: "editor", workerUrl: "../src/worker.js"});



let model = editorApi.newTextModel(initialText, "activity")
editor.setModel(model);


document.getElementById("doit").addEventListener('click', showMermaid)
document.getElementById("testme").addEventListener('click', testme)

async function testme() {
    await editor.executeOnWorker("ActivityParser.compute", ["calculate"]);
    document.getElementById("paths").style.visibility="visible";
    for (let k = 0; k <4; k++) {
        const pp = await editor.executeOnWorker ("ActivityParser.compute", ["get", k.toString()]);

        let e = document.getElementById("p"+k);
        e.innerHTML=''
        for (let i = 0; i < pp.length; i++) {
            const p = document.createElement("div");
            p.id = "p"+k+"-"+i
            p.className = "path"
            p.addEventListener("click", function(){ highlightPath(k, i); });
            p.style.color='white'
            p.innerText = pp[i];
            e.appendChild(p)
            console.log(pp[i]);
        }
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

async function highlightPath(pathGroupIdx, idx) {
    //highlight div
    const allPaths = document.getElementsByClassName("path");
    for (let i=0; i<allPaths.length; i++) {
        allPaths[i].style.color = 'white'
    }
    document.getElementById("p" + pathGroupIdx + "-" + idx).style.color = 'red'

    const mermaidText2 = await editor.executeOnWorker ("ActivityParser.compute",
        ["highlight", pathGroupIdx.toString(), idx.toString()]
    );
    console.log(mermaidText2[0])

    const drawDiagram2 = async function () {
        let element = document.getElementById("mermaidPane2");
        const graphDefinition = theme+"\n"+mermaidText2[0];
        const { svg } = await mermaid.render('graphDiv1', graphDefinition);
        element.innerHTML = svg;
    };

    await drawDiagram2();
}

async function showMermaid() {
    let mermaidText = editor.getProperty("mermaid")
    let mermaidText2 = editor.getProperty("mermaid2")

    const drawDiagram = async function () {
        let element = document.getElementById("mermaidPane");
        const graphDefinition = theme+"\n"+mermaidText;
        const { svg } = await mermaid.render('graphDiv', graphDefinition);
        element.innerHTML = svg;
    };
    await drawDiagram();

    const drawDiagram2 = async function () {
        let element = document.getElementById("mermaidPane2");
        const graphDefinition = theme+"\n"+mermaidText2;
        const { svg } = await mermaid.render('graphDiv1', graphDefinition);
        element.innerHTML = svg;
    };

    await drawDiagram2();

    console.log(mermaidText)
    console.log(mermaidText2)
}



