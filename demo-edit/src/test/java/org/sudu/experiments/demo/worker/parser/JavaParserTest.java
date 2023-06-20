package org.sudu.experiments.demo.worker.parser;

import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaParserTest {

  @Test
  void parseChars() {
    List<Object> result = new ArrayList<>();
    JavaParser.parseChars(new char[0], result);

    for (Object o : result) {
      assertTrue(o.getClass().isArray());
    }

    assertSame(result.get(0).getClass().componentType(), int.class);
    assertSame(result.get(1).getClass().componentType(), char.class);
    assertSame(result.get(2).getClass().componentType(), int.class);
  }

  @Test
  void charBufferTest() {
    CharBuffer cb = CharBuffer.allocate(0);
    cb.put("");
  }
}