package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;

interface FileSystemDirectoryReader extends JSObject {
  void readEntries(JsFunctions.Consumer<JSArrayReader<FileSystemEntry>> successCallback);
}

public interface FileSystemDirectoryEntry extends FileSystemEntry {
  FileSystemDirectoryReader createReader();
}
