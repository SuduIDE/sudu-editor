package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TestWalker implements DirectoryHandle.Reader {
  final Consumer<Object[]> result;
  final Thread currentThread = Thread.currentThread();
  final ArrayList<Object> list = new ArrayList<>();
  int jobs = 1, f = 0;

  public TestWalker(DirectoryHandle dir, Consumer<Object[]> result) {
    this.result = result;
    list.add(dir.toString());
    list.add(dir);
    System.out.println("Thread.currentThread() = " + currentThread);
  }

  @Override
  public void onDirectory(DirectoryHandle dir) {
    if (currentThread != Thread.currentThread())
      throw new RuntimeException();

    list.add(dir.toString());
    list.add(dir);
    System.out.println("dir[" + f++ + "] = " + dir + ", list.size = " + list.size());

    jobs++;
    dir.read(this);
  }


  @Override
  public void onFile(FileHandle file) {
    if (currentThread != Thread.currentThread())
      throw new RuntimeException();

    list.add(file.toString());
    list.add(file);
    System.out.println("file[" + f++ + "] = " + file + ", list.size = " + list.size());
  }

  @Override
  public void onComplete() {
    if (currentThread != Thread.currentThread())
      throw new RuntimeException();

    if (--jobs == 0) {
      System.out.println("asyncWithDir complete, size = " + list.size());
      ArrayOp.sendArrayList(list, result);
    }
  }
}
