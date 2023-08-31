package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.EditorComponent;
import org.sudu.experiments.demo.Location;
import org.sudu.experiments.demo.Model;
import org.sudu.experiments.demo.Uri;
import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindUsagesItemBuilder {
  static final FindUsagesItemData[] items0 = new FindUsagesItemData[0];
  private final ArrayList<FindUsagesItemData> list = new ArrayList<>();

  // TODO(Minor): Move runnable from item to window
  public void addItem(String fileName, String lineNumber, String codeContent, Runnable r) {
    addItem(new FindUsagesItemData(r, lineNumber, codeContent, fileName));
  }

  public void addItem(FindUsagesItemData item) {
    list.add(item);
  }

  public FindUsagesItemData[] items() {
    return list.toArray(items0);
  }

  public static FindUsagesItemData[] buildUsagesItems(List<Pos> usages, EditorComponent editorComponent) {
    return buildItems(usages, null, editorComponent);
  }

  public static FindUsagesItemData[] buildDefItems(Location[] defs, EditorComponent editorComponent) {
    return buildItems(null, defs, editorComponent);
  }

  private static String fileName(Uri uri) {
    return uri != null ? uri.getFileName() : "";
  }

  private static FindUsagesItemData[] buildItems(List<Pos> usages, Location[] defs, EditorComponent edit) {
    Model model = edit.model();

    FindUsagesItemBuilder tbb = new FindUsagesItemBuilder();
    int cnt = 0;
    int itemsLength = defs == null ? usages.size() : defs.length;
    for (int i = 0; i < itemsLength; i++) {
      int intLineNumber;
      String codeContent;
      String fileName;
      if (defs == null) {
        intLineNumber = usages.get(i).line;
        codeContent = model.document.line(intLineNumber).makeString().trim();
        fileName = fileName(model.uri);
      } else {
        intLineNumber = defs[i].range.startLineNumber;
        codeContent = Objects.equals(model.uri, defs[i].uri)
            ? model.document.line(intLineNumber).makeString().trim() : "";

        fileName = fileName(defs[i].uri);
      }
      String codeContentFormatted = codeContent.length() > 153
          ? codeContent.substring(0, 150) + "..." : codeContent;
      String fileNameFormatted = fileName.length() > 153
          ? fileName.substring(0, 150) + "..." : fileName;
      String lineNumber = String.valueOf(intLineNumber + 1);

      Location def;
      Pos pos;
      if (defs == null) {
        def = null;
        pos = usages.get(i);
      } else {
        pos = null;
        def = defs[i];
      }
      Runnable action = defs == null
          ? () -> edit.gotoUsage(pos)
          : () -> edit.gotoDefinition(def);
      tbb.addItem(
          fileNameFormatted,
          lineNumber,
          codeContentFormatted,
          action
      );
    }
    return tbb.items();
  }
}
