package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.V2i;

public class ModelTest {
  @Test
  void testSelection() {
    Model model = new Model();
    model.document.insertAt(0,0," ");
    V2i selectionLine0 = model.selection.getLine(0, new V2i());
    // we don't have any selection
    Assertions.assertNull(selectionLine0);
  }

  @Test
  void findAdjacentHighlightRange_nextAndPrevFromCaretPositions() {
    Model model = new Model();
    // three non-overlapping matches on one line: [2,5), [10,13), [20,23)
    Range a = new Range(0, 2, 0, 5);
    Range b = new Range(0, 10, 0, 13);
    Range c = new Range(0, 20, 0, 23);
    model.highlightRanges = new Range[]{a, b, c};

    // caret before all matches
    model.caretLine = 0;
    model.caretCharPos = 0;
    Assertions.assertSame(a, model.findAdjacentHighlightRange(true));
    Assertions.assertNull(model.findAdjacentHighlightRange(false));

    // caret on start of b
    model.caretCharPos = 10;
    Assertions.assertSame(c, model.findAdjacentHighlightRange(true));
    Assertions.assertSame(a, model.findAdjacentHighlightRange(false));

    // caret inside b (not on start) — prev must skip b and go to a
    model.caretCharPos = 11;
    Assertions.assertSame(c, model.findAdjacentHighlightRange(true));
    Assertions.assertSame(a, model.findAdjacentHighlightRange(false));

    // caret after last match
    model.caretCharPos = 30;
    Assertions.assertNull(model.findAdjacentHighlightRange(true));
    Assertions.assertSame(c, model.findAdjacentHighlightRange(false));
  }
}
