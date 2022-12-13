package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public class EditorColorScheme {
  public final CodeElementColor[] codeColors = CodeColors.toArray();
  public final LineNumbersColors lineNumbersColors = LineNumbersColors.ideaColorScheme();
  public final Color codeLineTailColor = Colors.editBgColor;
}
