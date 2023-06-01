package org.sudu.experiments.demo;

public class Location {

  public final Uri uri;
  public final int endColumn;
  public final int endLineNumber;
  public final int startColumn;
  public final int starLineNumber;

  public Location(Uri uri, int endColumn, int endLineNumber, int startColumn, int starLineNumber) {
    this.uri = uri;
    this.endColumn = endColumn;
    this.endLineNumber = endLineNumber;
    this.startColumn = startColumn;
    this.starLineNumber = starLineNumber;
  }
}
