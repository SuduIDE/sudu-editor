package org.sudu.experiments.demo;

public class Uri {

  public final String scheme;
  public final String authority;
  public final String path;

  public Uri(String scheme, String authority, String path) {
    this.scheme = scheme;
    this.authority = authority;
    this.path = path;
  }

  public Uri() { this(null, null ,null); }
}
