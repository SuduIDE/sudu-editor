package org.sudu.experiments.demo.ui;

import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.ToIntFunction;

public class FindUsagesItem {
  V4f tFiles;
  V4f tLines;
  V4f tContent;
  final V2i sizeFiles = new V2i();
  final V2i sizeLines = new V2i();
  final V2i sizeContent = new V2i();
  final FindUsagesItemData data;

  public FindUsagesItem(FindUsagesItemData data, FindUsagesItemColors colors) {
    this.data = data;
  }

  public void draw(WglGraphics g, GL.Texture texture, int scrollX) {
//    tFiles.drawText(g, texture, 0, 0, 1);
//    tLines.drawText(g, texture, tFiles.size.x, 0, 1);
//    tContent.drawText(g, texture, tFiles.size.x + tLines.size.x, 0, 1);
  }

  static FindUsagesItem[] reallocRenderLines(
      int newSize,
      FindUsagesItem[] lines, int first, int last,
      FindUsagesItemData[] data,
      RegionTexture regionTexture,
      FindUsagesItemColors c,
      ToIntFunction<String> m
  ) {
    FindUsagesItem[] r = new FindUsagesItem[newSize];
    if (lines.length > 0) for (int i = first; i <= last; i++) {
      FindUsagesItemData d = data[i];
      int newIndex = i % r.length;
      int oldIndex = i % lines.length;
      FindUsagesItem oldItem = lines[oldIndex];
      if (oldItem != null && oldItem.data != d) {
        free(regionTexture, oldItem);
        r[newIndex] = allocNewItem(d, c, regionTexture, m);
        lines[oldIndex] = null;
      } else if (oldItem != null && r[newIndex] == null) {
        r[newIndex] = oldItem;
        lines[oldIndex] = null;
      } else {
        r[newIndex] = allocNewItem(d, c, regionTexture, m);
      }
    } else if (newSize > 0) for (int i = first; i <= last; i++) {
      FindUsagesItemData d = data[i];
      int newIndex = i % r.length;
      r[newIndex] = allocNewItem(d, c, regionTexture, m);
    }
    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      if (line != null) {
        free(regionTexture, line);
        lines[i] = null;
      }
    }
    return r;
  }

  private static FindUsagesItem allocNewItem(
      FindUsagesItemData d,
      FindUsagesItemColors c,
      RegionTexture regionTexture,
      ToIntFunction<String> m
  ) {
    FindUsagesItem res = new FindUsagesItem(d, c);
    res.tFiles = regionTexture.alloc(d.fileName, m);
    res.sizeFiles.set((int) res.tFiles.z, (int) res.tFiles.w);
    res.tLines = regionTexture.alloc(d.lineNumber, m);
    res.sizeLines.set((int) res.tLines.z, (int) res.tLines.w);
    res.tContent = regionTexture.alloc(d.codeContent, m);
    res.sizeContent.set((int) res.tContent.z, (int) res.tContent.w);

    return res;
  }

  private static void free(RegionTexture regionTexture, FindUsagesItem item) {
    regionTexture.free(item.tFiles);
    regionTexture.free(item.tLines);
    regionTexture.free(item.tContent);
  }
}
