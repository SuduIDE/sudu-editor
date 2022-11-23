package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

public interface Const {
  Runnable emptyRunnable = () -> {};

  Consumer<V2i> emptyDragLock = p -> {};
  DoubleSupplier double0 = () -> 0;
}
