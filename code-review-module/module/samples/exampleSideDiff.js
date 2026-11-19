import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const textL =
`package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeReview implements JsCodeReviewView {

  public final WebWindow window;
  private FileDiffWindow w;
  private JsFileDiffViewController controller;
  private IDiffSizeChangeCallback sizeListener;
  private int eventNumLines, eventLineHeight;
  private float eventCssLineHeight;

  public JsCodeReview(
      FileDiffWindow fileDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = fileDiff;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());

    w.rootView.setOnDocumentSizeChange(this::onDocumentSizeChange);
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    JsHelper.consoleInfo("JsCodeDiff0.setReadonly");
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextModel modelL, JsITextModel modelR) {
    if (!(modelL instanceof JsTextModel jsModelL)) {
      boolean undefined = JSObjects.isUndefined(modelL);
      throw new IllegalArgumentException(
          undefined ? "left model is undefined" : "bad left model");
    }

    if (!(modelR instanceof JsTextModel jsModelR)) {
      boolean undefined = JSObjects.isUndefined(modelR);
      throw new IllegalArgumentException(
          undefined ? "right model is undefined" : "bad right model");
    }

    w.rootView.setModel(jsModelL.javaModel, jsModelR.javaModel);
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }
}
`;

const textR =
`package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeReview implements JsCodeReviewView {

  public final WebWindow window;
  private FileDiffWindow w;
  private JsFileDiffViewController controller;
  private IDiffSizeChangeCallback sizeListener;
  private int eventNumLines, eventLineHeight;
  private float eventCssLineHeight;

  public JsCodeReview(
      FileDiffWindow fileDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = fileDiff;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());

    w.rootView.setOnDocumentSizeChange(this::onDocumentSizeChange);
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void enableMergeButtons(boolean leftToRight, boolean rightToLeft) {
    w.rootView.enableMergeButtons(leftToRight, rightToLeft);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextDiffModel model) {
    if (!(model instanceof JsTextDiffModel jsModel)) {
      boolean undefined = JSObjects.isUndefined(model);
      throw new IllegalArgumentException(undefined ? "Model is undefined" : "Bad model: " + JsHelper.stringify(model));
    }
    w.rootView.setModel(jsModel.javaModel);
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }
}
`;

const model = editorApi.newDiffModel(
    threadPool, textL, textR,
    "classL.java", "classR.java",
    "java"
);

const leftView1 = editorApi.newSideDiff({
    containerId: "left1",
    workers: threadPool
}, true);

const leftView2 = editorApi.newSideDiff({
    containerId: "left2",
    workers: threadPool
}, true);

const rightView1 = editorApi.newSideDiff({
    containerId: "right1",
    workers: threadPool
}, false);

const rightView2 = editorApi.newSideDiff({
    containerId: "right2",
    workers: threadPool
}, false);

leftView1.setModel(model);
rightView1.setModel(model);
leftView2.setModel(model);
rightView2.setModel(model);

leftView1.setVScrollListener((pos) => rightView1.setVScrollPos(pos));
leftView2.setVScrollListener((pos) => rightView2.setVScrollPos(pos));
rightView1.setVScrollListener((pos) => leftView1.setVScrollPos(pos));
rightView2.setVScrollListener((pos) => leftView2.setVScrollPos(pos));

leftView1.focus();

console.log("SideDiff left.left =", leftView1.left, "right.left =", rightView1.left);
