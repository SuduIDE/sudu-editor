import {initControlPanel} from "./control-panel.js";
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
    `Deleted line 1
Deleted line 2
Common line
Edited line 1-3
Edited line 1-4
Edited line 1-5
Common line`;

const initialText2 = `Common line
Edited line 2-1
Edited line 2-2
Edited line 2-3
Edited line 2-4
Common line
Inserted line 2-5
Inserted line 2-6
Inserted line 2-7`;

let model = editorApi.newDiffModel(threadPool, initialText1, initialText2, "url1", "url2", null);
model.setApplyRejectListener(info => console.log('Change applied: ', info));
model.getLeftModel().setEditListener((m, info) => console.log('Left edit: ', info))
model.getRightModel().setEditListener((m, info) => console.log('Right edit: ', info))

codeReview.setModel(model);
codeReview.focus()

let compactView = false
let enableSyncEdit = false
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
  '⇔': () => {
    enableSyncEdit ^= true;
    model.enableSyncEdit(enableSyncEdit);
  },
  '🔄️': () => window.location.reload(),
  'linesInfo': () => {
    model.getLinesInfo().then(linesInfo => {
      console.log('linesInfo: ', linesInfo)
    })
  }
}

Object.entries(controls).forEach(([icon, handler]) => controlPanel.add(icon, handler))