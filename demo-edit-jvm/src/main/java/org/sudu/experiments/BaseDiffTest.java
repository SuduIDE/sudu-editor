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
  protected final Workers workers = new Workers(numThreads, FileDiffWorker::execute, edt);

  protected final Map<String, CollectorFolderDiffTestJvm.MethodStat> handlers = new HashMap<>();
  protected int jobNo;

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

    var stat = handlers.computeIfAbsent(method, MethodStat::new);
    stat.addCall(handler.getClass());

    if (jobNo % DUMP_STATS_CALLS_DELTA == 0) {
      dumpStats();
    }

    workers.sendToWorker(priority, handler, method, args);
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
