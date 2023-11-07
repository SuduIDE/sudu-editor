import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';
mermaid.initialize({ startOnLoad: false });

const initialText = "activity {\n" +
    "  select{A1,B1};\n" +
    "  random(2){A1,B1,C1};\n" +
    "  random {X1,Y1,Z1};\n" +
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
document.getElementById("randomSeed").addEventListener('click', randomSeed)
document.getElementById("seedText").addEventListener('keypress', keypressed)

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
            //console.log(pp[i]);
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

    const arrayWithDag2 = await editor.executeOnWorker ("ActivityParser.compute",
        ["highlight", pathGroupIdx.toString(), idx.toString()]
    );

    await drawMermaid2(arrayWithDag2[0])
}


function keypressed(e){
    if(e.keyCode === 13){
        e.preventDefault();
        setseed();

    } else if (e.charCode < 48 || e.charCode > 57) {
        e.preventDefault();
    }
}

async function randomSeed() {
    document.getElementById("seedText").value = Math.floor(Math.random() * 9999)
    setseed();
}
async function setseed() {
    let s = document.getElementById("seedText").value;
    if (s === '') {
        document.getElementById("seedText").value = 42;
        s = 42;
    }
    document.getElementById("paths").style.visibility="hidden";
    await editor.executeOnWorker ("ActivityParser.compute", ["seed", s]);
    await showMermaid()
}


async function showMermaid() {
    let dag1 = editor.getProperty("mermaid")

    const drawDag1 = async function () {
        let element = document.getElementById("mermaidPane");
        const graphDefinition = theme+"\n"+dag1;
        const { svg } = await mermaid.render('graphDiv', graphDefinition);
        element.innerHTML = svg;
    };
    await drawDag1();

    const arrayWithDag2 = await editor.executeOnWorker("ActivityParser.compute", ["dag2"]);
    await drawMermaid2(arrayWithDag2[0]);
}

async function drawMermaid2(dag2) {
    console.log(">>DAG2: "+dag2)

    const drawDiagram2 = async function () {
        let element = document.getElementById("mermaidPane2");
        const graphDefinition = theme+"\n"+dag2;
        const { svg } = await mermaid.render('graphDiv1', graphDefinition);
        element.innerHTML = svg;
    };

    await drawDiagram2();
}



