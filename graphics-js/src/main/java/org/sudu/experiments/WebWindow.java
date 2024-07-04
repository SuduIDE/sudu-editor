package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Node;

import java.util.function.Consumer;
import java.util.function.Function;

public class WebWindow implements Window {

  final AnimationFrameCallback frameCallback = this::onAnimationFrame;
  final Runnable repaint = this::repaint;
  final HTMLCanvasElement mainCanvas;
  final ResizeObserver observer = ResizeObserver.create(this::onSizeObserved);
  final EventListener<Event> handleWindowResize = this::handleWindowResize;

  private JSString canvasDivId;
  private JsInput eventHandler;
  private WebGraphics g;
  private boolean repaintRequested = true;
  private int animationFrameRequest;
  private String currentCursor;
  private Scene scene;

  // workers and jobs
  private final WebWorkersPool workers;

  public WebWindow(
      Function<SceneApi, Scene> factory,
      Runnable onWebGlError,
      String canvasDivId,
      JsArray<WebWorkerContext> workers
  ) {
    this(factory, onWebGlError, JSString.valueOf(canvasDivId), workers);
  }

  public WebWindow(
      Function<SceneApi, Scene> factory,
      Runnable onWebGlError,
      JSString canvasDivId,
      JsArray<WebWorkerContext> workers
  ) {
    this(canvasDivId, workers);
    if (!init(factory))
      onWebGlError.run();
  }

  // this ctor requires init after call
  public WebWindow(JSString canvasDivId, JsArray<WebWorkerContext> workers) {
    this.canvasDivId = canvasDivId;
    this.workers = new WebWorkersPool(workers);

//    JsHelper.consoleInfo("starting web window on " + canvasDivId);
    mainCanvas = JsHelper.createMainCanvas(null);
    GLApi.Context gl = JsHelper.createWebglContext(mainCanvas);

    if (gl != null) {
      connectToDom(canvasDivId);
      eventHandler = new JsInput(mainCanvas, repaint);
      g = new WebGraphics(gl, repaint);
      observer.observePixelsOrDefault(mainCanvas);

      JsWindow.current().addEventListener("resize", handleWindowResize);
    }
  }

  public JSString canvasDivId() {
    return canvasDivId;
  }

  public void disconnectFromDom() {
    Node parentNode = mainCanvas.getParentNode();
    if (parentNode != null) {
      parentNode.removeChild(mainCanvas);
      g.clientRect.set(0, 0);
      JsWindow.cancelAnimationFrame(animationFrameRequest);
    } else {
      System.err.println("disconnectFromDom: called on already disconnected");
    }
  }

  public void connectToDom(JSString containerId) {
    Node parentNode = mainCanvas.getParentNode();
    if (parentNode == null) {
      if (JsHelper.jsIf(containerId)) {
        this.canvasDivId = containerId;
      }
      var canvasDiv = canvasDiv();
      if (canvasDiv != null) {
        canvasDiv.appendChild(mainCanvas);
        requestNewFrame();
      }
    } else {
      System.err.println("connectToDom: called on already connected");
    }
  }

  private HTMLElement canvasDiv() {
    return HTMLDocument.current().getElementById(canvasDivId);
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

  public boolean init(Function<SceneApi, Scene> sf) {
    if (g != null) {
      scene = sf.apply(api());
//    JsHelper.consoleInfo("time start: ", timeNow());
      requestNewFrame();
    }
    return g != null;
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
      JsHelper.consoleInfo("  onCanvasSizeChanged: ", canvasDivId);
      JsHelper.consoleInfo("    inlineSize =  ", inlineSize);
      JsHelper.consoleInfo("    blockSize =  ", blockSize);
    }
    eventHandler.setClientRect(inlineSize, blockSize);

    boolean visible = inlineSize != 0 && blockSize != 0;
    if (visible) {
      mainCanvas.setWidth(inlineSize);
      mainCanvas.setHeight(blockSize);
    }
    g.setViewPortAndClientRect(inlineSize, blockSize);
    scene.onResize(g.clientRect, devicePixelRatio());
    if (visible) {
      scene.paint();
    }
  }

  private void handleWindowResize(Event evt) {
    if (1 < 0) {
      JsHelper.consoleInfo("handleWindowResize: ", canvasDivId);
      JsHelper.consoleInfo("  devicePixelRatio  = ", devicePixelRatio());
    }

    scene.onResize(g.clientRect, devicePixelRatio());
    if (g.clientRect.x * g.clientRect.y != 0) {
      scene.paint();
    }
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
  public void showDirectoryPicker(Consumer<DirectoryHandle> onResult) {
    JsFileDialog.showDirectoryPicker(onResult);
  }

  @Override
  public void showOpenFilePicker(Consumer<FileHandle> onResult) {
    JsFileDialog.showOpenFilePicker(onResult);
  }

  @Override
  public WorkerJobExecutor worker() {
    return workers;
  }

  @Override
  public void readClipboardText(Consumer<String> success, Consumer<Throwable> onError) {
    if (JsClipboard.hasClipboard()) {
      JsClipboard.get()
          .readText()
          .then(str -> success.accept(str.stringValue()), onError(onError));
    } else {
      onError.accept(new RuntimeException(JsClipboard.noClipboardDefined()));
    }
  }

  @Override
  public void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError) {
    if (JsClipboard.hasClipboard()) {
      JsClipboard.get()
          .writeText(TextDecoder.decodeUTF16(text.toCharArray()))
          .then(v -> success.run(), onError(onError));
    } else {
      onError.accept(new RuntimeException(JsClipboard.noClipboardDefined()));
    }
  }

  @Override
  public boolean isReadClipboardTextSupported() {
    return JsClipboard.isReadTextSupported();
  }

  @Override
  public boolean isClipboardSupported() {
    return JsClipboard.hasClipboard();
  }

  static JsFunctions.Consumer<JSError> onError(Consumer<Throwable> onError) {
    return jsError -> onError.accept(new RuntimeException(jsError.getMessage()));
  }

  @Override
  public void runLater(Runnable command) {
    JsWindow.setTimeout(command::run, 0);
  }

  public boolean addChild(String title, Function<SceneApi, Scene> sf) {
    Debug.consoleInfo("Window.addChild is not for web");
    return false;
  }
}
