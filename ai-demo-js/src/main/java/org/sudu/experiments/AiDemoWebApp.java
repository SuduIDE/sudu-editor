package org.sudu.experiments;

import org.sudu.experiments.editor.Editor0;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.Uri;
import org.sudu.experiments.js.*;
import org.sudu.experiments.text.SplitJsText;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AiDemoWebApp {

  static final String codeEditDiv = "codeEdit";

  static Editor0 editor;
  static WebWindow window;
  static String selectedFile = null;
  static Map<String, Model> modelMap = new HashMap<>();
  static Map<String, String> requestMap = new HashMap<>();

  public static void main(String[] args) {
    if (JsCanvas.checkFontMetricsAPI()) {
      Fetch.fetchBlob("teavm/worker.js")
          .then(AiDemoWebApp::start, JsHelper::onError);
    } else {
      FireFoxWarning.display(codeEditDiv);
    }
  }

  private static void start(JsBlob blob) {
    WebWorkerContext.start(
        AiDemoWebApp::startApp, e -> JsHelper.onError(e.cast()),
        URL.createObjectURL(blob), 3);
  }

  static void fetchFileList() {
    Fetch.fetch("fileList.txt")
        .then(Fetch.Response::text)
        .then(AiDemoWebApp::initFileList, JsHelper::onError);
  }

  static void putModel(String fileName, JSString text) {
    SplitInfo splitInfo = SplitJsText.split(text);
    var model = new Model(splitInfo.lines, new Uri(fileName));
    modelMap.put(fileName, model);
    if (Objects.equals(selectedFile, fileName)) {
      doSetModel(model);
    }
  }

  static void fetchModel(String fileName) {
    Debug.consoleInfo("fetch model " + fileName);
    Fetch.fetch(fileName)
        .then(Fetch.Response::text)
        .then(text -> putModel(fileName, text), JsHelper::onError);
  }

  static void initFileList(JSString list) {
    var fileList = HTMLDocument.elementById("fileList");
    SplitInfo split = SplitJsText.split(list);
    for (String fileName : split.lines) {
      if (fileName.isEmpty()) continue;
      HTMLButtonElement btn = HTMLDocument.current().createElement("button").cast();
      btn.setClassName("fileButton");
      btn.setInnerText(fileName);
      fileList.appendChild(btn);

      btn.addEventListener("click",  e -> {
        selectedFile = fileName;
        Model model = modelMap.get(selectedFile);
        if (model != null) {
          doSetModel(model);
        } else {
          if (requestMap.get(fileName) != null) {
            Debug.consoleInfo("request in progress " + fileName);
          } else {
            requestMap.put(fileName, fileName);
            fetchModel(fileName);
          }
        }
        respText1().setInnerText("clicked " + fileName);
      });
    }
  }

  private static void doSetModel(Model model) {
    editor.editor().setModel(model);
    window.focus();
  }

  static HTMLElement respText2() {
    return HTMLDocument.elementById("respText2");
  }

  static HTMLElement respText1() {
    return HTMLDocument.elementById("respText1");
  }

  static void startApp(JsArray<WebWorkerContext> workers) {
    fetchFileList();
    if (HTMLDocument.current().getElementById(codeEditDiv) == null) {
      System.err.println("no such element with id = " + codeEditDiv);
      return;
    }
    window = new WebWindow(
        api -> editor = new Editor0(api),
        AiDemoWebApp::onWebGlError,
        codeEditDiv, workers);
    window.focus();
  }

  static void onWebGlError() {
    JsHelper.addPreText(codeEditDiv,
        "FATAL: WebGL is not enabled in the browser");
  }
}
