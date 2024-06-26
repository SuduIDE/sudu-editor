package org.sudu.experiments.diff.folder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.sudu.experiments.diff.DiffTypes.EDITED;
import static org.sudu.experiments.diff.DiffTypes.INSERTED;

public class FolderDiffModelTest {

  @Test
  public void serializeTest() {
    var root = new FolderDiffModel(null);
    root.setChildren(3);
    root.rangeId = 4;
    root.child(0).itemCompared();
    root.child(0).diffType = INSERTED;
    root.child(0).rangeId = 1;
    root.child(1).setChildren(2);
    root.child(1).rangeId = 2;
    root.child(2).itemCompared();
    root.child(2).diffType = EDITED;
    root.child(2).rangeId = 3;

    int[] serialized = FolderDiffModel.toInts(root);
    FolderDiffModel deserialized = FolderDiffModel.fromInts(serialized);
    Assertions.assertEquals(root, deserialized);
  }
}
