package org.sudu.experiments.demo;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

public class CodeLine {
  static int cacheMiss, cacheHits;
  static boolean debug = false;

  CodeElement[] elements;
  int totalStrLength;

  float[] fMeasure;
  int[] iMeasure;
  int[][] glyphMeasureCache;
  boolean measureDirty;
  boolean contentDirty;

  public CodeLine(CodeElement ... data) {
    elements = data;
    int l = 0;
    for (CodeElement e : data) l += e.s.length();
    totalStrLength = l;
  }

  static CodeLine concat(CodeLine a, CodeLine b) {
    return new CodeLine(ArrayOp.add(a.elements, b.elements));
  }

  public CodeLine[] split(int pos) {
    if (pos <= 0) return new CodeLine[] {new CodeLine(), this};
    if (pos >= totalStrLength) return new CodeLine[] {this, new CodeLine()};
    CodeElement[] e = elements;
    int i = 0;
    for (; i < e.length; i++) {
      int el = e[i].s.length();
      if (pos < el) break;
      pos -= el;
    }
    if (pos == 0) {
      return new CodeLine[] {
        new CodeLine(ArrayOp.segment(e, 0, new CodeElement[i])),
        new CodeLine(ArrayOp.segment(e, i, new CodeElement[e.length - i]))
      };
    }

    CodeElement element = e[i];
    CodeElement[] segment1 = ArrayOp.segment(e, 0, new CodeElement[i + 1]);
    CodeElement[] segment2 = ArrayOp.segment(e, i, new CodeElement[e.length - i]);
    segment1[i] = element.splitLeft(pos);
    segment2[0] = element.splitRight(pos);

    return new CodeLine[] {new CodeLine(segment1), new CodeLine(segment2)};
  }

  public void deleteAt(int pos) {
    CodeElement[] e = elements;
    int i = 0;
    for (; i < e.length; i++) {
      int el = e[i].s.length();
      if (pos < el) break;
      pos -= el;
    }
    if (e[i].s.length() == 1) {
      elements = ArrayOp.remove(e, i, new CodeElement[e.length - 1]);
    } else {
      e[i] = e[i].deleteAt(pos);
    }
    totalStrLength -= 1;
    invalidateCache();
  }

  public void insertAt(int pos, String value) {
    int i = 0;
    for (; i + 1 < elements.length; i++) {
      int el = elements[i].s.length();
      if (pos <= el) break;
      pos -= el;
    }
    if (elements.length == 0) {
      elements = new CodeElement[] {new CodeElement(value, Colors.defaultText)};
    } else {
      elements[i] = elements[i].insertAt(pos, value);
    }
    totalStrLength += value.length();
    invalidateCache();
  }

  void measure(Canvas measuringCanvas) {
    int length = elements.length;
    if (iMeasure == null || iMeasure.length < length) {
      fMeasure = new float[length];
      iMeasure = new int[length];
      measureDirty = true;
    }

    if (measureDirty) {
      int totalLength = 0;
      float sumMeasure = .0f;
      cacheMiss++;
      for (int i = 0; i < length; i++) {
        CodeElement entry = elements[i];
        totalLength += entry.s.length();

        float wordLength = measuringCanvas.measureText(entry.s);
        sumMeasure += wordLength;
        fMeasure[i] = sumMeasure;
        iMeasure[i] = (int) (sumMeasure + .5f);
      }
      totalStrLength = totalLength;
      measureDirty = false;
    } else {
      cacheHits++;
    }
  }

  public void invalidateCache() {
    measureDirty = true;
    contentDirty = true;
    glyphMeasureCache = null;
  }

  public int computeCaretLocation(int pixelLocation, Canvas mCanvas) {
    if (elements.length == 0) return 0;

    // check borders
    if (pixelLocation <= 0) return 0;
    if (pixelLocation >= iMeasure[elements.length - 1])
      return totalStrLength;

    int entry = findEntryByPixel(pixelLocation);
    if (entry == elements.length) return totalStrLength;

    int[] cache = getCache(mCanvas, entry);

    int pos = 0;
    for (int i = 0; i < entry; i++) {
      pos += elements[i].s.length();
    }

    int prev = entry == 0 ? 0 : iMeasure[entry - 1];
    int next = iMeasure[entry];

    // todo optimize
    for (int i = 0; i < cache.length; pos++, i++) {
      next = cache[i];
      if (pixelLocation < next) break;
      prev = next;
    }

    if (debug) {
      Debug.consoleInfo("prev = " + prev
          + " pixelLocation = " + pixelLocation
          + ", next = " + next);
      Debug.consoleInfo(" pos = ", pos);
    }

    return (pixelLocation - prev) <= (next - pixelLocation) ? pos : pos + 1;
  }

  private int[] buildGlyphMeasureCache(int entry, Canvas mCanvas) {
    CodeElement element = elements[entry];
    String s = element.s;
    int[] cache = new int[s.length() - 1];
    char[] chars = s.toCharArray();
    float elementsBefore = entry == 0 ? 0 : fMeasure[entry - 1];
    for (int i = 0, n = cache.length; i < n; i++) {
      String substring = new String(chars, 0, i + 1);
      float l = mCanvas.measureText(substring);
      cache[i] = (int) (elementsBefore + l + .5f);
    }
    return glyphMeasureCache[entry] = cache;
  }

  private int[] getCache(Canvas mCanvas, int entry) {
    if (glyphMeasureCache == null) glyphMeasureCache = new int[elements.length][];
    int[] cache = glyphMeasureCache[entry];
    if (cache == null) {
      cache = buildGlyphMeasureCache(entry, mCanvas);
    }
    return cache;
  }

  private int findEntryByPixel(int pixelLocation) {
    // todo: rewrite to bSearch
    for (int i = 0; i < elements.length; i++) {
      if (pixelLocation < iMeasure[i]) return i;
    }
    return elements.length;
  }

  public int computePixelLocation(int caretCharPos, Canvas mCanvas) {
    if (elements.length == 0) return 0;
    if (caretCharPos == 0) return 0;
    if (measureDirty || iMeasure == null) {
      measure(mCanvas);
    }
    if (caretCharPos >= totalStrLength)
      return iMeasure[elements.length - 1];

    int elementsLength = 0, el = 0;

    // todo: optimize
    for (; el < elements.length; el++) {
      int elementsLengthNext = elementsLength + elements[el].s.length();
      if (caretCharPos < elementsLengthNext) break;
      if (caretCharPos == elementsLengthNext) return iMeasure[el];
      elementsLength = elementsLengthNext;
    }

    int[] cache = getCache(mCanvas, el);
    return cache[caretCharPos - elementsLength - 1];
  }

  public int lineMeasure() {
    return iMeasure[iMeasure.length - 1];
  }

  @Override
  public String toString() {
    return Arrays.toString(elements);
  }
}
