package org.sudu.experiments.demo;

public class Location {

  public final Uri uri;
  public final Range range = new Range();

  public Location(Uri uri) {
    this.uri = uri;
  }
}
