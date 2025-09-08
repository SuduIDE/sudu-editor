package org.sudu.experiments.diff;

import org.sudu.experiments.Channel;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.Subscribers;
import org.sudu.experiments.diff.folder.FolderDiffSide;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.esm.JsDialogProvider;
import org.sudu.experiments.esm.JsExternalFileOpener;
import org.sudu.experiments.esm.JsExternalMessageBar;
import org.sudu.experiments.esm.dlg.FsDialogs;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.protocol.*;
import org.sudu.experiments.ui.*;
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

import static org.sudu.experiments.diff.RemoteFileDiffWindow.sSuffix;

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

  private final TreeMap<Integer, BiConsumer<String, String>>
      openFileMap = new TreeMap<>();

  private int keyCnt = 0;

  final ArrayList<ActiveWindow> windows = new ArrayList<>();

  final JsFolderDiffController0 controller;

  final Subscribers<ViewEventListener> controllerListeners =
      new Subscribers<>(new ViewEventListener[0]);

  JsExternalFileOpener opener;
  JsDialogProvider dialogProvider;
  JsExternalMessageBar messageBar;

  private int[] lastFilters = null;
  private RemoteFolderDiffModel lastSelected;
  private boolean isLastLeftFocused = false;
  private boolean isRefresh = false;
  private FrontendMessage lastFrontendMessage = FrontendMessage.empty();

  private String leftRootPath, rightRootPath;

  private boolean disableParser;

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
    focusSave = rootView.left;

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
    LoggingJs.info("RemoteFolderDiffWindow created");
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
    for (ActiveWindow r: windows) {
      r.window.applyTheme(theme);
    }
  }

  protected void dispose() {
    LoggingJs.info("RemoteFolderDiffWindow.dispose");
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
    boolean success = ((Int32Array) jsResult.pop()).get(0) == 1;
    var openFileRun = openFileMap.remove(key);
    if (success && openFileRun != null && jsResult.getLength() != 0) {
      String source = JsCast.string(jsResult, 0);
      String encoding = JsCast.string(jsResult, 1);
      openFileRun.accept(source, encoding);
    } else {
      LoggingJs.error("error in openFile, key = " + key);
    }
  }

  private void update(JsArray<JSObject> jsResult) {
    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);
    if (isFiltered() || msg.rootReplaced || isRefresh) {
      isRefresh = false;
      updateNodes();
    }
    if (!updatedRoots || msg.rootReplaced) {
      LoggingJs.info("Init RemoteFolderDiff roots: " + rootModel.recToString());
      updatedRoots = true;
      leftRoot.setLine(replaceSlashes(msg.leftRootName));
      rightRoot.setLine(replaceSlashes(msg.rightRootName));
      leftRootPath = msg.leftRootPath;
      rightRootPath = msg.rightRootPath;
      window.setTitle(msg.leftRootName + " <-> " + msg.rightRootName);
      if (!leftRoot.isOpened() || !rightRoot.isOpened()) {
        leftRoot.doOpen();
        rightRoot.doOpen();
        openFrontendNode(rootModel);
      }
      sendFrontendModel();
    }
    if (!isFiltered()) updateDiffInfo();
    if (rootModel.isCompared()) {
      finished = true;
      rootView.fireFinished();
    }
    setStatMessages(msg);
  }

  private void setStatMessages(BackendMessage msg) {
    String statusBarMsg = mkStatusBarMsg(msg);
    JSString toolBarMsg = JSString.valueOf(mkToolBarMsg(msg));
    LoggingJs.trace("Status: " + statusBarMsg + " " + mkTimeMsg(msg.timeDelta));
    if (messageBar != null) {
      messageBar.setStatusBarMessage(JSString.valueOf(statusBarMsg));
      messageBar.setToolBarMessage(toolBarMsg);
    }
    if (!isFiltered()) lastFrontendMessage.openedFolders.updateDeepWithModel(rootModel);
  }

  private String mkStatusBarMsg(BackendMessage msg) {
    return "Compared " +
        msg.foldersCmp + " folder" + sSuffix(msg.foldersCmp) + ", " +
        msg.filesCmp + " file" + sSuffix(msg.filesCmp) +
        (rootModel.isCompared() ? "" : " (in progress)");
  }

  private String mkToolBarMsg(BackendMessage msg) {
    String text;
    if (lastFilters == null) {
      text = "different";
    } else {
      // Inserted and deleted are mixed up in filters
      boolean leftOnly = ArrayOp.contains(lastFilters, DiffTypes.INSERTED);
      boolean rightOnly = ArrayOp.contains(lastFilters, DiffTypes.DELETED);
      boolean modified = ArrayOp.contains(lastFilters, DiffTypes.EDITED);
      if (leftOnly) {
        if (rightOnly) {
          text = modified ? "different" : "left or right only";
        } else {
          text = modified ? "modified or left only" : "left only";
        }
      } else {
        if (rightOnly) {
          text = modified ? "modified or right only" : "right only";
        } else {
          text = modified ? "modified" : null;
        }
      }
    }
    if (text == null) return "";

    return switch (msg.differentFiles) {
      case 0 -> "No " + text + " files";
      case 1 -> "1 " + text + " file";
      default -> msg.differentFiles + " " + text + " files";
    };
  }

  private String mkTimeMsg(int ms) {
    if (ms < 1000) return ms + " ms";
    int sec = ms / 1000;
    int rest;
    if (sec >= 100 || (rest = (ms % 1000) / 100) == 0) return sec + " sec";
    return sec + "." + rest + " sec";
  }

  private void onDiffApplied(JsArray<JSObject> jsResult) {
    LoggingJs.info("RemoteFolderDiffWindow.onDiffApplied");
    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);
    setStatMessages(msg);
    if (!isFiltered()) lastFrontendMessage.openedFolders.updateDeepWithModel(rootModel);
    updateNodes();
  }

  private void onFiltersApplied(JsArray<JSObject> jsResult) {
    var root = isLastLeftFocused ? rootView.left : rootView.right;
    int delta = root.getSelectedIndexDelta();
    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);
    LoggingJs.info(lastFrontendMessage.toString());
    updateNodes(leftRoot, rightRoot, rootModel, lastFrontendMessage.openedFolders);
    updateDiffInfo();
    setStatMessages(msg);
    if (lastSelected != null) {
      var selectedRoot = isLastLeftFocused ? leftRoot : rightRoot;
      TreeNode newSelected = selectedRoot.getNearestParent(lastSelected);
      if (newSelected != null) {
        root.updateSelected(newSelected, delta);
        rootView.left.setScrollPosY(root.scrollPos.y);
        rootView.right.setScrollPosY(root.scrollPos.y);
      }
      lastSelected = null;
    }
  }

  private void onRefresh(JsArray<JSObject> jsResult) {
    var msg = BackendMessage.deserialize(jsResult);
    lastFrontendMessage.openedFolders.updateDeepWithModel(msg.root);
    setStatMessages(msg);
    isRefresh = true;
  }

  private void onNavigate(JsArray<JSObject> jsResult) {
    int[] filteredPath = JsCast.ints(jsResult.pop());
    boolean left = JsCast.ints(jsResult.pop())[0] == 1;
    LoggingJs.debug("RemoteFolderDiffWindow.onNavigate: " + "filteredPath = " + Arrays.toString(filteredPath));

    var msg = BackendMessage.deserialize(jsResult);
    rootModel.update(msg.root);
    var navigatedPair = navigatedNodes(leftRoot, rightRoot, rootModel, filteredPath, 0);
    LoggingJs.debug("RemoteFolderDiffWindow.onNavigate: " + "navigatedPair = " + navigatedPair);
    updateDiffInfo();
    if (navigatedPair.first != null) {
      rootView.left.setSelected(navigatedPair.first);
      rootView.right.setSelectedIndex(rootView.left.selectedIndex());
    } else {
      rootView.right.setSelected(navigatedPair.second);
      rootView.left.setSelectedIndex(rootView.right.selectedIndex());
    }
    rootView.left.checkScroll(rootView.left.selectedIndex());
    rootView.right.checkScroll(rootView.right.selectedIndex());
  }

  private void updateNodes() {
    var focusedRoot = getFocused();
    var selected = focusedRoot.selectedLine();
    int delta = focusedRoot.getSelectedIndexDelta();
    var selectedRoot = focusedRoot == rootView.left ? leftRoot : rightRoot;
    updateNodes(leftRoot, rightRoot, rootModel, lastFrontendMessage.openedFolders);
    updateDiffInfo();
    if (selected instanceof RemoteFileTreeNode remoteSelected) {
      TreeNode newSelected = selectedRoot.getNearestParent(remoteSelected.model());
      if (newSelected != null) {
        focusedRoot.updateSelected(newSelected, delta);
        rootView.left.setScrollPosY(focusedRoot.scrollPos.y);
        rootView.right.setScrollPosY(focusedRoot.scrollPos.y);
      }
    }
  }

  private void updateNodes(
      RemoteDirectoryNode left,
      RemoteDirectoryNode right,
      RemoteFolderDiffModel model,
      FrontendTreeNode treeNode
  ) {
    if (model == null || model.children == null ||
        treeNode == null || !treeNode.isOpened()
    ) return;
    left.doOpen();
    right.doOpen();

    if (treeNode.children.length != model.children.length) treeNode.updateWithModel(model);
    int lp = 0, rp = 0;
    for (int i = 0; i < model.children.length; i++) {
      var child = model.child(i);
      var childNode = treeNode.child(i, child.path, child.isFile());
      if (child.isBoth()) {
        var leftChild = left.child(lp++);
        var rightChild = right.child(rp++);
        if (leftChild instanceof RemoteDirectoryNode leftDir &&
            rightChild instanceof RemoteDirectoryNode rightDir
        ) updateNodes(leftDir, rightDir, child, childNode);
      } else if (child.isLeft()) {
        var leftChild = left.child(lp++);
        if (leftChild instanceof RemoteDirectoryNode leftDir) updateNode(leftDir, child, childNode);
      } else {
        var rightChild = right.child(rp++);
        if (rightChild instanceof RemoteDirectoryNode rightDir) updateNode(rightDir, child, childNode);
      }
    }
  }

  private void updateNode(
      RemoteDirectoryNode node,
      RemoteFolderDiffModel model,
      FrontendTreeNode treeNode
  ) {
    if (model == null || model.children == null ||
        treeNode == null || !treeNode.isOpened()
    ) return;
    node.doOpen();

    if (treeNode.children.length != model.children.length) treeNode.updateWithModel(model);
    for (int i = 0; i < model.children.length; i++) {
      var child = model.child(i);
      var childNode = treeNode.child(i, child.path, child.isFile());
      if (node.child(i) instanceof RemoteDirectoryNode dirChildNode)
        updateNode(dirChildNode, child, childNode);
    }
  }

  private Pair<FileTreeNode, FileTreeNode> navigatedNodes(
      FileTreeNode left,
      FileTreeNode right,
      RemoteFolderDiffModel model,
      int[] path, int ind
  ) {
    if (ind == path.length) return Pair.of(left, right);
    var childModel = model.child(path[ind]);
    FileTreeNode leftChild = null, rightChild = null;
    boolean needUpdate = false;
    if (left instanceof RemoteDirectoryNode leftDir) {
      if (!leftDir.isOpened()) {
        leftDir.doOpen();
        needUpdate = true;
      }
      leftChild = leftDir.child(childModel.path, childModel.isFile());
    }
    if (right instanceof RemoteDirectoryNode rightDir) {
      if (!rightDir.isOpened()) {
        rightDir.doOpen();
        needUpdate = true;
      }
      rightChild = rightDir.child(childModel.path, childModel.isFile());
    }
    if (needUpdate) openFrontendNode(model);
    return navigatedNodes(leftChild, rightChild, childModel, path, ind + 1);
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
    int type = array.get(0);
    switch (type) {
      case DiffModelChannelUpdater.FRONTEND_MESSAGE -> update(jsResult);
      case DiffModelChannelUpdater.OPEN_FILE -> openFile(jsResult);
      case DiffModelChannelUpdater.APPLY_DIFF -> onDiffApplied(jsResult);
      case DiffModelChannelUpdater.APPLY_FILTERS -> onFiltersApplied(jsResult);
      case DiffModelChannelUpdater.REFRESH -> onRefresh(jsResult);
      case DiffModelChannelUpdater.NAVIGATE -> onNavigate(jsResult);
    }
    LoggingJs.trace("Got message "
        + DiffModelChannelUpdater.messageName(type)
        + " in " + Numbers.iRnd(Performance.now() - startTime) + "ms"
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
        LoggingJs.trace("Expanding dir " + node.name());
        var model = node.model();
        if (model == null) {
          LoggingJs.error("dir " + node.name() + "haven't model");
          return;
        }
        if (model.children == null) {
          LoggingJs.error("model " + node.name() + "haven't children");
          return;
        }

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
      public void updateNodes(RemoteDirectoryNode node, boolean open) {
        if (open) openFrontendNode(node.model());
        else closeFrontendNode(node.model());
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
      public RemoteFileTreeNode getNearestParent(RemoteFolderDiffModel node) {
        var current = left ? leftRoot : rightRoot;
        Deque<String> deque = collectDequePath(node);
        while (!deque.isEmpty()) {
          var path = deque.removeFirst();
          if (deque.isEmpty() && node.isFile()) {
            RemoteFileNode subNode = current.findSubFile(path);
            return subNode != null ? subNode : current;
          }
          RemoteDirectoryNode subNode = current.findSubDir(path);
          if (subNode == null) return current;
          else current = subNode;
        }
        return current;
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
        Deque<String> deque = collectDequePath(model);
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

  private static Deque<String> collectDequePath(RemoteFolderDiffModel model) {
    Deque<String> deque = new LinkedList<>();
    var curModel = model;
    while (curModel != null) {
      deque.addFirst(curModel.path);
      curModel = curModel.parent();
    }
    deque.removeFirst();
    return deque;
  }

  private void openFrontendNode(RemoteFolderDiffModel model) {
    LoggingJs.trace("RemoteFolderDiffWindow.openFrontendNode: " + model.path);
    var path = collectDequePath(model);
    var node = lastFrontendMessage.find(path);
    if (node == null) {
      LoggingJs.error("Can't find node: " + model.path);
      return;
    }
    if (node.children != null) {
      LoggingJs.error("Node children is not null for: " + model.path);
      return;
    }
    if (model.children == null) {
      LoggingJs.error("Model children is null for: " + model.path);
      return;
    }
    LoggingJs.trace("openFrontendNode: " + model.path);
    node.children = FrontendTreeNode.mkChildrenWithModel(model);
  }

  private void closeFrontendNode(RemoteFolderDiffModel model) {
    LoggingJs.trace("RemoteFolderDiffWindow.closeFrontendNode: " + model.path);
    var path = collectDequePath(model);
    var node = lastFrontendMessage.find(path);
    if (node == null) {
      LoggingJs.error("Can't find node: " + model.path);
      return;
    }
    node.children = null;
  }

  private static Supplier<RemoteFolderDiffModel> childModel(Supplier<RemoteFolderDiffModel> parent, int i) {
    return () -> parent.get().child(i);
  }

  private void newEditor(RemoteFileNode node, boolean left) {
    if (opener == null) {
      var window = new EditorWindow(windowManager, theme, fonts);
      window.onControllerEvent(this::onWindowEvent);
      addWindow(window, new JsEditorViewController0());
      openFileMap.put(keyCnt, (source, encoding)
          -> window.open(source, encoding, node.name()));
      sendOpenFile(node, left, keyCnt++);
      Consumer<Model> onDiffMade = (src) -> fileDiffMade(node.model(), left, src);
      window.setDiffMade(onDiffMade);
      window.maximize();
      window.setReadonly(left ? rootView.leftReadonly : rootView.rightReadonly);
      onWindowEvent(window);
    } else {
      var path = getFullPath(node, left);
      opener.openEditor(JSString.valueOf(path), left);
    }
  }

  private void newCodeDiff(RemoteFileNode node, RemoteFileNode opposite, boolean left) {
    if (opener == null) {
      var window = new FileDiffWindow(windowManager, theme, fonts, disableParser, false);
      window.canSelectFiles = false;
      window.onEvent = this::onWindowEvent;
      addWindow(window, new JsFileDiffViewController0(window));
      openFileMap.put(keyCnt, (source, encoding) ->
          window.open(source, encoding, node.name(), left));
      sendOpenFile(node, left, keyCnt++);
      openFileMap.put(keyCnt, (source, encoding) ->
          window.open(source, encoding, opposite.name(), !left));
      sendOpenFile(opposite, !left, keyCnt++);
      Consumer<Model> onLeftDiff = left
          ? src -> fileDiffMade(node.model(), true, src)
          : src -> fileDiffMade(opposite.model(), true, src);
      Consumer<Model> onRightDiff = !left
          ? src -> fileDiffMade(node.model(), false, src)
          : src -> fileDiffMade(opposite.model(), false, src);
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
    return node.getFullPath(left ? leftRootPath : rightRootPath);
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
    if (!isFiltered())
      lastFrontendMessage = FrontendMessage.mkFrontendMessage(leftRoot, rightRoot, rootModel, searchString);
    var result = FrontendMessage.serialize(lastFrontendMessage);
    result.push(DiffModelChannelUpdater.FRONTEND_MESSAGE_ARRAY);
    channel.sendMessage(result);
  }

  private void sendOpenFile(RemoteFileNode node, boolean left, int key) {
    var result = JsArray.create();
    var path = node.model().getPathFromRoot();
    var ints = new int[]{key, left ? 1 : 0};
    result.push(JsCast.jsInts(ints));
    result.push(JsCast.jsInts(path));
    result.push(DiffModelChannelUpdater.OPEN_FILE_ARRAY);
    channel.sendMessage(result);
  }

  private void askApplyDiff(FolderDiffModel model, boolean left) {
    if (dialogProvider != null) {
      RemoteFolderDiffModel remoteModel = (RemoteFolderDiffModel) model;
      String fromPath = remoteModel.getFullPath(left ? leftRoot.name() : rightRoot.name());
      String toPath = remoteModel.getFullPath(!left ? leftRoot.name() : rightRoot.name());
      fromPath = replaceSlashes(fromPath);
      toPath = replaceSlashes(toPath);
      FsDialogs.showDlg(dialogProvider, fromPath, toPath, remoteModel, left,
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
    LoggingJs.info("RemoteFolderDiffWindow.sendApplyDiff");
  }

  void fileDiffMade(FolderDiffModel dModel, boolean left, Model model) {
    int[] path = dModel.getPathFromRoot();
    var result = JsArray.create();
    result.set(0, JsCast.jsInts(path));
    result.set(1, JsCast.jsInts(left ? 0 : 1));
    result.set(2, JsCast.jsString(model.document.getChars()));
    result.set(3, JsCast.jsString(model.encoding()));
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
        leftRootPath,
        rightRootPath,
        searchString
    );
    FrontendState state = FrontendState.deserialize(serialized);
    LoggingJs.info("RemoteFolderDiffWindow.serializeFrontendState, state = " + state);
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

  static String replaceSlashes(String path) {
    int slInd = path.indexOf('/');
    if (slInd == -1) return path;
    int revSlInd = path.indexOf('\\');
    if (revSlInd == -1) return path;
    char firstDelim = slInd < revSlInd ? '/' : '\\';
    char delimToReplace = slInd < revSlInd ? '\\' : '/';
    return path.replace(delimToReplace, firstDelim);
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
    return rootView.right.isFocused() ? rootView.right : rootView.left;
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    rootView.setReadonly(leftReadonly, rightReadonly);
  }

  public void setDisableParser(boolean disableParser) {
    this.disableParser = disableParser;
  }

  public boolean canNavigateUp() {
    return canNavigate(true);
  }

  public boolean canNavigateDown() {
    return canNavigate(false);
  }

  private boolean canNavigate(boolean up) {
    if (lastFilters != null && lastFilters.length == 1 && lastFilters[0] == DiffTypes.DEFAULT) return false;
    var focused = getFocused();
    if (focused == null) return false;
    int selectedInd = focused.selectedIndex();
    if (selectedInd < 0) return false;
    var selectedNode = rootView.left.model()[selectedInd];
    if (selectedNode == null) selectedNode = rootView.right.model()[selectedInd];
    if (!(selectedNode instanceof RemoteFileTreeNode selectedRemoteModel)) return false;
    var model = selectedRemoteModel.model();
    int[] path = model.getPathFromRoot();
    if (path.length == 0) {
      if (!up) return rootModel.canNavigateDown(-1);
    } else {
      int[] filteredPath = filteredPath(rootModel, path);
      var models = rootModel.getModelsByPath(filteredPath);
      return up
          ? canNavigateUp(models, filteredPath, path.length - 1)
          : canNavigateDown(models, filteredPath, path.length - 1);
    }
    return false;
  }

  private boolean canNavigateDown(FolderDiffModel[] models, int[] path, int ind) {
    if (ind < 0) return false;
    var model = models[ind];
    int stIndex = path[ind];
    if (ind == path.length - 1 && models[ind].child(path[ind]).isDir()) stIndex--;
    return model.canNavigateDown(stIndex) || canNavigateDown(models, path, ind - 1);
  }

  private boolean canNavigateUp(FolderDiffModel[] models, int[] path, int ind) {
    if (ind < 0) return false;
    var model = models[ind];
    return model.canNavigateUp(path[ind]) || canNavigateUp(models, path, ind - 1);
  }

  public void navigateUp() {
    getNavigatedNode(true);
  }

  public void navigateDown() {
    getNavigatedNode(false);
  }

  private void getNavigatedNode(boolean up) {
    var focused = getFocused();
    if (focused == null) return;
    int selectedInd = focused.selectedIndex();
    if (selectedInd < 0) return;
    var selectedNode = rootView.left.model()[selectedInd];
    if (selectedNode == null || selectedNode.isEmpty()) selectedNode = rootView.right.model()[selectedInd];
    if (!(selectedNode instanceof RemoteFileTreeNode selectedRemoteModel)) return;
    LoggingJs.debug("RemoteFolderDiffWindow.getNavigatedNode"
        + ": up = " + up
        + ", left = " + (focused == rootView.left)
        + ", canNavigate = " + canNavigate(up)
    );
    var model = selectedRemoteModel.model();
    int[] path = model.getPathFromRoot();
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(path));
    jsArray.set(1, JsCast.jsInts(focused == rootView.left ? 1 : 0, up ? 0 : 1));
    jsArray.push(DiffModelChannelUpdater.NAVIGATE_ARRAY);
    channel.sendMessage(jsArray);
  }

  public void applyDiffFilter(int[] filters) {
    var focused = getFocused();
    this.lastFilters = filters;
    this.isLastLeftFocused = focused == rootView.left;
    this.lastSelected = focused == null || focused.selectedLine() == null || focused.selectedLine().isEmpty()
        ? null : ((RemoteFileTreeNode) focused.selectedLine()).model();
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, JsCast.jsInts(filters));
    jsArray.push(DiffModelChannelUpdater.APPLY_FILTERS_ARRAY);
    channel.sendMessage(jsArray);
  }

  public int[] getDiffFilter() {
    return lastFilters != null ? lastFilters : new int[0];
  }

  private boolean isFiltered() {
    return lastFilters != null && !(lastFilters.length == 0 || lastFilters.length == 4);
  }

  private int[] filteredPath(FolderDiffModel filtered, int[] path) {
    if (!isFiltered()) return path;
    return filtered.filteredPath(path);
  }

  public void refresh() {
    LoggingJs.trace("RemoteFolderDiffWindow.refresh");
    rootView.fireRefreshed();
    updatedRoots = false;
    var result = JsArray.create();
    result.push(DiffModelChannelUpdater.REFRESH_ARRAY);
    channel.sendMessage(result);
  }

  void fireControllerEvent(JsViewController source) {
    var list = controllerListeners.array();
    if (debug) LoggingJs.debug(
        "fireControllerEvent: " + list.length + " listeners");
    for (var listener: list) {
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
