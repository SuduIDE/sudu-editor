package org.sudu.experiments.update;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import java.util.ArrayList;
import java.util.List;

public class UpdateDto {

  public RemoteFolderDiffModel leftRoot, rightRoot;

  public static int[] toInts(
      RemoteFolderDiffModel leftRoot,
      RemoteFolderDiffModel rightRoot,
      List<Object> result
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(leftRoot, rightRoot, result, writer);
    return writer.getInts();
  }

  public static void writeInts(
      RemoteFolderDiffModel leftRoot,
      RemoteFolderDiffModel rightRoot,
      List<Object> result,
      ArrayWriter writer
  ) {
    int pathLenPtr = writer.getPointer();
    writer.write(-1);

    ArrayList<String> paths = new ArrayList<>();
    RemoteFolderDiffModel.writeInts(leftRoot, paths, writer);
    RemoteFolderDiffModel.writeInts(rightRoot, paths, writer);

    writer.writeAtPos(pathLenPtr, paths.size());
    result.addAll(paths);
  }

  public static UpdateDto fromInts(int[] ints, Object[] result) {
    ArrayReader reader = new ArrayReader(ints);
    return fromInts(reader, result);
  }

  private static UpdateDto fromInts(ArrayReader reader, Object[] result) {
    UpdateDto dto = new UpdateDto();
    int pathLen = reader.next();

    String[] paths = new String[pathLen];
    for (int i = 0; i < pathLen; i++) paths[i] = (String) result[i + 1];

    dto.leftRoot = RemoteFolderDiffModel.fromInts(reader, paths, null);
    dto.rightRoot = RemoteFolderDiffModel.fromInts(reader, paths, null);

    return dto;
  }
}
