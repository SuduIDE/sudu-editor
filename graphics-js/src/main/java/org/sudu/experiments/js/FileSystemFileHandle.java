package org.sudu.experiments.js;

import org.teavm.jso.JSObject;

public interface FileSystemFileHandle extends FileSystemHandle {
  Promise<JsFile> getFile();

  interface FileSystemWritableFileStream extends JSObject { }
  Promise<FileSystemWritableFileStream> createWritable();

  // high performance but sync, only for worker threads
  Promise<FileSystemSyncAccessHandle> createSyncAccessHandle();
}
