package org.sudu.experiments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadResource {
  public static String readFile(String filename, Class<?> aClass) {
    try {
      var url = aClass.getClassLoader().getResource(filename);
      if (url == null)
        throw new IllegalArgumentException("Illegal resource name: " + filename);
      return Files.readString(Path.of(url.toURI()));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] readFileBytes(String filename, Class<?> aClass) {
    try {
      var url = aClass.getClassLoader().getResource(filename);
      if (url == null)
        throw new IllegalArgumentException("Illegal resource name: " + filename);
      return Files.readAllBytes(Path.of(url.toURI()));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
