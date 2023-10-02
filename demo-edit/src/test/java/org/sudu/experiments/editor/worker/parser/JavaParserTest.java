package org.sudu.experiments.editor.worker.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaParserTest {

  @Test
  void parseChars() {
    List<Object> result = new ArrayList<>();
    JavaParser.parse(new char[0], result);

    for (Object o : result) {
      assertTrue(o.getClass().isArray());
    }

    assertSame(result.get(0).getClass().componentType(), int.class);
    assertSame(result.get(1).getClass().componentType(), char.class);
    assertSame(result.get(2).getClass().componentType(), int.class);
  }
}
