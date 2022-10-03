package org.sudu.experiments;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DllLoader {

  // loads a dll from system FS,
  //    then if failed - try to locate in target\classes,
  //    and then try extract a jar file

  public static String loadDll(String library, Class<?> aClass) {
    String libraryName = System.mapLibraryName(library);

    try {
      System.load(libraryName);
      return libraryName;
    } catch (Throwable t) {
      String path = copyDllFromResources(libraryName, aClass.getClassLoader());
      if (path != null) {
        System.load(path);
      }
      return path;
    }
  }

  static String copyDllFromResources(String libraryName, ClassLoader classLoader) {
    try {
      return getResourceFile(libraryName, classLoader).getAbsolutePath();
    } catch (Throwable e) {
      System.err.println("Failed to load library \"" + libraryName + "\": " + e.getMessage());
      return null;
    }
  }

  static File getResourceFile(String libraryFullName, ClassLoader classLoader) throws IOException {
    URL fileUrl = classLoader.getResource(libraryFullName);
    if (fileUrl != null && "file".equals(fileUrl.getProtocol())) {
      return new File(fileUrl.getFile());
    } else {
      InputStream inputStream = classLoader.getResourceAsStream(libraryFullName);
      if (inputStream == null) {
        throw new IOException("classLoader can not locate resource: " + libraryFullName);
      }

      File tempDir = Files.createTempDirectory("sudu").toFile();
      tempDir.deleteOnExit();
      File tempDll = new File(tempDir, libraryFullName);
      tempDll.deleteOnExit();

      try (InputStream s = inputStream) {
        Files.copy(s, tempDll.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      return tempDll;
    }
  }
}
