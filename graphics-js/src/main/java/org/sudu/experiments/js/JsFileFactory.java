package org.sudu.experiments.js;

import org.sudu.experiments.FileHandle;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class JsFileFactory {

  public static FileHandle fromWebkitRelativeFile(JsFile jsFile) {
    return fromFile(jsFile, splitPath(jsFile.getWebkitRelativePath()));
  }

  public static FileHandle fromFile(JsFile jsFile, String[] path) {
    return new FileHandle() {
      @Override
      public int getSize() {
        return intSize();
      }

      private int intSize() {
        double jsFileSize = jsFile.getSize();
        int result = (int) jsFileSize;
        if (result != jsFileSize) {
          JsHelper.consoleInfo("File is too large: " + getName(), jsFileSize);
          return 0;
        }
        return result;
      }

      @Override
      public String getName() {
        return jsFile.getName();
      }

      @Override
      public String[] getPath() {
        return path;
      }

      @Override
      public void readAsText(Consumer<String> consumer, Consumer<String> onError) {
        jsFile.text().then(
            jsString -> consumer.accept(jsString.stringValue()),
            jsError -> onError.accept(jsError.getMessage())
        );
      }

      @Override
      public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
        jsFile.arrayBuffer().then(
            jsArrayBuffer -> consumer.accept(JsMemoryAccess.toByteArray(jsArrayBuffer)),
            jsError -> onError.accept(jsError.getMessage())
        );
      }

      @Override
      public String toString() {
        return FileHandle.toString(path, getName(), intSize());
      }
    };
  }

  static String[] splitPath(JSString path) {
    if (JSObjects.isUndefined(path) || path == null || path.getLength() == 0) return new String[0];
    JSString[] split = path.split(JSString.valueOf("/"));
    if (split.length == 0) return new String[0];
    String[] strings = new String[split.length - 1];
    for (int i = 0; i < strings.length; i++) strings[i] = split[i].stringValue();
    return strings;
  }
}
