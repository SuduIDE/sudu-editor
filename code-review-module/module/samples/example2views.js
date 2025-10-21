import { initControlPanel } from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

console.log("threadPool.getNumThreads()=", threadPool.getNumThreads())

const codeReview1 = editorApi.newCodeReview({
  containerId: "editor1", workers: threadPool
});

const controlPanel1 = initControlPanel(document.getElementById("editor1"))

const codeReview2 = editorApi.newCodeReview({
  containerId: "editor2", workers: threadPool
});

const controlPanel2 = initControlPanel(document.getElementById("editor2"))

const initialText1 =
  "This is an experimental project\n" +
  "to write a portable (Web + Desktop)\n" +
  "editor in java and kotlin\n";

const initialText2 =
  "This is a experimental project\n" +
  "to write an portable (Web + Desktop)\n" +
  "editor in kotlin and java\n";

let model11 = editorApi.newTextModel(initialText1 + "model11", null, "urlNew")
let model12 = editorApi.newTextModel(initialText2 + "model12", null, "urlNew")

let model21 = editorApi.newTextModel(initialText1 + "model21", null, "urlNew")
let model22 = editorApi.newTextModel(initialText2 + "model22", null, "urlNew")

model11.setEditListener(m => console.log("model11 change event"))
model12.setEditListener(m => console.log("model12 change event"))

codeReview1.setModel(model11, model12);
codeReview2.setModel(model21, model22);
codeReview1.focus();

let number = 1;
let focus1 = false;
let focus2 = false;

setInterval(() => {
  const f1 = codeReview1.hasFocus();
  const f2 = codeReview2.hasFocus();
  if (f1 !== focus1 || f2 !== focus2) {
    console.log(number, "codeReview1.hasFocus()", f1);
    console.log(number, "codeReview2.hasFocus()", f2);
    focus1 = f1;
    focus2 = f2;
    number++;
  }
}, 200);

const controls = (codeReview, compactView = false) => {

  const controller = codeReview.getController()
  controller.setCompactView(compactView)
  return ({
    '🔼': () => {
      controller.canNavigateUp() && controller.navigateUp();
      codeReview.focus();
    },
    '🔽': () => {
      controller.canNavigateDown() && controller.navigateDown();
      codeReview.focus();
    },
    '↕️': () => {
      compactView = !compactView;
      controller.setCompactView(compactView);
      codeReview.focus();
    }
  });
}

Object.entries(controls(codeReview1)).forEach(([icon, handler]) => controlPanel1.add(icon, handler))
Object.entries(controls(codeReview2)).forEach(([icon, handler]) => controlPanel2.add(icon, handler))
