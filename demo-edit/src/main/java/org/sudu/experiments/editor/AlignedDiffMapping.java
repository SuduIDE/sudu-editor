package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.diff.DiffRange;

import java.util.Arrays;

/**
 * View mapping that keeps corresponding {@link DiffRange}s on the same
 * visual rows. Each range occupies {@code max(lenL, lenR)} view lines;
 * the shorter side maps the extra rows to {@link #outOfRange} (blank).
 */
public class AlignedDiffMapping extends CodeLineMapping {

  final DiffRange[] ranges;
  final boolean left;
  final int[] viewStarts;
  final int length;

  public AlignedDiffMapping(DiffRange[] ranges, boolean left) {
    this.ranges = ranges;
    this.left = left;
    this.viewStarts = new int[ranges.length];
    int v = 0;
    for (int i = 0; i < ranges.length; i++) {
      viewStarts[i] = v;
      v += viewLen(ranges[i]);
    }
    this.length = v;
  }

  static int viewLen(DiffRange r) {
    return Math.max(r.lenL, r.lenR);
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public int docToView(int docLine) {
    int idx = rangeAtDoc(docLine);
    if (idx < 0) return outOfRange;
    DiffRange r = ranges[idx];
    return viewStarts[idx] + (docLine - r.from(left));
  }

  @Override
  public int docToViewCursor(int docLine) {
    return docToView(docLine);
  }

  @Override
  public int viewToDoc(int viewLine) {
    int idx = rangeAtView(viewLine);
    if (idx < 0) return outOfRange;
    DiffRange r = ranges[idx];
    int offset = viewLine - viewStarts[idx];
    int len = r.len(left);
    if (offset < 0 || offset >= len) return outOfRange;
    return r.from(left) + offset;
  }

  @Override
  public int viewLineDiffType(int viewLine) {
    int idx = rangeAtView(viewLine);
    return idx < 0 ? 0 : ranges[idx].type;
  }

  int rangeAtView(int viewLine) {
    if (viewLine < 0 || viewLine >= length || ranges.length == 0)
      return -1;
    int idx = Arrays.binarySearch(viewStarts, viewLine);
    if (idx >= 0) {
      while (idx + 1 < viewStarts.length
          && viewStarts[idx + 1] == viewLine
          && viewLen(ranges[idx]) == 0)
        idx++;
      return idx;
    }
    int p = -idx - 2;
    if (p < 0) return 0;
    return p;
  }

  int rangeAtDoc(int docLine) {
    int low = 0, high = ranges.length - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      DiffRange r = ranges[mid];
      int from = r.from(left);
      int to = r.to(left);
      if (to <= from) {
        if (docLine < from) high = mid - 1;
        else low = mid + 1;
        continue;
      }
      if (docLine < from) high = mid - 1;
      else if (docLine >= to) low = mid + 1;
      else return mid;
    }
    return -1;
  }
}
