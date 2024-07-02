package org.sudu.experiments.update;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class ReadDto {

  public RemoteFolderDiffModel model;
  public DirectoryHandle dirHandle;
  public int diffType, rangeId;

  public ReadDto(
      RemoteFolderDiffModel model,
      DirectoryHandle dirHandle,
      int diffType,
      int rangeId
  ) {
    this.model = model;
    this.dirHandle = dirHandle;
    this.diffType = diffType;
    this.rangeId = rangeId;
  }
}
