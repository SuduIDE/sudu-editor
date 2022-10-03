  package org.sudu.experiments.js;

  import org.sudu.experiments.Canvas;
  import org.sudu.experiments.FontDesk;
  import org.sudu.experiments.math.Color;
  import org.sudu.experiments.math.V4f;
  import org.teavm.jso.JSBody;
  import org.teavm.jso.JSMethod;
  import org.teavm.jso.JSObject;
  import org.teavm.jso.JSProperty;
  import org.teavm.jso.canvas.CanvasRenderingContext2D;
  import org.teavm.jso.core.JSObjects;
  import org.teavm.jso.core.JSString;
  import org.teavm.jso.dom.html.HTMLCanvasElement;
  import org.teavm.jso.dom.html.HTMLDocument;

public class JsCanvas extends Canvas {
  public HTMLCanvasElement element;
  public Context2D c2d;
  private JSString fontSet;

  public JsCanvas(int width, int height) {
    this.width = width;
    this.height = height;
    element = HTMLDocument.current().createElement("canvas").cast();
    element.setWidth(width);
    element.setHeight(height);

    c2d = element.getContext("2d").cast();
  }

  @Override
  public void clear() {
    c2d.clearRect(0, 0, width, height);
  }

  public void debugAddToDocument() {
    HTMLDocument.current().getElementById("panelDiv").appendChild(element);
  }

  public void setFont(int size, String font) {
    doSetFont((JSString) platformFont(font, size));
  }

  public void setFont(FontDesk font) {
    doSetFont((JSString) font.platformFont);
  }

  private void doSetFont(JSString font) {
    if (!jsStrEquals(fontSet, font)) {
      c2d.setFont(fontSet = font);
    }
  }

  @Override
  public Object platformFont(String font, int size) {
    return fontJSString(font, size);
  }

  @JSBody(params = {"a", "b"}, script = "return a == b;")
  static native boolean jsStrEquals(JSString a, JSString b);
  @JSBody(params = {"font", "size"}, script = "return size + 'px ' + font;")
  static native JSString fontJSString(String font, int size);
  @JSBody(params = {"font", "size", "weight"}, script = "return weight + ' ' + size + 'px ' + font;")
  static native JSString fontJSString(String font, int size, int weight);

  public void drawText(String s, float x, float y) {
    c2d.fillText(s, x, y);
  }

  @Override
  public float measureText(String s) {
    JSString jsString = JSString.valueOf(s);
    return (float) c2d.measureTextD(jsString).getWidth();
  }

  @Override
  public V4f getFontMetrics() {
    TextMetrics metrics = c2d.measureTextD("W");
    float ascent = (float) metrics.getFontBoundingBoxAscent();
    float descent = (float) metrics.getFontBoundingBoxDescent();
    float WCharWidth = (float) metrics.getWidth();
    float spaceWidth = (float) c2d.measureTextD(" ").getWidth();
    return new V4f(ascent, descent, WCharWidth, spaceWidth);
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
