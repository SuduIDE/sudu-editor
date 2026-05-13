import {initControlPanel} from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const createDestroyOnVisible = false;

const NUM_EDITORS = window.NUM_EDITORS ?? 50;

const container = document.body;
const editorDivs = Array(NUM_EDITORS);
const codeReviews = new Array(NUM_EDITORS);
const controlPanels = new Array(NUM_EDITORS);

const models = new Array(NUM_EDITORS);

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const focusStates = new Array(NUM_EDITORS).fill(false);
let messageId = 1;

function divId(i) {
  return "editor" + (i + 1);
}

for (let i = 0; i < NUM_EDITORS; i++) {
  const div = document.createElement("div");
  div.id = divId(i);
  div.className = "editor";
  div.internalId = i;
  editorDivs[i] = div;
}

container.append(...editorDivs);
console.log("document.body.getClientRects", document.body.getClientRects());

const visibilityObserver = new IntersectionObserver((entries) => {
  for (const entry of entries) {
    let element = entry.target;
    if (0) console.log(
        "editor", element.id,
        "internalId", element.internalId,
        entry.isIntersecting ? "visible" : "invisible");
    if (createDestroyOnVisible)
      changeCodeReviewVisibility(entry.isIntersecting, element.internalId);
  }
});

console.log("threadPool.getNumThreads()=", threadPool.getNumThreads())

const initialText =
    "This is an experimental project\n" +
    "to write a portable (Web + Desktop)\n" +
    "editor in java and kotlin\n";

for (let i = 0; i < NUM_EDITORS; i++) {
  const idA = "model " + (i + 1) + " a";
  const idB = "model " + (i + 1) + " b";
  const model = editorApi.newDiffModel(
      initialText + idA, initialText + idB,
      idA, idB, null)

  model.getLeftModel().setEditListener(m => console.log(idA, "change event"))
  model.getRightModel().setEditListener(m => console.log(idB, "change event"))
  models[i] = model;
  const containerId = divId(i);
  controlPanels[i] = initControlPanel(document.getElementById(containerId));
}

editorDivs.forEach(div => visibilityObserver.observe(div));

function createCodeReview(i) {
  const containerId = divId(i);
  const codeReview = editorApi.newCodeReview({
    containerId, workers: threadPool
  });
  codeReviews[i] = codeReview;
  codeReview.setModel(models[i]);
}

function deleteCodeReview(i) {
  codeReviews[i].dispose();
  codeReviews[i] = null;
}

function changeCodeReviewVisibility(isVisible, i) {
  if (codeReviews[i] && !isVisible) {
    console.log(messageId++, "removing editor", i);
    deleteCodeReview(i);
  }
  if (!codeReviews[i] && isVisible) {
    console.log(messageId++, "adding editor", i);
    createCodeReview(i);
  }
}

if (!createDestroyOnVisible) {
  for (let i = 0; i < NUM_EDITORS; i++) {
    createCodeReview(i);
  }
  codeReviews[0].focus();
}

setInterval(() => {
  codeReviews.forEach((codeReview, i) => {
    const f = codeReview && codeReview.hasFocus();
    if (f !== focusStates[i]) {
      // console.log(messageId++, divId(i), "hasFocus()", f);
      focusStates[i] = f;
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
        deleteCodeReview(i);
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

let textureUsageInfo = "";
const glDebugApi = editorApi.glDebugApi;

setInterval(() => {
  let data = glDebugApi.textureUsage();
  //if (data !== textureUsageInfo)
    console.log(messageId++, "textureUsage", textureUsageInfo = data);
}, 1000);
