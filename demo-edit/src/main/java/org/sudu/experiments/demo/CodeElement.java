package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

class CodeElement {
  String s;
  Color colorF;
  Color colorB;
  int fontIndex;

  CodeElement(String s, Color color) {
    this(s, color, null, 0);
  }

  CodeElement(String s, Color color, int fontIndex) {
    this(s, color, null, fontIndex);
  }

  CodeElement(String s, Color colorF, Color colorB, boolean bold, boolean italic) {
    this(s, colorF, colorB, fontIndex(bold, italic));
  }

  CodeElement(String s, Color colorF, Color colorB, int fontIndex) {
    this.s = s;
    this.colorF = colorF;
    this.colorB = colorB;
    this.fontIndex = fontIndex;
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

  public Color colorB(Color _default) {
    return colorB == null ? _default : colorB;
  }

  public CodeElement splitLeft(int pos) {
    return new CodeElement(s.substring(0, pos), colorF, colorB, fontIndex);
  }
  public CodeElement splitRight(int pos) {
    return new CodeElement(s.substring(pos), colorF, colorB, fontIndex);
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
      return new CodeElement(s.substring(1), colorF, colorB, fontIndex);
    }
    if (pos >= s.length() - 1)
      return new CodeElement(s.substring(0, s.length()-1), colorF, colorB, fontIndex);
    char[] data = new char[s.length() - 1];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = pos; i < data.length; i++) data[i] = s.charAt(i + 1);
    return new CodeElement(new String(data), colorF, colorB, fontIndex);
  }

  public CodeElement insertAt(int pos, String value) {
    if (pos <= 0) return new CodeElement(value.concat(s), colorF, colorB, fontIndex);
    if (pos >= s.length()) return new CodeElement(s.concat(value), colorF, colorB, fontIndex);
    int x = value.length();
    int y = x + pos, end = s.length() - pos;
    char[] data = new char[s.length() + x];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = 0; i < x; i++) data[i + pos] = value.charAt(i);
    for (int i = 0; i < end; i++) data[i + y] = s.charAt(i + pos);
    return new CodeElement(new String(data), colorF, colorB, fontIndex);
  }
}
