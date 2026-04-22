package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.core.JSBoolean;
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

  static final boolean debug = false;

  final AnimationFrameCallback frameCallback = this::onAnimationFrame;
  final Runnable repaint = this::repaint;
  final HTMLCanvasElement mainCanvas;
  final CanvasRenderingContext2D draw2d;
  final ResizeObserver observer = ResizeObserver.create(this::onSizeObserved);
  final EventListener<Event> handleWindowResize = this::handleWindowResize;

  private V2i clientRect = null;
  private JSString canvasDivId;
  private JsInput eventHandler;
  private WebGraphics g;
  private boolean repaintRequested = true;
  private int animationFrameRequest;
  private String currentCursor;
  private Scene scene;

  // workers and jobs
  private final WebWorkersPool workers;
  private final boolean ownsWorkers;

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
    this(canvasDivId, new WebWorkersPool(workers), true);
    if (!init(factory))
      onWebGlError.run();
  }

  // this ctor requires init after call
  public WebWindow(JSString canvasDivId, JsArray<WebWorkerContext> workers) {
    this(canvasDivId, new WebWorkersPool(workers), true);
  }

  // this ctor requires init after call
  public WebWindow(JSString canvasDivId, WebWorkersPool workers, boolean ownsWorkers) {
    this.ownsWorkers = ownsWorkers;
    this.canvasDivId = canvasDivId;
    this.workers = workers;

//    JsHelper.consoleInfo("starting web window on " + canvasDivId);
    mainCanvas = JsHelper.createMainCanvas(null);
    draw2d = mainCanvas.getContext("2d").cast();
    draw2d.setGlobalCompositeOperation("copy");
    g = WebGraphics.getInstance();

    if (g != null) {
      connectToDom(canvasDivId);
      eventHandler = new JsInput(mainCanvas, repaint);
      observer.observePixelsOrDefault(mainCanvas);

      JsWindow.current().addEventListener("resize", handleWindowResize);
    }
  }

  public void disconnectFromDom() {
    Node parentNode = mainCanvas.getParentNode();
    if (parentNode != null) {
      parentNode.removeChild(mainCanvas);
      clientRect = null;
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
    if (debug)
      JsHelper.consoleInfo("setting focus to", canvasDivId);
    mainCanvas.focus();
//    debugFocus();
  }

  private void debugFocus() {
    TimerHandler handler = new TimerHandler() {
      @Override
      public void onTimer() {
        var e = HTMLDocument.current().getActiveElement();
        JsHelper.consoleInfo("focus set to ", e,
            "has focus =", JSBoolean.valueOf(hasFocus()));
        JsWindow.setTimeout(this, 1000);
      }
    };
    JsWindow.setTimeout(handler, 500);
  }

  @Override
  public boolean hasFocus() {
    return mainCanvas == HTMLDocument.current().getActiveElement();
  }

  void onSizeObserved(JsArrayReader<ResizeObserver.ResizeObserverEntry> entries, ResizeObserver o) {
    // JsHelper.consoleInfo("onSizeObserved: entries.length = ", entries.getLength());
    for (int i = 0, n = entries.getLength(); i < n; i++) {
      var entry = entries.get(i);
      if (entry.getTarget() == mainCanvas) {
        if (JSObjects.hasProperty(entry, entry.devicePixelContentBoxSize)) {
          if (entry.getDevicePixelContentBoxSize().getLength() == 1) {
            var size = entry.getDevicePixelContentBoxSize().get(0);
            onCanvasSizeChanged((int) size.getInlineSize(), (int) size.getBlockSize());
          }
        } else { //fallback when no "devicePixelContentBoxSize" provided
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
    // g.dispose();
    if (eventHandler != null) {
      eventHandler.dispose();
      eventHandler = null;
    }
    if (ownsWorkers)
      workers.terminateAll();
    setScene(null);
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

  public void setScene(Scene s) {
    scene = Disposable.assign(scene, s);
  }

  public SceneApi api() {
    return g == null ? null :
        new SceneApi(g, eventHandler.listeners, this);
  }

  private void requestNewFrame() {
    animationFrameRequest = JsWindow.requestAnimationFrame(frameCallback);
  }

  public void repaint() {
    repaintRequested = true;
  }

  private void paintScene() {
    g.ensureSize(clientRect.x, clientRect.y);
    g.setViewPortAndClientRect(clientRect.x, clientRect.y);
    scene.paint();
    if (debug)
      JsHelper.consoleInfo("paint scene", canvasDivId, clientRect.x, "x", clientRect.y);
    draw2d.drawImage(g.glCanvas,
        0, g.canvasHeight - clientRect.y, clientRect.x, clientRect.y,
        0, 0, clientRect.x, clientRect.y);
  }

  private void onAnimationFrame(double timestamp) {
    if (scene.update(timestamp / 1000) || repaintRequested) {
      if (clientRect != null && clientRect.x * clientRect.y != 0) {
        repaintRequested = false;
        paintScene();
      }
    }
    requestNewFrame();
  }

  @JSBody(
      params = {"style", "w", "h"},
      script = "style.width = w + 'px'; style.height = h + 'px';")
  static native void setStyleWHPx(CSSStyleDeclaration style, double w, double h);

  private void onCanvasSizeChanged(int inlineSize, int blockSize) {
    if (clientRect == null)
      clientRect = new V2i();
    clientRect.set(inlineSize, blockSize);
    eventHandler.setClientRect(clientRect);
    if (debug) {
      JsHelper.consoleInfo("  onCanvasSizeChanged: ", canvasDivId,
          "clientRect set to", clientRect.x, clientRect.y);
    }

    boolean visible = inlineSize != 0 && blockSize != 0;
    if (visible) {
      mainCanvas.setWidth(inlineSize);
      mainCanvas.setHeight(blockSize);
    }
    scene.onResize(clientRect, devicePixelRatio());
    if (visible) {
      paintScene();
    }
  }

  private void handleWindowResize(Event evt) {
    if (debug) {
      JsHelper.consoleInfo("handleWindowResize: ", canvasDivId);
      JsHelper.consoleInfo("  devicePixelRatio  = ", devicePixelRatio());
    }

    if (clientRect != null) {
      scene.onResize(clientRect, devicePixelRatio());
      if (clientRect.x * clientRect.y != 0) {
        paintScene();
      }
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

  public String textureUsage() {
    return g.tc.string();
  }
}
