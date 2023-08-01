package org.sudu.experiments.demo.wasm;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.js.Fetch;
import org.sudu.experiments.math.V2i;

public class WasmDemo extends Scene {

  public WasmDemo(SceneApi api) {
    super(api);
    Fetch.fetch(WasmTest.module)
        .then(Fetch.Response::arrayBuffer)
        .then(WasmTest::instantiate)
        .then(WasmTest::onLoad, WasmTest::onError);
  }

  @Override
  public void dispose() {}

  @Override
  public void paint() {}

  @Override
  public void onResize(V2i size, float dpr) {}

}
