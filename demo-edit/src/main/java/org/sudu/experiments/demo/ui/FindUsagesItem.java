package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.Map;
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
      ToIntFunction<String> m,
      Map<String, CachableItem<V4f>> fileNameCache
  ) {
    FindUsagesItem[] r = new FindUsagesItem[newSize];
    if (lines.length > 0) for (int i = first; i <= last; i++) {
      FindUsagesItemData d = data[i];
      int newIndex = i % r.length;
      int oldIndex = i % lines.length;
      FindUsagesItem oldItem = lines[oldIndex];
      if (oldItem != null && oldItem.data != d) {
        free(regionTexture, oldItem, fileNameCache);
        r[newIndex] = allocNewItem(d, regionTexture, m, fileNameCache);
        lines[oldIndex] = null;
      } else if (oldItem != null && r[newIndex] == null) {
        r[newIndex] = oldItem;
        lines[oldIndex] = null;
      } else {
        r[newIndex] = allocNewItem(d, regionTexture, m, fileNameCache);
      }
    } else if (newSize > 0) for (int i = first; i <= last; i++) {
      FindUsagesItemData d = data[i];
      int newIndex = i % r.length;
      r[newIndex] = allocNewItem(d, regionTexture, m, fileNameCache);
    }
    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      if (line != null) {
        free(regionTexture, line, fileNameCache);
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
      Map<String, CachableItem<V4f>> fileNameCache,
      int l
  ) {
    int index = l % lines.length;
    if (lines[index] != null) free(regionTexture, lines[index], fileNameCache);
    lines[index] = allocNewItem(data[l], regionTexture, m, fileNameCache);
  }

  private static FindUsagesItem allocNewItem(
      FindUsagesItemData d,
      RegionTexture regionTexture,
      ToIntFunction<String> m,
      Map<String, CachableItem<V4f>> fileNameCache
  ) {
    FindUsagesItem res = new FindUsagesItem(d);

    CachableItem<V4f> cacheItem = fileNameCache.get(d.fileName);
    if (cacheItem == null) {
      cacheItem = new CachableItem<>(regionTexture.alloc(d.fileName, m));
      fileNameCache.put(d.fileName, cacheItem);
    } else cacheItem.inc();
    res.tFiles = cacheItem.content;
    res.sizeFiles.set((int) res.tFiles.z, (int) res.tFiles.w);
    res.tLines = regionTexture.alloc(d.lineNumber, m);
    res.sizeLines.set((int) res.tLines.z, (int) res.tLines.w);
    res.tContent = regionTexture.alloc(d.codeContent, m);
    res.sizeContent.set((int) res.tContent.z, (int) res.tContent.w);

    return res;
  }

  private static void free(
      RegionTexture regionTexture,
      FindUsagesItem item,
      Map<String, CachableItem<V4f>> fileNameCache
  ) {
    String fileName = item.data.fileName;
    CachableItem<V4f> cacheItem = fileNameCache.get(fileName);
    if (cacheItem.dec() == 0) {
      regionTexture.free(cacheItem.content);
      fileNameCache.remove(fileName);
    }
    regionTexture.free(item.tLines);
    regionTexture.free(item.tContent);
  }

  public static void freeFileNameCache(RegionTexture regionTexture, Map<String, CachableItem<V4f>> fileNameCache) {
    fileNameCache.forEach((key, v) -> regionTexture.free(v.content));
  }
}
