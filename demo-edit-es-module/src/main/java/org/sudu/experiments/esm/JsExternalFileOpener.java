package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsExternalFileOpener extends JSObject {
  // openCodeDiff(leftPath: string, rightPath: string): void
  void openCodeDiff(JSString leftPath, JSString rightPath);
  // openCodeEditor(path: string): void
  void openCodeEditor(JSString path);
}
