package org.sudu.experiments.win32;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.FontLoaderJvm;
import org.sudu.experiments.win32.d2d.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;

import static org.sudu.experiments.win32.d2d.D2d.*;

public class D2dFactory implements WglGraphics.CanvasFactory {

  long pD2D1Factory;
  long pWicFactory;
  long pDWriteFactory5;
  long pDWriteInMemoryFontFileLoader;
  long pDWriteFontSetBuilder1;
  long pDWriteFontCollection1Custom;
  long pDWriteFontCollection1System;

  final int[] hr = new int[1];
  final float[] textLayoutMetrics = IDWriteTextLayout.newMetrics();
  final Map<Key, FontDesk> fontsMap = new HashMap<>();

  public static D2dFactory create() {
    D2dFactory factory = new D2dFactory();
    if (!factory.init()) {
      throw new RuntimeException(
          "D2dCanvasFactory.init() failed: ".concat(factory.errorString()));
    }
    return factory;
  }

  public static D2dFactory create(FontLoaderJvm fontConfig) {
    D2dFactory f = D2dFactory.create();
    f.loadFontConfig(fontConfig);
    return f;
  }

  public boolean init() {
    return t(pD2D1Factory = ID2D1Factory.D2D1CreateFactory(ID2D1Factory.D2D1_FACTORY_TYPE_SINGLE_THREADED, hr))
        && t(pWicFactory = IWICImagingFactory.CoCreateInstance(hr))
        && t(pDWriteFactory5 = IDWriteFactory5.DWriteCreateFactory5(hr))
        && t(pDWriteInMemoryFontFileLoader = IDWriteFactory5.CreateInMemoryFontFileLoader(pDWriteFactory5, hr))
        && Win32.hr(hr[0] = IDWriteFactory5.RegisterFontFileLoader(pDWriteFactory5, pDWriteInMemoryFontFileLoader))
        && t(pDWriteFontSetBuilder1 = IDWriteFactory5.CreateFontSetBuilder1(pDWriteFactory5, hr))
        && t(pDWriteFontCollection1System = IDWriteFactory5.GetSystemFontCollection(
            pDWriteFactory5, false, false, hr));
  }

  public void dispose() {
    for (FontDesk font : fontsMap.values()) {
      D2dPlFont plFont = (D2dPlFont) font.platformFont;
      IUnknown.safeRelease(plFont.dWriteTextFormat);
    }
    fontsMap.clear();
    if (pDWriteFactory5 != 0 && pDWriteInMemoryFontFileLoader != 0) {
      IDWriteFactory5.UnregisterFontFileLoader(pDWriteFactory5, pDWriteInMemoryFontFileLoader);
    }
    pDWriteFontCollection1Custom = IUnknown.safeRelease(pDWriteFontCollection1Custom);
    pDWriteFontCollection1System = IUnknown.safeRelease(pDWriteFontCollection1System);
    pDWriteFontSetBuilder1 = IUnknown.safeRelease(pDWriteFontSetBuilder1);
    pDWriteInMemoryFontFileLoader = IUnknown.safeRelease(pDWriteInMemoryFontFileLoader);
    pDWriteFactory5 = IUnknown.safeRelease(pDWriteFactory5);
    pWicFactory = IUnknown.safeRelease(pWicFactory);
    pD2D1Factory = IUnknown.safeRelease(pD2D1Factory);
  }

  public void loadFontConfig(FontLoaderJvm config) {
    loadFontConfig(config, Const.double0);
  }

  public double[] loadFontConfig(FontLoaderJvm config, DoubleSupplier dt) {
    double loadResources = 0, loadToD2d = 0;
    for (String font : config.fonts) {
      byte[] fontData = config.loader.apply(font);
      loadResources += dt.getAsDouble();
      if (fontData != null) {
        boolean addFontFile = addFontFile(fontData);
        loadToD2d += dt.getAsDouble();
        if (!addFontFile) {
          System.err.println("Can not load font " + font + ", error = " + errorString());
          return null;
        }
      } else {
        System.err.println("Can not load (not present) font file " + font);
      }
    }
    return new double[] {loadResources, loadToD2d};
  }

  static boolean t(long x) { return x != 0; }

  public String errorString() { return D2d.errorToString(hr[0]); }

  public boolean addFontFile(byte[] data) {
    long pDWriteFontFile = IDWriteInMemoryFontFileLoader.CreateInMemoryFontFileReference(
        pDWriteInMemoryFontFileLoader, pDWriteFactory5, data, hr);
    if (pDWriteFontFile == 0) return false;
    hr[0] = IDWriteFontSetBuilder1.AddFontFile(pDWriteFontSetBuilder1, pDWriteFontFile);
    boolean ok = hr[0] >= 0;
    if (ok) pDWriteFontCollection1Custom = IUnknown.safeRelease(pDWriteFontCollection1Custom);
    IUnknown.Release(pDWriteFontFile);
    return ok;
  }

  // returns IDWriteFontCollection1
  public long getFontCollection() {
    if (pDWriteFontCollection1Custom == 0) {
      long fontSet = IDWriteFontSetBuilder1.CreateFontSet(pDWriteFontSetBuilder1, hr);
      if (fontSet != 0) {
        pDWriteFontCollection1Custom = IDWriteFactory5.CreateFontCollectionFromFontSet(pDWriteFactory5, fontSet, hr);
        IUnknown.release(fontSet);
      }
    }
    return pDWriteFontCollection1Custom;
  }

  // returns IDWriteFontCollection1
  public long getSystemFontCollection() {
    return pDWriteFontCollection1System;
  }

  public long textFormatJb(String family, float fontSize) {
    return textFormat(family, fontSize, DWRITE_FONT_WEIGHT_REGULAR, DWRITE_FONT_STYLE_NORMAL, getFontCollection());
  }

  // todo: collapse 3 native calls to single one
  public float measure(long textFormat, char[] text) {
    long layout = IDWriteFactory5.CreateTextLayout(pDWriteFactory5,
        text, 0, text.length, textFormat, 16384, 16384, hr);

    if (layout != 0) {
      int _hr = IDWriteTextLayout.GetMetrics(layout, textLayoutMetrics);
      if (_hr >= 0) {
        IUnknown.Release(layout);
        return textLayoutMetrics[IDWriteTextLayout.widthIncludingTrailingWhitespaceIdx];
      }
      hr[0] = _hr;
    }

    System.err.println("IDWriteFactory5.CreateTextLayout failed: " + errorString());
    IUnknown.safeRelease(layout);
    return 0;
  }

  public FontDesk getFont(String family, float size, int weight, int style) {
    Key k = new Key(family, size, weight, style);
    FontDesk desk = fontsMap.get(k);
    if (desk == null) {
      desk = createFontDesk(family, size, weight, style);
      Objects.requireNonNull(desk);
      fontsMap.put(k, desk);
    }
    return desk;
  }

  private FontDesk createFontDesk(String family, float size, int weight, int style) {
    FindRes fam = findFamily(family);
    long fontCollection = fam != null ? fam.dWriteFontCollection : pDWriteFontCollection1System;
    int d2dStyle = d2dStyle(style);
    long textFormat = textFormat(family, size, weight, d2dStyle, fontCollection);
    if (textFormat == 0) return null;
    D2dPlFont d2dPlFont = new D2dPlFont(textFormat);

    char[] metrics = IDWriteFont.newMetrics();

    if (fam != null) {
      long font = IDWriteFontFamily.GetFirstMatchingFont(fam.dWriteFontFamily,
          weight, DWRITE_FONT_STRETCH_NORMAL, d2dStyle, hr);
      if (font != 0) {
        hr[0] = IDWriteFont.GetMetrics(font, metrics);
        IUnknown.release(font);
      }
      if (font == 0 || hr[0] < 0) System.err.println("IDWriteFont.GetMetrics error: " + errorString());
      fam.dispose();
    }

    float ascent = metrics[0] != 0 ? IDWriteFont.ascent(metrics) * size : size * 14 / 16;
    float descent = metrics[0] != 0 ? IDWriteFont.descent(metrics) * size : size * 5 / 16;

    float spaceWidth = measure(textFormat, " \0".toCharArray());
    float WWidth     = measure(textFormat, "w\0".toCharArray());
    float dotWidth   = measure(textFormat, ".\0".toCharArray());

    return new FontDesk(family, size, weight, style,
       ascent, descent, spaceWidth, WWidth, dotWidth, d2dPlFont);
  }

  private FindRes findFamily(String family) {
    char[] cString = CString.toChar16CString(family);
    int[] familyIndex = new int[1];
    long fontCollection = getFontCollection();

    boolean found = IDWriteFontCollection.FindFamilyName(fontCollection, cString, familyIndex, hr) != 0;

    if (!found) {
      fontCollection = pDWriteFontCollection1System;
      found = IDWriteFontCollection.FindFamilyName(fontCollection, cString, familyIndex, hr) != 0;
    }

    if (found) {
      long dWriteFontFamily = IDWriteFontCollection.GetFontFamily(fontCollection, familyIndex[0], hr);
      if (dWriteFontFamily != 0) {
        return new FindRes(fontCollection, dWriteFontFamily);
      } else {
        System.err.println("ERROR: GetFontFamily error " + errorString());
      }
    } else {
      System.err.println("ERROR: Font family is not found " + family);
    }
    return null;
  }

  static final class FindRes {
    long dWriteFontCollection, dWriteFontFamily;

    FindRes(long collection, long dWriteFontFamily) {
      this.dWriteFontCollection = collection;
      this.dWriteFontFamily = dWriteFontFamily;
    }

    void dispose() {
      dWriteFontFamily = IUnknown.release(dWriteFontFamily);
    }
  }

  static int d2dStyle(int fontDeskStyle) {
    return switch (fontDeskStyle) {
      case 1 -> DWRITE_FONT_STYLE_OBLIQUE;
      case 2 -> DWRITE_FONT_STYLE_ITALIC;
      default -> DWRITE_FONT_STYLE_NORMAL;
    };
  }

  public long textFormat(String familyName, float size, int weight, int d2dStyle, long fontCollection) {
    long textFormat = IDWriteFactory5.CreateTextFormat(pDWriteFactory5,
        CString.toChar16CString(familyName),
        fontCollection,
        weight, d2dStyle,
        DWRITE_FONT_STRETCH_NORMAL,
        size, Locale.noLocale, hr);
    if (textFormat == 0 || hr[0] < 0) {
      System.err.println("D2dCanvasFactory::createTextFormat failed" +
          ", error = " + D2d.errorToString(hr[0]) + ", familyName = " + familyName);
    }
    return textFormat;
  }

  public long createBitmap(int w, int h) {
    return IWICImagingFactory.CreateBitmapPreRGBA(pWicFactory, w, h, hr);
  }

  public long createRenderTarget(long pWicBitmap) {
    return pWicBitmap == 0 ? 0
        : ID2D1Factory.CreateWicBitmapRenderTargetPre(pD2D1Factory, pWicBitmap, hr);
  }

  @Override
  public D2dCanvas create(int w, int h) {
    long pWicBitmap = createBitmap(w, h);
    long pD2D1RenderTarget = createRenderTarget(pWicBitmap);
    if (pWicBitmap == 0 || pD2D1RenderTarget == 0) throw new RuntimeException(
        "D2dCanvasFactory::createCanvas failed w=" + w + ", h=" + h + ", hr =" + D2d.errorToString(hr[0]));
    return new D2dCanvas(this, w, h, pWicBitmap, pD2D1RenderTarget);
  }

  record Key(String family, float size, int weight, int style) {}
}
