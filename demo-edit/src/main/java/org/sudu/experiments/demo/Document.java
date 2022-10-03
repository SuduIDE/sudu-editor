package org.sudu.experiments.demo;

import java.util.Arrays;

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
    if (caretLine >= document.length - 1 || caretLine < 0) throw new RuntimeException();
    CodeLine[] doc = new CodeLine[document.length - 1];
    if (caretLine > 0) System.arraycopy(document, 0, doc, 0, caretLine);
    doc[caretLine] = CodeLine.concat(document[caretLine], document[caretLine + 1]);
    if (doc.length > caretLine + 1) {
      System.arraycopy(document, caretLine + 2, doc, caretLine + 1, doc.length - caretLine - 1);
    }
    document = doc;
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

  private boolean isLastPosition(int caretLine, int caretCharPos) {
    return caretCharPos >= document[caretLine].totalStrLength;
  }
}
