package org.sudu.experiments.win32;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.fonts.JetBrainsMono;
import org.sudu.experiments.win32.d2d.*;

import java.util.Arrays;

import static org.sudu.experiments.win32.d2d.D2d.*;

public class D2dTest {
  public static final int FONT_SIZE = 16;
  static int[] hr = new int[1];

  public static void main(String[] args) {
    Helper.loadDlls();
    Win32.coInitialize();

    long pD2D1Factory = ID2D1Factory.D2D1CreateFactory(ID2D1Factory.D2D1_FACTORY_TYPE_SINGLE_THREADED, hr);
    HR(pD2D1Factory, "D2D1CreateFactory");
    System.out.println("pD2D1Factory = 0x" + Long.toHexString(pD2D1Factory));

    long pDWriteFactory5 = IDWriteFactory5.DWriteCreateFactory5(hr);
    HR(pDWriteFactory5, "DWriteCreateFactory5");

    long pWicFactory = IWICImagingFactory.CoCreateInstance(hr);
    HR(pWicFactory, "CoCreateInstanceWICImagingFactory");

    long pWicBitmap = IWICImagingFactory.CreateBitmapPreRGBA(pWicFactory, 100, 100, hr);
    HR(pWicBitmap, "IWICImagingFactory_CreateBitmap32bppPRGBA");
    System.out.println("pWicBitmap = 0x" + Long.toHexString(pWicBitmap));

    long pWicRT_pre = ID2D1Factory.CreateWicBitmapRenderTargetPre(pD2D1Factory, pWicBitmap, hr);
    HR(pWicRT_pre, "ID2D1Factory_CreateWicBitmapRenderTarget D2D1_ALPHA_MODE_PREMULTIPLIED");
    System.out.println("pWicRT_pre = 0x" + Long.toHexString(pWicRT_pre));

    long pBrush = ID2D1RenderTarget.CreateSolidColorBrush(pWicRT_pre, 1, 1, 1, 1, hr);
    HR(pBrush, "ID2D1RenderTarget_CreateSolidColorBrush");
    System.out.println("pBrush = 0x" + Long.toHexString(pBrush));

    ID2D1RenderTarget.BeginDraw(pWicRT_pre);
    ID2D1RenderTarget.Clear(pWicRT_pre);
    int hrEndDraw = ID2D1RenderTarget.EndDraw(pWicRT_pre);
    HR(hrEndDraw, "ID2D1RenderTarget_EndDraw");

    ID2D1RenderTarget.Clear(pWicRT_pre);
    ID2D1RenderTarget.BeginDraw(pWicRT_pre);
    hrEndDraw = ID2D1RenderTarget.EndDraw(pWicRT_pre);
    try {
      HR(hrEndDraw, "ID2D1RenderTarget_EndDraw clear->begin->end");
    } catch (RuntimeException e) {
      System.out.println("e.getMessage() = " + e.getMessage());
    }

    release(pBrush);
    release(pWicRT_pre);

    if (true) {
      long pWicRT_unk = ID2D1Factory.CreateWicBitmapRenderTarget(pD2D1Factory, pWicBitmap,
          DXGI_FORMAT_R8G8B8A8_UNORM, D2D1_ALPHA_MODE_UNKNOWN, hr);
      HR(pWicRT_unk, "ID2D1Factory_CreateWicBitmapRenderTarget D2D1_ALPHA_MODE_UNKNOWN");
      System.out.println("pWicRT_unk = 0x" + Long.toHexString(pWicRT_unk));
      release(pWicRT_unk);
    }

    if (false) { // D2D1_ALPHA_MODE_IGNORE does not work
      long pWicRT_ign = ID2D1Factory.CreateWicBitmapRenderTarget(pD2D1Factory, pWicBitmap,
          DXGI_FORMAT_R8G8B8A8_UNORM, D2D1_ALPHA_MODE_IGNORE, hr);
      HR(pWicRT_ign, "ID2D1Factory_CreateWicBitmapRenderTarget D2D1_ALPHA_MODE_IGNORE");

      System.out.println("pWicRT_ign = 0x" + Long.toHexString(pWicRT_ign));
      release(pWicRT_ign);
    }

    if (false) { // D2D1_ALPHA_MODE_STRAIGHT does not work
      long pWicRT_str = ID2D1Factory.CreateWicBitmapRenderTarget(pD2D1Factory, pWicBitmap,
          DXGI_FORMAT_R8G8B8A8_UNORM, D2D1_ALPHA_MODE_STRAIGHT, hr);
      HR(pWicRT_str, "ID2D1Factory_CreateWicBitmapRenderTarget D2D1_ALPHA_MODE_STRAIGHT");
      System.out.println("pWicRT_str = 0x" + Long.toHexString(pWicRT_str));
      release(pWicRT_str);
    }

    long pDWriteInMemoryFontFileLoader = IDWriteFactory5.CreateInMemoryFontFileLoader(pDWriteFactory5, hr);
    HR(pDWriteInMemoryFontFileLoader, "IDWriteFactory5_CreateInMemoryFontFileLoader");

    int hr1 = IDWriteFactory5.RegisterFontFileLoader(pDWriteFactory5, pDWriteInMemoryFontFileLoader);
    HR(hr1, "IDWriteFactory5_RegisterFontFileLoader");

    byte[] font = ResourceLoader.load(JetBrainsMono.Regular, JetBrainsMono.regular());
    System.out.println("font = byte[" + font.length + "] "  + font);

    long inMemoryFont = IDWriteInMemoryFontFileLoader.CreateInMemoryFontFileReference(
        pDWriteInMemoryFontFileLoader, pDWriteFactory5, font, hr);
    HR(inMemoryFont,
        "IDWriteInMemoryFontFileLoader_CreateInMemoryFontFileReference " + JetBrainsMono.Regular);

    long pDWriteFontSetBuilder1 = IDWriteFactory5.CreateFontSetBuilder1(pDWriteFactory5, hr);
    HR(pDWriteFontSetBuilder1, "IDWriteFactory5_CreateFontSetBuilder1");

    int hr2 = IDWriteFontSetBuilder1.AddFontFile(pDWriteFontSetBuilder1, inMemoryFont);
    HR(hr2, "IDWriteFontSetBuilder1_AddFontFile");

    long pFontSet = IDWriteFontSetBuilder1.CreateFontSet(pDWriteFontSetBuilder1, hr);
    HR(pFontSet, "IDWriteFontSetBuilder1_CreateFontSet");
    System.out.println("pFontSet = 0x" + Long.toHexString(pFontSet));

    long pDWriteFontCollection1 = IDWriteFactory5.CreateFontCollectionFromFontSet(pDWriteFactory5, pFontSet, hr);
    HR(pDWriteFontCollection1, "IDWriteFactory5_CreateFontCollectionFromFontSet");
    System.out.println("pDWriteFontCollection1 = 0x" + Long.toHexString(pDWriteFontCollection1));

    pFontSet = IUnknown.release(pFontSet);

    char[] consolas = CString.toChar16CString(Fonts.Consolas);
    char[] jetBrainsMono = CString.toChar16CString(Fonts.JetBrainsMono);
    char[] system = CString.toChar16CString("");

    long textFormatConsolas = textFormat(pDWriteFactory5, pDWriteFontCollection1, consolas);
    long textFormatJbMono = textFormat(pDWriteFactory5, pDWriteFontCollection1, jetBrainsMono);
    long textFormatSystem = textFormat(pDWriteFactory5, pDWriteFontCollection1, system);
    System.out.println("textFormatConsolas = " + Long.toHexString(textFormatConsolas));
    System.out.println("textFormatJbMono = " + Long.toHexString(textFormatJbMono));
    System.out.println("textFormatSystem = " + Long.toHexString(textFormatSystem));

    textLayoutAndMeasureTest(pDWriteFactory5, textFormatConsolas, textFormatSystem, textFormatJbMono);

    String fontFamilyNameConsolas = IDWriteTextFormat.getFontFamilyName(textFormatConsolas);
    System.out.println("IDWriteTextFormat.getFontFamilyName(textFormatConsolas) = " + fontFamilyNameConsolas);

    String fontFamilyNameJetBrainsMono = IDWriteTextFormat.getFontFamilyName(textFormatJbMono);
    System.out.println("IDWriteTextFormat.getFontFamilyName(jetBrainsMono) = " + fontFamilyNameJetBrainsMono);

    System.out.println("IDWriteTextFormat.getFontFamilyName(textFormatSystem) = " +
        IDWriteTextFormat.getFontFamilyName(textFormatSystem));

    int hr3 = IDWriteFactory5.UnregisterFontFileLoader(pDWriteFactory5, pDWriteInMemoryFontFileLoader);
    HR(hr3, "IDWriteFactory5_UnregisterFontFileLoader");

    System.out.println("pDWriteFactory5 = 0x" + Long.toHexString(pDWriteFactory5));
    System.out.println("pWicFactory = 0x" + Long.toHexString(pWicFactory));
    System.out.println("pDWriteInMemoryFontFileLoader = 0x" + Long.toHexString(pDWriteInMemoryFontFileLoader));
    System.out.println("pDWriteFontSetBuilder1 = 0x" + Long.toHexString(pDWriteFontSetBuilder1));

    release(inMemoryFont);
    release(pWicBitmap);

    release(textFormatConsolas);
    release(textFormatJbMono);
    release(pDWriteFontCollection1);
    release(pDWriteFontSetBuilder1);
    release(pDWriteInMemoryFontFileLoader);
    release(pDWriteFactory5);
    release(pD2D1Factory);
    release(pWicFactory);
  }

  static void textLayoutAndMeasureTest(long pDWriteFactory5, long ... textFormats) {
    String this_is_a_text = "This is a text";
    char[] text = this_is_a_text.toCharArray();

    float[] newMetrics = IDWriteTextLayout.newMetrics();
    float[] smallBuffer = new float[newMetrics.length - 1];

    for (int i = 0; i < textFormats.length; i++) {
      float[] metrics = IDWriteTextLayout.newMetrics();
      long layout = textLayout(pDWriteFactory5, textFormats[i], text);
      System.out.println("textLayout[" + i + "] = " + Long.toHexString(layout) +
          ", format.font = " + IDWriteTextFormat.getFontFamilyName(textFormats[i]));
      int hr1 = IDWriteTextLayout.GetMetrics(layout, metrics);
      HR(hr1, "IDWriteTextLayout.GetMetrics");
      System.out.println("metrics[" + i + "] = " + Arrays.toString(
          Arrays.copyOf(metrics, metrics.length - 2)));

      int hrFailed = IDWriteTextLayout.GetMetrics(layout, smallBuffer);
      if (hrFailed != Win32.E_INVALIDARG) throw new RuntimeException(
          "GetMetrics must return E_INVALIDARG with small buffer");

      IUnknown.Release(layout);
    }
  }

  static long textLayout(long pDWriteFactory5, long textFormat, char[] text) {
    long textLayout = IDWriteFactory5.CreateTextLayout(pDWriteFactory5,
        text, 0, text.length, textFormat, 1024, 1024, hr);
    HR(textLayout, "IDWriteFactory5.CreateTextLayout");
    return textLayout;
  }

  static long textFormat(long pDWriteFactory5, long pDWriteFontCollection1, char[] font) {
    long textFormat = IDWriteFactory5.CreateTextFormat(
        pDWriteFactory5, font, pDWriteFontCollection1,
        DWRITE_FONT_WEIGHT_REGULAR,
        DWRITE_FONT_STYLE_NORMAL,
        DWRITE_FONT_STRETCH_NORMAL,
        FONT_SIZE, Locale.en_us, hr);
    HR(textFormat, "IDWriteFactory5_CreateTextFormat");
    return textFormat;
  }

  static void release(long p) {
    IUnknown.Release(p);
  }

  static void HR(int errorCode, String title) {
    if (errorCode < 0) throw new RuntimeException(title + ": " + errorToString(errorCode));
  }

  static void HR(long result, String title) {
    if (result == 0) HR(hr[0], title);
  }

}
