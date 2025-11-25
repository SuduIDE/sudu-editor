package org.sudu.experiments.editor;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.text.SplitText;

import java.util.Arrays;
import java.util.function.Function;

public class CpxDiff {

  public final Diff[] diffs;
  public final Pos from, to;
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
      Pos from, Pos to,
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
    Diff[][] tmp = new Diff[diffs.length][];
    for (int i = 0; i < diffs.length; i++) tmp[i] = splitDiffByLines(diffs[i]);
    Diff[] flatten = ArrayOp.flatten(tmp);
    Diff[] copiedDiffs = new Diff[flatten.length];
    int len = 0;
    for (Diff diff: flatten) {
      int oppositeLine = getOpposite.apply(diff.line);
      if (oppositeLine == -1) continue;
      copiedDiffs[len++] = diff.copyWithNewLine(oppositeLine);
    }
    copiedDiffs = Arrays.copyOf(copiedDiffs, len);
    var copiedCaretBefore = new V2i(getOpposite.apply(caretBefore.x), caretBefore.y);
    var copiedCaretAfter = new V2i(getOpposite.apply(caretAfter.x), caretAfter.y);
    var copiedFrom = new Pos(getOpposite.apply(from.line), from.charPos);
    var copiedTo = new Pos(getOpposite.apply(to.line), to.charPos);
    return new CpxDiff(copiedDiffs, copiedFrom, copiedTo, copiedCaretBefore, copiedCaretAfter);
  }

  private Diff[] splitDiffByLines(Diff diff) {
    var lines = SplitText.split(diff.change);
    if (lines.length == 1) return ArrayOp.array(diff);
    Diff[] diffs = new Diff[lines.length];
    int diffLine = diff.line;
    int diffBegin = diff.pos;
    for (int l = 0; l < lines.length; l++) {
      String line = l != lines.length - 1 ? lines[l].concat("\n") : lines[l];
      diffs[l] = new Diff(diffLine, diffBegin, diff.isDelete, line);
      diffLine++;
      diffBegin = 0;
    }
    return diffs;
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
