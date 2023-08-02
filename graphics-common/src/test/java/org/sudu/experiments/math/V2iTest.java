package org.sudu.experiments.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class V2iTest {
  @Test
  void testEquals() {
    V2i v12 = new V2i(1,2);
    assertTrue(v12.equals(1,2));
    assertFalse(v12.equals(1,1));
    assertFalse(v12.equals(2,2));
  }
}
