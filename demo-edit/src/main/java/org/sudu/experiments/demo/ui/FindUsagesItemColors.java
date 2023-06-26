package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.V4f;

public final class FindUsagesItemColors {
  final V4f fileColor;
  final V4f lineColor;
  final V4f contentColor;
  final V4f bgColor;
  final V4f bgHighlight;

  public FindUsagesItemColors(V4f fileColor, V4f lineColor, V4f contentColor, V4f bgColor, V4f bgHighlight) {
    this.fileColor = fileColor;
    this.lineColor = lineColor;
    this.contentColor = contentColor;
    this.bgColor = bgColor;
    this.bgHighlight = bgHighlight;
  }
}
