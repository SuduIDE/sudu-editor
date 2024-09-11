package org.sudu.experiments.math;

public class Color extends V4f {

  public int r, g, b, a;

  public Color(int gray) {
    this(gray, gray, gray, 255);
  }

  public Color(int r, int g, int b) {
    this(r, g, b, 255);
  }

  // from web color hex string
  //   #rgb
  //   #rrggbb
  //   #rrggbbaa
  public Color(String s) {
    if (s.length() != 4 && s.length() != 7 && s.length() != 9 || s.charAt(0) != '#') return;

    if (s.length() == 4) {
      r = valueOfHex(s.charAt(1)) * 17;
      g = valueOfHex(s.charAt(2)) * 17;
      b = valueOfHex(s.charAt(3)) * 17;
      a = 255;
    } else {
      r = valueOfHex2(s.charAt(1), s.charAt(2));
      g = valueOfHex2(s.charAt(3), s.charAt(4));
      b = valueOfHex2(s.charAt(5), s.charAt(6));
      a = s.length() == 9 ? valueOfHex2(s.charAt(7), s.charAt(8)) : 255;
    }
    Cvt.fromRGBA(r, g, b, a, this);
  }

  public Color(int r, int g, int b, int a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    Cvt.fromRGBA(r, g, b, a, this);
  }

  public Color(Color c) {
    r = c.r; g = c.g; b = c.b; a = c.a;
    super.set(c);
  }

  public String toHexString() {
    return Cvt.toHexString(r, g, b);
  }

  @Override
  public int hashCode() {
    return a * 0x1000000 + r * 0x10000 + g * 0x100 + b;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder()
        .append("r:").append(r)
        .append(", g: ").append(g)
        .append(", b: ").append(b);
    if (a != 255) sb.append(", a:").append(a);
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    return o != null && getClass() == o.getClass() && equals((Color) o);
  }

  public boolean equals(Color color) {
    return super.equals(color) &&
        r == color.r && g == color.g && b == color.b && a == color.a;
  }

  void computeRgba() {
    r = (int) (x * 255 + .5f);
    g = (int) (y * 255 + .5f);
    b = (int) (z * 255 + .5f);
    a = (int) (w * 255 + .5f);
  }

  public interface Cvt {
    static V4f gray(int value) {
      return fromRGBA(value, value, value, 255);
    }

    static V4f gray(int value, V4f target) {
      return fromRGBA(value, value, value, 255, target);
    }

    static V4f fromRGB(int r, int g, int b) {
      return fromRGBA(r, g, b, 255, new V4f());
    }

    static V4f fromRGB(int r, int g, int b, V4f target) {
      return fromRGBA(r, g, b, 255, target);
    }

    static V4f fromRGBA(int r, int g, int b, int a) {
      return fromRGBA(r, g, b, a, new V4f());
    }

    static V4f fromRGBA(int r, int g, int b, int a, V4f target) {
      target.set(r / 255.f, g / 255.f, b / 255.f, a / 255.f);
      return target;
    }

    static V4f fromHSV(double h, double s, double v) {
      return fromHSV(h, s, v, 1, new V4f());
    }

    // valid range for all arguments is [0...1]
    static V4f fromHSV(double h, double s, double v, V4f result) {
      double h6 = h * 6;
      double vs = v * s;
      double c1 = vs * (1 - Math.abs(h6 % 2 - 1));
      double base = v - vs;

      double r = 0, g = 0, b = 0;

      if (h6 < 1) { r = vs; g = c1; } else
      if (h6 < 2) { r = c1; g = vs; } else
      if (h6 < 3) { g = vs; b = c1; } else
      if (h6 < 4) { g = c1; b = vs; } else
      if (h6 < 5) { r = c1; b = vs; } else
                  { r = vs; b = c1; }

      result.x = (float) (r + base);
      result.y = (float) (g + base);
      result.z = (float) (b + base);
      return result;
    }

    static V4f fromHSV(double h, double s, double v, float alpha, V4f result) {
      return fromHSV(h, s, v, result).setW(alpha);
    }

    static double fixHue(double h) {
      return h > 1 ? h - (int)h : h < 0 ? (h + 1 - (int) h) : h;
    }

    static String toHexString(int r, int g, int b) {
      char[] data = new char[7];
      data[0] = '#';
      data[1] = hexDigit(r / 16);
      data[2] = hexDigit(r % 16);
      data[3] = hexDigit(g / 16);
      data[4] = hexDigit(g % 16);
      data[5] = hexDigit(b / 16);
      data[6] = hexDigit(b % 16);
      return new String(data);
    }

    private static char hexDigit(int x) {
      return (char) (x < 10 ? x + '0' : x + 'A' - 10);
    }
  }

  private static int valueOfHex(char x) {
    return
        ('0' <= x && x <= '9') ? x - '0' :
        ('A' <= x && x <= 'F') ? x - 'A' + 10 :
        ('a' <= x && x <= 'f') ? x - 'a' + 10 : 0;
  }

  private static int valueOfHex2(char first, char second) {
    return 16 * valueOfHex(first) + valueOfHex(second);
  }
}
