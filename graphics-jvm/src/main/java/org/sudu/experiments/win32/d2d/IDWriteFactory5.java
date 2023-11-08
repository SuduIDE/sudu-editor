package org.sudu.experiments.win32.d2d;

public class IDWriteFactory5 {

  // factoryType = DWRITE_FACTORY_TYPE_SHARED
  public static native long DWriteCreateFactory5(int[] hr);

  // returns IDWriteInMemoryFontFileLoader
  public static native long CreateInMemoryFontFileLoader(long _this, int[] hr);

  // returns IDWriteFontSetBuilder1
  public static native long CreateFontSetBuilder1(long _this, int[] hr);

  // returns IDWriteFontCollection1
  public static native long CreateFontCollectionFromFontSet(long _this, long pDWriteFontSet, int[] hr);

  // returns IDWriteFontCollection1
  public static native long GetSystemFontCollection(
      long _this,
      boolean includeDownloadableFonts,
      boolean checkForUpdates,
      int[] hr);

  // returns IDWriteTextFormat
  public static native long CreateTextFormat(
      long _this, char[] fontFamilyName, long pDWriteFontCollection,
      int fontWeight, int fontStyle, int fontStretch, float fontSize,
      char[] localeName, int[] hr);

  // textFormat = IDWriteTextFormat
  // returns IDWriteTextLayout
  public static native long CreateTextLayout(
      long _this, char[] text, int offset, int length,
      long textFormat, float maxWidth, float maxHeight, int[] hr);

  public static native int RegisterFontFileLoader(long _this, long pDWriteInMemoryFontFileLoader);
  public static native int UnregisterFontFileLoader(long _this, long pDWriteInMemoryFontFileLoader);

  // Creates a rendering parameters object with default settings for the primary monitor.
  // returns IDWriteRenderingParams
  public static native long CreateRenderingParams(long _this, int[] hr);

  // Creates a rendering parameters object with default settings for the specified monitor.
  // returns IDWriteRenderingParams
  public static native long CreateMonitorRenderingParams(long _this, long hMonitor, int[] hr);

  // Creates a rendering parameters object with the specified properties.
  // returns IDWriteRenderingParams
  // pixelGeometry one of D2d.DWRITE_PIXEL_GEOMETRY_*
  // renderingMode one of D2d.DWRITE_RENDERING_MODE_*
  public static native long CreateCustomRenderingParams(
      long _this,
      float gamma, float enhancedContrast, float clearTypeLevel,
      int pixelGeometry, int renderingMode,
      int[] hr
  );
}
