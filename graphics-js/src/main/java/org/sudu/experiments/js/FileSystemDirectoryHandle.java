package org.sudu.experiments.js;

public interface FileSystemDirectoryHandle extends FileSystemHandle {

  JsAsyncIterator<FileSystemHandle> values();
}
