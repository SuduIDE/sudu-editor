/*
 *  Copyright 2023 Alexey Andreev.
 *
 *  This code is granted by Alexey Andreev,
 *      the author of the TeaVM project teavm.org
 */
package org.sudu.experiments.json;

public class JsonSyntaxException extends RuntimeException {
  private final int lineNumber;
  private final int columnNumber;
  private final String error;

  public JsonSyntaxException(int lineNumber, int columnNumber, String error) {
    super("JSON syntax error at " + (lineNumber + 1) + ":" + (columnNumber + 1) + ": " + error);
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.error = error;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public String getError() {
    return error;
  }
}
