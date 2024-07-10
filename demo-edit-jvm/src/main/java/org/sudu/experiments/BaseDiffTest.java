package org.sudu.experiments;

import org.sudu.experiments.editor.worker.FileDiffWorker;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

public abstract class BaseDiffTest implements WorkerJobExecutor {
  protected static final DoubleSupplier time = TimeUtil.dt();
  protected static final int DUMP_STATS_CALLS_DELTA = 2000;
  protected static int numThreads = 3;

  protected final EventQueue edt = new EventQueue();
  protected final Workers workers;

  private final Map<String, MethodStat> handlers = new HashMap<>();
  private int jobNo, jobsDone;

  BaseDiffTest(int numThreads) {
    workers = new Workers(numThreads, FileDiffWorker::execute, edt);
  }

  BaseDiffTest() {
    this(numThreads);
  }

  protected abstract boolean running();

  final void run() throws InterruptedException {
    while (running()) {
      edt.execute();
      Thread.sleep(1);
    }
    workers.shutdown();
  }

  protected void onComplete() {
    dumpStats();
  }

  JvmDirectoryHandle dir(Path path) {
    return new JvmDirectoryHandle(
        path, path, workers.bgWorkerHi, edt);
  }

  static final class PInt { public int value; }

  static final class MethodStat {
    static final Function<Class<?>, PInt> f = c -> new PInt();
    static final Function<String, MethodStat> n = MethodStat::new;

    final String name;
    final Map<Class<?>, PInt> map = new HashMap<>();
    int calls;

    MethodStat(String name) {
      this.name = name;
    }

    void addCall(Class<?> h) {
      calls++;
      map.computeIfAbsent(h, f).value++;
    }

    public void dump(StringBuilder sb) {
      final int mapSize = map.size();
      sb.append("{ calls: ").append(calls).append(", map : { ");
      map.forEach(
          new BiConsumer<>() {
            int cnt;
            @Override
            public void accept(Class<?> cl, PInt stat) {
              sb.append(cl.getSimpleName()).append(':').append(stat.value);
              if (++cnt != mapSize)
                sb.append(", ");
            }
          }
      );
      sb.append(" } }");
    }
  }

  @Override
  public void sendToWorker(
      boolean priority,
      Consumer<Object[]> handler, String method, Object... args
  ) {
    ++jobNo;

    var stat = handlers.computeIfAbsent(method, MethodStat.n);
    stat.addCall(handler.getClass());

    if (jobNo % DUMP_STATS_CALLS_DELTA == 0) {
      dumpStats();
    }

    Consumer<Object[]> h = r -> {
      jobsDone++;
      handler.accept(r);
    };

    workers.sendToWorker(priority, h, method, args);
  }

  void dumpStats() {
    StringBuilder sb = new StringBuilder();
    final int mapSize = handlers.size();
    sb.append("jobs ").append(jobNo)
        .append(", incomplete ").append(jobNo - jobsDone)
        .append(", handlers: \n");
    handlers.forEach(
        new BiConsumer<>() {
          int cnt;
          @Override
          public void accept(String name, MethodStat stat) {
            sb.append(name).append(':');
            stat.dump(sb);
            sb.append(++cnt != mapSize ? ",\n" : "\n");
          }
        }
    );
    System.out.print(sb.append('\n'));
  }

  interface Ctor {
    BaseDiffTest n(Path left, Path right, boolean content);
  }

  static void run(
      String[] args,
      Ctor ctor, Class<?> cls
  ) throws InterruptedException {
    if (args.length >= 2 && args.length <= 4) {
      Path p1 = Path.of(args[0]);
      Path p2 = Path.of(args[1]);
      boolean d1 = Files.isDirectory(p1);
      boolean d2 = Files.isDirectory(p2);
      boolean content = args.length >= 3 && args[2].equals("content");
      if (d1 && d2) {
        System.out.println("  path1 = " + p1);
        System.out.println("  path2 = " + p2);
        System.out.println("  content = " + content);
        ctor.n(p1, p2, content).run();
      } else {
        System.err.println(
            "path is not a directory: " + (d1 ? p2 : p1));
      }
    } else {
      System.out.println("Usage: " + cls.getSimpleName()
          + " <path1> <path2> [content]");
    }
  }

}
