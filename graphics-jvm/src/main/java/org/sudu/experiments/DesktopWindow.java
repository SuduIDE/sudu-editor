package org.sudu.experiments;

import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.math.V2i;

import java.util.function.BiFunction;

public class DesktopWindow implements Window {
  final Runnable repaint = this::requestRepaint;
  final InputListeners inputListeners = new InputListeners(repaint);

  private WglGraphics g;
  private boolean repaintRequested = true;
  private String currentCursor;
  private Scene scene;

  public DesktopWindow(BiFunction<SceneApi, String, Scene> factory) {
    scene = factory.apply(null, null);
  }

  @SuppressWarnings("unused")
  public void dispose() {
    g.dispose();
  }

  private void init(GLApi.Context gl, V2i clientRect, BiFunction<SceneApi, String, Scene> sf) {
    g = new JvmGraphics(gl, clientRect, repaint);

    scene = sf.apply(api(), null);
    scene.onResize(clientRect);
    double t0 = timeNow();
    Debug.consoleInfo("time start: ", t0);
    onAnimationFrame(t0);
  }

  private SceneApi api() {
    return new SceneApi(g, inputListeners, this);
  }

  private void requestRepaint() {
    repaintRequested = true;
  }

  private void onAnimationFrame(double timestamp) {
    if (scene.update(timestamp / 1000) || repaintRequested) {
      repaintRequested = false;
      scene.paint();
    }
  }

  private void handleWindowResize() {
    V2i size = null;
    Debug.consoleInfo("handleWindowResize new size: " + size);
//    mainCanvas.setWidth(size.x);
//    mainCanvas.setHeight(size.y);

    g.setWindowSize(size);
    scene.onResize(size);
    scene.paint();
  }

  public void setCursor(String cursor) {
    currentCursor = cursor; // JsHelper.setCursor(cursor, currentCursor, mainCanvas);
  }

  @Override
  public V2i getClientRect() {
    return new V2i(g.clientRect);
  }

  @Override
  public V2i getScreenRect() {
//    Screen screen = Window.current().getScreen();
    //todo: add Firefox <-> Chrome detection code
    //   firefox measures in DOM pixels
    //   chrome measures in Device pixels
//    return new V2i(screen.getWidth(), screen.getHeight());
    return new V2i(1920, 1080);
  }

  @Override
  public double timeNow() {
    return 0; // Performance.now() / 1000;
  }

  @Override
  public double devicePixelRatio() {
    return 1;
  }

}
