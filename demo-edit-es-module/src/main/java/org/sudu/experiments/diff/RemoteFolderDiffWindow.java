package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.ui.FileTreeNode;
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

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Supplier;

public class RemoteFolderDiffWindow extends ToolWindow0 {

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  RemoteDirectoryNode leftRoot, rightRoot;
  RemoteFolderDiffModel leftModel, rightModel;

  protected Channel channel;

  public RemoteFolderDiffWindow(
      EditorColorScheme theme,
      WindowManager wm,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts);
    rootView = new FolderDiffRootView(windowManager.uiContext);
    rootView.applyTheme(theme);
    var modelLeft = new FileTreeNode(UiText.empty, 0);
    var modelRight = new FileTreeNode(UiText.empty, 0);
    modelLeft.iconFolderOpened();
    modelRight.iconFolderOpened();

    rootView.left.setRoot(modelLeft);
    rootView.right.setRoot(modelRight);
    window = createWindow(rootView);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);
    leftModel = RemoteFolderDiffModel.REMOTE_DEFAULT;
    rightModel = RemoteFolderDiffModel.REMOTE_DEFAULT;
    this.channel = channel;
    this.channel.setOnMessage(this::onChannelMessage);
    JsHelper.consoleInfo("RemoteFolderDiffWindow created!");
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

  private void setRoots(
      RemoteFolderDiffModel leftModel,
      RemoteFolderDiffModel rightModel
  ) {
    this.leftRoot = new RemoteDirectoryNode(leftModel, getHandle(true));
    this.rightRoot = new RemoteDirectoryNode(rightModel, getHandle(false));

    rootView.left.setRoot(leftRoot);
    rootView.right.setRoot(rightRoot);
    window.setTitle(leftRoot.name() + " <-> " + rightRoot.name());

    leftRoot.doOpen();
    rightRoot.doOpen();
    window.context.window.repaint();
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
    var ints = JsMemoryAccess.toJavaArray((Int32Array) jsResult.get(0).cast());

    Object[] result = new Object[jsResult.getLength()];
    result[0] = ints;

    for (int i = 1; i < result.length; i++) {
      JSString path = jsResult.get(i).cast();
      result[i] = path.stringValue();
    }

    var dto = UpdateDto.fromInts(ints, result);
    leftModel = dto.leftRoot;
    rightModel = dto.rightRoot;
    setRoots(leftModel, rightModel);
  }

  private RemoteHandle getHandle(boolean left) {
    return new RemoteHandle() {
      @Override
      public void updateView() {
        updateDiffInfo();
      }

      @Override
      public void openFile(RemoteFileNode node) {
        JsHelper.consoleInfo("Trying to open file " + node.name());
        var opposite = getOppositeFile(node);
        if (opposite != null) setSelected(node, opposite);
      }

      @Override
      public RemoteDirectoryNode getOppositeDir(RemoteDirectoryNode node) {
        var opposite = getOppositeDir(node.model);
        if (opposite != null) setSelected(node, opposite);
        return opposite;
      }

      @Override
      public RemoteFileNode getOppositeFile(RemoteFileNode node) {
        var dir = getOppositeDir(node.model.parent());
        if (dir == null) return null;
        return dir.findSubFile(node.name());
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
}
