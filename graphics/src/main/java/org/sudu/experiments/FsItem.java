package org.sudu.experiments;

import java.util.Arrays;

public interface FsItem {
  String getName();
  String[] getPath();

  default String getExtension() {
    String name = getName();
    int ind = name.lastIndexOf('.');
    if (ind == -1) return name;
    else return name.substring(ind);
  }

  static String toString(String[] path, String name, int intSize) {
    return Arrays.toString(path) + " name: " + name + ", size = " + intSize;
  }

  default String getFullPath() {
    return fullPath(getPath(), getName());
  }

  static String fullPath(String[] path, String name) {
    if (path.length == 0) return name;
    StringBuilder sb = new StringBuilder();
    for (String p : path) {
      sb.append(p).append('/');
    }
    return sb.append(name).toString();
  }
}
