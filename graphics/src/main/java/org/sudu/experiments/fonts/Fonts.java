package org.sudu.experiments.fonts;

import org.sudu.experiments.math.ArrayOp;

public interface Fonts {
  String Helvetica = "Helvetica";
  String Verdana = "Verdana";
  String CourierNew = "Courier New";
  String SegoeUI = "Segoe UI";
  String Consolas = "Consolas";
  String JetBrainsMono = "JetBrains Mono";
  String codicon = "codicon";

  static String[] editorFonts(boolean withJB) {
    return withJB
            ? ArrayOp.array(Consolas, SegoeUI, Verdana, JetBrainsMono)
            : ArrayOp.array(Consolas, SegoeUI, Verdana);
  }
}
