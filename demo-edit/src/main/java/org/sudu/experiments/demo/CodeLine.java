package org.sudu.experiments.demo;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.fonts.FontDesk;
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

  public CodeElement get(int ind) {
    return elements[ind];
  }

  public int getElementPos(int charPos) {
    int ind = findEntryByPixel(charPos);
    int sum = 0;
    for (int i = 0; i < ind; i++) sum += elements[i].s.length();
    return sum;
  }

  public void delete(int beginIndex, int endIndex) {
    if (beginIndex <= 0 && endIndex >= totalStrLength) {
      elements = new CodeElement[0];
      invalidateCache();
      totalStrLength = 0;
      return;
    }
    if (beginIndex >= endIndex) return;
    int diff = endIndex - beginIndex;

    int i = 0, j = 0;
    while (i < elements.length && j < elements.length) {
      int iSz = elements[i].s.length();
      int jSz = elements[j].s.length();
      if (beginIndex <= iSz && endIndex <= jSz) break;
      if (beginIndex > iSz) {
        beginIndex -= iSz;
        i++;
      }
      if (endIndex > jSz) {
        endIndex -= jSz;
        j++;
      }
    }

    if (i == j) {
      CodeElement elem = elements[j];
      boolean isEmpty = beginIndex == 0 && endIndex == elem.s.length();
      if (isEmpty) {
        elements = ArrayOp.remove(elements, i, new CodeElement[elements.length - 1]);
        totalStrLength -= diff;
        invalidateCache();
        return;
      }

      String s1 = elem.s.substring(0, beginIndex);
      String s2 = elem.s.substring(endIndex);
      String newValue = s1.concat(s2);
      elements[i] = new CodeElement(newValue, elem.color, elem.fontIndex);
    } else {
      CodeElement from = elements[i];
      CodeElement to = elements[j];

      if (beginIndex != 0) {
        if (beginIndex != from.s.length()) {
          String newValue = from.s.substring(0, beginIndex);
          elements[i] = new CodeElement(newValue, from.color, from.fontIndex);
        }
        i++;
      }

      if (endIndex != to.s.length()) {
        if (endIndex != 0) {
          String newValue = to.s.substring(endIndex);
          elements[j] = new CodeElement(newValue, to.color, to.fontIndex);
        }
      } else j++;

      elements = ArrayOp.remove(elements, i, j, new CodeElement[elements.length - j + i]);
    }
    totalStrLength -= diff;
    invalidateCache();
  }

  public void delete(int beginIndex) {
    delete(beginIndex, totalStrLength);
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

  public CodeElement[] getElementsToLeft(int pos) {
    CodeLine[] splitted = split(pos);
    return splitted[0].elements;
  }

  public CodeElement[] getElementsToRight(int pos) {
    CodeLine[] splitted = split(pos);
    return splitted[1].elements;
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
    insertAt(i, pos, value);
    totalStrLength += value.length();
    invalidateCache();
  }

  private void insertAt(int ind, int pos, String value) {
    if (elements.length == 0) {
      elements = new CodeElement[] {new CodeElement(value)};
    } else {
      elements[ind] = elements[ind].insertAt(pos, value);
    }
    totalStrLength += value.length();
    invalidateCache();
  }

  public void insertToEnd(String value) {
    int elemSize = elements.length == 0 ? 0 : elements[elements.length - 1].s.length();
    insertAt(elements.length - 1, elemSize, value);
  }

  public void insertToBegin(String value) {
    insertAt(0, 0, value);
  }

  void measure(Canvas measuringCanvas, FontDesk[] fonts) {
    int length = elements.length;
    if (fMeasure == null || fMeasure.length < length) {
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

        measuringCanvas.setFont(fonts[entry.fontIndex]);
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

  int[] ensureIMeasure(Canvas mCanvas, FontDesk[] fonts) {
    if (iMeasure == null || measureDirty) measure(mCanvas, fonts);
    return iMeasure;
  }

  public int computeCaretLocation(int pixelLocation, Canvas mCanvas, FontDesk[] fonts) {
    int[] iMeasure = ensureIMeasure(mCanvas, fonts);
    if (elements.length == 0) return 0;

    // check borders
    if (pixelLocation <= 0) return 0;
    if (pixelLocation >= iMeasure[elements.length - 1])
      return totalStrLength;

    int entry = findEntryByPixel(pixelLocation);
    if (entry == elements.length) return totalStrLength;

    int[] cache = getCache(mCanvas, fonts, entry);

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

  private int[] buildGlyphMeasureCache(int entry, Canvas mCanvas, FontDesk[] fonts) {
    CodeElement element = elements[entry];
    mCanvas.setFont(fonts[element.fontIndex]);
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

  private int[] getCache(Canvas mCanvas, FontDesk[] fonts, int entry) {
    if (glyphMeasureCache == null) glyphMeasureCache = new int[elements.length][];
    int[] cache = glyphMeasureCache[entry];
    if (cache == null) {
      cache = buildGlyphMeasureCache(entry, mCanvas, fonts);
    }
    return cache;
  }

  char getChar(int charPos) {
    for (CodeElement element : elements) {
      int len = element.s.length();
      if (charPos < len) return element.s.charAt(charPos);
      charPos -= element.s.length();
    }
    return '\0';
  }

  private int findEntryByPixel(int pixelLocation) {
    int ind = Arrays.binarySearch(iMeasure, 0, elements.length, pixelLocation);
    if (ind < 0) ind = -ind - 1;
    return ind;
  }

  public int wordStart(int pixelLocation) {
    int pos = findEntryByPixel(pixelLocation);
    if (pos == 0) return 0;
    else pos--;
    int charInd = 0;
    for (int i = 0; i <= pos; i++)
      charInd += elements[i].s.length();
    return charInd;
  }

  public int wordEnd(int pixelLocation) {
    int pos = findEntryByPixel(pixelLocation);
    if (pos >= elements.length) pos = elements.length - 1;
    int charInd = 0;
    for (int i = 0; i < pos + 1; i++)
      charInd += elements[i].s.length();
    return charInd;
  }

  public int computePixelLocation(int caretCharPos, Canvas mCanvas, FontDesk[] fonts) {
    if (elements.length == 0) return 0;
    if (caretCharPos == 0) return 0;
    if (measureDirty || iMeasure == null) {
      measure(mCanvas, fonts);
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

    int[] cache = getCache(mCanvas, fonts, el);
    return cache[caretCharPos - elementsLength - 1];
  }

  public int lineMeasure() {
    return elements.length == 0 || iMeasure.length == 0
        ? 0 : iMeasure[elements.length - 1];
  }

  public int prevPos(int caretPos) {
    int pos = findEntryByPixel(caretPos);
    if (pos == 0 && caretPos == 0) return -1;
    int charInd = 0;
    for (int i = 0; i < pos; i++)
      charInd += elements[i].s.length();
    return charInd;
  }

  public int nextPos(int caretPos) {
    int pos = findEntryByPixel(caretPos);
    if (iMeasure[pos] == caretPos) pos++;
    pos++;
    if (pos >= elements.length && caretPos == lineMeasure()) return Integer.MAX_VALUE;
    int charInd = 0;
    for (int i = 0; i < pos; i++)
      charInd += elements[i].s.length();
    return charInd;
  }

  public String makeString() {
    return append(new StringBuilder(totalStrLength)).toString();
  }

  public StringBuilder append(StringBuilder sb) {
    for (CodeElement element : elements) {
      sb.append(element.s);
    }
    return sb;
  }



  public String makeString(int beginIndex) {
    return makeString().substring(beginIndex);
  }

  public String makeString(int beginIndex, int endIndex) {
    return makeString().substring(beginIndex, endIndex);
  }

  @Override
  public String toString() {
    return Arrays.toString(elements);
  }

  static CodeLine[] singleElementLine(String s) {
    return new CodeLine[]{new CodeLine(new CodeElement(s))};
  }
}
