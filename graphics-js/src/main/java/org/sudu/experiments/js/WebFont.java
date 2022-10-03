package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;

// TODO: consider using FontFace API to load fonts instead
// https://developer.mozilla.org/en-US/docs/Web/API/FontFace
// rationale: FontFace is a standard web api and does not require an external library

@SuppressWarnings("SpellCheckingInspection") // fontactive
public class WebFont {
  public static void loadGoogleFont(
      // This event is triggered when the fonts have rendered.
      CallBack active,
      // This event is triggered when the browser does not support linked fonts
      // or if none of the fonts could be loaded.
      CallBack inactive,
      // This event is triggered once for each font that renders.
      FontCallBack fontactive,
      // fonts to load
      JSArray<JSString> families
  ) {
    load(makeGoogleConfig(active, inactive, fontactive, families));
  }

  @JSBody(params = {"config"}, script = "WebFont.load(config);")
  static native void load(JSObject config);

  @JSBody(
      params = { "active", "inactive", "fontactive", "families" },
      script = "return { google: { families: families }, classes: false, "
          + "active: active, inactive: inactive, fontactive: fontactive }"
  )
  static native JSObject makeGoogleConfig(
      CallBack active, CallBack inactive,
      FontCallBack fontactive, JSArray<JSString> families
  );

  // examples:
  //   "JetBrains Mono:400,800i,500,200i"
  //   "Droid Serif:700i"
  @JSBody(params = {"f1"}, script = "return [f1];")
  public static native JSArray<JSString> makeFontList(String font);

  @JSBody(params = {"f1", "f2"}, script = "return [f1, f2];")
  public static native JSArray<JSString> makeFontList(String font1, String font2);

  @JSFunctor public interface CallBack extends JSObject { void f(); }
  @JSFunctor public interface FontCallBack extends JSObject { void f(String familyName, String fvd); }
}
