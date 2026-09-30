package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.text.SplitText;

import java.util.*;

public class CpxDiff {

  public final Diff[] diffs;
  public final Pos fromSelected, toSelected;
  public final V2i caretBefore, caretAfter;

  public CpxDiff(Diff[] diffs, Selection selection, V2i caretBefore) {
    this.diffs = diffs;
    this.fromSelected = selection != null ? new Pos(selection.getLeftPos()) : null;
    this.toSelected = selection != null ? new Pos(selection.getRightPos()) : null;
    this.caretBefore = caretBefore;
    var last = ArrayOp.last(diffs);
    this.caretAfter = last.isDelete ? caretBefore : makeCaretReturnPos(last.line, last.pos, last.change);
  }

  public CpxDiff(
      Diff[] diffs,
      Pos fromSelected, Pos toSelected,
      V2i caretBefore,
      V2i caretAfter
  ) {
    this.diffs = diffs;
    this.fromSelected = fromSelected;
    this.toSelected = toSelected;
    this.caretBefore = caretBefore;
    this.caretAfter = caretAfter;
  }

  public static class DiffMaker {
    private final FileDiffModel model;
    private final CpxDiff cpxDiff;
    private final boolean left;

    public DiffMaker(FileDiffModel model, CpxDiff cpxDiff, boolean left) {
      this.model = model;
      this.cpxDiff = cpxDiff;
      this.left = left;
    }

    public static CpxDiff mkOppositeDiff(FileDiffModel model, CpxDiff cpxDiff, boolean left) {
      return new DiffMaker(model, cpxDiff, left).mkOppositeDiff();
    }

    private CpxDiff mkOppositeDiff() {
      List<Diff> oppositeDiffs = new ArrayList<>();
      var diffModel = model.diffModel;
      var from = left ? model.leftModel : model.rightModel;
      var to = !left ? model.leftModel : model.rightModel;
      boolean skippedPrevLine = false;
      for (var diff: cpxDiff.diffs) {
        var lines = SplitText.split(diff.change);
        if (lines.length == 0) continue;
        boolean singleLineDiff = lines.length == 1;
        boolean newLineAdded = false;
        int diffBegin = diff.pos;
        for (int l = 0; l < lines.length; l++) {
          var oppositeLine = diffModel.oppositeLine(diff.line + l, left);
          if (l == lines.length - 1 && lines[l].isEmpty()) continue;
          boolean isMidLine = !singleLineDiff && (skippedPrevLine || l != lines.length - 1);
          boolean isLastLine = !singleLineDiff
              && oppositeLine == to.document.length() - 1
              && diff.line == from.document.length() - 1;
          if (oppositeLine == -1) {
            diffBegin = 0;
            skippedPrevLine = true;
            newLineAdded = false;
          } else {
            StringBuilder line = new StringBuilder();
            int curLine = oppositeLine;
            int curBegin = diffBegin;

            if (isLastLine && oppositeLine != 0 && !newLineAdded) {
              curLine--;
              curBegin = to.document.line(curLine).totalStrLength;
              line.append(Document.newLine);
              diffBegin = 0;
            }
            line.append(lines[l]);
            diffBegin += lines[l].length();
            if (!(isLastLine && oppositeLine != 0) && isMidLine) {
              line.append(Document.newLine);
              diffBegin = 0;
            }
            oppositeDiffs.add(new Diff(curLine, curBegin, diff.isDelete, line.toString()));
            newLineAdded = line.toString().endsWith("\n");
          }
        }
      }
      var copiedCaretBefore = new V2i(diffModel.oppositeLine(cpxDiff.caretBefore.x, left), cpxDiff.caretBefore.y);
      var copiedCaretAfter = new V2i(diffModel.oppositeLine(cpxDiff.caretAfter.x, left), cpxDiff.caretAfter.y);
      var merged = mergeDiffs(oppositeDiffs).toArray(Diff[]::new);
      return new CpxDiff(
          merged,
          null, null,
          copiedCaretBefore, copiedCaretAfter
      );
    }
  }

  private static List<Diff> mergeDiffs(List<Diff> diffs) {
    if (diffs.size() <= 1) return diffs;
    List<Diff> merged = new ArrayList<>();
    Diff diff = diffs.get(0);
    StringBuilder change = new StringBuilder(diff.change);

    int line = diff.line;
    for (int i = 1; i < diffs.size(); i++) {
      var cur = diffs.get(i);
      if (cur.line == line + 1 && cur.isDelete == diff.isDelete) {
        change.append(cur.change);
        line++;
      } else {
        merged.add(new Diff(diff.line, diff.pos, diff.isDelete, change.toString()));
        diff = cur;
        change = new StringBuilder(diff.change);
        line = diff.line;
      }
    }
    merged.add(new Diff(diff.line, diff.pos, diff.isDelete, change.toString()));
    return merged;
  }

  public Pair<Pos, Pos> selection() {
    return Pair.of(fromSelected, toSelected);
  }

  public int[] diffLineRanges() {
    if (diffs.length == 0) return new int[] {-1, -1};
    int from = Integer.MAX_VALUE;
    int to = Integer.MIN_VALUE;
    for (var diff: diffs) {
      from = Math.min(from, diff.line);
      to = Math.max(to, diff.line);
    }
    return new int[]{from, to + 1};
  }

  public boolean isEmpty() {
    return diffs.length == 0;
  }

  private V2i makeCaretReturnPos(int line, int pos, String change) {
    if (change.isEmpty()) return new V2i(line, pos);
    var delta = changeDelta(change);
    if (delta.x == 0) return new V2i(line, pos + delta.y);
    else return new V2i(line + delta.x, delta.y);
  }

  private V2i changeDelta(String change) {
    String[] lines = SplitText.split(change);
    return new V2i(lines.length - 1, ArrayOp.last(lines).length());
  }
}
