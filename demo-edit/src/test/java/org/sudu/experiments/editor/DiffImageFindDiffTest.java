package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.math.V2i;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiffImageFindDiffTest {

  @Test
  void hoverUsesViewRowsIncludingBlanks() {
    // 4 insert blanks, then doc line 0 (edited) at view 4.
    int[] viewToDoc = { -1, -1, -1, -1, 0, 1, 2, 3, 4, 5 };
    LineDiff[] model = new LineDiff[6];
    model[0] = new LineDiff(DiffTypes.EDITED);

    int height = viewToDoc.length;
    int imgPos = imgPosForViewLine(4, height, viewToDoc.length);
    V2i size = new V2i(8, height);
    V2i mouse = new V2i(4, imgPos);

    int found = DiffImage.findDiff(mouse, 0, 0, size, viewToDoc, model);
    assertEquals(0, found);

    // Mapping through document length (6) lands on a different view row.
    int compressed = DiffImage.imageToDocument(imgPos, height, model.length);
    assertTrue(compressed != 4,
        "document-length mapping should not match the view row");
  }

  @Test
  void fillerRowsAreNotHits() {
    int[] viewToDoc = { -1, -1, 0 };
    LineDiff[] model = { new LineDiff(DiffTypes.INSERTED) };
    int height = viewToDoc.length;
    int imgPos = imgPosForViewLine(0, height, viewToDoc.length);
    int found = DiffImage.findDiff(
        new V2i(1, imgPos), 0, 0, new V2i(4, height), viewToDoc, model);
    assertEquals(-1, found);
  }

  @Test
  void editedPaddingIsNotAHit() {
    // Real edited line, then several empty pads. Click a pad that is not
    // adjacent to the document line (findDiff also samples neighboring pixels).
    int[] viewToDoc = { 0, -1, -1, -1, -1, -1 };
    LineDiff[] model = { new LineDiff(DiffTypes.EDITED) };
    int height = viewToDoc.length;
    int imgPos = imgPosForViewLine(4, height, viewToDoc.length);
    int found = DiffImage.findDiff(
        new V2i(1, imgPos), 0, 0, new V2i(4, height), viewToDoc, model);
    assertEquals(-1, found);
  }

  @Test
  void mapKeepsBlankSlotsSoLaterHunksStayPut() {
    int[] viewToDoc = { -1, -1, -1, -1, 0, 1 };
    LineDiff[] model = { null, new LineDiff(DiffTypes.EDITED) };
    byte[] viewTypes = { 0, 0, 0, 0, 0, DiffTypes.EDITED };
    byte[] map = DiffImage.diffMap(model, viewToDoc.length, viewToDoc, viewTypes);
    assertEquals(0, map[0]);
    assertEquals(0, map[3]);
    assertEquals(DiffTypes.EDITED, map[5]);
  }

  static int imgPosForViewLine(int viewLine, int height, int viewLen) {
    for (int y = 0; y < height; y++) {
      if (DiffImage.imageToDocument(y, height, viewLen) == viewLine)
        return y;
    }
    throw new IllegalStateException("no pixel maps to view line " + viewLine);
  }
}
