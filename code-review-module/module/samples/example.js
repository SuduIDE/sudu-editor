import { initControlPanel } from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

console.log("Test: threadPool.getNumThreads()=", threadPool.getNumThreads())

const codeReview = editorApi.newCodeReview({
    containerId: "editor",
    workers: threadPool,
    disableParser: true
});

const controlPanel = initControlPanel(document.getElementById("editor"))

const initialText1 =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin";

const initialText2 =
    "This is a experimental project\n" +
    "to write an portable (Web + Desktop)\n" +
    "editor in kotlin and java";

let model1 = editorApi.newTextModel(initialText1, null, "urlNew")
let model2 = editorApi.newTextModel(initialText2, null, "urlNew")

model1.setEditListener(m => console.log("Test: model1 change event"))
model2.setEditListener(m => console.log("Test: model2 change event"))

codeReview.setDiffSizeListener(
    (numLines, lineHeight, cssLineHeight) => {
        console.log("Test: numLines", numLines,
            "lineHeight", lineHeight,
            "cssLineHeight", cssLineHeight);
    }
)

codeReview.setModel(model1, model2);
codeReview.focus()

let compactView = false

const controller = codeReview.getController();

controller.setCompactView(compactView)

const controls = {
    '🔼': () => {
        controller.canNavigateUp() && controller.navigateUp()
        codeReview.focus()
    },
    '🔽': () => {
        controller.canNavigateDown() && controller.navigateDown()
        codeReview.focus()
    },
    '↕️': () => {
        compactView = !compactView
        controller.setCompactView(compactView)
        codeReview.focus()
    },
    '🔄️': () => window.location.reload()
}

Object.entries(controls).forEach(([icon, handler]) => controlPanel.add(icon, handler))

const textModel = editorApi.newTextModel(initialText1, null, {path:"text.txt"});

console.log("language", textModel.language);
