package org.sudu.experiments;

public class SplitInfo {

  public static final byte LF = 0;   // "\n"
  public static final byte CRLF = 1; // "\r\n"
  public static final byte CR = 2;   // "\r"

  public String[] lines;
  public byte[] lineSeparators;

  public SplitInfo(String[] lines, byte[] lineSeparators) {
    this.lines = lines;
    this.lineSeparators = lineSeparators;
  }
}
