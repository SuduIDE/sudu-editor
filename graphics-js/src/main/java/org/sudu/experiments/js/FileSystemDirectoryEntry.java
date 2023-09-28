package org.sudu.experiments.js;

import org.teavm.jso.JSObject;

interface FileSystemDirectoryReader extends JSObject {
  void readEntries(JsFunctions.Consumer<JsArrayReader<FileSystemEntry>> successCallback);
}

public interface FileSystemDirectoryEntry extends FileSystemEntry {
  FileSystemDirectoryReader createReader();
}
