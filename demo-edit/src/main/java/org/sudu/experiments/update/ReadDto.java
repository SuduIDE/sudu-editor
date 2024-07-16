package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class ReadDto {

  public RemoteFolderDiffModel model;
  public DirectoryHandle dirHandle;
  public int diffType;

  public ReadDto(
      RemoteFolderDiffModel model,
      DirectoryHandle dirHandle,
      int diffType
  ) {
    this.model = model;
    this.dirHandle = dirHandle;
    this.diffType = diffType;
  }
}
