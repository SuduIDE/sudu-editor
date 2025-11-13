package org.sudu.experiments.angle;

import org.sudu.experiments.*;
import org.sudu.experiments.win32.Win32Time;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class NoWindow implements Window {
  Win32Time time = new Win32Time();
  boolean repaint;
  List<Runnable> tasks = new ArrayList<>();

  @Override
  public void setTitle(String title) {}

  @Override
  public void setCursor(String cursor) {}

  @Override
  public double timeNow() {
    return time.now();
  }

  @Override
  public void repaint() {
    repaint = true;
  }

  @Override
  public boolean hasFocus() {
    return true;
  }

  @Override
  public boolean addChild(String title, Function<SceneApi, Scene> sf) {
    return false;
  }

  @Override
  public Host getHost() {
    return Host.Direct2D;
  }

  @Override
  public void showDirectoryPicker(Consumer<DirectoryHandle> onResult) {}

  @Override
  public void showOpenFilePicker(Consumer<FileHandle> onResult) {}

  @Override
  public WorkerJobExecutor worker() {
    return null;
  }

  @Override
  public void runLater(Runnable r) {
    tasks.add(r);
  }

  @Override
  public void readClipboardText(Consumer<String> success, Consumer<Throwable> onError) {
    onError.accept(new Throwable("no window"));
  }

  @Override
  public void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError) {
    onError.accept(new Throwable("no window"));
  }
}
