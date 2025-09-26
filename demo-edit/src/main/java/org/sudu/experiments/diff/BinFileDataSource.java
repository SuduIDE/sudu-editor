package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class BinFileDataSource implements BinDataCache.DataSource {
  final FileHandle f;
  final WorkerJobExecutor e;

  public BinFileDataSource(FileHandle f, WorkerJobExecutor e) {
    this.f = f;
    this.e = e;
  }

  @Override
  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    FsWorkerJobs.asyncStats(e, f,
        stats -> result.accept(stats.size), onError);
  }

  @Override
  public void fetch(double address, int chinkSize, Result handler) {
    FsWorkerJobs.readBinFile(e, f, address, chinkSize,
        bytes -> handler.onData(address, bytes),
        error -> handler.onError(address, error));
  }
}
