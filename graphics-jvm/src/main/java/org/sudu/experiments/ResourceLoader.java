package org.sudu.experiments;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class ResourceLoader {
  public static Function<String, byte[]> loader(String folder, Class<?> aClass) {
    return name -> load(folder.concat(name), aClass);
  }

  public static byte[] load(String name, Class<?> aClass) {
    InputStream resource = aClass.getClassLoader().getResourceAsStream(name);
    if (resource == null) return null;

    try(InputStream s = resource) {
      byte[] data = new byte[s.available()];
      int nRead = readStream(s, data);
      if (nRead < data.length) {
        err("read less then expected: " + nRead + ", available " + data.length);
        return null;
      }
      return data;
    } catch (IOException e) {
      err(e.getMessage());
      return null;
    }
  }

  static void err(String msg) {
    System.err.println("resource load error: ".concat(msg));
  }

  static int readStream(InputStream in, byte[] data) throws IOException {
    int r = 0;
    while (r < data.length) {
      int n = in.read(data, r, data.length - r);
      if (n == -1) break;
      r += n;
    }
    return r;
  }
}
