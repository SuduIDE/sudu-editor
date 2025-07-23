package org.sudu.experiments;

import org.teavm.jso.core.JSString;

public interface JsFolderDiffSession extends AsyncShutdown {
  void changeFolder(JsFolderInput newFolder, boolean left, JSString excludeList);
}
