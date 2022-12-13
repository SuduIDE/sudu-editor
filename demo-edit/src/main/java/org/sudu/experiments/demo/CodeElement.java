package org.sudu.experiments.demo;

class CodeElement {
  String s;
  int color;
  int fontIndex;

  CodeElement(String s) {
    this(s, 0, 0);
  }

  CodeElement(String s, int color) {
    this(s, color, 0);
  }

  CodeElement(String s, int color, int font) {
    this.s = s;
    this.color = color;
    this.fontIndex = font;
  }

  CodeElement(String s, int color, boolean bold, boolean italic) {
    this(s, color, fontIndex(bold, italic));
  }

  public static int fontIndex(boolean bold, boolean italic) {
    return (bold ? 2 : 0) + (italic ? 1 : 0);
  }

  public static boolean bold(int fontIndex) {
    return (fontIndex & 2) != 0;
  }

  public static boolean italic(int fontIndex) {
    return (fontIndex & 1) != 0;
  }

  public CodeElement splitLeft(int pos) {
    return new CodeElement(s.substring(0, pos), color, fontIndex);
  }
  public CodeElement splitRight(int pos) {
    return new CodeElement(s.substring(pos), color, fontIndex);
  }

  public String toString() {
    StringBuilder b = new StringBuilder(s);
    boolean bold = bold(fontIndex);
    boolean italic = italic(fontIndex);
    if (bold || italic) b.append(" -");
    if (bold) b.append(" bold");
    if (italic) b.append(" italic");
    return b.toString();
  }

  public CodeElement deleteAt(int pos) {
    if (pos <= 0) {
      return new CodeElement(s.substring(1), color, fontIndex);
    }
    if (pos >= s.length() - 1)
      return new CodeElement(s.substring(0, s.length()-1), color, fontIndex);
    char[] data = new char[s.length() - 1];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = pos; i < data.length; i++) data[i] = s.charAt(i + 1);
    return new CodeElement(new String(data), color, fontIndex);
  }

  public CodeElement insertAt(int pos, String value) {
    if (pos <= 0) return new CodeElement(value.concat(s), color, fontIndex);
    if (pos >= s.length()) return new CodeElement(s.concat(value), color, fontIndex);
    int x = value.length();
    int y = x + pos, end = s.length() - pos;
    char[] data = new char[s.length() + x];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = 0; i < x; i++) data[i + pos] = value.charAt(i);
    for (int i = 0; i < end; i++) data[i + y] = s.charAt(i + pos);
    return new CodeElement(new String(data), color, fontIndex);
  }
}
