package org.sudu.experiments.js;

import org.teavm.jso.JSObject;

// https://developer.mozilla.org/en-US/docs/Web/API/FileSystemFileHandle
public interface FileSystemFileHandle extends FileSystemHandle {
  Promise<JsFile> getFile();

  interface FileSystemWritableFileStream extends JSObject { }
  Promise<FileSystemWritableFileStream> createWritable();

  // high performance but sync, only for worker threads
  Promise<FileSystemSyncAccessHandle> createSyncAccessHandle();
}
