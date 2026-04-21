import { initControlPanel } from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const NUM_EDITORS = 50;

const container = document.body;
const editorDivs = [];

for (let i = 1; i <= NUM_EDITORS; i++) {
  const div = document.createElement("div");
  div.id = "editor" + i;
  div.className = "editor";
  editorDivs.push(div);
}

container.append(...editorDivs);
console.log("document.body.getClientRects", document.body.getClientRects());

const visibilityObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    const id = entry.target.id;
    console.log("editor", id, entry.isIntersecting ? "visible" : "invisible");
  });
});

editorDivs.forEach(div => visibilityObserver.observe(div));

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

console.log("threadPool.getNumThreads()=", threadPool.getNumThreads())

const codeReviews = [];
const controlPanels = [];

for (let i = 1; i <= NUM_EDITORS; i++) {
  const codeReview = editorApi.newCodeReview({
    containerId: "editor" + i, workers: threadPool
  });
  codeReviews.push(codeReview);

  const controlPanel = initControlPanel(document.getElementById("editor" + i))
  controlPanels.push(controlPanel);

  const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin\n";

  let model1 = editorApi.newTextModel(initialText + "model " + i + " a", null, "urlNew")
  let model2 = editorApi.newTextModel(initialText + "model " + i + " b", null, "urlNew")

  model1.setEditListener(m => console.log("model " + i + " a " + "change event"))
  model2.setEditListener(m => console.log("model " + i + " b " + "change event"))

  codeReview.setModel(model1, model2);
}

codeReviews[0].focus();

let number = 1;
const focusStates = new Array(NUM_EDITORS).fill(false);

setInterval(() => {
  codeReviews.forEach((codeReview, i) => {
    const f = codeReview.hasFocus();
    if (f !== focusStates[i]) {
      console.log(number, "editor" + (i + 1) + ".hasFocus()", f);
      focusStates[i] = f;
      number++;
    }
  });
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

codeReviews.forEach((codeReview, i) => {
  Object.entries(controls(codeReview)).forEach(([icon, handler]) => controlPanels[i].add(icon, handler))
});
