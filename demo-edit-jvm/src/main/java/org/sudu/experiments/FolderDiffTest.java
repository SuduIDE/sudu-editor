package org.sudu.experiments;

import org.sudu.experiments.diff.DiffModelBuilder;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

class FolderDiffTest implements WorkerJobExecutor {
  static final DoubleSupplier time = TimeUtil.dt();
  public static final int DUMP_STATS_CALLS_DELTA = 2000;
  static int numThreads = 3;

  final Workers workers = new Workers(numThreads, EditorWorker::execute);
  final EventQueue edt = new EventQueue();
  final Path leftPath;
  final Path rightPath;
  final boolean content;

  boolean running = true;
  DirectoryNode leftRoot, rightRoot;
  FolderDiffModel leftModel = new FolderDiffModel(null);
  FolderDiffModel rightModel = new FolderDiffModel(null);
  int updateDiffInfoCounter, jobNo;

  FolderDiffTest(Path left, Path right, boolean content) {
    this.leftPath = left;
    this.rightPath = right;
    this.content = content;
    var leftH = dir(left);
    var rightH = dir(right);
    leftRoot = new DirectoryNode(leftH, null);
    rightRoot = new DirectoryNode(rightH, null);
    var builder = new DiffModelBuilder((_1, _2, _3) -> updateDiffInfo(), this, content);

    builder.compareRoots(
        leftRoot, rightRoot,
        leftModel, rightModel);
  }

  JvmDirectoryHandle dir(Path path) {
    return new JvmDirectoryHandle(
        path, path, workers.bgWorker, edt);
  }

  private void run() throws InterruptedException {
    while (running) {
      edt.execute();
      Thread.sleep(1);
    }
    workers.shutdown();
  }

  private void dumpResult() {
    String r = "" +
        "updateDiffInfo #calls = " + updateDiffInfoCounter + '\n'
        + "leftModel:\n"
        + "  .compared = " + leftModel.compared + '\n'
        + "  .diffType = " + DiffTypes.name(leftModel.diffType) + '\n'
        + "rightModel:\n"
        + "  .compared = " + rightModel.compared + '\n'
        + "  .diffType = " + DiffTypes.name(rightModel.diffType) + '\n'
        + "time: " + time + "s\n";
    System.out.print(r);
  }

  private void updateDiffInfo() {
    ++updateDiffInfoCounter;
    if (running) {
      if (leftModel.compared && rightModel.compared) {
        System.out.println("Finished" + (content ? " scan with content: " : ": "));
        dumpStats();
        dumpResult();
        running = false;
      }
    } else {
      System.err.println("updateDiffInfo after Finished, updateDiffInfoCounter = " + updateDiffInfoCounter);
    }
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
        new FolderDiffTest(p1, p2, content).run();
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

  final Map<String, MethodStat> handlers = new HashMap<>();

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
