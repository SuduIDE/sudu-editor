package org.sudu.experiments.js;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Color;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class JsCanvas extends Canvas {
  public HTMLCanvasElement element;
  final Context2D c2d;
  private JSString jsFont;

  public JsCanvas(int width, int height, boolean cleartype) {
    super(cleartype);
    this.width = width;
    this.height = height;
    element = HTMLDocument.current().createElement("canvas").cast();
    element.setWidth(width);
    element.setHeight(height);

    c2d = (cleartype
        ? element.getContext("2d", canvasAttributesNoAlpha())
        : element.getContext("2d")).cast();
    // default fillStyle = #000000
    if (cleartype) c2d.setFillStyle("#FFFFFF");
  }

  @JSBody(script = "return {alpha: false};")
  static native JSObject canvasAttributesNoAlpha();

  @Override
  public void clear() {
    c2d.clearRect(0, 0, width, height);
  }

  @Override
  public void setTextAlign(int align) {
    switch (align) {
      case TextAlign.LEFT -> c2d.setTextAlign("left");
      case TextAlign.CENTER -> c2d.setTextAlign("center");
      case TextAlign.RIGHT -> c2d.setTextAlign("right");
    }
  }

  public void setFont(String font, float size, int weight, int style) {
    setJsFont(jsPlatformFont(font, size, weight, style));
  }

  public void setFont(FontDesk font) {
    setJsFont((JSString) JsHelper.directJavaToJs(font.platformFont));
  }

  private void setJsFont(JSString font) {
    if (jsFont != font) {
      c2d.setFont(jsFont = font);
    }
  }

  public static JSString jsPlatformFont(String font, float size, int weight, int style) {
    return fontJSString(font, size, weight,
        switch (style) {
          case FontDesk.STYLE_OBLIQUE -> oblique();
          case FontDesk.STYLE_ITALIC -> italic();
          default -> normal();
        });
  }

  @JSBody(params = {"a", "b"}, script = "return a == b;")
  static native boolean jsStrEquals(JSString a, JSString b);

  @JSBody(script = "return 'italic '")
  static native JSString italic();
  @JSBody(script = "return 'oblique '")
  static native JSString oblique();
  @JSBody(script = "return ''")
  static native JSString normal();

  @JSBody(params = {"font", "size", "weight", "style"}, script = "return style + weight + ' ' + size + 'px ' + font;")
  static native JSString fontJSString(String font, float size, int weight, JSString style);

  public void drawText(String s, float x, float y) {
    c2d.fillText(s, x, y);
  }

  @Override
  public void setTopMode(boolean top) {
    if (top) {
      c2d.setTextBaseline("top");
    } else {
      c2d.setTextBaseline("alphabetic");
    }
  }

  @Override
  public float measureText(String s) {
    JSString jsString = JSString.valueOf(s);
    return (float) c2d.measureTextD(jsString).getWidth();
  }

  public FontDesk createFontDesk(String family, float size, int weight, int style) {
    JSString platformFont = jsPlatformFont(family, size, weight, style);
    setJsFont(platformFont);
    JsCanvas.TextMetrics metrics = c2d.measureTextD("W");
    float ascent = (float) metrics.getFontBoundingBoxAscent();
    float descent = (float) metrics.getFontBoundingBoxDescent();
    float wWidth = (float) metrics.getWidth();
    float spaceWidth = measureText(" ");
    float dotWidth = measureText(".");
    return new FontDesk(family, size, weight, style,
        ascent, descent, spaceWidth, wWidth, dotWidth,
        JsHelper.directJsToJava(platformFont));
  }

  @Override
  public void setFillColor(int r, int g, int b) {
    c2d.setFillStyle(Color.Cvt.toHexString(r, g, b));
  }

  public void drawSvgSample() {
    c2d.setFillStyle("white");
    c2d.setStrokeStyle("white");
    c2d.setLineWidth(10);
    c2d.strokeRect(75, 140, 150, 110);
    c2d.fillRect(130, 190, 40, 60);
    c2d.beginPath();
    c2d.moveTo(50, 140);
    c2d.lineTo(150, 60);
    c2d.lineTo(250, 140);
    c2d.closePath();
    c2d.stroke();
  }

  public String getFont() {
    return c2d.getFont();
  }

  interface TextMetrics extends JSObject {
    @JSProperty double getWidth();

    // Actual metrics depend on the printed characters shape and uninteresting
    @JSProperty double getActualBoundingBoxLeft();
    @JSProperty double getActualBoundingBoxRight();
    @JSProperty double getActualBoundingBoxAscent();
    @JSProperty double getActualBoundingBoxDescent();
    @JSProperty double getFontBoundingBoxAscent();
    @JSProperty double getFontBoundingBoxDescent();
  }

  interface Context2D extends CanvasRenderingContext2D {
    @JSProperty void setFont(JSString font);

    @JSMethod("measureText")
    TextMetrics measureTextD(String text);

    @JSMethod("measureText")
    TextMetrics measureTextD(JSString text);
  }

  public static boolean checkFontMetricsAPI() {
    HTMLCanvasElement canvas = JsHelper.createCanvas();
    Context2D c2d = canvas.getContext("2d").cast();
    TextMetrics textMetrics = c2d.measureTextD("");
    return JSObjects.hasProperty(textMetrics, "fontBoundingBoxAscent")
        && JSObjects.hasProperty(textMetrics, "fontBoundingBoxDescent");
  }
}
