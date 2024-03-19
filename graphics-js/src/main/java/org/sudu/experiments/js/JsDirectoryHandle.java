package org.sudu.experiments.js;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

class JsDirectoryHandle implements DirectoryHandle {

  static final JsFunctions.Consumer<JSError> onError = JsDirectoryHandle::onError;

  final FileSystemDirectoryHandle fsDirectory;
  final String[] path;
  private String[] chPath;

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle
  ) {
    this(handle, new String[0], new String[0]);
  }

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle,
      JSString path
  ) {
    fsDirectory = handle;
    if (path.getLength() == 0) {
      this.path = new String[0];
      this.chPath = new String[0];
    } else {
      this.path = toPath(path);
    }
  }

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle,
      String[] path
  ) {
    this(handle, path, null);
  }

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle,
      String[] path, String[] chPath
  ) {
    fsDirectory = handle;
    this.path = path;
    this.chPath = chPath;
  }

  @JSBody(params = {"p"}, script = "return p.split('/');")
  @NoSideEffects
  static native JsArrayReader<JSString> splitPath(JSString p);

  static String[] toPath(JSString path) {
    return path.getLength() == 0 ? new String[0]
        : JsHelper.jsToJava(splitPath(path));
  }

  @Override
  public void read(Reader reader) {
    new AsyncIterator(fsDirectory.values(), reader).iterate();
  }

  private class AsyncIterator implements
      JsFunctions.Consumer<JsIterator.Result<FileSystemHandle>> {

    final JsAsyncIterator<FileSystemHandle> values;
    final Reader reader;

    private AsyncIterator(
        JsAsyncIterator<FileSystemHandle> values, Reader reader
    ) {
      this.values = values;
      this.reader = reader;
    }

    private void iterate() {
      values.next().then(this, onError);
    }

    @Override
    public void f(JsIterator.Result<FileSystemHandle> r) {
      if (r.getDone()) {
        reader.onComplete();
      } else {
        iterate();
        FileSystemHandle handle = r.getValue();
        if (handle.isFile()) {
          var file = new JsFileHandle(handle.cast(), chPath());
          reader.onFile(file);
        } else {
          var dir = new JsDirectoryHandle(handle.cast(), chPath());
          reader.onDirectory(dir);
        }
      }
    }
  }

  private String[] chPath() {
    return chPath != null ? chPath :
        (chPath = ArrayOp.add(path, getName()));
  }

  @Override
  public String getName() {
    return fsDirectory.getName().stringValue();
  }

  @Override
  public String[] getPath() {
    return path;
  }

  public String toString() {
    return FsItem.fullPath(path, getName());
  }

  static void onError(JSError e) {
    JsHelper.consoleInfo("JsDirectoryHandle: ", e);
  }

  @JSBody(params = {"a", "b"}, script = "return a + '/' + b;")
  @NoSideEffects
  public static native JSString add(JSString a, JSString  b);

  static JSString pathToJSString(String[] path) {
    if (path.length == 0) return JSString.valueOf("");
    JSString s = JSString.valueOf(path[0]);
    for (int i = 1; i < path.length; i++) {
      s = add(s, JSString.valueOf(path[i]));
    }
    return s;
  }
}
