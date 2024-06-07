package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class NodeFileHandle implements FileHandle {

  final String pathName;
  final String[] path;
  NodeFs.Stats stats;

  public NodeFileHandle(String pathName, String[] path) {
    this.pathName = pathName;
    this.path = path;
  }

  @Override
  public void getSize(IntConsumer result) {
    result.accept(intSize());
  }

  private NodeFs.Stats stats() {
    return stats == null
        ? (stats = Fs.fs().lstatSync(JSString.valueOf(pathName)))
        : stats;
  }

  private int intSize() {
    double jsSize = stats().size();
    int result = (int) jsSize;
    if (result != jsSize) {
      JsHelper.consoleError(
          "File is too large: " + pathName + ", size = ",
          JSNumber.valueOf(jsSize));
      return 0;
    }
    return result;
  }

  @Override
  public String getName() {
    return pathName;
  }

  @Override
  public String[] getPath() {
    return path;
  }

  @Override
  public void readAsText(Consumer<String> consumer, Consumer<String> onError) {
//    JsFunctions.Consumer<JSError> onJsError = wrapError(onError);
//    JsFunctions.Consumer<JSString> onString = jsString
//        -> consumer.accept(jsString.stringValue());
//    if (jsFile != null) {
//      jsFile.text().then(onString, onJsError);
//    } else {
//      fileHandle.getFile().then(
//          file -> file.text().then(onString, onJsError), onJsError);
//    }
  }

  @Override
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    int fileSize = intSize();
    if (begin <= fileSize) {
      length = Math.min(length, fileSize - begin);
      if (length > 0) {
        Fs fs = Fs.fs();
        try {
          int h = fs.openSync(JSString.valueOf(pathName), fs.constants().O_RDONLY());
          byte[] bytes = new byte[length];
          fs.readSync(
              h,
              JsMemoryAccess.uInt8View(bytes), 0, length,
              begin);
          fs.closeSync(h);
          consumer.accept(bytes);
        } catch (Exception e) {
          onError.accept(e.getMessage());
        }
      } else {
        consumer.accept(new byte[0]);
      }
    } else {
      onError.accept("");
    }
  }

  @Override
  public String toString() {
    return pathName;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(pathName) * 31 + Arrays.hashCode(path);
  }
}

