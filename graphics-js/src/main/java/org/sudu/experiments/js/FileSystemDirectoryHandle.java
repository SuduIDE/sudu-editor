package org.sudu.experiments.js;

import org.teavm.jso.core.JSString;

public interface FileSystemDirectoryHandle extends FileSystemHandle {
  JsAsyncIterator<JSString> keys();
  JsAsyncIterator<FileSystemHandle> values();
}
