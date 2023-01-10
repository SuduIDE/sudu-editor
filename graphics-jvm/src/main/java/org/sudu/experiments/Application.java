package org.sudu.experiments;

import org.sudu.experiments.win32.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class Application {

  public static void run(Function<SceneApi, Scene> sf) throws InterruptedException {
    run(sf, new FontConfig());
  }

  public static void run(Function<SceneApi, Scene> sf, FontConfig fontConfig) throws InterruptedException {
    run(sf, sf.getClass().getName(), fontConfig);
  }

  public static void run(Function<SceneApi, Scene> sf, String title, FontConfig fontConfig) throws InterruptedException {
    Win32Time time = new Win32Time();
    Win32.coInitialize();
    // todo: debug under other launcher
    //   Win32.setProcessDpiAwareness(Win32.PROCESS_SYSTEM_DPI_AWARE);

    D2dFactory d2dCanvasFactory = D2dFactory.create();
    if (!loadFontConfig(fontConfig, d2dCanvasFactory)) return;

    time.printTime("fonts loaded: ");

    Supplier<Win32Graphics> graphics = Win32Graphics.lazyInit(d2dCanvasFactory);
    EventQueue eventQueue = new EventQueue();
    ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    Win32Window window = new Win32Window(eventQueue, time, ioExecutor);

    if (!window.init(title, sf, graphics, null)) {
      throw new RuntimeException("window.init failed");
    }

    time.printTime("first frame rendered: ");

    while (Win32.PeekTranslateDispatchMessage() && window.opened()) {
      eventQueue.execute();
      if (!window.update()) Thread.sleep(1);
    }

    window.dispose();
    ioExecutor.shutdown();
  }

  static boolean loadFontConfig(FontConfig config, D2dFactory factory) {
    System.out.println("[Fonts] Loading fonts ...");
    double[] times = factory.loadFontConfig(config, TimeUtil.dt());
    if (times != null && times.length == 17) {
      System.out.println("[Fonts]   loadResources: " + TimeUtil.toString3(times[0]) + " ms");
      System.out.println("[Fonts]   d2dAddFontFiles: " + TimeUtil.toString3(times[1]) + " ms");
    }
    return true;
  }
}
