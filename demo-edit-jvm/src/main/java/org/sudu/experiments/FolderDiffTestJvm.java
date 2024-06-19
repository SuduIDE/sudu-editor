package org.sudu.experiments;

import org.sudu.experiments.diff.tests.FolderDiffTest;
import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

class FolderDiffTestJvm implements WorkerJobExecutor {
  static final DoubleSupplier time = TimeUtil.dt();
  static final int DUMP_STATS_CALLS_DELTA = 2000;
  static int numThreads = 3;

  final Workers workers = new Workers(numThreads, EditorWorker::execute);
  final EventQueue edt = new EventQueue();

  final Map<String, MethodStat> handlers = new HashMap<>();
  int jobNo;

  FolderDiffTest test;

  FolderDiffTestJvm(Path left, Path right, boolean content) {
    var leftH = dir(left);
    var rightH = dir(right);

    test = new FolderDiffTest(leftH, rightH, content,
        this, time, this::onComplete);
    test.scan();
  }

  private void onComplete() {
    dumpStats();
  }

  JvmDirectoryHandle dir(Path path) {
    return new JvmDirectoryHandle(
        path, path, workers.bgWorker, edt);
  }

  private void run() throws InterruptedException {
    while (test.running()) {
      edt.execute();
      Thread.sleep(1);
    }
    workers.shutdown();
  }

  public static void main(String[] args) throws InterruptedException {
    if (args.length == 2 || args.length == 3) {
      Path p1 = Path.of(args[0]);
      Path p2 = Path.of(args[1]);
      boolean d1 = Files.isDirectory(p1);
      boolean d2 = Files.isDirectory(p2);
      boolean content = args.length == 3 && args[2].equals("content");
      if (d1 && d2) {
        System.out.println("path1: " + p1);
        System.out.println("path2: " + p2);
        new FolderDiffTestJvm(p1, p2, content).run();
      } else {
        System.err.println(
            "path is not a directory: " + (d1 ? p2 : p1));
      }
    } else {
      System.out.println("Usage: FolderDiffTest <path1> <path2> [content]");
    }
  }

  static final class PInt { public int value; }

  static final class MethodStat {
    static final Function<Class<?>, PInt> f = c -> new PInt();

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
  public void sendToWorker(Consumer<Object[]> handler, String method, Object... args) {
    ++jobNo;

    var stat = handlers.computeIfAbsent(method, MethodStat::new);
    stat.addCall(handler.getClass());

    if (jobNo % DUMP_STATS_CALLS_DELTA == 0) {
      dumpStats();
    }

    workers.sendToWorker(handler, method, args, edt);
  }

  void dumpStats() {
    StringBuilder sb = new StringBuilder();
    final int mapSize = handlers.size();
    sb.append("jobs ").append(jobNo).append(", handlers: \n");
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
}
