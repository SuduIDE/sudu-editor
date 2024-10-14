package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.Subscribers;
import org.sudu.experiments.diff.folder.FolderDiffSide;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.esm.JsExternalFileOpener;
import org.sudu.experiments.esm.dlg.FsDialogs;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.protocol.BackendMessage;
import org.sudu.experiments.protocol.FrontendMessage;
import org.sudu.experiments.protocol.FrontendState;
import org.sudu.experiments.protocol.JsCast;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.fs.RemoteDirectoryNode;
import org.sudu.experiments.ui.fs.RemoteFileNode;
import org.sudu.experiments.ui.fs.RemoteFileTreeNode;
import org.sudu.experiments.ui.fs.RemoteHandle;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Performance;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;

import java.util.*;
import java.util.function.*;

public class RemoteFolderDiffWindow extends ToolWindow0 {

  static boolean debug;

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  RemoteDirectoryNode leftRoot, rightRoot;
  RemoteFolderDiffModel rootModel;

  protected Channel channel;
  private final double startTime;

  private boolean updatedRoots = false;
  boolean finished = false;

  String searchString = "*";

  private final int MAP_SIZE = 100;
  private final Consumer<String>[] openFileMap = new Consumer[MAP_SIZE];
  private int keyCnt = 0;

  final ArrayList<ActiveWindow> windows = new ArrayList<>();

  final JsFolderDiffController0 controller;

  final Subscribers<ViewEventListener> controllerListeners =
      new Subscribers<>(new ViewEventListener[0]);

  JsExternalFileOpener opener;
  JsDialogProvider dialogProvider;

  public RemoteFolderDiffWindow(
      EditorColorScheme theme,
      WindowManager wm,
      Supplier<String[]> fonts,
      Channel channel
  ) {
    super(wm, theme, fonts);
    rootView = new FolderDiffRootView(windowManager.uiContext);
    rootView.applyTheme(theme);

    rootModel = new RemoteFolderDiffModel(null, "");
    leftRoot = new RemoteDirectoryNode("", getHandle(true, () -> rootModel), 0);
    rightRoot = new RemoteDirectoryNode("", getHandle(false, () -> rootModel), 0);

    rootView.left.setRoot(leftRoot);
    rootView.right.setRoot(rightRoot);
    rootView.right.clearSelection();

    window = createWindow(rootView);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);

    startTime = Performance.now();

    this.channel = channel;
    this.channel.setOnMessage(this::onChannelMessage);

    rootView.left.setOnSelectedLineChanged(this::leftSelectedChanged);
    rootView.right.setOnSelectedLineChanged(this::rightSelectedChanged);

    controller = new JsFolderDiffController0(this);
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
    for (ActiveWindow r : windows) {
      r.window.applyTheme(theme);
    }
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
    fireControllerEvent(controller);
  }

  private void openFile(JsArray<JSObject> jsResult) {
    int key = ((Int32Array) jsResult.pop()).get(0);
    var openFileRun = openFileMap[key];
    if (openFileRun != null && jsResult.getLength() != 0) {
      String source = ((JSString) jsResult.get(0)).stringValue();
      openFileRun.accept(source);
      openFileMap[key] = null;
    }
  }

  private void update(JsArray<JSObject> jsResult) {
    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);

    if (!updatedRoots) {
      updatedRoots = true;
      leftRoot.setLine(replaceSlashes(msg.leftRootName));
      rightRoot.setLine(replaceSlashes(msg.rightRootName));
      window.setTitle(msg.leftRootName + " <-> " + msg.rightRootName);
      leftRoot.doOpen();
      rightRoot.doOpen();
      sendFrontendModel();
    }
    window.context.window.repaint();
    updateDiffInfo();
    if (rootModel.isCompared()) {
      finished = true;
      LoggingJs.info("RemoteFolderDiff finished");
      rootView.fireFinished();
    }
  }

  private void onDiffApplied(JsArray<JSObject> jsResult) {
    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);
    updateNodes(leftRoot, rightRoot, rootModel);
    updateDiffInfo();
  }

  private void updateNodes(
      RemoteDirectoryNode left,
      RemoteDirectoryNode right,
      RemoteFolderDiffModel model
  ) {
    HashSet<String> opened = new HashSet<>();
    for (var child: left.getChildren()) {
      if (!child.isOpened()) continue;
      opened.add(child.name());
    }
    for (var child: right.getChildren()) {
      if (!child.isOpened()) continue;
      opened.add(child.name());
    }
    var leftChildren = new RemoteFileTreeNode[1];
    var rightChildren = new RemoteFileTreeNode[1];
    int lp = 0, rp = 0;
    int leftFolderCnt = 0;
    int rightFolderCnt = 0;
    for (int i = 0; i < model.children.length; i++) {
      var child = model.child(i);
      String path = child.path;
      boolean isFolder = !child.isFile();
      boolean isOpened = opened.contains(path);
      if (child.isLeft()) {
        if (isFolder) leftFolderCnt++;
        leftChildren[lp] = findUpdateNode(left, path, isFolder, isOpened);
        leftChildren[lp++].setHandle(getHandle(true, childModel(left.getModelSupplier(), i)));
      }
      if (child.isRight()) {
        if (isFolder) rightFolderCnt++;
        rightChildren[rp] = findUpdateNode(right, path, isFolder, isOpened);
        rightChildren[rp++].setHandle(getHandle(false, childModel(right.getModelSupplier(), i)));
      }
      if (!isOpened) continue;
      if (child.isBoth()) {
        var leftChild = leftChildren[lp - 1];
        var rightChild = rightChildren[rp - 1];
        if (leftChild instanceof RemoteDirectoryNode leftDir &&
            rightChild instanceof RemoteDirectoryNode rightDir
        ) updateNodes(leftDir, rightDir, child);
      } else if (child.isLeft()) {
        var leftChild = leftChildren[lp - 1];
        if (leftChild instanceof RemoteDirectoryNode leftDir) updateNode(leftDir, child, true);
      } else {
        var rightChild = rightChildren[rp - 1];
        if (rightChild instanceof RemoteDirectoryNode rightDir) updateNode(rightDir, child, false);
      }
    }
    left.setChildren(Arrays.copyOf(leftChildren, lp));
    left.folderCnt = leftFolderCnt;
    right.setChildren(Arrays.copyOf(rightChildren, rp));
    right.folderCnt = rightFolderCnt;
  }

  private void updateNode(
      RemoteDirectoryNode node,
      RemoteFolderDiffModel model,
      boolean left
  ) {
    HashSet<String> opened = new HashSet<>();
    for (var child: node.getChildren()) {
      if (!child.isOpened()) continue;
      opened.add(child.name());
    }
    var children = new RemoteFileTreeNode[1];
    int p = 0;
    int folderCnt = 0;
    for (int i = 0; i < model.children.length; i++) {
      var child = model.child(i);
      String path = child.path;
      boolean isFolder = !model.isFile();
      boolean isOpened = opened.contains(path);
      if (isFolder) folderCnt++;
      children[p] = findUpdateNode(node, path, isFolder, isOpened);
      children[p++].setHandle(getHandle(left, childModel(node.getModelSupplier(), i)));
      if (isOpened) continue;
      if (children[p - 1] instanceof RemoteDirectoryNode dirNode) updateNode(dirNode, child, left);
    }
    children = Arrays.copyOf(children, p);
    node.folderCnt = folderCnt;
    node.setChildren(children);
  }

  private static RemoteFileTreeNode findUpdateNode(
      RemoteDirectoryNode node,
      String path,
      boolean isFolder,
      boolean isOpened
  ) {
    var leftNode = node.findSubItem(path, isFolder);
    var updNode =  leftNode != null
        ? leftNode
        : isFolder
        ? new RemoteDirectoryNode(path, null, node.depth + 1)
        : new RemoteFileNode(path, null, node.depth + 1);
    if (isOpened) updNode.open();
    return updNode;
  }

  protected void updateDiffInfo() {
    rootView.left.updateModel(rootModel, rightRoot, FolderDiffSide.LEFT);
    rootView.right.updateModel(rootModel, leftRoot, FolderDiffSide.RIGHT);
    rootView.setDiffModel(DiffModelBuilder.getDiffInfo(
        rootView.left.model(),
        rootView.right.model()
    ));
    rootView.setMergeButtons(this::askApplyDiff);
    window.context.window.repaint();
  }

  private void onChannelMessage(JsArray<JSObject> jsResult) {
    Int32Array array = jsResult.pop().cast();
    switch (array.get(0)) {
      case DiffModelChannelUpdater.FRONTEND_MESSAGE -> update(jsResult);
      case DiffModelChannelUpdater.OPEN_FILE -> openFile(jsResult);
      case DiffModelChannelUpdater.APPLY_DIFF -> onDiffApplied(jsResult);
    }
    LoggingJs.trace(
        "Got message in " + Numbers.iRnd(Performance.now() - startTime) + "ms"
    );
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
        var model = node.model();
        if (model.children == null) return;

        int foldersLen = 0;

        var children = new RemoteFileTreeNode[1];
        int childPtr = 0;

        int side = left ? FolderDiffSide.LEFT : FolderDiffSide.RIGHT;
        int mP = model.nextInd(0, side);
        while (mP >= 0) {
          RemoteFileTreeNode childNode;
          var child = model.child(mP);
          var handle = getHandle(left, childModel(modelSupplier, mP));
          if (child.isFile()) {
            childNode = new RemoteFileNode(child.path, handle, node.depth + 1);
          } else {
            foldersLen++;
            childNode = new RemoteDirectoryNode(child.path, handle, node.depth + 1);
          }
          childNode.posInParent = childPtr;
          children = ArrayOp.addAt(childNode, children, childPtr++);
          mP = model.nextInd(mP + 1, side);
        }
        children = Arrays.copyOf(children, childPtr);
        node.setChildren(children);
        node.folderCnt = foldersLen;
      }

      @Override
      public void openFile(RemoteFileNode node) {
        var opposite = getOppositeFile(node);
        if (opposite != null) {
          newCodeDiff(node, opposite, left);
        } else {
          newEditor(node, left);
        }
      }

      @Override
      public void sendModel() {
        sendFrontendModel();
      }

      @Override
      public void closeDir(RemoteDirectoryNode node) {
        super.closeDir(node);
      }

      @Override
      public RemoteDirectoryNode getOppositeDir(RemoteDirectoryNode node) {
        return getOppositeDir(node.model());
      }

      @Override
      public RemoteFileNode getOppositeFile(RemoteFileNode node) {
        var dir = getOppositeDir(node.model().parent());
        if (dir == null) return null;
        return dir.findSubFile(node.name());
      }

      @Override
      public Supplier<RemoteFolderDiffModel> getModelSupplier() {
        return modelSupplier;
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
    };
  }

  private static Supplier<RemoteFolderDiffModel> childModel(Supplier<RemoteFolderDiffModel> parent, int i) {
    return () -> parent.get().child(i);
  }

  private void newEditor(RemoteFileNode node, boolean left) {
    if (opener == null) {
      var window = new EditorWindow(windowManager, theme, fonts);
      window.onControllerEvent(this::onWindowEvent);
      addWindow(window, new JsEditorViewController0());
      openFileMap[keyCnt] = (source) -> window.open(source, node.name());
      sendOpenFile(node, left);
      Consumer<String> onDiffMade = (src) -> fileDiffMade(node.model(), left, src);
      window.setDiffMade(onDiffMade);
      window.maximize();
      window.setReadonly(left ? rootView.leftReadonly : rootView.rightReadonly);
      onWindowEvent(window);
    } else {
      var path = getFullPath(node, left);
      opener.openEditor(JSString.valueOf(path));
    }
  }

  private void newCodeDiff(RemoteFileNode node, RemoteFileNode opposite, boolean left) {
    if (opener == null) {
      var window = new FileDiffWindow(windowManager, theme, fonts);
      window.onEvent = this::onWindowEvent;
      addWindow(window, new JsFileDiffViewController0(window));
      openFileMap[keyCnt] = (source) -> window.open(source, node.name(), left);
      sendOpenFile(node, left);
      openFileMap[keyCnt] = (source) -> window.open(source, opposite.name(), !left);
      sendOpenFile(opposite, !left);
      Consumer<String> onLeftDiff = left
          ? (src) -> fileDiffMade(node.model(), true, src)
          : (src) -> fileDiffMade(opposite.model(), true, src);
      Consumer<String> onRightDiff = !left
          ? (src) -> fileDiffMade(node.model(), false, src)
          : (src) -> fileDiffMade(opposite.model(), false, src);
      window.setOnDiffMade(onLeftDiff, onRightDiff);
      window.rootView.setReadonly(rootView.leftReadonly, rootView.rightReadonly);
      window.window.maximize();
      onWindowEvent(window);
    } else {
      var lPath = left ? getFullPath(node, true) : getFullPath(opposite, true);
      var rPath = !left ? getFullPath(node, false) : getFullPath(opposite, false);
      opener.openFileDiff(JSString.valueOf(lPath), JSString.valueOf(rPath));
    }
  }

  private String getFullPath(RemoteFileNode node, boolean left) {
    return node.getFullPath(left ? leftRoot.name() : rightRoot.name());
  }

  JsViewController find(ToolWindow0 w) {
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, size = windows.size(); i < size; i++) {
      ActiveWindow r = windows.get(i);
      if (r.window == w)
        return r.controller;
    }
    return null;
  }

  private void onWindowEvent(ToolWindow0 diffWindow) {
    if (debug) LoggingJs.debug(
        "onWindowEvent: " + diffWindow.getClass().getSimpleName());

    var controller = find(diffWindow);
    fireControllerEvent(controller);
  }

  private void onWindowClosed(ActiveWindow r) {
    if (debug) LoggingJs.debug(
        "RemoteFolderDiffWindow.onWindowClosed: "
            + r.window.getClass().getSimpleName());
    windows.remove(r);
    if (r.window instanceof FileDiffWindow fileDiff) {
      if (debug) LoggingJs.debug(
          "closed fileDiff = " + fileDiff);
    } else if (r.window instanceof EditorWindow editor) {
      if (debug) LoggingJs.debug(
          "closed editor = " + editor);
    }
  }

  void addWindow(ToolWindow0 window, JsViewController c) {
    ActiveWindow r = new ActiveWindow(window, c);
    windows.add(r);
    window.setOnClose(() -> onWindowClosed(r));
  }

  private void sendFrontendModel() {
    var result = FrontendMessage.serialize(
        leftRoot,
        rightRoot,
        rootModel,
        searchString
    );
    result.push(DiffModelChannelUpdater.FRONTEND_MESSAGE_ARRAY);
    channel.sendMessage(result);
  }

  private void sendOpenFile(RemoteFileNode node, boolean left) {
    var result = JsArray.create();
    var path = getFullPath(node, left);
    result.push(JsMemoryAccess.bufferView(new int[]{keyCnt}));
    result.push(JSString.valueOf(path));
    result.push(DiffModelChannelUpdater.OPEN_FILE_ARRAY);
    channel.sendMessage(result);
    keyCnt = (keyCnt + 1) % MAP_SIZE;
  }

  private void askApplyDiff(FolderDiffModel model, boolean left) {
    if (dialogProvider != null) {
      RemoteFolderDiffModel remoteModel = (RemoteFolderDiffModel) model;
      String fromPath = remoteModel.getFullPath(left ? leftRoot.name() : rightRoot.name());
      String toPath = remoteModel.getFullPath(!left ? leftRoot.name() : rightRoot.name());
      FsDialogs.showDlg(dialogProvider, fromPath, toPath,
          () -> sendApplyDiff(model, left));
    } else {
      sendApplyDiff(model, left);
    }
  }

  private void sendApplyDiff(FolderDiffModel model, boolean left) {
    int[] path = model.getPathFromRoot();
    var result = JsArray.create();
    result.set(0, JsCast.jsInts(path));
    result.set(1, JsCast.jsInts(left ? 0 : 1));
    result.push(DiffModelChannelUpdater.APPLY_DIFF_ARRAY);
    channel.sendMessage(result);
  }

  void fileDiffMade(FolderDiffModel model, boolean left, String source) {
    int[] path = model.getPathFromRoot();
    var result = JsArray.create();
    result.set(0, JsCast.jsInts(path));
    result.set(1, JsCast.jsInts(left ? 0 : 1));
    result.set(2, JsCast.jsString(source));
    result.push(DiffModelChannelUpdater.FILE_SAVE_ARRAY);
    channel.sendMessage(result);
  }

  private void serializeFrontendState() {
    int leftSelected = rootView.left.selectedIndex();
    int rightSelected = rootView.right.selectedIndex();
    JsArray<JSObject> serialized = FrontendState.serialize(
        0,
        leftSelected,
        rightSelected,
        leftRoot,
        rightRoot,
        rootModel,
        searchString
    );
    FrontendState state = FrontendState.deserialize(serialized);
    System.out.println(state);
  }

  public FolderDiffSelection getSelected() {
    return getSelected(rootView.left.isFocused());
  }

  public FolderDiffSelection getSelected(boolean left) {
    var root = left ? rootView.left : rootView.right;
    if (root.selectedIndex() < 0) return null;
    var node = root.model()[root.selectedIndex()];
    if (node.isEmpty()) return new FolderDiffSelection(null, left, false, false);

    String path = ((RemoteFileTreeNode) node).getRelativePath();
    boolean isFolder = node instanceof RemoteDirectoryNode;
    int diffType = ((RemoteFileTreeNode) node).model().getDiffType();
    boolean isOrphan = diffType == DiffTypes.INSERTED || diffType == DiffTypes.DELETED;
    return new FolderDiffSelection(path, left, isFolder, isOrphan);
  }

  void leftSelectedChanged(int idx) {
    if (idx >= 0) {
      rootView.right.checkScroll(idx);
      rootView.right.clearSelection();
    }
    rootView.fireSelectionChanged(getSelected(true));
    fireControllerEvent(controller);
  }

  void rightSelectedChanged(int idx) {
    if (idx >= 0) {
      rootView.left.checkScroll(idx);
      rootView.left.clearSelection();
    }
    rootView.fireSelectionChanged(getSelected(false));
    fireControllerEvent(controller);
  }

  String replaceSlashes(String path) {
    int slInd = path.indexOf('/');
    if (slInd == -1) return path;
    int revSlInd = path.indexOf('\\');
    if (revSlInd == -1) return path;
    char delim = slInd < revSlInd ? '/' : '\\';
    return path.replace('/', delim).replace('\\', delim);
  }

  @Override
  protected boolean onContextMenu(V2i pos) {
    if (rootView.left.hitTest(pos))
      return rootView.left.onContextMenu(pos);
    if (rootView.right.hitTest(pos))
      return rootView.right.onContextMenu(pos);
    return false;
  }

  private FileTreeView getFocused() {
    return rootView.left.isFocused()
        ? rootView.left
        : rootView.right.isFocused()
        ? rootView.right
        : null;
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    rootView.setReadonly(leftReadonly, rightReadonly);
  }

  public boolean canNavigateUp() {
    return canNavigate(true);
  }

  public boolean canNavigateDown() {
    return canNavigate(false);
  }

  private boolean canNavigate(boolean up) {
    var focused = getFocused();
    if (focused == null) return false;
    int selectedInd = focused.selectedIndex();
    if (selectedInd < 0) return false;
    var diffInfo = rootView.diffSync.model;
    boolean left = focused == rootView.left;
    var lines = left ? diffInfo.lineDiffsL : diffInfo.lineDiffsR;
    if (up) {
      for (int i = selectedInd - 1; i >= 0; i--) {
        if (lines[i].type != DiffTypes.DEFAULT) return true;
      }
    } else {
      for (int i = selectedInd + 1; i < lines.length; i++) {
        if (lines[i].type != DiffTypes.DEFAULT) return true;
      }
    }
    return false;
  }

  public void navigateUp() {
    var focused = getFocused();
    if (focused == null) return;
    int selectedIndex = getNavigateInd(focused, true);
    if (selectedIndex < 0) return;
    focused.setSelectedIndex(selectedIndex);
  }

  public void navigateDown() {
    var focused = getFocused();
    if (focused == null) return;
    int selectedIndex = getNavigateInd(focused, false);
    if (selectedIndex < 0) return;
    focused.setSelectedIndex(selectedIndex);
  }

  private int getNavigateInd(FileTreeView focused, boolean up) {
    int selectedInd = focused.selectedIndex();
    if (selectedInd < 0) return -1;
    var diffInfo = rootView.diffSync.model;
    boolean left = focused == rootView.left;
    var lines = left ? diffInfo.lineDiffsL : diffInfo.lineDiffsR;
    if (up) {
      for (int i = selectedInd; i >= 0; i--) {
        if (lines[i].type != DiffTypes.DEFAULT) return i;
      }
    } else {
      for (int i = selectedInd; i < lines.length; i++) {
        if (lines[i].type != DiffTypes.DEFAULT) return i;
      }
    }
    return -1;
  }

  public void applyDiffFilter(int[] filters) {
    // TODO
  }

  public int[] getDiffFilter() {
    // TODO
    return new int[0];
  }

  public void refresh() {
    updatedRoots = false;
    var result = JsArray.create();
    result.push(DiffModelChannelUpdater.REFRESH_ARRAY);
    channel.sendMessage(result);
  }

  void fireControllerEvent(JsViewController source) {
    var list = controllerListeners.array();
    if (debug) LoggingJs.debug(
        "fireControllerEvent: " + list.length + " listeners");
    for (var listener : list) {
      listener.onEvent(source);
    }
  }

  static class ActiveWindow {
    ToolWindow0 window;
    JsViewController controller;

    ActiveWindow(ToolWindow0 window, JsViewController controller) {
      this.window = window;
      this.controller = controller;
    }
  }
}
