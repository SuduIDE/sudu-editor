package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.FileTreeNode;

import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryNode extends FileTreeNode {

  final DirectoryHandle dir;
  final Handler handler;

  public interface Handler {
    void openFile(FileHandle file, FileTreeNode node);
    void folderOpened(DirectoryNode node);
    void folderClosed(DirectoryNode node);

    default Runnable open(FileHandle file, FileTreeNode node) {
      return () -> openFile(file, node);
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
    arrowRight();
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
  }

  private void readDirectory() {
    System.out.println("readDirectory: " + name());
    dir.read(new DirectoryHandle.Reader() {

      final ArrayList<FileTreeNode> dList = new ArrayList<>();
      final ArrayList<FileTreeNode> fList = new ArrayList<>();

      @Override
      public void onDirectory(DirectoryHandle dir) {
        var d = new DirectoryNode(dir, depth + 1, handler);
        dList.add(d);
      }

      @Override
      public void onFile(FileHandle file) {
        var f = new FileTreeNode(file.getName(), depth + 1);
        f.onDblClick = handler.open(file, f);
        fList.add(f);
      }

      @Override
      public void onComplete() {
        if (!dList.isEmpty() || !fList.isEmpty()) {
          FileTreeNode[] folders = dList.toArray(ch0);
          FileTreeNode[] files = fList.toArray(ch0);
          Arrays.sort(folders, cmp);
          Arrays.sort(files, cmp);
          var nodes = ArrayOp.add(folders, files);
          setContent(nodes);
        }
        open();
        handler.folderOpened(DirectoryNode.this);
      }
    });
  }
}
