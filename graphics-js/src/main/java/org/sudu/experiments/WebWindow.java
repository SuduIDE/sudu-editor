package org.sudu.experiments;

import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.js.*;
import org.sudu.experiments.math.V2i;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.browser.Screen;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.function.BiFunction;

public class WebWindow implements org.sudu.experiments.Window {
  final AnimationFrameCallback frameCallback = this::onAnimationFrame;
  final Runnable repaint = this::repaint;
  final InputListeners inputListeners = new InputListeners(repaint);
  final HTMLElement canvasDiv;
  final HTMLCanvasElement mainCanvas;
  private JsInput eventHandler;
  private WebGraphics g;
  private boolean repaintRequested = true;
  private String currentCursor;
  private Scene scene;

  public WebWindow(BiFunction<SceneApi, String, Scene> factory, Runnable onWebGlError, String canvasDivId) {
    canvasDiv = HTMLDocument.current().getElementById(canvasDivId);
    V2i clientRect = JsHelper.elementSizeToPixelSize(canvasDiv);

//    JsHelper.addPreText("WebApp::WebApp size in pixels: = " + clientRect);

    mainCanvas = JsHelper.createMainCanvas(clientRect, null);
    canvasDiv.appendChild(mainCanvas);

    GLApi.Context gl = JsHelper.createContext(mainCanvas);

    if (gl != null) {
      init(gl, clientRect, factory);
    } else {
      onWebGlError.run();
    }
  }

  @SuppressWarnings("unused")
  public void dispose() {
    g.dispose();
    if (eventHandler != null) {
      eventHandler.dispose();
      eventHandler = null;
    }
  }

  private void init(GLApi.Context gl, V2i clientRect, BiFunction<SceneApi, String, Scene> sf) {
    eventHandler = new JsInput(mainCanvas, inputListeners);
    g = new WebGraphics(gl, repaint, clientRect);
    mainCanvas.focus();

    Window.current().addEventListener("resize", this::handleWindowResize);

    scene = sf.apply(api(), Window.current().getLocation().getHash());
    scene.onResize(clientRect);
    double t0 = timeNow();
    Debug.consoleInfo("time start: ", t0);
    onAnimationFrame(t0);
  }

  @Override
  public void setTitle(String title) {}

  private SceneApi api() {
    return new SceneApi(g, inputListeners, this);
  }

  private void requestNewFrame() {
    Window.requestAnimationFrame(frameCallback);
  }
  public void repaint() {
    repaintRequested = true;
  }

  private void onAnimationFrame(double timestamp) {
    if (scene.update(timestamp / 1000) || repaintRequested) {
      repaintRequested = false;
      scene.paint();
    }
    requestNewFrame();
  }

  private void handleWindowResize(Event evt) {
    V2i size = JsHelper.elementSizeToPixelSize(canvasDiv);
    Debug.consoleInfo("handleWindowResize new size: " + size);
    mainCanvas.setWidth(size.x);
    mainCanvas.setHeight(size.y);
    g.setClientRect(size);
    g.setViewPortToClientRect();
    scene.onResize(size);
    scene.paint();
  }

  public void setCursor(String cursor) {
    currentCursor = JsHelper.setCursor(cursor, currentCursor, mainCanvas);
  }

  @Override
  public V2i getClientRect() {
    return new V2i(g.clientRect);
  }

  @Override
  public V2i getScreenRect() {
    Screen screen = Window.current().getScreen();
    //todo: add Firefox <-> Chrome detection code
    //   firefox measures in DOM pixels
    //   chrome measures in Device pixels
    return new V2i(screen.getWidth(), screen.getHeight());
  }

  @Override
  public double timeNow() {
    return Performance.now() / 1000;
  }

  @Override
  public double devicePixelRatio() {
    return Window.current().getDevicePixelRatio();
  }

  @Override
  public Host getHost() {
    return Host.Chrome;
  }
}
