package org.sudu.experiments.js;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSError;

class JsDirectoryHandle implements DirectoryHandle {

  final FileSystemDirectoryHandle fsDirectory;
  final JsFunctions.Consumer<JSError> onError;
  final String[] path;
  private String[] chPath;

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle,
      JsFunctions.Consumer<JSError> onError
  ) {
    this(handle, onError, new String[0]);
  }

  JsDirectoryHandle(
      FileSystemDirectoryHandle handle,
      JsFunctions.Consumer<JSError> onError,
      String[] path
  ) {
    fsDirectory = handle;
    this.onError = onError;
    this.path = path;
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
          var file = new JsFileHandle(handle.cast(), null, chPath());
          reader.onFile(file);
        } else {
          var dir = new JsDirectoryHandle(handle.cast(), onError, chPath());
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
}
