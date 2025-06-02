package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.diff.folder.ItemFolderDiffModel;
import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.editor.Document;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.fs.*;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class DiffUtils {

  public static final String FIND_DIFFS = "DiffUtils.findDiffs";

  public static void findDiffs(
      char[] charsN, int[] intsN,
      char[] charsM, int[] intsM,
      int[] syncL, int[] syncR,
      int[] ints,
      List<Object> result
  ) {
    DiffModel model = new DiffModel();
    model.compareLinesOnly = ints[0] == 1;
    model.syncL = syncL;
    model.syncR = syncR;
    int[] resultInts = model.findDiffs(charsN, intsN, charsM, intsM);
    result.add(resultInts);
  }


  public static final String CMP_FOLDERS = "asyncCompareFolders";

  public static void compareFolders(
      DirectoryHandle left, DirectoryHandle right,
      Consumer<Object[]> r
  ) {
    FolderDiffHandler handler = new FolderDiffHandler(left, right, r);
    handler.read();
  }

  public static final String READ_FOLDER = "asyncReadFolder";

  public static void readFolder(
      DirectoryHandle folder,
      int[] ints,
      Consumer<Object[]> r
  ) {
    var model = new ItemFolderDiffModel(null, folder.getName());
    int diffType = ints[0];
    int itemKind = ints[1];
    model.setDiffType(diffType);
    model.setItem(folder);
    var reader = new ReadFolderHandler(model, diffType, itemKind, r);
    model.posInParent = ints[2];
    reader.beginRead();
  }

  public static final String REREAD_FOLDER = "asyncRereadFolder";

  public static void rereadFolder(
      DirectoryHandle handle,
      int[] ints, char[] chars,
      Consumer<Object[]> r
  ) {
    ArrayReader reader = new ArrayReader(ints);
    LinkedList<TreeS> paths = new LinkedList<>();
    int len = reader.next();
    for (int i = 0; i < len; i++) {
      int offset = reader.next();
      int count = reader.next();
      boolean isFolder = reader.next() == 1;
      paths.add(new TreeS(new String(chars, offset, count), isFolder));
    }

    ArrayWriter writer = new ArrayWriter();
    ArrayList<FsItem> items = new ArrayList<>();
    var leftReader = new RereadFolderHandler(handle, items, writer, paths, r);
    leftReader.beginRead();
  }

  public static int[] makeIntervals(Document document, int fromLine, int toLine, boolean cmpOnlyLines) {
    ArrayWriter writer = new ArrayWriter();
    writer.write(toLine - fromLine);
    int offset = 0;
    for (int i = fromLine; i < toLine; i++) {
      CodeLine line = document.line(i);
      if (cmpOnlyLines) {
        writer.write(1);
        writer.write(offset, line.totalStrLength);
        offset += line.totalStrLength;
      } else {
        int Mi = line.length();
        writer.write(Mi);
        for (int j = 0; j < Mi; j++) {
          var elem = line.get(j);
          writer.write(offset, elem.length());
          offset += elem.length();
        }
      }
      offset++;
    }
    return writer.getInts();
  }

  public static int[] makeIntervals(Document document, boolean cmpOnlyLines) {
    return makeIntervals(document, 0, document.length(), cmpOnlyLines);
  }

  public static DiffInfo readDiffInfo(int[] ints) {
    ArrayReader reader = new ArrayReader(ints);
    return new DiffInfo(
        readLineDiffs(reader),
        readLineDiffs(reader),
        readRanges(reader)
    );
  }

  public static LineDiff[] readLineDiffs(ArrayReader reader) {
    int len = reader.next();
    LineDiff[] lineDiff = new LineDiff[len];
    for (int i = 0; i < len; i++) {
      int line = reader.next();
      if (line == -1) continue;
      int type = reader.next();
      int lineLen = reader.next();
      if (lineLen == -1) lineDiff[line] = new LineDiff(type);
      else {
        lineDiff[line] = new LineDiff(type, lineLen);
        for (int j = 0; j < lineLen; j++) {
          lineDiff[line].elementTypes[j] = reader.next();
        }
      }
    }
    return lineDiff;
  }

  public static DiffRange[] readRanges(ArrayReader reader) {
    int len = reader.next();
    DiffRange[] ranges = new DiffRange[len];
    for (int i = 0; i < len; i++) {
      int fromL = reader.next();
      int lengthL = reader.next();
      int fromR = reader.next();
      int lengthR = reader.next();
      int type = reader.next();
      ranges[i] = new DiffRange(fromL, lengthL, fromR, lengthR, type);
    }
    return ranges;
  }

  public static void printInfo(DiffInfo info, Document docN, Document docM) {
    int i = 0, j = 0;
    while (i < info.lineDiffsL.length
        && j < info.lineDiffsR.length) {
      var diffN = info.lineDiffsL[i];
      var diffM = info.lineDiffsR[j];
      System.out.println(formatStr(docN, diffN, i) + "\t\t" + formatStr(docM, diffM, j));
      i++;
      j++;
    }
    for (; i < info.lineDiffsL.length; i++) {
      System.out.println(formatStr(docN, info.lineDiffsL[i], i));
    }
    for (; j < info.lineDiffsR.length; j++) {
      System.out.println(" ".repeat(46) + "\t\t" + formatStr(docM, info.lineDiffsR[j], j));
    }
  }

  public static void printInfo(
      DiffInfo info,
      Document docN, Document docM,
      int[] syncL, int[] syncR
  ) {
    int i = 0, j = 0;
    ArrayList<String> lines = new ArrayList<>();
    String subLine = "_".repeat(46);
    String space = " ".repeat(46);
    while (i < info.lineDiffsL.length
        && j < info.lineDiffsR.length) {
      var diffN = info.lineDiffsL[i];
      var diffM = info.lineDiffsR[j];
      lines.add((formatStr(docN, diffN, i) + "\t\t" + formatStr(docM, diffM, j)));
      i++;
      j++;
    }
    for (; i < info.lineDiffsL.length; i++) {
      lines.add(formatStr(docN, info.lineDiffsL[i], i));
    }
    for (; j < info.lineDiffsR.length; j++) {
      lines.add(space + "\t\t" + formatStr(docM, info.lineDiffsR[j], j));
    }
    int inserted = 0;
    for (int k = 0; k < syncL.length; k++) {
      int sL = syncL[k];
      int sR = syncR[k];
      if (sL == sR) {
        lines.add(sL + inserted++, subLine + "\t\t" + subLine);
      } else {
        lines.add(sL + inserted++, subLine);
        lines.add(sR + inserted++, space + "\t\t" + subLine);
      }
    }
    lines.forEach(System.out::println);
  }

  private static String formatStr(Document doc, LineDiff diff, int ind) {
    String line = doc.line(ind).makeString();
    if (line.length() < 40) line = line + " ".repeat(40 - line.length());

    if (diff == null) return String.format("%4d  %.40s", ind + 1, line);
    else if (diff.type == DiffTypes.DELETED) return String.format("%4d- %.40s", ind + 1, line);
    else if (diff.type == DiffTypes.INSERTED) return String.format("%4d+ %.40s", ind + 1, line);
    else if (diff.type == DiffTypes.EDITED) return String.format("%4d# %.40s", ind + 1, line);
    return String.format("%4d  %.40s", ind + 1, line);
  }

  public static void findDiffs(
      Document document1,
      Document document2,
      boolean cmpOnlyLines,
      int[] syncL, int[] syncR,
      Consumer<DiffInfo> result,
      WorkerJobExecutor window
  ) {
    char[] chars1 = document1.getChars();
    char[] chars2 = document2.getChars();
    int[] intervals1 = makeIntervals(document1, cmpOnlyLines);
    int[] intervals2 = makeIntervals(document2, cmpOnlyLines);

    window.sendToWorker(true,
        r -> {
          int[] reply = ((ArrayView) r[0]).ints();
          DiffInfo model = readDiffInfo(reply);
          result.accept(model);
        }, FIND_DIFFS,
        chars1, intervals1,
        chars2, intervals2,
        syncL, syncR,
        new int[] {cmpOnlyLines ? 1 : 0}
    );
  }

  public static void findIntervalDiffs(
      Document document1,
      Document document2,
      Consumer<DiffInfo> result,
      WorkerJobExecutor window,
      int fromL, int toL,
      int fromR, int toR
  ) {
    char[] chars1 = document1.getChars(fromL, toL);
    char[] chars2 = document2.getChars(fromR, toR);
    int[] intervals1 = makeIntervals(document1, fromL, toL, false);
    int[] intervals2 = makeIntervals(document2, fromR, toR, false);
    int[] syncL = new int[]{}, syncR = new int[]{};

    window.sendToWorker(true,
        r -> {
          int[] reply = ((ArrayView) r[0]).ints();
          DiffInfo model = readDiffInfo(reply);
          result.accept(model);
        }, FIND_DIFFS,
        chars1, intervals1,
        chars2, intervals2,
        syncL, syncR,
        new int[] {0}
    );
  }

  public static final String asyncListDirectory = "asyncListDirectory";

  public static void listDirectory(
      DirectoryHandle handle, Consumer<Object[]> r
  ) {
    handle.read(new DirectoryHandle.Reader() {
      final ArrayList<FsItem> items = new ArrayList<>();
      @Override
      public void onDirectory(DirectoryHandle dir) {
        items.add(dir);
      }

      @Override
      public void onFile(FileHandle file) {
        items.add(file);
      }

      @Override
      public void onComplete() {
        ArrayOp.sendArrayList(items, r);
      }

      @Override
      public void onError(String error) {
        r.accept(new Object[] {error});
      }
    });
  }
}
