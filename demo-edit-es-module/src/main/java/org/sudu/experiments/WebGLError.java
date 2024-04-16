package org.sudu.experiments;

import org.sudu.experiments.js.JsHelper;

public interface WebGLError {
  String text = "FATAL: WebGL is not enabled in the browser";

  static void onWebGlError() {
    JsHelper.consoleInfo(text);
  }
}
