package org.sudu.experiments.ui;

public interface ToolbarConst {

  Toolbar.HoverCallback fireOnHover = (mouse, index, item) -> {
    item.action().run();
  };
}
