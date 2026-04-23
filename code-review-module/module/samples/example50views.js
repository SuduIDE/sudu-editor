import {initControlPanel} from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const NUM_EDITORS = 50;

const container = document.body;
const editorDivs = Array(NUM_EDITORS);
const codeReviews = new Array(NUM_EDITORS);
const controlPanels = new Array(NUM_EDITORS);

const modelsA = new Array(NUM_EDITORS);
const modelsB = new Array(NUM_EDITORS);

function divId(i) {
  return "editor" + (i + 1);
}

for (let i = 0; i < NUM_EDITORS; i++) {
  const div = document.createElement("div");
  div.id = divId(i);
  div.className = "editor";
  editorDivs[i] = div;
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


const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin\n";

for (let i = 0; i < NUM_EDITORS; i++) {
  const idA = "model " + (i + 1) + " a";
  const idB = "model " + (i + 1) + " b";
  const model1 = editorApi.newTextModel(initialText + idA, null, idA)
  const model2 = editorApi.newTextModel(initialText + idB, null, idB)

  model1.setEditListener(m => console.log(idA, "change event"))
  model2.setEditListener(m => console.log(idB, "change event"))
  modelsA[i] = model1;
  modelsB[i] = model2;
  const containerId = divId(i);
  controlPanels[i] = initControlPanel(document.getElementById(containerId));
}

function createCodeReview(i) {
  const containerId = divId(i);
  const codeReview = editorApi.newCodeReview({
    containerId, workers: threadPool
  });
  codeReviews[i] = codeReview;
  codeReview.setModel(modelsA[i], modelsB[i]);
}

for (let i = 0; i < NUM_EDITORS; i++) {
  createCodeReview(i);
}

codeReviews[0].focus();

let number = 1;
const focusStates = new Array(NUM_EDITORS).fill(false);

setInterval(() => {
  codeReviews.forEach((codeReview, i) => {
    const f = codeReview?.hasFocus();
    if (f !== focusStates[i]) {
      console.log(number, divId(i), "hasFocus()", f);
      focusStates[i] = f;
      number++;
    }
  });
}, 200);

const controls = (i) => {
  let compactView = false;
  return ({
    '🔼': () => {
      const codeReview = codeReviews[i];
      if (codeReview) {
        const controller = codeReview.getController()
        controller.canNavigateUp() && controller.navigateUp();
        codeReview.focus();
      }
    },
    '🔽': () => {
      const codeReview = codeReviews[i];
      if (codeReview) {
        const controller = codeReview.getController()
        controller.canNavigateDown() && controller.navigateDown();
        codeReview.focus();
      }
    },
    '↕️': () => {
      const codeReview = codeReviews[i];
      if (codeReview) {
        const controller = codeReview.getController()
        compactView = !compactView;
        controller.setCompactView(compactView);
        codeReview.focus();
      }
    },
    '🔃': () => {
      const div = editorDivs[i];
      const codeReview = codeReviews[i];
      console.log(div.children, div.children.length);
      if (div.children.length === 2) {
        codeReview.disconnectFromDom();
      } else {
        codeReview.reconnectToDom();
      }
    },
    '❌': () => {
      if (codeReviews[i]) {
        codeReviews[i].dispose();
        codeReviews[i] = null;
      } else {
        createCodeReview(i);
      }
    }
  });
}

for (let i = 0; i < NUM_EDITORS; i++) {
  Object.entries(controls(i))
      .forEach(([icon, handler]) => controlPanels[i].add(icon, handler))
}

setInterval(() => {
  console.log("textureUsage", editorApi.textureUsage());
}, 1000)