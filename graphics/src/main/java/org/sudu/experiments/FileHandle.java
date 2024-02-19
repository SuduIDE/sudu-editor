package org.sudu.experiments;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FileHandle extends FsItem {
  void getSize(IntConsumer result);

  void readAsText(Consumer<String> consumer, Consumer<String> onError);

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError);
}
