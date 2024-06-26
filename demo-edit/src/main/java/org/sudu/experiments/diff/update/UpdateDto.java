package org.sudu.experiments.diff.update;

import org.sudu.experiments.FsItem;
import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.parser.common.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.IdentityHashMap;

public class UpdateDto {

  public FolderDiffModel leftRoot, rightRoot;

  public Pair<FsItem, FolderDiffModel>[] leftRemainModels;
  public Pair<FsItem, FolderDiffModel>[] rightRemainModels;

  public static int[] toInts(
      FolderDiffModel leftRoot, FolderDiffModel rightRoot,
      List<CollectDto> dtoList, Object[] result
  ) {
    ArrayWriter writer = new ArrayWriter();
    writeInts(leftRoot, rightRoot, dtoList, result, writer);
    return writer.getInts();
  }

  public static void writeInts(
      FolderDiffModel leftRoot, FolderDiffModel rightRoot,
      List<CollectDto> dtoList, Object[] result,
      ArrayWriter writer
  ) {
    int length = dtoList.size();
    writer.write(length);

    IdentityHashMap<FolderDiffModel, Integer> leftModelToInt = new IdentityHashMap<>();
    IdentityHashMap<FolderDiffModel, Integer> rightModelToInt = new IdentityHashMap<>();

    for (int i = 0; i < dtoList.size(); i++) {
      var dto = dtoList.get(i);
      result[1 + i] = dto.leftItem;
      result[1 + length + i] = dto.rightItem;
      leftModelToInt.put(dto.leftModel, i);
      rightModelToInt.put(dto.rightModel, i);
    }

    FolderDiffModel.writeInts(leftRoot, writer, leftModelToInt);
    FolderDiffModel.writeInts(rightRoot, writer, rightModelToInt);
  }

  public static UpdateDto fromInts(int[] ints, Object[] result) {
    ArrayReader reader = new ArrayReader(ints);
    return fromInts(reader, result);
  }

  private static UpdateDto fromInts(ArrayReader reader, Object[] result) {
    UpdateDto dto = new UpdateDto();

    int length = reader.next();
    dto.leftRemainModels = new Pair[length];
    dto.rightRemainModels = new Pair[length];
    for (int i = 0; i < length; i++) {
      var leftItem = (FsItem) result[1 + i];
      var rightItem = (FsItem) result[1 + length + i];
      dto.leftRemainModels[i] = new Pair<>(leftItem, null);
      dto.rightRemainModels[i] = new Pair<>(rightItem, null);
    }
    dto.leftRoot = FolderDiffModel.fromInts(reader, null, dto.leftRemainModels);
    dto.rightRoot = FolderDiffModel.fromInts(reader, null, dto.rightRemainModels);

    return dto;
  }

  @Override
  public String toString() {
    return "UpdateDto{" +
        "leftRoot=" + leftRoot +
        ", rightRoot=" + rightRoot +
        ", leftRemainModels=" + Arrays.toString(leftRemainModels) +
        ", rightRemainModels=" + Arrays.toString(rightRemainModels) +
        "}";
  }
}
