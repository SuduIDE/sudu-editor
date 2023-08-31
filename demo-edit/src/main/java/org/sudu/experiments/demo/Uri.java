package org.sudu.experiments.demo;

import java.util.Objects;

public class Uri {

  public final String scheme;
  public final String authority;
  public final String path;
  public final Object nativeObject;

  public Uri(String path) {
    this(null, null, path, null);
  }

  public Uri(String scheme, String authority, String path, Object nativeObject) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
    this.nativeObject = nativeObject;
  }

  public String getFileName() {
    if (path == null) {
      return "";
    }
    var i = path.lastIndexOf("/");
    return i == -1 ? path : path.substring(i + 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Uri uri = (Uri) o;
    return Objects.equals(scheme, uri.scheme)
        && Objects.equals(authority, uri.authority)
        && Objects.equals(path, uri.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheme, authority, path);
  }
}
