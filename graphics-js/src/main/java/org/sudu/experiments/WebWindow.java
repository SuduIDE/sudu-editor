package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.math.Numbers;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.function.Consumer;
import java.util.function.Function;

public class WebWindow implements Window {

  final AnimationFrameCallback frameCallback = this::onAnimationFrame;
  final Runnable repaint = this::repaint;
  final HTMLElement canvasDiv;
  final HTMLCanvasElement mainCanvas;
  final ResizeObserver observer = ResizeObserver.create(this::onSizeObserved);
  final EventListener<Event> handleWindowResize = this::handleWindowResize;

  private JsInput eventHandler;
  private WebGraphics g;
  private boolean repaintRequested = true;
  private int animationFrameRequest;
  private String currentCursor;
  private Scene scene;

  // workers and jobs
  private final WorkersPool workers;

  public WebWindow(
      Function<SceneApi, Scene> factory,
      Runnable onWebGlError,
      String canvasDivId,
      JsArray<WorkerContext> workers
  ) {
    this.workers = new WorkersPool(workers);

//    JsHelper.consoleInfo("starting web window on " + canvasDivId);
    canvasDiv = HTMLDocument.current().getElementById(canvasDivId);
    mainCanvas = JsHelper.createMainCanvas(null);
    canvasDiv.appendChild(mainCanvas);

    GLApi.Context gl = JsHelper.createWebglContext(mainCanvas);

    if (gl != null) {
      init(gl, factory);
    } else {
      onWebGlError.run();
    }
  }

  public void focus() {
    mainCanvas.focus();
  }

  @Override
  public boolean hasFocus() {
    return mainCanvas == HTMLDocument.current().getActiveElement();
  }

  void onSizeObserved(JsArrayReader<ResizeObserver.ResizeObserverEntry> entries, ResizeObserver o) {
//    JsHelper.consoleInfo("onSizeObserved: entries.length = ", entries.getLength());
    for (int i = 0, n = entries.getLength(); i < n; i++) {
      var entry = entries.get(i);
      if (entry.getTarget() == mainCanvas) {
        if (JSObjects.hasProperty(entry, entry.devicePixelContentBoxSize)) {
          if (entry.getDevicePixelContentBoxSize().getLength() == 1) {
            var size = entry.getDevicePixelContentBoxSize().get(0);
            onCanvasSizeChanged((int) size.getInlineSize(), (int) size.getBlockSize());
          }
        } else {
          var domRect = entry.getContentRect();
          double ratio = devicePixelRatio();
          int width = Numbers.iRnd(domRect.getWidth() * ratio);
          int height = Numbers.iRnd(domRect.getHeight() * ratio);
          onCanvasSizeChanged(width, height);
        }
      }
    }
  }

  public Scene scene() {
    return scene;
  }

  @SuppressWarnings("unused")
  public void dispose() {
    if (animationFrameRequest != 0) {
      JsWindow.cancelAnimationFrame(animationFrameRequest);
      animationFrameRequest = 0;
    }
    JsWindow.current().removeEventListener("resize", handleWindowResize);

    observer.disconnect();
    g.dispose();
    if (eventHandler != null) {
      eventHandler.dispose();
      eventHandler = null;
    }
    workers.terminateAll();
  }

  private void init(GLApi.Context gl, Function<SceneApi, Scene> sf) {
    eventHandler = new JsInput(mainCanvas, repaint);
    g = new WebGraphics(gl, repaint);
    observer.observePixelsOrDefault(mainCanvas);

    JsWindow.current().addEventListener("resize", handleWindowResize);

    scene = sf.apply(api());
//    JsHelper.consoleInfo("time start: ", timeNow());
    requestNewFrame();
  }

  @Override
  public void setTitle(String title) {
    HTMLDocument.current().setTitle(title);
  }

  private SceneApi api() {
    return new SceneApi(g, eventHandler.listeners, this);
  }

  private void requestNewFrame() {
    animationFrameRequest = JsWindow.requestAnimationFrame(frameCallback);
  }
  public void repaint() {
    repaintRequested = true;
  }

  private void onAnimationFrame(double timestamp) {
    if (scene.update(timestamp / 1000) || repaintRequested) {
      if (g.clientRect.x * g.clientRect.y != 0) {
        repaintRequested = false;
        scene.paint();
      }
    }
    requestNewFrame();
  }

  @JSBody(
      params = {"style", "w", "h"},
      script = "style.width = w + 'px'; style.height = h + 'px';")
  static native void setStyleWHPx(CSSStyleDeclaration style, double w, double h);

  private void onCanvasSizeChanged(int inlineSize, int blockSize) {
    if (1 < 0) {
      JsHelper.consoleInfo("  onCanvasSizeChanged: ", JsHelper.WithId.get(canvasDiv));
      JsHelper.consoleInfo("    inlineSize =  ", inlineSize);
      JsHelper.consoleInfo("    blockSize =  ", blockSize);
    }
    eventHandler.setClientRect(inlineSize, blockSize);

    mainCanvas.setWidth(inlineSize);
    mainCanvas.setHeight(blockSize);
//    double ratio = Window.current().getDevicePixelRatio();
//    double widthPx = (1. / 32. + inlineSize) / ratio;
//    double heightPx = (1. / 32. + blockSize) / ratio;
//    setStyleWHPx(mainCanvas.getStyle(), widthPx, heightPx);
    g.setViewPortAndClientRect(inlineSize, blockSize);
    scene.onResize(g.clientRect, devicePixelRatio());
    scene.paint();
  }

  private void handleWindowResize(Event evt) {
    if (1 < 0) {
      JsHelper.consoleInfo("handleWindowResize: ", JsHelper.WithId.get(canvasDiv));
      JsHelper.consoleInfo("  devicePixelRatio  = ", devicePixelRatio());
    }

    scene.onResize(g.clientRect, devicePixelRatio());
    scene.paint();
  }

  public void setCursor(String cursor) {
    currentCursor = JsHelper.setCursor(cursor, currentCursor, mainCanvas);
  }

  @Override
  public double timeNow() {
    return Performance.now() / 1000;
  }

  public float devicePixelRatio() {
    return (float) JsWindow.current().getDevicePixelRatio();
  }

  @Override
  public Host getHost() {
    return Host.Chrome;
  }

  @Override
  public void showDirectoryPicker(Consumer<FileHandle> onResult) {
    JsFileDialog.showDirectoryPicker(onResult);
  }

  @Override
  public void showOpenFilePicker(Consumer<FileHandle> onResult) {
    JsFileDialog.showOpenFilePicker(onResult);
  }

  @Override
  public void sendToWorker(Consumer<Object[]> handler, String method, Object... args) {
    workers.sendToWorker(handler, method, args);
  }

  @Override
  public void sendToWorker(Consumer<Object[]> handler, int ch, String method, Object... args) {
    workers.sendToWorker(handler, ch, method, args);
  }

  @Override
  public void readClipboardText(Consumer<String> success, Consumer<Throwable> onError) {
    JsClipboard.get().readText().then(
            str -> success.accept(str.stringValue()), onError(onError));
  }

  @Override
  public void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError) {
    JsClipboard.get()
        .writeText(TextDecoder.decodeUTF16(text.toCharArray()))
        .then(v -> success.run(), onError(onError));
  }

  @Override
  public boolean isReadClipboardTextSupported() {
    return JsClipboard.isReadTextSupported();
  }

  static JsFunctions.Consumer<JSError> onError(Consumer<Throwable> onError) {
    return jsError -> onError.accept(new RuntimeException(jsError.getMessage()));
  }

  @Override
  public void runLater(Runnable command) {
    JsWindow.setTimeout(command::run, 0);
  }
}
