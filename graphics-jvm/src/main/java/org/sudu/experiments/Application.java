package org.sudu.experiments;

import org.sudu.experiments.fonts.FontResources;
import org.sudu.experiments.win32.*;
import org.sudu.experiments.worker.WorkerExecutor;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Application {

  public static void run(Function<SceneApi, Scene> sf, FontResources ... fontConfig) throws InterruptedException {
    run(sf, WorkerExecutor.i(), 0, sf.getClass().getName(), fontConfig);
  }

  public static void run(
      Function<SceneApi, Scene> sf,
      WorkerExecutor workerExecutor, int numThreads,
      String title,
      FontResources ... fontConfig
  ) throws InterruptedException {
    Win32Time time = new Win32Time();
    Win32.coInitialize();
    // todo: debug under other launcher
    //   Win32.setProcessDpiAwareness(Win32.PROCESS_SYSTEM_DPI_AWARE);

    D2dFactory d2dCanvasFactory = D2dFactory.create();
    if (!loadFontConfig(fontConfig, d2dCanvasFactory)) return;

    time.printTime("fonts loaded: ");

    Supplier<Win32Graphics> graphics = Win32Graphics.lazyInit(d2dCanvasFactory);
    EventQueue eventQueue = new EventQueue();

    ExecutorService ioExecutor = newThreadPool(numThreads);

    Win32Window window = new Win32Window(eventQueue, time, ioExecutor, workerExecutor);

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

  static ExecutorService newThreadPool(int maxThreads) {
    // to save memory we can use executor.allowCoreThreadTimeOut(true);
    // to shutdown worker threads if no activity
    return maxThreads <= 1
        ? Executors.newSingleThreadExecutor()
        : Executors.newFixedThreadPool(maxThreads);
  }

  static boolean loadFontConfig(FontResources[] config, D2dFactory factory) {
    if (config.length != 0) {
      System.out.println("[Fonts] Loading fonts ...");
      double[] times = factory.loadFontConfig(config, TimeUtil.dt());
      if (times != null && times.length == 2) {
        System.out.println("[Fonts]   loadResources: " + TimeUtil.toString3(times[0]) + " ms");
        System.out.println("[Fonts]   d2dAddFontFiles: " + TimeUtil.toString3(times[1]) + " ms");
      }
    }
    return true;
  }

}
