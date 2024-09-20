package org.sudu.experiments;

import org.sudu.experiments.angle.AngleEGL;
import org.sudu.experiments.angle.AngleWindow;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.win32.*;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Win32Window implements WindowPeer, Window {

  static boolean debugContext = AppPreferences.getInt("debugContext", 0) != 0;
  static boolean debugMousePointer = true;

  final Runnable repaint = this::repaint;
  final InputListeners inputListeners = new InputListeners(repaint);
  final EventQueue eventQueue;
  final String config;
  final Workers workers;

  private boolean repaintRequested = true;
  private boolean closed;
  private String currentCursor;
  private long currentCursorHandle = Win32Cursors.IDC_ARROW;
  private Scene scene;
  Win32Time time;

  // platform windows
  long hWnd, timerId;
  boolean mouseTracking;
  int windowDpi;
  AngleWindow angleWindow;
  V2i angleSurfaceSize = new V2i();
  V2i windowSize = new V2i();
  State windowState = State.NORMAL;
  ArrayList<Win32Window> childWindows = new ArrayList<>();
  Win32InputState inputState;
  String title = "";
  FpsMeter fpsMeter = new FpsMeter(this::updateFps);

  enum State { MINIMIZED, NORMAL, MAXIMIZED }

  public Win32Window(EventQueue eq, Win32Time t, Workers workers) {
    this(eq, "window", t, workers);
  }

  public Win32Window(EventQueue eq, String configName, Win32Time t, Workers workers) {
    eventQueue = eq;
    config = configName;
    time = t;
    inputState = new Win32InputState(t);
    this.workers = workers;
  }

  @Override
  public boolean hasFocus() {
    return hWnd == Win32.GetFocus();
  }

  public boolean opened() {
    return !closed;
  }

  public boolean init(
      Function<SceneApi, Scene> sf,
      Supplier<Win32Graphics> graphics,
      Win32Window mainWindow
  ) {
    hWnd = Win32.CreateWindow(this, "",
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT,
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT,
        Win32.GetModuleHandle0(), 2000);
    if (hWnd == 0) return false;

    windowDpi = Win32.GetDpiForWindow(hWnd);

    boolean maximized = loadMaximized();
    if (maximized) {
      Win32.SendMessageW(hWnd, WindowPeer.WM_SYSCOMMAND, WindowPeer.SC_MAXIMIZE, 0);
    }

    if (mainWindow == null && debugContext) {
      System.out.println("Using Angle debugContext");
    }

    angleWindow = new AngleWindow(hWnd, debugContext,
        mainWindow != null ? mainWindow.angleWindow : null, graphics);

    if (!angleWindow.initialized()) {
      System.err.println("angleContext init failed: " + AngleEGL.getErrorString());
      return false;
    }

    V2i angleSize = angleWindow.getSurfaceSize();
    if (angleSize == null) {
      System.err.println("angleContext eglMakeCurrent failed: " + AngleEGL.getErrorString());
      return false;
    }
    angleSurfaceSize.set(angleSize);
    setVSync(false);

    if (mainWindow == null) {
      time.printTime("window, angle and graphics started: ");
    }

    scene = sf.apply(api());
    setTitle(scene.getClass().getSimpleName());
    scene.onResize(angleSurfaceSize, devicePixelRatio());

    return renderFirst(maximized);
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
    Win32.SetWindowTextW(hWnd, CString.toChar16CString(title));
  }

  private void updateFps(double fps) {
    Win32Graphics graphics = angleWindow.graphics();
    int drawCalls = graphics.getAngleGl().getDrawCallCount();
    String t = title + " - " + Numbers.iRnd(fps) + "fps, "
        + drawCalls + " #dCalls, " + graphics.tc.string();
    Win32.SetWindowTextW(hWnd, CString.toChar16CString(t));
  }

  private boolean renderFirst(boolean maximized) {
    Win32.ShowWindow(hWnd, maximized ? Win32.SW_MAXIMIZE : Win32.SW_NORMAL);
    double t0 = time.now();
    scene.update(t0);
    scene.paint();
    repaintRequested = false;
    return angleWindow.swapBuffers();
  }

  public boolean update() {
    return updateSelf() || updateChildren();
  }

  private boolean updateSelf() {
    if (windowSize.x * windowSize.y == 0) return false;

    V2i angleSize = angleWindow.makeCurrent();
    if (angleSize == null) return false;

    if (!angleSurfaceSize.equals(angleSize)) {
      angleSurfaceSize.set(angleSize);
      scene.onResize(angleSurfaceSize, devicePixelRatio());
      repaintRequested = true;
    }

    boolean needsRepaint = scene.update(time.now()) || repaintRequested;
    if (needsRepaint) {
      repaintRequested = false;
      angleWindow.initViewport(angleSize);
      scene.paint();
      angleWindow.swapBuffers();
      angleWindow.graphics().getAngleGl().notifyNewFrame();
      fpsMeter.notifyNewFrame();
    }
    return needsRepaint;
  }

  public void dispose() {
    disposeChildren();

    scene.dispose();
    inputListeners.clear();
    scene = null;
    currentCursor = null;

    boolean contextIsRoot = angleWindow.isRootContext();
    if (contextIsRoot)
      angleWindow.graphics().reportLostResources();
    angleWindow.dispose();
    if (mouseTracking)
      mouseTracking = !trackMouseLeave(false);
    hWnd = Win32.DestroyWindow(hWnd) ? 0 : -1;
    if (hWnd != 0) System.err.println("DesktopWindow.dispose: destroyWindow failed");
    windowSize.set(0,0);
  }

  private SceneApi api() {
    return new SceneApi(angleWindow.graphics(), inputListeners, this);
  }

  public void repaint() {
    repaintRequested = true;
  }

  private void onWindowResize(int msgWidth, int msgHeight, State state) {
    if (angleWindow != null) saveMaximized(state == State.MAXIMIZED);
    windowState = state;
    windowSize.set(msgWidth, msgHeight);
    windowDpi = Win32.GetDpiForWindow(hWnd);
//    Debug.consoleInfo("WM_SIZE: " + windowSize + ", state = " + windowState);
    repaint();
  }

  private boolean loadMaximized() {
    return AppPreferences.getInt(cfgMax(), 0) != 0;
  }

  private void saveMaximized(boolean max) {
    AppPreferences.setInt(cfgMax(), max ? 1 : 0);
  }

  private String cfgMax() { return config.concat(".maximized"); }

  @SuppressWarnings("CommentedOutCode")
  private void onWindowMove(int x, int y) {
    int[] rect4 = new int[4];
    Win32.GetWindowRect(hWnd, rect4);
    windowDpi = Win32.GetDpiForWindow(hWnd);

    // todo: save windowPos
//    AppPreferences.setInt(config.concat(".x"), rect4[0]);
//    AppPreferences.setInt(config.concat(".y"), rect4[1]);
//    AppPreferences.setInt(config.concat(".w"), rect4[2] - rect4[0]);
//    AppPreferences.setInt(config.concat(".h"), rect4[3] - rect4[1]);
//    System.out.println("[Window] WM_MOVE: x = " + x + ", y = " + y);
//    System.out.println("[Window] WM_MOVE: rect = " + Arrays.toString(rect4));
  }

  public void setVSync(boolean vSync) {
    angleWindow.swapInterval(vSync ? 1 : 0);
  }

  int cursorModCount;
  final Exception[] modCalls = new Exception[3];

  @SuppressWarnings("StringEquality")
  public void setCursor(String cursor) {
    if (currentCursor != cursor) {
      if (debugMousePointer && cursorModCount < modCalls.length)
        modCalls[cursorModCount] = new Exception();
      cursorModCount++;
      currentCursor = cursor;
      currentCursorHandle = Win32Cursors.toWin32(cursor);
      Win32.SetCursor(currentCursorHandle);
    }
  }

  @Override
  public double timeNow() {
    return time.now();
  }

  public float devicePixelRatio() {
    return windowDpi / 96.f;
  }

  private void onTimer() {
    eventQueue.execute();
    update();
  }

  void onEnterExitSizeMove(long hWnd, boolean enter) {
    if (enter) {
      if (timerId == 0) timerId = Win32.SetTimer(hWnd, 0, 16, 0);
    } else {
      if (timerId != 0) { Win32.KillTimer(hWnd, 0); timerId = 0; }
    }
  }

  private void onKillFocus() {
    inputState.onKillFocus();
    inputListeners.sendBlurEvent();
  }

  private void onSetFocus() {
    inputState.readState();
    inputListeners.sendFocusEvent();
  }

  private void onActivate(boolean active) {}

  @Override
  public long windowProc(long hWnd, int msg, long wParam, long lParam) {
//    System.out.println("hWnd = " + Long.toHexString(hWnd) + ", msg = " + WindowPeer.wmToString(msg));
    switch (msg) {
      case WM_PAINT -> { Win32.ValidateRect0(hWnd); return 0; }
      case WM_CLOSE -> { closed = true; return 0; }
      case WM_TIMER -> { onTimer(); return 0; }
    }

    if (WM_LBUTTONDOWN <= msg && msg <= WM_MBUTTONDBLCLK) {
      inputState.onMouseButton(msg, lParam, windowSize, hWnd, inputListeners);
    }

    switch (msg) {
      case WM_SIZE -> onWindowResize(
          Win32.LOWORD(lParam), Win32.HIWORD(lParam),
          wParamToState((int) wParam));
      case WM_MOVE -> onWindowMove(Win32.LOWORD(lParam), Win32.HIWORD(lParam));
      case WM_ENTERSIZEMOVE, WM_EXITSIZEMOVE
          -> onEnterExitSizeMove(hWnd, msg == WM_ENTERSIZEMOVE);

      case WM_MOUSEMOVE -> {
        cursorModCount = 0;
        if (!mouseTracking)
          mouseTracking = trackMouseLeave(true);
        inputState.onMouseMove(lParam, windowSize, inputListeners);
        if (cursorModCount > 1)
          System.err.println("cursorModCount = " + cursorModCount);
        if (debugMousePointer && cursorModCount > 0)
          Arrays.fill(modCalls, null);
      }

      case WindowPeer.WM_MOUSELEAVE -> {
        mouseTracking = false;
        inputState.onMouseLeave(windowSize, inputListeners);
      }

      case WM_MOUSEWHEEL, WM_MOUSEHWHEEL -> inputState.onMouseWheel(
          lParam, wParam, windowSize, hWnd,
          inputListeners, msg == WM_MOUSEWHEEL);
      case WM_SETCURSOR -> {
        if (Win32HitTest.hitClient(lParam)) {
          Win32.SetCursor(currentCursorHandle);
          return 1;
        }
      }

      // keyboard
      case WM_SYSKEYUP, WM_SYSKEYDOWN ->
          inputState.onKey(hWnd, msg, wParam, lParam, inputListeners);
      case WM_KEYUP, WM_KEYDOWN, WM_CHAR -> {
        if (inputState.onKey(hWnd, msg, wParam, lParam, inputListeners))
          return 0;
      }

      // focus
      case WM_ACTIVATE -> onActivate(wParam != 0);
      case WM_KILLFOCUS -> onKillFocus();
      case WM_SETFOCUS -> onSetFocus();
    }
    return Win32.DefWindowProcW(hWnd, msg, wParam, lParam);
  }

  private boolean trackMouseLeave(boolean enable) {
    return Win32.TrackMouseEvent(
        Win32.TME_LEAVE | (enable ? 0 : Win32.TME_CANCEL),
        hWnd, Win32.HOVER_DEFAULT);
  }

  public GL.ImageData readPixels() {
    return null;
  }

  static State wParamToState(int wParam) {
    return switch (wParam) {
      case SIZE_MINIMIZED -> State.MINIMIZED;
      case SIZE_MAXIMIZED -> State.MAXIMIZED;
      default -> State.NORMAL;
    };
  }

  public boolean addChild(String title, Function<SceneApi, Scene> sf) {
    Win32Window child = new Win32Window(eventQueue, "child", time, workers);

    if (!child.init(sf, angleWindow::graphics, Win32Window.this)) {
      System.err.println("Window.init failed");
      return false;
    }

    childWindows.add(child);
    return true;
  }

  private void disposeChildren() {
    for (Win32Window window : childWindows) {
      window.dispose();
    }
    childWindows.clear();
  }

  private boolean updateChildren() {
    if (childWindows.isEmpty()) return false;
    boolean update = false;
    for (Iterator<Win32Window> iterator = childWindows.iterator(); iterator.hasNext(); ) {
      Win32Window w = iterator.next();
      if (!w.opened()) {
        iterator.remove();
        w.dispose();
      } else {
        update = w.update() | update;
      }
    }
    return update;
  }

  @Override
  public Host getHost() {
    return Host.Direct2D;
  }

  @Override
  public void showDirectoryPicker(Consumer<DirectoryHandle> onResult) {
    eventQueue.execute(() -> folderDialog(onResult));
  }

  private void folderDialog(Consumer<DirectoryHandle> onResult) {
    onEnterExitSizeMove(hWnd, true);
    String result = Win32FileDialog.openFolderDialog(hWnd);
    onEnterExitSizeMove(hWnd, false);
    if (result != null) {
      Path folderPath = Path.of(result);
      var dir = new JvmDirectoryHandle(
          folderPath, folderPath,
          workers.bgWorkerHi, eventQueue);
      eventQueue.execute(() -> onResult.accept(dir));
    }
  }

  @Override
  public void showOpenFilePicker(Consumer<FileHandle> onResult) {
    eventQueue.execute(() -> fileDialog(onResult));
  }

  private void fileDialog(Consumer<FileHandle> onResult) {
    onEnterExitSizeMove(hWnd, true);
    String file = Win32FileDialog.openFileDialog(hWnd);
    onEnterExitSizeMove(hWnd, false);
    if (file != null) {
      Path path = Path.of(file);
      FileHandle fh = new JvmFileHandle(
          path, path, workers.bgWorkerLo, eventQueue);
      eventQueue.execute(() -> onResult.accept(fh));
    }
  }

  @Override
  public WorkerJobExecutor worker() {
    return workers;
  }

  @Override
  public void readClipboardText(Consumer<String> success, Consumer<Throwable> onError) {
    String clipboardText = Win32.getClipboardText(hWnd, null);
    Runnable r = clipboardText != null ?
      () -> success.accept(clipboardText) :
      () -> onError.accept(new RuntimeException("getClipboardText failed"));
    eventQueue.execute(r);
  }

  @Override
  public void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError) {
    boolean set = Win32.setClipboardText(hWnd, text);
    Runnable r = set ? success :
            () -> onError.accept(new RuntimeException("setClipboardText failed"));
    eventQueue.execute(r);
  }

  @Override
  public void runLater(Runnable r) {
    eventQueue.execute(r);
  }
}
