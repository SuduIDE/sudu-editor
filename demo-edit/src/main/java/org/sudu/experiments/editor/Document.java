package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.tree.IntervalTree;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.text.SplitText;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class Document extends CodeLines {
  public static final char newLine = '\n';

  public CodeLine[] lines;
  public IntervalTree tree;
  public ScopeGraph scopeGraph = new ScopeGraph();
  public final Map<Pos, Pos> usageToDef = new HashMap<>();
  public final Map<Pos, List<Pos>> defToUsages = new HashMap<>();
  public int[] linePrefixSum;

  /** Source line → document line. {@code null} means identity (no inline disassembly). */
  public int[] sourceToDoc;
  /** Cumulative source-char starts; built once with {@link #sourceToDoc}. */
  public int[] srcStart;
  /** Document-global max display number (AUTO-aware). */
  public int maxLineNumber;
  boolean mappedReadonlyWarned;
  boolean producerViolationWarned;
  boolean applyAbortWarned;

  int currentVersion;
  int lastParsedVersion;
  double lastDiffTimestamp;
  public BiConsumer<Diff, Boolean> updateModelOnDiff;
  public BiConsumer<CpxDiff, Boolean> syncEditing;
  public Supplier<UndoBuffer> getUndoBuffer;
  public Supplier<V2i> getCaretPos;
  public Supplier<Selection> getSelection;
  public Consumer<CpxDiff> onEditMade;
  public Consumer<CpxDiff> onChangeApplied;

  public Document() {
    this(CodeLine.emptyLine());
  }

  public Document(String[] text) {
    this(CodeLine.makeLines(text));
  }

  public Document(CodeLine... data) {
    if (data.length == 0) throw new IllegalArgumentException();
    lines = data;
    tree = initialInterval();
    maxLineNumber = data.length;
  }

  /**
   * Build a document from per-line content and optional display numbers.
   * {@code lineNumbers[i] > 0} marks a source line; {@code 0} is attached (or AUTO
   * if no number is present on any line).
   */
  public static Document fromDescriptors(String[] contents, int[] lineNumbers) {
    if (contents.length == 0) return new Document();
    CodeLine[] data = new CodeLine[contents.length];
    int sourceCount = 0;
    for (int i = 0; i < contents.length; i++) {
      data[i] = new CodeLine(contents[i]);
      int n = lineNumbers != null && i < lineNumbers.length ? lineNumbers[i] : 0;
      if (n > 0) {
        data[i].lineNumber = n;
        sourceCount++;
      }
    }
    Document doc = new Document(data);
    if (sourceCount > 0) {
      int[] map = new int[sourceCount];
      for (int i = 0, s = 0; i < data.length; i++) {
        if (data[i].lineNumber > 0) map[s++] = i;
      }
      doc.initSourceMapping(map);
    }
    return doc;
  }

  public boolean isMapped() {
    return sourceToDoc != null;
  }

  public int sourceCount() {
    return sourceToDoc == null ? lines.length : sourceToDoc.length;
  }

  /** The only writer of {@link #sourceToDoc} / {@link #srcStart} besides tests. */
  public void initSourceMapping(int[] sourceToDoc) {
    this.sourceToDoc = sourceToDoc;
    this.srcStart = computeSrcStart();
    refreshMaxLineNumber();
  }

  public void refreshMaxLineNumber() {
    if (sourceToDoc == null) {
      maxLineNumber = lines.length;
      return;
    }
    int max = 0;
    for (CodeLine line : lines) {
      if (line.lineNumber > max) max = line.lineNumber;
    }
    maxLineNumber = max;
  }

  public int resolveLineNumber(int documentIndex) {
    if (documentIndex < 0 || documentIndex >= lines.length) return 0;
    int stored = lines[documentIndex].lineNumber;
    if (stored > 0) return stored;
    if (sourceToDoc == null) return documentIndex + 1;
    if (indexOfDocLine(documentIndex) >= 0) {
      warnProducerViolation();
    }
    return 0;
  }

  public int docLineOfSource(int sourceIndex) {
    if (sourceToDoc == null) return sourceIndex;
    if (sourceIndex < 0 || sourceIndex >= sourceToDoc.length) return -1;
    return sourceToDoc[sourceIndex];
  }

  public int sourceIndex(int documentLine) {
    if (sourceToDoc == null) return documentLine;
    if (documentLine < 0 || documentLine >= lines.length) return -1;
    int n = lines[documentLine].lineNumber;
    if (n > 0) {
      int s = n - 1;
      if (s >= 0 && s < sourceToDoc.length && sourceToDoc[s] == documentLine) return s;
    }
    return indexOfDocLine(documentLine);
  }

  /** Greatest source index {@code i} with {@code sourceToDoc[i] < documentLine}, or {@code -1}. */
  public int enclosingSource(int documentLine) {
    if (sourceToDoc == null) return documentLine;
    return lowerBound(sourceToDoc, documentLine) - 1;
  }

  public int indexOfDocLine(int documentLine) {
    if (sourceToDoc == null) return documentLine;
    int i = lowerBound(sourceToDoc, documentLine);
    if (i < sourceToDoc.length && sourceToDoc[i] == documentLine) return i;
    return -1;
  }

  public static int lowerBound(int[] a, int value) {
    int lo = 0, hi = a.length;
    while (lo < hi) {
      int mid = (lo + hi) >>> 1;
      if (a[mid] < value) lo = mid + 1;
      else hi = mid;
    }
    return lo;
  }

  public static int upperBound(int[] a, int value) {
    int lo = 0, hi = a.length;
    while (lo < hi) {
      int mid = (lo + hi) >>> 1;
      if (a[mid] <= value) lo = mid + 1;
      else hi = mid;
    }
    return lo;
  }

  public int sourceLineAtOffset(int sourceOffset) {
    if (srcStart == null) return getLine(sourceOffset).x;
    int i = upperBound(srcStart, sourceOffset) - 1;
    if (i < 0) return 0;
    if (i >= srcStart.length) return srcStart.length - 1;
    return i;
  }

  public int sourceLineAtEndOffset(int exclusiveStop) {
    if (srcStart == null) return getLine(Math.max(0, exclusiveStop)).x;
    int e = lowerBound(srcStart, exclusiveStop) - 1;
    return Math.max(0, e);
  }

  public Pos toSourcePos(Pos docPos) {
    if (sourceToDoc == null) return docPos;
    int s = sourceIndex(docPos.line);
    if (s < 0) return null;
    int max = strLength(sourceToDoc[s]);
    int c = Math.min(docPos.charPos, max);
    return new Pos(s, c);
  }

  public Pos toDocPos(Pos sourcePos) {
    if (sourceToDoc == null) return sourcePos;
    if (sourcePos.line < 0 || sourcePos.line >= sourceToDoc.length) return null;
    return new Pos(sourceToDoc[sourcePos.line], sourcePos.charPos);
  }

  public int toDocLine(int sourceLine) {
    if (sourceToDoc == null) return sourceLine;
    if (sourceLine < 0 || sourceLine >= sourceToDoc.length) return -1;
    return sourceToDoc[sourceLine];
  }

  public char[] getSourceChars() {
    if (sourceToDoc == null) return getChars();
    int n = sourceToDoc.length;
    if (n == 0) return new char[0];
    int len = srcStart[n - 1] + strLength(sourceToDoc[n - 1]);
    char[] dst = new char[len];
    for (int i = 0, pos = 0; i < n; ) {
      pos = lines[sourceToDoc[i]].toCharArray(dst, pos);
      if (++i < n) dst[pos++] = newLine;
    }
    return dst;
  }

  int[] computeSrcStart() {
    if (sourceToDoc == null) return null;
    int n = sourceToDoc.length;
    int[] start = new int[n];
    for (int i = 0; i < n - 1; i++) {
      start[i + 1] = start[i] + strLength(sourceToDoc[i]) + 1;
    }
    return start;
  }

  void warnProducerViolation() {
    if (producerViolationWarned) return;
    producerViolationWarned = true;
    System.err.println("Document: sourceToDoc member has lineNumber == 0");
  }

  public void warnApplyAbort() {
    if (applyAbortWarned) return;
    applyAbortWarned = true;
    System.err.println("Document: mapped interval apply aborted (N != e - s + 1)");
  }

  public void warnMappedReadonly() {
    if (mappedReadonlyWarned) return;
    mappedReadonlyWarned = true;
    System.err.println("Document: mapped document attached without readonly");
  }

  private boolean rejectMappedEdit() {
    return sourceToDoc != null;
  }

  public boolean isEmpty() {
    return lines.length == 1 && lines[0].totalStrLength == 0;
  }

  private IntervalTree initialInterval() {
    return IntervalTree.singleInterval(0, getFullLength(), 0);
  }

  public CodeLine line(int i) {
    return lines[i];
  }

  public char getChar(int line, int pos) {
    return lines[line].getChar(pos);
  }

  public void invalidateFont() {
    for (CodeLine codeLine: lines) {
      codeLine.invalidateCache();
    }
  }

  public void invalidateMeasure() {
    for (CodeLine line: lines) {
      line.invalidateMeasure();
    }
  }

  public int length() {
    return lines.length;
  }

  public int getFullLength() {
    return getIntervalLength(0, lines.length);
  }

  public int getIntervalLength(int fromLine, int toLine) {
    return getLineStartInd(fromLine, toLine);
  }

  public void clear() {
    usageToDef.clear();
    defToUsages.clear();

    currentVersion = lastParsedVersion = 0;
    tree = initialInterval();
    var undoBuffer = undoBuffer();
    if (undoBuffer != null) undoBuffer.clear(this);
  }

  public int strLength(int i) {
    return lines[i].totalStrLength;
  }

  public void setLine(int ind, CodeLine newLine, boolean success) {
    var oldLine = lines[ind];
    lines[ind] = newLine;
    if (/*success || */oldLine.length() != newLine.length()) return;
    for (int i = 0; i < oldLine.length(); i++) {
      CodeElement oldElem = oldLine.elements[i];
      CodeElement newElem = newLine.elements[i];
      if (oldElem.isError()) continue;
      boolean replacedToDefault = oldElem.color != DEFAULT && newElem.color == DEFAULT;
      boolean oldSemanticToken = isSemanticToken(oldElem.color);
      if (replacedToDefault || oldSemanticToken) {
        newElem.color = oldElem.color;
        newElem.style = oldElem.style;
      }
    }
  }

  public void newLineOp(int caretLine, int caretCharPos) {
    if (rejectMappedEdit()) return;
    CodeLine line = lines[caretLine];

    lines = Arrays.copyOf(lines, lines.length + 1);
    for (int pos = lines.length - 1; pos - 1 > caretLine; pos--) {
      lines[pos] = lines[pos - 1];
    }

    if (caretCharPos == 0) {
      lines[caretLine] = new CodeLine();
      lines[caretLine + 1] = line;
    } else if (caretCharPos == line.totalStrLength) {
      lines[caretLine] = line;
      lines[caretLine + 1] = new CodeLine();
    } else {
      CodeLine[] split = line.split(caretCharPos);
      lines[caretLine] = split[0];
      lines[caretLine + 1] = split[1];
    }

    var cpxDiff = makeDiff(caretLine, caretCharPos, false, "\n");
    refreshMaxLineNumber();
    onEditMade(cpxDiff);
  }

  public void concatLines(int caretLine) {
    if (rejectMappedEdit()) return;
    int caretPos = strLength(caretLine);
    concatLinesOp(caretLine);
    var cpxDiff = makeDiff(caretLine, caretPos, true, "\n");
    refreshMaxLineNumber();
    onEditMade(cpxDiff);
  }

  private void concatLinesOp(int caretLine) {
    if (caretLine + 1 >= length()) return;
    CodeLine newLine = CodeLine.concat(lines[caretLine], lines[caretLine + 1]);
    deleteLineOp(caretLine);
    lines[caretLine] = newLine;
  }

  public void deleteLine(int caretLine) {
    if (rejectMappedEdit()) return;
    String deleted = lines[caretLine].makeString().concat("\n");
    if (lines.length > 1) {
      deleteLineOp(caretLine);
    } else {
      lines[0].delete(0);
    }

    var cpxDiff = makeDiff(caretLine, 0, true, deleted);
    refreshMaxLineNumber();
    onEditMade(cpxDiff);
  }

  public void replaceText(String[] newLines) {
    if (rejectMappedEdit()) return;
    CodeLine[] newCodeLines = new CodeLine[newLines.length];
    for (int i = 0; i < newLines.length; i++) newCodeLines[i] = new CodeLine(newLines[i]);
    applyChange(0, lines.length, newCodeLines);
  }

  public void applyChange(int fromLine, int toLine, CodeLine[] newLines) {
    if (rejectMappedEdit()) return;
    boolean applyFromTheStart = fromLine == 0;
    boolean applyToTheEnd = toLine == length();

    Diff deleteDiff = deleteLines(fromLine, toLine, applyFromTheStart, applyToTheEnd);
    Diff insertDiff = copyLines(fromLine, newLines, applyFromTheStart, applyToTheEnd);
    if (deleteDiff == null && insertDiff == null) return;

    Diff[] changeDiffs = new Diff[2];
    int ptr = 0;
    if (insertDiff != null) changeDiffs[ptr++] = insertDiff;
    if (deleteDiff != null) changeDiffs[ptr++] = deleteDiff;
    changeDiffs = Arrays.copyOf(changeDiffs, ptr);
    var cpxDiff = mkCpxDiff(changeDiffs);
    onChangeApplied(cpxDiff);
    addDiff(cpxDiff);
    currentVersion++;
    refreshMaxLineNumber();
  }

  private Diff copyLines(
      int fromLine, CodeLine[] newLines,
      boolean insertFromTheStart,
      boolean insertToTheEnd
  ) {
    if (newLines.length == 0) return null;

    StringBuilder sb = new StringBuilder();
    if (!insertFromTheStart) sb.append(newLine);
    for (int i = 0; i < newLines.length - 1; i++)
      sb.append(newLines[i].makeString()).append(newLine);
    sb.append(ArrayOp.last(newLines).makeString());
    if (insertFromTheStart && !insertToTheEnd) sb.append(newLine);

    String inserted = sb.toString();
    int insertLine = !insertFromTheStart ? fromLine - 1 : fromLine;
    int insertPos = !insertFromTheStart ? lines[fromLine - 1].totalStrLength : 0;
    Diff insertDiff = new Diff(insertLine, insertPos, false, inserted);
    makeDiffOp(insertDiff);

    CodeLine[] newDocument = new CodeLine[lines.length + newLines.length];
    System.arraycopy(lines, 0, newDocument, 0, fromLine);
    System.arraycopy(newLines, 0, newDocument, fromLine, newLines.length);
    System.arraycopy(lines, fromLine, newDocument, fromLine + newLines.length, lines.length - fromLine);

    this.lines = newDocument;
    updateModelOnDiff(insertDiff, false);
    return insertDiff;
  }

  public CodeLine[] getLines(int fromLine, int toLine) {
    return Arrays.stream(Arrays.copyOfRange(lines, fromLine, toLine))
        .map(line -> new CodeLine(line.makeString()))
        .toArray(CodeLine[]::new);
  }

  public String copyLine(int caretLine) {
    return lines[caretLine].makeString().concat("\n");
  }

  private void deleteLineOp(int caretLine) {
    if (caretLine >= lines.length || caretLine < 0) throw new RuntimeException();
    CodeLine[] doc = new CodeLine[lines.length - 1];
    ArrayOp.remove(lines, caretLine, doc);
    lines = doc;
  }

  public Diff deleteLines(int fromLine, int toLine) {
    if (rejectMappedEdit()) return null;
    return deleteLines(fromLine, toLine, fromLine == 0, toLine == length());
  }

  public Diff deleteLines(
      int fromLine, int toLine,
      boolean deleteFromTheStart,
      boolean deleteToTheEnd
  ) {
    if (rejectMappedEdit()) return null;
    if (fromLine >= toLine) return null;

    StringBuilder deletedSB = new StringBuilder();
    if (!deleteFromTheStart) deletedSB.append(newLine);
    deletedSB.append(new String(getChars(fromLine, toLine)));
    if (!deleteFromTheStart && !deleteToTheEnd) deletedSB.deleteCharAt(deletedSB.length() - 1);

    int deleteLine = !deleteFromTheStart ? fromLine - 1 : fromLine;
    int deletePos = !deleteFromTheStart ? lines[fromLine - 1].totalStrLength : 0;
    Diff diff = new Diff(deleteLine, deletePos, true, deletedSB.toString());
    deleteLinesOp(fromLine, toLine);
    makeDiffOp(diff);
    updateModelOnDiff(diff, false);
    return diff;
  }

  private void deleteLinesOp(int fromLine, int toLine) {
    if (fromLine >= lines.length || fromLine < 0) throw new RuntimeException();
    if (toLine > lines.length || toLine < 0) throw new RuntimeException();
    int newLength = lines.length - toLine + fromLine;
    CodeLine[] doc = new CodeLine[newLength];
    ArrayOp.remove(lines, fromLine, toLine, doc);
    lines = doc;
  }

  public void deleteChar(int caretLine, int caretCharPos) {
    if (rejectMappedEdit()) return;
    if (isLastPosition(caretLine, caretCharPos)) {
      // do nothing at the document end
      if (caretLine != lines.length - 1) {
        var cpxDiff = makeDiff(caretLine, caretCharPos, true, "\n");
        concatLinesOp(caretLine);
        onEditMade(cpxDiff);
      }
    } else {
      var cpxDiff = makeDiff(caretLine, caretCharPos, true, String.valueOf(getChar(caretLine, caretCharPos)));
      lines[caretLine].deleteAt(caretCharPos);
      onEditMade(cpxDiff);
    }
  }

  public void insertAt(int line, int pos, String value) {
    if (rejectMappedEdit()) return;
    lines[line].insertAt(pos, value);
  }

  public void deleteAt(int line, int pos, int endIndex) {
    if (rejectMappedEdit()) return;
    lines[line].delete(pos, endIndex);
  }

  public void insertLines(int line, int pos, String[] lines) {
    if (rejectMappedEdit()) return;
    insertLinesOp(line, pos, lines);
    var cpxDiff = makeDiff(line, pos, false, String.join("\n", lines));
    refreshMaxLineNumber();
    onEditMade(cpxDiff);
  }

  private void insertLinesOp(int line, int pos, String[] lines) {
    if (lines.length == 0) return;
    if (this.lines.length == 0) {
      if (line != 0 && pos != 0) throw new IllegalArgumentException(String.format(
          "Document.insertLines: Trying to insert lines in empty document at (%d: %d)",
          line, pos));
      CodeLine[] doc = new CodeLine[lines.length];
      for (int i = 0; i < doc.length; i++) doc[i] = new CodeLine(lines[i]);
      this.lines = doc;
      return;
    }
    if (lines.length == 1) {
      this.lines[line].insertAt(pos, lines[0]);
      return;
    }
    int len = lines.length - 1;

    CodeLine[] pair = this.lines[line].split(pos);
    CodeLine splitA = pair[0];
    CodeLine splitB = pair[1];
    CodeLine[] doc = Arrays.copyOf(this.lines, this.lines.length + len);

    for (int p = doc.length - 1; p - len > line; p--) {
      doc[p] = doc[p - len];
    }
    splitA.insertToEnd(lines[0]);
    doc[line] = splitA;
    for (int i = 1; i < len; i++) {
      CodeLine newLine = lines[i].isEmpty()
          ? new CodeLine() : new CodeLine(lines[i]);
      doc[line + i] = newLine;
    }
    splitB.insertToBegin(lines[len]);
    doc[line + len] = splitB;
    this.lines = doc;
  }

  private boolean isLastPosition(int caretLine, int caretCharPos) {
    return caretCharPos >= lines[caretLine].totalStrLength;
  }

  public String copy(Selection selection, boolean isCut) {
    String result = getSelectedText(selection);
    if (isCut) deleteSelected(selection, result);
    return result;
  }

  public String getSelectedText(Selection selection) {
    Pos leftPos = selection.getLeftPos();
    Pos rightPos = selection.getRightPos();

    if (leftPos.line == rightPos.line) {
      return lines[leftPos.line].makeString(leftPos.charPos, rightPos.charPos);
    }
    StringBuilder selected = new StringBuilder();

    String firstLine = lines[leftPos.line].makeString(leftPos.charPos);
    selected.append(firstLine).append('\n');

    Arrays.stream(lines, leftPos.line + 1, rightPos.line)
        .forEach(line -> selected.append(line.makeString()).append('\n'));

    String lastLine = lines[rightPos.line].makeString(0, rightPos.charPos);
    selected.append(lastLine);
    return selected.toString();
  }

  public void deleteSelected(Selection selection) {
    deleteSelected(selection, getSelectedText(selection));
  }

  private void deleteSelected(Selection selection, String selected) {
    if (rejectMappedEdit()) return;
    deleteAreaOp(selection.getLeftPos(), selection.getRightPos());
    Pos leftPos = selection.getLeftPos();
    var cpxDiff = makeDiff(leftPos.line, leftPos.charPos, true, selected);
    refreshMaxLineNumber();
    onEditMade(cpxDiff);
  }

  private void deleteAreaOp(Pos from, Pos to) {
    if (from.line == to.line) {
      lines[from.line].delete(from.charPos, to.charPos);
    } else {
      lines[from.line].delete(from.charPos);
      if (to.charPos != 0) lines[to.line].delete(0, to.charPos);
      deleteLinesOp(from.line + 1, to.line);
      concatLinesOp(from.line);
    }
  }

  public boolean hasDefOrUsagesForElementPos(Pos elementPos) {
    return usageToDef.containsKey(elementPos) || defToUsages.containsKey(elementPos);
  }

  public Pos getDefinition(Pos pos) {
    return usageToDef.get(pos);
  }

  public List<Pos> getUsagesList(Pos pos) {
    return defToUsages.get(pos);
  }

  public Pos getElementStart(int line, int charPos) {
    int elemPos = lines[line].getElementStart(charPos);
    return new Pos(line, elemPos);
  }

  public void moveToElementStart(Pos pos) {
    pos.charPos = lines[pos.line].getElementStart(pos.charPos);
  }

  public V2i getLine(int ind) {
    for (int i = 0, sum = 0; i < lines.length; i++) {
      int totalStrLength = lines[i].totalStrLength;
      if (sum + totalStrLength >= ind) return new V2i(i, ind - sum);
      sum += totalStrLength + 1;
    }
    return new V2i(lines.length, 0);
  }

  public int getLineStartInd(int firstLine) {
    return getLineStartInd(0, firstLine);
  }

  public int getLineStartInd(int fromLine, int firstLine) {
    int result = 0;
    int lines = this.lines.length;
    for (int i = fromLine; i < firstLine; ) {
      result += strLength(i);
      if (++i < lines) result++;
    }
    return result;
  }

  public int getVpEnd(int lastLine) {
    int result = 0;
    int limit = Math.min(lastLine + 1, lines.length);
    for (int i = 0; i < limit; i++) {
      result += strLength(i);
      if (i != lines.length - 1) result++;
    }
    return result;
  }

  public String makeString() {
    return new String(getChars());
  }

  public String lineToString(int line) {
    return new String(getChars(line, line + 1));
  }

  public String[] linesToStrings(int fromLine, int toLine) {
    String[] result = new String[toLine - fromLine + 1];
    for (int i = fromLine; i < toLine; i++) result[i - fromLine] = lineToString(i);
    result[result.length - 1] = "";
    return result;
  }

  public CodeElement getCodeElement(Pos pos) {
    return lines[pos.line].getCodeElement(pos.charPos);
  }

  public char[] getChars() {
    return getChars(0, lines.length);
  }

  public char[] getChars(int fromLine, int toLine) {
    char[] dst = new char[getIntervalLength(fromLine, toLine)];
    for (int i = fromLine, pos = 0; i < toLine; ) {
      pos = lines[i].toCharArray(dst, pos);
      if (++i < length()) dst[pos++] = newLine;
    }
    return dst;
  }

  public int[] getIntervals() {
    List<Interval> intervalList = tree.toList();
    int[] intervals = new int[3 * intervalList.size()];
    for (int i = 0, ind = 0; i < intervals.length; ind++) {
      var interval = intervalList.get(ind);
      intervals[i++] = interval.start;
      intervals[i++] = interval.stop;
      intervals[i++] = interval.intervalType;
    }
    return intervals;
  }

  public CpxDiff makeDiff(int line, int from, boolean isDelete, String change) {
    return makeDiff(new Diff(line, from, isDelete, change));
  }

  public CpxDiff makeDiff(Diff diff) {
    currentVersion++;
    var cpxDiff = mkCpxDiff(diff);
    addDiff(cpxDiff);
    makeDiffOp(diff);
    syncEditing(cpxDiff);
    updateModelOnDiff(diff, false);
    return cpxDiff;
  }

  public CpxDiff mkCpxDiff(Diff diff) {
    return mkCpxDiff(ArrayOp.array(diff));
  }

  public CpxDiff mkCpxDiff(
      int[] lines,
      int[] poses,
      boolean isDelete,
      String[] changes,
      V2i caretPos
  ) {
    Diff[] diffs = new Diff[lines.length];
    for (int i = 0; i < lines.length; i++)
      diffs[i] = new Diff(lines[i], poses[i], isDelete, changes[i]);
    return mkCpxDiff(diffs, caretPos);
  }

  public CpxDiff mkCpxDiff(Diff[] diffs) {
    return mkCpxDiff(diffs, caretPos());
  }

  public CpxDiff mkCpxDiff(Diff[] diffs, V2i caretPos) {
    return new CpxDiff(diffs, selection(), caretPos);
  }

  public void makeDiffWithCaretReturn(int line, int from, boolean isDelete, String change) {
    currentVersion++;
    var diff = new Diff(line, from, isDelete, change);
    var cpxDiff = mkCpxDiff(diff);
    addDiff(cpxDiff);
    makeDiffOp(diff);
    syncEditing(cpxDiff);
    updateModelOnDiff(diff, false);
  }

  public void makeTabCpxDiff(
      int[] lines, int[] poses,
      boolean isDelete,
      String[] changes,
      V2i caretPos
  ) {
    if (rejectMappedEdit()) return;
    currentVersion++;
    var cpxDiff = mkCpxDiff(lines, poses, isDelete, changes, caretPos);
    addDiff(cpxDiff);
    for (int i = 0; i < lines.length; i++) makeDiffOp(cpxDiff.diffs[i]);
    syncEditing(cpxDiff);
    for (int i = 0; i < lines.length; i++) {
      if (!isDelete) insertAt(lines[i], 0, EditorConst.tabIndent);
      else deleteAt(lines[i], 0, changes[i].length());
      updateModelOnDiff(cpxDiff.diffs[i], false);
    }
  }

  void makeDiffOp(Diff diff) {
    int posInDoc = getLineStartInd(diff.line) + diff.pos;
    if (diff.isDelete) {
      tree.makeDeleteDiff(posInDoc, diff.change.length());
      scopeGraph.makeDeleteDiff(posInDoc, diff.change.length());
    } else {
      tree.makeInsertDiff(posInDoc, diff.change.length());
      scopeGraph.makeInsertDiff(posInDoc, diff.change.length());
    }
  }

  public CpxDiff undoLastDiff(boolean isRedo) {
    return undoBuffer().undoLastDiff(this, isRedo);
  }

  public CpxDiff undoLastDiff(CpxDiff cpxDiff, boolean isRedo) {
    if (rejectMappedEdit()) return null;
    return doCpxDiff(cpxDiff, isRedo);
  }

  public CpxDiff doSyncCpxDiff(CpxDiff cpxDiff, boolean isRedo) {
    if (rejectMappedEdit()) return null;
    currentVersion++;
    var diffs = cpxDiff.diffs;
    if (isRedo) diffs = ArrayOp.reverse(diffs);
    for (var diff: diffs) doDiff(diff, !isRedo);
    onEditMade(cpxDiff);
    return cpxDiff;
  }

  public CpxDiff doCpxDiff(CpxDiff cpxDiff, boolean isRedo) {
    if (rejectMappedEdit()) return null;
    currentVersion++;
    var diffs = cpxDiff.diffs;
    if (isRedo) diffs = ArrayOp.reverse(diffs);
    for (var diff: diffs) doDiff(diff, isRedo);
    onEditMade(cpxDiff);
    return cpxDiff;
  }

  private void doDiff(Diff diff, boolean isRedo) {
    String[] lines = SplitText.split(diff.change);
    if (diff.isDelete ^ isRedo) {
      insertLinesOp(diff.line, diff.pos, lines);
      tree.makeInsertDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());
      scopeGraph.makeInsertDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());
    } else {
      Pos from = new Pos(diff.line, diff.pos);
      Pos to = lines.length == 1
          ? new Pos(diff.line, diff.pos + lines[0].length())
          : new Pos(diff.line + lines.length - 1, ArrayOp.last(lines).length());
      deleteAreaOp(from, to);
      tree.makeDeleteDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());
      scopeGraph.makeDeleteDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());
    }
    updateModelOnDiff(diff, !isRedo);
  }

  public void setLastDiffTimestamp(double timestamp) {
    lastDiffTimestamp = timestamp;
  }

  public void printIntervals() {
    Debug.consoleInfo("Current Version: " + currentVersion);
    Debug.consoleInfo("Last Parsed Version: " + lastParsedVersion);
    tree.printIntervals(makeString());
  }

  public boolean needReparse(double timestamp) {
    return needReparse() && (timestamp - lastDiffTimestamp > EditorConst.TYPING_STOP_TIME);
  }

  public boolean needReparse() {
    return (lastParsedVersion != currentVersion);
  }

  public void onReparse() {
    lastParsedVersion = currentVersion;
  }

  public Pos getPositionAt(int offset) {
    int lineOffset = 0;
    for (int line = 0; line < lines.length; ++line) {
      CodeLine codeLine = lines[line];
      int lineLength = codeLine.totalStrLength;
      if (offset <= lineOffset + lineLength) {
        return new Pos(line, offset - lineOffset);
      }
      lineOffset += lineLength + 1;
    }
    return new Pos(lines.length, 0);
  }

  public int getOffsetAt(Pos pos) {
    return getOffsetAt(pos.line, pos.charPos);
  }

  public int getOffsetAt(int lineNumber, int column) {
    int position = 0;
    for (int i = 0; i < lineNumber; ) {
      position += lines[i].totalStrLength;
      if (++i < lines.length) position++;
      else break;
    }
    return position + column;
  }

  public void countPrefixes() {
    linePrefixSum = new int[lines.length + 1];
    for (int i = 0; i < lines.length; i++) {
      linePrefixSum[i + 1] = linePrefixSum[i] + lines[i].totalStrLength + 1;
    }
  }

  private Pos binarySearchPosAt(int offset) {
    if (linePrefixSum == null) return getPositionAt(offset);
    int lineInd = Arrays.binarySearch(linePrefixSum, offset);
    if (lineInd < 0) lineInd = -lineInd - 1;
    int len;
    if (lineInd - 1 < 0) len = 0;
    else len = linePrefixSum[lineInd - 1];
    Pos pos = new Pos(lineInd - 1, offset - len);
    if (pos.line < 0) return new Pos(0, 0);
    if (pos.line >= lines.length) {
      return new Pos(lines.length - 1, lines[lines.length - 1].totalStrLength);
    }
    if (pos.charPos >= lines[pos.line].totalStrLength) {
      pos.line++;
      pos.charPos = 0;
    }
    return pos;
  }

  /** Source-space offset → Pos. Identity documents reuse the document-space search. */
  private Pos sourcePosAt(int offset) {
    if (sourceToDoc == null) return binarySearchPosAt(offset);
    int srcLine = sourceLineAtOffset(offset);
    int charPos = offset - srcStart[srcLine];
    int len = strLength(sourceToDoc[srcLine]);
    if (charPos >= len) {
      return srcLine + 1 < sourceToDoc.length
          ? new Pos(srcLine + 1, 0) : new Pos(srcLine, len);
    }
    return new Pos(srcLine, charPos);
  }

  public void onResolve(int[] resolveInts, boolean highlightErrors) {
    ArrayReader reader = new ArrayReader(resolveInts);
    usageToDef.clear();
    defToUsages.clear();
    countPrefixes();
    while (reader.hasNext()) {
      int refFlag = reader.next();
      if (refFlag == -1) continue;
      Pos refSrc = sourcePosAt(reader.next());
      Pos refDoc = toDocPos(refSrc);
      if (refDoc == null) continue;
      CodeElement refElem = lines[refDoc.line].getCodeElement(refDoc.charPos);

      int declFlag = reader.next();
      if (declFlag == -1) {
        if (highlightErrors && refElem != null) {
          refElem.color = ParserConstants.TokenTypes.DEFAULT;
          refElem.style = ParserConstants.TokenStyles.error(refElem.style);
        }
        continue;
      }
      Pos declSrc = sourcePosAt(reader.next());
      int type = reader.next();
      int style = reader.next();

      usageToDef.put(refSrc, declSrc);
      defToUsages.putIfAbsent(declSrc, new ArrayList<>());
      defToUsages.get(declSrc).add(refSrc);
      if (refElem != null) {
        refElem.color = type;
        refElem.style = style;
      }
    }
  }

  private void addDiff(CpxDiff cpxDiff) {
    var undoBuffer = undoBuffer();
    if (undoBuffer == null) return;
    undoBuffer.addDiff(this, cpxDiff);
  }

  public CpxDiff lastDiff() {
    var undoBuffer = undoBuffer();
    if (undoBuffer == null) return null;
    return undoBuffer.lastDiff(this);
  }

  public Pair<CpxDiff, Integer> lastDiffVersion() {
    return undoBuffer().lastDiffVersion(this);
  }

  public UndoBuffer undoBuffer() {
    return getUndoBuffer != null ? getUndoBuffer.get() : null;
  }

  public V2i caretPos() {
    return getCaretPos != null ? getCaretPos.get() : null;
  }

  public Selection selection() {
    return getSelection != null ? getSelection.get() : null;
  }

  public int version() {
    return currentVersion;
  }

  private void updateModelOnDiff(Diff diff, boolean isUndo) {
    if (this.updateModelOnDiff != null) updateModelOnDiff.accept(diff, isUndo);
  }

  private void syncEditing(CpxDiff diff) {
    if (syncEditing != null) syncEditing.accept(diff, false);
  }

  private void onEditMade(CpxDiff cpxDiff) {
    if (onEditMade != null) onEditMade.accept(cpxDiff);
  }

  private void onChangeApplied(CpxDiff cpxDiff) {
    if (onChangeApplied != null) onChangeApplied.accept(cpxDiff);
  }
}
