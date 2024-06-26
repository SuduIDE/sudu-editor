package org.sudu.experiments.diff.update;

import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.FolderDiffModel;

@FunctionalInterface
public interface CollectConsumer {

  void accept(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      FsItem leftItem, FsItem rightItem
  );
}
