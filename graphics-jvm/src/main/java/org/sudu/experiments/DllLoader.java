package org.sudu.experiments;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
      return getResourceFile(libraryName, classLoader);
    } catch (Throwable e) {
      System.err.println("Failed to load library \"" + libraryName + "\": " + e.getMessage());
      return null;
    }
  }

  static String getResourceFile(String libraryFullName, ClassLoader classLoader) throws IOException {
    URL fileUrl = classLoader.getResource(libraryFullName);
    if (fileUrl != null && "file".equals(fileUrl.getProtocol())) {
      return new File(fileUrl.getFile()).getAbsolutePath();
    } else {
      InputStream inputStream = classLoader.getResourceAsStream(libraryFullName);
      if (inputStream == null) {
        throw new IOException("classLoader can not locate resource: " + libraryFullName);
      }

      Path dll = AppHomeDir.appHome.resolve(libraryFullName).toAbsolutePath();

      System.out.println("Files.copy: dll = " + dll);
      try (InputStream s = inputStream) {
        Files.copy(s, dll, StandardCopyOption.REPLACE_EXISTING);
      }
      return dll.toString();
    }
  }
}
