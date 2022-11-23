package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

public abstract class Scene implements Disposable {

  protected final SceneApi api;

  public Scene(SceneApi api) {
    this.api = api;
  }

  // if scene update returns true it means
  // there is some animation going, and we need to re-paint
  //   timestamp - time since app start, in seconds
  public abstract boolean update(double timestamp);

  public abstract void paint();

  public abstract void onResize(V2i size);
}
