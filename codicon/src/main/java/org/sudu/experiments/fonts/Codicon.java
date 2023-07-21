package org.sudu.experiments.fonts;

import org.sudu.experiments.math.ArrayOp;

public interface Codicon {
  String typeface = "codicon";
  String folder = "fonts/";

  String file = typeface + ".ttf";

  static FontResources fontResource() {
    return new FontResources(Codicon.class, folder, file);
  }

  static FontConfigJs[] webConfig() {
    return ArrayOp.array(
        new FontConfigJs(typeface, folder.concat(file),
            FontDesk.NORMAL, FontDesk.WEIGHT_REGULAR));
  }
}
