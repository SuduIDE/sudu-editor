package org.sudu.experiments;

public class SplitInfo {

  public static final byte CRLF = 0;
  public static final byte CR = 1;
  public static final byte LF = 2;

  public String[] lines;
  public byte[] lineSeparators;

  public SplitInfo(String[] lines, byte[] lineSeparators) {
    this.lines = lines;
    this.lineSeparators = lineSeparators;
  }
}
