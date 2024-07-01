package org.sudu.experiments.diff.folder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.sudu.experiments.diff.DiffTypes.EDITED;
import static org.sudu.experiments.diff.DiffTypes.INSERTED;

public class FolderDiffModelTest {

  private static void setChildren(FolderDiffModel parent, int len) {
    parent.isFile = true;
    parent.children = new FolderDiffModel[len];
    parent.childrenComparedCnt = 0;
    for (int i = 0; i < len; i++) parent.children[i] = new FolderDiffModel(parent);
    if (len == 0) {
      parent.compared = true;
      if (parent.parent != null) parent.childCompared();
    }
  }

  @Test
  public void serializeTest() {
    var root = new FolderDiffModel(null);
    setChildren(root, 3);
    root.rangeId = 4;
    root.child(0).itemCompared();
    root.child(0).diffType = INSERTED;
    root.child(0).rangeId = 1;
    setChildren(root.child(1), 2);
    root.child(1).rangeId = 2;
    root.child(2).itemCompared();
    root.child(2).diffType = EDITED;
    root.child(2).rangeId = 3;

    int[] serialized = FolderDiffModel.toInts(root);
    FolderDiffModel deserialized = FolderDiffModel.fromInts(serialized);
    Assertions.assertEquals(root, deserialized);
  }
}
