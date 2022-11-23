package org.sudu.experiments.win32;

import org.sudu.experiments.CString;
import org.sudu.experiments.Canvas;
import org.sudu.experiments.FontDesk;
import org.sudu.experiments.angle.AngleGL;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.win32.d2d.*;

public class D2dCanvas extends Canvas {
  private final D2dFactory factory;
  private FontDesk font;
  private long pWicBitmap, pD2D1RenderTarget;
  private long pBrush;

  // reference
  private long textFormatRef;

  D2dCanvas(D2dFactory f, int w, int h, long pWicBitmap, long pD2D1RenderTarget) {
    IUnknown.requireNonNull(pWicBitmap);
    factory = f;
    this.pWicBitmap = pWicBitmap;
    this.pD2D1RenderTarget = pD2D1RenderTarget;
    width = w;
    height = h;
    pBrush = ID2D1RenderTarget.CreateSolidColorBrush(pD2D1RenderTarget, 1, 1, 1, 1, f.hr);
    beginDraw();
  }

  private void beginDraw() {
    ID2D1RenderTarget.BeginDraw(pD2D1RenderTarget);
  }

  private void endDraw() {
    int endDrHR = ID2D1RenderTarget.EndDraw(pD2D1RenderTarget);
    if (endDrHR < 0) System.err.println("ID2D1RenderTarget.EndDraw error: " + D2d.errorToString(endDrHR));
  }

  public void texSubImage2D(int target, int level, int xOffset, int yOffset, int format, int type) {
    endDraw();
    long pWICBitmapLock = IWICBitmap.Lock(pWicBitmap,
        width, height, IWICBitmap.WICBitmapLockRead, factory.hr);
    if (pWICBitmapLock != 0) {
      int lockDataLength = width * height * 4;
      long pointer = IWICBitmapLock.GetDataPointer(pWICBitmapLock, lockDataLength, null);
      if (pointer != 0) {
        AngleGL.texSubImage2DPtr(target, level, xOffset, yOffset,
            width, height, format, type, pointer);
      }
      IUnknown.Release(pWICBitmapLock);
    } else {
      System.err.println("IWICBitmap.Lock failed: " + factory.errorString());
    }
    beginDraw();
  }

  @Override
  public void dispose() {
    endDraw();
    pBrush = IUnknown.release(pBrush);
    pD2D1RenderTarget = IUnknown.release(pD2D1RenderTarget);
    pWicBitmap = IUnknown.release(pWicBitmap);
    super.dispose();
  }

  @Override
  public void setFont(String font, float size, int weight, int style) {
    setFont(factory.getFont(font, size, weight, style));
  }

  @Override
  public void setFont(FontDesk f) {
    font = f;
    textFormatRef = ((D2dPlFont)f.platformFont).dWriteTextFormat;
  }

  @Override
  public void setTextAlign(int align) {
    // todo
  }

  @Override
  public Object platformFont(String font, float size) {
    return null;
  }

  @Override
  public V4f getFontMetrics() {
    return new V4f(font.fAscent, font.fDescent, font.WWidth, font.spaceWidth);
  }

  @Override
  public float measureText(String s) {
    if (textFormatRef == 0) return 0;
    return factory.measure(textFormatRef, CString.toChar16CString(s));
  }

  @Override
  public void drawText(String s, float x, float y) {
    if (textFormatRef == 0) return;
    char[] cString = CString.toChar16CString(s);
    float top = y - font.fAscent;
    ID2D1RenderTarget.DrawText(pD2D1RenderTarget,
        cString, 0, cString.length,
        textFormatRef,
        x, top, x + 16384, top + 16384, pBrush);
  }

  @Override
  public void setFillColor(int r, int g, int b) {
    float c = 1.f / 255;
    ID2D1SolidColorBrush.SetColor(pBrush, r * c, g * c, b * c, 1);
  }

  @Override
  public void clear() {
    ID2D1RenderTarget.Clear(pD2D1RenderTarget);
  }
}
