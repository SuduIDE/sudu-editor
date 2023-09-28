package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;

import java.util.List;

class ExternalHighlights {
  int caretLine, caretColumn;
  DocumentHighlight[] data;

  ExternalHighlights(int line, int column, DocumentHighlight[] data) {
    this.caretLine = line;
    this.caretColumn = column;
    this.data = data;
  }

  public void buildUsages(Document document, List<CodeElement> usages) {
    int length = document.length();
    for (DocumentHighlight hl : data) {
      int startLine = hl.range.startLineNumber;
      int endLine = hl.range.endLineNumber;
      for (int ln = startLine; ln <= endLine && ln < length; ln++) {
        CodeLine codeLine = document.line(ln);
        if (codeLine.elements.length == 0) continue;
        int startChar = ln > startLine ? 0
            : Math.max(hl.range.startColumn, 0);
        int endChar = ln < endLine ? codeLine.totalStrLength
            : Math.min(hl.range.endColumn, codeLine.totalStrLength);
        while (startChar < endChar) {
          int elementIndex = codeLine.getElementIndex(startChar);
          int elementStart = codeLine.getElementStartAtIndex(elementIndex);
          int elementEnd = codeLine.getElementEndAtIndex(elementIndex);
          CodeElement element = codeLine.get(elementIndex);
          if (elementStart == startChar && elementEnd <= endChar) {
            usages.add(element);
          } else {
            System.err.println(
                "highlight at (" + ln + ':' + startChar + ") does not match code model");
          }
          startChar = elementEnd;
        }
      }
    }
  }

  public void dump() {
    for (var highlight : data) {
      var range = highlight.range;
      Debug.consoleInfo("Range: " + "[(" + range.startLineNumber + ","
          + range.startColumn + "),(" + range.endLineNumber + "," + range.endColumn + ")]");
      Debug.consoleInfo("Kind: " + highlight.kind);
    }
  }
}
