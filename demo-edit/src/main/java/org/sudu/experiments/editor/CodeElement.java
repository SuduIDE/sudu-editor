package org.sudu.experiments.editor;

import org.sudu.experiments.parser.ParserConstants;

public class CodeElement {
  public String s;

  // color values: org.sudu.experiments.parser.ParserConstants.TokenTypes
  int color;

  // style:
  //   2 bits for font index
  //   2 bits for underline index
  int style;

  public CodeElement(String s) {
    this(s, 0, 0);
  }

  public CodeElement(String s, int color) {
    this(s, color, 0);
  }

  public CodeElement(String s, int color, int style) {
    this.s = s;
    this.color = color;
    this.style = style;
  }

  final int fontIndex() {
    return style & 0b0011;
  }

  final int underlineIndex() {
    return (style & 0b1100) >> 2;
  }

  final int setUnderlineIndex(int ul) {
    return (style & 0b11) + (ul << 2);
  }

  boolean isError() {
    return color == ParserConstants.TokenTypes.ERROR;
  }

  public CodeElement(String s, int color, boolean bold, boolean italic) {
    this(s, color, fontIndex(bold, italic));
  }

  public static int fontIndex(boolean bold, boolean italic) {
    return (bold ? 2 : 0) + (italic ? 1 : 0);
  }

  public boolean bold() {
    return (style & 2) != 0;
  }

  public void setBold(boolean b) { if (b) style |= 2; else style &= ~2; }

  public boolean italic() {
    return (style & 1) != 0;
  }

  public CodeElement splitLeft(int pos) {
    return new CodeElement(s.substring(0, pos), color, style);
  }
  public CodeElement splitRight(int pos) {
    return new CodeElement(s.substring(pos), color, style);
  }

  public int length() {
    return s.length();
  }

  public String toString() {
    StringBuilder b = new StringBuilder(s);
    boolean bold = bold();
    boolean italic = italic();
    if (bold || italic) b.append(" -");
    if (bold) b.append(" bold");
    if (italic) b.append(" italic");
    return b.toString();
  }

  public CodeElement deleteAt(int pos) {
    if (pos <= 0) {
      return new CodeElement(s.substring(1), color, style);
    }
    if (pos >= s.length() - 1)
      return new CodeElement(s.substring(0, s.length()-1), color, style);
    char[] data = new char[s.length() - 1];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = pos; i < data.length; i++) data[i] = s.charAt(i + 1);
    return new CodeElement(new String(data), color, style);
  }

  public CodeElement insertAt(int pos, String value) {
    if (pos <= 0) return new CodeElement(value.concat(s), color, style);
    if (pos >= s.length()) return new CodeElement(s.concat(value), color, style);
    int x = value.length();
    int y = x + pos, end = s.length() - pos;
    char[] data = new char[s.length() + x];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = 0; i < x; i++) data[i + pos] = value.charAt(i);
    for (int i = 0; i < end; i++) data[i + y] = s.charAt(i + pos);
    return new CodeElement(new String(data), color, style);
  }

  public char charAt(int index) {
    return s.charAt(index);
  }
}
