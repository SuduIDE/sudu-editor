package org.sudu.experiments.demo;

import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Document {
  CodeLine[] document;

  public Document(CodeLine ... data) {
    document = data;
  }

  public Document(int n) {
    this(TestText.document(n, false));
  }

  public CodeLine line(int i) {
    return document[i];
  }

  public void invalidateFont() {
    for (CodeLine codeLine : document) {
      codeLine.invalidateCache();
    }
  }

  public int length() {
    return document.length;
  }
  public int strLength(int i) {
    return document[i].totalStrLength;
  }

  public void newLineOp(int caretLine, int caretCharPos) {
    CodeLine line = document[caretLine];

    document = Arrays.copyOf(document, document.length + 1);
    for (int pos = document.length - 1; pos - 1 > caretLine; pos--) {
      document[pos] = document[pos - 1];
    }

    if (caretCharPos == 0) {
      document[caretLine] = new CodeLine();
      document[caretLine + 1] = line;
    } else if (caretCharPos == line.totalStrLength) {
      document[caretLine] = line;
      document[caretLine + 1] = new CodeLine();
    } else {
      CodeLine[] split = line.split(caretCharPos);
      document[caretLine] = split[0];
      document[caretLine + 1] = split[1];
    }
  }

  public void concatLines(int caretLine) {
    CodeLine newLine = CodeLine.concat(document[caretLine], document[caretLine + 1]);
    CodeLine[] doc = deleteLineOp(caretLine);
    doc[caretLine] = newLine;
    document = doc;
  }

  public void deleteLine(int caretLine) {
    document = deleteLineOp(caretLine);
  }

  public String copyLine(int caretLine) {
    return document[caretLine].makeString().concat("\n");
  }

  private CodeLine[] deleteLineOp(int caretLine) {
    if (caretLine >= document.length || caretLine < 0) throw new RuntimeException();
    CodeLine[] doc = new CodeLine[document.length - 1];
    ArrayOp.remove(document, caretLine, doc);
    return doc;
  }

  public void deleteLines(int fromLine, int toLine) {
    document = deleteLinesOp(fromLine, toLine);
  }

  private CodeLine[] deleteLinesOp(int fromLine, int toLine) {
    if (fromLine >= document.length || fromLine < 0) throw new RuntimeException();
    if (toLine > document.length || toLine < 0) throw new RuntimeException();

    CodeLine[] doc = new CodeLine[document.length - toLine + fromLine];
    ArrayOp.remove(document, fromLine, toLine, doc);
    return doc;
  }

  public void deleteChar(int caretLine, int caretCharPos) {
    if (isLastPosition(caretLine, caretCharPos)) {
      // do nothing at the document end
      if (caretLine != document.length - 1) {
        concatLines(caretLine);
      }
    } else {
      document[caretLine].deleteAt(caretCharPos);
    }
  }

  public void insertAt(int line, int pos, String value) {
    document[line].insertAt(pos, value);
  }

  public void insertLines(int line, int pos, String[] lines) {
    if (lines.length == 0) return;
    if (lines.length == 1) {
      document[line].insertAt(pos, lines[0]);
      return;
    }
    int len = lines.length - 1;

    CodeLine[] splited = document[line].split(pos);
    CodeLine[] doc = Arrays.copyOf(document, document.length + len);

    for (int p = doc.length - 1; p - len > line; p--) {
      doc[p] = doc[p - len];
    }
    splited[0].insertToEnd(lines[0]);
    doc[line] = splited[0];
    for (int i = 1; i < len; i++) {
      CodeLine newLine;
      if (!lines[i].isEmpty())
        newLine = new CodeLine(new CodeElement(lines[i], 0, 0));
      else
        newLine = new CodeLine();
      doc[line + i] = newLine;
    }
    splited[1].insertToBegin(lines[len]);
    doc[line + len] = splited[1];
    document = doc;
  }

  private boolean isLastPosition(int caretLine, int caretCharPos) {
    return caretCharPos >= document[caretLine].totalStrLength;
  }

  public String copy(Selection selection, boolean isCut) {
    Selection.SelPos leftPos = selection.getLeftPos();
    Selection.SelPos rightPos = selection.getRightPos();
    String result;

    if (leftPos.line == rightPos.line) {
      String line = document[leftPos.line].makeString();
      result = line.substring(leftPos.charInd, rightPos.charInd);
    } else {
      StringBuilder sb = new StringBuilder();

      String firstLine = document[leftPos.line]
          .makeString(leftPos.charInd);
      sb.append(firstLine).append('\n');

      Arrays.stream(document, leftPos.line + 1, rightPos.line)
          .forEach(line -> sb.append(line.makeString()).append('\n'));

      String lastLine = document[rightPos.line]
          .makeString(0, rightPos.charInd);
      sb.append(lastLine);

      result = sb.toString();
    }
    if (isCut) deleteSelected(selection);
    return result;
  }

  public void deleteSelected(Selection selection) {
    Selection.SelPos leftPos = selection.getLeftPos();
    Selection.SelPos rightPos = selection.getRightPos();
    if (leftPos.line == rightPos.line) {
      document[leftPos.line].delete(leftPos.charInd, rightPos.charInd);
    } else {
      document[leftPos.line].delete(leftPos.charInd);
      document[rightPos.line].delete(0, rightPos.charInd);
      deleteLines(leftPos.line + 1, rightPos.line);
      concatLines(leftPos.line);
    }
  }

  public byte[] getBytes() {
    List<String> lines = Arrays.stream(document).map(CodeLine::makeString).collect(Collectors.toList());
    String documentText = String.join("\n", lines);
    return documentText.getBytes(StandardCharsets.UTF_8);
  }
}
