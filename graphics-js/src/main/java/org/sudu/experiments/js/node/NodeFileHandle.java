package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class NodeFileHandle implements FileHandle {

  final String pathName;
  final String[] path;

  public NodeFileHandle(String pathName, String[] path) {
    this.pathName = pathName;
    this.path = path;
  }

  @Override
  public void getSize(IntConsumer result) {

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
//    JsFunctions.Consumer<JSError> onJsError = wrapError(onError);
//    JsFunctions.Consumer<ArrayBuffer> onBuffer = toJava(consumer);
//    if (jsFile != null) {
//      readBlob(begin, length, onBuffer, onJsError, jsFile);
//    } else {
//      fileHandle.getFile().then(
//          file -> readBlob(begin, length, onBuffer, onJsError, file),
//          onJsError
//      );
//    }
  }

  @Override
  public String toString() {
    return pathName;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName()) * 31 + Arrays.hashCode(path);
  }
}

