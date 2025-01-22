package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsFunctions.BiConsumer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

public interface JsSftpClient extends JSObject {
  interface DirEntry extends JSObject {
    @JSProperty
    JSString getFilename();
    @JSProperty
    JSObject getAttrs();
  }
  void readdir(
      JSString path,
      BiConsumer<JSError, JsArrayReader<DirEntry>> callback);
}
