package org.sudu.experiments;

import java.util.Arrays;
import java.util.function.Consumer;

public interface FileHandle {
  int getSize();
  String getName();
  String[] getPath();

  void readAsText(Consumer<String> consumer, Consumer<String> onError);

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError);

  static String toString(String[] path, String name, int intSize) {
    return Arrays.toString(path) + " name: " + name + ", size = " + intSize;
  }
}