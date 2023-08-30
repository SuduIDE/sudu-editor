package org.sudu.experiments.demo.ui;

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

  public FindUsagesItem(FindUsagesItemData data) {
    this.data = data;
  }

  static FindUsagesItem[] reallocRenderLines(
      int newSize,
      FindUsagesItem[] lines, int first, int last,
      FindUsagesItemData[] data,
      RegionTexture regionTexture,
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
        r[newIndex] = allocNewItem(d, regionTexture, m);
        lines[oldIndex] = null;
      } else if (oldItem != null && r[newIndex] == null) {
        r[newIndex] = oldItem;
        lines[oldIndex] = null;
      } else {
        r[newIndex] = allocNewItem(d, regionTexture, m);
      }
    } else if (newSize > 0) for (int i = first; i <= last; i++) {
      FindUsagesItemData d = data[i];
      int newIndex = i % r.length;
      r[newIndex] = allocNewItem(d, regionTexture, m);
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

  public static void setNewItem(
      FindUsagesItem[] lines,
      FindUsagesItemData[] data,
      RegionTexture regionTexture,
      ToIntFunction<String> m,
      int l
  ) {
    int index = l % lines.length;
    if (lines[index] != null) free(regionTexture, lines[index]);
    lines[index] = allocNewItem(data[l], regionTexture, m);
  }

  private static FindUsagesItem allocNewItem(
      FindUsagesItemData d,
      RegionTexture regionTexture,
      ToIntFunction<String> m
  ) {
    FindUsagesItem res = new FindUsagesItem(d);
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
