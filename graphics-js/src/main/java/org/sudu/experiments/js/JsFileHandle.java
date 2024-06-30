package org.sudu.experiments.js;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class JsFileHandle implements FileHandle {

  // fileHandle is optional, it is unsupported in FireFox
  final FileSystemFileHandle fileHandle;
  final JsFile jsFile;
  final String[] path;

  public static FileHandle fromWebkitRelativeFile(JsFile jsFile) {
    return new JsFileHandle(null, jsFile, JsHelper.splitPath(jsFile.getWebkitRelativePath()));
  }

  public JsFileHandle(FileSystemFileHandle fileHandle) {
    this(fileHandle, new String[0]);
  }

  public JsFileHandle(FileSystemFileHandle fileHandle, String[] path) {
    this(fileHandle, null, path);
  }

  private JsFileHandle(FileSystemFileHandle fileHandle, JsFile jsFile, String[] path) {
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

  @Override
  public void syncAccess(
      Consumer<SyncAccess> consumer,
      Consumer<String> onError
  ) {
    if (fileHandle != null) {
      fileHandle.createSyncAccessHandle().then(
          sa -> consumer.accept(new JsSyncAccess(sa)),
          jsError -> onError.accept(jsError.getMessage())
      );
    } else {
      onError.accept("no file handle");
    }
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
    JsFunctions.Consumer<JSError> onJsError = JsHelper.wrapError(onError);
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
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    JsFunctions.Consumer<JSError> onJsError = JsHelper.wrapError(onError);
    JsFunctions.Consumer<ArrayBuffer> onBuffer = JsHelper.toJava(consumer);
    if (jsFile != null) {
      readBlob(begin, length, onBuffer, onJsError, jsFile);
    } else {
      fileHandle.getFile().then(
          file -> readBlob(begin, length, onBuffer, onJsError, file),
          onJsError
      );
    }
  }

  private void readBlob(
      int begin, int length,
      JsFunctions.Consumer<ArrayBuffer> onBuffer,
      JsFunctions.Consumer<JSError> onJsError, JsFile file
  ) {
    JsBlob blob = length < 0 ? begin == 0 ? file : file.slice(begin)
        : file.slice(begin, begin + length);
    blob.arrayBuffer().then(onBuffer, onJsError);
  }

  @Override
  public String toString() {
    return jsFile != null
        ? FsItem.toString(getClass().getSimpleName(),
            path, getName(), false)
        : FsItem.fullPath(path, getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName()) * 31 + Arrays.hashCode(path);
  }

}

