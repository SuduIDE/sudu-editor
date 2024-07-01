package org.sudu.experiments.update;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

public class CollectDto {

  public RemoteFolderDiffModel leftModel, rightModel;
  public FsItem leftItem, rightItem;

  public CollectDto(
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel,
      FsItem leftItem, FsItem rightItem
  ) {
    this.leftModel = leftModel;
    this.rightModel = rightModel;
    this.leftItem = leftItem;
    this.rightItem = rightItem;
  }

  public boolean isFile() {
    return leftItem instanceof FileHandle && rightItem instanceof FileHandle;
  }
}
