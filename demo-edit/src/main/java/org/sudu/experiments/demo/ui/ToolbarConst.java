package org.sudu.experiments.demo.ui;

public interface ToolbarConst {

  Toolbar.HoverCallback fireOnHover = (mouse, index, item) -> {
    item.action().run();
    item.setHover(true);
  };
}
