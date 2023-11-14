package org.sudu.experiments.nativelib;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CopyAngleToResources {

  public static void main(String[] args) throws IOException {
    String dll = "libGLESv2.dll";
    Path dir = Paths.get("angle-dll/src/main/resources/");
    Path resourceDir = dir.toAbsolutePath();
    if (!Files.isDirectory(resourceDir)) {
      Files.createDirectories(resourceDir);
      if (!Files.isDirectory(resourceDir)) {
        System.out.println("resourceDir path is not a directory: " + resourceDir);
        return;
      }
    }
    Path edgePath = Paths.get("C:\\Program Files (x86)\\Microsoft\\Edge\\Application");

    if (!Files.isDirectory(edgePath)) {
      System.out.println("edgePath path is not a directory: " + edgePath);
      return;
    }

    ArrayList<Path> dlls = new ArrayList<>();
    Map<Path, String> versions = new HashMap<>();

    DirectoryStream<Path> dirStream = Files.newDirectoryStream(edgePath);
    for (Path dirFile: dirStream) {
      if (Files.isDirectory(dirFile)) {
        Path dllPath = dirFile.resolve(dll);
        if (Files.isRegularFile(dllPath)) {
          dlls.add(dllPath);
          versions.put(dllPath, dirFile.getFileName().toString());
        }
      }
    }
    dirStream.close();

    if (dlls.isEmpty()) {
      System.out.println("no " + dll + " found");
    } else {
      dlls.sort(Path::compareTo);

      Path source = dlls.get(dlls.size() - 1);
      Path target = resourceDir.resolve(dll);
      String versioString = versions.get(source);
      Path versionFile = resourceDir.resolve("libGLESv2.version");

      if (Files.isReadable(versionFile)) {
        String oldVersion = Files.readString(versionFile);
        System.out.println("oldVersion = " + oldVersion);
      }
      System.out.println("newVersion = " + versioString);

      System.out.println("copying ... "
          + "\n from " + source
          + "\n to → " + target);
      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

      Files.writeString(versionFile, versioString,
          StandardOpenOption.WRITE,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
    }
  }

}
