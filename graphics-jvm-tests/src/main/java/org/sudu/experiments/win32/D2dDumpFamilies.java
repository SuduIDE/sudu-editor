package org.sudu.experiments.win32;

import org.sudu.experiments.CString;
import org.sudu.experiments.FontLoader;
import org.sudu.experiments.Fonts;
import org.sudu.experiments.win32.d2d.*;

import java.io.IOException;
import java.util.Arrays;

public class D2dDumpFamilies {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();
    Win32.coInitialize();

    D2dFactory f = D2dFactory.create(FontLoader.JetBrainsMono.regular());

    dumpAllNames(f);

    testFind(f, Fonts.Consolas, Fonts.SegoeUI, Fonts.JetBrainsMono);

  }

  private static void dumpAllNames(D2dFactory f) {
    dump(f, "Jetbrains fonts",
        IDWriteFontCollection.getAllFontFamilies(f.getFontCollection(), f.hr));
    dump(f, "systemFontCollection",
        IDWriteFontCollection.getAllFontFamilies(f.getSystemFontCollection(), f.hr));
  }

  static void testFind(D2dFactory f, String ... typefaces) {
    long fontCollection = f.getFontCollection();
    long systemFontCollection = f.getSystemFontCollection();

    int[] familyIndexBadFont = new int[1];
    boolean findResultBadFont = IDWriteFontCollection.FindFamilyName(fontCollection,
        CString.toChar16CString("bad_font"), familyIndexBadFont, f.hr) != 0;
    System.out.println("findResultBadFont = " + findResultBadFont + ", familyIndexBadFont = " + familyIndexBadFont[0]);

    int[] familyIndex = new int[1];
    int[] familyIndexSys = new int[1];
    for (String typeface : typefaces) {
      char[] cString = CString.toChar16CString(typeface);
      boolean foundInCollection = IDWriteFontCollection.FindFamilyName(
          fontCollection, cString, familyIndex, f.hr) != 0;
      boolean foundInSystem = IDWriteFontCollection.FindFamilyName(
          systemFontCollection, cString, familyIndexSys, f.hr) != 0;

      if (foundInCollection) {
        System.out.println("foundInCollection " + typeface + ": familyIndex = " + familyIndex[0]);
      }
      if (foundInSystem) {
        System.out.println("foundInSystem " + typeface + ": familyIndex = " + familyIndexSys[0]);
      }
    }
  }

  static void dump(D2dFactory f, String title, long[] families) {
    StringBuilder s = new StringBuilder(title).append('\n');
    for (int i = 0; i < families.length; i++) {
      String fam = (families[i] != 0)
          ? IDWriteFontFamily.getFamilyName(families[i], f.hr)
          : "null family";
      s.append(fam);
      if (i + 1 < families.length) s.append(", ");
      if (s.length() > 70) {
        System.out.println(s);
        s = new StringBuilder();
      }
    }
    if (s.length() > 0) {
      System.out.println(s);
    }
  }

  static void dump2(D2dFactory f, long[] families) {
    for (long family : families) {
      if (family != 0) {
        long font = IDWriteFontFamily.GetFirstMatchingFont(family,
            D2d.DWRITE_FONT_WEIGHT_REGULAR,
            D2d.DWRITE_FONT_STRETCH_NORMAL,
            D2d.DWRITE_FONT_STYLE_NORMAL,
            f.hr);
        if (font != 0) {
          char[] metrics = IDWriteFont.newMetrics();
          if (IDWriteFont.GetMetrics(font, metrics) == 0) {
            System.out.println("metrics = " + Arrays.toString(metrics));
          }
        }
        System.out.println("font = " + font);
      } else {
        System.out.println("null family");
      }
    }
  }
}
