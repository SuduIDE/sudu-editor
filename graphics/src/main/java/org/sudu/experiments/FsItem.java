package org.sudu.experiments;

public interface FsItem {
  String getName();
  String[] getPath();

  default String getExtension() {
    String name = getName();
    int ind = name.lastIndexOf('.');
    if (ind == -1) return name;
    else return name.substring(ind);
  }

  static String toString(String kind, String[] path, String name, boolean onWorker) {
    StringBuilder sb = new StringBuilder(kind).append(": ");
    fullPath(path, name, sb);
    if (onWorker) sb.append(" worker");
    return sb.toString();
  }

  default String getFullPath() {
    return fullPath(getPath(), getName());
  }

  static String fullPath(String[] path, String name) {
    if (path.length == 0) return name;
    return fullPath(path, name, new StringBuilder()).toString();
  }

  static StringBuilder fullPath(String[] path, String name, StringBuilder sb) {
    for (String p : path) {
      sb.append(p).append('/');
    }
    return sb.append(name);
  }
}
