package org.sudu.experiments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriteMbFile {
  public static void main(String[] args) throws IOException {
    if (args.length != 2)
      throw new RuntimeException("usage file N");
    Files.write(Path.of(args[0]), mbData(Integer.parseInt(args[1])));
  }

  static byte[] mbData(int n) {
    return new byte[n * 1024 * 1024];
  }
}
