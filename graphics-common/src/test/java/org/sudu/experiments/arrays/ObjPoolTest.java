package org.sudu.experiments.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjPoolTest {
  
  static class Datum {}
  
  @Test
  void test() {
    var pool = new ObjPool<>(new Datum[2], Datum::new);

    Datum a = pool.add();
    Datum b = pool.add();
    assertNotNull(a);
    assertNotNull(b);
    assertNotNull(pool.add());
    assertNotNull(pool.add());

    assertEquals(4, pool.size());
    Datum[] data = pool.data();
    assertNotNull(data);
    assertTrue(4 >= data.length);
    assertSame(a, data[0]);
    assertSame(b, data[1]);

    pool.clear();
    assertEquals(0, pool.size());
  }

}
