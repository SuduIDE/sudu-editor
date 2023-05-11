/*
 *  Copyright 2023 Alexey Andreev.
 *
 *  This code is granted by Alexey Andreev,
 *      the author of the TeaVM project teavm.org
 */
package org.sudu.experiments.json;

public abstract class JsonVisitor {
  public JsonVisitor object(JsonErrorReporter reporter) {
    return null;
  }

  public JsonVisitor array(JsonErrorReporter reporter) {
    return null;
  }

  public JsonVisitor property(JsonErrorReporter reporter, String name) {
    return null;
  }

  public void stringValue(JsonErrorReporter reporter, String value) {
  }

  public void intValue(JsonErrorReporter reporter, long value) {
  }

  public void floatValue(JsonErrorReporter reporter, double value) {
  }

  public void nullValue(JsonErrorReporter reporter) {
  }

  public void booleanValue(JsonErrorReporter reporter, boolean value) {
  }

  public void end(JsonErrorReporter reporter) {
  }
}
