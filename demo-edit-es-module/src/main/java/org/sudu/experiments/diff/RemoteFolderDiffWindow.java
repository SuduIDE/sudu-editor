package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.JsIntArrayReader;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.fs.RemoteFileTreeNode;
import org.sudu.experiments.ui.fs.RemoteHandle;
import org.sudu.experiments.ui.fs.RemoteDirectoryNode;
import org.sudu.experiments.ui.fs.RemoteFileNode;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.UpdateDto;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.*;
import java.util.function.Supplier;

public class RemoteFolderDiffWindow extends ToolWindow0 {

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  RemoteDirectoryNode leftRoot, rightRoot;
  RemoteFolderDiffModel leftModel, rightModel;

  protected Channel channel;
  private final long startTime;

  private boolean updatedRoots = false;

  public RemoteFolderDiffWindow(
      EditorColorScheme theme,
      WindowManager wm,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts);
    rootView = new FolderDiffRootView(windowManager.uiContext);
    rootView.applyTheme(theme);

    leftModel = new RemoteFolderDiffModel(null, "");
    rightModel = new RemoteFolderDiffModel(null, "");
    leftRoot = new RemoteDirectoryNode(leftModel, getHandle(true, () -> leftModel));
    rightRoot = new RemoteDirectoryNode(rightModel, getHandle(false, () -> rightModel));

    rootView.left.setRoot(leftRoot);
    rootView.right.setRoot(rightRoot);

    window = createWindow(rootView);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);

    startTime = System.currentTimeMillis();

    this.channel = channel;
    this.channel.setOnMessage(this::onChannelMessage);
    JsHelper.consoleInfo("RemoteFolderDiffWindow created!");
    System.out.println("RemoteFolderDiffWindow:" + channel.toString());
  }

  protected void dispose() {
    window = null;
    rootView = null;
    leftRoot = rightRoot = null;
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    if (rootView.left == f || rootView.right == f)
      focusSave = f;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  private void update(JsArray<JSObject> jsResult) {
    Int32Array ints = jsResult.get(0).cast();
    var dto = fromJsInts(ints, jsResult);
    leftModel.update(dto.leftRoot);
    rightModel.update(dto.rightRoot);

    if (!updatedRoots) {
      updatedRoots = true;
      window.setTitle(leftRoot.name() + " <-> " + rightRoot.name());
      leftRoot.doOpen();
      rightRoot.doOpen();
    }
    window.context.window.repaint();
    updateDiffInfo();
  }

  protected void updateDiffInfo() {
    rootView.left.updateModel(leftModel);
    rootView.right.updateModel(rightModel);
    rootView.setDiffModel(DiffModelBuilder.getDiffInfo(
        rootView.left.model(),
        rootView.right.model()
    ));
  }

  private void onChannelMessage(
      JsArray<JSObject> jsResult
  ) {
    JsHelper.consoleInfo("Got update batch from channel");
    update(jsResult);
    JsHelper.consoleInfo("Updated in " + (System.currentTimeMillis() - startTime) + "ms");
  }

  private RemoteHandle getHandle(
      boolean left,
      Supplier<RemoteFolderDiffModel> modelSupplier
  ) {
    return new RemoteHandle() {
      @Override
      public void updateView() {
        updateDiffInfo();
      }

      @Override
      public void openDir(RemoteDirectoryNode node) {
        var model = getModel();
        if (model.children == null) return;

        int foldersLen = 0;
        int childLen = model.children.length;

        var children = new RemoteFileTreeNode[childLen];
        int childPtr = 0;

        for (int i = 0; i < childLen; i++) {
          RemoteFileTreeNode childNode;
          var child = model.child(i);
          if (child.isFile()) {
            childNode = new RemoteFileNode(child, getHandle(left, getModel(i)));
          } else {
            foldersLen++;
            childNode = new RemoteDirectoryNode(child, getHandle(left, getModel(i)));
          }
          children = ArrayOp.addAt(childNode, children, childPtr++);
        }
        node.setChildren(children);
        node.folderCnt = foldersLen;

        var array = JsArray.create();
        array.push(JSString.valueOf("Opened: " + node.value()));
        channel.sendMessage(array);
      }

      @Override
      public void openFile(RemoteFileNode node) {
        JsHelper.consoleInfo("Trying to open file " + node.name());
        var opposite = getOppositeFile(node);
        if (opposite != null) setSelected(node, opposite);
        var array = JsArray.create();
        array.push(JSString.valueOf("Opened: " + node.value()));
        channel.sendMessage(array);
      }

      @Override
      public void closeDir(RemoteDirectoryNode node) {
        super.closeDir(node);
        var array = JsArray.create();
        array.push(JSString.valueOf("Closed: " + node.value()));
        channel.sendMessage(array);
      }

      @Override
      public RemoteDirectoryNode getOppositeDir(RemoteDirectoryNode node) {
        var opposite = getOppositeDir(node.model());
        if (opposite != null) setSelected(node, opposite);
        return opposite;
      }

      @Override
      public RemoteFileNode getOppositeFile(RemoteFileNode node) {
        var dir = getOppositeDir(node.model().parent());
        if (dir == null) return null;
        return dir.findSubFile(node.name());
      }

      @Override
      public RemoteFolderDiffModel getModel() {
        return modelSupplier.get();
      }

      private Supplier<RemoteFolderDiffModel> getModel(int i) {
        return () -> getModel().child(i);
      }

      private RemoteDirectoryNode getOppositeDir(RemoteFolderDiffModel model) {
        var root = left ? rightRoot : leftRoot;
        Deque<String> deque = new LinkedList<>();
        var curModel = model;
        while (curModel != null) {
          deque.addFirst(curModel.path);
          curModel = curModel.parent();
        }
        deque.removeFirst();
        return getOpposite(root, deque);
      }

      private RemoteDirectoryNode getOpposite(RemoteDirectoryNode current, Deque<String> deque) {
        if (deque.isEmpty()) return current;
        var path = deque.removeFirst();
        var subNode = current.findSubDir(path);
        if (subNode == null) return null;
        else return getOpposite(subNode, deque);
      }

      private void setSelected(RemoteFileTreeNode node, RemoteFileTreeNode opposite) {
        if (left) {
          rootView.left.setSelected(node);
          rootView.right.setSelected(opposite);
        } else {
          rootView.right.setSelected(node);
          rootView.left.setSelected(opposite);
        }
      }
    };
  }

  private static UpdateDto fromJsInts(Int32Array ints, JsArray<JSObject> result) {
    JsIntArrayReader reader = new JsIntArrayReader(ints);
    return fromJsInts(reader, result);
  }

  private static UpdateDto fromJsInts(JsIntArrayReader reader, JsArray<JSObject> result) {
    UpdateDto dto = new UpdateDto();
    int pathLen = reader.next();

    String[] paths = new String[pathLen];
    for (int i = 0; i < pathLen; i++) paths[i] = ((JSString) result.get(i + 1)).stringValue();

    dto.leftRoot = RemoteFolderDiffModel.fromInts(reader, paths, null);
    dto.rightRoot = RemoteFolderDiffModel.fromInts(reader, paths, null);

    return dto;
  }
}
