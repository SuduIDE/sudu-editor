package org.sudu.experiments.encoding;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.js.TextDecoder;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public interface JsTextFileReader {

  void onText(JSString text, JSString encoding);

  static void read(
      FileHandle fileHandle,
      JsTextFileReader reader,
      Consumer<String> onError
  ) {
    fileHandle.readAsBytes(bytes -> {
      boolean gbk = FileEncoding.needGbk(bytes);
      if (gbk) {
        JSString text = TextDecoder.fromGbk(bytes);
        reader.onText(text, JSString.valueOf(FileEncoding.gbk));
      } else {
        JSString text = TextDecoder.fromUtf8(bytes);
        reader.onText(text, JSString.valueOf(FileEncoding.utf8));
      }
    }, onError);
  }
}
