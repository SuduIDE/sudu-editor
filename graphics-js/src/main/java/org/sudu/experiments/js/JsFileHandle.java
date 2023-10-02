package org.sudu.experiments.js;

import org.sudu.experiments.FileHandle;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class JsFileHandle implements FileHandle {

  // fileHandle is optional, it is unsupported in FireFox
  final FileSystemFileHandle fileHandle;
  final JsFile jsFile;
  final String[] path;

  public static FileHandle fromWebkitRelativeFile(JsFile jsFile) {
    return new JsFileHandle(null, jsFile, splitPath(jsFile.getWebkitRelativePath()));
  }

  public JsFileHandle(FileSystemFileHandle fileHandle, JsFile jsFile) {
    this(fileHandle, jsFile, new String[0]);
  }

  public JsFileHandle(FileSystemFileHandle fileHandle, JsFile jsFile, String[] path) {
    this.fileHandle = fileHandle;
    this.jsFile = jsFile;
    this.path = path;
  }

  @Override
  public void getSize(IntConsumer result) {
    if (jsFile != null) {
      result.accept(intSize(jsFile.getSize()));
    } else {
      fileHandle.getFile().then(
          f -> result.accept(intSize(f.getSize())),
          error -> {
            System.err.println(error.getMessage());
            result.accept(0);
          });
    }
  }

  private int jsFileSize() {
    double jsFileSize = jsFile.getSize();
    return intSize(jsFileSize);
  }

  private int intSize(double jsSize) {
    int result = (int) jsSize;
    if (result != jsSize) {
      JsHelper.consoleInfo("File is too large: " + getName(), jsSize);
      return 0;
    }
    return result;
  }

  @Override
  public String getName() {
    return jsName().stringValue();
  }

  private JSString jsName() {
    return fileHandle != null ? fileHandle.getName() : jsFile.getName();
  }

  @Override
  public String[] getPath() {
    return path;
  }

  @Override
  public void readAsText(Consumer<String> consumer, Consumer<String> onError) {
    JsFunctions.Consumer<JSError> onJsError = wrapError(onError);
    JsFunctions.Consumer<JSString> onString = jsString
        -> consumer.accept(jsString.stringValue());
    if (jsFile != null) {
      jsFile.text().then(onString, onJsError);
    } else {
      fileHandle.getFile().then(
          file -> file.text().then(onString, onJsError), onJsError);
    }
  }

  @Override
  public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    JsFunctions.Consumer<JSError> onJsError = wrapError(onError);
    JsFunctions.Consumer<ArrayBuffer> onBuffer = jsArrayBuffer
        -> consumer.accept(JsMemoryAccess.toByteArray(jsArrayBuffer));
    if (jsFile != null) {
      jsFile.arrayBuffer().then(onBuffer, onJsError);
    } else {
      fileHandle.getFile().then(
          file -> file.arrayBuffer().then(onBuffer, onJsError), onJsError);
    }
  }

  static JsFunctions.Consumer<JSError> wrapError(Consumer<String> onError) {
    return jsError -> onError.accept(jsError.getMessage());
  }

  @Override
  public String toString() {
    return jsFile != null
        ? FileHandle.toString(path, getName(), jsFileSize())
        : FileHandle.toString(path, getName());
  }

  @JSBody(params = {"str", "arg" }, script = "return str.split(arg);")
  static native JsArrayReader<JSString> stringSplit(JSString str, JSString arg);

  static String[] splitPath(JSString path) {
    if (JSObjects.isUndefined(path) || path == null || path.getLength() == 0) return new String[0];
    JsArrayReader<JSString> split = stringSplit(path, JSString.valueOf("/"));
    if (split.getLength() == 0) return new String[0];
    String[] strings = new String[split.getLength() - 1];
    for (int i = 0; i < strings.length; i++) strings[i] = split.get(i).stringValue();
    return strings;
  }
}

