package org.sudu.experiments.protocol;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;

import java.util.List;

public interface RemoteModelWriter {

  int DEPTH = 2;

  static void writeInts(
      RemoteFolderDiffModel model,
      List<String> pathList,
      FrontendTreeNode frontendNode,
      ArrayWriter writer
  ) {
    writer.write(model.flags);
    writer.write(model.childrenComparedCnt);

    writer.write(pathList.size());
    pathList.add(model.path);

    if (model.children == null) writer.write(-1);
    else if (frontendNode.children == null) {
      writer.write(model.children.length);
      for (var child: model.children)
        writeInts((RemoteFolderDiffModel) child, pathList, DEPTH, writer);
    } else {
      writer.write(model.children.length);
      for (int i = 0; i < model.children.length; i++) {
        RemoteFolderDiffModel childModel = model.child(i);
        FrontendTreeNode childNode = frontendNode.children[i];
        writeInts(childModel, pathList, childNode, writer);
      }
    }
  }

  static void writeInts(
      RemoteFolderDiffModel model,
      List<String> pathList,
      int remainDepth,
      ArrayWriter writer
  ) {
    writer.write(model.flags);
    writer.write(model.childrenComparedCnt);

    writer.write(pathList.size());
    pathList.add(model.path);

    if (model.children == null || remainDepth == 0) writer.write(-1);
    else {
      writer.write(model.children.length);
      if (model.children.length == 1)
        writeInts(model.child(0), pathList, remainDepth, writer);
      else for (var child: model.children)
        writeInts((RemoteFolderDiffModel) child, pathList, remainDepth - 1, writer);
    }
  }
}
