package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.DiffStatus;
import org.sudu.experiments.diff.folder.PropTypes;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.FileTreeNode;

import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryNode extends FileTreeNode {

  static final DirectoryNode[] dn0 = new DirectoryNode[0];
  static final FileNode[] fn0 = new FileNode[0];

  public final DirectoryHandle dir;
  public final Handler handler;

  private DirectoryNode[] folders = dn0;
  private FileNode[] files = fn0;

  public interface Handler {
    void openFile(FileNode file);

    void folderOpened(DirectoryNode node);

    void folderClosed(DirectoryNode node);

    void updateView(DirectoryNode node);

    default void applyFileIcon(FileTreeNode f, String fileName) {
      f.iconFile();
    }

    default Runnable open(FileNode file) {
      return () -> openFile(file);
    }
  }

  public DirectoryNode(DirectoryHandle dir, Handler handler) {
    this(dir, 0, handler);
  }

  public DirectoryNode(DirectoryHandle dir, int d, Handler handler) {
    super(dir.getName(), d);
    this.dir = dir;
    this.handler = handler;
    readOnClick();
    close();
  }

  public String toString() {
    return dir.toString();
  }

  public DirectoryNode[] folders() {
    return folders;
  }

  public FileNode[] files() {
    return files;
  }

  public DirectoryNode findSubDir(String what) {
    return FileTreeNode.bs(folders, what);
  }

  public FileNode findFile(String what) {
    return FileTreeNode.bs(files, what);
  }

  public void readOnClick() {
    onClick = this::readDirectory;
    onClickArrow = onClick;
    setContent(ch0);
  }

  public void closeOnClick() {
    onClick = this::closeFolder;
    onClickArrow = onClick;
  }

  private void closeFolder() {
    close();
    handler.folderClosed(this);
    folders = dn0;
    files = fn0;
  }

  private void readDirectory() {
    System.out.println("readDirectory: " + name());
    dir.read(new DirectoryHandle.Reader() {

      final ArrayList<DirectoryNode> dList = new ArrayList<>();
      final ArrayList<FileNode> fList = new ArrayList<>();

      @Override
      public void onDirectory(DirectoryHandle dir) {
        var d = new DirectoryNode(dir, depth + 1, handler);
        if (status != null && status.propagation == PropTypes.PROP_DOWN) {
          d.status = new DiffStatus(status);
          d.status.propagation = status.propagation;
          d.status.diffType = status.diffType;
        }
        dList.add(d);
      }

      @Override
      public void onFile(FileHandle file) {
        String fileName = file.getName();
        var f = new FileNode(fileName, depth + 1, file);
        handler.applyFileIcon(f, fileName);
        f.onDblClick = handler.open(f);
        if (status != null && status.propagation == PropTypes.PROP_DOWN) {
          f.status = new DiffStatus(status);
          f.status.propagation = status.propagation;
          f.status.diffType = status.diffType;
          f.status.rangeId = status.rangeId;
        }
        fList.add(f);
      }

      @Override
      public void onComplete() {
        if (!dList.isEmpty() || !fList.isEmpty()) {
          folders = dList.toArray(dn0);
          files = fList.toArray(fn0);
          Arrays.sort(folders, cmp);
          Arrays.sort(files, cmp);
          var children = ArrayOp.add(folders, files,
              new FileTreeNode[folders.length + files.length]);
          if (status.children != null) {
            for (int i = 0; i < children.length; i++) children[i].status = status.children[i];
          } else {
            status.children = new DiffStatus[children.length];
            for (int i = 0; i < children.length; i++) status.children[i] = children[i].status;
          }
          setContent(children);
        }
        open();
        handler.folderOpened(DirectoryNode.this);
      }
    });
  }
}
