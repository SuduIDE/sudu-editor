package org.sudu.experiments.demo;

import org.sudu.experiments.demo.ui.RegionTexture;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.ToIntFunction;

public class LineNumbersRenderer {
  V4f tContent;
  final V2i size = new V2i();
  final int num;

  public LineNumbersRenderer(int num) {
    this.num = num;
  }

  public static LineNumbersRenderer[] reallocRenderLines(
      int newSize,
      LineNumbersRenderer[] view,
      int firstLine,
      int lastLine,
      RegionTexture regionTexture,
      ToIntFunction<String> m
  ) {
    LineNumbersRenderer[] r = new LineNumbersRenderer[newSize];
    if (view.length > 0) for (int i = firstLine; i <= lastLine; i++) {
      int newIndex = i % r.length;
      int oldIndex = i % view.length;
      LineNumbersRenderer oldItem = view[oldIndex];
      if (oldItem != null && oldItem.num != i) {
        free(regionTexture, oldItem);
        r[newIndex] = allocNewItem(i, regionTexture, m);
        view[oldIndex] = null;
      } else if (oldItem != null && r[newIndex] == null) {
        r[newIndex] = oldItem;
        view[oldIndex] = null;
      } else {
        r[newIndex] = allocNewItem(i, regionTexture, m);
      }
    } else if (newSize > 0) for (int i = firstLine; i <= lastLine; i++) {
      int newIndex = i % r.length;
      r[newIndex] = allocNewItem(i, regionTexture, m);
    }
    for (int i = 0; i < view.length; i++) {
      var item = view[i];
      if (item != null) {
        free(regionTexture, item);
        view[i] = null;
      }
    }
    return r;
  }

  public static void setNewItem(
      LineNumbersRenderer[] view,
      int line,
      RegionTexture regionTexture,
      ToIntFunction<String> m
  ) {
    int index = line % view.length;
    if (view[index] != null) free(regionTexture, view[index]);
    view[index] = allocNewItem(line, regionTexture, m);
  }

  private static LineNumbersRenderer allocNewItem(
      int num,
      RegionTexture view,
      ToIntFunction<String> m
  ) {
    LineNumbersRenderer res = new LineNumbersRenderer(num+1);

    res.tContent = view.alloc(String.valueOf(num+1), m);
    res.size.set((int) res.tContent.z, (int) res.tContent.w);

    return res;
  }

  private static void free(
      RegionTexture regionTexture,
      LineNumbersRenderer item
  ) {
    regionTexture.free(item.tContent);
  }
}
