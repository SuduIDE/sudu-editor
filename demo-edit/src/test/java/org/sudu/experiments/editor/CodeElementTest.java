package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CodeElementTest {

  @Test
  void setBold() {
    for (int k = 0; k < 4; k++) {
      CodeElement e = new CodeElement("", 0, k);
      boolean it = e.italic();
      e.setBold(true);

      Assertions.assertTrue(e.bold());
      Assertions.assertEquals(e.italic(), it);

      e.setBold(false);

      Assertions.assertFalse(e.bold());
      Assertions.assertEquals(e.italic(), it);

    }
  }
}
