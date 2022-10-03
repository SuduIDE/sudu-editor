package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public interface Const {
  Runnable emptyRunnable = () -> {};

  Consumer<V2i> emptyDragLock = p -> {};
}
