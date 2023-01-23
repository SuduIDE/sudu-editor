package org.sudu.experiments;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FileHandle {

  String getName();
  String[] getPath();

  void getSize(IntConsumer result);

  void readAsText(Consumer<String> consumer, Consumer<String> onError);

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError);

  static String toString(String[] path, String name, int intSize) {
    return Arrays.toString(path) + " name: " + name + ", size = " + intSize;
  }

  static String toString(String[] path, String name) {
    return Arrays.toString(path) + " name: " + name;
  }
}