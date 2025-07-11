package org.sudu.experiments;

public interface JsFolderDiffSession extends AsyncShutdown {
  void changeFolder(JsFolderInput newFolder, boolean left);
}
