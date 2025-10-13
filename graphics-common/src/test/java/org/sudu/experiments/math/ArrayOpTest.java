package org.sudu.experiments.math;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ArrayOpTest {

  @Test
  void writeInt() {
    XorShiftRandom r = new XorShiftRandom();

    byte[] data = new byte[17];
    int value0 = r.nextInt(0x10000);

    int pos0 = 2;
    int pos1 = ArrayOp.writeInt16Le(data, pos0, value0);
    assertEquals(pos1, pos0 + 2);

    int value1 = ArrayOp.readInt16Le(data, pos0);
    assertEquals(value0, value1);

    int pos2 = ArrayOp.writeInt32Le(data, pos1, value0);
    assertEquals(pos2, pos1 + 4);

    int value2 = ArrayOp.readInt32Le(data, pos1);
    assertEquals(value0, value2);

  }

  @Test
  void deleteTest() {
    String[] from = new String[] { "A", "B", "C", "D", "E"};
    String[] r1 = ArrayOp.remove(from, 0, 3, new String[2]);
    String[] r2 = ArrayOp.remove(from, 1, 4, new String[2]);
    String[] r3 = ArrayOp.remove(from, 2, 5, new String[2]);
    assertArrayEquals(new String[] {"D", "E"}, r1);
    assertArrayEquals(new String[] {"A", "E"}, r2);
    assertArrayEquals(new String[] {"A", "B"}, r3);
  }

  @Test
  void testAddArrays() {
    Integer[] a = {1, 2};
    Integer[] b = {3, 4};
    Integer[] res = ArrayOp.add(a, b);
    assertArrayEquals(new Integer[]{1, 2, 3, 4}, res);

    Integer[] target = new Integer[4];
    ArrayOp.add(a, a.length, b, b.length, target);
    assertArrayEquals(new Integer[]{1, 2, 3, 4}, target);
  }

  @Test
  void testSegmentAndRemove() {
    String[] src = {"a", "b", "c", "d"};
    String[] seg = new String[2];
    ArrayOp.segment(src, 1, seg);
    assertArrayEquals(new String[]{"b", "c"}, seg);

    String[] removed = new String[3];
    ArrayOp.remove(src, 2, removed); // remove element at index 2 ("c")
    assertArrayEquals(new String[]{"a", "b", "d"}, removed);

    String[] removedRange = new String[2];
    ArrayOp.remove(src, 1, 3, removedRange); // keep 0 and 3
    assertArrayEquals(new String[]{"a", "d"}, removedRange);
  }

  @Test
  void testAddAndRemoveElement() {
    String[] arr = {"x", "y"};
    String[] added = ArrayOp.add(arr, "z");
    assertArrayEquals(new String[]{"x", "y", "z"}, added);

    String[] afterRemove = ArrayOp.remove(added, "y");
    assertArrayEquals(new String[]{"x", "z"}, afterRemove);

    // removing non‑existing element returns original array
    assertSame(added, ArrayOp.remove(added, "not"));
  }

  @Test
  void testIndexOf() {
    Integer[] arr = {5, 6, 7};
    assertEquals(1, ArrayOp.indexOf(arr, 6));
    assertEquals(-1, ArrayOp.indexOf(arr, 10));

    int[] ints = {1, 2, 3};
    assertEquals(2, ArrayOp.indexOf(ints, 3));
    assertEquals(-1, ArrayOp.indexOf(ints, 0));
  }

  @Test
  void testReadWriteInt16Le() {
    byte[] data = new byte[4];
    int pos = ArrayOp.writeInt16Le(data, 0, 0xABCD);
    assertEquals(2, pos);
    assertEquals(0xCD, data[0] & 0xFF);
    assertEquals(0xAB, data[1] & 0xFF);
    int value = ArrayOp.readInt16Le(data, 0);
    assertEquals(0xABCD, value);
  }

  @Test
  void testReadWriteInt32Le() {
    byte[] data = new byte[8];
    int pos = ArrayOp.writeInt32Le(data, 0, 0x11223344);
    assertEquals(4, pos);
    assertEquals(0x44, data[0] & 0xFF);
    assertEquals(0x33, data[1] & 0xFF);
    assertEquals(0x22, data[2] & 0xFF);
    assertEquals(0x11, data[3] & 0xFF);
    int value = ArrayOp.readInt32Le(data, 0);
    assertEquals(0x11223344, value);
  }

  @Test
  void testSendArrayList() {
    ArrayList<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    final Object[] result = new Object[1];
    Consumer<Object[]> consumer = arr -> result[0] = arr;
    ArrayOp.sendArrayList(list, consumer);
    assertArrayEquals(new Object[]{1, 2}, (Object[]) result[0]);
  }

  @Test
  void testSupplierAndArray() {
    Supplier<String[]> sup = ArrayOp.supplier("a", "b");
    assertArrayEquals(new String[]{"a", "b"}, sup.get());
    assertArrayEquals(new String[]{"x", "y"}, ArrayOp.array("x", "y"));
  }

  @Test
  void testResizeOrReturn() {
    Integer[] a = {1, 2, 3};
    Integer[] same = ArrayOp.resizeOrReturn(a, 3);
    assertSame(a, same);
    Integer[] resized = ArrayOp.resizeOrReturn(a, 5);
    assertEquals(5, resized.length);
    assertArrayEquals(new Integer[]{1, 2, 3, null, null}, resized);

    byte[] b = {1, 2};
    assertSame(b, ArrayOp.resizeOrReturn(b, 2));
    assertArrayEquals(new byte[]{1, 2, 0, 0}, ArrayOp.resizeOrReturn(b, 4));
  }

  @Test
  void testAddAt() {
    Integer[] data = new Integer[2];
    data[0] = 0;
    data[1] = 0;
    Integer[] after = ArrayOp.addAt(5, data, 2);
    assertEquals(4, after.length); // grew
    assertEquals(5, after[2]);
  }

  @Test
  void testInsertAndRemoveAt() {
    int[] ints = {1, 2, 3};
    int[] inserted = ArrayOp.insertAt(99, ints, 1);
    assertArrayEquals(new int[]{1, 99, 2, 3}, inserted);
    int[] removed = ArrayOp.removeAt(inserted, 1);
    assertArrayEquals(ints, removed);

    double[] ds = {1.0, 2.0};
    double[] insD = ArrayOp.insertAt(3.5, ds, 2);
    assertArrayEquals(new double[]{1.0, 2.0, 3.5}, insD);
    double[] remD = ArrayOp.removeAt(insD, 0);
    assertArrayEquals(new double[]{2.0, 3.5}, remD);
  }

  @Test
  void testGenericRemoveAt() {
    String[] data = {"a", "b", "c", "d"};
    // remove middle element
    String[] midRemoved = ArrayOp.removeAt(data, 1);
    assertArrayEquals(new String[]{"a", "c", "d"}, midRemoved);
    // remove first element
    String[] firstRemoved = ArrayOp.removeAt(data, 0);
    assertArrayEquals(new String[]{"b", "c", "d"}, firstRemoved);
    // remove last element
    String[] lastRemoved = ArrayOp.removeAt(data, data.length - 1);
    assertArrayEquals(new String[]{"a", "b", "c"}, lastRemoved);
  }

  @Test
  void testSwapAndReverse() {
    Integer[] arr = {1, 2, 3, 4};
    ArrayOp.swap(arr, 0, 3);
    assertArrayEquals(new Integer[]{4, 2, 3, 1}, arr);
    Integer[] rev = ArrayOp.reverse(arr);
    assertArrayEquals(new Integer[]{1, 3, 2, 4}, rev);
  }

  @Test
  void testCopyOfAndContains() {
    char[] chars = {'a', 'b'};
    char[] copy = ArrayOp.copyOf(chars);
    assertArrayEquals(chars, copy);
    assertNotSame(chars, copy);

    int[] ints = {5, 6, 7};
    int[] intCopy = ArrayOp.copyOf(ints);
    assertArrayEquals(ints, intCopy);
    assertTrue(ArrayOp.contains(ints, 6));
    assertFalse(ArrayOp.contains(ints, 10));

    String[] strs = {"x", "y"};
    assertTrue(ArrayOp.contains(strs, "y"));
    assertFalse(ArrayOp.contains(strs, "z"));
  }

  @Test
  void testFillSequence() {
    int[] expected = { 0, 0, 5, 6, 7, 0 };
    int[] a = new int[expected.length];
    ArrayOp.fillSequence(a, 2, 2 + 3, 5);
    assertArrayEquals(expected, a);
  }
}
