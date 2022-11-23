package org.sudu.experiments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppPreferences {
  static Path path = AppHomeDir.subdir(".settings");

  public static void setInt(String name, int value) {
    if (path != null) write(name, Integer.toString(value).getBytes());
  }

  public static int getInt(String name, int defaultValue) {
    Path file = path.resolve(name);
    byte[] bytes = Files.isRegularFile(file) ? read(file) : null;
    return bytes != null ? parseInt(bytes, defaultValue, new int[1]) : defaultValue;
  }

  static int parseInt(byte[] bytes, int defaultValue, int[] pPos) {
    int result = 0, pos = pPos[0];
    while (pos < bytes.length) {
      byte value = bytes[pos];
      if ('0' <= value && value <= '9') {
        result = result * 10 + value - '0';
        pos++;
      } else break;
    }
    if (pos == pPos[0]) result = defaultValue;
    else pPos[0] = pos;
    return result;
  }

  static void write(String name, byte[] bytes) {
    try {
      Files.write(path.resolve(name), bytes);
    } catch (IOException e) {
      System.err.println("AppPreferences.write error: " + e.getMessage());
    }
  }

  static byte[] read(Path file) {
    try {
      return Files.readAllBytes(file);
    } catch (IOException e) {
      System.err.println("AppPreferences.read error: " + e.getMessage());
    }
    return null;
  }
}
