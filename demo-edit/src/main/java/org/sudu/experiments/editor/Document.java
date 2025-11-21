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

  int currentVersion;
  int lastParsedVersion;
  double lastDiffTimestamp;
  public BiConsumer<Diff, Boolean> updateModelOnDiff;
  public BiConsumer<CpxDiff, Boolean> syncEditing;
  public Supplier<UndoBuffer> getUndoBuffer;
  public Supplier<V2i> getCaretPos;
  public Supplier<Selection> getSelection;
  public Runnable onDiffMade;

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
    undoBuffer().clear(this);
    usageToDef.clear();
    defToUsages.clear();

    currentVersion = lastParsedVersion = 0;
    tree = initialInterval();
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

    makeDiff(caretLine, caretCharPos, false, "\n");
    onDiffMade();
  }

  public void concatLines(int caretLine) {
    concatLinesOp(caretLine);
    makeDiff(caretLine, strLength(caretLine), true, "\n");
    onDiffMade();
  }

  private void concatLinesOp(int caretLine) {
    if (caretLine + 1 >= length()) return;
    CodeLine newLine = CodeLine.concat(lines[caretLine], lines[caretLine + 1]);
    deleteLineOp(caretLine);
    lines[caretLine] = newLine;
  }

  public void deleteLine(int caretLine) {
    String deleted = lines[caretLine].makeString().concat("\n");
    if (lines.length > 1) {
      deleteLineOp(caretLine);
    } else {
      lines[0].delete(0);
    }

    makeDiff(caretLine, 0, true, deleted);
    onDiffMade();
  }

  public void replaceText(String[] newLines) {
    CodeLine[] newCodeLines = new CodeLine[newLines.length];
    for (int i = 0; i < newLines.length; i++) newCodeLines[i] = new CodeLine(newLines[i]);
    applyChange(0, lines.length, newCodeLines);
  }

  public void applyChange(int fromLine, int toLine, CodeLine[] newLines) {
    Diff deleteDiff = deleteLines(fromLine, toLine);
    Diff insertDiff = copyLines(fromLine, newLines);
    if (deleteDiff == null && insertDiff == null) return;
    onDiffMade();

    Diff[] changeDiffs = new Diff[2];
    int ptr = 0;
    if (insertDiff != null) changeDiffs[ptr++] = insertDiff;
    if (deleteDiff != null) changeDiffs[ptr++] = deleteDiff;
    changeDiffs = Arrays.copyOf(changeDiffs, ptr);
    undoBuffer().addDiff(this, mkCpxDiff(changeDiffs));
    currentVersion++;
  }

  private Diff copyLines(int fromLine, CodeLine[] newLines) {
    if (newLines.length == 0) return null;
    boolean insertFromStart = fromLine == 0;

    StringBuilder sb = new StringBuilder();
    if (!insertFromStart) sb.append(newLine);
    for (int i = 0; i < newLines.length - 1; i++)
      sb.append(newLines[i].makeString()).append("\n");
    sb.append(newLines[newLines.length - 1].makeString());

    String inserted = sb.toString();
    int insertLine = !insertFromStart ? fromLine - 1 : fromLine;
    int insertPos = !insertFromStart ? lines[fromLine - 1].totalStrLength : 0;
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
    if (fromLine >= toLine) return null;
    boolean deleteFromStart = fromLine == 0;
    boolean deleteToEnd = toLine == length();

    StringBuilder deletedSB = new StringBuilder();
    if (!deleteFromStart) deletedSB.append(newLine);
    deletedSB.append(new String(getChars(fromLine, toLine)));
    if (!deleteFromStart && !deleteToEnd) deletedSB.deleteCharAt(deletedSB.length() - 1);

    int deleteLine = !deleteFromStart ? fromLine - 1 : fromLine;
    int deletePos = !deleteFromStart ? lines[fromLine - 1].totalStrLength : 0;
    Diff diff = new Diff(deleteLine, deletePos, true, deletedSB.toString());
    deleteLinesOp(fromLine, toLine);
    makeDiffOp(diff);
    updateModelOnDiff(diff, false);
    return diff;
  }

  private void deleteLinesOp(int fromLine, int toLine) {
    if (fromLine >= lines.length || fromLine < 0) throw new RuntimeException();
    if (toLine > lines.length || toLine < 0) throw new RuntimeException();

    CodeLine[] doc = new CodeLine[lines.length - toLine + fromLine];
    ArrayOp.remove(lines, fromLine, toLine, doc);
    lines = doc;
  }

  public void deleteChar(int caretLine, int caretCharPos) {
    if (isLastPosition(caretLine, caretCharPos)) {
      // do nothing at the document end
      if (caretLine != lines.length - 1) {
        makeDiff(caretLine, caretCharPos, true, "\n");
        concatLinesOp(caretLine);
      }
    } else {
      makeDiff(caretLine, caretCharPos, true, String.valueOf(getChar(caretLine, caretCharPos)));
      lines[caretLine].deleteAt(caretCharPos);
    }
    onDiffMade();
  }

  public void insertAt(int line, int pos, String value) {
    lines[line].insertAt(pos, value);
  }

  public void insertLines(int line, int pos, String[] lines) {
    insertLinesOp(line, pos, lines);
    makeDiff(line, pos, false, String.join("\n", lines));
    onDiffMade();
  }

  private void insertLinesOp(int line, int pos, String[] lines) {
    if (lines.length == 0) return;
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
    Selection.SelPos leftPos = selection.getLeftPos();
    Selection.SelPos rightPos = selection.getRightPos();

    if (leftPos.line == rightPos.line) {
      String line = lines[leftPos.line].makeString();
      return line.substring(leftPos.charInd, rightPos.charInd);
    } else {
      StringBuilder selected = new StringBuilder();

      String firstLine = lines[leftPos.line]
          .makeString(leftPos.charInd);
      selected.append(firstLine).append('\n');

      Arrays.stream(lines, leftPos.line + 1, rightPos.line)
          .forEach(line -> selected.append(line.makeString()).append('\n'));

      String lastLine = lines[rightPos.line]
          .makeString(0, rightPos.charInd);
      selected.append(lastLine);
      return selected.toString();
    }
  }

  public void deleteSelected(Selection selection) {
    deleteSelected(selection, getSelectedText(selection));
  }

  private void deleteSelected(Selection selection, String selected) {
    deleteSelectedOp(selection);
    Selection.SelPos leftPos = selection.getLeftPos();
    makeDiff(leftPos.line, leftPos.charInd, true, selected);
    onDiffMade();
  }

  private void deleteSelectedOp(Selection selection) {
    Selection.SelPos leftPos = selection.getLeftPos();
    Selection.SelPos rightPos = selection.getRightPos();
    if (leftPos.line == rightPos.line) {
      lines[leftPos.line].delete(leftPos.charInd, rightPos.charInd);
    } else {
      lines[leftPos.line].delete(leftPos.charInd);
      if (rightPos.charInd != 0) lines[rightPos.line].delete(0, rightPos.charInd);
      deleteLinesOp(leftPos.line + 1, rightPos.line);
      concatLinesOp(leftPos.line);
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
    pos.pos = lines[pos.line].getElementStart(pos.pos);
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
    return lines[pos.line].getCodeElement(pos.pos);
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

  public void makeDiff(int line, int from, boolean isDelete, String change) {
    makeDiff(new Diff(line, from, isDelete, change));
  }

  public void makeDiff(Diff diff) {
    currentVersion++;
    var cpxDiff = mkCpxDiff(diff);
    undoBuffer().addDiff(this, cpxDiff);
    makeDiffOp(diff);
    updateModelOnDiff(diff, false);
    syncEditing(cpxDiff, false);
  }

  public CpxDiff mkCpxDiff(Diff diff) {
    return mkCpxDiff(ArrayOp.array(diff));
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
    undoBuffer().addDiff(this, mkCpxDiff(diff));
    makeDiffOp(diff);
    updateModelOnDiff(diff, false);
  }

  public void makeComplexDiff(
      int[] lines,
      int[] from,
      boolean[] areDeletes,
      String[] changes,
      V2i caretPos,
      BiConsumer<Integer, String> editorAction
  ) {
    currentVersion++;

    Diff[] diffs = new Diff[lines.length];
    for (int i = 0; i < lines.length; i++) {
      diffs[i] = new Diff(lines[i], from[i], areDeletes[i], changes[i]);
    }
    var cpxDiff = mkCpxDiff(diffs, caretPos);
    undoBuffer().addDiff(this, cpxDiff);
    for (int i = 0; i < lines.length; i++) {
      Diff diff = new Diff(lines[i], from[i], areDeletes[i], changes[i]);
      makeDiffOp(diff);
      editorAction.accept(lines[i], changes[i]);
      updateModelOnDiff(diff, false);
    }
    syncEditing(cpxDiff, false);
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
    return doCpxDiff(cpxDiff, isRedo);
  }

  public CpxDiff doCpxDiff(CpxDiff cpxDiff, boolean isRedo) {
    currentVersion++;
    var diffs = cpxDiff.diffs;
    if (isRedo) diffs = ArrayOp.reverse(diffs);
    for (var diff: diffs) doDiff(diff, isRedo);
    onDiffMade();
    return cpxDiff;
  }

  private void doDiff(Diff diff, boolean isRedo) {
    String[] lines = SplitText.split(diff.change);
    if (diff.isDelete ^ isRedo) {
      insertLinesOp(diff.line, diff.pos, lines);
      tree.makeInsertDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());
      scopeGraph.makeInsertDiff(getLineStartInd(diff.line) + diff.pos, diff.change.length());

    } else {
      Selection selection = new Selection();
      selection.startPos.set(diff.line, diff.pos);

      if (lines.length == 1) {
        selection.endPos.set(diff.line, diff.pos + lines[0].length());
      } else {
        selection.endPos.set(diff.line + lines.length - 1, lines[lines.length - 1].length());
      }

      deleteSelectedOp(selection);
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
    return getOffsetAt(pos.line, pos.pos);
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
    if (pos.pos >= lines[pos.line].totalStrLength) {
      pos.line++;
      pos.pos = 0;
    }
    return pos;
  }

  public void onResolve(int[] resolveInts, boolean highlightErrors) {
    ArrayReader reader = new ArrayReader(resolveInts);
    usageToDef.clear();
    defToUsages.clear();
    countPrefixes();
    while (reader.hasNext()) {
      int refFlag = reader.next();
      if (refFlag == -1) continue;
      var refPos = binarySearchPosAt(reader.next());
      var refElem = lines[refPos.line].getCodeElement(refPos.pos);

      int declFlag = reader.next();
      if (declFlag == -1) {
        if (highlightErrors) {
          refElem.color = ParserConstants.TokenTypes.DEFAULT;
          refElem.style = ParserConstants.TokenStyles.error(refElem.style);
        }
        continue;
      }
      var declPos = binarySearchPosAt(reader.next());
      var type = reader.next();
      var style = reader.next();

      usageToDef.put(refPos, declPos);
      defToUsages.putIfAbsent(declPos, new ArrayList<>());
      defToUsages.get(declPos).add(refPos);
      refElem.color = type;
      refElem.style = style;
    }
  }

  public CpxDiff lastDiff() {
    return undoBuffer().lastDiff(this);
  }

  public Pair<CpxDiff, Integer> lastDiffVersion() {
    return undoBuffer().lastDiffVersion(this);
  }

  public UndoBuffer undoBuffer() {
    return getUndoBuffer.get();
  }

  public V2i caretPos() {
    return getCaretPos != null ? getCaretPos.get() : new V2i(0, 0);
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

  private void syncEditing(CpxDiff diff, boolean isUndo) {
    if (syncEditing != null) syncEditing.accept(diff, isUndo);
  }

  private void onDiffMade() {
    if (onDiffMade != null) onDiffMade.run();
  }
}
