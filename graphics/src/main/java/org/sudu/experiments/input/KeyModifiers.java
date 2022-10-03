package org.sudu.experiments.input;

public class KeyModifiers {
  public final boolean shift, ctrl, alt, meta;

  public KeyModifiers(boolean _ctrl, boolean _alt, boolean _shift, boolean _meta) {
    shift = _shift;
    ctrl = _ctrl;
    alt = _alt;
    meta = _meta;
  }

  public boolean shiftOnly() {
    return shift && !ctrl && !alt && !meta;
  }

  public boolean controlOnly() {
    return ctrl && !shift && !alt && !meta;
  }
}
