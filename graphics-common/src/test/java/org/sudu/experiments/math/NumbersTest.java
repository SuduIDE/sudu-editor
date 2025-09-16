package org.sudu.experiments.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersTest {

  @Test
  public void testOverflow() {
    assertEquals(100, Numbers.divRound(4, 50, 2));

    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 1, 1));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 10, 10));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 100, 100));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 1000, 1000));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 10000, 10000));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 100000, 100000));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, 1000000, 1000000));

    assertEquals(
            Math.round(Integer.MAX_VALUE / 10.),
            Numbers.divRound(Integer.MAX_VALUE, 10, 100));
    assertEquals(
            Math.round(Integer.MAX_VALUE / 100.),
            Numbers.divRound(Integer.MAX_VALUE, 100, 10000));
    assertEquals(
            Math.round(Integer.MAX_VALUE / 1000.),
            Numbers.divRound(Integer.MAX_VALUE, 1000, 1000000));


    assertEquals(1, Numbers.divRound(Integer.MAX_VALUE / 2, 2, Integer.MAX_VALUE - 1));
    assertEquals(100, Numbers.divRound(Integer.MAX_VALUE / 10, 100, Integer.MAX_VALUE / 10));
    assertEquals(Integer.MAX_VALUE, Numbers.divRound(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

  @Test
  void testNumDigits() {
    assertEquals(1, Numbers.numDecimalDigits(-1));
    assertEquals(1, Numbers.numDecimalDigits(0));
    assertEquals(1, Numbers.numDecimalDigits(9));
    assertEquals(2, Numbers.numDecimalDigits(10));
    assertEquals(2, Numbers.numDecimalDigits(99));
    assertEquals(3, Numbers.numDecimalDigits(100));
    assertEquals(3, Numbers.numDecimalDigits(999));
    assertEquals(4, Numbers.numDecimalDigits(1000));
    assertEquals(4, Numbers.numDecimalDigits(9999));
    assertEquals(10, Numbers.numDecimalDigits(1_000_000_000));
    assertEquals(10, Numbers.numDecimalDigits(2_147_483_647));
  }
}
