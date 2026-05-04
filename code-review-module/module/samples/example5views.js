import { initControlPanel } from "./control-panel.js";
import * as editorApi from "../src/codereview.js";

const workers = await editorApi.newWorkerPool("../src/worker.js", 3);

console.log("workers.getNumThreads()=", workers.getNumThreads())

const theme = "dark";

let focus = [];
let editors = [];

for (let i = 1; i <= 5; i++) {
  const containerId = "editor" + i;
  console.log("containerId", containerId)
  const editor = editorApi.newEditor({ containerId, workers, theme });
  const controlPanel = initControlPanel(document.getElementById(containerId))
  const initialText =
      "This is an experimental project\n" +
      "to write a portable (Web + Desktop)\n" +
      "editor in java and kotlin\n" +
      "editorId = " + containerId;
  const model = editorApi.newTextModel(initialText, null, "url" + containerId)
  model.setEditListener(m => console.log("containerId change event"))
  editor.setModel(model);
  const focused = i === 1;
  if (focused) editor.focus();
  editors.push(editor);
  focus.push(focused);

  const controls = (codeReview, compactView = false) => {
    const controller = codeReview.getController()
   // controller.setCompactView(compactView)
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

  Object.entries(controls(editor))
      .forEach(([icon, handler]) =>
          controlPanel.add(icon, handler))

}

let number = 1;

setInterval(() => {
  for (let i = 0; i < 5; i++) {
    const f = editors[i].hasFocus();
    if (f !== focus[i]) {
      console.log(number, "editor", i + 1, "focus", focus[i], "->", f);
      focus[i] = f;
    }
  }
  number++;
}, 200);

setInterval(() => {
  console.log("textureUsage", editorApi.glDebugApi.textureUsage());
}, 1000)