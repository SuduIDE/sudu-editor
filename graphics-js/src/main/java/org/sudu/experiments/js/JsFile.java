package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;

interface JsBlob extends JSObject {
  JsBlob slice(int start);
  JsBlob slice(int start, int end);
  Promise<JSString> text();
  Promise<ArrayBuffer> arrayBuffer();
}

public interface JsFile extends JsBlob {
  @JSProperty String getName();
  @JSProperty String getType();
  @JSProperty double getSize();
  @JSProperty JSString getWebkitRelativePath();
}
