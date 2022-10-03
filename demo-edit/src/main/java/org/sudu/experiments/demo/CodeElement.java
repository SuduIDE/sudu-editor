package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

class CodeElement {
  String s;
  Color colorF;
  Color colorB;

  CodeElement(String s, Color color) {
    this(s, color, null);
  }

  CodeElement(String s, Color colorF, Color colorB) {
    this.s = s;
    this.colorF = colorF;
    this.colorB = colorB;
//    this.fontSize = 0;
  }

  public Color colorB(Color _default) {
    return colorB == null ? _default : colorB;
  }

  public CodeElement splitLeft(int pos) {
    return new CodeElement(s.substring(0, pos), colorF, colorB);
  }
  public CodeElement splitRight(int pos) {
    return new CodeElement(s.substring(pos), colorF, colorB);
  }

  public String toString() {
    return s;
  }

  public CodeElement deleteAt(int pos) {
    if (pos <= 0) {
      return new CodeElement(s.substring(1), colorF, colorB);
    }
    if (pos >= s.length() - 1)
      return new CodeElement(s.substring(0, s.length()-1), colorF, colorB);
    char[] data = new char[s.length() - 1];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = pos; i < data.length; i++) data[i] = s.charAt(i + 1);
    return new CodeElement(new String(data), colorF, colorB);
  }

  public CodeElement insertAt(int pos, String value) {
    if (pos <= 0) return new CodeElement(value.concat(s), colorF, colorB);
    if (pos >= s.length()) return new CodeElement(s.concat(value), colorF, colorB);
    int x = value.length();
    int y = x + pos, end = s.length() - pos;
    char[] data = new char[s.length() + x];
    for (int i = 0; i < pos; i++) data[i] = s.charAt(i);
    for (int i = 0; i < x; i++) data[i + pos] = value.charAt(i);
    for (int i = 0; i < end; i++) data[i + y] = s.charAt(i + pos);
    return new CodeElement(new String(data), colorF, colorB);
  }
}
