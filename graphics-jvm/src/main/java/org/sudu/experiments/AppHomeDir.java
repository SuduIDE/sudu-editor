package org.sudu.experiments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppHomeDir {
  public static final Path appHome = appHome();

  static Path appHome() {
    Path home = Paths.get(System.getProperty("user.home"));
    Path appHome = home.resolve(".sudu");
    System.out.println("appHome = " + appHome);
    if (!Files.isDirectory(appHome)) {
      try {
        Files.createDirectories(appHome);
      } catch (IOException e) {
        return home;
      }
    }
    return appHome;
  }

  public static Path subdir(String name) {
    Path dir = appHome.resolve(name);
    try {
      return Files.isDirectory(dir) ? dir : Files.createDirectories(dir);
    } catch (IOException e) {
      System.err.println("AppHomeDir.subdir: error " + e.getMessage());
    }
    return null;
  }

}
