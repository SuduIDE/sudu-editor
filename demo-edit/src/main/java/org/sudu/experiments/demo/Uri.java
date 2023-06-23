package org.sudu.experiments.demo;

import java.util.Objects;

public class Uri {

  public final String scheme;
  public final String authority;
  public final String path;

  public Uri(String scheme, String authority, String path) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
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
