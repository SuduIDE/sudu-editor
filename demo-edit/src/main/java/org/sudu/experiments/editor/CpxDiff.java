package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.text.SplitText;

import java.util.Arrays;
import java.util.function.Function;

public class CpxDiff {

  public final Diff[] diffs;
  public final Selection.SelPos from, to;
  public final V2i caretBefore, caretAfter;

  public CpxDiff(Diff[] diffs, Selection selection, V2i caretBefore) {
    this.diffs = diffs;
    this.from = selection != null ? selection.getLeftPos() : null;
    this.to = selection != null ? selection.getRightPos() : null;
    this.caretBefore = caretBefore;
    var last = last();
    this.caretAfter = last.isDelete ? caretBefore : makeCaretReturnPos(last.line, last.pos, last.change);
  }

  public CpxDiff(
      Diff[] diffs,
      Selection.SelPos from,
      Selection.SelPos to,
      V2i caretBefore,
      V2i caretAfter
  ) {
    this.diffs = diffs;
    this.from = from;
    this.to = to;
    this.caretBefore = caretBefore;
    this.caretAfter = caretAfter;
  }

  public CpxDiff copyWithNewLine(Function<Integer, Integer> getOpposite) {
    Diff[] copiedDiffs = new Diff[diffs.length];
    int len = 0;
    for (Diff diff: diffs) {
      int oppositeLine = getOpposite.apply(diff.line);
      if (oppositeLine == -1) continue;
      copiedDiffs[len++] = diff.copyWithNewLine(oppositeLine);
    }
    copiedDiffs = Arrays.copyOf(copiedDiffs, len);
    var copiedCaretBefore = new V2i(getOpposite.apply(caretBefore.x), caretBefore.y);
    var copiedCaretAfter = new V2i(getOpposite.apply(caretAfter.x), caretAfter.y);
    var copiedFrom = new Selection.SelPos(getOpposite.apply(from.line), from.charInd);
    var copiedTo = new Selection.SelPos(getOpposite.apply(to.line), to.charInd);
    return new CpxDiff(copiedDiffs, copiedFrom, copiedTo, copiedCaretBefore, copiedCaretAfter);
  }

  private V2i makeCaretReturnPos(int line, int pos, String change) {
    if (change.isEmpty()) return new V2i(line, pos);
    var delta = changeDelta(change);
    if (delta.x == 0) return new V2i(line, pos + delta.y);
    else return new V2i(line + delta.x, delta.y);
  }

  private V2i changeDelta(String change) {
    String[] lines = SplitText.split(change);
    return new V2i(lines.length - 1, lines[lines.length - 1].length());
  }

  private Diff last() {
    return diffs[diffs.length - 1];
  }
}
