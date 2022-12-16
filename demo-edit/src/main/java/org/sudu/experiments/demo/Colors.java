package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

interface Colors {

  V4f scrollBarBody1 = Color.Cvt.fromRGBA(50, 50, 50, 100);
  V4f scrollBarBody2 = Color.Cvt.fromRGBA(80, 80, 80, 200);
  V4f toolbarBg = Color.Cvt.fromRGBA(40, 40, 40, 200);
  V4f toolbarTextBg = Color.Cvt.fromRGBA(40, 40, 40, 128);
  V4f toolbarTextBg2 = Color.Cvt.fromRGBA(70, 70, 70, 200);

  Toolbar.ButtonColors toolbarText2 = new Toolbar.ButtonColors(
      new Color("#6897BB"), toolbarTextBg, toolbarTextBg2);
  Toolbar.ButtonColors toolbarText3 = new Toolbar.ButtonColors(
      new Color("#629755"), toolbarTextBg, toolbarTextBg2);

  static Toolbar.ButtonColors rngToolButton() {
    return new Toolbar.ButtonColors(
        Color.Cvt.fromHSV(Math.random(), 1, 1), toolbarTextBg, toolbarTextBg2);
  }

}
