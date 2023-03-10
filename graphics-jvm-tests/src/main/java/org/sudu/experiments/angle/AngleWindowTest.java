package org.sudu.experiments.angle;

import org.sudu.experiments.Application;
import org.sudu.experiments.JetBrainsMono;
import org.sudu.experiments.tests.WindowSizeTestScene;
import org.sudu.experiments.win32.Helper;

public class AngleWindowTest {
  public static void main(String[] args) throws InterruptedException {
    Helper.loadDlls();
    Application.run(WindowSizeTestScene::new, JetBrainsMono.all());
  }
}