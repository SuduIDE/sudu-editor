package org.sudu.experiments.nativelib;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class CopyAngleToResources {

  public static void main(String[] args) throws IOException {
    String dll = "libGLESv2.dll";
    Path dir = Paths.get("angle-dll/src/main/resources/");
    Path resourceDir = dir.toAbsolutePath();
    if (!Files.isDirectory(resourceDir)) {
      System.out.println("resourceDir path is not a directory: " + resourceDir);
      return;
    }
    Path edgePath = Paths.get("C:\\Program Files (x86)\\Microsoft\\Edge\\Application");

    if (!Files.isDirectory(edgePath)) {
      System.out.println("edgePath path is not a directory: " + edgePath);
      return;
    }

    ArrayList<Path> dlls = new ArrayList<>();

    DirectoryStream<Path> dirStream = Files.newDirectoryStream(edgePath);
    for (Path dirFile: dirStream) {
      if (Files.isDirectory(dirFile)) {
        Path dllPath = dirFile.resolve(dll);
        if (Files.isRegularFile(dllPath)) {
          dlls.add(dllPath);
        }
      }
    }
    dirStream.close();

    if (dlls.size() == 0) {
      System.out.println("no " + dll + " found");
    } else {
      dlls.sort(Path::compareTo);

      Path source = dlls.get(dlls.size() - 1);
      Path target = resourceDir.resolve(dll);

      System.out.println("copying ... \n" +
          " from " + source + "\n to-> " + target);
      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
  }

}
