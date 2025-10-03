package org.sudu.experiments.esm;

import org.sudu.experiments.WebWorkersPool;
import org.sudu.experiments.editor.EditorConst;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface EditArgs extends JSObject {
  @JSProperty
  JSString getContainerId();

  @JSProperty
  JsWorkerPool getWorkers();

  String workerUrlProperty = "workerUrl";

  @JSProperty
  JSString getWorkerUrl();

  String themeProperty = "theme";

  @JSProperty
  JSString getTheme();

  String readonlyProperty = "readonly";

  @JSProperty
  boolean getReadonly();

  String disableParser = "disableParser";

  @JSProperty
  boolean getDisableParser();

  String numThreadsProperty = "numThreads";

  @JSProperty
  double getNumThreads();

  String codiconUrlProp = "codiconUrl";

  @JSProperty
  JSString getCodiconUrl();

  default boolean hasCodiconUrl() {
    return JSObjects.hasProperty(this, codiconUrlProp);
  }

  default int numWorkerThreads() {
    return JSObjects.hasProperty(this, numThreadsProperty)
        ? (int) getNumThreads() : 2;
  }

  default boolean hasTheme() {
    return JSObjects.hasProperty(this, themeProperty);
  }

  default boolean hasReadonly() {
    return JSObjects.hasProperty(this, readonlyProperty);
  }

  default boolean hasDisableParser() {
    return JSObjects.hasProperty(this, disableParser);
  }

  default boolean getDisableParserOrDefault() {
    return hasDisableParser()
        ? getDisableParser()
        : EditorConst.DEFAULT_DISABLE_PARSER;
  }

  default JSString workerUrl() {
    return JSObjects.hasProperty(this, workerUrlProperty)
        ? getWorkerUrl() : JSString.valueOf("worker.js");
  }

  static WebWorkersPool getPool(EditArgs args) {
    JsWorkerPool pool = args.getWorkers();
    if (pool instanceof JsWorkerPool.Impl instance)
      return instance.workers;
    throw new IllegalArgumentException("workers pol has to be of type JsWorkerPoolImpl");
  }
}
