package org.sudu.experiments.input;

import org.sudu.experiments.math.V2i;

public class MouseEvent extends KeyModifiers {
  public final V2i position;
  public final V2i resolution;

  public MouseEvent(
      V2i position, V2i resolution,
      boolean ctrl, boolean alt, boolean shift, boolean meta
  ) {
    super(ctrl, alt, shift, meta);
    this.position = position;
    this.resolution = resolution;
  }
}
