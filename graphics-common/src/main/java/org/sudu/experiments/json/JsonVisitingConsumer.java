/*
 *  Copyright 2023 Alexey Andreev.
 *
 *  This code is granted by Alexey Andreev,
 *      the author of the TeaVM project teavm.org
 */
package org.sudu.experiments.json;

import java.util.ArrayDeque;
import java.util.Deque;

public class JsonVisitingConsumer extends JsonConsumer {
  private Deque<JsonVisitor> visitorStack = new ArrayDeque<>();
  private int noVisitorLevel;

  public JsonVisitingConsumer(JsonVisitor visitor) {
    visitorStack.push(visitor);
  }

  @Override
  public void enterObject(JsonErrorReporter reporter) {
    if (noVisitorLevel == 0) {
      JsonVisitor next = visitorStack.peek().object(reporter);
      if (next == null) {
        noVisitorLevel = 1;
      } else {
        visitorStack.push(next);
      }
    } else {
      noVisitorLevel++;
    }
  }

  @Override
  public void exitObject(JsonErrorReporter reporter) {
    exit(reporter);
  }

  @Override
  public void enterArray(JsonErrorReporter reporter) {
    if (noVisitorLevel == 0) {
      JsonVisitor next = visitorStack.peek().array(reporter);
      if (next == null) {
        noVisitorLevel = 1;
      } else {
        visitorStack.push(next);
      }
    } else {
      noVisitorLevel++;
    }
  }

  @Override
  public void exitArray(JsonErrorReporter reporter) {
    exit(reporter);
  }

  @Override
  public void enterProperty(JsonErrorReporter reporter, String name) {
    if (noVisitorLevel == 0) {
      JsonVisitor next = visitorStack.peek().property(reporter, name);
      if (next == null) {
        noVisitorLevel = 1;
      } else {
        visitorStack.push(next);
      }
    } else {
      noVisitorLevel++;
    }
  }

  @Override
  public void exitProperty(JsonErrorReporter reporter, String name) {
    exit(reporter);
  }

  private void exit(JsonErrorReporter reporter) {
    if (noVisitorLevel > 0) {
      noVisitorLevel--;
    } else {
      visitorStack.pop();
    }
    if (noVisitorLevel == 0 && !visitorStack.isEmpty()) {
      visitorStack.peek().end(reporter);
    }
  }

  @Override
  public void stringValue(JsonErrorReporter reporter, String value) {
    if (noVisitorLevel == 0) {
      visitorStack.peek().stringValue(reporter, value);
    }
  }

  @Override
  public void intValue(JsonErrorReporter reporter, long value) {
    if (noVisitorLevel == 0) {
      visitorStack.peek().intValue(reporter, value);
    }
  }

  @Override
  public void floatValue(JsonErrorReporter reporter, double value) {
    if (noVisitorLevel == 0) {
      visitorStack.peek().floatValue(reporter, value);
    }
  }

  @Override
  public void nullValue(JsonErrorReporter reporter) {
    if (noVisitorLevel == 0) {
      visitorStack.peek().nullValue(reporter);
    }
  }

  @Override
  public void booleanValue(JsonErrorReporter reporter, boolean value) {
    if (noVisitorLevel == 0) {
      visitorStack.peek().booleanValue(reporter, value);
    }
  }
}
