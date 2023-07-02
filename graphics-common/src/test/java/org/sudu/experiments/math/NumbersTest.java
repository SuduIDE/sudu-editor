package org.sudu.experiments.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumbersTest {

  @Test
  public void testOverflow() {
    Assertions.assertEquals(100, Numbers.iDivRound(4, 50, 2));

    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 1, 1));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 10, 10));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 100, 100));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 1000, 1000));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 10000, 10000));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 100000, 100000));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, 1000000, 1000000));

    Assertions.assertEquals(
            Math.round(Integer.MAX_VALUE / 10.),
            Numbers.iDivRound(Integer.MAX_VALUE, 10, 100));
    Assertions.assertEquals(
            Math.round(Integer.MAX_VALUE / 100.),
            Numbers.iDivRound(Integer.MAX_VALUE, 100, 10000));
    Assertions.assertEquals(
            Math.round(Integer.MAX_VALUE / 1000.),
            Numbers.iDivRound(Integer.MAX_VALUE, 1000, 1000000));


    Assertions.assertEquals(1, Numbers.iDivRound(Integer.MAX_VALUE / 2, 2, Integer.MAX_VALUE - 1));
    Assertions.assertEquals(100, Numbers.iDivRound(Integer.MAX_VALUE / 10, 100, Integer.MAX_VALUE / 10));
    Assertions.assertEquals(Integer.MAX_VALUE, Numbers.iDivRound(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

}
