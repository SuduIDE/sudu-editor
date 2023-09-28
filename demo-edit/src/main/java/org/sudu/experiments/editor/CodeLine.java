package org.sudu.experiments.editor;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;

public class CodeLine {
  static int cacheMiss, cacheHits;
  static boolean debug = false;

  CodeElement[] elements;
  public int totalStrLength;

  float[] fMeasure;
  int[] iMeasure;
  int[] lengthCache;
  int[][] glyphMeasureCache;
  boolean measureDirty;
  boolean contentDirty;
  boolean lengthDirty;

  public CodeLine(CodeElement ... data) {
    elements = data;
    int l = 0;
    for (CodeElement e : data) l += e.s.length();
    totalStrLength = l;
    invalidateCache();
  }

  static CodeLine concat(CodeLine a, CodeLine b) {
    return new CodeLine(ArrayOp.add(a.elements, b.elements));
  }

  static CodeLine[] makeLines(String[] text) {
    if (text.length == 0) return singleElementLine("");
    CodeLine[] cl = new CodeLine[text.length];
    for (int i = 0; i < text.length; i++) {
      cl[i] = new CodeLine(new CodeElement(text[i]));
    }
    return cl;
  }

  public int length() {
    return elements.length;
  }

  public CodeElement get(int ind) {
    return elements[ind];
  }

  public int getElementStart(int charPos) {
    int index = getElementIndex(charPos);
    return getElementStartAtIndex(index);
  }

  // this works only after getElementIndex call
  public int getElementStartAtIndex(int index) {
    return index <= 0 ? 0 : lengthCache[index - 1];
  }

  // this works only after getElementIndex call
  public int getElementEndAtIndex(int element) {
    return element < elements.length
        ? lengthCache[element] : totalStrLength;
  }

  public int getElementIndex(int charPos) {
    int length = elements.length;
    if (length == 0) return -1;
    int ind = Arrays.binarySearch(lengthCache(),
            0, length - 1, charPos);
    return ind < 0 ? -ind - 1 : ind + 1;
  }

  public CodeElement getCodeElement(int pos) {
    if (elements.length == 0) return null;
    return elements[getElementIndex(pos)];
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
  }

  private void insertAt(int ind, int pos, String value) {
    if (elements.length == 0) {
      elements = new CodeElement[] {new CodeElement(value)};
    } else if (ind == 0 && pos == 0) {
      insertToLineStart(value);
    } else {
      elements[ind] = elements[ind].insertAt(pos, value);
    }
    totalStrLength += value.length();
    invalidateCache();
  }

  private void insertToLineStart(String value) {
    CodeElement[] newElements = new CodeElement[elements.length + 1];
    System.arraycopy(elements, 0, newElements, 1, elements.length);
    newElements[0] = new CodeElement(value);
    elements = newElements;
  }

  public int getBlankStartLength() {
    int c = 0;
    for (CodeElement e: elements) {
      for (int i = 0; i < e.length(); i++) {
        if (e.charAt(i) != ' ') return c;
        c++;
      }
    }
    return c;
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

    allocateLengthCache();

    if (measureDirty) {
      int totalLength = 0;
      float sumMeasure = .0f;
      cacheMiss++;
      for (int i = 0; i < length; i++) {
        CodeElement entry = elements[i];
        totalLength += entry.s.length();
        lengthCache[i] = totalLength;

        measuringCanvas.setFont(fonts[entry.fontIndex]);
        float wordLength = measuringCanvas.measureText(entry.s);
        sumMeasure += wordLength;
        fMeasure[i] = sumMeasure;
        iMeasure[i] = (int) (sumMeasure + .5f);
      }
      totalStrLength = totalLength;
      measureDirty = false;
      lengthDirty = false;
    } else {
      cacheHits++;
    }
  }

  private void allocateLengthCache() {
    if (lengthCache == null || lengthCache.length < elements.length) {
      lengthCache = new int[elements.length];
      lengthDirty = true;
    }
  }

  private int[] lengthCache() {
    if (lengthCache == null || lengthDirty) {
      allocateLengthCache();

      for (int i = 0, sum = 0, e = elements.length; i < e; i++) {
        sum += elements[i].s.length();
        lengthCache[i] = sum;
      }
      lengthDirty = false;
    }
    return lengthCache;
  }

  public void invalidateCache() {
    measureDirty = true;
    contentDirty = true;
    lengthDirty = true;
    glyphMeasureCache = null;
  }

  int[] ensureIMeasure(Canvas mCanvas, FontDesk[] fonts) {
    if (iMeasure == null || measureDirty) measure(mCanvas, fonts);
    return iMeasure;
  }

  public int computeCharPos(int pixelLocation, Canvas mCanvas, FontDesk[] fonts) {
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

  public int computePixelLocation(int charPos, Canvas mCanvas, FontDesk[] fonts) {
    if (elements.length == 0 || charPos == 0) return 0;
    if (measureDirty || iMeasure == null) {
      measure(mCanvas, fonts);
    }
    if (charPos >= totalStrLength)
      return iMeasure[elements.length - 1];

    int elementsLength = 0, el = 0;

    // todo: optimize
    for (; el < elements.length; el++) {
      int elementsLengthNext = elementsLength + elements[el].s.length();
      if (charPos < elementsLengthNext) break;
      if (charPos == elementsLengthNext) return iMeasure[el];
      elementsLength = elementsLengthNext;
    }

    int[] cache = getCache(mCanvas, fonts, el);
    return cache[charPos - elementsLength - 1];
  }

  public int lineMeasure() {
    return elements.length == 0 || iMeasure.length == 0
        ? 0 : iMeasure[elements.length - 1];
  }

  public int nextPos(int charPos) {
    if (charPos >= totalStrLength) return charPos + 1;
    int element = getElementIndex(charPos);
    return lengthCache[element];
  }

  public int prevPos(int charPos) {
    if (charPos == 0) return -1;
    int element = getElementIndex(charPos);
    if (element > 0 && lengthCache[element - 1] == charPos) element--;
    return element > 0 ? lengthCache[element - 1] : 0;
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
    return elements.length == 1
        ? elements[0].toString()
        : Arrays.toString(elements);
  }

  static CodeLine[] singleElementLine(String s) {
    return new CodeLine[]{new CodeLine(new CodeElement(s))};
  }

  public int toCharArray(char[] dst, int pos) {
    for (CodeElement element : elements) {
      String s = element.s;
      s.getChars(0, s.length(), dst, pos);
      pos += s.length();
    }
    return pos;
  }
}
