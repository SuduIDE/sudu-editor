package org.sudu.experiments.diff.update;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.FolderDiffModel;

public class CollectDto {

  public FolderDiffModel leftModel, rightModel;
  public FsItem leftItem, rightItem;

  public CollectDto(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
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
