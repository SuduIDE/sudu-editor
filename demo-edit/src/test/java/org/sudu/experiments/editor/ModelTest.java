package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.V2i;

public class ModelTest {
  @Test
  void testSelection() {
    Model model = new Model();
    model.document.insertAt(0,0," ");
    V2i selectionLine0 = model.selection.getLine(0);
    // we don't have any selection
    Assertions.assertNull(selectionLine0);
  }
}
